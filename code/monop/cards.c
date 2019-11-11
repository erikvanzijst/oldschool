#include "globals.h"
#include "cards.h"
#include "game.h"

int IsChance(int pos)	/* hardcoded chance positions */
{	if((pos == 7)||(pos == 22)||(pos == 36))
		return 1;	/* chance card */
	return 0;	/* no chance */
}


int IsCChest(int pos)	/* hardcoded community chest positions */
{	if((pos == 2)||(pos == 17)||(pos == 33))
		return 1;	/* community chest card */
	return 0;	/* no community chest */
}


int DoChance(struct Player player[], struct ServParms *servParms, int nX)
{	char temp[MAXLINE+1];
			
	sprintf(temp, "%s took a chance card (%d) saying: \"%s\"\n", player[nX].nick, servParms->chancelist[servParms->chance], servParms->chanceCard[servParms->chancelist[servParms->chance]].name);
	println(temp);
	servParms->chance = (servParms->chance + 1) % 16;

	return 0;
}


int DoCChest(struct Player player[], struct ServParms *servParms, int nX)
{	char temp[MAXLINE+1];

	sprintf(temp, "%s took a community chest card (%d) saying: \"%s\"\n", player[nX].nick, servParms->cchestlist[servParms->cchest], servParms->cchestCard[servParms->cchestlist[servParms->cchest]].name);
	println(temp);
	servParms->cchest = (servParms->cchest + 1) % 16;

	return 0;
}
