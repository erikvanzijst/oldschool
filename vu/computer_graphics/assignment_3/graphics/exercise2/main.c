/*
 * Computer Graphics Course 2004
 *
 * Exercise 2
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 24.nov.2004
 */

#include <math.h>
#include "globals.h"
#include "objects.h"
#include "menu.h"
#include "input.h"

GLfloat light1_position[] = { 4.0, 4.0, -3.0, 1.0 };
GLfloat light2_position[] = { -4.0, -4.0, 3.0, 1.0 };

GLfloat pyramid_angle_x = 0.0;
GLfloat pyramid_angle_y = 0.0;
GLfloat pyramid_angle_z = 0.0;

GLfloat zoom = 65;	// initial fovy for glPerspective()
int width = 800;
int height = 600;
GLfloat matrix_angle_y = 0.0;	// used to drag the camera with the mouse
GLfloat cube_orbit_angle = 0.0;
GLfloat cube_angle = 0.0;
GLfloat f16_orbit_angle = 0.0;

GLuint texture;	// name of our texture
static int pyramidRotation = 1;
int animation = 1;
int animate_pyramid = 0;	// only 1 when left mouse button is pressed
double animationStartPyramid = 0.0;
double animationStartCubeOrbit = 0.0;
double animationStartCube = 0.0;
double animationStartF16Orbit = 0.0;
int mouseLocation[] = {-1, -1};	// used to store the current location of the mouse

/* function prototypes */
void myInit(void);
void display(void);
void myReshape(int w, int h);
void computePyramidRotation(void);
void computeCubeOrbit(void);
void computeCubeRotation(void);
void computeF16Orbit(void);
double degrees_to_radians(const double degrees);

void display(void)
{
	glClearColor(0.0, 0.0, 0.2, 0.0);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glLoadIdentity();

//	drawMatrix();	// if enabled, draws a line for each axis

	// draw a "light bulb" in the scene representing GL_LIGHT1
	glPushMatrix();
	glTranslatef(light1_position[0], light1_position[1], light1_position[2]);
	glRotatef(90, 1.0, 0.0, 0.0);
	drawLight();
	glPopMatrix();

	// draw a "light bulb" in the scene representing GL_LIGHT2
	glPushMatrix();
	glTranslatef(light2_position[0], light2_position[1], light2_position[2]);
	glRotatef(90, 1.0, 0.0, 0.0);
	drawLight();
	glPopMatrix();

	drawPyramid();
	glRotatef(cube_orbit_angle, 0.0, 0.0, 1.0);	// orbit around Z-axis
	glTranslatef(0.0, DISTANCE, 0.0);
	drawCube();

	glRotatef(f16_orbit_angle, 0.0, 0.0, 1.0);	// orbit around the X-axis
	glTranslatef(0.0, ORBIT_F16, 0.0);
	drawF16();

	glFlush();
	glutSwapBuffers();
}

/*
 * Reshapes the screen. This function is also called when the mouse moves the
 * camera to a new position as this changes the fovy value of gluPerspective().
 */
void reshape(int w, int h)
{
	double radians;
	width = w;
	height = h;
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();

	gluPerspective(zoom, (float)w/(float)h, 0.1, 15.0);

	radians = degrees_to_radians(matrix_angle_y);
	gluLookAt(sin(radians) * 8.0, 0.0, cos(radians) * 8.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	glLightfv(GL_LIGHT1, GL_POSITION, light1_position);
	glLightfv(GL_LIGHT2, GL_POSITION, light2_position);

    glMatrixMode(GL_MODELVIEW);
}

/*
 * Simple utility function for converting our degrees to randians so they can
 * be fed to geometry functions like sin and cos.
 */
double degrees_to_radians(const double degrees)
{
	return degrees * (PI / 180);
}

/*
 * Here we compute all new animation angles and update the scene accordingly.
 */
void idle(void)
{
	computePyramidRotation();
	computeCubeOrbit();
	computeCubeRotation();
	computeF16Orbit();

	glutPostRedisplay();
}

/*
 * Computes the current value for orbit_angle in degrees.
 */
void computeCubeOrbit()
{
	double interval, now;
	float offset;
	
	if(animation)
	{
		now = Wallclock();
		interval = now - animationStartCubeOrbit;

		if(interval > ORBIT_TIME_CUBE)
		{
			fprintf(stdout, "Cube orbit completed in %f seconds\n", interval);
			animationStartCubeOrbit = now;
			while(interval > ORBIT_TIME_CUBE)
			{
				interval -= ORBIT_TIME_CUBE;
			}
		}

		offset = interval / ORBIT_TIME_CUBE;	// 0 <= offset < ORBIT_TIME_CUBE
		offset = offset * 360.0;	// convert proportional offset to degrees
		cube_orbit_angle = offset;
	}
}

/*
 * Computes the current value for f16_orbit_angle in degrees.
 */
void computeF16Orbit()
{
	double interval, now;
	float offset;
	
	if(animation)
	{
		now = Wallclock();
		interval = now - animationStartF16Orbit;

		if(interval > ORBIT_TIME_F16)
		{
			fprintf(stdout, "F16 orbit completed in %f seconds\n", interval);
			animationStartF16Orbit = now;
			while(interval > ORBIT_TIME_F16)
			{
				interval -= ORBIT_TIME_F16;
			}
		}

		offset = interval / ORBIT_TIME_F16;	// 0 <= offset < ORBIT_TIME_F16
		offset = offset * 360.0;	// convert proportional offset to degrees
		f16_orbit_angle = offset;
	}
}

/*
 * Computes the current values for pyramid_angle_x, pyramid_angle_y
 * and pyramid_angle_z in degrees.
 */
void computePyramidRotation()
{
	double interval, now;
	float offset;
	
	if(animate_pyramid)
	{
		now = Wallclock();
		interval = now - animationStartPyramid;

		if(interval > ROTATION_TIME_PYRAMID)
		{
			fprintf(stdout, "Pyramid rotation completed in %f seconds\n", interval);
			animationStartPyramid = now;
			while(interval > ROTATION_TIME_PYRAMID)
			{
				interval -= ROTATION_TIME_PYRAMID;
			}
		}

		offset = interval / ROTATION_TIME_PYRAMID;	// 0 <= offset < ROTATION_TIME_PYRAMID
		offset = offset * 360.0;	// convert proportional offset to degrees
		pyramid_angle_x = pyramid_angle_y = pyramid_angle_z = offset;
	}
}

/*
 * Computes the current value for cube_angle in degrees.
 */
void computeCubeRotation()
{
	double interval, now;
	float offset;
	
	if(animation)
	{
		now = Wallclock();
		interval = now - animationStartCube;

		if(interval > ROTATION_TIME_CUBE)
		{
			fprintf(stdout, "Cube rotation completed in %f seconds\n", interval);
			animationStartCube = now;
			while(interval > ROTATION_TIME_CUBE)
			{
				interval -= ROTATION_TIME_CUBE;
			}
		}

		offset = interval / ROTATION_TIME_CUBE;	// 0 <= offset < ROTATION_TIME_CUBE
		offset = offset * 360.0;	// convert proportional offset to degrees
		cube_angle = offset;
	}
}

void myInit(void)
{
	RGBImage *image;

	// Configure light types. We only use diffuse and specular.
	GLfloat light_ambient[] =	{0.0, 0.0, 0.0, 1.0};
	GLfloat light_diffuse[]	=	{1.0, 1.0, 1.0, 1.0};
	GLfloat light_specular[] =	{1.0, 1.0, 1.0, 1.0};

	// configure light reflection
	GLfloat material_ambient[] =	{0.0, 0.06, 0.0, 1.0};
	GLfloat material_diffuse[] =	{0.43, 0.47, 0.55, 1.0};
	GLfloat material_specular[] =	{0.71, 0.75, 0.75, 1.0};
	GLfloat material_emission[] =	{0.0, 0.0, 0.0, 0.0};
	GLfloat material_shininess[] =	{52.0};

	// apply the lighting properties
	glLightfv(GL_LIGHT1, GL_AMBIENT, light_ambient);
	glLightfv(GL_LIGHT1, GL_DIFFUSE, light_diffuse);
	glLightfv(GL_LIGHT1, GL_SPECULAR, light_specular);
	glLightfv(GL_LIGHT2, GL_AMBIENT, light_ambient);
	glLightfv(GL_LIGHT2, GL_DIFFUSE, light_diffuse);
	glLightfv(GL_LIGHT2, GL_SPECULAR, light_specular);

	// apply the material properties
	glMaterialfv(GL_FRONT, GL_AMBIENT, material_ambient);
	glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
	glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);
	glMaterialfv(GL_FRONT, GL_EMISSION, material_emission);
	glMaterialfv(GL_FRONT, GL_SHININESS, material_shininess);

	// turn on the light
	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT1);
	glEnable(GL_LIGHT2);

	// set the polygon mode to fill
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	glEnable(GL_DEPTH_TEST); /* Enable hidden--surface--removal */

	// register callbacks
	glutKeyboardFunc(keyboard);
	glutMouseFunc(mouse);
	glutMotionFunc(motionTracking);
	glutPassiveMotionFunc(passiveMotionTracking);
	glutIdleFunc(idle);
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);

	/* create the context menu */
	createMenu();

	/* set up our texture */
	glGenTextures(1, &texture);
	glBindTexture(GL_TEXTURE_2D, texture);	// select our texture
	// when texture area is small, bilinear filter the closest mipmap
	glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST );
	// when texture area is large, bilinear filter the original
	glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
	// the texture wraps over at the edges (repeat)
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

	image = LoadRGB(tex_file);	// texture loaded
	if(image == NULL)
	{
		fprintf(stdout, "Unable to load texture file (%s).\n", tex_file);
		exit(1);
	}
	else
	{
		fprintf(stdout, "Texture (%s) loaded. x = %d, y = %d, format = %d, components = %d\n", tex_file, image->sizeX, image->sizeY, image->format, image->components);
	}

	// build our texture mipmaps
	gluBuild2DMipmaps(GL_TEXTURE_2D, 3, image->sizeX, image->sizeY, GL_RGBA, GL_UNSIGNED_BYTE, image->data);

	/* create the static objects */
	createPyramid(GL_SMOOTH);
	createCube(GL_SMOOTH);
	createF16(GL_SMOOTH);

	/* initialize their rotation variables */
	animationStartPyramid = Wallclock();
	animationStartCubeOrbit = Wallclock();
	animationStartCube = Wallclock();
	animationStartF16Orbit = Wallclock();
}

int main(int argc, char **argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize (width,height);
	glutInitWindowPosition(0,0);
    glutCreateWindow ("exercise 2");

	myInit();

	glutMainLoop();
}
