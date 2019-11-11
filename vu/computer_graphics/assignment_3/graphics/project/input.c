/*
 *
 *
 */

#include "globals.h"
#include "input.h"

extern Player p1, p2;
extern Monster dino;
extern BOOL animation;

void topMenu(int selection);

/*
 * Catches the quit command as well as the "w,a,s,d" keys that are used to
 * control the first player in the upper screen.
 */
void keyboard(unsigned char key, int x, int y)
{
	static double monster_idetime, p1_idletime, p2_idletime;

	switch(key)
	{
		case 'q':
		case 'Q':
		case 27:	// ESC
			printf("User requested program termination.\n");
			exit(0);
		case 'w':	// p1 forward
			p1.forward = TRUE;
			break;
		case 's':	// p1 reverse
			p1.reverse = TRUE;
			break;
		case 'a':	// p1 left
			p1.left = TRUE;
			break;
		case 'd':	// p1 right
			p1.right = TRUE;
			break;
		case 'e':	// camera view toggle
			if(p1.camera.view == CAR) p1.camera.view = HELICOPTER;
			else p1.camera.view = CAR;
			p1.moved = TRUE;
			fprintf(stdout, "Switching camera perspective for player 1.\n");
			break;
		case 127:	// delete
			if(p2.camera.view == CAR) p2.camera.view = HELICOPTER;
			else p2.camera.view = CAR;
			p2.moved = TRUE;
			fprintf(stdout, "Switching camera perspective for player 2.\n");
			break;
		case 32:	// spacebar - toggle all animation
			{
				double now = Wallclock();

				switch(animation) {
					case TRUE:
						monster_idetime = now - dino.last_computation_time;
						p1_idletime = now - p1.location.last_computation_time;
						p2_idletime = now - p2.location.last_computation_time;
						animation = FALSE;
						fprintf(stdout, "All animation suspended.\n");
						break;
					case FALSE:
						dino.last_computation_time = now - monster_idetime;
						p1.location.last_computation_time = now - p1_idletime;
						p2.location.last_computation_time = now - p2_idletime;
						animation = TRUE;
						fprintf(stdout, "All animation resumed.\n");
						break;
				}
			}
			break;
		default:
			fprintf(stdout, "Unused key (%d) pressed.\n", key);
			break;
	}
}

/*
 * Called when a key is released.
 */
void keyboardUp(unsigned char key, int x, int y)
{
	switch(key)
	{
		case 'w':	// p1 forward
			p1.forward = FALSE;
			break;
		case 's':	// p1 reverse
			p1.reverse = FALSE;
			break;
		case 'a':	// p1 left
			p1.left = FALSE;
			break;
		case 'd':	// p1 right
			p1.right = FALSE;
			break;
		default:
			fprintf(stdout, "Unused key (%d) released.\n", key);
			break;
	}
}

/*
 * Catches the arrow keys. These keys are used by the second player in the
 * bottom screen.
 */
void specialFunc(int key, int x, int y)
{
	switch(key)
	{
		case GLUT_KEY_UP:		// p2 forward
		case '8':
			p2.forward = TRUE;
			break;
		case GLUT_KEY_DOWN:		// p2 reverse
		case '5':
			p2.reverse = TRUE;
			break;
		case GLUT_KEY_LEFT:		// p2 left
		case '4':
			p2.left = TRUE;
			break;
		case GLUT_KEY_RIGHT:	// p2 right
		case '6':
			p2.right = TRUE;
			break;
		default:
			fprintf(stdout, "Unused special key (%d) pressed.\n", key);
			break;
	}
}

/*
 * Catches the arrow keys. These keys are used by the second player in the
 * bottom screen.
 */
void specialUpFunc(int key, int x, int y)
{
	switch(key)
	{
		case GLUT_KEY_UP:		// p2 forward
		case '8':
			p2.forward = FALSE;
			break;
		case GLUT_KEY_DOWN:		// p2 reverse
		case '5':
			p2.reverse = FALSE;
			break;
		case GLUT_KEY_LEFT:		// p2 left
		case '4':
			p2.left = FALSE;
			break;
		case GLUT_KEY_RIGHT:	// p2 right
		case '6':
			p2.right = FALSE;
			break;
		default:
			fprintf(stdout, "Unused special key (%d) released.\n", key);
			break;
	}
}

void mouse(int button, int state, int x, int y)
{
}

void passiveMotionTracking(int x, int y)
{
}

void motionTracking(int x, int y)
{
}

void menuDifficulty(int selection) {

	switch(selection) {

		case 1:
			dino.speed = 2;
			break;
		case 2:
			dino.speed = 4;
			break;
		case 3:
			dino.speed = 6;
			break;
	}
}

/*
 * Creates the right-button context menu.
 */
void createMenu(void)
{
	int sub_difficulty;

	sub_difficulty = glutCreateMenu(menuDifficulty);
	glutAddMenuEntry("easy", 1);
	glutAddMenuEntry("medium", 2);
	glutAddMenuEntry("hard", 3);

	glutCreateMenu(topMenu);
	glutAddMenuEntry("quit", 1);
	glutAddMenuEntry("toggle collision box", 2);
	glutAddSubMenu("difficulty", sub_difficulty);
	glutAttachMenu(GLUT_RIGHT_BUTTON);
}

/*
 * Event handler that is called by opengl when the user selects one
 * of the options from the right-button context menu. Our implementation
 * recognized only a single event for termination the program.
 */
void topMenu(int selection)
{
	switch(selection)
	{
		case 1:
			fprintf(stdout, "User selected quit from the context menu. Terminating program.\n");
			exit(0);
		case 2:
			if(p1.bounding_box) {
				p1.bounding_box = p2.bounding_box = FALSE;
			} else {
				p1.bounding_box = p2.bounding_box = TRUE;
			}
			break;
		default:
			// assertion error
			fprintf(stdout, "Unknown menu item selected (%d).\n", selection);
			break;
	}
}
