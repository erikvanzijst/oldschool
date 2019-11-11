#ifndef ADSERVER_UTIL_H
#define ADSERVER_UTIL_H

#include "adserver.h"

#define ADSERVER_UTIL_UPLOAD_DIR	"ads"

extern char *prices_file_name;
extern int adserver_account;

int pricelist_read_prices(pricelist_t *prices);
int pricelist_add_price(pricelist_t *prices, struct price *price);
void pricelist_clear(pricelist_t *prices);

float pricelist_get_amount(pricelist_t *pricelist, int units);
int pricelist_get_units(pricelist_t *pricelist, float budget);

int ad_read(struct ad *ad);
int ad_write(struct ad *ad);
int ad_copy(struct ad *dst, struct ad *src);
void ad_free(struct ad *ad);
void ad_clear(struct ad *ad);

int purchase_add_ad(struct purchase *purchase, struct ad *ad);
void purchase_clear(struct purchase *purchase);

#endif
