#ifndef __MODELS_H
#define __MODELS_H

#include "globals.h"
#include "maps.h"

int loadMap(LinkedList **root, LinkedList **trajectory, int context);
void drawMap(LinkedList *root, int context);
void positionObject(Object *object);
GLMmodel *loadGLMmodel(char *filename);
void drawGLMmodel(GLMmodel *model, GLfloat *transformation_matrix);
int loadDino(TreeNode **root, int context);
void drawDino(TreeNode *root, int context);

#endif __MODELS_H
