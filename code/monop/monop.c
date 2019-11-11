/****************************************************************************
*
* Monop server Protocol 1.0  by Erik van Zijst (erik@prutser.cx) May 1999
*
* Dedicated monopoly server. To play, you will need a client. (Using telnet
* is very hard)
*
****************************************************************************/

#include <signal.h>
#include "globals.h"
#include "init.h"
#include "network.h"
#include "config.h"
#include "monop.h"
#include "io.h"

struct Output *outStream;

int main(int argc, char **argv)
{
	/* variables */
	char *temp;
	int listenfd, stop, highest, retval;
	struct ServParms servParms;
	struct Login login[40];	/* structures for the logins */
	struct Player player[10];	/* max 10 players, normally only 6 in use */
	struct timeval tv;
	fd_set rfds;

	outStream = &servParms.output;	/* make the output struct global */
	/* println("Monop server (protocol 1) starting...\n\n");*/

	stop = 0;
	InitServParms(&servParms);	/* included from init.h */
	InitPlayers(player, &servParms, -1);	/* reset the player structures to zero */
	InitLogins(login, &servParms);	/* reset the login structures to zero */
	InitDeedCards(&servParms);	/* load the default US cards */
	InitProperties(servParms.property);	/* load the default US street prices */

	/* read the configfile */
	ReadConfig(&servParms);

	/* parse commandline, config file values may be overridden */
	ParseArgs(argc, argv, &servParms);

	if(servParms.output.logging) OpenLogfile();	/* start logging */
	DisplayConfig(&servParms);
	/* setup the listen socket */
	if( (listenfd = TcpListen(servParms.listenPort)) == 0)
	{	temp = (char *) malloc(80);	/* only malloc just now, i don't wanna hold up mem for nothing */
		sprintf(temp, "Couldn't listen on port %d. Terminating...\n", (int) servParms.listenPort);
		println(temp);
		CloseLogfile();
		exit(1);
	}

	println("--monop server running--\n");
	signal(SIGINT, sigint);	/* ctrl-c trapped */
	signal(SIGQUIT, sigquit);
	while(!stop)	/* main program loop */
	{
		tv.tv_sec = 1; tv.tv_usec = 0;	/* make sure the program doesn't deadlock */
		MakeSelectList(listenfd, &rfds, &highest, login, player, &servParms);
		retval = select(highest+1, &rfds, NULL, NULL, &tv);
		
		if(retval)	/* data to be read */
		{	GetConsoleInput(player, &servParms);	/* read from stdin trhough the 0 socket */
			AcceptNewConn(listenfd, login, player, &servParms);	/* put it in the login queue */
			ReadFromLogin(login, player, &servParms);
			ReadFromPlayer(player, &servParms);
		}
		else if(retval == -1) /* error but continue execution anyway */
			perror("Unusual error in main select() call.\n");

		/* check for timeouts etc */
	}


	exit(0);
}


void sigint()
{
	signal(SIGINT, sigint);	/* some unices reset to the default action after a signal */
	println("Ctrl-c pressed, discarded. Please use \"/shutdown\"\n");
	println("Press ctrl-\\ to send SIGQUIT (not recommended)\n");

	return;
}


void sigquit()
{
	signal(SIGQUIT, sigquit);	/* some unices reset to the default action after a signal */
	println("Ctrl-\\ pressed, committing suicide...\n");
	CloseLogfile();
	exit(0);	/* normal program termination */
}
