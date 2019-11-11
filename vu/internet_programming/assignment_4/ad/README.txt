Ad server

General outline

The ad server allows clients to purchase ads according to a pricelist and
request them free of charge. The pricelist of the ad server is stored in a
configuration file that contains one unit - amount pair per line separated
by an equals sign. The ad server can support not only prices for 1, 5 and 10
units but for any user defined value. Uploaded ads are stored in the ads
subdirectory.

The ad server is started with a config file with prices and an account number
that it should use. When purchasing ads the client is only allowed to send the
ads, a source account and a certificate, the ad server already knows its own
(and thus the destination) acocunt number and also calculates the price
independant of the client. It than verifies with the bank if there is such a
transaction.

The adsend application is given a budget to determine how many ads it should
purchase. The source account number and the account number of the ad server
need to be specified as well. After the budget, source account and destination
acocunt a list of ads the adsend application is allowed to send is given as
addional parameters. The adsend application first downloads the pricelist, then
calculates the maximum number of ads it can afford, and what the cost would be.
It then transfers the appropriate amount through the bank server. If the bank
account does not hold enough money this will fail, regardless of the budget
specified. When the money has been successfully transferred a list of ads is
presented to the ad server together with the source account number and the
certificate.

The adget application simply requests an ad from the server, stores the ad in
the /tmp filesystem (the filename is pid.ad, where pid is the process id of the
adget application) and executes the viewer specified on the command line with
the name of the ad as its argument. The adserver maintains a static integer
variable that is used to iterate through the list of ads.

There is no size limit on the ads the server can handle (other than available
memory or disk space).


Adget CGI (Assignment 4)
========================

The adget.cgi program requests a named ad from the adserver. The adserver was
extended with an additional method that first tries to read the named file
from the upload directory of the adserver and reverts to the round-robin
mechanism of the previous assignment as a backup. 

If the adget.cgi program detects an internal error (malloc, zero sized ad, 
etc..) it displays a built-in ad. We wrote a small program called convert.c 
that converts a binary file to a C header file containing a character array 
and the size of the array.

The naming scheme the adserver uses when ads are purchased has 
also been changed to resolve the bug reported in the previous assignment. The 
adserver generates a timestamp and a sequence number for every ad stored and
appends the extension of the original filename to it.  It preserves the 
extension of the file so the client requesting the ad can 
use this information to set (for example) the content type (it would also have 
been possible to add a more formal type field to the ad structure). 

Adget.cgi uses routines written by J. Marshall to read CGI input variables.


	New Files
	  adget.cgi.c	 - source
	  fallback_jpg.h - built-in JPG image
	  cgi_util.[ch]	 - CGI input variable routines
	  adspage.jpg	 - the source for the built-in JPG image
	  convert.c	 - source used to generate the fallback_jpg.h file

	Arguments
	  The adget.cgi supports only one optional argument:
	    ad=<filename>

	Examples
	  http://hostname.domainname/cgi-bin/adget.cgi?ad=ad4.gif
	  http://hostname.domainname/cgi-bin/adget.cgi


A.P.J. van Loo <sander@marketxs.com>, 1351753
E. van Zijst <erik@marketxs.com>, 1351745

