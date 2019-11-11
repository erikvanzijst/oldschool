#ifndef BANK_CLIENT_H
#define BANK_CLIENT_H

#include "protocol.h"
#include "network.h"

/* bank_pay() returns the payment certificate, */
/* or -1 in case of an error */ 
int bank_pay(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount); 

/* bank_check() returns 0 if the certificate is valid, */ 
/* or -1 if the certificate is invalid */ 
int bank_check(char *bank_ip_address, int bank_port_nb, int source_account, int destination_account, float amount, int certificate); 

#endif
