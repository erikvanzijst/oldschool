#ifndef __LOADER_H
#define __LOADER_H

#define E_INCOMPLETE -1
#define E_POLYGON -2
#define E_FOPEN -3

long load(char **files, int files_count, int name);

#endif __LOADER_H
