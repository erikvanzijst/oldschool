#include "globals.h"
#include "util.h"

/*
 * Simple utility function for converting our degrees to randians so they can
 * be fed to geometry functions like sin and cos.
 */
double to_radians(const double degrees)
{
	return degrees * (PI / 180);
}

double to_degrees(const double radians) {
	return radians / (PI / 180.0);
}

/*
 * (Aka the Scalar Product)
 * The dot product take two vectors as an input, and results in a scalar.
 */
GLfloat DotProduct(Vertex *v1, Vertex *v2) {
	return (*v1)[0] * (*v2)[0] + (*v1)[1] * (*v2)[1] + (*v1)[2] * (*v2)[2];
}

/*
 * The Cross Product (aka the Vectorial Product) will take an input of two
 * vectors, and generate a vector which is perpendicular to the plane (the
 * normal)on which the two input vectors lie.
 */
void CrossProduct(Vertex *v1, Vertex *v2, Vertex *out) {

	(*out)[0] = (GLfloat)((*v1)[1] * (*v2)[2]) - ((*v1)[2] * (*v2)[1]);
	(*out)[1] = (GLfloat)((*v1)[2] * (*v2)[0]) - ((*v1)[0] * (*v2)[2]);
	(*out)[2] = (GLfloat)((*v1)[0] * (*v2)[1]) - ((*v1)[1] * (*v2)[0]);
}

/*
 * Calculates a vector's length.
 */
GLfloat vectorLength(Vertex *in) {

	return sqrt((*in)[0] * (*in)[0] + (*in)[1] * (*in)[1] + (*in)[2] * (*in)[2]);
}

/*
 * Normalizes vector src and stores the result in dest.
 */
void normalize(Vertex *in, Vertex *out) {
	
	GLfloat length = sqrt((*in)[0] * (*in)[0] + (*in)[1] * (*in)[1] + (*in)[2] * (*in)[2]);
	(*out)[0] = (*in)[0] / length;
	(*out)[1] = (*in)[1] / length;
	(*out)[2] = (*in)[2] / length;
}

/*
 * Used to apply the given 4x4 transformation matrix to the vertex pointed to
 * by *in. The result is stored in *out.
 */
void transform(GLfloat *matrix, GLfloat *in, GLfloat *out) {

    int ii;

    for (ii=0; ii<4; ii++) {
	out[ii] = 
	    in[0] * matrix[0*4+ii] +
	    in[1] * matrix[1*4+ii] +
	    in[2] * matrix[2*4+ii] +
	    in[3] * matrix[3*4+ii];
    }
}

/*
 * Subtracts two points, leading to a vector that is stored under *out.
 */
void subtractVertices(Vertex *in1, Vertex *in2, Vertex *out) {

	(*out)[0] = (GLfloat)((*in1)[0] - (*in2)[0]);
	(*out)[1] = (GLfloat)((*in1)[1] - (*in2)[1]);
	(*out)[2] = (GLfloat)((*in1)[2] - (*in2)[2]);
	(*out)[3] = 1.0;
}

void addVertices(Vertex *in1, Vertex *in2, Vertex *out) {

	(*out)[0] = (GLfloat)((*in1)[0] + (*in2)[0]);
	(*out)[1] = (GLfloat)((*in1)[1] + (*in2)[1]);
	(*out)[2] = (GLfloat)((*in1)[2] + (*in2)[2]);
	(*out)[3] = 1.0;
}

void vectorScalarMultiply(Vertex *vector, GLfloat scalar, Vertex *result) {

	(*result)[0] = (*vector)[0] * scalar;
	(*result)[1] = (*vector)[1] * scalar;
	(*result)[2] = (*vector)[2] * scalar;
}
