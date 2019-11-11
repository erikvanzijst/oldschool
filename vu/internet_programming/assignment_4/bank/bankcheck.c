#include "bank_client.h"
#include "hosts.h"


int main(int argc, char *argv[])
{
  int source_account;
  int destination_account;
  float amount;
  int certificate;
  int valid = -1;

  if (argc == 5)
  {
    source_account = atoi(argv[1]);
    destination_account = atoi(argv[2]);
    amount = atof(argv[3]);
    certificate = atoi(argv[4]);
    valid = bank_check(BANK_SERVER_ADDR, BANK_SERVER_PORT, source_account, destination_account, amount, certificate);
    if (valid == 0) fprintf(stdout, "Certificate valid!\n");
    else fprintf(stderr, "Certificate invalid!\n");
  }
  else
  {
    fprintf(stdout, "Usage: %s <source_account> <destination_account> <amount> <certificate>\n", argv[0]);
  }
  
  return valid;
}

 
