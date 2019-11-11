#ifndef BANK_ACCOUNT_H
#define BANK_ACCOUNT_H

#define _XOPEN_SOURCE
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <errno.h>
#include "strlcpy.h"


#define BANK_ACCOUNT_FILE_BUFFER_SIZE  256
#define BANK_ACCOUNT_SALT_CHAR         "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNUVWXYZ0123456789./"
#define BANK_ACCOUNT_PASSWORD_SIZE     20

struct bank_account
{
  int account;
  float amount;
  char passwd[BANK_ACCOUNT_PASSWORD_SIZE];
};

// read accounts from disk
int read_accounts(char *account_file_name, size_t *accountc, struct bank_account **accountv);

// write accounts to disk
int write_accounts(char *account_file_name, size_t accountc, struct bank_account *accountv);

// initialize the random number generator for the salt
void prepare_salt(void);

// set the password
int set_password(struct bank_account *account, char *password);

// verify that the password matches the account
int verify_password(struct bank_account *account, char *password);

#endif
