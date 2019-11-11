#ifndef INIT_H
#define INIT_H

#include "globals.h"

int InitServParms(struct ServParms *servParms);
int ParseArgs(int argc, char **argv, struct ServParms *servParms);
int InitPlayers(struct Player player[], struct ServParms *servParms, int number);
int InitLogin(struct Login login[], struct ServParms *servParms);
int InitProperties(struct Property property[]);
int InitDeedCards(struct ServParms *servParms);
int ShakeChance(struct ServParms *servParms);
int ShakeCChest(struct ServParms *servParms);

/* see init.c for detailed information on the functions */

#endif
