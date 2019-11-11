#include "adserver.h"
#include "adserver_util.h"
#include "../bank/hosts.h"
#include "../bank/bank_client.h"


int main(int argc, char *argv[])
{
  CLIENT *clnt;
  pricelist_t *pricelist;
  char *adserver_get_pricelist_1_arg;
  int *purchase_retval;
  struct purchase purchase;
  struct ad ad_buffer;
  int max_units;
  float budget;
  int retval = 0;
  int i;
  int destination_account;
  const int min_args = 4;

  // check if a minimum number of arguments is available
  if (argc >= min_args)
  {
    // set the budget
    budget = atof(argv[1]);
    // initialize the purchase structure
    memset(&purchase, 0, sizeof(purchase));
    // set the source account
    purchase.source_account = atoi(argv[2]);
    // set the account of the ad server
    destination_account = atoi(argv[3]);
    // set up RPC
    clnt = clnt_create (AD_SERVER_ADDR, ADSERVER, ADSERVER_VERS_1, "tcp");
    if (clnt == NULL) // RPC setup failed, we give up
    {
      clnt_pcreateerror (AD_SERVER_ADDR);
      exit (1);
    }

    // request a pricelist from the ad server
    pricelist = adserver_get_pricelist_1((void*)&adserver_get_pricelist_1_arg, clnt);
    if (pricelist == (pricelist_t *) NULL) // pricelist request failed
    {
      clnt_perror (clnt, "call failed");
      exit (1); // without a pricelist is is pointless to continue
    }
    else // pricelist request
    {
      // calculate maximum number of units within the budget
      max_units = pricelist_get_units(pricelist, budget);
    }

    // verify that we can order at least one ad
    if (max_units > 0)
    {
      fprintf(stdout, "%d ad%s within the budget of %f totalling %f\n", max_units, max_units == 1 ? "" : "s", budget, pricelist_get_amount(pricelist, max_units));
      // initialize the ad buffer
      memset(&ad_buffer, 0, sizeof(ad_buffer));
      if (max_units <= argc - min_args) // enough ads provided
      {
      	// read ads from disk and ad them to the purchase
        for (i = 0; i < max_units && i + min_args < argc && !retval; i++)
        {
          // use the filenames provided on the command line
          ad_buffer.filename = argv[i+min_args];
          // read ad
          retval = ad_read(&ad_buffer);
          if (!retval)
          {
            // ad the add to the purchase
            retval = purchase_add_ad(&purchase, &ad_buffer);
            if (retval)
            {
              fprintf(stderr, "int main(int argc, char *argv[]): Failed to add ad to purchase list\n");
            }
          }
          else // ad_read failed
          {
            fprintf(stderr, "int main(int argc, char *argv[]): Failed to read ad from disk\n");
          }
        }
        if (!retval) // if the read operation succeeded
        {
          fprintf(stderr, "Purchasing %d ad%s\n", purchase.ads.ads_len, purchase.ads.ads_len == 1 ? "" : "s");
          if (purchase.ads.ads_len > 0) // ads to buy
          {
            // send a payment request to the bank
            purchase.certificate = bank_pay(BANK_SERVER_ADDR, BANK_SERVER_PORT, purchase.source_account, destination_account, pricelist_get_amount(pricelist, purchase.ads.ads_len));
            if (purchase.certificate >= 0) // transaction ok
            {
              // send the certificate, the source account number and the ads as a purchase to the ad server
              purchase_retval = adserver_purchase_ads_1(&purchase, clnt);
              if (purchase_retval == (int *) NULL)
              {
                clnt_perror (clnt, "call failed");
                exit (1);
              }
              else if (!(*purchase_retval)) // purchase approved
              {
                fprintf(stdout, "Purchase completed\n");
              }
              else // purchase rejected
              {
                fprintf(stderr, "Purchase rejected\n");
                retval = -1;
              }
            }
            else // transaction failed
            {
              fprintf(stderr, "Failed to transfer money\n");
              retval = -1;
            }
          }
        }
      }
      else // not enough ads provided
      {
        fprintf(stdout, "Please provide more ads (%d ad%s provided, %d ad%s needed)\n", argc - min_args, (argc - min_args) == 1 ? "" : "s", max_units, max_units == 1 ? "" : "s");
        retval = -1;
      }
    }
    else // cannot afford any ads
    {
      fprintf(stdout, "Budget does not allow the purchase of any ads\n");
      retval = -1;
    }
    clnt_destroy (clnt);
  }
  else  // not enough arguments provided
  {
    fprintf(stdout, "Usage: %s <budget> <source account> <destination account> [ad]...\n", argv[0]);
  }

  return retval;
}

