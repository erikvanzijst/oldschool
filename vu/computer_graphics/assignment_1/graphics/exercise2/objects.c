/*
 * Computer Graphics Course 2004
 *
 * Exercise 2
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 06.oct.2004
 */

#include "globals.h"
#include "loader.h"

extern GLfloat pyramid_angle_x;
extern GLfloat pyramid_angle_y;
extern GLfloat pyramid_angle_z;
extern GLfloat cube_orbit_angle;
extern GLfloat cube_angle;

/*
 * This file implements the static objects (pyramid and cube).
 */
void createPyramid(void)
{
	GLfloat vertices[][3] = {{0.0, 0.0, 1.0}, {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0},
		{1.0, 0.0, 0.0}, {0.0, -1.0, 0.0}};

	glNewList(PYRAMID, GL_COMPILE);
		glBegin(GL_POLYGON);
			glColor3fv(red);
			glVertex3fv(vertices[0]);	// top
			glVertex3fv(vertices[1]);
			glVertex3fv(vertices[2]);
		glEnd();

		glBegin(GL_POLYGON);
			glColor3fv(blue);
			glVertex3fv(vertices[0]);	// top
			glVertex3fv(vertices[2]);
			glVertex3fv(vertices[3]);
		glEnd();

		glBegin(GL_POLYGON);
			glColor3fv(yellow);
			glVertex3fv(vertices[0]);	// top
			glVertex3fv(vertices[3]);
			glVertex3fv(vertices[4]);
		glEnd();

		glBegin(GL_POLYGON);
			glColor3fv(magenta);
			glVertex3fv(vertices[0]);	// top
			glVertex3fv(vertices[4]);
			glVertex3fv(vertices[1]);
		glEnd();

		glBegin(GL_POLYGON);	// square ground face
			glColor3fv(cyan);
			glVertex3fv(vertices[1]);
			glVertex3fv(vertices[4]);
			glVertex3fv(vertices[3]);
			glVertex3fv(vertices[2]);
		glEnd();

	glEndList();
}

/*
 * Draws a simple 2-2-2 cube around the matrix' origin.
 */
void createCube(void)
{
	GLfloat vertices[][3] = {{-1.0,-1.0,-1.0},{1.0,-1.0,-1.0},
		{1.0,1.0,-1.0}, {-1.0,1.0,-1.0}, {-1.0,-1.0,1.0}, 
		{1.0,-1.0,1.0}, {1.0,1.0,1.0}, {-1.0,1.0,1.0}};

	glNewList(CUBE, GL_COMPILE);

		glBegin(GL_POLYGON);	// bottom
			glColor3fv(red);
			glVertex3fv(vertices[0]);
			glVertex3fv(vertices[1]);
			glVertex3fv(vertices[2]);
			glVertex3fv(vertices[3]);
		glEnd();

		glBegin(GL_POLYGON);	// top
			glColor3fv(blue);
			glVertex3fv(vertices[4]);
			glVertex3fv(vertices[5]);
			glVertex3fv(vertices[6]);
			glVertex3fv(vertices[7]);
		glEnd();

		glBegin(GL_POLYGON);	// left
			glColor3fv(yellow);
			glVertex3fv(vertices[0]);
			glVertex3fv(vertices[3]);
			glVertex3fv(vertices[7]);
			glVertex3fv(vertices[4]);
		glEnd();

		glBegin(GL_POLYGON);	// right
			glColor3fv(green);
			glVertex3fv(vertices[2]);
			glVertex3fv(vertices[1]);
			glVertex3fv(vertices[5]);
			glVertex3fv(vertices[6]);
		glEnd();

		glBegin(GL_POLYGON);	// front
			glColor3fv(cyan);
			glVertex3fv(vertices[3]);
			glVertex3fv(vertices[2]);
			glVertex3fv(vertices[6]);
			glVertex3fv(vertices[7]);
		glEnd();

		glBegin(GL_POLYGON);	// back
			glColor3fv(magenta);
			glVertex3fv(vertices[1]);
			glVertex3fv(vertices[0]);
			glVertex3fv(vertices[4]);
			glVertex3fv(vertices[5]);
		glEnd();

	glEndList();
}

void createF16()
{
	int retval;

	retval = load((char **)f16_files, F16);
	switch(retval)
	{
		case -1:
			fprintf(stdout, "F16 files incomplete (unexpected EOF).\n");
			exit(1);
		case -2:
			fprintf(stdout, "Unsupported polygon type in F16 file(s).\n");
			exit(1);
		case -3:
			fprintf(stdout, "Unable to open one or more F16 definition files.\n");
		default:
			if(BEDUG) fprintf(stdout, "F16 model with %d vertices successfully read.\n", retval);
			break;
	}

}

void drawMatrix()
{
	if(MATRIX_ON)
	{
		glColor3fv(white);
		glBegin(GL_LINES);	// X-axis
			glVertex3d(-10, 0, 0);
			glVertex3d(10, 0, 0);
		glEnd();

		glBegin(GL_LINES);	// Y-axis
			glVertex3d(0, 10, 0);
			glVertex3d(0, -10, 0);
		glEnd();

		glBegin(GL_LINES);	// Z-axis
			glVertex3d(0, 0, -10);
			glVertex3d(0, 0, 10);
		glEnd();
	}
}

void drawPyramid()
{
	glPushMatrix();
	glRotatef(pyramid_angle_x, 1.0, 0.5, 0.0);
	glRotatef(pyramid_angle_y, 0.0, 1.0, 0.5);
	glRotatef(pyramid_angle_z, 0.5, 0.0, 1.0);
	glCallList(PYRAMID);
	glPopMatrix();
}

void drawCube()
{
	glPushMatrix();

	// first compensate for the cube's orbit angle
	glRotatef(cube_orbit_angle, 0.0, 0.0, -1.0);

	// like the earth, the cube is slightly tilted
	glRotatef(20, 0.0, 1.0, 0.0);

	// now make the cube rotate around its Z-axis
	glRotatef(cube_angle, 0.0, 0.0, -1.0);

	glScalef(0.5, 0.5, 0.5);
	glCallList(CUBE);
	glPopMatrix();
}

void drawF16()
{
	glPushMatrix();

	glRotatef(-90, 0.0, 1.0, 0.0);
//	glRotatef(90, 1.0, 0.0, 1.0);
	glCallList(F16);

	glPopMatrix();
}
