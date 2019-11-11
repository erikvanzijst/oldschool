#include "globals.h"
#include "maps.h"
#include "models.h"
#include "loader.h"
#include "util.h"

GLMmodel* pmodel = NULL;
extern Monster dino;

/*
 * Loads the world map and stores the loaded objects in a linked list. The
 * address of the list's root node is assigned to the specified pointer.
 * The object list can be traversed. Each
 * object is a struct that contains the object's vertices, normals, color
 * and polygon type as well as the call list identifier under which the
 * described polygons are stored by GL. The vertices as well as the call list
 * are in relative coordinates. For positioning in the scene, the struct also
 * contains the 16-field transformation matrix by which the GL model view matrix
 * is multiplied when drawing the object. This matrix is also used to convert
 * the relative vertices into "absolute" points in the scene. The latter is
 * required by the collision detection algorithms.
 *
 * Returns != NULL if the world map was successfully loaded.
 */
int loadMap(LinkedList **root, LinkedList **trajectory, int context) {

	LinkedList *entry, *previous;
	LinkedList *objectDescriptions, *mapDescriptor;
	
	objectDescriptions = getMap(trajectory);
	mapDescriptor = objectDescriptions;
	if(objectDescriptions != NULL) {
		for(previous = NULL; objectDescriptions != NULL; objectDescriptions = objectDescriptions->next) {
			
			ObjectDescription *description;
			description = (ObjectDescription *)objectDescriptions->data;
//			fprintf(stdout, "Reading object description for file %s...\n", description->filename);

			if(description == NULL) {
				return -1;
			} else {
				GLuint listId;
				char* filenames[1];
				int loadResult;
				Object *object;
				
				listId = glGenLists(1);
				if(listId == 0) {
					fprintf(stderr, "Unable to create new call list.\n");
					return -1;
				}

				object = (Object *)malloc(sizeof(Object));
				object->call_list = listId;
				filenames[0] = description->filename;
				loadResult = load(filenames, 1, listId, &object->polygons, context);

				switch(loadResult) {
					case E_INCOMPLETE:
						fprintf(stderr, "Error loading object file \"%s\": object incomplete.\n", description->filename);
						exit(1);
					case E_POLYGON:
						fprintf(stderr, "Error loading object file \"%s\": unsupported polygon type encountered.\n", description->filename);
						exit(1);
					case E_FOPEN:
						fprintf(stderr, "Error loading object file \"%s\": unable to open file.\n", description->filename);
						exit(1);
					default:
						object->num_polygons = loadResult;
//						fprintf(stdout, "Scenery object loaded from file %s with %d polygons.\n", description->filename, object->num_polygons);
						break;
				}

				// compute and store the object's transformation matrix
				glMatrixMode(GL_MODELVIEW);
				glPushMatrix();
				glLoadIdentity();
				glTranslatef(description->translate[0], description->translate[1], description->translate[2]);
				glRotatef(description->rotate[0], 1.0, 0.0, 0.0);
				glRotatef(description->rotate[1], 0.0, 1.0, 0.0);
				glRotatef(description->rotate[2], 0.0, 0.0, 1.0);
				glScalef(description->scale[0], description->scale[1], description->scale[2]);
				glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&object->transformation_matrix);
				glPopMatrix();

				// compute and store the object's absolute coordinates and plane equations
				positionObject(object);

				// add the new object to the scenery's list
				entry = (LinkedList *)malloc(sizeof(LinkedList));
				entry->data = object;
				entry->next = NULL;

				if(previous == NULL) {
					*root = entry;
				} else {
					previous->next = entry;
				}
				previous = entry;
			}

		}
		freeMap(mapDescriptor);

		return 0;
	} else {
		return -1;
	}
}

/*
 * Takes a pointer to the root of the scenery map and draws each object in the
 * scene at the designated spot. This function is used from the program's
 * display() functions.
 */
void drawMap(LinkedList *root, int context) {

	LinkedList *entry;

	for(entry = root; entry != NULL; entry = entry->next) {

		Object *object;
		object = (Object *)entry->data;

		glPushMatrix();
		glMultMatrixf((GLfloat *)&object->transformation_matrix);
		glCallList(object->call_list);
		glPopMatrix();
	}
}

/*
 * Takes an object struct with its transformation matrix and *vertices set and
 * calculates the object's absolute vertices. For each of the object's
 * surfaces it also calculates the plane's normal vector and distance from the
 * origin. This function assumes that *absolute_vertices points to an array of
 * Vertex structs that is large enough to hold all vertices.
 */
void positionObject(Object *object) {

	int i;

	for(i = 0; i < object->num_polygons; i++) {

		int j;
		Surface *polygon = &object->polygons[i];

		// transform all relative vertices into absolute vertices
		for(j = 0; j < polygon->num_vertices; j++) {

			transform((GLfloat *)&object->transformation_matrix, (GLfloat *)&polygon->vertices[j], (GLfloat *)&polygon->absolute_vertices[j]);
//			fprintf(stdout, "Relative vertex (%f, %f, %f) from polygon %d in call list %d transformed into absolute vertex (%f, %f, %f)\n", polygon->vertices[j][0], polygon->vertices[j][1], polygon->vertices[j][2], i, object->call_list, polygon->absolute_vertices[j][0], polygon->absolute_vertices[j][1], polygon->absolute_vertices[j][2]);
		}

		// derive the normal vector from the first 3 vertices
		if(polygon->num_vertices >= 3) {

			Vertex vector1 = {0.0, 0.0, 0.0, 1.0};
			Vertex vector2 = {0.0, 0.0, 0.0, 1.0};
			Vertex plane_normal = {0.0, 0.0, 0.0, 1.0};
			polygon->plane_normal[3] = 1.0;

			// derive 2 vectors through vertex subtraction of the first 3 vertices
			subtractVertices(&polygon->absolute_vertices[1], &polygon->absolute_vertices[0], &vector1);
			subtractVertices(&polygon->absolute_vertices[polygon->num_vertices - 1], &polygon->absolute_vertices[0], &vector2);

			// derive the vector that is perpendicular to the plane of the above vectors
			CrossProduct(&vector1, &vector2, &plane_normal);
			normalize(&plane_normal, &polygon->plane_normal);

			polygon->distance = DotProduct(&polygon->plane_normal, &polygon->absolute_vertices[0]);

//			fprintf(stdout, "Plane equation for polygon %d of call list %d: (%f, %f, %f, %f) + %f\n", i, object->call_list, polygon->plane_normal[0], polygon->plane_normal[1], polygon->plane_normal[2], polygon->plane_normal[3], polygon->distance);
		}
	}
}

GLMmodel *loadGLMmodel(char *filename) {

	GLMmodel *model = glmReadOBJ(filename);

    if (!model) {
		fprintf(stdout, "Unable to load GLM model file (%s).\n", filename);
		exit(1);
	} else {
        glmUnitize(model);
//		glmWeld(model, 0.01);
        glmFacetNormals(model);
        glmVertexNormals(model, 90.0);
//		fprintf(stdout, "GLM model %s loaded\n", filename);
		return model;
	}
}

void drawGLMmodel(GLMmodel *model, GLfloat *transformation_matrix) {

	glPushMatrix();
	glMultMatrixf(transformation_matrix);
    glmDraw(model, GLM_SMOOTH | GLM_MATERIAL);
	glPopMatrix();
}

int loadDino(TreeNode **root, int context) {

	char *texture = "../textures/lez.rgb";
	char *body_file = "../models/dino/body.sgf";
	char *arm_file = "../models/dino/arm.sgf";
	char *leg_file = "../models/dino/leg.sgf";
	char *eyes_file = "../models/dino/eyes.sgf";
	TreeNode *body, *eyes;
	Surface *polygons;
	GLuint startId;
	int loadResult;
	long polygon_count = 0;

	// let GL reserve call lists
	startId = glGenLists(4);
	if(startId == 0) {
		fprintf(stderr, "Unable to create call lists for the dino.\n");
		exit(1);
	}

	// allocate memory
	body = (TreeNode *)malloc(sizeof(TreeNode));
	eyes = (TreeNode *)malloc(sizeof(TreeNode));
	dino.larm = (TreeNode *)malloc(sizeof(TreeNode));
	dino.rarm = (TreeNode *)malloc(sizeof(TreeNode));
	dino.lleg = (TreeNode *)malloc(sizeof(TreeNode));
	dino.rleg = (TreeNode *)malloc(sizeof(TreeNode));

	// assign call lists
	body->call_list = startId++;
	dino.larm->call_list = startId;
	dino.rarm->call_list = startId++;
	dino.lleg->call_list = startId;
	dino.rleg->call_list = startId++;
	eyes->call_list = startId;

	// create object hierarchy
	body->sibling = NULL;
	body->child = dino.larm;
	dino.larm->child = NULL;
	dino.larm->sibling = dino.rarm;
	dino.rarm->child = NULL;
	dino.rarm->sibling = dino.lleg;
	dino.lleg->child = NULL;
	dino.lleg->sibling = dino.rleg;
	dino.rleg->child = NULL;
	dino.rleg->sibling = eyes;
	eyes->child = NULL;
	eyes->sibling = NULL;

	glMatrixMode(GL_MODELVIEW);
	glPushMatrix();
	
	glLoadIdentity();
	glTranslatef(0.0, 0.0, -1.5);
	glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&body->base_matrix);
	memcpy((GLfloat *)&body->render_matrix, (GLfloat *)&body->base_matrix, sizeof(body->base_matrix));
	
	glLoadIdentity();
	glTranslatef(0.0, 0.0, 3.0);
	glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&dino.rarm->base_matrix);
	memcpy((GLfloat *)&dino.rarm->render_matrix, (GLfloat *)&dino.rarm->base_matrix, sizeof(dino.rarm->render_matrix));

	glLoadIdentity();
	glTranslatef(0.0, 0.0, -0.75);
	glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&dino.larm->base_matrix);
	memcpy((GLfloat *)&dino.larm->render_matrix, (GLfloat *)&dino.larm->base_matrix, sizeof(dino.larm->render_matrix));

	glLoadIdentity();
	glTranslatef(0.0, 0.0, 3.0);
	glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&dino.rleg->base_matrix);
	memcpy((GLfloat *)&dino.rleg->render_matrix, (GLfloat *)&dino.rleg->base_matrix, sizeof(dino.rleg->render_matrix));

	glLoadIdentity();
	glTranslatef(0.0, 0.0, -1.5);
	glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&dino.lleg->base_matrix);
	memcpy((GLfloat *)&dino.lleg->render_matrix, (GLfloat *)&dino.lleg->base_matrix, sizeof(dino.lleg->render_matrix));

	glLoadIdentity();
	glTranslatef(0.0, 0.0, -0.1);
	glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&eyes->base_matrix);
	memcpy((GLfloat *)&eyes->render_matrix, (GLfloat *)&eyes->base_matrix, sizeof(eyes->render_matrix));
	
	glPopMatrix();

	if((loadResult = loadTextured(&body_file, 1, body->call_list, &polygons, context, texture)) < 0) {
		fprintf(stdout, "Error loading dino file \"%s\" (error code %d).\n", body_file, loadResult);
		exit(1);
	} else {
		polygon_count += loadResult;
	}
	if((loadResult = loadTextured(&arm_file, 1, dino.larm->call_list, &polygons, context, texture)) < 0) {
		fprintf(stdout, "Error loading dino file \"%s\" (error code %d).\n", arm_file, loadResult);
		exit(1);
	} else {
		polygon_count += loadResult;
	}
	if((loadResult = loadTextured(&leg_file, 1, dino.lleg->call_list, &polygons, context, texture)) < 0) {
		fprintf(stdout, "Error loading dino file \"%s\" (error code %d).\n", leg_file, loadResult);
		exit(1);
	} else {
		polygon_count += loadResult;
	}
	if((loadResult = load(&eyes_file, 1, eyes->call_list, &polygons, context)) < 0) {
		fprintf(stdout, "Error loading dino file \"%s\" (error code %d).\n", eyes_file, loadResult);
		exit(1);
	} else {
		polygon_count += loadResult;
	}

	*root = body;
	return 0;
}

void traverse(TreeNode *node) {

	if(node == NULL) {
		return;
	}
	glPushMatrix();
	glMultMatrixf(node->render_matrix);
	glCallList(node->call_list);
	traverse(node->child);
	glPopMatrix();
	traverse(node->sibling);
}

void drawDino(TreeNode *root, int context) {

	traverse(root);
}

/*
 * Called when the monster is killed. The function selects a random landmark
 * from the monster's trajectory and returns it. The monster will be respawned
 * at that location.
 */
LinkedList *spawnMonster(LinkedList *trajectory) {

	int num_landmarks = 1, offset, i;
	LinkedList *entry, *first;

	if(trajectory == NULL) {
		return NULL;
	}
	first = trajectory;
	for(entry = trajectory->next; entry != first; entry = entry->next) {

		num_landmarks++;
	}

//	offset = (int)frand(0.0, (float)num_landmarks) + 1;
	offset = 1+(int) (num_landmarks*rand()/(RAND_MAX+1.0));
//	fprintf(stdout, "Selecting the %d's landmark for monster respawning.\n", offset);
	entry = trajectory;
	for(i = 0; i < offset; i++) {
		entry = entry->next;
	}

	return entry;
}
