#ifndef GLOBALS_H
#define GLOBALS_H

#include "globals.h"

int IsChance(int pos);
int IsCChest(int pos);
int DoChance(struct Player player[], struct ServParms *servParms, int nX);
int DoCChest(struct Player player[], struct ServParms *servParms, int nX);

#endif
