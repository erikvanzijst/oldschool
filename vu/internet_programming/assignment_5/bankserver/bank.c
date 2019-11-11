#include "bank.h"


int bank_init(struct bank *bank)
{
  int retval = 0;
  char *errorstr;

  if (bank)
  {
    // initialize the bank structure
    bank->payments = 0;
    bank->payment_count = 0;
    srand(time(0));
    retval = pthread_mutex_init(&bank->lock, 0);
    if (retval)
    {
      errorstr = strerror(retval);
      fprintf(stderr, "int bank_init(struct bank *bank): %s\n", errorstr ? errorstr : "Failed to initialize lock\n");
    }
  }
  
  return retval;
}


void bank_destroy(struct bank *bank)
{
  int retval = 0;
  char *errorstr;

  if (bank)
  {
    // destroy the bank structure
    if (bank->payments) free(bank->payments);
    retval = pthread_mutex_destroy(&bank->lock); 
    if (retval)
    {
      errorstr = strerror(retval);
      fprintf(stderr, "void bank_destroy(struct bank *bank): %s\n", errorstr ? errorstr : "Failed to destroy mutex");
      // if we cannot destroy a mutex all is lost
      exit(retval);
    }
  }
}


int bank_pay(struct bank *bank, int source_account, int destination_account, float amount, char *password)
{
  struct bank_payment payment;
  struct bank_account *destination_account_ptr;
  struct bank_account *source_account_ptr;
  int retval = 0;
  int certificate = -1;
  char *errorstr;

  if (bank && password)
  {
    payment.source_account = source_account;
    payment.destination_account = destination_account;
    payment.amount = amount;
    // generate certificate
    payment.certificate = rand();
    // lock the bank
    retval = pthread_mutex_lock(&bank->lock);
    if (!retval)
    {
      //look for the source account
      source_account_ptr = bank_find_account(bank, 0, bank->account_count, payment.source_account);
      if (source_account_ptr)
      {
        // verify that the password matches that of the source account
        if (verify_password(source_account_ptr, password))
        {
          // verify that there is enough money on the source account
          if (source_account_ptr->amount >= payment.amount)
          {
            // look for the destination account
            destination_account_ptr = bank_find_account(bank, 0, bank->account_count, payment.destination_account);
            if (destination_account_ptr)
            {
              // transfer money
              source_account_ptr->amount -= payment.amount;
              destination_account_ptr->amount += payment.amount;
              // store the transaction
              retval = bank_store_payment(bank, 0, bank->payment_count, &payment);
              if (!retval)
              {
                certificate = payment.certificate;
              }
              else // bank_store_payment failed
              {
                fprintf(stderr, "int bank_pay(struct bank *bank, int source_account, int destination_account, float amount): Failed to store payment certificate\n");
              }
            }
            else // no destination account
            {
              fprintf(stderr, "int bank_pay(struct bank *bank, int source_account, int destination_account, float amount): Destination account not found\n");
            }
          }
          else // not enough credits on source account
          {
            fprintf(stderr, "int bank_pay(struct bank *bank, int source_account, int destination_account, float amount): Insufficient credit on source account\n");
          }
        }
        else // invalid password
        {
          fprintf(stderr, "int bank_pay(struct bank *bank, int source_account, int destination_account, float amount): Authorization failed\n");
        }
      }
      else // no source account
      {
        fprintf(stderr, "int bank_pay(struct bank *bank, int source_account, int destination_account, float amount): Source account not found\n");
      }
      // unlock the bank
      retval = pthread_mutex_unlock(&bank->lock);
      if (retval)
      {
        errorstr = strerror(retval);
        fprintf(stderr, "int bank_pay(struct bank *bank, int source_account, int destination_account, float amount): %s\n", errorstr ? errorstr : "Failed to unlock mutex");
        // if we cannot unlock a mutex all is lost
        exit(retval);
      }
    }
    else // pthread_mutex_lock failed
    {
      errorstr = strerror(retval);
      fprintf(stderr, "int bank_pay(struct bank *bank, int source_account, int destination_account, float amount): %s\n", errorstr ? errorstr : "Failed to lock mutex");
    }
  }
  
  return certificate;
}


int bank_check(struct bank *bank, int source_account, int destination_account, float amount, int certificate, char *password)
{
  struct bank_payment payment;
  struct bank_account *destination_account_ptr;
  int retval = 0;
  int certificate_valid = -1;
  char *errorstr;

  if (bank && password)
  {
    payment.source_account = source_account;
    payment.destination_account = destination_account;
    payment.amount = amount;
    payment.certificate = certificate;
    // lock the bank
    retval = pthread_mutex_lock(&bank->lock);
    if (!retval)
    {
      // look for the destination account
      destination_account_ptr = bank_find_account(bank, 0, bank->account_count, payment.destination_account);
      if (destination_account_ptr)
      {
        // verify that the password matches that of the destination account
        if (verify_password(destination_account_ptr, password))
        {
          // verify the transaction
          if (bank_verify_payment(bank, 0, bank->payment_count, &payment)) certificate_valid = 0;
        }
        else // invalid password
        {
          fprintf(stderr, "int bank_check(struct bank *bank, int source_account, int destination_account, float amount, int certificate, char *password): Authorization failed\n");
        }
      }
      else // no destination account
      {
        fprintf(stderr, "int bank_check(struct bank *bank, int source_account, int destination_account, float amount, int certificate, char *password): Destination account not found\n");
      }
      // unlock the bank
      retval = pthread_mutex_unlock(&bank->lock);
      if (retval)
      {
        errorstr = strerror(retval);
        fprintf(stderr, "int bank_check(struct bank *bank, int source_account, int destination_account, float amount, int certificate, char *password): %s\n", errorstr ? errorstr : "Failed to unlock mutex");
        // if we cannot unlock a mutex all is lost
        exit(retval);
      }
    }
    else // pthread_mutex_lock failed
    {
      errorstr = strerror(retval);
      fprintf(stderr, "int bank_check(struct bank *bank, int source_account, int destination_account, float amount, int certificate, char *password): %s\n", errorstr ? errorstr : "Failed to lock mutex");
    }
  }

  return certificate_valid;
}


int bank_store_payment(struct bank *bank, size_t lb, size_t ub, struct bank_payment *payment)
{
  int retval = 0;
  void *result;
  size_t mid;
  //size_t i; // debugging code

  if (bank && payment)
  {
    // store a payment in table sorted on certificate
    // use a binary search
    if (lb == ub) // insert here
    {
      // expand the table
      result = realloc(bank->payments, (bank->payment_count + 1) * sizeof(struct bank_payment));
      if (result)
      {
        bank->payments = (struct bank_payment *) result;
        if (lb < bank->payment_count) // move existing data
        {
          memmove(&bank->payments[lb + 1], &bank->payments[lb], (bank->payment_count - lb) * sizeof(struct bank_payment));
        }
        // copy new data
        memcpy(&bank->payments[lb], payment, sizeof(struct bank_payment));
        bank->payment_count++;
        /* debugging code
        for (i = 0; i < bank->payment_count; i++)
        {
          fprintf(stdout, "Cert: %10d\tSource: %10d\tDest: %10d\tAmount: %f\n", bank->payments[i].certificate, bank->payments[i].source_account, bank->payments[i].destination_account, bank->payments[i].amount);
        }
        */
      }
      else // memory allocation error
      {
        perror("int bank_store_payment(struct bank *bank, size_t lb, size_t ub, struct bank_payment *payment)");
        retval = -1;
      }
    }
    else // refine search
    {
      mid = (lb + ub) / 2;
      if (payment->certificate <= bank->payments[mid].certificate ) // search to the left
      {
        retval = bank_store_payment(bank, lb, mid, payment);
      }
      else // search to the right
      {
        retval = bank_store_payment(bank, mid + 1, ub, payment);
      }
    }
  }

  return retval;
}


int bank_verify_payment(struct bank *bank, size_t lb, size_t ub, struct bank_payment *payment)
{
  int valid = 0;
  size_t mid;
  size_t i;

  if (bank && payment)
  {
    // look for the certificate in a sorted table
    // using binary search
    if (lb == ub) // search from here
    {
      // verify elements from this position
      // because we used a random number as a certificate we need to accomodate duplicate certificates 
      for (i = lb; i < bank->payment_count && bank->payments[i].certificate == payment->certificate && !valid; i++) 
      {
        valid = bank->payments[i].source_account == payment->source_account &&
                bank->payments[i].destination_account == payment->destination_account &&
                bank->payments[i].amount == payment->amount;
      }
    }
    else // refine search
    {
      mid = (lb + ub) / 2;
      if (payment->certificate <= bank->payments[mid].certificate ) // search left
      {
        valid = bank_verify_payment(bank, lb, mid, payment);
      }
      else // search right
      {
        valid = bank_verify_payment(bank, mid + 1, ub, payment);
      }
    }
  }

  return valid;
}


int bank_initialize_account(struct bank *bank, size_t lb, size_t ub, struct bank_account *account)
{
  int retval = 0;
  void *result;
  size_t mid;

  if (bank && account)
  {
    // add a bank account to a sorted table on account number
    // using binary search
    if (lb == ub) // insert here
    {
      // expand the table
      result = realloc(bank->accounts, (bank->account_count + 1) * sizeof(struct bank_account));
      if (result)
      {
        bank->accounts = (struct bank_account *) result;
        if (lb < bank->account_count) // move existing data
        {
          memmove(&bank->accounts[lb + 1], &bank->accounts[lb], (bank->account_count - lb) * sizeof(struct bank_account));
        }
        // copy data
        memcpy(&bank->accounts[lb], account, sizeof(struct bank_account));
        bank->account_count++;
      }
      else // memory allocation error
      {
        perror("int bank_initialize_account(struct bank *bank, size_t lb, size_t ub, struct bank_account *account)");
        retval = -1;
      }
    }
    else // refine search
    {
      mid = (lb + ub) / 2;
      if (account->account <= bank->accounts[mid].account ) // search left
      {
        retval = bank_initialize_account(bank, lb, mid, account);
      }
      else // search right
      {
        retval = bank_initialize_account(bank, mid + 1, ub, account);
      }
    }
  }

  return retval;
}


struct bank_account *bank_find_account(struct bank *bank, size_t lb, size_t ub, int account)
{
  struct bank_account *retval = 0;
  size_t mid;

  if (bank)
  {
    // look for a bank account in a sorted table
    // using binary search
    if (lb == ub) // look here
    {
      if (lb < bank->account_count && bank->accounts[lb].account == account) retval = &bank->accounts[lb];
    }
    else // refine search
    {
      mid = (lb + ub) / 2;
      if (account <= bank->accounts[mid].account ) // search left
      {
        retval = bank_find_account(bank, lb, mid, account);
      }
      else // search right
      {
        retval = bank_find_account(bank, mid + 1, ub, account);
      }
    }
  }

  return retval;
}

