#include "globals.h"
#include "commands.h"
#include "strings.h"

int StripCR(char msg[])	/* find and remove trailing newlines and carriage returns */
{	int stop;

	stop = 0;

	while(!stop)
		if( (msg[strlen(msg)-1]	== '\r') || (msg[strlen(msg)-1] == '\n') )
			msg[strlen(msg)-1] = 0;	/* terminate the string */
		else
			stop = 1;

	return 0;
}


int GetCommand(char msg[])	/* retrieve the "/" command, return NULL on error */
{	int count;
	char *temp;
	count = 0;

	temp = (char *) malloc(strlen(msg)+1);
	while( (msg[count] != ' ') && (msg[count] != 0) )
	{	temp[count] = msg[count];
		count++;
	}
	if(count == 0)
	{	free(temp);
		return UNKNOWN;
	}
	temp[count] = 0;

	if( strcmp( UpperCase(temp, temp), NAMESTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return NAME;
	}
	if( strcmp( UpperCase(temp, temp), EMAILSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return EMAIL;
	}
	if( strcmp( UpperCase(temp, temp), NICKSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return NICK;
	}
	if( strcmp( UpperCase(temp, temp), TOKENSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return TOKEN;
	}
	if( strcmp( UpperCase(temp, temp), FQDNSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return FQDN;
	}
	if( strcmp( UpperCase(temp, temp), IPNRSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return IPNR;
	}
	if( strcmp( UpperCase(temp, temp), NAMESSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return NAMES;
	}
	if( strcmp( UpperCase(temp, temp), QUITSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return QUIT;
	}
	if( strcmp( UpperCase(temp, temp), CHATSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return CHAT;
	}
	if( strcmp( UpperCase(temp, temp), MSGSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return MSG;
	}
	if( strcmp( UpperCase(temp, temp), BUDGETSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return BUDGET;
	}
	if( strcmp( UpperCase(temp, temp), READYSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return READY;
	}
	if( strcmp( UpperCase(temp, temp), DICESTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return DICE;
	}
	if( strcmp( UpperCase(temp, temp), POSSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return POS;
	}
	if( strcmp( UpperCase(temp, temp), PROPERTYSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return PROPERTY;
	}
	if( strcmp( UpperCase(temp, temp), SHUTDOWNSTR) == 0 )
	{	RemoveFirstWord(msg);	/* strip the first word */
		free(temp);
		return SHUTDOWN;
	}

	free(temp);
	return UNKNOWN;
}


int GetSubCommand(char msg[])
{	char *temp;

	temp = (char *) malloc(strlen(msg) + 1);
	if( GetFirstWord(temp, msg) )
	{	return UNKNOWN;
		free(temp);
	}
	if(strcmp( UpperCase(temp, temp), "GET") == 0)
	{	RemoveFirstWord(msg);
		free(temp);
		return GET;
	}
	if(strcmp( UpperCase(temp, temp), "SET") == 0)
	{	RemoveFirstWord(msg);
		free(temp);
		return SET;
	}
	if(strcmp( UpperCase(temp, temp), "HIDE") == 0)
	{	RemoveFirstWord(msg);
		free(temp);
		return HIDE;
	}
	if(strcmp( UpperCase(temp, temp), "ROLL") == 0)
	{	RemoveFirstWord(msg);
		free(temp);
		return ROLL;
	}
	if(strcmp( UpperCase(temp, temp), "BUY") == 0)
	{	RemoveFirstWord(msg);
		free(temp);
		return BUY;
	}
	if(strcmp( UpperCase(temp, temp), "MORTGAGE") == 0)
	{	RemoveFirstWord(msg);
		free(temp);
		return MORTGAGE;
	}

	return UNKNOWN;
}


int GetFirstWord(char temp[], char msg[])	/* destination, source */
{	int nX;

	nX = 0;
	if( (strlen(msg) == 0) || ( msg[0] == ' ' ) )
		return 1;	/* error no word at the start */

	while( ( nX < strlen(msg) )&&(msg[nX] != ' ') )
	{	temp[nX] = msg[nX];
		nX++;
	}
	temp[nX] = 0;	/* terminate */

	return 0;
}


int RemoveFirstWord(char msg[])
{	int nX, count, mX;
	char *temp;

	count = mX = 0;
	temp = (char *) malloc( strlen(msg) + 1);
	if( (strlen(msg) == 0) || ( msg[0] == ' ' ) )
	{	free(temp);
		return 1;	/* error no word at the start */
	}

	strcpy(temp, msg);	/* copy msg to temp */

	while(( msg[count] != ' ' )&&(count < strlen(msg)))	/* first find out where the first word ends */
		count++;
	while(( msg[count] == ' ' )&&(count < strlen(msg)))	/* find out where the second word starts */
		count++;
	if(count >= strlen(msg))
	{	msg[0] = 0;
		free(temp);
		return 1;	/* error, no 2nd word present */
	}

	for(nX = count; nX < strlen(temp)+1; nX++)
		msg[mX++] = temp[nX];

	free(temp);
	return 0;
}


char * UpperCase(char dest[], char msg[])	/* converts a string to uppercase */
{	int nX;	/* assumes there's enough space in the destination array! */

	for(nX = 0; nX < strlen(msg); nX++)
		dest[nX] = toupper(msg[nX]);
	dest[nX] = 0;	/* terminate, strlen() doesn't count the termination */
	return dest;
}


int GetFirstString(char dest[], char msg[])	/* finds multiple commands in one string */
{	int nX, mX, iX, second;

	if(strlen(msg) < 1) return 0;
	second = mX = iX = 0;
	for(nX = 0; nX < strlen(msg); nX++)
	{	if((msg[nX] == '/')&&(msg[nX+1] != '/'))	/* find the single slashes */
		{	if(second)	/* this is the second slash */
			{	for(mX = 0; mX < nX; mX++)
					dest[mX] = msg[mX];
				dest[mX] = 0;	/* terminate */
				while(mX < strlen(msg))	/* cut the first string out of the original string */
					msg[iX++] = msg[mX++];
				msg[iX] = 0;	/* terminate */
				return 1;	/* tell the caller we got a result string */
			}
			else second++;
		}
		if((msg[nX] == '/')&&(msg[nX+1] == '/'))	/* if there are 2 slashes */
			nX++;	/* don't check the 2nd slash */
	}
	if(!second)	/* not even 1 slash */
		return 0;	/* tell the caller we did nothing */
	else	/* there's only 1 command in this string, which is normal */
	{	strcpy(dest, msg);
		msg[0] = 0;	/* empty the original string */
		return 1;	/* we have result */
	}
	return 0;
}


char * ByteStuffing(char msg[], int operation)
{	int nX, mX;
	char *cp;

	mX = 0;
	cp = (char *) malloc(strlen(msg) + 2);
	switch (operation)
	{	case DESTUFF:	/* remove double slashes */
			for(nX = 0; nX < strlen(msg); nX++)
			{	if( (msg[nX] == '/') && (msg[nX+1] == '/') )	/* if there's a double slash */
					msg[mX++] = msg[nX++];
				else
					msg[mX++] = msg[nX];
			}
			msg[mX] = 0;	/* learn to fucking terminate */
			break;
		case STUFF:	/* add double slashes */
			mX = 1;
			cp[0] = msg[0];	/* don't stuff the first slash */
			for(nX = 1; nX < strlen(msg); nX++)
				if( msg[nX] == '/' )
				{	cp = (char *) realloc(cp, mX+4);
					cp[mX++] = '/';
					cp[mX++] = '/';
				}
				else
				{	cp = (char *) realloc(cp, mX+4);
					cp[mX++] = msg[nX];
				}
			cp[mX] = 0;	/* manual terminate */
			msg = (char *) realloc(msg, mX+5);
			/* crappy code: no overflow check */
			strcpy(msg, cp);
			break;
	}
	free(cp);
	return msg;
}
