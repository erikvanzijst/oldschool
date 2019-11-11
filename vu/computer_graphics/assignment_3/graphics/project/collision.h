#ifndef __COLLISION_H
#define __COLLISION_H

#include "globals.h"

BOOL vertexInPolygon(Vertex *point, Vertex *vertices, int num_vertices);
BOOL detectSceneryCollision(Vertex *from, Vertex *to, LinkedList *scenery);
BOOL detectMonsterCollision(Monster *m, Player *p);
LinkedList *spawnMonster(LinkedList *trajectory);

#endif
