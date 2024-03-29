/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "adserver.h"


void
adserver_1(char *host)
{
	CLIENT *clnt;
	pricelist_t  *result_1;
	char *adserver_get_pricelist_1_arg;
	int  *result_2;
	struct purchase  adserver_purchase_ads_1_arg;
	struct ad  *result_3;
	char *adserver_get_ad_1_arg;

#ifndef	DEBUG
	clnt = clnt_create (host, ADSERVER, ADSERVER_VERS_1, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */

	result_1 = adserver_get_pricelist_1((void*)&adserver_get_pricelist_1_arg, clnt);
	if (result_1 == (pricelist_t *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_2 = adserver_purchase_ads_1(&adserver_purchase_ads_1_arg, clnt);
	if (result_2 == (int *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_3 = adserver_get_ad_1((void*)&adserver_get_ad_1_arg, clnt);
	if (result_3 == (struct ad *) NULL) {
		clnt_perror (clnt, "call failed");
	}
#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	 /* DEBUG */
}


int
main (int argc, char *argv[])
{
	char *host;

	if (argc < 2) {
		printf ("usage: %s server_host\n", argv[0]);
		exit (1);
	}
	host = argv[1];
	adserver_1 (host);
exit (0);
}
