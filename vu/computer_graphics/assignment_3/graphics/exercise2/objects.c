/*
 * Computer Graphics Course 2004
 *
 * Exercise 2
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 24.nov.2004
 */

#include "globals.h"
#include "loader.h"

extern GLuint texture;	// name of our texture
extern GLfloat pyramid_angle_x;
extern GLfloat pyramid_angle_y;
extern GLfloat pyramid_angle_z;
extern GLfloat cube_orbit_angle;
extern GLfloat cube_angle;

GLfloat modulation = GL_REPLACE;
GLfloat tiles[] = {1.0, 1.0};	// allows for 4x2, 1x1, etc on bottom texture

/*
 * Creates the texture-mapped pyramid that rotates around the world's origin.
 * For the bottom surface, the tiles[] values are used. These 2 floats allow
 * the user to specify how many tiles vertically and horizontally should be
 * mapped on the surface. 1x1 is the default. This is controlled by the
 * context menu.
 *
 * Notice:	the glMaterial calls currently use a lot of hardcoded color values.
 *			This was the result of major experimentation to get the lighting
 *			satisfactory. Hardcoded values will be avoided in the final
 *			project where-ever possible.
 */
void createPyramid(GLenum shadeModel)
{
	GLfloat vertices[][3] = {{0.0, 0.0, 1.0}, {-1.0, 0.0, 0.0}, {0.0, 1.0, 0.0},
		{1.0, 0.0, 0.0}, {0.0, -1.0, 0.0}};
	GLfloat diffuse[4];
	GLfloat specular[4];

	glGetMaterialfv(GL_FRONT, GL_DIFFUSE, (GLfloat*)diffuse);
	glGetMaterialfv(GL_FRONT, GL_SPECULAR, (GLfloat*)specular);
	glPushAttrib(GL_ALL_ATTRIB_BITS);	// good practice
	glPushMatrix();
	glShadeModel(shadeModel);


	glNewList(PYRAMID, GL_COMPILE);
		glEnable(GL_TEXTURE_2D);
		glBindTexture( GL_TEXTURE_2D, texture );	// bind the texture
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, modulation);
		glBegin(GL_POLYGON);
		{
			GLfloat normal[] = {-1.0, -1.0, 1.0};
			GLfloat material_diffuse[] =	{0.43 * red[0], 0.47 * red[1], 0.55 * red[2], 1.0};
			GLfloat material_specular[] =	{0.71 * red[0], 0.75 * red[1], 0.75 * red[2], 1.0};
			glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
			glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);
			
			glNormal3fv(normal);
			glTexCoord2f(0.5, 1.0);
			glVertex3fv(vertices[0]);	// top
			glTexCoord2f(1.0, 0.0);
			glVertex3fv(vertices[1]);
			glTexCoord2f(0.0, 0.0);
			glVertex3fv(vertices[2]);
		}
		glEnd();

		glBegin(GL_POLYGON);
		{
			GLfloat normal[] = {1.0, -1.0, 1.0};
			GLfloat material_diffuse[] =	{0.43 * blue[0], 0.47 * blue[1], 0.55 * blue[2], 1.0};
			GLfloat material_specular[] =	{0.71 * blue[0], 0.75 * blue[1], 0.75 * blue[2], 1.0};
			glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
			glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);
			
			glNormal3fv(normal);
			glTexCoord2f(0.5, 1.0);
			glVertex3fv(vertices[0]);	// top
			glTexCoord2f(1.0, 0.0);
			glVertex3fv(vertices[2]);
			glTexCoord2f(0.0, 0.0);
			glVertex3fv(vertices[3]);
		}
		glEnd();

		glBegin(GL_POLYGON);
		{
			GLfloat normal[] = {1.0, 1.0, 1.0};
			GLfloat material_diffuse[] =	{0.43 * yellow[0], 0.47 * yellow[1], 0.55 * yellow[2], 1.0};
			GLfloat material_specular[] =	{0.71 * yellow[0], 0.75 * yellow[1], 0.75 * yellow[2], 1.0};
			glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
			glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);
			
			glNormal3fv(normal);
			glTexCoord2f(0.5, 1.0);
			glVertex3fv(vertices[0]);	// top
			glTexCoord2f(1.0, 0.0);
			glVertex3fv(vertices[3]);
			glTexCoord2f(0.0, 0.0);
			glVertex3fv(vertices[4]);
		}
		glEnd();

		glBegin(GL_POLYGON);
		{
			GLfloat normal[] = {-1.0, 1.0, 1.0};
			GLfloat material_diffuse[] =	{0.43 * magenta[0], 0.47 * magenta[1], 0.55 * magenta[2], 1.0};
			GLfloat material_specular[] =	{0.71 * magenta[0], 0.75 * magenta[1], 0.75 * magenta[2], 1.0};
			glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
			glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);
			
			glNormal3fv(normal);
			glTexCoord2f(0.5, 1.0);
			glVertex3fv(vertices[0]);	// top
			glTexCoord2f(1.0, 0.0);
			glVertex3fv(vertices[4]);
			glTexCoord2f(0.0, 0.0);
			glVertex3fv(vertices[1]);
		}
		glEnd();

		glBegin(GL_POLYGON);	// square ground face
		{
			GLfloat normal[] = {0.0, 0.0, -1.0};
			GLfloat material_diffuse[] =	{0.43 * cyan[0], 0.47 * cyan[1], 0.55 * cyan[2], 1.0};
			GLfloat material_specular[] =	{0.71 * cyan[0], 0.75 * cyan[1], 0.75 * cyan[2], 1.0};

			glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
			glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);

			glNormal3fv(normal);
			glTexCoord2f(1.0 * tiles[0], 0.0 * tiles[1]);
			glVertex3fv(vertices[1]);
			glTexCoord2f(0.0 * tiles[0], 0.0 * tiles[1]);
			glVertex3fv(vertices[4]);
			glTexCoord2f(0.0 * tiles[0], 1.0 * tiles[1]);
			glVertex3fv(vertices[3]);
			glTexCoord2f(1.0 * tiles[0], 1.0 * tiles[1]);
			glVertex3fv(vertices[2]);
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);

	glEndList();
	glPopMatrix();
	glPopAttrib();

	// restore the previous material properties
	glMaterialfv(GL_FRONT, GL_DIFFUSE, diffuse);
	glMaterialfv(GL_FRONT, GL_SPECULAR, specular);

}

/*
 * Draws a simple 2-2-2 cube around the matrix' origin.
 */
void createCube(GLenum shadeModel)
{
	int retval;

    // set the shading model
	glShadeModel(shadeModel);

	retval = load((char **)cube_files, sizeof(cube_files) / sizeof(char*), CUBE);
	switch(retval)
	{
		case E_INCOMPLETE:
			fprintf(stdout, "Cube model files incomplete (unexpected EOF).\n");
			exit(1);
		case E_POLYGON:
			fprintf(stdout, "Unsupported polygon type in cube model file(s).\n");
			exit(1);
		case E_FOPEN:
			fprintf(stdout, "Unable to open one or more cube definition files.\n");
			exit(1);
		default:
			if(BEDUG) fprintf(stdout, "Cube model with %d vertices successfully read.\n", retval);
			break;
	}
}

/*
 * Uses the object loader to load all the F16 files and store it in a call
 * list.
 */
void createF16(GLenum shadeModel)
{
	int retval;

    // set the shading model
	glShadeModel(shadeModel);

	retval = load((char **)f16_files, sizeof(f16_files) / sizeof(char*), F16);
	switch(retval)
	{
		case E_INCOMPLETE:
			fprintf(stdout, "F16 files incomplete (unexpected EOF).\n");
			exit(1);
		case E_POLYGON:
			fprintf(stdout, "Unsupported polygon type in F16 file(s).\n");
			exit(1);
		case E_FOPEN:
			fprintf(stdout, "Unable to open one or more F16 definition files.\n");
			exit(1);
		default:
			if(BEDUG) fprintf(stdout, "F16 model with %d vertices successfully read.\n", retval);
			break;
	}

}

/*
 * This was used during development to draw the 3 axis in the scene. This was
 * later disabled.
 */
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

/*
 * Creates a "light bulb" around the origin. This is basically just light
 * emitting, yellow sphere (emitting white light), with a "fitting". Two of
 * these bulbs represent the real GL light positions in the scene.
 */
void drawLight()
{
	GLUquadricObj *sphereObj = gluNewQuadric();
	GLUquadricObj *cylinderObj = gluNewQuadric();
	GLfloat diffuse[4];
	GLfloat specular[4];
	GLfloat material_diffuse[] =	{0.0, 0.0, 0.0, 0.0};
	GLfloat material_specular[] =	{0.0, 0.0, 0.0, 0.0};
	GLfloat material_emission[] =	{0.9, 0.9, 0.1, 1.0};

	glGetMaterialfv(GL_FRONT, GL_DIFFUSE, (GLfloat*)diffuse);
	glGetMaterialfv(GL_FRONT, GL_SPECULAR, (GLfloat*)specular);
	glPushAttrib(GL_ALL_ATTRIB_BITS);	// good practice
	glPushMatrix();

	glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
	glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);
	glMaterialfv(GL_FRONT, GL_EMISSION, material_emission);

	gluSphere(sphereObj, 0.3, 8, 8);

	material_diffuse[0] = 0.4;
	material_diffuse[1] = 0.4;
	material_diffuse[2] = 0.4;
	material_diffuse[3] = 0.0;
	material_specular[0] = 0.4;
	material_specular[1] = 0.4;
	material_specular[2] = 0.4;
	material_specular[3] = 0.0;
	material_emission[0] = 0.0;
	material_emission[1] = 0.0;
	material_emission[2] = 0.0;
	material_emission[3] = 0.0;
	glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
	glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);
	glMaterialfv(GL_FRONT, GL_EMISSION, material_emission);

	glTranslatef(0.0, 0.0, -0.6);
	gluCylinder(cylinderObj, 0.15, 0.15, 0.6, 10.0, 10.0);

	glPopMatrix();
	glPopAttrib();
	// restore the previous material properties
	glMaterialfv(GL_FRONT, GL_DIFFUSE, diffuse);
	glMaterialfv(GL_FRONT, GL_SPECULAR, specular);
}

/*
 * Draws the rotated pyramid.
 */
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
	glCallList(F16);

	glPopMatrix();
}
