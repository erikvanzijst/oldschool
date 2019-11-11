/*
 * Computer Graphics Course 2004
 *
 * Exercise 1: the basics
 *
 * Erik van Zijst - erik@marketxs.com - 05.oct.2004
 */

#include <GL/glut.h>
#include <stdio.h>
#include <util/glutil.h>

/* function prototypes */
void keyboard(unsigned char key, int x, int y);
void mouse(int button, int state, int x, int y);
void passiveMotionTracking(int x, int y);
void motionTracking(int x, int y);
void top_menu(int selection);
void sub_menu1(int selection);
void wireframe_color(int selection);
void wireframe_thickness(int selection);
void myInit(void);
void display(void);
void reshapeFunc(int w, int h);
void idle(void);
void createSquare(void);
void createWireframe(void);

#define SQUARE 1
#define WIREFRAME 2
#define THICK 3.0
#define THIN 0.1

// we define some standard colors for convenience
const GLfloat red[] = {1.0, 0.0, 0.0};
const GLfloat yellow[] = {1.0, 1.0, 0.0};
const GLfloat cyan[] = {0.0, 1.0, 1.0};
const GLfloat white[] = {1.0, 1.0, 1.0};
const GLfloat black[] = {0.0, 0.0, 0.0};
const GLfloat blue[] = {0.0, 0.0, 1.0};
const GLfloat green[] = {0.0, 1.0, 0.0};
const GLfloat magenta[] = {1.0, 0.0, 1.0};

float rotation = 0.0;
double lastTime = 0.0;
int rotating = 1;
GLfloat *wireframeColor = (GLfloat *) blue;	// blue by default
GLfloat wireframeThickness = THIN;

/*
 * Called when the window was resized. The function defines a viewport and
 * an orthographic viewing volume that keeps its aspect ratio.
 */
void reshapeFunc(int w, int h)
{
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();

    if (w <= h)
        glOrtho(-10.0, 10.0, -10.0 * (GLfloat) h / (GLfloat) w,
            10.0 * (GLfloat) h / (GLfloat) w, -1.0, 1.0);
    else
        glOrtho(-10.0 * (GLfloat) w / (GLfloat) h,
            10.0 * (GLfloat) w / (GLfloat) h, -10.0, 10.0, -1.0, 1.0);

	glMatrixMode(GL_MODELVIEW);
}

/*
 * Called by opengl when the next scene needs to be rendered.
 */
void display(void)
{
	glClearColor(0.0, 0.0, 0.70, 0.0);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glLoadIdentity();
	glCallList(SQUARE);	// draw the square */

	glRotatef(rotation, 0.0, 0.0, 1.0);
	glColor3fv(wireframeColor);
	glLineWidth(wireframeThickness);
	glCallList(WIREFRAME);	/* draw wireframe */

	glFlush();
	glutSwapBuffers();
}

/*
 * Called by opengl when the runtime system is idle. Our implementation uses
 * it to compute the rotation degree and then displays the next scene.
 */
void idle(void)
{
	double interval, now;
	float offset;
	
	if(rotating)
	{
		now = Wallclock();
		interval = now - lastTime;

		if(interval > 3.0)
		{
			fprintf(stdout, "Rotation completed in %f seconds\n", interval);
			lastTime = now;
			while(interval > 3.0)
			{
				interval -= 3.0;
			}
		}

		offset = interval / 3.0;	// 0 <= offset < 3.0
		offset = offset * 360.0;	// convert proportional offset to degrees
		rotation = offset;
	}

	glutPostRedisplay();
}

/*
 * Keyboard event handler called by opengl when a key is pressed.
 * Our application recognized the following keys: 'q', 'Q' and 'Esc'
 * to terminate the program and 'spacebar' to toggle the rotation of
 * the wireframe.
 */
void keyboard(unsigned char key, int x, int y)
{
	static double interval;

	switch(key)
	{
		case 'q':
		case 'Q':
		case 27:	// ESC
			printf("User requested program termination.\n");
			exit(0);
		case 32:	// spacebar - toggle rotation
			if(rotating)
			{
				// switch off rotation by first recording exactly how long
				// the wireframe had rotated and then turning the rotation off
				interval = Wallclock() - lastTime;
				rotating = 0;
				fprintf(stdout, "Rotation paused.\n");
			}
			else
			{
				// turn rotation on by using the recorded interval to derive
				// the correct value for the global lastTime variable
				lastTime = Wallclock() - interval;
				rotating = 1;
				fprintf(stdout, "Rotation continued.\n");
			}
			break;
		default:
			fprintf(stdout, "Unused key (%d) ignored.\n", key);
			break;
	}
}

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
}

/*
 * Event handler that is called by opengl everytime the mouse is moved
 * to a new location inside the application window, while one or more
 * mouse buttons are pressed.
 * We only print a line of output to the console.
 */
void motionTracking(int x, int y)
{
	fprintf(stdout, "Mouse is moved to (%d, %d) with one or more buttons pressed.\n", x, y);
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
 * This function is called from the program's main() routine at startup.
 * It takes care of one-time initialization.
 */
void myInit(void)
{
	int sub_menu1_id, wireframe_menu_id, thickness_menu_id;

	/* create the context menu */
	sub_menu1_id = glutCreateMenu(sub_menu1);
	glutAddMenuEntry("say something", 1);
	glutAddMenuEntry("say something else", 2);

	wireframe_menu_id = glutCreateMenu(wireframe_color);
	glutAddMenuEntry("yellow", 1);
	glutAddMenuEntry("cyan", 2);
	glutAddMenuEntry("white", 3);
	glutAddMenuEntry("black", 4);
	glutAddMenuEntry("blue", 5);
	glutAddMenuEntry("green", 6);
	glutAddMenuEntry("magenta", 7);
	glutAddMenuEntry("red", 8);

	thickness_menu_id = glutCreateMenu(wireframe_thickness);
	glutAddMenuEntry("thin", 1);
	glutAddMenuEntry("thick", 2);

	glutCreateMenu(top_menu);
	glutAddMenuEntry("quit", 1);
	glutAddSubMenu("words of wisdom", sub_menu1_id);
	glutAddSubMenu("wireframe color", wireframe_menu_id);
	glutAddSubMenu("wireframe line", thickness_menu_id);
	glutAttachMenu(GLUT_RIGHT_BUTTON);

	/* create the call lists */
	createSquare();
	createWireframe();

	/* initialize the rotation clock */
	lastTime = Wallclock();
}

/*
 * Event handler that is called by opengl when the user selects one
 * of the options from the right-button context menu. Our implementation
 * recognized only a single event for termination the program.
 */
void top_menu(int selection)
{
	switch(selection)
	{
		case 1:
			fprintf(stdout, "User selected quit from the context menu. Terminating program.\n");
			exit(0);
		default:
			// assertion error
			fprintf(stdout, "Unknown menu item selected (%d).\n", selection);
			break;
	}
}

/*
 * Event handler that is called by opengl when the user selects an
 * entry from the submenu titled "words of wisdom".
 */
void sub_menu1(int selection)
{
	switch(selection)
	{
		case 1:
			fprintf(stdout, "Something...\n");
			break;
		case 2:
			fprintf(stdout, "Something else...\n");
			break;
		default:
			// assertion error
			fprintf(stdout, "Unknown menu item selected (%d).\n", selection);
			break;
	}
}

/*
 * Changes the color for the wireframe on the fly. This is accessible from
 * the "wireframe color" submenu inside the right-button context menu.
 */
void wireframe_color(int selection)
{
	const char *string = "Changing the wireframe color to %s\n";
	char *_color;

	switch(selection)
	{
		case 1:	// make the wireframe yellow
			wireframeColor = (GLfloat *) yellow;
			_color = "yellow";
			break;
		case 2:	// make the wireframe cyan
			wireframeColor = (GLfloat *) cyan;
			_color = "cyan";
			break;
		case 3:	// make the wireframe white
			wireframeColor = (GLfloat *) white;
			_color = "white";
			break;
		case 4:	// make the wireframe black
			wireframeColor = (GLfloat *) black;
			_color = "black";
			break;
		case 5:	// make the wireframe blue
			wireframeColor = (GLfloat *) blue;
			_color = "blue";
			break;
		case 6:	// make the wireframe green
			wireframeColor = (GLfloat *) green;
			_color = "green";
			break;
		case 7:	// make the wireframe magenta
			wireframeColor = (GLfloat *) magenta;
			_color = "magenta";
			break;
		case 8:	// make the wireframe red
			wireframeColor = (GLfloat *) red;
			_color = "red";
			break;
		default:
			_color = "unknown";
			break;
	}

	fprintf(stdout, string, _color);
}

/*
 * Event handler behind the "wireframe thickness" submenu that is
 * accessible from the right-button context menu. The implementation
 * changes the wireframe's line from thin to thick on the fly.
 */
void wireframe_thickness(int selection)
{
	const char *string = "Changing the wireframe's line to %s.\n";
	char *state;

	if(selection == 1)
	{
		wireframeThickness = THIN;
		state = "thin";
	}
	else
	{
		wireframeThickness = THICK;
		state = "thick";
	}

	fprintf(stdout, string, state);
}

/*
 * Creates a solid, red square from the following points: (-5,-5,0), (-5,5,0),
 * (5,5,0), (5,-5,0) and stores it in a call list.
 */
void createSquare(void)
{
	glNewList(SQUARE, GL_COMPILE);
		glBegin(GL_POLYGON);
			glColor3fv(red);
			glVertex3d(-5, -5, 0);
			glVertex3d(-5, 5, 0);
			glVertex3d(5, 5, 0);
			glVertex3d(5, -5, 0);
		glEnd();
	glEndList();
}

/*
 * Creates the wireframe. The display() function is responsible for setting
 * the color. The wireframe's definition is stored in a call list and has the
 * same dimensions as the red square.
 */
void createWireframe(void)
{
	glNewList(WIREFRAME, GL_COMPILE);
		glBegin(GL_LINE_LOOP);
			glVertex3d(-5, -5, 0);
			glVertex3d(-5, 5, 0);
			glVertex3d(5, 5, 0);
			glVertex3d(5, -5, 0);
		glEnd();
	glEndList();
}

/*
 * Th program's main routine. Creates the application window, initializes
 * some opengl environment properties, calls the program's init method for
 * creating the context menu and objects and then enters the opengl main
 * loop.
 */
int main(int argc, char **argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode (GLUT_RGB | GLUT_DOUBLE);
	glutInitWindowSize (800,600);
	glutInitWindowPosition(0,0);
    glutCreateWindow ("exercise 1");

	glutIdleFunc(idle);
	glutDisplayFunc(display);
	glutKeyboardFunc(keyboard);
	glutMouseFunc(mouse);
	glutMotionFunc(motionTracking);
	glutPassiveMotionFunc(passiveMotionTracking);
	glutReshapeFunc(reshapeFunc);
	myInit();

	glutMainLoop();
}
