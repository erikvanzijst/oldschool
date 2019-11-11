/*
 * Computer Graphics Course 2004
 *
 * Exercise 2
 * This file implements the object loader. It complies with the rules given
 * in the file sgf-format.txt.
 *  
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 06.oct.2004
 */

#include "globals.h"
#include "loader.h"

/*
 * Loads the objects stored in the given list of files and stores
 * them as a ready-to-use call-list under the given name.
 *
 * When the loader failed, a value < 0 is returned. When the loader
 * succeeded, the number of vertices that was parsed is returned.
 * The following error codes are used:
 *
 * -1	File incomplete (unexpected EOF).
 * -2	Unsupported polygon type.
 * -3	Unable to open one or more definition files.
 */
long load(char **files, int name)
{
	char type[1024];	// yes, this is susceptible to buffer overflows
	long total = 0;
	long count = 0;
	int retval = 0;
	int identifier = 0;	// the GL polygon identifier.
	GLfloat color[3];
	GLfloat alpha;
	int nX = 0;

	glNewList(name, GL_COMPILE);

	while(strlen(files[nX]) > 0)
	{
		FILE * handle;

		handle = fopen(files[nX], "r");
		if(handle == NULL)
		{
			if(BEDUG)
			{
				fprintf(stdout, "Unable to read one or more object definition files (%s): ", files[nX]);
				perror("");
			}
			retval = -3;	// unable to open file
			break;
		}

		if(BEDUG) fprintf(stdout, "Reading object definition file %s\n", files[nX]);

		while(TRUE)
		{
			int nX;
			GLfloat vertex[3];

			if(fscanf(handle, "%s", &type) == EOF) break;
			if(strncmp(type, "POINTS", (size_t)strlen("POINTS")) == 0)
			{
				identifier = GL_POINTS;
			}
			else if(strncmp(type, "LINES", (size_t)strlen("LINES")) == 0)
			{
				identifier = GL_LINES;
			}
			else if(strncmp(type, "LINE_STRIP", (size_t)strlen("LINE_STRIP")) == 0)
			{
				identifier = GL_LINE_STRIP;
			}
			else if(strncmp(type, "LINE_LOOP", (size_t)strlen("LINE_LOOP")) == 0)
			{
				identifier = GL_LINE_LOOP;
			}
			else if(strncmp(type, "TRIANGLES", (size_t)strlen("TRIANGLES")) == 0)
			{
				identifier = GL_TRIANGLES;
			}
			else if(strncmp(type, "QUADS", (size_t)strlen("QUADS")) == 0)
			{
				identifier = GL_QUADS;
			}
			else if(strncmp(type, "TRIANGLE_STRIP", (size_t)strlen("TRIANGLE_STRIP")) == 0)
			{
				identifier = GL_TRIANGLE_STRIP;
			}
			else if(strncmp(type, "TRIANGLE_FAN", (size_t)strlen("TRIANGLE_FAN")) == 0)
			{
				identifier = GL_TRIANGLE_FAN;
			}
			else if(strncmp(type, "QUAD_STRIP", (size_t)strlen("QUAD_STRIP")) == 0)
			{
				identifier = GL_QUAD_STRIP;
			}
			else if(strncmp(type, "POLYGON", (size_t)strlen("POLYGON")) == 0)
			{
				identifier = GL_POLYGON;
			}
			else
			{
				retval = -2;	// Unknown polygon type.
				if(BEDUG) fprintf(stdout, "Unknown polygon type read: %s\n", type);
				break;
			}

			// now parse the number of vertices
			if(fscanf(handle, "%d", &count) == EOF)
			{
				retval = -1;	// file incomplete
				break;
			}
			// and the vertex color
			if(fscanf(handle, "%f %f %f %f", &alpha, &color[0], &color[1], &color[2]) == EOF)
			{
				retval = -1;	// file incomplete
				break;
			}

			if(BEDUG) fprintf(stdout, "Reading %d vertices of type %s with color (%f, %f, %f)\n", count, type, color[0], color[1], color[2]);


			glBegin(identifier);

			glColor3fv(color);
			for(nX = 0; retval >= 0 && nX < count; nX++)
			{
				// read the vertex
				if(fscanf(handle, "%f %f %f", &vertex[0], &vertex[1], &vertex[2]) == EOF)
				{
					retval = -1;	// file incomplete
					break;
				}
				glVertex3fv(vertex);

				// read the normal vector...
				if(fscanf(handle, "%f %f %f", &vertex[0], &vertex[1], &vertex[2]) == EOF)
				{
					retval = -1;	// file incomplete
					break;
				}
				// ...and ignore it for now

				total++;
				if(BEDUG) fprintf(stdout, "Vertex read (%f, %f, %f)\n", vertex[0], vertex[1], vertex[2]);
			}

			glEnd();
		}


		nX++;
	}




	glEndList();

	return retval < 0 ? retval : total;
}
