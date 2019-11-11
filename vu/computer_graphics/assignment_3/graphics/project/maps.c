#include "maps.h"
#include "loader.h"

/*
 * Used by the program to obtain a world map (containing the descriptions of
 * the objects that must be loaded and where they should be placed. This
 * function is a facade that hides the origin of the world map. This may be
 * hardcoded or read from a map file.
 * Returns 0 if the world map was successfully loaded and stored in the given
 * pointer.
 */
LinkedList *getMap(LinkedList **trajectory)
{
//	return getHardcodedMap();
	return getMapFromFile(trajectory);
}

/*
 * Takes a pointer to the root of a list of ObjectDescription structs and
 * frees its memory.
 */
void freeMap(LinkedList *map) {

	int count = 0;
	LinkedList *entry;

	for(entry = map; entry != NULL; count++) {

		LinkedList *next = entry->next;

		if(entry->data != NULL) {
			free(entry->data);
		}

		free(entry);
		entry = next;
	}
//	fprintf(stdout, "%d ObjectDescription structs released.\n", count);
}

/*
 * Returns a hardcoded city description.
 */
LinkedList *getHardcodedMap(LinkedList **trajectory) {

	ObjectDescription *ground, *eastWall, *westWall, *northWall, *southWall, *house1, *fence;
	LinkedList *entry, *retval;

	retval = (LinkedList *)malloc(sizeof(LinkedList));

	ground = (ObjectDescription *)malloc(sizeof(ObjectDescription));
	eastWall = (ObjectDescription *)malloc(sizeof(ObjectDescription));
	westWall = (ObjectDescription *)malloc(sizeof(ObjectDescription));
	northWall = (ObjectDescription *)malloc(sizeof(ObjectDescription));
	southWall = (ObjectDescription *)malloc(sizeof(ObjectDescription));
	house1 = (ObjectDescription *)malloc(sizeof(ObjectDescription));
	fence = (ObjectDescription *)malloc(sizeof(ObjectDescription));

	retval->data = ground;
	ground->filename = "../models/maps/ground.sgf";
	ground->rotate[0] = ground->rotate[1] = ground->rotate[2] = 0.0;
	ground->scale[0] = ground->scale[1] = ground->scale[2] = 1.0;
	ground->translate[0] = ground->translate[1] = ground->translate[2] = 0.0;
	
	entry = (LinkedList *)malloc(sizeof(LinkedList));
	retval->next = entry;
	entry->data = eastWall;
	eastWall->filename = "../models/maps/wall.sgf";
	eastWall->rotate[0] = 0.0; eastWall->rotate[1] = 90.0; eastWall->rotate[2] = 0.0;
	eastWall->translate[0] = 20.0; eastWall->translate[1] = 0.0; eastWall->translate[2] = 0.0;
	eastWall->scale[0] = eastWall->scale[1] = eastWall->scale[2] = 1.0;

	entry->next = NULL;
	
	entry->next = (LinkedList *)malloc(sizeof(LinkedList));
	entry = entry->next;
	entry->data = westWall;
	westWall->filename = "../models/maps/wall.sgf";
	westWall->rotate[0] = 0.0; westWall->rotate[1] = 90.0; westWall->rotate[2] = 0.0;
	westWall->translate[0] = -20.0; westWall->translate[1] = 0.0; westWall->translate[2] = 0.0;
	westWall->scale[0] = westWall->scale[1] = westWall->scale[2] = 1.0;
	
	entry->next = (LinkedList *)malloc(sizeof(LinkedList));
	entry = entry->next;
	entry->data = northWall;
	northWall->filename = "../models/maps/wall.sgf";
	northWall->rotate[0] = 0.0; northWall->rotate[1] = 0.0; northWall->rotate[2] = 0.0;
	northWall->translate[0] = 0.0; northWall->translate[1] = 0.0; northWall->translate[2] = -20.0;
	northWall->scale[0] = northWall->scale[1] = northWall->scale[2] = 1.0;
	
	entry->next = (LinkedList *)malloc(sizeof(LinkedList));
	entry = entry->next;
	entry->data = southWall;
	southWall->filename = "../models/maps/wall.sgf";
	southWall->rotate[0] = 0.0; southWall->rotate[1] = 180.0; southWall->rotate[2] = 0.0;
	southWall->translate[0] = 0.0; southWall->translate[1] = 0.0; southWall->translate[2] = 20.0;
	southWall->scale[0] = southWall->scale[1] = southWall->scale[2] = 1.0;

	entry->next = (LinkedList *)malloc(sizeof(LinkedList));
	entry = entry->next;
	entry->data = house1;
	house1->filename = "../models/maps/appartment.sgf";
	house1->rotate[0] = 0.0; house1->rotate[1] = 45.0; house1->rotate[2] = 0.0;
	house1->translate[0] = 0.0; house1->translate[1] = 0.0; house1->translate[2] = 10.0;
	house1->scale[0] = house1->scale[1] = house1->scale[2] = 1.0;

	entry->next = (LinkedList *)malloc(sizeof(LinkedList));
	entry = entry->next;
	entry->data = fence;
	fence->filename = "../models/maps/fence.sgf";
	fence->rotate[0] = 0.0; fence->rotate[1] = 0.0; fence->rotate[2] = 0.0;
	fence->translate[0] = 0.0; fence->translate[1] = 0.0; fence->translate[2] = -10.0;
	fence->scale[0] = fence->scale[1] = fence->scale[2] = 1.0;
	
	
	entry->next = NULL;

	return retval;
}

/*
 * Loads the default map file.
 */
LinkedList *getMapFromFile(LinkedList **trajectory) {

	LinkedList *map = NULL;
	int result;
	char *mapfile = "..\\models\\maps\\standard.map";

	if( (result = loadMapDescription(&map, trajectory, mapfile)) < 0) {

		switch(result) {
			case E_FOPEN:
				fprintf(stdout, "Unable to read the scenery map file (%s)\n", mapfile);
				exit(1);
			case E_INCOMPLETE:
				fprintf(stdout, "Scenery map file (%s) incomplete or corrupt.\n", mapfile);
				exit(1);
			default:
				break;
		}
	}

	return map;
}
