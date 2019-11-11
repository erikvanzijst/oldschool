#include "parsers.h"
#include "globals.h"
#include "network.h"
#include "misc.h"
#include "strings.h"
#include "commands.h"
#include "game.h"
#include "io.h"

extern int errno;

int ParseLogin(struct Login login[], int nX, char msg[], struct Player player[], struct ServParms *servParms)
{	char temp[MAXLINE+1];
	int number;
	StripCR(msg);

	switch (login[nX].stage)
	{	case W8FORLOGIN:
			if(!NickVal((char *)msg, VALID, player, servParms))
			{	RemoveLogin( login, nX, NICKINVAL );
				break;
			}
			if(!NickVal(msg, AVAIL, player, servParms))
			{	RemoveLogin( login, nX, (char *) NICKINUSE );
				break;
			}

			strcpy( login[nX].nick, (char *) msg );	/* catch the nickname */
			Tell( login[nX].fd, (char *) PASSWDMSG );	/* display the password prompt */
			login[nX].stage = W8FORPASSWD;
			break;
		case W8FORPASSWD:
			if( strcmp( servParms->password, (char *) msg ) != 0)	/* passwd incorrect */
			{	Tell( (int) login[nX].fd, (char *) INCORRECTPASS );
				if( ++login[nX].retry > MAXRETRIES-1 )
				{	RemoveLogin(login, nX, (char *) "");	/* disconnect */
					break;
				}
				Tell( login[nX].fd, (char *) PASSWDMSG );	/* display the password prompt */
			}
			else
			{	/* passwords is correct */
				if( (number = AddToPlayer(login, nX, player, servParms)) != -1)	/* new user becomes a player */
				{	MakeUserList( (char *) temp, player, servParms);	/* make userlist to inform players */
					sprintf( (char *) temp, "%s\r\n", (char *) temp);
					BroadCast(player, servParms, (char *) temp);	/* broadcast the new userlist */
					sprintf(temp, "%s has joined the game.\n", (char *) player[number].nick);
					println(temp);
				}
			}
			break;
		default:
			break;
	}

	return 0;
}


int ParsePlayer(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	/* is this a clean parser or what */
	StripCR(msg);

	if(msg[0] != '/') return 0;

	switch( GetCommand(msg) )
	{	case NAME:
			ParserName(player, nX, msg, servParms);
			break;
		case EMAIL:
			ParserEmail(player, nX, msg, servParms);
			break;
		case NICK:
			ParserNick(player, nX, msg, servParms);
			break;
		case TOKEN:
			ParserToken(player, nX, msg, servParms);
			break;
		case FQDN:
			ParserFqdn(player, nX, msg, servParms);
			break;
		case IPNR:
			ParserIpnr(player, nX, msg, servParms);
			break;
		case NAMES:
			ParserNames(player, nX, msg, servParms);
			break;
		case QUIT:
			ParserQuit(player, nX, msg, servParms);
			break;
		case CHAT:
			ParserChat(player, nX, msg, servParms);
			break;
		case MSG:
			ParserMsg(player, nX, msg, servParms);
			break;
		case BUDGET:
			ParserBudget(player, nX, msg, servParms);
			break;
		case READY:
			ParserReady(player, nX, msg, servParms);
			break;
		case DICE:
			ParserDice(player, nX, msg, servParms);
			break;
		case PROPERTY:
			ParserProperty(player, nX, msg, servParms);
			break;
		case POS:
			ParserPos(player, nX, msg, servParms);
			break;
		case SHUTDOWN:
			ParserShutdown(player, nX, msg, servParms);
			break;
		case UNKNOWN:
			ParserUnknown(player, nX, msg, servParms);
			break;
		default:
			break;
	}

	return 0;
}


int ParserName(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char temp[MAXLINE+1];
	int number;

	if(nX == -1)	/* coming from the console */
		return 0;

	switch( GetSubCommand(msg) )
	{	case GET:
			if( GetFirstWord((char *)temp, (char *)msg) ) return 1;	/* no nick specified */
			if( (number = GetPlayerByNick(player, servParms, (char *)temp)) == -1 ) return 1;	/* unknown player specified */
			sprintf(temp, "/name %s %s\r\n", (char *)player[number].nick, (char *)player[number].realname);
			Tell(player[nX].fd, temp);
			break;
		case SET:
			strcpy(player[nX].realname, (char *)msg);
			sprintf(temp, "%s set realname to %s.\n", (char *)player[nX].nick, (char *)player[nX].realname);
			println(temp);
			sprintf(temp, "/name %s %s\r\n", (char *)player[nX].nick, (char *)player[nX].realname);
			BroadCast(player, servParms, (char *)temp);	/* send this to all players */
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}

	return 0;	/* normal termination */
}


int ParserEmail(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char temp[MAXLINE+1];
	int number;

	if(nX == -1)	/* coming from the console */
		return 0;

	switch( GetSubCommand(msg) )
	{	case GET:
			if( GetFirstWord(temp, msg) ) return 1;	/* no nick specified */
			if( (number = GetPlayerByNick(player, servParms, (char *)temp)) == -1 ) return 1;	/* unknown player specified */
			sprintf(temp, "/email %s %s\r\n", player[number].nick, player[number].email);
			Tell(player[nX].fd, temp);
			break;
		case SET:
			strcpy(player[nX].email, msg);
			sprintf(temp, "%s set email to %s.\n", (char *)player[nX].nick, (char *)player[nX].email);
			println(temp);
			sprintf(temp, "/email %s %s\r\n", (char *)player[nX].nick, (char *)player[nX].email);
			BroadCast(player, servParms, temp);	/* send this to all players */
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}

	return 0;	/* normal termination */
}


int ParserNick(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char temp[MAXLINE+1];
	char tempNick[MAXNICKLEN+1];

	if(nX == -1)	/* coming from the console */
		return 0;

	switch( GetSubCommand(msg) )
	{	case GET:	/* player wants an echo of it's own nick */
			sprintf(temp, "/nick %s\r\n", player[nX].nick);
			Tell(player[nX].fd, temp);
			break;
		case SET:
			if( GetFirstWord(temp, msg) ) return 1;	/* no nick specified */
			if( NickVal(temp, VALID, player, servParms) == 0)	/* invalid nick */
			{	Tell(player[nX].fd, "/nick error invalid\r\n");
				break;
			}
			if( NickVal(temp, AVAIL, player, servParms) == 0)	/* nick in use */
			{	Tell(player[nX].fd, "/nick error inuse\r\n");
				break;
			}
			strcpy(tempNick, player[nX].nick);	/* backup the old nick */
			strcpy(player[nX].nick, temp);	/* activate the nick */
			sprintf(temp, "/nick %s %s\r\n", tempNick, player[nX].nick);
			BroadCast(player, servParms, temp);	/* tell everyone */
			sprintf(temp, "%s is now known as %s.\n", tempNick, player[nX].nick);	/* dump to the console */
			println(temp);
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}
	return 0;
}


int ParserToken(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char temp[MAXLINE+1];
	int number;

	if(nX == -1)	/* coming from the console */
		return 0;

	switch( GetSubCommand(msg) )
	{	case GET:	/* request for some player's token number */
			if( GetFirstWord(temp, msg) ) return 1;	/* no nick specified */
			if( (number = GetPlayerByNick(player, servParms, temp)) == -1 ) return 1;	/* unknown player specified */
			sprintf(temp, "/token %s %d\r\n", player[number].nick, player[number].token);
			Tell(player[nX].fd, temp);
			break;
		case SET:	/* player wants another token */
			if( GetFirstWord(temp, msg) ) return 1;	/* no token number specified */
			if(!isdigit(temp[0]))	/* the user typed a character, not a digit */
			{	Tell(player[nX].fd, "/token error invalid\r\n");
				break;
			}
			number = atoi(temp);
			if( TokenVal(number, VALID, player, servParms) )	/* invalid token number */
			{	Tell(player[nX].fd, "/token error invalid\r\n");
				break;
			}
			if( TokenVal(number, AVAIL, player, servParms) )	/* token in use */
			{	Tell(player[nX].fd, "/token error inuse\r\n");
				break;
			}
			player[nX].token = number;	/* activate new token number */
			sprintf(temp, "%s took token %d.\n", player[nX].nick, player[nX].token);
			println(temp);
			sprintf(temp, "/token %s %d\r\n", player[nX].nick, player[nX].token);
			BroadCast(player, servParms, temp);
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}

	return 0;
}


int ParserFqdn(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char temp[MAXLINE+1];
	int number;

	if(nX == -1)	/* coming from the console */
		return 0;

	switch( GetSubCommand(msg) )
	{	case GET:	/* request for some player's domain name */
			if( GetFirstWord(temp, msg) ) return 1;	/* no nick specified */
			if( (number = GetPlayerByNick(player, servParms, temp)) == -1 ) return 1;	/* unknown player specified */
			sprintf(temp, "/fqdn %s %s\r\n", player[number].nick, player[number].fqdn);
			Tell(player[nX].fd, temp);
			break;
		case SET:	/* one's domain name cannot be changed */
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}

	return 0;
}


int ParserIpnr(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char temp[MAXLINE+1];
	int number;

	if(nX == -1)	/* coming from the console */
		return 0;

	switch( GetSubCommand(msg) )
	{	case GET:	/* request for some player's ip number (dotted decimal) */
			if( GetFirstWord(temp, msg) ) return 1;	/* no nick specified */
			if( (number = GetPlayerByNick(player, servParms, temp)) == -1 ) return 1;	/* unknown player specified */
			sprintf(temp, "/ipnr %s %s\r\n", player[number].nick, player[number].ipnr);
			Tell(player[nX].fd, temp);
			break;
		case SET:	/* one's ip number cannot be changed */
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}
	return 0;
}


int ParserNames(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char temp[MAXLINE+1];
	int number;

	if(nX == -1)	/* coming from the console */
	{	MakeUserList(temp, player, servParms);	/* put all nicknames in a string */
		sprintf(temp, "Userlist: %s\n", temp);
		println(temp);
		return 0;
	}

	switch( GetSubCommand(msg) )
	{	case GET:	/* request for the list of nicknames */
			MakeUserList(temp, player, servParms);	/* put all nicknames in a string */
			sprintf(temp, "%s\r\n", temp);
			Tell(player[nX].fd, temp);
			break;
		case SET:	/* userlists can not be set by users */
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}

	return 0;
}


int ParserChat(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char *tmp;	/* function to forward chat text to everyone */
	if(strlen(msg) == 0)	/* empty line */
		return 0;

	if(nX == -1)	/* coming from the console */
		return 0;

	tmp = (char *) malloc( strlen(msg) + 3 + strlen(CHATSTR) + MAXNICKLEN + MAXLINE);
	if( tmp == NULL )
	{	sprintf(tmp, "Error in malloc() in function ParserChat(): %s\n", strerror(errno) );
		println(tmp);
		free(tmp);	/* useless, i know */
		exit(1);	/* too serious to continue execution */
	}
	sprintf(tmp, "[chat] <%s> %s\n", player[nX].nick, msg);	/* to the console */
	println(tmp);
	sprintf(tmp, "/chat %s %s\r\n", player[nX].nick, msg);
	BroadCast(player, servParms, tmp);	/* to all players */
	free(tmp);
	return 0;
}


int ParserMsg(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char *tmp;
	int number;

	tmp = (char *) malloc( strlen(msg) + 3 + strlen(MSGSTR) + MAXNICKLEN + 20);
	if( GetFirstWord(tmp, msg) )
	{	free(tmp);
		return 1;	/* no nick specified */
	}
	if( (number = GetPlayerByNick(player, servParms, tmp)) == -1 )
	{	free(tmp);
		return 1;	/* unknown player specified */
	}
	RemoveFirstWord(msg);	/* now only the message remains */	
	if(strlen(msg) == 0)	/* empty line */
	{	free(tmp);
		return 0;
	}
	sprintf(tmp, "[msg] <%s> to <%s> %s\n", player[nX].nick, player[number].nick, msg);	/* to the console */
	println(tmp);
	sprintf(tmp, "/msg %s %s\r\n", player[nX].nick, msg);
	Tell(player[number].fd, tmp);	/* forward to the receiver */
	free(tmp);
	return 0;
}


int ParserBudget(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char *temp;	/* to be alloc-ed later */
	int number;

	if(nX == -1)	/* coming from the console */
		return 0;

	temp = (char *) malloc( strlen(msg) + MAXNICKLEN + 2);
	switch( GetSubCommand(msg) )
	{	case GET:	/* request for someone's buget */
			if( GetFirstWord(temp, msg) )
			{	free(temp);
				return 1;	/* no nick specified */
			}
			if( (number = GetPlayerByNick(player, servParms, temp)) == -1 )
			{	free(temp);
				return 1;	/* unknown player specified */
			}
			if(( !player[number].hidebudget )||( nX == number ))	/* ok to display */
			{	sprintf(temp, "/budget %s %d\r\n", player[number].nick, player[number].budget);
				Tell(player[nX].fd, temp);
			}
			else	/* don't display */
			{	sprintf(temp, "/budget %s -1\r\n", player[number].nick);
				Tell(player[nX].fd, temp);
			}
			break;
		case HIDE:	/* toggle hide/unhide of your money */
			if( GetFirstWord(temp, msg) )
			{	free(temp);
				return 1;	/* 1 or 0 specified */
			}
			if(!isdigit(temp[0]))	/* the user typed a character, not a digit */
			{	free(temp);
				return 1;	/* 1 or 0 specified */
			}
			number = atoi(temp);
			if(number == 0)	/* show it */
			{	player[nX].hidebudget = number;
				println(player[nX].nick);
				println(" lays all money out in the open for everyone to see.\n");
				sprintf(temp, "/budget %s %d\r\n", player[nX].nick, player[nX].budget);
				BroadCast(player, servParms, temp);	/* make it popup on all clients */
			}
			if(number == 1)	/* hide it */
			{	player[nX].hidebudget = number;
				println(player[nX].nick);
				println(" covers his money.\n");
				sprintf(temp, "/budget %s %d\r\n", player[nX].nick, -1);
				BroadCast(player, servParms, temp);	/* make it hidden on all clients */
			}
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}

	free(temp);
	return 0;
}


int ParserReady(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char *temp;	/* to be alloc-ed later */
	int number;

	if(nX == -1)	/* coming from the console */
		return 0;

	temp = (char *) malloc( strlen(msg) + MAXNICKLEN + 2);
	switch( GetSubCommand(msg) )
	{	case GET:	/* request for someone's ready status */
			if( GetFirstWord(temp, msg) )	/* get the specified nick */
			{	free(temp);
				return 1;	/* no nick specified */
			}
			if( (number = GetPlayerByNick(player, servParms, temp)) == -1 )
			{	free(temp);
				return 1;	/* unknown player specified */
			}
			temp = (char *) realloc(temp, MAXLINE+1);
			sprintf(temp, "/ready %s %d\r\n", player[number].nick, player[number].ready);
			Tell(player[nX].fd, temp);
			break;
		case SET:	/* toggle from or to ready status */
			if( !servParms->running )	/* waiting for other players */
			{	if( GetFirstWord(temp, msg) )
				{	free(temp);
					return 1;	/* nothing specified */
				}
				if(!isdigit(temp[0]))	/* the user typed a character, not a digit */
				{	free(temp);
					return 1;	/* no 1 or 0 specified */
				}
				number = atoi(temp);
				if((number == 1)&&(player[nX].ready == 0))	/* you're ready to start */
				{	player[nX].ready = 1;
					println(player[nX].nick);
					println(" is ready to go.\n");
					temp = (char *) realloc(temp, MAXLINE+1);
					sprintf(temp, "/ready %s 1\r\n", player[nX].nick);
					BroadCast(player, servParms, temp);
					if(AllReady(player, servParms))
						StartGame(servParms, player);	/* game starts */
				}
				else if((number == 0)&&(player[nX].ready == 1))	/* changed your mind */
				{	player[nX].ready = 0;
					println(player[nX].nick);
					println(" is no longer ready to go.\n");
					temp = (char *) realloc(temp, MAXLINE+1);
					sprintf(temp, "/ready %s 0\r\n", player[nX].nick);
					BroadCast(player, servParms, temp);
				}
			}
			else	/* game is running, players is is done rolling */
			{	if((nX == servParms->currentPlayer)&&( ReadyOK(player, servParms, nX )))
				{	player[nX].rolled = player[nX].doubles = 0;
					servParms->currentPlayer = NextPlayer(player, servParms, servParms->currentPlayer);
					temp = realloc(temp, 10 + MAXNICKLEN);	/* make sure there's enough space */
					sprintf(temp, "/turn %s\r\n", player[servParms->currentPlayer].nick);
					BroadCast(player, servParms, temp);
					println(player[servParms->currentPlayer].nick);
					println("'s turn now.\n");
				}
			}
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}
	free(temp);
	return 0;
}


int ParserDice(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char *temp;	/* to be alloc-ed later */
	int number;

	if(nX == -1)	/* coming from the console */
		return 0;

	temp = (char *) malloc( strlen(msg) + 3 + strlen(DICESTR) + MAXNICKLEN + 30);
	switch( GetSubCommand(msg) )
	{	case ROLL:
			if( !AllowedToRoll(player, nX, servParms) )
			{	free(temp);
				return 1;
			}
			RollDice(&servParms->dice);
			sprintf( temp, "/dice %s %d:%d\r\n", player[nX].nick, servParms->dice.stone1, servParms->dice.stone2);
			BroadCast(player, servParms, temp);
			sprintf(temp, "%s rolls %d (%d + %d).\n", player[nX].nick, (servParms->dice.stone1 + servParms->dice.stone2), servParms->dice.stone1, servParms->dice.stone2);
			println(temp);
			++player[nX].rolled;
			if( servParms->dice.stone1 == servParms->dice.stone2 ) ++player[nX].doubles;	/* doubles */
			/* if doubles is 3 now, send the sucker to jail, do not continue */
			if((servParms->dice.stone1 + servParms->dice.stone2 + player[nX].pos) == 40)	/* landed on start */
			{	if( servParms->doubleIncomeOnStart )
				{	player[nX].budget = player[nX].budget + 400;
					sprintf(temp, "/budget %s %d\r\n", player[nX].nick, player[nX].budget);	/* notify, budget changed */
					Tell(player[nX].fd, temp);
				}
			}
			else if((servParms->dice.stone1 + servParms->dice.stone2 + player[nX].pos) > 40)	/* passed start */
			{	player[nX].budget = player[nX].budget + 200;
				sprintf(temp, "/budget %s %d\r\n", player[nX].nick, player[nX].budget);	/* notify, budget changed */
				Tell(player[nX].fd, temp);
			}
			player[nX].pos = (player[nX].pos + servParms->dice.stone1 + servParms->dice.stone2)%40;	/* new position */
			sprintf(temp, "%s lands on street %d.\n", player[nX].nick, player[nX].pos);
			println(temp);
			BroadCastPos(player, servParms, nX);	/* tell everyone the new location */
			DoStreet(player, servParms, nX);
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}

	free(temp);
	return 0;
}


int ParserProperty(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char *temp;	/* to be alloc-ed later */
	int number;

	if(nX == -1)	/* coming from the console */
		return 0;

	temp = (char *) malloc( strlen(PROPERTYSTR) + MAXNICKLEN + 15);
	switch( GetSubCommand(msg) )
	{	case BUY:	/* buy the current property */
			if( servParms->currentPlayer != nX ) break;	/* not your turn */
			if( servParms->property[player[nX].pos].type != STREET && servParms->property[player[nX].pos].type != STATION  && servParms->property[player[nX].pos].type != UTILITY )
				break;	/* property not for sale */
			if( servParms->property[player[nX].pos].owner > -1 )	/* already owned */
				break;
			if( player[nX].budget < servParms->property[player[nX].pos].price )	/* can't afford */
				break;
			player[nX].budget = player[nX].budget - servParms->property[player[nX].pos].price;
			servParms->property[player[nX].pos].owner = nX;
			sprintf(temp, "%s bought street %d.\n", player[nX].nick, player[nX].pos);
			println(temp);
			sprintf(temp, "/property buy %s %d\r\n", player[nX].nick, player[nX].pos);
			BroadCast(player, servParms, temp);
			break;
		case MORTGAGE:
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}

	free(temp);
	return 0;
}


int ParserPos(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char *temp;	/* to be alloc-ed later */
	int number;

	if(nX == -1)	/* coming from the console */
		return 0;

	temp = (char *) malloc(strlen(msg) + 10);
	switch( GetSubCommand(msg) )
	{	case GET:	/* retrieve someone's position */
			if( GetFirstWord(temp, msg) )	/* get the specified nick */
				break;	/* no nick specified */
			if( (number = GetPlayerByNick(player, servParms, temp)) == -1 )
				break;	/* unknown player specified */
			temp = (char *) realloc(temp, strlen(POSSTR) + MAXNICKLEN + 10);
			sprintf(temp, "/pos %s %d\r\n", player[number].nick, player[number].pos);
			Tell(player[nX].fd, temp);
			break;
		case UNKNOWN:
			break;
		default:
			break;
	}

	free(temp);
	return 0;
}


int ParserQuit(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char temp[MAXLINE+1];
	int number;

	if(nX == -1)	/* coming from the console */
		return 0;

	if( strlen(msg) )	/* reason specified */
	{	sprintf(temp, "/quit %s %s\r\n", player[nX].nick, msg);
		sprintf(temp, "Signoff %s (%s)\n", player[nX].nick, msg);
		println(temp);
		RemovePlayer(player, servParms, nX, msg);	/* physically disconnect */
		BroadCast(player, servParms, temp);	/* show everyone the quit message */
	}
	else	/* no reason specified */
	{	sprintf(temp, "/quit %s\r\n", player[nX].nick);
		sprintf(temp, "Signoff %s (%s has no reason)\n", player[nX].nick, player[nX].nick );
		println(temp);
		RemovePlayer(player, servParms, nX, "");	/* physically disconnect */
		BroadCast(player, servParms, temp);	/* show everyone the quit message */
	}

	return 0;
}


int ParserShutdown(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char *temp;

	if( strlen(msg) == 0 )
		strcpy(msg, "no reason");
	temp = (char *) malloc( strlen(msg) + 40 );

	if( (( nX <= 0) && (player[nX].flags == ROOT)) || (nX == -1))	/* killing server */
	{	sprintf(temp, "Server shutdown (%s)\r\n", msg);
		sprintf(temp, "*** Shutting down the server (%s).\n", msg);
		println(temp);
		BroadCast(player, servParms, temp);
		free(temp);
		CloseLogfile();
		exit(0);	/* normal exit */
	}
}


int ParserUnknown(struct Player player[], int nX, char msg[], struct ServParms *servParms)
{	char temp[MAXLINE+1];
	if(nX == -1)	/* coming from the console */
	{	sprintf(temp, "*** Invalid command: %s\n", msg);
		println(temp);
		return 0;
	}

	printf(temp, "%s issued an unknown command (%s).\n", player[nX].nick, msg);
	println(temp);
	SayWhat(player, nX);
	return 0;
}
