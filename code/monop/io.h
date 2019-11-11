#ifndef IO_H
#define IO_H

#include "globals.h"

int GetConsoleInput(struct Player player[], struct ServParms *servParms);
int println(char msg[]);
int OpenLogfile(void);
int CloseLogfile(void);

#endif
