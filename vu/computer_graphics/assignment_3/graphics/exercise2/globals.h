/*
 * Computer Graphics Course 2004
 *
 * Exercise 2
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 06.oct.2004
 */

#ifndef __GLOBALS_H
#define __GLOBALS_H

#include <GL/glut.h>
#include <stdio.h>
#include <util/glutil.h>

void idle(void);	// declare global so we can force a re-display from other files

typedef int BOOL;
#define TRUE 1
#define FALSE 0

/* defines */
#define PI 3.14159265
#define VOLUME_X 10.0
#define VOLUME_Y 10.0
#define VOLUME_Z 1.0
#define PYRAMID 1
#define CUBE 2
#define F16 3
#define DISTANCE 5.0
#define ROTATION_TIME_PYRAMID 3.0
#define ROTATION_TIME_CUBE 4.0
#define ORBIT_TIME_CUBE 5.0
#define ORBIT_TIME_F16 4.0
#define ORBIT_F16 2.0
#define MATRIX_ON 1
#define BEDUG 1

// some standard color definitions
static const GLfloat red[] = {1.0, 0.0, 0.0};
static const GLfloat yellow[] = {1.0, 1.0, 0.0};
static const GLfloat cyan[] = {0.0, 1.0, 1.0};
static const GLfloat white[] = {1.0, 1.0, 1.0};
static const GLfloat black[] = {0.0, 0.0, 0.0};
static const GLfloat blue[] = {0.0, 0.0, 1.0};
static const GLfloat green[] = {0.0, 1.0, 0.0};
static const GLfloat magenta[] = {1.0, 0.0, 1.0};

static const char* tex_file = "..\\textures\\text.rgb";

static const char* cube_files[1] = {(char *)"cube\\cube.sgf"};

static const char* f16_files[8] = {(char *)"f-16\\afterburner.sgf", (char *)"f-16\\body.sgf", (char *)"f-16\\bomb.sgf",
		(char *)"f-16\\cockpit.sgf", (char *)"f-16\\rockets.sgf", (char *)"f-16\\tailfin.sgf",
		(char *)"f-16\\tailwings.sgf", (char *)"f-16\\wings.sgf"};

#endif __GLOBALS_H
