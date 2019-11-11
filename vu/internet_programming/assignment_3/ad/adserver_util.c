#include "adserver_util.h"


char *prices_file_name;
int adserver_account;


int pricelist_read_prices(pricelist_t *prices)
{
  int retval = 0;
  FILE *prices_file;
  struct price price;
  char buffer[ADSERVER_PRICES_FILE_BUFFER_SIZE];
  char delimiters[] = {'\n','=',' ',0};
  char *value;
  char *lasts;
  int field;

  if (prices)
  {
    prices_file = fopen(prices_file_name, "r");
    if (prices_file)
    {
      while (fgets(buffer, sizeof(buffer), prices_file) && !retval)
      {
        lasts = 0;
        field = 0;
        for (value = strtok_r(buffer, delimiters, &lasts); value && !retval; value = strtok_r(0, delimiters, &lasts))
        {
          switch (field)
          {
            case 0:
              price.units = atoi(value);
              break;
            case 1:
              price.amount = atof(value);
              break;
            default:
              fprintf(stderr, "int pricelist_read_prices(pricelist_t *prices): Unknown field in message\n");
              retval = -1;
              break;
          }
          field++;
        }
        if (!retval)
        {
          retval = pricelist_add_price(prices, &price);
          if (retval)
          {
            fprintf(stderr, "int pricelist_read_prices(pricelist_t *prices): Failed to add price to the list\n");
          }
        }
      }
      fclose(prices_file);
    }
    else // fopen failed
    {
      perror("int pricelist_read_prices(pricelist_t *prices)");
      retval = -1;
    }
  }

  return retval;
}


int pricelist_add_price(pricelist_t *prices, struct price *price)
{
  int retval = 0;
  void *result = 0;

  if (prices && price)
  {
    result = realloc(prices->pricelist_t_val, (prices->pricelist_t_len + 1) * sizeof(struct price));
    if (result)
    {
      prices->pricelist_t_val = result;
      memcpy(&prices->pricelist_t_val[prices->pricelist_t_len], price, sizeof(struct price));
      prices->pricelist_t_len++;
    }
    else // realloc failed
    {
      perror("int pricelist_add_price(pricelist_t *prices, struct price *price)");
      retval = -1;
    }
  }
  
  return 0;
}


void pricelist_clear(pricelist_t *prices)
{
  if (prices)
  {
    prices->pricelist_t_len = 0;
    if (prices->pricelist_t_val) 
    {
      free(prices->pricelist_t_val);
      prices->pricelist_t_val = 0;
    }
  }
}


float pricelist_get_amount(pricelist_t *pricelist, int units)
{
  float amount = 0;
  struct price price;
  unsigned int i;

  if (pricelist)
  {
    memset(&price, 0, sizeof(price));
    for (i = 0; i < pricelist->pricelist_t_len; i++)
    {
      if (units >= pricelist->pricelist_t_val[i].units && pricelist->pricelist_t_val[i].units > 0) // price is valid
      {
        if (pricelist->pricelist_t_val[i].units > price.units) // more accurate price
        {
          price = pricelist->pricelist_t_val[i];
          amount = (price.amount / price.units) * units;
        }
      }
    }
  }

  return amount;
}


int pricelist_get_units(pricelist_t *pricelist, float budget)
{
  int units = 0;
  float amount = 0;
  int i;
  
  if (pricelist)
  {
    for(i = 0; amount <= budget; i++)
    {
      amount = pricelist_get_amount(pricelist, i);
      if (amount <= budget) units = i;
    }
  }

  return units;
}


int ad_read(struct ad *ad)
{
  int retval = 0;
  char byte;
  FILE *adfile;
  void *result;

  if (ad)
  {
    ad->data.data_len = 0;
    if (ad->data.data_val) 
    {
      free(ad->data.data_val);
      ad->data.data_val = 0;
    }
    if (ad->filename)
    {
      adfile = fopen(ad->filename, "r");
      if (adfile)
      {
        while (!feof(adfile) && !retval)
        {
          byte = fgetc(adfile);
          if (!feof(adfile))
          {
            result = realloc(ad->data.data_val, ad->data.data_len + 1);
            if (result)
            {
              ad->data.data_val = result;
              ad->data.data_val[ad->data.data_len] = byte;
              ad->data.data_len++;
            }
            else // realloc failed
            {
              perror("int ad_read(struct ad *ad)");
              retval = -1;
            }
          }
        }
        fclose(adfile);
      }
      else // fopen failed
      {
        perror("int ad_read(struct ad *ad)");
        retval = -1;
      }
    }
    else
    {
      fprintf(stderr, "int ad_read(struct ad *ad): Filename not set\n");
      retval = -1;
    }
  }

  return retval;
}


int ad_write(struct ad *ad)
{
  int retval = 0;
  FILE *adfile;
  unsigned int pos;
  int byte;
 
  if (ad)
  {
    if (ad->filename)
    {
      adfile = fopen(ad->filename, "w");
      if (adfile)
      {
        for (pos = 0; pos < ad->data.data_len && !retval; pos++)
        {
          byte = fputc(ad->data.data_val[pos], adfile);
          if (byte == EOF)
          {
            perror("int ad_write(struct ad *ad)");
            retval = -1;
          }
        }
        fclose(adfile);
      }
      else // fopen failed
      {
        perror("int ad_write(struct ad *ad)");
        retval = -1;
      }
    }
    else
    {
      fprintf(stderr, "int ad_write(struct ad *ad): Filename not set\n");
      retval = -1;
    }
  }
 
  return retval;
}


int ad_copy(struct ad *dst, struct ad *src)
{
  int retval = 0;
  void *result;

  if (dst && src)
  {
    if (dst->filename) 
    {
      free(dst->filename);
      dst->filename = 0;
    }
    if (src->filename)
    {
      dst->filename = strdup(src->filename);
      if (!dst->filename)
      {
        perror("int ad_copy(struct ad *dst, struct ad *src)");
        retval = -1;
      }
    }
    if (!retval)
    {
      if (src->data.data_val)
      {
        result = realloc(dst->data.data_val, src->data.data_len);
        if (result)
        {
          dst->data.data_val = result;
          dst->data.data_len = src->data.data_len;
          memcpy(dst->data.data_val, src->data.data_val, dst->data.data_len);
        }
        else // realloc failed
        {
          perror("int ad_copy(struct ad *dst, struct ad *src)");
          retval = -1;
        }
      }
      else // free dst data
      {
        dst->data.data_len = 0;
        if (dst->data.data_val)
        {
          free(dst->data.data_val);
          dst->data.data_val = 0;
        }
      }
    }
  }

  return retval;
}


void ad_free(struct ad *ad)
{
  if (ad)
  {
    if (ad->filename) free(ad->filename);
    if (ad->data.data_val) free(ad->data.data_val);
  }
}


int purchase_add_ad(struct purchase *purchase, struct ad *ad)
{
  int retval = 0;
  void *result = 0;

  if (purchase && ad)
  {
    result = realloc(purchase->ads.ads_val, (purchase->ads.ads_len + 1) * sizeof(struct ad));
    if (result)
    {
      purchase->ads.ads_val = result;
      memset(&purchase->ads.ads_val[purchase->ads.ads_len], 0, sizeof(struct ad));
      retval = ad_copy(&purchase->ads.ads_val[purchase->ads.ads_len], ad);
      if (!retval)
      {
        purchase->ads.ads_len++;
      }
      else // ad_copy failed
      {
        fprintf(stderr, "int purchase_add_ad(struct purchase *purchase, struct ad *ad): Failed to copy add\n");
      }
    }
    else // realloc failed
    {
      perror("int purchase_add_ad(struct purchase *purchase, struct ad *ad)");
      retval = -1;
    }
  }
  
  return 0;
}


void purchase_clear(struct purchase *purchase)
{
  unsigned int i;

  if (purchase)
  {
    for (i = 0; i < purchase->ads.ads_len; i++) ad_free(&purchase->ads.ads_val[i]);
    purchase->ads.ads_len = 0;
    if (purchase->ads.ads_val)
    {
      free(purchase->ads.ads_val);
      purchase->ads.ads_val = 0;
    }
  }
}


