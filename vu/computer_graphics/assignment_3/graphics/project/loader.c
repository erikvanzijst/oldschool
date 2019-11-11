/*
 * Computer Graphics Course 2004
 *
 * Final Project
 * This file implements the object loader. It complies with the rules given
 * in the file sgf-format.txt.
 *  
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 06.oct.2004
 */

#include "globals.h"
#include "loader.h"
#include "maps.h"

#define BEDUG 0

/*
 * Utility function that reads a single line of text from a given, open file.
 * Lines starting with a '#' symbol are interpreted as comments and silently
 * skipped over.
 */
char *readLine(FILE *fd, char* line) {

	do {
		if(fgets(line, 1023, fd) == NULL) {
			return NULL;
		}
		while(line[strlen(line) - 1] == '\n') line[strlen(line) - 1] = '\0';
	} while(*line == '#' || *line == '\0');	// skip empty lines and lines starting with a '#'

	return line;
}

typedef struct _TextureTuple {
	char filename[1024];
	GLuint tex_id;
	int context;
} TextureTuple;

/*
 * Load the spcified rgb texture file and returns the texture's ID. If the
 * same texture file is loaded twice, the function returns the same ID as the
 * first time and does not load the texture in memory a second time.
 */
GLuint loadTexture(char *file, int context) {

	static LinkedList *loadedTextures = NULL;
	GLuint tex_id = 0, tmp_id;
	RGBImage *image;
	TextureTuple *tuple;
	LinkedList *record, *entry;

	// see if this texture was loaded before
	for(entry = loadedTextures; entry != NULL; entry = entry->next) {

		tuple = (TextureTuple *)entry->data;
		if(tuple == NULL) {
			fprintf(stdout, "Texture list corrupt.\n");
			exit(1);
		} else {
			if(strcmp(tuple->filename, file) == 0 && tuple->context == context) {
				// we already loaded this file before
//				fprintf(stdout, "Using previously loaded texture %s from memory in context %d.\n", file, context);
				return tuple->tex_id;
			}
		}
	}

	// this is a new texture, load it from file
	glEnable(GL_TEXTURE_2D);
	glPushAttrib(GL_ALL_ATTRIB_BITS);
	glGenTextures(1, &tmp_id);
	glBindTexture(GL_TEXTURE_2D, tmp_id);	// select our texture
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	image = LoadRGB(file);	// texture loaded
	if(image == NULL) {
		fprintf(stdout, "Unable to load texture file (%s).\n", file);
		exit(1);
	} else {
//		fprintf(stdout, "Texture (%s) loaded. x = %d, y = %d, format = %d, components = %d\n", file, image->sizeX, image->sizeY, image->format, image->components);
	}
	gluBuild2DMipmaps(GL_TEXTURE_2D, image->components, image->sizeX, image->sizeY, image->format, GL_UNSIGNED_BYTE, image->data);
	glPopAttrib();
	glDisable(GL_TEXTURE_2D);

	// now store it for future use
	tuple = (TextureTuple *)malloc(sizeof(TextureTuple));
	strncpy(tuple->filename, file, 1023);
	tuple->tex_id = tmp_id;
	tuple->context = context;
	record = (LinkedList *)malloc(sizeof(LinkedList));
	record->data = tuple;
	record->next = NULL;

	if(loadedTextures == NULL) {
		loadedTextures = record;
	} else {
		// append the loaded texture to the texture list
		for(entry = loadedTextures; ; entry = entry->next) {
			if(entry->next == NULL) {
				break;
			}
		}
		entry->next = record;
	}
//	fprintf(stdout, "New texture map (%s) loaded and stored in context %d.\n", file, tuple->context);

	return tuple->tex_id;
}

long load(char **files, int file_count, int name, Surface **_polygons, int context) {

	return loadTextured(files, file_count, name, _polygons, context, NULL);
}

/*
 * Loads the objects stored in the given list of files and stores
 * them as a ready-to-use call-list under the specified list id.
 * The loader stores a copy of all polygons with their vertices and normals as
 * an array of Surface structures, with the address of the first structure
 * assigned to the given Surface pointer. Note that the Surface array is
 * dynamically allocated.
 *
 * When the loader failed, a value < 0 is returned. When the loader
 * succeeded, the number of polygons that was parsed is returned.
 * The following error codes are used:
 *
 * E_INCOMPLETE	File incomplete (unexpected EOF).
 * E_POLYGON	Unsupported polygon type.
 * E_FOPEN		Unable to open one or more definition files.
 *
 * Note that 
 */
long loadTextured(char **files, int file_count, int name, Surface **_polys, int context, char *texture) {

	char line[1024], type[1024];	// yes, this is susceptible to buffer overflows
	long total = 0;
	int retval = 0;
	GLenum identifier = 0;	// the GL polygon identifier.
	Surface *polygons = NULL;
	int nX = 0;

	polygons = NULL;

	for(; nX < file_count; nX++)
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
			retval = E_FOPEN;	// unable to open file
			break;
		}

		if(BEDUG) fprintf(stdout, "Reading object definition file %s\n", files[nX]);

		while(TRUE)
		{
			int mX, tmp;
			char tex_file[1024];

			if(readLine(handle, line) == NULL) break;	// normal EOF
			polygons = (Surface *)realloc(polygons, ++total * sizeof(Surface));
			sscanf(line, "%s %d", &type, &polygons[total - 1].num_vertices);

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
				retval = E_POLYGON;	// Unknown polygon type.
				if(BEDUG) fprintf(stdout, "Unknown polygon type read: %s\n", type);
				break;
			}
			polygons[total - 1].polygon_type = identifier;

			// read the vertex color and check for a texture file
			if(readLine(handle, line) == NULL) {
				retval = E_INCOMPLETE;	// file incomplete
				break;
			}
			tmp = sscanf(line, "%f %f %f %f %s\n", &polygons[total - 1].color[3], &polygons[total - 1].color[0], &polygons[total - 1].color[1], &polygons[total - 1].color[2], tex_file);
			switch(tmp) {
				case 5:
					// a texture file is specified - apply texture mapping
					polygons[total - 1].textured = TRUE;
					break;
				case 4:
					// no texture file specified - apply no texture mapping
					polygons[total - 1].textured = FALSE;
					break;
				default:
					retval = E_INCOMPLETE;	// file incomplete
			}
			if(texture != NULL) {
				// explicit texture overrides file contents
				polygons[total - 1].textured = TRUE;
				strcpy((char *)&tex_file, texture);
			}

			if(polygons[total - 1].textured) {
				polygons[total - 1].tex_id = loadTexture(tex_file, context);
			}

			polygons[total - 1].vertices = (Vertex *)malloc(polygons[total - 1].num_vertices * sizeof(Vertex));
			polygons[total - 1].tex_coords = (Vertex *)malloc(polygons[total - 1].num_vertices * sizeof(Vertex));
			polygons[total - 1].absolute_vertices = (Vertex *)malloc(polygons[total - 1].num_vertices * sizeof(Vertex));
			polygons[total - 1].vertex_normals = (Vertex *)malloc(polygons[total - 1].num_vertices * sizeof(Vertex));
			for(mX = 0; retval >= 0 && mX < polygons[total - 1].num_vertices; mX++)
			{
				// read the vertex
				if(readLine(handle, line) == NULL) {
					retval = E_INCOMPLETE;	// file incomplete
					break;
				}
				polygons[total - 1].vertices[mX][3] = polygons[total - 1].absolute_vertices[mX][3] = 1.0;
				if(sscanf(line, "%f %f %f %f %f %f %f %f", &polygons[total - 1].vertices[mX][0], &polygons[total - 1].vertices[mX][1], &polygons[total - 1].vertices[mX][2], &polygons[total - 1].vertex_normals[mX][0], &polygons[total - 1].vertex_normals[mX][1], &polygons[total - 1].vertex_normals[mX][2], &polygons[total - 1].tex_coords[mX][0], &polygons[total - 1].tex_coords[mX][1]) < 8 && polygons[total - 1].textured) {
					polygons[total - 1].tex_coords[mX][0] = frand(0.0, 1.0);
					polygons[total - 1].tex_coords[mX][1] = frand(0.0, 1.0);
				}
			}

		}
		fclose(handle);
	}

	if(retval < 0) {
		return retval;
	} else {

		int i;

		glPushAttrib(GL_ALL_ATTRIB_BITS);
		// now build the call list
		glNewList(name, GL_COMPILE);
		for(nX = 0; nX < total; nX++) {

			GLfloat diffuse[] = {0.85, 0.85, 0.85, 1.0};	// standard reflection properties
			GLfloat specular[] = {0.9, 0.9, 0.9, 1.0};
	
			diffuse[0] *= polygons[nX].color[0];
			diffuse[1] *= polygons[nX].color[1];
			diffuse[2] *= polygons[nX].color[2];
			specular[0] *= polygons[nX].color[0];
			specular[1] *= polygons[nX].color[1];
			specular[2] *= polygons[nX].color[2];

			if(polygons[nX].textured) {
				glEnable(GL_TEXTURE_2D);
				glBindTexture(GL_TEXTURE_2D, polygons[nX].tex_id);	// bind the texture
				glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			}

			glMaterialfv(GL_FRONT, GL_DIFFUSE, diffuse);
			glMaterialfv(GL_FRONT, GL_SPECULAR, specular);
			glBegin(polygons[nX].polygon_type);

				for(i = 0; i < polygons[nX].num_vertices; i++) {

					glNormal3fv(polygons[nX].vertex_normals[i]);	// set the normal vector prior to the vertex
					if(polygons[nX].textured) {
						glTexCoord2f(polygons[nX].tex_coords[i][0], polygons[nX].tex_coords[i][1]);
					}
					glVertex3fv(polygons[nX].vertices[i]);
				}

			glEnd();

			if(polygons[nX].textured) {
				glDisable(GL_TEXTURE_2D);
			}
		}

		glEndList();
		glPopAttrib();

		*_polys = polygons;
		return total;
	}
}

/*
 * Loads a specific map file. Returns the number of scenery object descriptors
 * loaded, or < 0 in case of an error.
 * The following error codes are used:
 *
 * E_INCOMPLETE	File incomplete (unexpected EOF).
 * E_FOPEN		Unable to open one or more definition files.
 */
int loadMapDescription(LinkedList **map, LinkedList **trajectory, char *mapfile) {

	FILE *handle;
	int retval = 0, num_landmarks;
	char line[1024];
	LinkedList *entry, *previous = NULL;
	ObjectDescription *desc;

//	fprintf(stdout, "Loading scenery from %s.\n", mapfile);
	handle = fopen(mapfile, "r");
	if(handle == NULL) {

		retval = E_FOPEN;	// unable to open file
	} else {

		// first read the monster's trajectory
		if(readLine(handle, line) == NULL) {
			retval = E_INCOMPLETE;
		} else {

			int i;
			LinkedList *landmark, *previous_landmark = NULL;
			Vertex *lm;
			GLfloat x, y;

			sscanf(line, "%d", &num_landmarks);
			for(i = 0; i < num_landmarks; i++) {
				if(readLine(handle, line) == NULL) {
					retval = E_INCOMPLETE;
					break;
				}
				landmark = (LinkedList *)malloc(sizeof(LinkedList));
				lm = (Vertex *)malloc(sizeof(Vertex));
				landmark->data = lm;
				sscanf(line, "%f %f", &x, &y);
				(*lm)[0] = x;
				(*lm)[1] = 0.0;
				(*lm)[2] = y;
				(*lm)[3] = 1.0;

				// attach the entry to the trajectory list
				if(previous_landmark == NULL) {
					*trajectory = landmark;
				} else {
					previous_landmark->next = landmark;
				}
				previous_landmark = landmark;
			}
			if(i > 0) {
				landmark->next = *trajectory;
			} else {
				retval = E_INCOMPLETE;
			}
//			fprintf(stdout, "Trajectory with %d landmarks loaded.\n", i);
		}

		while(retval >= 0) {

			if(readLine(handle, line) == NULL) break;	// normal EOF

			entry = (LinkedList *)malloc(sizeof(LinkedList));
			entry->next = NULL;
			desc = (ObjectDescription *)malloc(sizeof(ObjectDescription));
			desc->filename = (char *)malloc(strlen(line) * sizeof(char));
			strcpy(desc->filename, line);
			entry->data = desc;

			// read the translation values
			if(readLine(handle, line) == NULL) {
				retval = E_INCOMPLETE;
				break;
			}
			sscanf(line, "%f %f %f", &desc->translate[0], &desc->translate[1], &desc->translate[2]);

			// read the rotational angles
			if(readLine(handle, line) == NULL) {
				retval = E_INCOMPLETE;
				break;
			}
			sscanf(line, "%f %f %f", &desc->rotate[0], &desc->rotate[1], &desc->rotate[2]);

			// read the scaling factors
			if(readLine(handle, line) == NULL) {
				retval = E_INCOMPLETE;
				break;
			}
			sscanf(line, "%f %f %f", &desc->scale[0], &desc->scale[1], &desc->scale[2]);

			// attach the entry to the linked list
			if(previous == NULL) {
				*map = entry;
			} else {
				previous->next = entry;
			}
			previous = entry;
			retval++;
		}

		fclose(handle);
	}


	if(retval >= 0) {
//		fprintf(stdout, "Loaded %d scenery objects from map %s.\n", retval, mapfile);
	}
	return retval;
}
