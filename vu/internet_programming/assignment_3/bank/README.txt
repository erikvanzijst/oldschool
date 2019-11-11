Bank server

General outline

The bank server (bankd) manages accounts and transactions. Accounts are read
from a configfile that can be specified on the command line. The accounts
configuration file contains on account - amount pair per line. Accounts and
amounts are seperated by a comma.No spaces are allowed. Bank accounts are
stored in a sorted table that is accessed using a binary search.

For every transaction the bank server verifies that the source account exists,
the source account has enough credit and the destination account exists. If the
transaction completes successfully it is stored in a sorted table that can be
accessed using a binary search.

For a verification request the bank server verifies that source account,
destination account, amount and certificate are all as specified. If only one
value is different it will fail. The bank server allows duplicate certificates
to be generated, unless all other parameters are equals as well, this will not
pose a problem.

bank_client.[ch] acts as a client stub for communicating with the bank server.
It handles connection set-up/tear-down, serialization and deserialization. The
actual serialization and deserialization is implemented in protocol.[ch].
protocol.[ch] defines a structure that is passed to a (de)serializer containing
all supported fields. The write format between client and server is ASCII to
rule out any possible byte ordering problems.  Messages are terminated with an
ETX.

Wire format:
<command number>|<src acct>|<dst acct>|<amount>|<certificate>|<retval><ETX>

The client and server use a synchronous request-reply model for communication.

A.P.J. van Loo <sander@marketxs.com>, 1351753
E. van Zijst <erik@marketxs.com>, 1351745

