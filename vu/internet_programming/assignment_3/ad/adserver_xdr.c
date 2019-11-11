/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "adserver.h"

bool_t
xdr_price (XDR *xdrs, price *objp)
{
	//register int32_t *buf;

	 if (!xdr_float (xdrs, &objp->amount))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->units))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_pricelist_t (XDR *xdrs, pricelist_t *objp)
{
	//register int32_t *buf;

	 if (!xdr_array (xdrs, (char **)&objp->pricelist_t_val, (u_int *) &objp->pricelist_t_len, ~0,
		sizeof (price), (xdrproc_t) xdr_price))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_filename_t (XDR *xdrs, filename_t *objp)
{
	//register int32_t *buf;

	 if (!xdr_string (xdrs, objp, FILENAME_LEN_MAX))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_ad (XDR *xdrs, ad *objp)
{
	//register int32_t *buf;

	 if (!xdr_filename_t (xdrs, &objp->filename))
		 return FALSE;
	 if (!xdr_bytes (xdrs, (char **)&objp->data.data_val, (u_int *) &objp->data.data_len, ~0))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_purchase (XDR *xdrs, purchase *objp)
{
	//register int32_t *buf;

	 if (!xdr_array (xdrs, (char **)&objp->ads.ads_val, (u_int *) &objp->ads.ads_len, ~0,
		sizeof (ad), (xdrproc_t) xdr_ad))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->source_account))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->certificate))
		 return FALSE;
	return TRUE;
}
