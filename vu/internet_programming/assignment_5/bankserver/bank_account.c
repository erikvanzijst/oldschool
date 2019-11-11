#include "bank_account.h"


int read_accounts(char *account_file_name, size_t *accountc, struct bank_account **accountv)
{
  int retval = 0;
  FILE *account_file;
  struct bank_account account;
  char buffer[BANK_ACCOUNT_FILE_BUFFER_SIZE];
  char delimiters[] = {'\n',':',0};
  char *value;
  char *lasts;
  void *result;
  int field;

  if (account_file_name && accountc && accountv)
  {
    // read data from the account file
    // open account file
    account_file = fopen(account_file_name, "r");
    if (account_file)
    {
      // read till end of file
      while (fgets(buffer, sizeof(buffer), account_file))
      {
        lasts = 0;
        field = 0;
        memset(&account, 0, sizeof(account));
        // tokenize
        for (value = strtok_r(buffer, delimiters, &lasts); value && !retval; value = strtok_r(0, delimiters, &lasts))
        {
          switch (field)
          {
            case 0: // account number
              account.account = atoi(value);
              break;
            case 1: // account credit
              account.amount = atof(value);
              break;
            case 2: // passwd
              if (value) strlcpy(account.passwd, value, sizeof(account.passwd));
              break;
            default:
              fprintf(stderr, "int read_accounts(char *account_file_name, size_t *accountc, struct bank_account **accountv): Unknown field in message\n");
              retval = -1;
              break;
          }
          field++;
        }
        if (!retval) // no previous error encountered
        {
          result = realloc((*accountv), sizeof(struct bank_account) * ((*accountc) + 1));
          if (result)
          {
            (*accountv) = result;
            memcpy(&(*accountv)[(*accountc)], &account, sizeof(struct bank_account));
            (*accountc)++;
          }
          else // memory allocation error
          {
            perror("int read_accounts(char *account_file_name, size_t *accountc, struct bank_account **accountv)");
            retval = -1;
          }
        }
      }
      // close the acount file
      fclose(account_file);
    }
    else // fopen failed
    {
      perror("int read_accounts(char *account_file_name, size_t *accountc, struct bank_account **accountv)");
      retval = -1;
    }
  }

  return retval;
}


int write_accounts(char *account_file_name, size_t accountc, struct bank_account *accountv)
{
  int retval = 0;
  FILE *account_file;
  size_t itr;

  if (account_file_name && accountc && accountv)
  {
    // write data to the account file
    // open account file
    account_file = fopen(account_file_name, "w");
    if (account_file)
    {
      // iterate through the accounts
      for (itr = 0; itr < accountc; itr++)
      {
        fprintf(account_file, "%d:%f:%s\n", accountv[itr].account, accountv[itr].amount, accountv[itr].passwd);
      }
      // close the acount file
      fclose(account_file);
    }
    else // fopen failed
    {
      perror("int write_accounts(char *account_file_name, size_t accountc, struct bank_account *accountv)");
      retval = -1;
    }
  }

  return retval;
}


void prepare_salt(void)
{
  // initialize the random number generator with time
  srand(time(0));
}


int set_password(struct bank_account *account, char *password)
{
  int retval = 0;
  char salt[3] = {0};
  char *hash;

  if (account && password)
  {
    // generate a random salt from the range of allowed characters
    salt[0] = BANK_ACCOUNT_SALT_CHAR[(int) (((float)strlen(BANK_ACCOUNT_SALT_CHAR)) * rand() / (RAND_MAX + 1.0))];
    salt[1] = BANK_ACCOUNT_SALT_CHAR[(int) (((float)strlen(BANK_ACCOUNT_SALT_CHAR)) * rand() / (RAND_MAX + 1.0))];
    // encrypt the password with the salt
    hash = crypt(password, salt);
    if (hash)
    {
      // copy the (hashed) password to the account
      strlcpy(account->passwd, hash, sizeof(account->passwd));
    }
    else // failed to hash password
    {
      fprintf(stderr, "int set_password(struct bank_account *account, char *password): Failed to set password\n");
    }
  }
 
  return retval;
}


int verify_password(struct bank_account *account, char *password)
{
  int retval = 0;
  char salt[3] = {0};
  char *hash;

  if (account && password)
  {
    if (strlen(account->passwd) >= 2)
    {
      // the first two characters from the hash are the salt
      memcpy(salt, account->passwd, 2);
      // do the encryption on the password
      hash = crypt(password, account->passwd);
      if (hash)
      {
        // if the new hash matches the stored hash the password must be ok
        if (strcmp(account->passwd, hash) == 0) retval = 1;
      }
    }
  }

  return retval;
}


