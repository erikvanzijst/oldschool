#ifndef __UTIL_H
#define __UTIL_H

#include "globals.h"

double to_radians(const double degrees);
double to_degrees(const double radians);

GLfloat DotProduct(Vertex *v1, Vertex *v2);
void CrossProduct(Vertex *v1, Vertex *v2, Vertex *dest);
GLfloat vectorLength(Vertex *in);
void normalize(Vertex *src, Vertex *dest);
void transform(GLfloat *matrix, GLfloat *in, GLfloat *out);
void subtractVertices(Vertex *in1, Vertex *in2, Vertex *out);
void addVertices(Vertex *in1, Vertex *in2, Vertex *out);
void vectorScalarMultiply(Vertex *vector, GLfloat scalar, Vertex *result);

#endif
