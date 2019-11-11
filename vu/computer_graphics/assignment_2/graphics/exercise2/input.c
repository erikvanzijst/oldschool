/*
 * Computer Graphics Course 2004
 *
 * Exercise 2
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 06.oct.2004
 */

#include "globals.h"
#include "input.h"

void reshape(int w, int h);
void trackMouse(int x, int y);

extern int width;
extern int height;

extern GLfloat zoom;
extern int mouseLocation[];
extern GLfloat matrix_angle_y;
extern int animation;
extern int animate_pyramid;	// only 1 when left mouse button is pressed
extern double animationStartPyramid;
extern double animationStartCubeOrbit;
extern double animationStartCube;
extern double animationStartF16Orbit;

BOOL tracking = TRUE;

/*
 * Event handler for mouse button events. Every time a button is pressed
 * or released, we print a line of output to the console. Pressing the left
 * button makes the pyramid rotate.
 */
void mouse(int button, int state, int x, int y)
{
	static double intervalPyramidRotation;

	fprintf(stdout, "%s mouse button %s. Pointer at location (%d, %d)\n",
		( button==GLUT_LEFT_BUTTON ? "Left" : (button==GLUT_RIGHT_BUTTON ? "Right" : "Middle") ),
		(state == GLUT_DOWN ? "pressed" : (state == GLUT_UP ? "released" : "touched")),
		x, y);

	if(button == GLUT_LEFT_BUTTON)
	{
		if(state == GLUT_DOWN)
		{
			// restore rotation interval and resume pyramid rotation
			animationStartPyramid = Wallclock() - intervalPyramidRotation;
			animate_pyramid = 1;
		}
		else
		{
			// save the current rotation interval and stop pyramid rotation
			intervalPyramidRotation = Wallclock() - animationStartPyramid;
			animate_pyramid = 0;
		}
	}
}

/*
 * Event handler that is called by opengl everytime the mouse is moved
 * to a new location inside the application window, while one or more
 * mouse buttons are pressed.
 * As a little something extra, the application tracks the motion of the
 * mouse when the middle button is pressed (where available) and maps the
 * rotation of the world's X- and Y-axis to the mouse's axis.
 */
void motionTracking(int x, int y)
{
	fprintf(stdout, "Mouse is moved to (%d, %d) with one or more buttons pressed.\n", x, y);
	trackMouse(x, y);
}

/*
 * Event handler that is called by opengl everytime the mouse is moved
 * to a new location inside the application window, while no mouse
 * button is pressed.
 * We print a line of output to the console and call trackMouse().
 */
void passiveMotionTracking(int x, int y)
{
	fprintf(stdout, "Mouse is moved to (%d, %d) with no buttons pressed.\n", x, y);
	trackMouse(x, y);
}

/*
 * We run this function everytime the mouse is moved. When moved left or
 * right, we move the camera in a circle around our scene. During this
 * movement the camera stays targeted at the origin. When the mouse is moved
 * up or down, we zoom in or out. Zooming is done by changing the fovy
 * argument to gluPerspective(). This makes it a "real" zoom in that the
 * camera stays put.
 */
void trackMouse(int x, int y)
{
	int angle_y = (int)matrix_angle_y;

	if(mouseLocation[0] == -1 && mouseLocation[1] == -1)
	{
		mouseLocation[0] = x;
		mouseLocation[1] = y;
	}

	if(tracking)
	{
		/*
		 * Compute how far we should zoom.
		 */
		zoom += (GLfloat)(y - mouseLocation[1]);
		if(zoom >= 180.0)
		{
			zoom = 179.0;
		}
		else if(zoom < 1.0)
		{
			zoom = 1.0;
		}
		fprintf(stdout, "Zooming %s to %f.\n", (y - mouseLocation[1] > 0 ? "in" : (y - mouseLocation[1] < 0 ? "out" : "")), zoom);
		mouseLocation[1] = y;


		/*
		 * Calculate the mouse travel (x) since the previous location
		 * and add that distance as degrees to the rotation of the camera.
		 */
		angle_y += x - mouseLocation[0];
		angle_y %= 360;
		while(angle_y < 0)
		{
			angle_y += 360;
			angle_y = angle_y % 360;
		}
		matrix_angle_y = (GLfloat)angle_y;
		mouseLocation[0] = x;
		reshape(width, height);
	}
}

/*
 * Keyboard event handler called by opengl when a key is pressed.
 * Our application recognized the following keys: 'q', 'Q' and 'Esc'
 * to terminate the program and 'spacebar' to toggle the rotation of
 * the wireframe.
 */
void keyboard(unsigned char key, int x, int y)
{
	static double intervalCubeOrbit, intervalCubeRotation, intervalF16Orbit;

	switch(key)
	{
		case 'q':
		case 'Q':
		case 27:	// ESC
			printf("User requested program termination.\n");
			exit(0);
		case 32:	// spacebar - toggle rotation
			if(animation)
			{
				// switch off all animation
				intervalCubeOrbit = Wallclock() - animationStartCubeOrbit;
				intervalCubeRotation = Wallclock() - animationStartCube;
				intervalF16Orbit = Wallclock() - animationStartF16Orbit;

				animation = 0;
				fprintf(stdout, "All animation suspended.\n");
			}
			else
			{
				// turn all animation back on
				animationStartCubeOrbit = Wallclock() - intervalCubeOrbit;
				animationStartCube = Wallclock() - intervalCubeRotation;
				animationStartF16Orbit = Wallclock() - intervalF16Orbit;

				animation = 1;
				fprintf(stdout, "Animation continued.\n");
			}
			break;
		default:
			fprintf(stdout, "Unused key (%d) ignored.\n", key);
			break;
	}
}
