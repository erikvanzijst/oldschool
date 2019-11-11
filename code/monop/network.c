#include "globals.h"
#include "network.h"
#include "parsers.h"
#include "misc.h"

extern int errno;

int TcpListen(int port)	/* easy method for listening on a TCP port */
{	char temp[MAXLINE+1];
	struct sockaddr_in servaddr;
	int listenfd, reuse = 1;

	listenfd = socket(AF_INET, SOCK_STREAM, 0);
	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);	/* any host may connect */
	servaddr.sin_port = htons(port);

	/* release the socket after program crash, avoid TIME_WAIT */
	if(setsockopt(listenfd, SOL_SOCKET, SO_REUSEADDR, &reuse, sizeof(reuse)) == -1)
	{	/* error */
		sprintf(temp, "Error in setsockopt(): %s\n", strerror(errno));
		println(temp);
		close(listenfd);
		return 0;
	}

	if( (bind(listenfd, (struct sockaddr_in *) &servaddr, sizeof(servaddr))) == -1)
	{	/* error */
		sprintf(temp, "Error in bind(): %s\n", strerror(errno));
		println(temp);
		close(listenfd);
		return 0;
	}

	if(listen(listenfd, LISTENQ) == -1)
	{	/* error */
		sprintf(temp, "Error in listen(): %s\n", strerror(errno));
		println(temp);
		close(listenfd);
		return 0;
	}

	UnBlock(listenfd);	/* set to non-blocking for select() */
	return listenfd;
}


int UnBlock(int fd)	/* set fd in non-blocking mode */
{	int flags;
	char temp[MAXLINE+1];
	/* get current socket flags */
	if ((flags=fcntl(fd, F_GETFL)) == -1)
	{	sprintf(temp, "Error in F_GETFL: %s\n", strerror(errno));
		println(temp);
		exit(1);	/* serious error, kill program */
	}

	/* set socket to non-blocking */
	flags |= O_NDELAY;
	if (fcntl(fd, F_SETFL, flags) == -1)
	{	sprintf(temp, "Error in F_SETFL: %s\n", (char *) strerror(errno));
		println(temp);
		exit(1);	/* serious error, kill program */
	}
	return 0;
}


int MakeSelectList(int listenfd, fd_set *rfds, int *highest, struct Login login[], struct Player player[], struct ServParms *servParms)	/* create the list of fd's for select */
{	int nX;
	FD_ZERO(rfds);
	FD_SET(listenfd, rfds);
	*highest = listenfd;

	/* add stdin to the select list */
	FD_SET(0, rfds);

	for(nX = 0; nX < servParms->maxPlayers; nX++)	/* add the connected players */
	{	if( player[nX].fd > 0 )
		{	FD_SET(player[nX].fd, rfds);
			if( player[nX].fd > *highest )
				*highest = player[nX].fd;
		}
	}

	for(nX = 0; nX < servParms->maxConns; nX++)	/* add the current logins */
	{	if( login[nX].fd > 0 )
		{	FD_SET(login[nX].fd, rfds);
			if( login[nX].fd > *highest )
				*highest = login[nX].fd;
		}
	}
	return 0;
}


int RecvReady(int sockfd)	/* check for available data on a socket */
{	/* returns 0 on no data, 1 if there's data to be read */
	fd_set rfds;
	char temp[200];	/* just a one time little buffer */
	struct timeval tv;
	int response;

	FD_ZERO(&rfds);
	FD_SET(sockfd, &rfds);
	tv.tv_sec = 0;
	tv.tv_usec = 1;	/* return immediately */
	if ((response=select(sockfd+1, &rfds, NULL, NULL, &tv)) == -1)
	{	sprintf(temp, "Error in select(): %s", (char*) strerror(errno));
		println(temp);
		exit(1);
	}
	return (response != 0 && FD_ISSET(sockfd, &rfds));
}


int AcceptNewConn(int listenfd, struct Login login[], struct Player player[], struct ServParms *servParms)
{	int sockfd, len, nX, count1, count2;
	struct sockaddr_in cliaddr;

	if(!RecvReady(listenfd))
		return 0;

	len = sizeof(cliaddr);
	count1 = count2 = 0;
	sockfd = accept(listenfd, (struct sockaddr *) &cliaddr, &len);

	/* check if we're full */
	for(nX = 0; nX < servParms->maxPlayers; nX++)	/* already max amount of players */
		if(player[nX].fd > 0) count1++;
	for(nX = 0; nX < servParms->maxConns; nX++)	/* login queue maxed out */
		if(login[nX].fd > 0) count2++;
	
	if(( count1 >= servParms->maxPlayers ) || ( count2 >= servParms->maxConns ))
	{	write(sockfd, "Sorry, this server is full.\r\n", sizeof("Sorry, this server is full.\r\n"));
		close(sockfd);
		return 1;
	}
	/* move it to a login struct */
	for(nX = 0; nX < servParms->maxConns; nX++)	/* find an empty login struct */
		if( login[nX].fd == -1 )
		{	AddToLogin(login, nX, sockfd, (struct sockaddr *) &cliaddr);
			break;
		}

	return 0;
}


int ReadFromLogin(struct Login login[], struct Player player[], struct ServParms *servParms)
{	int nX, stop, n;
	char msg[MAXLINE+1];

	stop = 0;
	for(nX = 0; (nX < servParms->maxConns); nX++)
		if(login[nX].fd > 0)
			if(RecvReady(login[nX].fd))
			{	stop = 1;
				break;
			}

	if(!stop)	/* no login connection had data */
		return 0;

	n = read(login[nX].fd, msg, MAXLINE);
	if(n == 0)	/* connection was closed */
	{	RemoveLogin(login, nX, "");
		return 0;
	}
	msg[n] = 0;	/* terminate the string */
	
	/* send it to the parser */
	ParseLogin(login, nX, msg, player, servParms);

	return 0;
}


int ReadFromPlayer(struct Player player[], struct ServParms *servParms)
{	int nX, stop, n;
	char msg[MAXLINE+1];
	char *tmp;

	stop = 0;
	for(nX = 0; nX < servParms->maxPlayers; nX++)
		if(player[nX].fd > 0)
			if(RecvReady(player[nX].fd))
			{	stop = 1;
				break;
			}

	if(!stop)	/* no player had data */
		return 0;

	n = read(player[nX].fd, msg, MAXLINE);
	if(n == 0)	/* connection was closed */
	{	sprintf(msg, "Signoff %s (Connection closed by remote host).\n", player[nX].nick);
		println(msg);
		sprintf(msg, "/quit %s (Connection closed by remote host)\r\n", player[nX].nick);
		RemovePlayer(player, servParms, nX, "");
		BroadCast(player, servParms, msg);
		return 0;
	}

	msg[n] = 0;	/* terminate the string */

	tmp = (char *) malloc(strlen(msg)*2);
	strcpy(tmp, msg);
	while(GetFirstString(tmp, msg))	/* get the first command from the string */
	{	tmp = (char *) ByteStuffing(tmp, DESTUFF);
		ParsePlayer(player, nX, tmp, servParms);	/* send it to the parser */
	} free(tmp);

	return 0;
}


int AddToLogin(struct Login login[], int nX, int sockfd, const struct sockaddr *cliaddr)
{	char temp[MAXLINE+1];
	login[nX].fd = sockfd;
	login[nX].stage = W8FORLOGIN;
	login[nX].cliaddr = (struct sockaddr_in *) cliaddr;
	strcpy( login[nX].ipnr, (char *) inet_ntoa( login[nX].cliaddr->sin_addr.s_addr ));
	AddressToName((struct sockaddr *) login[nX].cliaddr, login[nX].fqdn);

	sprintf(temp, "Login from %s (%s)\n", login[nX].fqdn, login[nX].ipnr);
	println(temp);

	Tell(login[nX].fd, (char *) LOGINMSG);	/* send login message to the new client */
	return 0;
}


int AddToPlayer(struct Login login[], int nX, struct Player player[], struct ServParms *servParms)
{	/* returns the number of the new player or -1 on error */
	/* check for room and free the login structure */
	int mX, lX, stop, token, count;
	char temp[MAXEMAILLEN+1];

	stop = token = count = 0;
	if(!NickVal( login[nX].nick, AVAIL, player, servParms))
	{	RemoveLogin( login, nX, NICKINUSE );
		return -1;	/* nickname in use */
	}

	for(mX = 0; mX < servParms->maxPlayers; mX++)
		if( player[mX].fd == -1)
		{	stop = 1;
			break;
		}
	if(!stop)
	{	RemoveLogin( login, nX, SERVERFULL );
		return -1;	/* player not added */
	}

	if( servParms->running )	/* game already in session */
	{	RemoveLogin( login, nX, RUNNING );
		return -1;	/* player not added */
	}
	/* OK to shift the login structure to a player structure */
	Tell(login[nX].fd, (char *) "Login successfull.\r\n");
	player[mX].fd = login[nX].fd;	/* copy the file descriptor */
	player[mX].budget = servParms->budget;
	strcpy(player[mX].nick, login[nX].nick);	/* nickname */
	strcpy(player[mX].ipnr, login[nX].ipnr);	/* ip */
	strcpy(player[mX].fqdn, login[nX].fqdn);	/* and domain name */
	strcpy(player[mX].realname, "Realname");
	sprintf(temp, "%s@%s", player[mX].nick, player[mX].fqdn);	/* guess some email address */
	strcpy(player[mX].email, temp);
	player[mX].token = GenToken(player, servParms);	/* select a token for this player */
	player[mX].pos = player[mX].ready = 0;

	/* check if this is the first player, if so, make this root */
	for(lX = 0; lX < servParms->maxPlayers; lX++)
		if(player[lX].fd > 0) count++;
	if(count == 1) player[mX].flags = ROOT;
	else player[mX].flags = NONE;

	/* clear the login struct */
	login[nX].fd = -1;
	login[nX].retry = 0;
	login[nX].stage = W8FORLOGIN;
	strcpy(login[nX].nick, "");
	strcpy(login[nX].ipnr, "");
	strcpy(login[nX].fqdn, "");
	return mX;	/* player successfully added */
}


int RemoveLogin(struct Login login[], int nX, char msg[])
{	char temp[MAXLINE+1];
	sprintf(temp, "%s (%s) resigned from logging in.\n", login[nX].fqdn, login[nX].ipnr);
	println(temp);
	Tell( login[nX].fd, msg);
	close(login[nX].fd);
	login[nX].fd = -1;
	strcpy(login[nX].nick, "");
	strcpy(login[nX].ipnr, "");
	strcpy(login[nX].fqdn, "");
	login[nX].stage = W8FORLOGIN;
	login[nX].retry = 0;
	return 0;
}


int RemovePlayer(struct Player player[], struct ServParms *servParms, int nX, char msg[])
{	char userlist[MAXLINE+1];
	Tell( player[nX].fd, msg );	/* tell the player why */

	/* give away the player possessions */

	close(player[nX].fd);
	InitPlayers(player, servParms, nX);	/* reset the player structure */

	/* now tell everyone by sending a new userlist */
	MakeUserList(userlist, player, servParms);
	sprintf(userlist, "%s\r\n", userlist);
	BroadCast(player, servParms, userlist);	/* tell everyone */
	/* check if remaining players are ready */
	if( (AllReady(player, servParms) )&&(!servParms->running) )
		StartGame(servParms, player);	/* game starts */
	return 0;
}


int Tell(int sockfd, char message[])	/* wrapper for write() */
{	char *msg;
	msg = (char *) malloc( 4*(strlen(message)+3) ); /* extra room to add chars */
	if(msg == NULL)
	{	println("Error on malloc\n");
		exit(1);	/* severe error, likely to cause seg faults on further execution */
	}
	strcpy(msg, message);	/* copy the original message into the larger array */
	/* strcat(msg, "");	/* and add something */
	msg = (char *) ByteStuffing(msg, STUFF);	/* make any slash double */
	write(sockfd, msg, strlen(msg));	/* now send it onto the network */
	free(msg);	/* free the allocated memory */
	return 0;
}


int AddressToName(const struct sockaddr *addr, char fqdn[]) /* get the dns from an IP */
{	/* sets &fqdn to the dns or on error to "unresolved" */
	struct hostent *host;
	struct sockaddr_in *sin;

	sin = (struct sockaddr_in *) addr;
	if( (host = gethostbyaddr((char *)&sin->sin_addr, sizeof(sin->sin_addr), AF_INET)) == NULL)
		strcpy(fqdn, "unresolved");
	else
		strcpy(fqdn, host->h_name);
	return 0;
}


int BroadCast(struct Player player[], struct ServParms *servParms, char msg[])
{	int nX;	/* send a string to all connected players */

	for(nX = 0; nX < servParms->maxPlayers; nX++)
		if( player[nX].fd > 0 )
			Tell(player[nX].fd, msg);
	return 0;
}
