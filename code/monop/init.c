#include "globals.h"
#include "init.h"

#define LOGFILE "monop.log"	/* base path is your homedir */

int InitServParms(struct ServParms *servParms)
{	/* set all server parameters to their defaults */
	char logfile[MAXLINE+1];
	srand((unsigned) time(NULL));	/* randomize timer */
	servParms->maxPlayers = MAXPLAYERS;
	servParms->maxConns = MAXCONNS;
	servParms->budget = STARTBUDGET;
	servParms->listenPort = LISTENPORT;
	servParms->doubleIncomeOnStart = 0;
	strcpy(servParms->password, "");	/* no password */
	servParms->currentPlayer = servParms->running = 0;

	ShakeChance(servParms);	/* randomize the chance stack */
	ShakeCChest(servParms);	/* and community chest */
	servParms->chance = servParms->cchest = 0;	/* current card */

	servParms->output.console = 1;	/* by default, print to stdout */
	servParms->output.logging = 1;	/* by default, do logging */
	servParms->output.fp = NULL;	/* initialize the file pointer at null */
	if(getenv("HOME") == NULL)
		strcpy(servParms->output.logFile, "");	/* no log file by default */
	else
		sprintf( servParms->output.logFile, "%s/%s", (char *) getenv("HOME"), (char *) LOGFILE);
	
	return 0;
}

int ParseArgs(int argc, char **argv, struct ServParms *servParms)
{
	return 0;
}

int InitPlayers(struct Player player[], struct ServParms *servParms, int number)
{	int nX;

	if(number == -1)	/* reset all player structs */
	{	for(nX = 0; nX < 10; nX++)
		{	strcpy(player[nX].nick, "");
			strcpy(player[nX].realname, "Realname");
			strcpy(player[nX].ipnr, "");
			strcpy(player[nX].fqdn, "");
			strcpy(player[nX].email, "");
			player[nX].budget = servParms->budget;
			player[nX].hidebudget = 0;	/* budget visible by default */
			player[nX].token = -1;
			player[nX].fd = -1;
			player[nX].pos = player[nX].ready = 0;
			player[nX].flags = NONE;
		}
	}
	else	/* only clear the specified player struct */
	{
		strcpy(player[number].nick, "");
		strcpy(player[number].realname, "Realname");
		strcpy(player[number].ipnr, "");
		strcpy(player[number].fqdn, "");
		strcpy(player[number].email, "");
		player[number].budget = servParms->budget;
		player[number].hidebudget = 0;	/* budget visible by default */
		player[number].token = -1;
		player[number].fd = -1;
		player[number].pos = player[number].ready = 0;
		player[number].flags = NONE;
	}
	return 0;
}

int InitLogins(struct Login login[], struct ServParms *servParms)
{	int nX;
	for(nX = 0; nX < servParms->maxConns; nX++)
	{	login[nX].fd = -1;
		login[nX].stage = W8FORLOGIN;
		strcpy(login[nX].nick, "");
		strcpy(login[nX].ipnr, "");
		strcpy(login[nX].fqdn, "");
		login[nX].retry = 0;
	}
	return 0;
}


int InitProperties(struct Property property[])
{	int nX, mX;

	for( nX = 0; nX < 40; nX++ )
	{	property[nX].mortgaged = 0;	/* not mortgaged */
		property[nX].owner = -1;	/* unowned by default */
		property[nX].type = STREET;	/* most of them are streets */
		for( mX = 0; mX < 6; mX++ )	/* reset the rent */
			property[nX].rent[mX] = 0;
		property[nX].houses = property[nX].hotels = 0;	/* no houses */
		if( (int) (nX / 10) < 1 )	/* price of a house and hotel */
			property[nX].housePrice = 50;
		else if( (int) (nX / 10) < 2 )
			property[nX].housePrice = 100;
		else if( (int) (nX / 10) < 3 )
			property[nX].housePrice = 150;
		else if( (int) (nX / 10) < 4 )
			property[nX].housePrice = 200;
		
	}
	property[0].type = GO;
	property[10].type = JAIL;
	property[20].type = FREEPARKING;
	property[30].type = GOTOJAIL;
	property[2].type = property[17].type = property[33].type = CCHEST;
	property[7].type = property[22].type = property[36].type = CHANCE;
	property[5].type = property[15].type = property[25].type = property[35].type = STATION;
	property[12].type = property[28].type = UTILITY;
	property[4].type = property[38].type = TAX;

	/* now fill the damn values for each street */
	property[1].price = 60;
	property[1].rent[0] = 2;	/* base rent */
	property[1].rent[1] = 10;	/* 1 house */
	property[1].rent[2] = 30;	/* 2 houses */
	property[1].rent[3] = 90;	/* 3 houses */
	property[1].rent[4] = 160;	/* 4 houses */
	property[1].rent[5] = 250;	/* hotel */

	property[3].price = 60;
	property[3].rent[0] = 4;	/* base rent */
	property[3].rent[1] = 20;	/* 1 house */
	property[3].rent[2] = 60;	/* 2 houses */
	property[3].rent[3] = 180;	/* 3 houses */
	property[3].rent[4] = 320;	/* 4 houses */
	property[3].rent[5] = 450;	/* hotel */

	property[5].price = 200;	/* station south */
	property[5].rent[0] = 25;	/* base rent */
	property[5].rent[1] = 0;
	property[5].rent[2] = 50;	/* if you have 2 stations */
	property[5].rent[3] = 100;	/* if you have 3 */
	property[5].rent[4] = 200;	/* if you have 'm all */
	property[5].rent[5] = 0;

	property[6].price = 100;
	property[6].rent[0] = 6;	/* base rent */
	property[6].rent[1] = 30;	/* 1 house */
	property[6].rent[2] = 90;	/* 2 houses */
	property[6].rent[3] = 270;	/* 3 houses */
	property[6].rent[4] = 400;	/* 4 houses */
	property[6].rent[5] = 550;	/* hotel */

	property[8].price = 100;
	property[8].rent[0] = 6;	/* base rent */
	property[8].rent[1] = 30;	/* 1 house */
	property[8].rent[2] = 90;	/* 2 houses */
	property[8].rent[3] = 270;	/* 3 houses */
	property[8].rent[4] = 400;	/* 4 houses */
	property[8].rent[5] = 550;	/* hotel */

	property[9].price = 120;
	property[9].rent[0] = 8;	/* base rent */
	property[9].rent[1] = 40;	/* 1 house */
	property[9].rent[2] = 100;	/* 2 houses */
	property[9].rent[3] = 300;	/* 3 houses */
	property[9].rent[4] = 450;	/* 4 houses */
	property[9].rent[5] = 600;	/* hotel */

	property[11].price = 140;
	property[11].rent[0] = 10;	/* base rent */
	property[11].rent[1] = 50;	/* 1 house */
	property[11].rent[2] = 150;	/* 2 houses */
	property[11].rent[3] = 450;	/* 3 houses */
	property[11].rent[4] = 625;	/* 4 houses */
	property[11].rent[5] = 750;	/* hotel */

	property[13].price = 140;
	property[13].rent[0] = 10;	/* base rent */
	property[13].rent[1] = 50;	/* 1 house */
	property[13].rent[2] = 150;	/* 2 houses */
	property[13].rent[3] = 450;	/* 3 houses */
	property[13].rent[4] = 625;	/* 4 houses */
	property[13].rent[5] = 750;	/* hotel */

	property[14].price = 160;
	property[14].rent[0] = 12;	/* base rent */
	property[14].rent[1] = 60;	/* 1 house */
	property[14].rent[2] = 180;	/* 2 houses */
	property[14].rent[3] = 500;	/* 3 houses */
	property[14].rent[4] = 700;	/* 4 houses */
	property[14].rent[5] = 900;	/* hotel */

	property[15].price = 200;	/* station east */
	property[15].rent[0] = 25;	/* base rent */
	property[15].rent[1] = 0;
	property[15].rent[2] = 50;	/* if you have 2 stations */
	property[15].rent[3] = 100;	/* if you have 3 */
	property[15].rent[4] = 200;	/* if you have 'm all */
	property[15].rent[5] = 0;

	property[16].price = 180;
	property[16].rent[0] = 14;	/* base rent */
	property[16].rent[1] = 70;	/* 1 house */
	property[16].rent[2] = 200;	/* 2 houses */
	property[16].rent[3] = 550;	/* 3 houses */
	property[16].rent[4] = 750;	/* 4 houses */
	property[16].rent[5] = 950;	/* hotel */

	property[18].price = 180;
	property[18].rent[0] = 14;	/* base rent */
	property[18].rent[1] = 70;	/* 1 house */
	property[18].rent[2] = 200;	/* 2 houses */
	property[18].rent[3] = 550;	/* 3 houses */
	property[18].rent[4] = 750;	/* 4 houses */
	property[18].rent[5] = 950;	/* hotel */

	property[19].price = 200;
	property[19].rent[0] = 16;	/* base rent */
	property[19].rent[1] = 80;	/* 1 house */
	property[19].rent[2] = 220;	/* 2 houses */
	property[19].rent[3] = 600;	/* 3 houses */
	property[19].rent[4] = 800;	/* 4 houses */
	property[19].rent[5] = 1000;	/* hotel */

	property[21].price = 220;
	property[21].rent[0] = 18;	/* base rent */
	property[21].rent[1] = 90;	/* 1 house */
	property[21].rent[2] = 250;	/* 2 houses */
	property[21].rent[3] = 700;	/* 3 houses */
	property[21].rent[4] = 875;	/* 4 houses */
	property[21].rent[5] = 1050;	/* hotel */

	property[23].price = 220;
	property[23].rent[0] = 18;	/* base rent */
	property[23].rent[1] = 90;	/* 1 house */
	property[23].rent[2] = 250;	/* 2 houses */
	property[23].rent[3] = 700;	/* 3 houses */
	property[23].rent[4] = 875;	/* 4 houses */
	property[23].rent[5] = 1050;	/* hotel */

	property[24].price = 240;
	property[24].rent[0] = 20;	/* base rent */
	property[24].rent[1] = 100;	/* 1 house */
	property[24].rent[2] = 300;	/* 2 houses */
	property[24].rent[3] = 750;	/* 3 houses */
	property[24].rent[4] = 925;	/* 4 houses */
	property[24].rent[5] = 1100;	/* hotel */

	property[25].price = 200;	/* station north */
	property[25].rent[0] = 25;	/* base rent */
	property[25].rent[1] = 0;
	property[25].rent[2] = 50;	/* if you have 2 stations */
	property[25].rent[3] = 100;	/* if you have 3 */
	property[25].rent[4] = 200;	/* if you have 'm all */
	property[25].rent[5] = 0;

	property[26].price = 260;
	property[26].rent[0] = 22;	/* base rent */
	property[26].rent[1] = 110;	/* 1 house */
	property[26].rent[2] = 330;	/* 2 houses */
	property[26].rent[3] = 800;	/* 3 houses */
	property[26].rent[4] = 975;	/* 4 houses */
	property[26].rent[5] = 1150;	/* hotel */

	property[27].price = 260;
	property[27].rent[0] = 22;	/* base rent */
	property[27].rent[1] = 110;	/* 1 house */
	property[27].rent[2] = 330;	/* 2 houses */
	property[27].rent[3] = 800;	/* 3 houses */
	property[27].rent[4] = 975;	/* 4 houses */
	property[27].rent[5] = 1150;	/* hotel */

	property[29].price = 280;
	property[29].rent[0] = 24;	/* base rent */
	property[29].rent[1] = 120;	/* 1 house */
	property[29].rent[2] = 360;	/* 2 houses */
	property[29].rent[3] = 850;	/* 3 houses */
	property[29].rent[4] = 1025;	/* 4 houses */
	property[29].rent[5] = 1200;	/* hotel */

	property[31].price = 300;
	property[31].rent[0] = 26;	/* base rent */
	property[31].rent[1] = 130;	/* 1 house */
	property[31].rent[2] = 390;	/* 2 houses */
	property[31].rent[3] = 900;	/* 3 houses */
	property[31].rent[4] = 1100;	/* 4 houses */
	property[31].rent[5] = 1275;	/* hotel */

	property[32].price = 300;
	property[32].rent[0] = 26;	/* base rent */
	property[32].rent[1] = 130;	/* 1 house */
	property[32].rent[2] = 390;	/* 2 houses */
	property[32].rent[3] = 900;	/* 3 houses */
	property[32].rent[4] = 1100;	/* 4 houses */
	property[32].rent[5] = 1275;	/* hotel */

	property[34].price = 320;
	property[34].rent[0] = 28;	/* base rent */
	property[34].rent[1] = 150;	/* 1 house */
	property[34].rent[2] = 450;	/* 2 houses */
	property[34].rent[3] = 1000;	/* 3 houses */
	property[34].rent[4] = 1200;	/* 4 houses */
	property[34].rent[5] = 1400;	/* hotel */

	property[35].price = 200;	/* station west */
	property[35].rent[0] = 25;	/* base rent */
	property[35].rent[1] = 0;
	property[35].rent[2] = 50;	/* if you have 2 stations */
	property[35].rent[3] = 100;	/* if you have 3 */
	property[35].rent[4] = 200;	/* if you have 'm all */
	property[35].rent[5] = 0;

	property[37].price = 350;
	property[37].rent[0] = 35;	/* base rent */
	property[37].rent[1] = 175;	/* 1 house */
	property[37].rent[2] = 500;	/* 2 houses */
	property[37].rent[3] = 1100;	/* 3 houses */
	property[37].rent[4] = 1300;	/* 4 houses */
	property[37].rent[5] = 1500;	/* hotel */

	property[39].price = 400;	/* boardwalk */
	property[39].rent[0] = 50;	/* base rent */
	property[39].rent[1] = 200;	/* 1 house */
	property[39].rent[2] = 600;	/* 2 houses */
	property[39].rent[3] = 1400;	/* 3 houses */
	property[39].rent[4] = 1700;	/* 4 houses */
	property[39].rent[5] = 2000;	/* hotel */

	return 0;
}


int InitDeedCards(struct ServParms *servParms)
{	/* fill the chance cards with the default us rules */
	char temp[MAXLINE+1];
	int nX;
	for( nX = 0; nX < 16; nX++)	/* first, clear 'm all */
	{	sprintf(temp, "chance card #%d.", nX);
		strcpy( servParms->chanceCard[nX].name, (char *) temp );
		servParms->chanceCard[nX].payToBank = 0;
		servParms->chanceCard[nX].payToAll = 0;
		servParms->chanceCard[nX].repair = 0;
		servParms->chanceCard[nX].perHouse = 0;
		servParms->chanceCard[nX].perHotel = 0;
		servParms->chanceCard[nX].isFreeJailCard = 0;
		servParms->chanceCard[nX].gotoJail = 0;
		servParms->chanceCard[nX].stepForward = 0;
		servParms->chanceCard[nX].gotoStreet = -1;
		servParms->chanceCard[nX].gotoSpecialProperty = 0;
		servParms->chanceCard[nX].SPPayPerEye = 0;
		servParms->chanceCard[nX].SPPay = 0;
		servParms->chanceCard[nX].orOtherDeed = 0;

		sprintf(temp, "community chest card #%d.", nX);
		strcpy( servParms->cchestCard[nX].name, (char *) temp);
		servParms->cchestCard[nX].payToBank = 0;
		servParms->cchestCard[nX].payToAll = 0;
		servParms->cchestCard[nX].repair = 0;
		servParms->cchestCard[nX].perHouse = 0;
		servParms->cchestCard[nX].perHotel = 0;
		servParms->cchestCard[nX].isFreeJailCard = 0;
		servParms->cchestCard[nX].gotoJail = 0;
		servParms->cchestCard[nX].stepForward = 0;
		servParms->cchestCard[nX].gotoStreet = -1;
		servParms->cchestCard[nX].gotoSpecialProperty = 0;
		servParms->cchestCard[nX].SPPayPerEye = 0;
		servParms->cchestCard[nX].SPPay = 0;
		servParms->cchestCard[nX].orOtherDeed = 0;
	}

	/* Now fill them with the default, hardcoded rules */
	/* chance cards first */
	strcpy( servParms->chanceCard[0].name, (char *) "Uw bouwverzekering vervalt, U ontvangt f150,-");
	servParms->chanceCard[0].payToBank = -150;

	strcpy( servParms->chanceCard[1].name, (char *) "Opgebracht wegens dronkenschap, betaal f20,- boete.");
	servParms->chanceCard[1].payToBank = 20;

	strcpy( servParms->chanceCard[2].name, (char *) "U hebt een kruiswoord puzzle gewonnen en ontvangt f100,-");
	servParms->chanceCard[2].payToBank = -100;

	strcpy( servParms->chanceCard[3].name, (char *) "De bank betaalt U f50,- dividend.");
	servParms->chanceCard[3].payToBank = -50;

	strcpy( servParms->chanceCard[4].name, (char *) "Boete voor te snel rijden, f15,-");
	servParms->chanceCard[4].payToBank = 15;

	strcpy( servParms->chanceCard[5].name, (char *) "Betaal schoolgeld f150,-");
	servParms->chanceCard[5].payToBank = 150;

	strcpy( servParms->chanceCard[6].name, (char *) "Ga verder naar Bartel Jorisstraat. Indien U langs start komt, ontvangt U f200,-");
	servParms->chanceCard[6].stepForward = 1;	/* proceed, may pass start */
	servParms->chanceCard[6].gotoStreet = 11;

	strcpy( servParms->chanceCard[7].name, (char *) "Ga verder naar Heerestraat. Indien U langs start komt, ontvangt U f200,-");
	servParms->chanceCard[7].stepForward = 1;
	servParms->chanceCard[7].gotoStreet = 24;

	strcpy( servParms->chanceCard[8].name, (char *) "Ga verder naar Kalverstraat.");
	servParms->chanceCard[8].stepForward = 1;
	servParms->chanceCard[8].gotoStreet = 39;

	strcpy( servParms->chanceCard[9].name, (char *) "Ga direct naar de gevangenis, ga niet langs start, U ontvangt geen f200,-");
	servParms->chanceCard[10].stepForward = 0;
	servParms->chanceCard[9].gotoJail = 1;

	strcpy( servParms->chanceCard[10].name, (char *) "Ga drie plaatsen terug.");
	servParms->chanceCard[10].stepForward = -3;

	strcpy( servParms->chanceCard[11].name, (char *) "Verlaat de gevangenis zonder betalen.");
	servParms->chanceCard[11].isFreeJailCard = 1;

	strcpy( servParms->chanceCard[12].name, (char *) "Reis naar station West, indoen U langs start komt ontvangt U f200,-");
	servParms->chanceCard[12].stepForward = 1;
	servParms->chanceCard[12].gotoStreet = 15;

	strcpy( servParms->chanceCard[13].name, (char *) "Ga verder naar start.");
	servParms->chanceCard[13].stepForward = 1;
	servParms->chanceCard[13].gotoStreet = 0;

	strcpy( servParms->chanceCard[14].name, (char *) "U wordt aangeslagen voor straatgeld. Betaal f40,- per huis en f115,- per hotel.");
	servParms->chanceCard[14].repair = 1;
	servParms->chanceCard[14].perHouse = 40;
	servParms->chanceCard[14].perHotel = 115;

	strcpy( servParms->chanceCard[15].name, (char *) "Repareer Uw huizen, Betaal voor elk huis f25,- Betaal voor elk hotel f100,-");
	servParms->chanceCard[15].repair = 1;
	servParms->chanceCard[15].perHouse = 25;
	servParms->chanceCard[15].perHotel = 100;

	/* now community chest cards */
	strcpy( servParms->cchestCard[0].name, (char *) "U erft f100,-");
	servParms->cchestCard[0].payToBank = -100;

	strcpy( servParms->cchestCard[1].name, (char *) "Een vergissing van de bank in Uw voordeel, U ontvangt f200,-");
	servParms->cchestCard[1].payToBank = -200;

	strcpy( servParms->cchestCard[2].name, (char *) "Door verkoop van effecten ontvangt U f50,-");
	servParms->cchestCard[2].payToBank = -50;

	strcpy( servParms->cchestCard[3].name, (char *) "Lijfrente vervalt, U ontvangt f100,-");
	servParms->cchestCard[3].payToBank = -100;

	strcpy( servParms->cchestCard[4].name, (char *) "Restitutie inkomsten belasting, U ontvangt f20,-");
	servParms->cchestCard[4].payToBank = -20;

	strcpy( servParms->cchestCard[5].name, (char *) "Betaal Uw verzekeringspremie, f50,-");
	servParms->cchestCard[5].payToBank = 50;

	strcpy( servParms->cchestCard[6].name, (char *) "Betaal Uw doktersrekening, f50,-");
	servParms->cchestCard[6].payToBank = 50;

	strcpy( servParms->cchestCard[7].name, (char *) "U ontvangt 7% over preferente aandelen, f25,-");
	servParms->cchestCard[7].payToBank = -25;

	strcpy( servParms->cchestCard[8].name, (char *) "U hebt de tweede prijs in een schoonheidswedstrijd gewonnen en ontvangt f10,-");
	servParms->cchestCard[8].payToBank = -10;

	strcpy( servParms->cchestCard[9].name, (char *) "Betaal het hospitaal f100,-");
	servParms->cchestCard[9].payToBank = 100;

	strcpy( servParms->cchestCard[10].name, (char *) "U bent jarig en ontvangt van iedere speler f10,-");
	servParms->cchestCard[10].payToAll = -10;

	strcpy( servParms->cchestCard[11].name, (char *) "Ga verder naar start.");
	servParms->cchestCard[11].stepForward = 1;
	servParms->cchestCard[11].gotoStreet = 0;

	strcpy( servParms->cchestCard[12].name, (char *) "Ga direct naar de gevangenis, ga niet door start, U ontvangt geen f200,-");
	servParms->cchestCard[12].gotoJail = 1;

	strcpy( servParms->cchestCard[13].name, (char *) "Ga terug naar dorpsstraat Ons Dorp.");
	servParms->cchestCard[13].stepForward = 0;	/* go back there, do not pass start */
	servParms->cchestCard[13].gotoStreet = 1;

	strcpy( servParms->cchestCard[14].name, (char *) "Verlaat de gevangenis zonder betalen.");
	servParms->cchestCard[14].isFreeJailCard = 1;

	strcpy( servParms->cchestCard[15].name, (char *) "Betaal f10,- boete of neem een kans kaart");
	servParms->cchestCard[15].payToBank = 10;
	servParms->cchestCard[15].orOtherDeed = 1;

	return 0;
}


int ShakeChance(struct ServParms *servParms)
{	int nX, nY, number, unique;

	for( nX = 0; nX < 16; nX++ )	/* first, reset the array */
		servParms->chancelist[nX] = -1;
	nX = 0;
	unique = 1;
	while( nX < 16 )	/* well noticed! maximum of 16 cards is hardcoded */
	{	number = 1 + (int) (16.0 * rand()/(RAND_MAX + 1.0));	/* random number from 1 to 16 */
		for( nY = 0; nY < nX; nY++ )
			if( servParms->chancelist[nY] == number )
				unique = 0;
		if(unique)
			servParms->chancelist[nX++] = number;
		else
			unique = 1;
	}
	return 0;
}


int ShakeCChest(struct ServParms *servParms)
{	int nX, nY, number, unique;

	for( nX = 0; nX < 16; nX++ )	/* first, reset the array */
		servParms->cchestlist[nX] = -1;
	nX = 0;
	unique = 1;
	while( nX < 16 )	/* well noticed! maximum of 16 cards is hardcoded */
	{	number = (int) (16.0 * rand()/(RAND_MAX + 1.0));	/* random number from 1 to 16 */
		for( nY = 0; nY < nX; nY++ )
			if( servParms->cchestlist[nY] == number )
				unique = 0;
		if(unique)
			servParms->cchestlist[nX++] = number;
		else
			unique = 1;
	}
	return 0;
}
