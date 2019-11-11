#include "bank_client.h"
#include "hosts.h"


int main(int argc, char *argv[])
{
  int source_account;
  int destination_account;
  float amount;
  int certificate = -1;

  if (argc == 4)
  {
    source_account = atoi(argv[1]);
    destination_account = atoi(argv[2]);
    amount = atof(argv[3]);
    certificate = bank_pay(BANK_SERVER_ADDR, BANK_SERVER_PORT, source_account, destination_account, amount);
    if (certificate >= 0) fprintf(stdout, "Certificate received: %10d\n", certificate);
    else fprintf(stderr, "Error transferring money\n");
  }
  else
  {
    fprintf(stdout, "Usage: %s <source_account> <destination_account> <amount>\n", argv[0]);
  }
  
  return certificate;
}

 
