#ifndef BANK_H
#define BANK_H

#include <pthread.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>


struct bank_payment
{
  int source_account;
  int destination_account;
  float amount;
  int certificate;
};


struct bank_account
{
  int account;
  float amount;
};


struct bank
{
  struct bank_payment *payments;
  size_t payment_count;
  struct bank_account *accounts;
  size_t account_count;
  pthread_mutex_t lock;
};


int bank_init(struct bank *bank);
void bank_destroy(struct bank *bank);

/* bank_pay() returns the payment certificate, */ 
/* or -1 in case of an error */ 
int bank_pay(struct bank *bank, int source_account, int destination_account, float amount); 

/* bank_check() returns 0 if the certificate is valid, */ 
/* or -1 if the certificate is invalid */ 
int bank_check(struct bank *bank, int source_account, int destination_account, float amount, int certificate); 

/* maintain a sorted list of certificates issued, return non-zero on error */
int bank_store_payment(struct bank *bank, size_t lb, size_t ub, struct bank_payment *payment);

/* verify payment against the sorted list of certificates issued, return 0 if invalid, 1 if valid */
int bank_verify_payment(struct bank *bank, size_t lb, size_t ub, struct bank_payment *payment);

int bank_initialize_account(struct bank *bank, size_t lb, size_t ub, struct bank_account *account);
struct bank_account *bank_find_account(struct bank *bank, size_t lb, size_t ub, int account);

#endif
