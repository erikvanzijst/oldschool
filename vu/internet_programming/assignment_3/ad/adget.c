#include "adserver.h"
#include "adserver_util.h"
#include "../bank/hosts.h"
#include <unistd.h>


int main(int argc, char *argv[])
{
  CLIENT *clnt;
  struct ad  *ad;
  char *adserver_get_ad_1_arg;
  char filename_buffer[FILENAME_LEN_MAX] = {0};
  int retval = 0;

  if (argc == 2) // enough arguments provided
  {
    // set up RPC using TCP
    clnt = clnt_create (AD_SERVER_ADDR, ADSERVER, ADSERVER_VERS_1, "tcp");
    if (clnt == NULL) // set up failed, terminate
    {
      clnt_pcreateerror (AD_SERVER_ADDR);
      exit (1);
    }

    // request an ad
    ad = adserver_get_ad_1((void*)&adserver_get_ad_1_arg, clnt);
    if (ad == (struct ad *) NULL) // request failed, terminate
    {
      clnt_perror (clnt, "call failed");
      exit (1);
    }
    else // request succeeded
    {
      // remove the original filename
      if (ad->filename) free(ad->filename);
      // generate a new temporary filname based on process id
      snprintf(filename_buffer, FILENAME_LEN_MAX - 1, "/tmp/%d.ad", (int) getpid());
      // set the new filename
      ad->filename = strdup(filename_buffer);
      if (ad->filename) // new filename set
      {
        retval = ad_write(ad); // write the ad to disk
        if (!retval) // write completed
        {
          // start the viewer with the ad as argument
          retval = execlp(argv[1], argv[1], ad->filename, 0);
          if (retval == -1)
          {
            perror("Error starting viewer");
          }
        }
        else // failed to save ad to disk
        {
          fprintf(stderr, "int main(int argc, char *argv[]): Failed to save ad to temporary file\n");
        }
      }
      else // failed to generate new filename
      {
        fprintf(stderr, "int main(int argc, char *argv[]): Failed to generate new filename\n");
        retval = -1;
      }
    }
    // clean up the client structure
    clnt_destroy (clnt);
  }
  else // not enough arguments, display usage
  {
    fprintf(stdout, "Usage: %s <viewer>\n", argv[0]);
  }

  return retval;
}


