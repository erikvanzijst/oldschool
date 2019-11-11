Assignment 5
============

1 Which tools can be used to provide confidentiality?

	Encryption with either symmetric or asymmetric key technology or a
combination of both technologies to provide maximum performance (i.e.
establishing a symmetric session key with the aid of symmetric key technology).
Various packages exist that have already implemented protocols for preserving
confidentiality: PGP/GPG, SSL, SSH, IPSec. We are looking for a package that
we can easily apply to the bank server/client with minimum modification to the
existing software. IPSec would be an option but requires operating system
support. We choose SSL in the form of the stunnel package. Stunnel can be
deployed on both client and server side encrypting the communication between
the client and server instances of stunnel using SSL leaving only the (local)
communication between the bank server and its stunnel instance and the bank
client and its corresponding stunnel instance unencrypted.

	bankserver/startserver: checks that stunnel (server instance) is
running and starts it if required. The private keys is encrypted with a
password: 'internetprogramming'.

	bankserver/stopstunnel: used to stop the stunnel (server instance)
after terminating the bankd daemon.

	bankclient/paynow: prompts the user for source account, destination
account and amount (defaults are provided). Check that stunnel (client
instance) is running and starts it if required. The (optional) private key of the client
is encrypted with the password: 'internetprogramming'. To transfer money we
have implemented client authentication with a password (question 3). Account
passwords are 'abc' followed by the account number.

	bankclient/checknow: prompts the user for source account, destination
account, amount (defaults are provided) and requests a certificate (manual
input required). Check that stunnel (client instance) is running and starts it
if required. The (optional) private key of the client is encrypted with the password:
'internetprogramming'. To verify a certificate the client is required to prove
it owns the destination account by sending a password to the server. Again
account passwords are 'abc' followed by the account number.

	bankclient/stopstunnel: used to stop the stunnel (client instance).


2 Which mechanisms can be used to provide server authentication?

	SSL: the server sends a signed certificate to the client. The client can be
configured to require verification of this certificate with the CA (verify=2)
or against a locally installed certificate (verify=3). We opted to verify
with the Certificate Authority certificate (verify=2) which has the advantage
that the client does not have to configure a seperate certificate for each
server it wants to talk to. 


3 Can stunnel provide client authentication to the bank server?

	No! Although with stunnel it is possible to enforce that only valid
clients can connect (using preconfigured certificates (verify=3)), it is not
possible for the bank server to verify the account number to client binding
since the bank server is completely unaware of stunnel/SSL. The most common
solution for client to server authentication in combination with SSL is to use
some kind of password authentication. We can associate a password with each
account and verify whether the client knows the password for that account. The
password can be stored hashed with a salt (as with UNIX passwords). We have
implemented this in the bankserver and its clients. 


4 Key Management

Required files:
	Server:
		server-signed-key.pem	This is the file containing the
					private key, the certificate request 
					(not used) and the certificate 
					resulting from the certificate
					request. The private key is required 
					by the server to decrypt the 
					pre-master secret sent to it by the
					client. The certificate is required to
                                        enable the client to encrypt the pre-
                                        master secret. The certificate is
                                        actually the public key of the server
                                        signed with the private key of the CA.

	Client:
		cacert.pem		This is the certificate of the CA used 
					to verify certificates signed by the 
					CA. In this case it is the key used
					to verify the server certificate.

Optional files:
	Server:
		cacert.pem		This is the certificate of the CA used 
					to verify certificates signed by the 
					CA. The server requires this
					certificate if SSL is configured with 
					mutual authentication and the server
                                        requires clients to have valid 
                                        (signed by the CA) certificates as 
                                        well.

	Client:
		client-signed-key.pem	This is the file containing the
					private key, the certificate request 
					(not used) and the certificate 
					resulting from the certificate
					request. The certificate and private
                                        key contained in this file are needed 
                                        if SSL is configured for mutual 
                                        authentication.

		server-cert.pem		This file is needed if the client
					explicitly configured for a specific 
					SSL server. If this is the case stunnel
					needs a link to this file with the 
					hashvalue of the certificate followed 
					by a .0 as a name to be able to verify 
					the server certificate.


Notes

* The bank server can be forced to update the accounts file on disk by sending a
SIGUSR1 to it. 
* The passwd utility allows you to set passwords.
* Passwords are currently still echoed to the screen.
* We used stunnel 4.04 (included).
* bankclient/config and bankserver/config are used to configure paths to stunnel
and libs.


Sander van Loo <sander@marketxs.com>, 1351753
Erik van Zijst <erik@marketxs.com>, 1351745

