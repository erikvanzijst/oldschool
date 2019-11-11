#ifndef COMMANDS_H
#define COMMANDS_H

enum commands { GET, SET, UNKNOWN, NAME, NICK, TOKEN, EMAIL, FQDN, IPNR, NAMES,
		QUIT, CHAT, MSG, BUDGET, HIDE, READY, DICE, ROLL, POS, PROPERTY, BUY,
		MORTGAGE, SHUTDOWN };

#define NAMESTR "/NAME"	/* sets your realname.. can be anything */
#define NICKSTR "/NICK"	/* sets your nick */
#define TOKENSTR "/TOKEN"	/* asks or sets your token */
#define EMAILSTR "/EMAIL" /* asks or sets your emailaddress field */
#define FQDNSTR "/FQDN"	/* asks for a users domain name */
#define IPNRSTR "/IPNR"	/* asks for a users IP number */
#define NAMESSTR "/NAMES"	/* asks the list op player nicks */
#define QUITSTR "/QUIT"	/* tell the server you quit */
#define CHATSTR "/CHAT"	/* say a public line in the partyline */
#define MSGSTR "/MSG"	/* or a private line to someone */
#define BUDGETSTR "/BUDGET"	/* retrieve someone's money, or hide it */
#define READYSTR "/READY"	/* makes a player ready to go */
#define DICESTR "/DICE"	/* roll the dice */
#define POSSTR "/POS"	/* ask someone's location on the board */
#define PROPERTYSTR "/PROPERTY"	/* buy and mortgage property */
#define SHUTDOWNSTR "/SHUTDOWN"	/* kill the server */

#endif
