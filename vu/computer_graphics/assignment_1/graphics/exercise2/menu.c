/*
 * Computer Graphics Course 2004
 *
 * Exercise 2
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 06.oct.2004
 */

#include "globals.h"
#include "menu.h"

void topMenu(int selection);
void saySomething(int selection);

void createMenu()
{
	int sub_menu1_id;

	sub_menu1_id = glutCreateMenu(saySomething);
	glutAddMenuEntry("say something", 1);
	glutAddMenuEntry("say something else", 2);

	glutCreateMenu(topMenu);
	glutAddMenuEntry("quit", 1);
	glutAddSubMenu("words of wisdom", sub_menu1_id);
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
void saySomething(int selection)
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
