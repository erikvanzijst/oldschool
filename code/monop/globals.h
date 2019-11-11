#ifndef GLOBALS_H
#define GLOBALS_H

#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <unistd.h>
#include <time.h>
#include <sys/time.h>
#include <errno.h>
#include <string.h>
#include <ctype.h>

#define LOGINMSG "Monop server Protocol 1.0\n\nLogin: "
#define PASSWDMSG "Password: "
#define INCORRECTPASS "Wrong password.\r\n"
#define NICKINUSE "Nickname in use.\r\n"
#define NICKINVAL "Nickname invalid.\r\n"
#define SERVERFULL "Server is full.\r\n"
#define RUNNING "Game already in session.\r\n"
#define MAXRETRIES 3	/* maximum amount of incorrect passwords */

#define MAXPLAYERS 6	/* NEVER make this higher than 10! */
#define MAXCONNS 20	/* higher than players, cause users can log on simultaniously */
#define LISTENPORT 2525
#define MAXPASSWDLEN 8
#define MAXNICKLEN 9
#define MAXREALNAMELEN 32
#define MAXEMAILLEN 32
#define MAXLINE 2048
#define STARTBUDGET 1500	/* start money */

enum constants { W8FORLOGIN, W8FORPASSWD, VALID, AVAIL, NONE, ROOT, STUFF, DESTUFF };
enum streetType { STREET, CHANCE, CCHEST, UTILITY, TAX, STATION, GO, JAIL, FREEPARKING, GOTOJAIL };

struct Dice	/* structure that holds the 2 stones */
{	int stone1;
	int stone2;
};

struct Output
{	int console;	/* printf to the console? */
	int logging;	/* log to file? */
	char logFile[MAXLINE+1];	/* full name of the logfile */
	FILE *fp;	/* file pointer to the logfile */
};

struct Property	/* a struct for each of the 40 properties */
{	int type;	/* STREET, CHANCE, TAX etc */
	int owner;	/* reference number to a player struct, or -1 */
	int price;	/* price of the property */
	int housePrice;	/* price of one house */
	int hotelPrice;	/* price of one hotel */
	int rent[6];	/* base-rent up to rent with hotel */
	int houses;	/* number of houses */
	int hotels;	/* number of hotels, always 0 or 1 */
	int mortgageValue;	/* money you get from mortgaging this street */
	int mortgaged;	/* is this street mortgaged? */
};

struct DeedCard	/* properties for both chance and community chest cards */
{	char name[MAXLINE+1];	/* text on the card, only for printing to the console */
	int payToBank;	/* negative values allowed */
	int payToAll;	/* pay to each player, negative values allowed */
	int repair;	/* repair houses */
	int perHouse;	/* if so, pay this per house */
	int perHotel;	/* and this for each hotel */
	int isFreeJailCard;	/* 1 = get out of jail free */
	int gotoJail;	/* directly to jail, don't pass start */
	int stepForward;	/* negative values allowed */
	int gotoStreet;	/* proceed to a certain street number, also used for goto Start */
	int gotoSpecialProperty;	/* proceed to nearest service (1) or station (2) */
	int SPPayPerEye;	/* if so, pay this amount per roll */
	int SPPay;	/* pay normal rent times this */
	int orOtherDeed;	/* do the specified actions, or take other kind of card */
};

struct ServParms	/* just a datatype definition, no global variable */
{	int maxPlayers;
	int maxConns;
	int budget;
	int doubleIncomeOnStart;
	int listenPort;	/* TCP port to listen on */
	char password[MAXPASSWDLEN+1];
	struct Dice dice;	/* create some space to hold the numbers of the dice */
	int running;	/* indicates a running game */
	int currentPlayer;	/* the player that rolls the dice */
	struct DeedCard chanceCard[16];	/* pointers to chance card structures */
	struct DeedCard cchestCard[16];	/* pointers to community chest cards */
	int chancelist[16];	/* 16 chance cards, randomized */
	int cchestlist[16];	/* 16 community chest cards, randomized */
	int chance;	/* current chance card */
	int cchest;	/* current community chest card */
	struct Property property[40];	/* the whole board */
	struct Output output;	/* struct controlling all output */
};

struct Player
{	char nick[MAXNICKLEN+1];
	char realname[MAXREALNAMELEN+1];
	char email[MAXEMAILLEN+1];
	char ipnr[16];	/* 1.2.3.4 */
	char fqdn[255];	/* host.net */
	long budget;	/* your total amount of money */
	int hidebudget;	/* 1 if budget is hidden, 0 is not hidden */
	int token;	/* your token 0-9 */
	int fd;	/* the TCP socket to the client */
	int pos;	/* position on the board, streets are numbered */
	int rolled;	/* the number of times the player rolled this turn */
	int doubles;	/* the number of times the player rolled doubles this turn */
	int flags; /* determines whether the player can control the game */
	int ready;	/* indicates whether a player is ready to start */
};

struct Login	/* new connections are being put in a Login struct during login */
{	int fd;	/* the TCP socket to the client */
	char nick[MAXNICKLEN+1];
	int stage;	/* W8FORLOGIN / W8FORPASSWD */
	struct sockaddr_in *cliaddr;
	char ipnr[16];	/* dotted decimal */
	char fqdn[255];	/* host.net */
	int retry;	/* amount of wrong passwds */
};

#endif
