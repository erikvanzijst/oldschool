/*
 * Computer Graphics Course 2004
 *
 * Exercise 2
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 06.oct.2004
 */

#include "globals.h"
#include "input.h"

extern int mouseLocation[];
extern BOOL middleButtonDown;
extern GLfloat matrix_angle_x;
extern GLfloat matrix_angle_y;
extern int animation;
extern double animationStartPyramid;
extern double animationStartCubeOrbit;
extern double animationStartCube;
extern double animationStartF16Orbit;

/*
 * Event handler for mouse button events. Every time a button is pressed
 * or released, we print a line of output to the console.
 */
void mouse(int button, int state, int x, int y)
{
	fprintf(stdout, "%s mouse button %s. Pointer at location (%d, %d)\n",
		( button==GLUT_LEFT_BUTTON ? "Left" : (button==GLUT_RIGHT_BUTTON ? "Right" : "Middle") ),
		(state == GLUT_DOWN ? "pressed" : (state == GLUT_UP ? "released" : "touched")),
		x, y);

	if(button == GLUT_MIDDLE_BUTTON)
	{
		if(state == GLUT_DOWN)
		{
			// middle button pressed down; mark the cursor and track motion
			mouseLocation[0] = x;
			mouseLocation[1] = y;
			middleButtonDown = TRUE;
		}
		else
		{
			middleButtonDown = FALSE;
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
	int angle_x, angle_y;
	angle_x = (int)matrix_angle_x;
	angle_y = (int)matrix_angle_y;

	fprintf(stdout, "Mouse is moved to (%d, %d) with one or more buttons pressed.\n", x, y);

	/*
	 * Calculate the mouse travel (x and y) since the previous location
	 * and add that distance as degrees to the rotation of the world's
	 * X- and Y-axis.
	 */
	if(middleButtonDown)
	{
		angle_x += y - mouseLocation[1];
		angle_x %= 360;
		while(angle_x < 0)
		{
			angle_x += 360;
			angle_x = angle_x % 360;
		}
		matrix_angle_x = (GLfloat)angle_x;
		mouseLocation[1] = y;

		angle_y += x - mouseLocation[0];
		angle_y %= 360;
		while(angle_y < 0)
		{
			angle_y += 360;
			angle_y = angle_y % 360;
		}
		matrix_angle_y = (GLfloat)angle_y;
		mouseLocation[0] = x;
	}
}

/*
 * Event handler that is called by opengl everytime the mouse is moved
 * to a new location inside the application window, while no mouse
 * button is pressed.
 * We only print a line of output to the console.
 */
void passiveMotionTracking(int x, int y)
{
	fprintf(stdout, "Mouse is moved to (%d, %d) with no buttons pressed.\n", x, y);
}

/*
 * Keyboard event handler called by opengl when a key is pressed.
 * Our application recognized the following keys: 'q', 'Q' and 'Esc'
 * to terminate the program and 'spacebar' to toggle the rotation of
 * the wireframe.
 */
void keyboard(unsigned char key, int x, int y)
{
	static double intervalPyramidRotation, intervalCubeOrbit,
		intervalCubeRotation, intervalF16Orbit;

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
				intervalPyramidRotation = Wallclock() - animationStartPyramid;
				intervalCubeOrbit = Wallclock() - animationStartCubeOrbit;
				intervalCubeRotation = Wallclock() - animationStartCube;
				intervalF16Orbit = Wallclock() - animationStartF16Orbit;

				animation = 0;
				fprintf(stdout, "All animation suspended.\n");
			}
			else
			{
				// turn all animation back on
				animationStartPyramid = Wallclock() - intervalPyramidRotation;
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
