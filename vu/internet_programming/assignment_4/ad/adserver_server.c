#include <sys/types.h>
#include <dirent.h>

#include "adserver.h"
#include "adserver_util.h"
#include "../bank/hosts.h"
#include "../bank/bank_client.h"


pricelist_t *adserver_get_pricelist_1_svc(void *argp, struct svc_req *rqstp)
{
  static pricelist_t pricelist = {0};
  int retval = 0;
  unsigned int i;

  // read prices from the pricelist (the pricelist file is specified on
  // startup)
  pricelist_clear(&pricelist);
  retval = pricelist_read_prices(&pricelist);
  if (retval)
  {
    fprintf(stderr, "pricelist_t *adserver_get_pricelist_1_svc(void *argp, struct svc_req *rqstp): Failed to read prices\n");
  }
  // display the prices for debugging purposes
  for (i = 0; i < pricelist.pricelist_t_len; i++)
  {
    fprintf(stdout, "%d unit%s: %f\n", pricelist.pricelist_t_val[i].units, pricelist.pricelist_t_val[i].units == 1 ? "" : "s", pricelist.pricelist_t_val[i].amount);
  }

  return &pricelist;
}


int *adserver_purchase_ads_1_svc(struct purchase *argp, struct svc_req *rqstp)
{
  static int retval;
  static int sequence = 0;
  static time_t systemtime = 0;
  time_t now;
  pricelist_t pricelist= {0};
  char filename_buffer[FILENAME_LEN_MAX];
  char *last_dot;
  char *ext;
  void *result;
  unsigned int i;

  // print the number of ads received
  fprintf(stdout, "%d ad%s received\n", argp->ads.ads_len, argp->ads.ads_len == 1 ? "" : "s");
  // re-read the prices from the config file, they may have been changed
  retval = pricelist_read_prices(&pricelist);
  if (!retval)
  {
    // our own bank account number has been specified on startup, the client
    // is only allowed to specify the source account and the certificate
    // for security the amount is calculated from the number of ads provided
    retval = bank_check(BANK_SERVER_ADDR, BANK_SERVER_PORT, argp->source_account, adserver_account, pricelist_get_amount(&pricelist, argp->ads.ads_len), argp->certificate);
    if (!retval) // verification successful, proceed
    {
      // save all ads in the designated subdirectory
      for (i = 0; i < argp->ads.ads_len && !retval; i++)
      {
        if (argp->ads.ads_val[i].filename) // the ad has a name
        {
          // find the extension of the file
          last_dot = strrchr(argp->ads.ads_val[i].filename, '.');
          if (last_dot) ext = &last_dot[1]; // dot found, set ext pointer
        }
        // generate a new filename based on a timestamp and use the extension provided (if any)
        now = time(0);
        if (now != systemtime)
        {
          systemtime = now;
          sequence = 0;
        }
        else sequence++;
        snprintf(filename_buffer, FILENAME_LEN_MAX - 1, "%s/%ld-%d%s%s", ADSERVER_UTIL_UPLOAD_DIR, (long) now, sequence, ext ? "." : "", ext ? ext : "");
        // allocate additional space for the new filename
        result = realloc(argp->ads.ads_val[i].filename, strlen(filename_buffer) + 1);
        if (result) // allocation succeeded
        {
          argp->ads.ads_val[i].filename = result;
          memcpy(argp->ads.ads_val[i].filename, filename_buffer, strlen(filename_buffer) + 1);
          // write the ad with the new filename
          retval = ad_write(&argp->ads.ads_val[i]);
          if (retval)
          {
            fprintf(stderr, "int *adserver_purchase_ads_1_svc(struct purchase *argp, struct svc_req *rqstp): Failed to save ad\n");
          }
        }
        else // memory allocation failed
        {
          perror("int *adserver_purchase_ads_1_svc(struct purchase *argp, struct svc_req *rqstp)");
          retval = -1;
        }
      } // done saving all ads
    }
    else // bank check failed
    {
      fprintf(stderr, "int *adserver_purchase_ads_1_svc(struct purchase *argp, struct svc_req *rqstp): Failed to verify payment\n");
    }
  }
  else // failed to read pricelist
  {
    fprintf(stderr, "int *adserver_purchase_ads_1_svc(struct purchase *argp, struct svc_req *rqstp): Failed to read pricelist\n");
  }

  return &retval;
}


struct ad *adserver_get_ad_1_svc(void *argp, struct svc_req *rqstp)
{
  static struct ad ad = {0};
  static size_t last_served = 0;
  int retval = 0;
  DIR *upload_dir;
  struct dirent *buffer;
  char **names = 0;
  size_t name_count = 0;
  size_t i;
  void *result;
  char filename_buffer[FILENAME_LEN_MAX] = {0};

  // open the upload directory and check for ads
  upload_dir = opendir(ADSERVER_UTIL_UPLOAD_DIR);
  if (upload_dir) // upload dir succesfully opened
  {
    // iterate trough the directory entries skipping '.' and '..'
    // and build a list of valid names
    while ((buffer = readdir(upload_dir)) && !retval)
    {
      if (strcasecmp(buffer->d_name, ".") && strcasecmp(buffer->d_name, "..")) // we skip '.' and '..'
      {
        // add a name to the list
        result = realloc(names, (name_count + 1) * sizeof(char *));
        if (result) // allocation successful
        {
          names = result;
          // allocate the name itself
          names[name_count] = malloc(strlen(buffer->d_name) + 1);
          if (names[name_count]) // allocation succeeded
          {
            memcpy(names[name_count], buffer->d_name, strlen(buffer->d_name) + 1);
            name_count++;
          }
          else // malloc failed
          {
            perror("struct ad *adserver_get_ad_1_svc(void *argp, struct svc_req *rqstp)");
            retval = -1;
          }
        }
        else // realloc failed
        {
          perror("struct ad *adserver_get_ad_1_svc(void *argp, struct svc_req *rqstp)");
          retval = -1;
        }
      }
    } // iteratoin completed

    if (!retval) // no previous errors
    {
      if (name_count > 0) // files are present
      {
        last_served = (last_served + 1) % name_count;
        // prepend the upload dir path to the filename
        snprintf(filename_buffer, FILENAME_LEN_MAX - 1, "%s/%s", ADSERVER_UTIL_UPLOAD_DIR, names[last_served]);
        // set the new filename in the ad
        result = realloc(ad.filename, strlen(filename_buffer) + 1);
        if (result) // allocation successful
        {
          ad.filename = result;
          strcpy(ad.filename, filename_buffer);
          // read the ad from disk
          retval = ad_read(&ad);
          if (!retval) // read complete
          {
            fprintf(stdout, "Serving %s (%u bytes)\n", ad.filename, ad.data.data_len);
          }
          else // ad read failed
          {
            fprintf(stderr, "struct ad *adserver_get_ad_1_svc(void *argp, struct svc_req *rqstp): Failed to read image\n");
          }
        }
        else // realloc failed
        {
          fprintf(stderr, "struct ad *adserver_get_ad_1_svc(void *argp, struct svc_req *rqstp): Failed to set filename\n");
        }
      }
      else // no ads
      {
        fprintf(stderr, "struct ad *adserver_get_ad_1_svc(void *argp, struct svc_req *rqstp): No ads available\n");
      }
    }
    // free the list of names
    for (i = 0; i < name_count; i++) if (names[i]) free(names[i]);
    if (names) free(names);
    // close the upload directory
    closedir(upload_dir);
  }
  else // failed to open upload dir
  {
    perror("struct ad *adserver_get_ad_1_svc(void *argp, struct svc_req *rqstp)");
  }

  return &ad;
}


struct ad *adserver_get_named_ad_1_svc(filename_t *argp, struct svc_req *rqstp)
{
  static struct ad ad = {0};
  int retval = 0;
  void *result;
  char filename_buffer[FILENAME_LEN_MAX] = {0};

  if (argp && *argp && strlen(*argp) > 0)
  {
    fprintf(stdout, "Requested %s\n", *argp ? *argp : "");
    // prepend the upload dir path to the filename
    snprintf(filename_buffer, FILENAME_LEN_MAX - 1, "%s/%s", ADSERVER_UTIL_UPLOAD_DIR, (char *) *argp ? *argp : "");
    // set the new filename in the ad
    result = realloc(ad.filename, strlen(filename_buffer) + 1);
    if (result) // allocation successful
    {
      ad.filename = result;
      strcpy(ad.filename, filename_buffer);
      // read the ad from disk
      retval = ad_read(&ad);
      if (!retval) // read complete
      {
        fprintf(stdout, "Serving %s (%u bytes)\n", ad.filename, ad.data.data_len);
      }
      else // ad read failed
      {
        // probably the file does not exist, try a random ad using round-robin
        fprintf(stderr, "struct ad *adserver_get_ad_1_svc(void *argp, struct svc_req *rqstp): Failed to read image, selecting one at random\n");
        return adserver_get_ad_1_svc(0, rqstp);
      }
    }
    else // realloc failed
    {
      fprintf(stderr, "struct ad *adserver_get_ad_1_svc(void *argp, struct svc_req *rqstp): Failed to set filename\n");
    }
  }
  else // no filename provided
  {
    // try a random ad using round-robin
    return adserver_get_ad_1_svc(0, rqstp);
  }

  return &ad;
}

