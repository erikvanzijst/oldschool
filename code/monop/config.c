#include "config.h"
#include "globals.h"
#include "strings.h"

#define RCFILE ".monoprc"	/* name of the configfile which will be placed in the users homedir */

int ReadConfig(struct ServParms *servParms)
{
	char filename[MAXLINE+1];
	FILE *fp;

	if(getenv("HOME") == NULL)
	{	println("Unable to locate your home directory, ommitting config file.\n");
		return 1;
	}
	sprintf(filename, "%s/%s", (char *) getenv("HOME"), (char *) RCFILE);

	if( (fp = fopen(filename, "r")) != NULL)	/* see if the file exists */
	{	/* read the file */
		while(ReadEntry(fp, servParms, filename))	/* read all lines one by one */
			;
	}
	else if( (fp = fopen(filename, "w+")) != NULL)	/* write the current parameters to the file */
	{	DumpHeader(fp);
		WriteEntry(fp, "maxPlayers", servParms);
		WriteEntry(fp, "maxConns", servParms);
		WriteEntry(fp, "budget", servParms);
		WriteEntry(fp, "doubleIncomeOnStart", servParms);
		WriteEntry(fp, "port", servParms);
		WriteEntry(fp, "password", servParms);
	}
	else
	{	sprintf(filename, "Error opening %s/%s: %s\n", (char *) getenv("HOME"), (char *) RCFILE, (char *) strerror(errno) );
		println(filename);
		return 1;
	}

	fclose(fp);	/* close the file */
	return 0;
}


int ReadEntry(FILE *fp, struct ServParms *servParms, char filename[])
{	/* read and process one entry */
	int nX;
	char tmp_char;
	char line[MAXLINE+1];
	char key[MAXLINE+1];
	char value[MAXLINE+1];
	char printmsg[MAXLINE+1];

	nX = 0;

	while( (tmp_char = fgetc(fp)) != EOF)
	{	if( (tmp_char == '\n') || (tmp_char == '#') || (nX > MAXLINE) )
		{	if(tmp_char == '#')	/* flush to the end of the line */
			{	while( (tmp_char = fgetc(fp)) != '\n')
					if( tmp_char == EOF)
					{	sprintf(printmsg, "Make sure the config file (%s) is terminated with a carriage return (enter).\nTerminating...\n", filename);
						println(printmsg);
						exit(1);
					}
			}
			break;
		}
		else
			line[nX++] = tmp_char;
	}
	if(tmp_char == EOF) return 0; /* end of file, abort */
	if(nX > MAXLINE)	return 1;	/* almost buffer overflow, discard line */
	line[nX] = 0;	/* terminate */
	if( GetFirstWord(key, line) )
		return 1;	/* empty line */

	if( RemoveFirstWord(line) )
		return 1; /* no value specified */

	if( GetFirstWord(value, line) ) return 1;	/* no value */

	if( strstr(key, "maxPlayers") != NULL)
	{	nX = atoi(value);
		if((nX < 1)||(nX > 10))
		{	sprintf(printmsg, "Invalid amount of maximum players encountered in config file: %d\n(Hint: rm %s) Terminating...\n", nX, filename);
			println(printmsg);
			exit(1);
		}
		else	/* ok amount of players, put in the server structure */
			servParms->maxPlayers = nX;
	}

	if( strstr(key, "maxConns") != NULL)
	{	nX = atoi(value);
		if((nX < 1)||(nX > 40))
		{	sprintf(printmsg, "Invalid amount of maximum connections encountered in config file: %d\n(Hint: rm %s) Terminating...\n", nX, filename);
			println(printmsg);
			exit(1);
		}
		else	/* ok amount of connection, put in the server structure */
			servParms->maxConns = nX;
	}

	if( strstr(key, "budget") != NULL)
	{	nX = atoi(value);
		if((nX < 1)||(nX > 32767))	/* maximum of a signed integer */
		{	sprintf(printmsg, "Invalid budget encountered in config file: %d\n(Hint: rm %s) Terminating...\n", nX, filename);
			println(printmsg);
			exit(1);
		}
		else	/* ok amount of connection, put in the server structure */
			servParms->budget = nX;
	}

	if( strstr(key, "doubleIncomeOnStart") != NULL)
	{	nX = atoi(value);
		if((nX != 1)&&(nX != 0))	/* maximum of a signed integer */
		{	printf(printmsg, "Invalid value for doubleIncomeOnStart: %d\n(Hint: rm %s) Terminating...\n", nX, filename);
			println(printmsg);
			exit(1);
		}
		else
			servParms->doubleIncomeOnStart = nX;
	}

	if( strstr(key, "port") != NULL)
	{	nX = atoi(value);
		if((nX < 1)||(nX > 65535))	/* max unsigned int */
		{	sprintf(printmsg, "Invalid port number encountered in config file: %d\n(Hint: rm %s) Terminating...\n", nX, filename);
			println(printmsg);
			exit(1);
		}
		else	/* ok amount of connection, put in the server structure */
			servParms->listenPort = nX;
	}

	return 1;
}


int WriteEntry(FILE *fp, char key[], struct ServParms *servParms)
{	int nX;
	char line[MAXLINE+1];
	nX = 0;
	if( strstr(key, "maxPlayers") )
	{	sprintf(line, "maxPlayers %d\t# Maximum number of players; min = 2; max = 10; default 6\n\n", servParms->maxPlayers);
		fputs(line, fp);
	}
	if( strstr(key, "maxConns") )
	{	sprintf(line, "maxConns %d\t# Maximum number of connections (login + players); default 20\n\n", servParms->maxConns);
		fputs(line, fp);
	}
	if( strstr(key, "budget") )
	{	sprintf(line, "budget %d\t# Start money; default 1500\n\n", servParms->budget);
		fputs(line, fp);
	}
	if( strstr(key, "doubleIncomeOnStart") )
	{	sprintf(line, "doubleIncomeOnStart %d\t# receive 400 when landing on Start\n\n", servParms->doubleIncomeOnStart);
		fputs(line, fp);
	}
	if( strstr(key, "port") )
	{	sprintf(line, "port %d\t# Serverport; default 2525\n\n", servParms->listenPort);
		fputs(line, fp);
	}
	if( strstr(key, "password") )
	{	sprintf(line, "password %s\t# Password; no passwd by default (max 8 chars)\n\n", servParms->password);
		fputs(line, fp);
	}
	return 0;
}


int DumpHeader(FILE *fp)
{	char header[] = "#  Monop server config file.\n#\n"
									"# All the options can be overridden by specifying them on the commandline.\n"
									"# On the console it is possible to issue \"/save\" to force monop server to\n"
									"# dump all the current settings to this file.\n"
									"# Editing by hand here is also allowed.\n\n";
	fputs(header, fp);
	return 0;
}


void DisplayConfig(struct ServParms *servParms)
{	char msg[MAXLINE+1];
	sprintf(msg, "Game configuration\n"
					" maximum players: %d\n"
					" maximum connections: %d\n"
					" start money: %d\n"
					" listening for connections on port: %d\n"
					" password on the server: \"%s\"\n", servParms->maxPlayers,
					servParms->maxConns, servParms->budget, servParms->listenPort,
					servParms->password);
	println(msg);
	if(servParms->doubleIncomeOnStart) println(" double income on start: enabled\n");
	else println(" double income on start: disabled\n");
	return;
}
