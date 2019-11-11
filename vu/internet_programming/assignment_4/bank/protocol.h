#ifndef PROTOCOL_H
#define PROTOCOL_H

#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>

#define PROTOCOL_BUFFER_SIZE		256
#define ETX				3

#define PROTOCOL_COMMAND_BANK_PAY	1
#define PROTOCOL_COMMAND_BANK_CHECK	2


struct protocol_msg
{
  int command;
  int source_account;
  int destination_account;
  float amount;
  int certificate;
  int retval;
};

int protocol_msg_serialize(struct protocol_msg *msg, char *buffer);
int protocol_msg_deserialize(struct protocol_msg *msg, char *buffer);

#endif
