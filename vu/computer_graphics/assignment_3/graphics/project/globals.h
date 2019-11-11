#ifndef __GLOBALS_H
#define __GLOBALS_H

#include <stdio.h>
#include <stdlib.h>
#include <GL/glut.h>
#include <util/glutil.h>
#include <math.h>
#include "glm.h"

#define PI 3.1415926535897
#define TURNRADIUS 3		// the turn radius of the vehicles
#define TURNCIRCLE (2.0 * PI * TURNRADIUS)

typedef int BOOL;
typedef GLfloat Vertex[4];
#define TRUE 1
#define FALSE 0

typedef enum
{
	CAR, HELICOPTER
} CamView;

typedef struct _LinkedList
{
	void *data;
	struct _LinkedList *next;
} LinkedList;

typedef struct _Location
{
	float position[2];	// current position in the xz-plane
	float direction;	// direction of the object
	float speed;		// current speed in points per second
	const float topspeed;		// how fast the vehicle can go
	const float acceleration;	// normal forward acceleraction in points per second squared
	const float deceleration;	// normal speed-reduction-rate when buttons are released
	const float brakedecel;	// how fast the vehicle can brake to a standstill
	double last_computation_time;	// last time the position was updated
} Location;

typedef struct _Camera
{
	CamView view;		// on/in the car or helicopter-view
	const float viewDistance;	// how far in front of the vehicle we focus
	float height;		// how high above the vehicle it floats
	float behind;		// how far behind the vehicle the camera floats
	float position[3];	// ready-to-use values for eyeX, eyeY and eyeZ
	float center[3];	// ready-to-use values for centerX, centerY and centerZ
	float up[3];		// ready-to-use values for upX, upY and upZ;
} Camera;

typedef struct _Player
{
	int points;			// number of kills
	BOOL forward, reverse, left, right;	// buttons pressed
	Location location;	// current location/direction/speed of the player's vehicle
	Camera camera;		// current position and angle of the camera
	BOOL moved;			// indicates whether the player has moved in this time frame
	GLMmodel *model;	// the wavefront model used to represent the player
	GLfloat model_offset_matrix[16];	// offset, rotation & scale of the model
	Vertex model_corners[4];	// the car's 4 corners (relative to the origin)
	Vertex model_absolute_corners[4];	// the car's 4 corners
	GLfloat player_transformation_matrix[16];	// derived from each frame from location.position and location.direction
	BOOL bounding_box;	// display collision bounding boxes around the car
} Player;

typedef struct _Surface
{
	Vertex color;
	int num_vertices;
	GLuint tex_id;		// the id of the texture used
	GLenum polygon_type;
	Vertex *tex_coords;	// uses only the first 2 values from each vertex
	Vertex *vertices;
	Vertex *absolute_vertices;
	Vertex *vertex_normals;
	Vertex plane_normal;
	GLfloat distance;	// smallest distance to the origin for this plane
	BOOL textured;		// whether or not the polygon is textured
} Surface;

typedef struct _Object
{
	int call_list;
	int num_polygons;
	Surface *polygons;
	GLfloat transformation_matrix[16];
} Object;

typedef struct _TreeNode
{
	GLfloat base_matrix[16], render_matrix[16];
	int call_list;
	struct _TreeNode *sibling;
	struct _TreeNode *child;
} TreeNode;

// used to describe a dino or horse
typedef struct _Monster
{
	Vertex position;
	float speed;
	TreeNode *model_graph;
	LinkedList *trajectory;	// circular list of coordinates
	LinkedList *from, *to;
	TreeNode *larm, *rarm, *lleg, *rleg;
	GLfloat monster_transformation_matrix[16];	// derived from each frame from position and direction
	double last_computation_time;
	GLfloat angle;	// the current direction
} Monster;

// some standard color definitions
static const GLfloat red[] = {1.0, 0.0, 0.0};
static const GLfloat yellow[] = {1.0, 1.0, 0.0};
static const GLfloat cyan[] = {0.0, 1.0, 1.0};
static const GLfloat white[] = {1.0, 1.0, 1.0};
static const GLfloat black[] = {0.0, 0.0, 0.0};
static const GLfloat blue[] = {0.0, 0.0, 1.0};
static const GLfloat green[] = {0.0, 1.0, 0.0};
static const GLfloat magenta[] = {1.0, 0.0, 1.0};
static const GLfloat grey[] = {0.7, 0.7, 0.7, 1.0};

static const char porsche_file[] = {(char *)"../models/porsche/porsche.obj"};
//static const char porsche_file[] = {(char *)"../models/alcapone/al.obj"};

// textures

#endif __GLOBALS_H
