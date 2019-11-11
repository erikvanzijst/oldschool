#ifndef __MAPS_H
#define __MAPS_H

#include "globals.h"

typedef struct _ObjectDescription
{
	char *filename;	// name of the .sgf file used by the loader
	GLfloat translate[3];
	GLfloat rotate[3];
	GLfloat scale[3];
} ObjectDescription;

LinkedList *getMap(LinkedList **trajectory);
void freeMap(LinkedList *map);
LinkedList *getHardcodedMap(LinkedList **trajectory);
LinkedList *getMapFromFile(LinkedList **trajectory);

#endif
