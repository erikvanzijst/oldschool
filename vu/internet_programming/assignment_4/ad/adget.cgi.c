#include "adserver.h"
#include "adserver_util.h"
#include "../bank/hosts.h"
#include "cgi_util.h"
#include "fallback_jpg.h"
#include <unistd.h>


void print_fallback(void);


int main(int argc, char *argv[])
{
  CLIENT *clnt;
  struct ad  *ad;
  filename_t filename = 0;
  char mimetype[100];
  char *last_dot;
  char *ext = 0;
  char **cgivars;
  int retval = 0;
  unsigned int pos;
  int byte;
  int i;

  // First, get the CGI variables into a list of strings   
  cgivars= getcgivars() ;

  for (i=0; cgivars[i]; i+= 2) // look for the ad variable
  {
    if (strcasecmp(cgivars[i], "ad") == 0) filename = cgivars[i+1];
  }
  if (filename == 0) // no filename provided
  {
    filename = strdup("");
    if (!filename)
    {
      print_fallback();
      fprintf(stderr, "Failed to initialize filename\n");
      exit(1);
    }
  }

  // set up RPC using TCP
  clnt = clnt_create (AD_SERVER_ADDR, ADSERVER, ADSERVER_VERS_1, "tcp");
  if (clnt == NULL) // set up failed, terminate
  {
    print_fallback();
    clnt_pcreateerror (AD_SERVER_ADDR);
    exit (1);
  }

  // request an ad
  ad = adserver_get_named_ad_1(&filename, clnt);
  if (ad == (struct ad *) NULL) // request failed, terminate
  {
    print_fallback();
    clnt_perror (clnt, "call failed");
    exit (1);
  }
  else // request succeeded
  {
    if (ad->data.data_len > 0) // non-zero ad
    {
      if (ad->filename) // filename available
      {
        last_dot = strrchr(ad->filename, '.');
        if (last_dot) ext = &last_dot[1];
      }
      if (ext) // extension available
      {
        if (strcasecmp(ext, "gif") == 0) // GIF
        {
          strcpy(mimetype, "image/gif");
        }
        else if (strcasecmp(ext, "jpg") == 0 || strcasecmp(ext, "jpeg") == 0) // JPEG
        {
          strcpy(mimetype, "image/jpeg");
        }
        else // unknown type
        {
          strcpy(mimetype, "image/unknown");
        }
      }
      else // unknown type
      {
        strcpy(mimetype, "image/unknown");
      }
      fprintf(stdout, "Content-type: %s\n\n", mimetype);
      for (pos = 0; pos < ad->data.data_len && !retval; pos++)
      {
        byte = fputc(ad->data.data_val[pos], stdout);
        if (byte == EOF)
        {
          perror("adget-cgi");
          retval = -1;
        }
      }
    }
    else // show built-int fallback ad
    {
      print_fallback();
    }
    ad_free(ad);
  }
  // clean up the client structure
  clnt_destroy (clnt);

  // Free anything that needs to be freed 
  for (i=0; cgivars[i]; i++) free(cgivars[i]) ;
  free(cgivars) ;

  return retval;
}


void print_fallback(void)
{
  fprintf(stdout, "Content-type: image/jpeg\n\n");
  // do not check return value, if it fails we are not going to do anything about it
  fwrite(fallback_jpg, sizeof(unsigned char), fallback_jpg_len, stdout); 
}
