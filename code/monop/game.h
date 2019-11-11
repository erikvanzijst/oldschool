#ifndef GAME_H
#define GAME_H

#include "globals.h"

int StartGame(struct ServParms *servParms, struct Player player[]);
int NextPlayer(struct Player player[], struct ServParms *servParms, int current);
int AllowedToRoll(struct Player player[], int nX, struct ServParms *servParms);
int RollDice(struct Dice *dice);	/* get 2 random numbers */
int ReadyOK(struct Player player[], struct ServParms *servParms, int nX);
int DoStreet(struct Player player[], struct ServParms *servParms, int nX);
int BroadCastPos(struct Player player[], struct ServParms *servParms, int nX);

#endif
