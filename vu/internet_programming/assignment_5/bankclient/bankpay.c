#include "bank_client.h"
#include "hosts.h"


int main(int argc, char *argv[])
{
  int source_account;
  int destination_account;
  float amount;
  int certificate = -1;
  char *server_addr = 0;
  int server_port;
  char *password = 0;

  if (argc == 5 || argc == 7)
  {
    source_account = atoi(argv[1]);
    destination_account = atoi(argv[2]);
    amount = atof(argv[3]);
    password = argv[4];
    if (argc >= 7) // server address and port provided
    {
      server_addr = argv[5];
      server_port = atoi(argv[6]);
    }
    else // no server address and port provided, use defaults
    {
      server_addr = BANK_SERVER_ADDR;
      server_port = BANK_SERVER_PORT;
    }
    certificate = bank_pay(server_addr, server_port, source_account, destination_account, amount, password);
    if (certificate >= 0) fprintf(stdout, "Certificate received: %d\n", certificate);
    else fprintf(stderr, "Error transferring money\n");
  }
  else
  {
    fprintf(stdout, "Usage: %s <source_account> <destination_account> <amount> <password> [<server addr> <server port>]\n", argv[0]);
  }
  
  return certificate;
}

 
