struct price
{
  float amount;
  int units;
};
typedef struct price pricelist_t<>;


const FILENAME_LEN_MAX = 256;
typedef string filename_t<FILENAME_LEN_MAX>;


struct ad
{
  filename_t filename;
  opaque data<>;
};


struct purchase
{
  struct ad ads<>;
  int source_account;
  int certificate;
};


const ADSERVER_PRICES_FILE_BUFFER_SIZE = 256;


program ADSERVER
{
  version ADSERVER_VERS_1
  {
    pricelist_t ADSERVER_GET_PRICELIST(void) = 1;
    int ADSERVER_PURCHASE_ADS(struct purchase new_ads) = 2;
    struct ad ADSERVER_GET_AD(void) = 3;
  } = 1;
} = 400001;
 

