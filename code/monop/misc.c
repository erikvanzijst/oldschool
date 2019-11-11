#include "globals.h"
#include "strings.h"

int NickVal(char nick[], int operation, struct Player player[], struct ServParms *servParms)
{	int nX;	/* nick validation function */
	char temp[MAXNICKLEN+1];
	char temp2[MAXNICKLEN+1];
	switch (operation)
	{
		case AVAIL:	/* check if the nick is already in use */
			for(nX = 0; nX < servParms->maxPlayers; nX++)
				if( ( strcmp(UpperCase(temp2, player[nX].nick), UpperCase(temp, nick)) == 0 ) && ( player[nX].fd > 0 ) )
					return 0;	/* rejected */
			return 1;

		case VALID:	/* check for illegal chars, length etc */
			if( ((strlen(nick) < 1)||(strlen(nick) > MAXNICKLEN)) || (strchr(nick, ' ') != NULL) )
				return 0;	/* invalid length or space detected */
			else
				return 1;

		default:	/* undefined situation, reject for security reasons */
			return 0;
	}

	return 0;
}


int TokenVal(int token, int operation, struct Player player[], struct ServParms *servParms)
{	int nX;

	switch (operation)
	{	case AVAIL:
			for(nX = 0; nX < servParms->maxPlayers; nX++)
				if( player[nX].token == token )	/* token in owned by another player */
					return 1;
			break;
		case VALID:
			if( (token < 0) || (token > 9) )
				return 1;	/* token out of range */
			break;
		default:
			return 1;	/* return error */
	}

	return 0;	/* token is ok */
}


int GenToken(struct Player player[], struct ServParms *servParms)
{	int mX, nX, token, exist;	/* return the first free token */
	token = 0;	/* player can always change his token */

	for(mX=0; mX < servParms->maxPlayers; mX++)
	{	exist = 0;
		for(nX = 0; nX < servParms->maxPlayers; nX++)
		{	if( player[nX].token == token )	/* token already in use by someone */
			{	exist = 1;
				break;
			}
		}
		if(!exist) break;
		else token++;
	}

	return token;	/* return first available token */
}


int GetPlayerByNick(struct Player player[], struct ServParms *servParms, char nick[])
{	int nX;	/* get the number of a certain player */
	char temp[MAXNICKLEN+1];
	char temp2[MAXNICKLEN+1];

	if( strlen(nick) > MAXNICKLEN )	return -1;	/* nick to long */
	for( nX = 0; nX < servParms->maxPlayers; nX++ )
		if(strcmp( UpperCase(temp, nick), UpperCase(temp2, player[nX].nick)) == 0)
			return nX;	/* found it */
	return -1;
}


char * MakeUserList(char list[], struct Player player[], struct ServParms *servParms)
{	int nX;
	strcpy(list, "/names");	/* begin of the string to send to a player */
	for( nX = 0; nX < servParms->maxPlayers; nX++)
		if( player[nX].fd > 0 )	/* all connected players */
			sprintf(list, "%s %s", list, player[nX].nick);	/* are being added */
	return list;	/* return the string */
}


int SayWhat(struct Player player[], int nX)
{
	char saywhat[] = "Say what??\r\n";
	Tell(player[nX].fd, saywhat);
	return 0;
}


int TotalPlayers(struct Player player[], struct ServParms *servParms)
{	int nX, count;
	count = 0;
	for(nX = 0; nX < servParms->maxPlayers; nX++)
		if(player[nX].fd > -1)
			count++;

	return count;
}


int AllReady(struct Player player[], struct ServParms *servParms)
{	int nX, allready, count;
	allready = count = 0;
	for(nX = 0; nX < servParms->maxPlayers; nX++)
		if(player[nX].ready == 1)
			count++;
	if(count == TotalPlayers(player, servParms))
		return 1;	/* all players are ready to start the game */
	else
		return 0;	/* at least one is not yet ready */
}
