#ifndef PARSERS_H
#define PARSERS_H

#include "globals.h"

int ParseLogin(struct Login login[], int nX, char msg[], struct Player player[], struct ServParms *servParms);
int ParsePlayer(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserName(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserNick(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserToken(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserUnknown(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserEmail(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserFqdn(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserIpnr(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserNames(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserChat(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserMsg(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserQuit(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserShutdown(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserBudget(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserReady(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserDice(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserProperty(struct Player player[], int nX, char msg[], struct ServParms *servParms);
int ParserPos(struct Player player[], int nX, char msg[], struct ServParms *servParms);
/* more info on these subparsers can be found in parsers.c */

#endif
