#include <pthread.h>
#include <signal.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include "bank.h"
#include "network.h"
#include "hosts.h"
#include "protocol.h"
#include "bank_account.h"


struct bank bank;
char *account_file_name;

void *client_handler_impl(void *client_fd_ptr);
int read_and_initialize_accounts(struct bank *bank, char *account_file_name);
void signal_usr1_handler(int arg);

int main(int argc, char *argv[])
{
  int retval = 0;
  int server_fd = -1;
  int client_fd = -1;
  int *client_fd_ptr = 0;
  pthread_t tid;
  char *errorstr;
  struct sockaddr_in address;
  socklen_t address_len = sizeof(address);

  if (argc == 2) // enough arguments
  {
    account_file_name = argv[1];
    // initialize the bank
    retval = bank_init(&bank);
    if (!retval)
    {
      // read accounts and amount from file
      retval = read_and_initialize_accounts(&bank, account_file_name);
      if (!retval)
      {
        // try to set the SIGUSR1 to save acocunts, don't bother if it fails
        signal(SIGUSR1, signal_usr1_handler);
        // prepare the server socket
        server_fd = prepare_server_fd(BANK_SERVER_PORT, BANK_SERVER_BACKLOG);
        while (server_fd >= 0) // we have a valid file descriptor
        {
          // wait for and accept a new connection
          client_fd = accept(server_fd, (struct sockaddr *) &address, &address_len);
          if (client_fd >=0) // valid client fd
          {
            fprintf(stdout, "Connection from %s:%d\n", inet_ntoa(address.sin_addr), ntohs(address.sin_port));
            client_fd_ptr = malloc(sizeof(client_fd)); // allocate a client_fd resource pointer
            if (client_fd_ptr) // allocation succeeded
            {
              (*client_fd_ptr) = client_fd;
              // start the client handler
              retval = pthread_create(&tid, 0, client_handler_impl, (void *) client_fd_ptr);
              if (!retval) // client handler started
              {
                // detach the thread, we will not wait for it to terminate
                retval = pthread_detach(tid);
                if (retval) // should not fail
                {
                  errorstr = strerror(retval);
                  fprintf(stderr, "int main(void): Failed to detach client handler thread\n");
                  exit(retval); // fatal error
                }
              }
              else // pthread_create failed
              {
                errorstr = strerror(retval);
                fprintf(stderr, "int main(void): Failed to start client handler thread\n");
                free(client_fd_ptr); // free resources as the client handler will not do this
                if (close(client_fd) == -1) perror("int main(void)");
              }
            }
            else // malloc failed
            {
              perror("int main(void)");
              if (close(client_fd) == -1) perror("int main(void)");
            }
          }
          else // accept failed
          {
            perror("int main(void)");
            if (close(server_fd) == -1) perror("int main(void)");
            server_fd = -1;
          }
        }
      }
      else // read_and_initialize_accounts failed
      {
        fprintf(stderr, "int main(void): Failed to read and initialize accounts from %s\n", argv[1]);
      }
      // try to set the SIGUSR1 to default
      signal(SIGUSR1, SIG_DFL);
      // cleanup bank
      bank_destroy(&bank);
    }
    else // bank_init failed
    {
      fprintf(stderr, "int main(void): Failed to prepare bank\n");
    }
  }
  else // insufficient parameters
  {
    fprintf(stdout, "Usage: %s <accounts file>\n", argv[0]);
  }

  return retval;
}


void *client_handler_impl(void *client_fd_ptr)
{
  void *retval_ptr = 0;
  int client_fd;
  char buffer[PROTOCOL_BUFFER_SIZE];
  struct protocol_msg msg;
  ssize_t cnt;
  int retval = 0;

  if (client_fd_ptr)
  {
    // copy the resource pointer to a local variable
    client_fd = (*((int *)client_fd_ptr));
    // free the resource ptr
    free(client_fd_ptr);
    while (client_fd >= 0) // valid client fd
    {
      // wait for data
      cnt = recv(client_fd, buffer, (PROTOCOL_BUFFER_SIZE - 1), 0);
      if (cnt > 0 && cnt < (PROTOCOL_BUFFER_SIZE - 1)  && buffer[cnt - 1] == ETX) // valid message
      {
        buffer[cnt] = 0;
        // deserialize the data
        retval = protocol_msg_deserialize(&msg, buffer);
        if (!retval)
        {
          switch (msg.command)
          {
            case PROTOCOL_COMMAND_BANK_PAY:
              msg.retval = bank_pay(&bank, msg.source_account, msg.destination_account, msg.amount, msg.passwd);
              break;
            case PROTOCOL_COMMAND_BANK_CHECK:
              msg.retval = bank_check(&bank, msg.source_account, msg.destination_account, msg.amount, msg.certificate, msg.passwd);
              break;
            default:
              msg.retval = -1;
              break;
          }
          // serialize reply
          retval = protocol_msg_serialize(&msg, buffer);
          if (!retval)
          {
            // send reply
            cnt = send(client_fd, buffer, strlen(buffer), 0);
            if (cnt < 0) 
            {
              fprintf(stderr, "void *client_handler_impl(void *client_fd_ptr): Error writing message to client\n");
              if (close(client_fd) == -1) perror("void *client_handler_impl(void *client_fd_ptr)");
              client_fd = -1;
            }
          }
          else // protocol_serialize failed
          {
            fprintf(stderr, "void *client_handler_impl(void *client_fd_ptr): Error serializing message for client\n");
            if (close(client_fd) == -1) perror("void *client_handler_impl(void *client_fd_ptr)");
            client_fd = -1;
          }
        }
        else // protocol_deserialize failed
        {
          fprintf(stderr, "void *client_handler_impl(void *client_fd_ptr): Error deserializing message from client\n");
          if (close(client_fd) == -1) perror("void *client_handler_impl(void *client_fd_ptr)");
          client_fd = -1;
        }
      }
      else // error reading from client
      {
        if (cnt != 0) fprintf(stderr, "void *client_handler_impl(void *client_fd_ptr): Error reading message from client\n");
        //else fprintf(stdout, "Closing connection\n");
        if (close(client_fd) == -1) perror("void *client_handler_impl(void *client_fd_ptr)");
        client_fd = -1;
      }
    }
  }

  return retval_ptr;
}



int read_and_initialize_accounts(struct bank *bank, char *account_file_name)
{
  int retval = 0;
  struct bank_account *accountv = 0;
  size_t accountc = 0;
  size_t itr;

  if (bank && account_file_name)
  {
    retval = read_accounts(account_file_name, &accountc, &accountv);
    if (!retval)
    {
      for (itr = 0; itr < accountc && !retval; itr++)
      {
        // initialize account with data read from the file
        retval = bank_initialize_account(bank, 0, bank->account_count, &accountv[itr]);
        if (retval)
        {
          fprintf(stderr, "int read_and_initialize_accounts(struct bank *bank, char *account_file_name): Failed to initialize account\n");
        }
      }
    }
    else // read_account failed
    {
      fprintf(stderr, "int read_and_initialize_accounts(struct bank *bank, char *account_file_name): Failed to read accounts\n");
    }
    if (accountv) free(accountv);
  }

  return retval;
}


void signal_usr1_handler(int arg)
{
  int local_retval = 0;
  char *errorstr = 0;

  local_retval = pthread_mutex_lock(&bank.lock);
  if (!local_retval)
  {
    local_retval = write_accounts(account_file_name, bank.account_count, bank.accounts);
    if (!local_retval)
    {
      fprintf(stderr, "Accounts stored\n");
    }
    else
    {
      fprintf(stderr, "void signal_usr1_handler(int arg): Failed to store accounts\n");
    }
    local_retval = pthread_mutex_unlock(&bank.lock);
    if (local_retval)
    {
      errorstr = strerror(local_retval);
      fprintf(stderr, "void signal_usr1_handler(int arg): %s\n", errorstr ? errorstr : "Failed to unlock mutex");
      // if we cannot unlock a mutex all is lost
      exit(local_retval);
    }
  }
  else // pthread_mutex_lock failed
  {
    errorstr = strerror(local_retval);
    fprintf(stderr, "void signal_usr1_handler(int arg): %s\n", errorstr ? errorstr : "Failed to lock mutex");
  }
}


