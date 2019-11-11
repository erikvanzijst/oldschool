#ifndef STRINGS_H
#define STRINGS_H

#include "globals.h"

int ReadConfig(struct ServParms *servParms);
int ReadEntry(FILE *fp, struct ServParms *servParms, char filename[]);
int WriteEntry(FILE *fp, char key[], struct ServParms *servParms);
int DumpHeader(FILE *fp);
void DisplayConfig(struct ServParms *servParms);

#endif
