#include "bank_account.h"


int main(int argc, char *argv[])
{
  struct bank_account *accountv = 0;
  size_t accountc = 0;
  int retval = 0;
  int account;
  size_t itr;
  char password1[10] = {0};
  char password2[10] = {0}; 
  char *hashedpassword = 0;

  if (argc >= 3)
  {
    prepare_salt();
    retval = read_accounts(argv[1], &accountc, &accountv);
    if (!retval)
    {
      account = atoi(argv[2]);
      for (itr = 0; itr < accountc; itr++)
      {
        if (accountv[itr].account == account)
        {
          fprintf(stdout, "%d\t%f\t%s\n", accountv[itr].account, accountv[itr].amount, accountv[itr].passwd);
          fprintf(stdout, "Enter new password: ");
          fgets(password1, sizeof(password1), stdin);
          if (strlen(password1) > 0) password1[strlen(password1) - 1] = 0;
          fprintf(stdout, "Confirm password: ");
          fgets(password2, sizeof(password2), stdin);
          if (strlen(password2) > 0) password2[strlen(password2) - 1] = 0;
          if (strcmp(password1, password2) == 0)
          {
	    retval = set_password(&accountv[itr], password1); 
            if (!retval)
            {
              fprintf(stderr, "Password set\n");
            }
            else // set_password failed
            {
              fprintf(stderr, "Failed to set password\n");
            }
          }
          else
          {
            fprintf(stderr, "Passwords do not match\n");
          }
        }
      }
      retval = write_accounts(argv[1], accountc, accountv);
      if (retval)
      {
        fprintf(stderr, "Failed to write accounts\n");
      }
    }
    else 
    {
      fprintf(stderr, "Failed to read accounts\n");
    }
    if (accountv) free(accountv);
  }
  else // print usage
  {
    fprintf(stdout, "Usage: %s <accounts file> <account number>\n", argv[0]);
  }

  return retval;
}

