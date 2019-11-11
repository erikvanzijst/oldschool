#ifndef NETWORK_H
#define NETWORK_H

#include "globals.h"
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <fcntl.h>

#define LISTENQ 1024

int TcpListen(int port);
int UnBlock(int fd);
int MakeSelectList(int listenfd, fd_set *rfds, int *highest, struct Login login[], struct Player player[], struct ServParms *servParms);
int RecvReady(int sockfd);
int AcceptNewConn(int listenfd, struct Login login[], struct Player player[], struct ServParms *servParms);
int AddToLogin(struct Login login[], int nX, int sockfd, const struct sockaddr *cliaddr);
int AddToPlayer(struct Login login[], int nX, struct Player player[], struct ServParms *servParms);
int RemoveLogin(struct Login login[], int nX, char msg[]);
int RemovePlayer(struct Player player[], struct ServParms *servParms, int nX, char msg[]);
int Tell(int sockfd, char message[]);
int ReadFromLogin(struct Login login[], struct Player player[], struct ServParms *servParms);
int ReadFromPlayer(struct Player player[], struct ServParms *servParms);
int AddressToName(const struct sockaddr *addr, char fqdn[]);
int BroadCast(struct Player player[], struct ServParms *servParms, char msg[]);
/* see network.c for detailed info on these functions */

#endif
