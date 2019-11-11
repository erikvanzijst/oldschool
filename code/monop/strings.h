#ifndef STRINGS_H
#define STRINGS_H

#include "globals.h"

int StripCR(char msg[]);
int GetCommand(char msg[]);
int GetSubCommand(char msg[]);
int GetFirstWord(char temp[], char msg[]);
int RemoveFirstWord(char msg[]);
char * UpperCase(char dest[], char msg[]);
int GetFirstString(char dest[], char msg[]);
char * ByteStuffing(char msg[], int operation);

#endif
