/*
 * Computer Graphics Course 2004
 *
 * Exercise 2
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 06.oct.2004
 */

#include "globals.h"
#include "menu.h"
#include "objects.h"

extern GLfloat modulation;
extern GLfloat tiles[];	// allows for 4x2, 1x1, etc on bottom texture
extern BOOL tracking;
GLenum shadeModel = GL_SMOOTH;

void topMenu(int selection);
void saySomething(int selection);
void modulate(int selection);
void wrapping(int selection);
void shading(int selection);
void track(int selection);

/*
 * Creates the right-button context menu.
 */
void createMenu()
{
	int sub_menu1_id, sub_menu2_id, sub_menu3_id, sub_menu4_id, sub_menu5_id;

	sub_menu1_id = glutCreateMenu(saySomething);
	glutAddMenuEntry("say something", 1);
	glutAddMenuEntry("say something else", 2);

	sub_menu2_id = glutCreateMenu(modulate);
	glutAddMenuEntry("GL_MODULATE", 1);
	glutAddMenuEntry("GL_REPLACE", 2);
	glutAddMenuEntry("GL_BLEND", 3);
	glutAddMenuEntry("GL_DECAL", 4);

	sub_menu3_id = glutCreateMenu(wrapping);
	glutAddMenuEntry("1 x 1", 1);
	glutAddMenuEntry("1 x 0.5", 2);
	glutAddMenuEntry("4 x 2", 3);

	sub_menu4_id = glutCreateMenu(shading);
	glutAddMenuEntry("GL_SMOOTH", 1);
	glutAddMenuEntry("GL_FLAT", 2);

	sub_menu5_id = glutCreateMenu(track);
	glutAddMenuEntry("no mouse tracking pls", 1);
	glutAddMenuEntry("move and zoom follows mouse", 2);

	glutCreateMenu(topMenu);
	glutAddMenuEntry("quit", 1);
	glutAddSubMenu("shading model", sub_menu4_id);
	glutAddSubMenu("texture colors", sub_menu2_id);
	glutAddSubMenu("texture tiles", sub_menu3_id);
	glutAddSubMenu("words of wisdom", sub_menu1_id);
	glutAddSubMenu("zooming/moving", sub_menu5_id);
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
 * Allows the user to change the scene's shading module.
 */
void shading(int selection)
{
	switch(selection)
	{
		case 1:
			// use smooth shading
			shadeModel = GL_SMOOTH;
			// re-create the models
			createPyramid(shadeModel);
			createCube(shadeModel);
			createF16(shadeModel);

			fprintf(stdout, "Now using smooth shading.\n");
			idle();	// force re-display and updated animation values
			break;
		case 2:
			// use flat shading
			shadeModel = GL_FLAT;
			// re-create the models
			createPyramid(shadeModel);
			createCube(shadeModel);
			createF16(shadeModel);
			fprintf(stdout, "Now using flat shading.\n");
			idle();	// force re-display and updated animation values
			break;
		default:
			// assertion error
			fprintf(stdout, "Unknown menu item selected (%d).\n", selection);
			break;
	}
}

/*
 * Event handler that is called by opengl when the user selects an
 * entry from the submenu titled "words of wisdom". Kinda useless but I think
 * it's part of the requirements.
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

/*
 * Controls how the textures of the pyramid's surfaces interact with the
 * natural colors (material properties) of the surfaces.
 */
void modulate(int selection)
{
	switch(selection)
	{
		case 1:
			modulation = GL_MODULATE;
			createPyramid(shadeModel);
			fprintf(stdout, "Texture color modulation selected.\n");
			idle();	// force re-display and updated animation values
			break;
		case 2:
			modulation = GL_REPLACE;
			createPyramid(shadeModel);
			fprintf(stdout, "Texture color replacing selected.\n");
			idle();	// force re-display and updated animation values
			break;
		case 3:
			modulation = GL_BLEND;
			createPyramid(shadeModel);
			fprintf(stdout, "Texture color blending selected.\n");
			idle();	// force re-display and updated animation values
			break;
		case 4:
			modulation = GL_DECAL;
			createPyramid(shadeModel);
			fprintf(stdout, "Texture color decal selected.\n");
			idle();	// force re-display and updated animation values
			break;
		default:
			// assertion error
			fprintf(stdout, "Unknown menu item selected (%d).\n", selection);
			break;
	}
}

/*
 * Controls the number of texture tiles are mapped to the pyramid's ground
 * surface.
 */
void wrapping(int selection)
{
	switch(selection)
	{
		case 1:
			// full
			tiles[0] = tiles[1] = 1.0;
			fprintf(stdout, "Tile layout (1 x 1) selected.\n");
			break;
		case 2:
			// half - only the word "computer" is visible
			tiles[0] = -1.0;
			tiles[1] = -0.5;
			fprintf(stdout, "Tile layout (1 x 0.5) selected.\n");
			break;
		case 3:
			// 4 x 2
			tiles[0] = 4.0;
			tiles[1] = 2.0;
			fprintf(stdout, "Tile layout (4 x 2) selected.\n");
			break;
		default:
			// assertion error
			fprintf(stdout, "Unknown menu item selected (%d).\n", selection);
			break;
	}
	createPyramid(shadeModel);
	idle();	// force re-display and updated animation values
}

/*
 * Allows the user to toggle mouse tracking. When enabled (default at program
 * startup), the camera will move left/right according to the mouse and zoom
 * in and out when the mouse is moved up and down.
 */
void track(int selection)
{
	switch(selection)
	{
		case 1:
			tracking = FALSE;
			fprintf(stdout, "The camera will not respond to mouse movements.\n");
			break;
		case 2:
			tracking = TRUE;
			fprintf(stdout, "The camera will now follow the movements of the mouse.\n");
			break;
		default:
			// assertion error
			fprintf(stdout, "Unknown menu item selected (%d).\n", selection);
			break;
	}
}
