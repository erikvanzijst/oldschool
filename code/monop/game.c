#include "globals.h"
#include "misc.h"
#include "game.h"
#include "cards.h"

int StartGame(struct ServParms *servParms, struct Player player[])
{	/* player order is as the order in /names */
	char temp[MAXLINE+1];
	if(( TotalPlayers(player, servParms) < 2)||( TotalPlayers(player, servParms) > servParms->maxPlayers))
		return 0;	/* not enough players yet, need at least two */

	servParms->running = 1;	/* game in session */
	servParms->currentPlayer = NextPlayer(player, servParms, -1);
	BroadCast(player, servParms, "/startgame\r\n");	/* notify all */
	println("Game starts with the current players, no new logins possible.\n");
	return 0;
}


int NextPlayer(struct Player player[], struct ServParms *servParms, int current)
{	int nX;	/* returns the number of the next player to roll the dice */

	for( nX = 0; nX < servParms->maxPlayers; nX++)
		if( player[nX].fd > -1 )	/* cycle the connected players */
			if(( nX > current )&&( current != -1 ))	/* if there is a next player */
				return nX;	/* return its number */

	for( nX = 0; nX < servParms->maxPlayers; nX++)
		if( player[nX].fd > -1 )	/* cycle the connected players */
			return nX;	/* return the first player */

	println("No next player could be found. Terminating...\n");
	exit(1);	/* strange error, should never occur */
}


int AllowedToRoll(struct Player player[], int nX, struct ServParms *servParms)
{	if( !servParms->running ) return 0;	/* the game hasn't started yet */
	if( nX != servParms->currentPlayer ) return 0;	/* not your turn */
	if( player[nX].rolled == 0 ) return 1;	/* player hasn't rolled yet */
	if( player[nX].doubles >= 3 ) return 0;	/* 3 doubles, you should already be in jail */
	if( player[nX].rolled > player[nX].doubles ) return 0;
	return 1;	/* fine to roll */
}


int RollDice(struct Dice *dice)	/* get 2 random numbers */
{	dice->stone1 = 1 + (int) (6.0 * rand()/(RAND_MAX + 1.0 ));
	dice->stone2 = 1 + (int) (6.0 * rand()/(RAND_MAX + 1.0 ));
	return 0;
}


int ReadyOK(struct Player player[], struct ServParms *servParms, int nX)
{	if( nX != servParms->currentPlayer ) return 0;	/* not your turn */
	if( player[nX].rolled == 0) return 0;	/* you haven't rolled the dice yet */
	if(( player[nX].rolled < 3 )&&( player[nX].rolled == player[nX].doubles )) return 0;	/* you had doubles */
	return 1;
}


int DoStreet(struct Player player[], struct ServParms *servParms, int nX)
{	/* player stepped on a street, now act on it */
	if( IsChance(player[nX].pos) )	/* landed on a chance card */
	{	DoChance(player, servParms, nX);
		return 0;
	}
	if( IsCChest(player[nX].pos) )	/* landed on a community chest card */
	{	DoCChest(player, servParms, nX);
		return 0;
	}
	return 0;
}


int BroadCastPos(struct Player player[], struct ServParms *servParms, int nX)
{	char temp[MAXLINE+1];	/* data on the stack */

	sprintf(temp, "/pos %s %d\r\n", player[nX].nick, player[nX].pos);
	BroadCast(player, servParms, temp);
	return 0;
}
