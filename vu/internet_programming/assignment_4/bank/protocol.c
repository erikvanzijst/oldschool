#include "protocol.h"


int protocol_msg_serialize(struct protocol_msg *msg, char *buffer)
{
  int retval = 0;
  int size;

  if (msg && buffer)
  {
    // serialize all data from the protocol message using ASCII encoding (machine independant)
    size = snprintf(buffer, PROTOCOL_BUFFER_SIZE, "%d|%d|%d|%f|%d|%d%c", msg->command, msg->source_account, msg->destination_account, msg->amount, msg->certificate, msg->retval, ETX);
    if (size < 0 || size >= PROTOCOL_BUFFER_SIZE)
    {
      fprintf(stderr, "int protocol_msg_serialize(struct protocol_msg *msg, char *buffer): Failed to format message\n");
      retval = -1;
    }
  }

  return retval;
}


int protocol_msg_deserialize(struct protocol_msg *msg, char *buffer)
{
  int retval = 0;
  char delimiters[] = {'|', ETX, 0};
  char *lasts = 0;
  char *value;
  int field = 0;

  if (msg && buffer)
  {
    // tokenize the ascii string and set the approriate fields of the protocol message
    for (value = strtok_r(buffer, delimiters, &lasts); value && !retval; value = strtok_r(0, delimiters, &lasts))
    {
      switch(field)
      {
        case 0:
          msg->command = atoi(value);
          break;
        case 1:
          msg->source_account = atoi(value);
          break;
        case 2:
          msg->destination_account = atoi(value);
          break;
        case 3:
          msg->amount = atof(value);
          break;
        case 4:
          msg->certificate = atoi(value);
          break;
        case 5:
          msg->retval = atoi(value);
          break;
        default:
          fprintf(stderr, "int protocol_msg_deserialize(struct protocol_msg *msg, char *buffer): Unknown field in message\n");
          retval = -1;
          break;
      }
      field++;
    }
  }
  
  return retval;
}


