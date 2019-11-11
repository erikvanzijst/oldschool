#ifndef MISC_H
#define MISC_H

#include "globals.h"

int NickVal(char nick[], int operation, struct Player player[], struct ServParms *servParms);
int TokenVal(int token, int operation, struct Player player[], struct ServParms *servParms);
int GenToken(struct Player player[], struct ServParms *servParms);
int GetPlayerByNick(struct Player player[], struct ServParms *servParms, char nick[]);
char * MakeUserList(char list[], struct Player player[], struct ServParms *servParms);
int SayWhat(struct Player player[], int nX);
int TotalPlayers(struct Player player[], struct ServParms *servParms);
int AllReady(struct Player player[], struct ServParms *servParms);

#endif
