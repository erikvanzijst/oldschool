/*
 * Computer Graphics Course 2004
 *
 * Exercise 2
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 06.oct.2004
 */

#include "globals.h"
#include "objects.h"
#include "menu.h"
#include "input.h"

GLfloat pyramid_angle_x = 0.0;
GLfloat pyramid_angle_y = 0.0;
GLfloat pyramid_angle_z = 0.0;

GLfloat matrix_angle_x = 0.0;
GLfloat matrix_angle_y = 0.0;
GLfloat cube_orbit_angle = 0.0;
GLfloat cube_angle = 0.0;
GLfloat f16_orbit_angle = 0.0;

static int pyramidRotation = 1;
int animation = 1;
double animationStartPyramid = 0.0;
double animationStartCubeOrbit = 0.0;
double animationStartCube = 0.0;
double animationStartF16Orbit = 0.0;
BOOL middleButtonDown = FALSE;
int mouseLocation[2];	// used to store the current location of the mouse

/* function prototypes */
void myInit(void);
void display(void);
void myReshape(int w, int h);
void idle(void);
void computePyramidRotation(void);
void computeCubeOrbit(void);
void computeCubeRotation(void);
void computeF16Orbit(void);

extern GLfloat pyramid_angle_x;

void display(void)
{
	glClearColor(0.0, 0.0, 0.0, 0.0);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glLoadIdentity();
	glRotatef(matrix_angle_x, 1.0, 0.0, 0.0);
	glRotatef(matrix_angle_y, 0.0, 1.0, 0.0);

	drawMatrix();
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

void myReshape(int w, int h)
{
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();

    if (w <= h)
		gluPerspective(45, w/h, 0.1, 1000.0);
//        glFrustum(-VOLUME_X, VOLUME_X, -VOLUME_Y * (GLfloat) h / (GLfloat) w,
//            VOLUME_Y * (GLfloat) h / (GLfloat) w, 20.0, 300.0);
    else
		gluPerspective(45, w/h, 0.1, 1000.0);
//        glFrustum(-VOLUME_X * (GLfloat) w / (GLfloat) h,
//            VOLUME_X * (GLfloat) w / (GLfloat) h, -VOLUME_Y, VOLUME_Y, 20.0, 300.0);
	glTranslatef(0.0,0.0,-22.5);

    glMatrixMode(GL_MODELVIEW);
}

void idle(void)
{
	computePyramidRotation();
	computeCubeOrbit();
	computeCubeRotation();
	computeF16Orbit();

	glutPostRedisplay();
}

/*
 * Computes the current value for orbit_angle.
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
 * and pyramid_angle_z.
 */
void computePyramidRotation()
{
	double interval, now;
	float offset;
	
	if(animation)
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
 * Computes the current value for orbit_angle.
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
    // Set the shading model
	glShadeModel(GL_SMOOTH);

	// Set the polygon mode to fill
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	glEnable(GL_DEPTH_TEST); /* Enable hidden--surface--removal */

	// register callbacks
	glutKeyboardFunc(keyboard);
	glutMouseFunc(mouse);
	glutMotionFunc(motionTracking);
	glutPassiveMotionFunc(passiveMotionTracking);
	glutIdleFunc(idle);
	glutDisplayFunc(display);
	glutReshapeFunc(myReshape);

	/* create the context menu */
	createMenu();

	/* create the static objects */
	createPyramid();
	createCube();
	createF16();
}

int main(int argc, char **argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize (800,600);
	glutInitWindowPosition(0,0);
    glutCreateWindow ("exercise 2");

	myInit();

	glutMainLoop();
}

