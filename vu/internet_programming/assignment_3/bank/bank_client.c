#include "bank_client.h"


int bank_pay(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount)
{
  struct protocol_msg msg;
  char buffer[PROTOCOL_BUFFER_SIZE];
  int server_fd = -1;
  int local_retval = 0;
  ssize_t cnt;

  msg.command = PROTOCOL_COMMAND_BANK_PAY;
  msg.source_account = source_account;
  msg.destination_account = destination_account;
  msg.amount = amount;

  // serialize a payment request, send it to the bank server,
  // wait for the results and deserialize them

  // serialize data
  local_retval = protocol_msg_serialize(&msg, buffer);
  if (!local_retval)
  {
    // connect to the bank server
    server_fd = connect_to_server(bank_ip_address, bank_port_nb);
    if (server_fd >= 0) // valid server fd
    {
      // send serialized data to the bank server
      cnt = send(server_fd, buffer, strlen(buffer), 0);
      if (cnt > 0) // data send
      {
        // wait for data from the bank server
        cnt = recv(server_fd, buffer, PROTOCOL_BUFFER_SIZE - 1, 0);
        if (cnt > 0 && cnt < (PROTOCOL_BUFFER_SIZE - 1)  && buffer[cnt - 1] == ETX) // valid message
        {
          buffer[cnt] = 0;
          // deserialize data
          local_retval = protocol_msg_deserialize(&msg, buffer);
          if (local_retval)
          {
            fprintf(stderr, "int bank_pay(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount): Failed to deserialize message\n");
            msg.retval = -1;
          }
        }
        else // failed to receive message
        {
          fprintf(stderr, "int bank_pay(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount): Error receiving message from server\n");
          msg.retval = -1;
        }
      }
      else // failed send message
      {
        fprintf(stderr, "int bank_pay(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount): Error sending message to server\n");
        msg.retval = -1;
      }
      if (close(server_fd) == -1) fprintf(stderr, "int bank_pay(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount): Error closing connection to server\n");
    }
    else // failed to connect to server
    {
      fprintf(stderr, "int bank_pay(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount): Error connecting to server\n");
      msg.retval = -1;
    }
  }
  else // failed to serialize message
  {
    fprintf(stderr, "int bank_pay(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount): Failed to serialize message\n");
    msg.retval = -1;
  }

  return msg.retval;
}


int bank_check(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount, int certificate)
{
  struct protocol_msg msg;
  char buffer[PROTOCOL_BUFFER_SIZE];
  int server_fd = -1;
  int local_retval = 0;
  ssize_t cnt;

  msg.command = PROTOCOL_COMMAND_BANK_CHECK;
  msg.source_account = source_account;
  msg.destination_account = destination_account;
  msg.amount = amount;
  msg.certificate = certificate;

  // serialize a verification request, send the data to the bank server
  // wait for a reply and deserialize the data

  // serialize request
  local_retval = protocol_msg_serialize(&msg, buffer);
  if (!local_retval)
  {
    // connect to the server
    server_fd = connect_to_server(bank_ip_address, bank_port_nb);
    if (server_fd >= 0) // valid server fd
    {
      // send serialized data
      cnt = send(server_fd, buffer, strlen(buffer), 0);
      if (cnt > 0)
      {
        // receive serialized reply
        cnt = recv(server_fd, buffer, PROTOCOL_BUFFER_SIZE - 1, 0);
        if (cnt > 0 && cnt < (PROTOCOL_BUFFER_SIZE - 1)  && buffer[cnt - 1] == ETX) // valid message
        {
          buffer[cnt] = 0;
          // deserialize
          local_retval = protocol_msg_deserialize(&msg, buffer);
          if (local_retval)
          {
            fprintf(stderr, "int bank_check(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount, int certificate): Failed to deserialize message\n");
            msg.retval = -1;
          }
        }
        else // failed to receive message
        {
          fprintf(stderr, "int bank_check(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount, int certificate): Error receiving message from server\n");
          msg.retval = -1;
        }
      }
      else // failed send message
      {
        fprintf(stderr, "int bank_check(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount, int certificate): Error sending message to server\n");
        msg.retval = -1;
      }
      if (close(server_fd) == -1) fprintf(stderr, "int bank_check(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount, int certificate): Error closing connection to server\n");
    }
    else // failed to connect to server
    {
      fprintf(stderr, "int bank_check(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount, int certificate): Error connecting to server\n");
      msg.retval = -1;
    }
  }
  else // failed to serialize message
  {
    fprintf(stderr, "int bank_check(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount, int certificate): Failed to serialize message\n");
    msg.retval = -1;
  }

  return msg.retval;
}


