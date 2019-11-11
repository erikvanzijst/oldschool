#ifndef __LOADER_H
#define __LOADER_H

#define E_INCOMPLETE -1
#define E_POLYGON -2
#define E_FOPEN -3

long load(char **files, int files_count, int name, Surface **polygons, int context);
long loadTextured(char **files, int file_count, int name, Surface **_polygons, int context, char *texture);
int loadMapDescription(LinkedList **map, LinkedList **trajectory, char *mapfile);

#endif __LOADER_H
