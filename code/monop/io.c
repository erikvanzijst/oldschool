#include "globals.h"
#include "io.h"
#include "parsers.h"

extern int errno;
extern struct Output *outStream;

int GetConsoleInput(struct Player player[], struct ServParms *servParms)
{	int n;
	char msg[MAXLINE+1];

	if(!RecvReady(0))
		return 0;	/* there's no data to be read from the keyboard */

	n = read(0, msg, MAXLINE);
	msg[n] = 0;	/* terminate */
	ParsePlayer(player, -1, msg, servParms);	/* mangle it through the parser */
	return 0;
}


int println(char msg[])
{
	if(outStream->console)
		printf("%s", msg);
	if((outStream->logging)&&(outStream->fp != NULL))
		fprintf(outStream->fp, "%s", msg);
	return 0;
}


int OpenLogfile(void)
{	char temp[MAXLINE+1];
	if(strcmp(outStream->logFile, "") == 0)
	{	outStream->logging = 0;
		println("Unable to start logging: no logfile specified.\n");
		return 1;
	}
	else if( (outStream->fp = fopen((char *) outStream->logFile, "a+")) != NULL)	/* opened for appending */
	{	outStream->logging = 1;
		println("Logfile opened.\n");
	}
	else
	{	outStream->logging = 0;
		sprintf(temp, "Unable to open logfile: %s: %s\n", (char *) outStream->logFile, strerror(errno) );
		println(temp);
	}
	return 0;
}


int CloseLogfile(void)
{	if(outStream->logging)
	{	println("Closing logfile.\n");
		fclose(outStream->fp);
		outStream->fp = NULL;
	}
	return 0;
}
