#include "bank_client.h"
#include "hosts.h"


int main(int argc, char *argv[])
{
  int source_account;
  int destination_account;
  float amount;
  int certificate;
  int valid = -1;
  char *server_addr = 0;
  int server_port;
  char *password = 0;

  if (argc == 6 || argc == 8)
  {
    source_account = atoi(argv[1]);
    destination_account = atoi(argv[2]);
    amount = atof(argv[3]);
    certificate = atoi(argv[4]);
    password = argv[5];
    if (argc == 8) // server address and port provided
    {
      server_addr = argv[6];
      server_port = atoi(argv[7]);
    }
    else // no server address and port provided, use defaults
    {
      server_addr = BANK_SERVER_ADDR;
      server_port = BANK_SERVER_PORT;
    }
    valid = bank_check(server_addr, server_port, source_account, destination_account, amount, certificate, password);
    if (valid == 0) fprintf(stdout, "Certificate valid!\n");
    else fprintf(stderr, "Certificate invalid!\n");
  }
  else
  {
    fprintf(stdout, "Usage: %s <source_account> <destination_account> <amount> <certificate> <password> [<server addr> <server port>]\n", argv[0]);
  }
  
  return valid;
}

 
