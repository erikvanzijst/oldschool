/*
 * Computer Graphics Course 2004
 *
 * Final Project
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 27.nov.2004
 */
#include <math.h>
#include "globals.h"
#include "input.h"
#include "models.h"
#include "util.h"
#include "collision.h"

#define GAP 22	// the distance between the viewports and the window edges

GLvoid *font_style = GLUT_BITMAP_TIMES_ROMAN_10;
GLuint window, top, bottom;
int width = 800, height = 600;
int viewport_width, viewport_height;
GLfloat light1_position[] = { 4.0, 8.0, -3.0, 1.0 };
GLfloat light2_position[] = { -4.0, 8.0, 3.0, 1.0 };

// we always have 2 players - p1 is upper screen, p2 lower screen
Player p1 = {0, FALSE, FALSE, FALSE, FALSE, {{0.0, 0.0}, 0.0, 0.0, 7.0, 3.0, 1.0, 8.0, 0.0}, {CAR, 5.0, 1.2, 1.0}, TRUE};
Player p2 = {0, FALSE, FALSE, FALSE, FALSE, {{0.0, 0.0}, 0.0, 0.0, 7.0, 3.0, 1.0, 8.0, 0.0}, {CAR, 5.0, 1.2, 1.0}, TRUE};

// this is the first objectin the list of all stationary objects, with a reference to the next
LinkedList *scenery = NULL;
Monster dino;
float arm_angle = 0.0;
int frames = 0;	// frames rendered this second
double last_fps_collection;
BOOL animation = TRUE;


/* function prototypes */
void setfont(char* name, int size);
void drawstr(GLuint x, GLuint y, char* format, ...);
void init(void);
void main_display(void);
void main_reshape(int w, int h);
void top_display(void);
void top_reshape(int w, int h);
void bottom_display(void);
void bottom_reshape(int w, int h);
void draw_scene(GLuint context);
void idle(void);
void left_forward(Player *p, double turn_angle);
void right_forward(Player *p, double turn_angle);
void left_reverse(Player *p, double turn_angle);
void right_reverse(Player *p, double turn_angle);
void locate_camera(Player *p);
void locate_car(Player *p);

void main_reshape(int w, int h)
{
	if(w <= 2 * GAP) w = 2 * GAP + 1;
	if(h <= 3 * GAP) h = 3 * GAP + 1;
	width = w;
	height = h;
	viewport_width = (width - (2 * GAP));
	viewport_height = (int)(((float)h / 2.0) - (1.5 * GAP));

    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(0, w, h, 0);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    glutSetWindow(top);
    glutPositionWindow(GAP, GAP);
    glutReshapeWindow(viewport_width, viewport_height);
    glutSetWindow(bottom);
    glutPositionWindow(GAP, (int)(((float)h / 2.0) + 0.5 * GAP));
    glutReshapeWindow(viewport_width, viewport_height);
}

void main_display(void)
{
	glClearColor(0.8, 0.8, 0.8, 0.0);	// standard greyish
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glColor3ub(0, 0, 0);	// black font
    setfont("helvetica", 12);

    drawstr(GAP, GAP-5, "Player 1 - kills: %d", p1.points);
    drawstr(GAP, (2 * GAP) + (int)(((float)height / 2.0) - (1.5 * GAP)) - 5, "Player 2 - kills: %d", p2.points);

	glutSwapBuffers();
}

void top_reshape(int w, int h)
{
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
	glLightfv(GL_LIGHT1, GL_POSITION, light1_position);
	glLightfv(GL_LIGHT2, GL_POSITION, light2_position);
    glMatrixMode(GL_MODELVIEW);

    glEnable(GL_DEPTH_TEST);
}

void top_display(void)
{
	draw_scene(top);
}

void bottom_reshape(int w, int h)
{
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
	glLightfv(GL_LIGHT1, GL_POSITION, light1_position);
	glLightfv(GL_LIGHT2, GL_POSITION, light2_position);
    glMatrixMode(GL_MODELVIEW);

    glEnable(GL_DEPTH_TEST);
}

void bottom_display(void)
{
	draw_scene(bottom);
}

void fps_counter() {
	
	double now = Wallclock();

	frames++;
	if(now - last_fps_collection > 1.0) {
	
		char title[80];
		int wnd;
		
		sprintf(title, "Roadkill (%d fps)", frames);
		wnd = glutGetWindow();
		glutSetWindow(window);
		glutSetWindowTitle(title);
		glutSetWindow(wnd);
		frames = 0;
		last_fps_collection = now;
	}
}

/*
 * Called from both top_display and bottom_display to render the entire scene.
 */
void draw_scene(GLuint context)
{
	fps_counter();

	glClearColor(0.4, 0.45, 1.0, 0.0);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// first position the camera for this viewport
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluPerspective(60.0, (GLfloat)viewport_width/viewport_height, 0.1, 256.0);
	if(context == top) {
		gluLookAt(p1.camera.position[0], p1.camera.position[1], p1.camera.position[2],
				p1.camera.center[0], p1.camera.center[1], p1.camera.center[2],
				p1.camera.up[0], p1.camera.up[1], p1.camera.up[2]);
	} else {
		gluLookAt(p2.camera.position[0], p2.camera.position[1], p2.camera.position[2],
				p2.camera.center[0], p2.camera.center[1], p2.camera.center[2],
				p2.camera.up[0], p2.camera.up[1], p2.camera.up[2]);
	}

    glMatrixMode(GL_MODELVIEW);

	glEnable(GL_LIGHTING);
	glPushMatrix();
	drawMap(scenery, context);
	glPopMatrix();

	glPushMatrix();
	glMultMatrixf((GLfloat *)&dino.monster_transformation_matrix);
	glTranslatef(-1.0, 0.0, 0.0);
	glScalef(0.1, 0.1, 0.1);
	drawDino(dino.model_graph, context);

	glPopMatrix();

	// draw player 1
	glPushMatrix();
		glMultMatrixf((GLfloat *)&p1.player_transformation_matrix);

		if(p1.bounding_box) {
			glPushMatrix();
				glBegin(GL_LINE_LOOP);
					glVertex3fv((GLfloat *)&p1.model_corners[0]);
					glVertex3fv((GLfloat *)&p1.model_corners[1]);
					glVertex3fv((GLfloat *)&p1.model_corners[2]);
					glVertex3fv((GLfloat *)&p1.model_corners[3]);
				glEnd();
			glPopMatrix();
		}

		drawGLMmodel(p1.model, (GLfloat *)&p1.model_offset_matrix);
		if(context != top) {

			glDisable(GL_LIGHTING);
			glColor3fv(red);
			glTranslatef(0.0, 1.0, 0.0);
			drawstr(0, 0, "Player 1 (%d kills)", p1.points);
			glEnable(GL_LIGHTING);
		}
	glPopMatrix();

	// draw player 2
	glPushMatrix();
		glMultMatrixf((GLfloat *)&p2.player_transformation_matrix);

		if(p2.bounding_box) {
			glPushMatrix();
				glBegin(GL_LINE_LOOP);
					glVertex3fv((GLfloat *)&p2.model_corners[0]);
					glVertex3fv((GLfloat *)&p2.model_corners[1]);
					glVertex3fv((GLfloat *)&p2.model_corners[2]);
					glVertex3fv((GLfloat *)&p2.model_corners[3]);
				glEnd();
			glPopMatrix();
		}

		drawGLMmodel(p2.model, (GLfloat *)&p2.model_offset_matrix);
		if(context != bottom) {

			glDisable(GL_LIGHTING);
			glColor3fv(red);
			glTranslatef(0.0, 1.0, 0.0);
			drawstr(0, 0, "Player 2 (%d kills)", p2.points);
			glEnable(GL_LIGHTING);
		}
	glPopMatrix();

	glDisable(GL_LIGHTING);

	glFlush();
	glutSwapBuffers();
}

void redisplay_all(void)
{
    glutSetWindow(top);
    glutPostRedisplay();
    glutSetWindow(bottom);
    glutPostRedisplay();
}

void faceMonster(Monster *m) {

	Vertex *from, *to;
	GLfloat dx, dy;

	// let the monster face its new destination
	to = (Vertex *)m->to->data;
	from = (Vertex *)m->from->data;
	dx = (*to)[0] - (*from)[0];
	dy = (*to)[2] - (*from)[2];
	m->angle = to_degrees(atan(dy / dx));

	if(dx == 0.0) {	// atan() undefined here
		if(dy > 0.0) m->angle = 90.0;
		if(dy < 0.0) m->angle = -90.0;
	} else if(dx < 0.0) {
		m->angle += 180.0;
	}
}

/*
 * Called for every frame to calculate the monster's new location.
 */
void locate_monster(Monster *m) {

	double now = Wallclock();
	double dt = now - m->last_computation_time;
	float distance = m->speed * dt;
	Vertex ray = {0.0, 0.0, 0.0, 1.0}, direction = {0.0, 0.0, 0.0, 1.0}, dest = {0.0, 0.0, 0.0, 1.0};
	Vertex *tmp, traveled, remaining, xaxis = {1.0, 0.0, 0.0, 1.0};
	float length_traveled, length_remaining;

	subtractVertices((Vertex *)m->to->data, (Vertex *)m->from->data, (Vertex *)&ray);
	normalize(&ray, &direction);
	vectorScalarMultiply(&direction, distance, &ray);
	addVertices(&m->position, &ray, &dest);

	// see if the monster passed it's destination
	subtractVertices((Vertex *)m->to->data, &m->position, &remaining);
	subtractVertices((Vertex *)m->to->data, &dest, &traveled);
	length_traveled = vectorLength(&traveled);
	length_remaining = vectorLength(&remaining);

	if(length_traveled - length_remaining == 0.0f || length_remaining == 0.0f ||
		(length_remaining < length_traveled)) {

		// select the next landmark
		memcpy(&m->position, (Vertex *)m->to->data, sizeof(Vertex));
		m->from = m->to;
		m->to = m->to->next;

		faceMonster(m);
	} else {
		memcpy(&m->position, &dest, sizeof(Vertex));
	}


	tmp = (Vertex *)m->to->data;

	// use GL to calculate the new transformation matrix
	glMatrixMode(GL_MODELVIEW);
	glPushMatrix();
	glLoadIdentity();
	glTranslatef(m->position[0], m->position[1], m->position[2]);
	glRotatef(m->angle, 0.0, -1.0, 0.0);
	glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&m->monster_transformation_matrix);
	glPopMatrix();

	// calculate the swing of the arms and legs
	{
		float swing, angle;
		#define FULLSWING 1.7

		swing = distance / FULLSWING;
		while(swing >= FULLSWING) swing -= FULLSWING;
		arm_angle += swing / (FULLSWING / (2 * PI));
		while(arm_angle >= 2*PI) arm_angle -= 2*PI;
		angle = sin(arm_angle) * 45;

		// use GL to calculate the new render matrices for the limbs
		glPushMatrix();

		glLoadIdentity();
		glMultMatrixf((GLfloat *)&m->larm->base_matrix);
		glTranslatef(9.0, 10.0, 0.0);
		glRotatef(angle, 0.0, 0.0, 1.0);
		glTranslatef(-9.0, -10.0, 0.0);
		glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&m->larm->render_matrix);

		glLoadIdentity();
		glMultMatrixf((GLfloat *)&m->rarm->base_matrix);
		glTranslatef(9.0, 10.0, 0.0);
		glRotatef(angle, 0.0, 0.0, -1.0);
		glTranslatef(-9.0, -10.0, 0.0);
		glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&m->rarm->render_matrix);

		glLoadIdentity();
		glMultMatrixf((GLfloat *)&m->rleg->base_matrix);
		glTranslatef(9.0, 6.0, 0.0);
		glRotatef(angle, 0.0, 0.0, 1.0);
		glTranslatef(-9.0, -6.0, 0.0);
		glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&m->rleg->render_matrix);

		glLoadIdentity();
		glMultMatrixf((GLfloat *)&m->lleg->base_matrix);
		glTranslatef(9.0, 6.0, 0.0);
		glRotatef(angle, 0.0, 0.0, -1.0);
		glTranslatef(-9.0, -6.0, 0.0);
		glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&m->lleg->render_matrix);

		glPopMatrix();
	}

	m->last_computation_time = now;
}

void locate_car(Player *p)
{
	// compute the players' new positions
	// player 1 pressed forward, reverse or nothing
	BOOL changed = FALSE;
	double distance_x, distance_z;
	double distance, dv;	// the total distance we travelled and speed-change
	double turn_angle;	// in case a turn is made
	double now = Wallclock();
	double dt = now - p->location.last_computation_time;	// delta-time

	if((p->forward && p->reverse) || (!p->forward && !p->reverse))
	{
		if(p->location.speed != 0.0)
		{
			if(p->location.speed > 0)
			{
				// player released buttons while going forward - apply normal deceleration
				dv = (double)p->location.deceleration * dt;	// speed-decrease
				// distance traveled: s(t) = -0.5 * a * dt^2 + v * dt
				distance = (float)(-0.5 * (double)p->location.deceleration * pow(dt, 2.0) + (double)p->location.speed * dt);

				if(dv > p->location.speed) p->location.speed = 0.0;	// we came to a hold
				else p->location.speed -= (float)dv;
			}
			else if(p->location.speed < 0)
			{
				// player released buttons while going reverse - apply normal deceleration
				dv = (double)p->location.deceleration * dt;	// speed-increase
				// distance traveled: s(t) = 0.5 * a * dt^2 + -v * dt
				distance = (float)(0.5 * (double)p->location.deceleration * pow(dt, 2.0) + (double)p->location.speed * dt);

				if(dv > p->location.speed * -1) p->location.speed = 0.0;	// we came to a hold
				else p->location.speed += (float)dv;
			}

			if(p->left && !p->right)
			{
				turn_angle = (distance / TURNCIRCLE) * 360.0;
				turn_angle = turn_angle < 0.0 ? -turn_angle : turn_angle;
				if(p->location.speed > 0) left_forward(p, turn_angle);
				else left_reverse(p, turn_angle);
			}
			else if(!p->left && p->right)
			{
				turn_angle = (distance / TURNCIRCLE) * 360.0;
				turn_angle = turn_angle < 0.0 ? -turn_angle : turn_angle;
				if(p->location.speed > 0) right_forward(p, turn_angle);
				else right_reverse(p, turn_angle);
			}
			else
			{
				// now that we know the distance, decompose it into x and z distances
				distance_x = distance * cos(to_radians(p->location.direction));
				distance_z = distance * sin(to_radians(p->location.direction));
				p->location.position[0] += distance_x;
				p->location.position[1] += distance_z;
//				printf("p1 dx = %f, dz = %f\n", distance_x, distance_z);
			}
			changed = TRUE;
		}
	}
	else if(p->forward)
	{
		// player 1 presses forward
		if(p->location.speed >= 0)
		{
			float accel = (p->location.speed >= p->location.topspeed ? 0.0 : p->location.acceleration);

			// vehicle already going forward, apply normal acceleration
			// new speed: v + a * dt
			dv = (double)accel * dt;	// speed-increase

			// distance traveled: s(t) = 0.5 * a * dt^2 + v * dt
			distance = (float)(0.5 * (double)accel * pow(dt, 2.0) + (double)p->location.speed * dt);
		}
		else
		{
			// User pressed forward, while the vehicle is currenlty still in reverse,
			// apply the vehicle's brake deceleration to increase the speed
			// (to simulate hard braking). Compute new speed and the distance traveled
			// with the hard deceleration applied.
			// new speed: v + a * dt
			dv = (double)(p->location.brakedecel) * dt;	// level of speed-decrease

			// distance traveled: s(t) = 0.5 * a * dt^2 + v * dt
			distance = (float)(0.5 * (double)(p->location.brakedecel) * pow(dt, 2.0) + (double)p->location.speed * dt);
		}
		p->location.speed += (float)dv;
		if(p->location.speed > p->location.topspeed) p->location.speed = p->location.topspeed;

		if(p->left && !p->right)
		{
			turn_angle = (distance / TURNCIRCLE) * 360.0;
			turn_angle = turn_angle < 0.0 ? -turn_angle : turn_angle;
			if(p->location.speed > 0) left_forward(p, turn_angle);
			else left_reverse(p, turn_angle);
		}
		else if(!p->left && p->right)
		{
			turn_angle = (distance / TURNCIRCLE) * 360.0;
			turn_angle = turn_angle < 0.0 ? -turn_angle : turn_angle;
			if(p->location.speed > 0) right_forward(p, turn_angle);
			else right_reverse(p, turn_angle);
		}
		else
		{
			// now that we know the distance, decompose it into x and z distances
			distance_x = distance * cos(to_radians(p->location.direction));
			distance_z = distance * sin(to_radians(p->location.direction));
			p->location.position[0] += distance_x;
			p->location.position[1] += distance_z;
		}
		changed = TRUE;
	}
	else if(p->reverse)
	{
		// player 1 presses reverse
		if(p->location.speed <= 0)
		{
			float accel = (p->location.speed >= p->location.topspeed ? 0.0 : p->location.acceleration);

			// vehicle already going reverse, apply normal (negative) acceleration
			// new speed: v - a * dt
			dv = (double)accel * dt;	// speed-increase

			// distance traveled: s(t) = 0.5 * -a * dt^2 + v * dt
			distance = (float)(0.5 * (double)-accel * pow(dt, 2.0) + (double)p->location.speed * dt);
		}
		else
		{
			// User pressed reverse, while the vehicle is currenlty still going forward,
			// apply the vehicle's brake deceleration to decrease the speed
			// (to simulate hard braking). Compute new speed and the distance traveled
			// with the hard deceleration applied.
			// new speed: v - a * dt
			dv = (double)(p->location.brakedecel) * dt;	// level of speed-decrease

			// distance traveled: s(t) = 0.5 * -a * dt^2 + v * dt
			distance = (float)(0.5 * (double)-(p->location.brakedecel) * pow(dt, 2.0) + (double)p->location.speed * dt);
		}
		p->location.speed -= (float)dv;
		if(p->location.speed < -p->location.topspeed) p->location.speed = -p->location.topspeed;

		// now that we know the distance, decompose it into x and z distances
		if(p->left && !p->right)
		{
			turn_angle = (distance / TURNCIRCLE) * 360.0;
			turn_angle = turn_angle < 0.0 ? -turn_angle : turn_angle;
			if(p->location.speed > 0) left_forward(p, turn_angle);
			else left_reverse(p, turn_angle);
		}
		else if(!p->left && p->right)
		{
			turn_angle = (distance / TURNCIRCLE) * 360.0;
			turn_angle = turn_angle < 0.0 ? -turn_angle : turn_angle;
			if(p->location.speed > 0) right_forward(p, turn_angle);
			else right_reverse(p, turn_angle);
		}
		else
		{
			// go straight
			distance_x = distance * cos(to_radians(p->location.direction));
			distance_z = distance * sin(to_radians(p->location.direction));
			p->location.position[0] += distance_x;
			p->location.position[1] += distance_z;
		}
		changed = TRUE;
	}

	if(changed) {
		
		int i;

		// use GL to calculate the new transformation matrix
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
		glTranslatef(p->location.position[0], 0.0, p->location.position[1]);
		glRotatef(-p->location.direction, 0.0, 1.0, 0.0);
		glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&p->player_transformation_matrix);
		glPopMatrix();

		for(i = 0; i < (sizeof(p->model_corners) / sizeof(Vertex)); i++) {
			transform((GLfloat *)p->player_transformation_matrix, (GLfloat *)&p->model_corners[i], (GLfloat *)&p->model_absolute_corners[i]);
//			if(i == 0) fprintf(stdout, "New corner: (%f %f %f %f)\n", p->model_absolute_corners[i][0], p->model_absolute_corners[i][1], p->model_absolute_corners[i][2], p->model_absolute_corners[i][3]);
		}
	}

	p->moved = TRUE;
}

void idle(void)
{
	// compute the players' new positions

	if(animation) {
		Player org_p1, org_p2;
		BOOL p1_collided = FALSE, p2_collided = FALSE, kill = FALSE;
		double now = Wallclock();
		int i;

		locate_monster(&dino);
		memcpy(&org_p1, &p1, sizeof(Player));
		memcpy(&org_p2, &p2, sizeof(Player));
		locate_car(&p1);
		locate_car(&p2);

		if(p1.moved) {

			for(i = 0; i < (sizeof(p1.model_absolute_corners) / sizeof(Vertex)) && !p1_collided; i++) {

				// check the vector of each corner
				if(detectSceneryCollision(&org_p1.model_absolute_corners[i], &p1.model_absolute_corners[i], scenery)) {
					p1_collided = TRUE;
					break;
				}

				// check for objects inside the new car (between the corner vertices)
				if(i > 0) {
					if(detectSceneryCollision(&p1.model_absolute_corners[i-1], &p1.model_absolute_corners[i], scenery)) {
						p1_collided = TRUE;
						break;
					}
				} else {
					if(detectSceneryCollision(&p1.model_absolute_corners[i], &p1.model_absolute_corners[(sizeof(p1.model_absolute_corners) / sizeof(Vertex)) - 1], scenery)) {
						p1_collided = TRUE;
						break;
					}
				}
			}

			if(p1_collided) {
				// kill speed and restore original position
				org_p1.location.speed = 0.0;
				memcpy(&p1, &org_p1, sizeof(Player));
			}
		}
		if(p2.moved) {

			for(i = 0; i < (sizeof(p2.model_corners) / sizeof(Vertex)) && !p2_collided; i++) {

				// check the vector of each corner
				if(detectSceneryCollision(&org_p2.model_absolute_corners[i], &p2.model_absolute_corners[i], scenery)) {
					p2_collided = TRUE;
					break;
				}

				// check for objects inside the new car (between the corner vertices)
				if(i > 0) {
					if(detectSceneryCollision(&p2.model_absolute_corners[i-1], &p2.model_absolute_corners[i], scenery)) {
						p2_collided = TRUE;
						break;
					}
				} else {
					if(detectSceneryCollision(&p2.model_absolute_corners[i], &p2.model_absolute_corners[(sizeof(p2.model_absolute_corners) / sizeof(Vertex)) - 1], scenery)) {
						p2_collided = TRUE;
						break;
					}
				}
			}

			if(p2_collided) {
				// kill speed and restore original position
				org_p2.location.speed = 0.0;
				memcpy(&p2, &org_p2, sizeof(Player));
			}
		}

		// check for collisions between the cars - stop both on collision
		if(!p1_collided && p1.moved) {
			for(i = 0; i < (sizeof(p1.model_absolute_corners) / sizeof(Vertex)); i++) {
				if(vertexInPolygon(&p1.model_absolute_corners[i], (Vertex *)p2.model_absolute_corners, sizeof(p1.model_absolute_corners) / sizeof(Vertex))) {
					p1_collided = p2_collided = TRUE;
					fprintf(stdout, "Player 1 collided with player2.\n");
				}
			}
		}
		if(!p2_collided && p2.moved) {
			for(i = 0; i < (sizeof(p2.model_absolute_corners) / sizeof(Vertex)); i++) {
				if(vertexInPolygon(&p2.model_absolute_corners[i], (Vertex *)&p1.model_absolute_corners, sizeof(p1.model_absolute_corners) / sizeof(Vertex))) {
					p1_collided = p2_collided = TRUE;
					fprintf(stdout, "Player 2 collided with player1.\n");
				}
			}
		}

		if(p1_collided) {
			org_p1.location.speed = 0.0;
			memcpy(&p1, &org_p1, sizeof(Player));
		}
		if(p2_collided) {
			org_p2.location.speed = 0.0;
			memcpy(&p2, &org_p2, sizeof(Player));
		}

		if(!p1_collided) locate_camera(&p1);
		if(!p2_collided) locate_camera(&p2);
		p1.location.last_computation_time = p2.location.last_computation_time = now;
		p1.moved = p2.moved = FALSE;

		if(detectMonsterCollision(&dino, &p1)) {
			fprintf(stdout, "Player 1 hit the dino.\n");
			p1.points++;
			kill = TRUE;
		} else if(!kill && detectMonsterCollision(&dino, &p2)) {
			fprintf(stdout, "Player 2 hit the dino.\n");
			p2.points++;
			kill = TRUE;
		}
		if(kill) {
			// respawn monster
			LinkedList *landmark = spawnMonster(dino.trajectory);
			if(landmark != NULL) {
				dino.from = landmark;
				dino.to = landmark->next;
				memcpy(&dino.position, dino.from->data, sizeof(Vertex));
				locate_monster(&dino);	// automatically lets the monster face the right direction
				fprintf(stdout, "Monster respawned at (%f %f %f %f)\n", dino.position[0], dino.position[1], dino.position[2], dino.position[3]);
				
				// update the score in the outer screen
				glutSetWindow(window);
				glutPostRedisplay();
			}
		}
	}
	redisplay_all();
}

/*
 * Recomputes the position and angle of the camera to match the current
 * location and direction of the vehicle.
 */
void locate_camera(Player *p)
{
	if(p->camera.view == CAR)
	{
		// view from the vehicle
		p->camera.position[0] = p->location.position[0] - (p->camera.behind * cos(to_radians(p->location.direction)));
		p->camera.position[1] = p->camera.height;
		p->camera.position[2] = p->location.position[1] - (p->camera.behind * sin(to_radians(p->location.direction)));
		p->camera.center[0] = p->location.position[0] + (p->camera.viewDistance * cos(to_radians(p->location.direction)));
		p->camera.center[1] = 0.0;		// always focus at the road in front of us
		p->camera.center[2] = p->location.position[1] + (p->camera.viewDistance * sin(to_radians(p->location.direction)));
	}
	else
	{
		// view from helicopter-perspective
		p->camera.position[0] = -10.0;
		p->camera.position[1] = 20.0;
		p->camera.position[2] = 10.0;
		p->camera.center[0] = p->location.position[0];
		p->camera.center[1] = 0.0;
		p->camera.center[2] = p->location.position[1];
	}
	p->camera.up[0] = 0.0;
	p->camera.up[1] = 1.0;
	p->camera.up[2] = 0.0;
	glClearColor(0.5f,0.5f,0.5f,1.0f);			// We'll Clear To The Color Of The Fog ( Modified )
}

/*
 * Lets the player's vehicle make a left turn of turn_angle degrees and
 * computes (and updates) the player's new position and direction.
 */
void left_forward(Player *p, double turn_angle)
{
	double g, e, i, l, j;	// angles
	double P, Q, dx, dz;	// distances

	g = 180.0 - 90.0 - p->location.direction;
	e = 90.0 - turn_angle - g;
	i = 180.0 - 90.0 - e;
	l = (180.0 - turn_angle) / 2.0;
	j = 180.0 - l - i;
	P = TURNRADIUS * sin(to_radians(e));
	Q = TURNRADIUS * cos(to_radians(g));
	dx = Q - P;
	dz = dx * tan(to_radians(j));

	p->location.position[0] += dx;
	p->location.position[1] += dz;
	p->location.direction -= turn_angle;
	// now normalize the direction angle
	while(p->location.direction < 0.0) p->location.direction += 360.0;
}

void right_forward(Player *p, double turn_angle)
{
	// right-forward
	double m, g, e, i, l, j;	// angles
	double P, Q, dx, dz;		// distances

	m = 90.0 - p->location.direction;
	g = 180.0 - 90.0 - m;
	e = 90.0 - turn_angle - g;
	i = 180.0 - e - 90.0;
	l = (180.0 - turn_angle) / 2.0;
	j = 180.0 - l - i;
	P = TURNRADIUS * sin(to_radians(g));
	Q = TURNRADIUS * cos(to_radians(e));
	dx = Q - P;
	dz = dx / tan(to_radians(j));

	p->location.position[0] += dx;
	p->location.position[1] += dz;
	p->location.direction += turn_angle;
	// now normalize the direction angle
	while(p->location.direction > 360.0) p->location.direction -= 360.0;
}

void left_reverse(Player *p, double turn_angle)
{
	// left-reverse
	double m, g, e, i, l, j;	// angles
	double P, Q, dx, dz;		// distances

	m = 90.0 - p->location.direction;
	g = 180.0 - 90.0 - m;
	e = 90.0 - turn_angle - g;
	i = 180.0 - 90.0 - e;
	l = (180.0 - turn_angle) / 2.0;
	j = 180.0 - i - l;
	P = TURNRADIUS * cos(to_radians(m));
	Q = TURNRADIUS * cos(to_radians(e));
	dx = Q - P;
	dz = dx / tan(to_radians(j));

	p->location.position[0] -= dx;
	p->location.position[1] -= dz;
	p->location.direction += turn_angle;
	// now normalize the direction angle
	while(p->location.direction > 360.0) p->location.direction -= 360.0;
}

void right_reverse(Player *p, double turn_angle)
{
	// right-reverse
	double g, e, i, l, j;	// angles
	double P, Q, dx, dz;		// distances

	g = 180.0 - p->location.direction - 90.0;
	e = 90.0 - turn_angle - g;
	i = 180.0 - e - 90.0;
	l = (180.0 - turn_angle) / 2.0;
	j = 180.0 - i - l;
	P = TURNRADIUS * sin(to_radians(e));
	Q = TURNRADIUS * cos(to_radians(g));
	dx = Q - P;
	dz = dx * tan(to_radians(j));

	p->location.position[0] -= dx;
	p->location.position[1] -= dz;
	p->location.direction -= turn_angle;
	// now normalize the direction angle
	while(p->location.direction < 0.0) p->location.direction += 360.0;
}

void init()
{
	GLfloat dimensions[3];
	int i;

	// configure light types
	GLfloat light_ambient[] =	{0.2, 0.2, 0.2, 1.0};	// tiny bit of ambient light
	GLfloat light_diffuse[]	=	{1.0, 1.0, 1.0, 1.0};
	GLfloat light_specular[] =	{1.0, 1.0, 1.0, 1.0};

	// configure default light reflection properties
	GLfloat material_ambient[] =	{0.11, 0.11, 0.11, 1.0};
	GLfloat material_diffuse[] =	{0.5, 0.5, 0.50, 1.0};
	GLfloat material_specular[] =	{0.8, 0.8, 0.75, 1.0};
	GLfloat material_emission[] =	{0.0, 0.0, 0.0, 0.0};
	GLfloat material_shininess[] =	{80.0};

	// apply the lighting properties
	glLightfv(GL_LIGHT1, GL_AMBIENT, light_ambient);
	glLightfv(GL_LIGHT1, GL_DIFFUSE, light_diffuse);
	glLightfv(GL_LIGHT1, GL_SPECULAR, light_specular);
	glLightfv(GL_LIGHT2, GL_AMBIENT, light_ambient);
	glLightfv(GL_LIGHT2, GL_DIFFUSE, light_diffuse);
	glLightfv(GL_LIGHT2, GL_SPECULAR, light_specular);

	// apply the default material properties
	glMaterialfv(GL_FRONT, GL_AMBIENT, material_ambient);
	glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
	glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);
	glMaterialfv(GL_FRONT, GL_EMISSION, material_emission);
	glMaterialfv(GL_FRONT, GL_SHININESS, material_shininess);

	// turn on the light
	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT1);
	glEnable(GL_LIGHT2);
	glEnable(GL_TEXTURE_2D);

	// set the polygon mode to fill
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	glEnable(GL_DEPTH_TEST); /* enable hidden-surface-removal */
	glShadeModel(GL_SMOOTH);

	p2.model = p1.model = loadGLMmodel((char *)&porsche_file);
	p2.bounding_box = p2.bounding_box = FALSE;
	glmDimensions(p1.model, (GLfloat *)&dimensions);

	p1.location.position[0] = 0.0;
	p1.location.position[1] = 3.0;
	p1.location.direction = 0.0;
	p1.camera.height = dimensions[1] * 1.9;
	p1.camera.behind = dimensions[2] / 2.0;
	p1.location.last_computation_time = Wallclock();
	locate_camera(&p1);

	p2.location.position[0] = 0.0;
	p2.location.position[1] = -1.0;
	p2.location.direction = 180.0;
	p2.camera.height = dimensions[1] * 1.9;
	p2.camera.behind = dimensions[2] / 2.0;
	p2.location.last_computation_time = Wallclock();
	locate_camera(&p2);

	// compute and store the model's offset matrix and player transformation
	glMatrixMode(GL_MODELVIEW);
	glPushMatrix();

	glLoadIdentity();
	glTranslatef(p1.location.position[0], 0.0, p1.location.position[1]);
	glRotatef(p1.location.direction, 0.0, 1.0, 0.0);
	glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&p1.player_transformation_matrix);

	glLoadIdentity();
	glTranslatef(p2.location.position[0], 0.0, p2.location.position[1]);
	glRotatef(p2.location.direction, 0.0, 1.0, 0.0);
	glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&p2.player_transformation_matrix);

	glLoadIdentity();
	glTranslatef(0.0, dimensions[1] / 2.0, 0.0);
	glRotatef(90, 0.0, 1.0, 0.0);
	glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&p1.model_offset_matrix);
	glGetFloatv(GL_MODELVIEW_MATRIX, (GLfloat *)&p2.model_offset_matrix);
	glPopMatrix();

	// calculate the model's bounding box for collision detection
	// front left
	p2.model_corners[0][0] = p1.model_corners[0][0] = dimensions[2] / 2.1;
	p2.model_corners[0][1] = p1.model_corners[0][1] = dimensions[1] / 2.0;
	p2.model_corners[0][2] = p1.model_corners[0][2] = -dimensions[0] / 2.1;
	p2.model_corners[0][3] = p1.model_corners[0][3] = 1.0;

	// left rear
	p2.model_corners[1][0] = p1.model_corners[1][0] = -dimensions[2] / 2.1;
	p2.model_corners[1][1] = p1.model_corners[1][1] = dimensions[1] / 2.0;
	p2.model_corners[1][2] = p1.model_corners[1][2] = -dimensions[0] / 2.1;
	p2.model_corners[1][3] = p1.model_corners[1][3] = 1.0;

	// right rear
	p2.model_corners[2][0] = p1.model_corners[2][0] = -dimensions[2] / 2.1;
	p2.model_corners[2][1] = p1.model_corners[2][1] = dimensions[1] / 2.0;
	p2.model_corners[2][2] = p1.model_corners[2][2] = dimensions[0] / 2.1;
	p2.model_corners[2][3] = p1.model_corners[2][3] = 1.0;

	// right front
	p2.model_corners[3][0] = p1.model_corners[3][0] = dimensions[2] / 2.1;
	p2.model_corners[3][1] = p1.model_corners[3][1] = dimensions[1] / 2.0;
	p2.model_corners[3][2] = p1.model_corners[3][2] = dimensions[0] / 2.1;
	p2.model_corners[3][3] = p1.model_corners[3][3] = 1.0;

	// apply the transformation matrix to calculate the absolute position for the corners
	for(i = 0; i < (sizeof(p1.model_corners) / sizeof(Vertex)); i++) {
		transform((GLfloat *)p1.player_transformation_matrix, (GLfloat *)&p1.model_corners[i], (GLfloat *)&p1.model_absolute_corners[i]);
		transform((GLfloat *)p2.player_transformation_matrix, (GLfloat *)&p2.model_corners[i], (GLfloat *)&p2.model_absolute_corners[i]);
	}

	dino.last_computation_time = Wallclock();

	// create the context menu
	createMenu();
}

void usage(void) {
	fprintf(stdout, "	-= Multiplayer hunt for Godzilla =-\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "In this split-screen mini-game, 2 players each drive a Porsche 928 through\n");
	fprintf(stdout, "the same city with a mission to run over dinosaurs before they get a chance\n");
	fprintf(stdout, "to destroy the city.\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "You kill Godzilla by simply running into him. The statistics above your\n");
	fprintf(stdout, "viewport reflect the number of kills you have made.\n");
	fprintf(stdout, "See if you can get more kills than your opponent!\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "			Controls\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "Player 1 (top screen)           Player 2 (lower screen)\n");
	fprintf(stdout, "accelerate:     w               accelerate:     up-arrow\n");
	fprintf(stdout, "brake/reverse:  s               brake/reverse:  down-arrow\n");
	fprintf(stdout, "left:           a               left:           left-arrow\n");
	fprintf(stdout, "right:          d               right:          right-arrow\n");
	fprintf(stdout, "camera view:    e               camera view:    delete\n");
}

int main(int argc, char **argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize (width, height);
	glutInitWindowPosition(0,0);

	window = glutCreateWindow("Roadkill");
	glutReshapeFunc(main_reshape);
	glutDisplayFunc(main_display);
	glutKeyboardFunc(keyboard);
	glutKeyboardUpFunc(keyboardUp);
	glutSpecialFunc(specialFunc);
	glutSpecialUpFunc(specialUpFunc);
	glutIdleFunc(idle);
    
    top = glutCreateSubWindow(window, GAP, GAP, width - (2 * GAP), (int)(((float)height / 2.0) - 1.5 * GAP));
    glutReshapeFunc(top_reshape);
    glutDisplayFunc(top_display);
    glutKeyboardFunc(keyboard);
	glutKeyboardUpFunc(keyboardUp);
	glutSpecialFunc(specialFunc);
	glutSpecialUpFunc(specialUpFunc);
	if(loadMap(&scenery, &dino.trajectory, top) != 0) {
		fprintf(stdout, "Error loading scenery for the upper screen.\n");
		exit(1);
	}
	loadDino((TreeNode **)&dino.model_graph, top);
	init();
	// create the models

    bottom = glutCreateSubWindow(window, GAP, (int)(((float)height / 2.0) + (0.5 * GAP)), width - (2 * GAP), (int)(((float)height / 2.0) - 1.5 * GAP));
    glutReshapeFunc(bottom_reshape);
    glutDisplayFunc(bottom_display);
    glutKeyboardFunc(keyboard);
	glutKeyboardUpFunc(keyboardUp);
	glutSpecialFunc(specialFunc);
	glutSpecialUpFunc(specialUpFunc);
	// create the models
	if(loadMap(&scenery, &dino.trajectory, bottom) != 0) {
		fprintf(stdout, "Error loading scenery for the lower screen.\n");
		exit(1);
	}
	loadDino((TreeNode **)&dino.model_graph, bottom);
	init();

	srand((unsigned int)Wallclock());
	dino.from = spawnMonster(dino.trajectory);
	dino.to = dino.trajectory->next;
	dino.speed = 2.0;	// start easy
	memcpy(&dino.position, dino.from->data, sizeof(Vertex));
	dino.last_computation_time = Wallclock();
	locate_monster(&dino);

	last_fps_collection = Wallclock();
	usage();
	glutMainLoop();
}

void setfont(char* name, int size)
{
    font_style = GLUT_BITMAP_HELVETICA_10;
    if (strcmp(name, "helvetica") == 0) {
        if (size == 12) 
            font_style = GLUT_BITMAP_HELVETICA_12;
        else if (size == 18)
            font_style = GLUT_BITMAP_HELVETICA_18;
    } else if (strcmp(name, "times roman") == 0) {
        font_style = GLUT_BITMAP_TIMES_ROMAN_10;
        if (size == 24)
            font_style = GLUT_BITMAP_TIMES_ROMAN_24;
    } else if (strcmp(name, "8x13") == 0) {
        font_style = GLUT_BITMAP_8_BY_13;
    } else if (strcmp(name, "9x15") == 0) {
        font_style = GLUT_BITMAP_9_BY_15;
    }
}

void drawstr(GLuint x, GLuint y, char* format, ...)
{
    va_list args;
    char buffer[255], *s;
    
    va_start(args, format);
    vsprintf(buffer, format, args);
    va_end(args);
    
    glRasterPos2i(x, y);
    for (s = buffer; *s; s++)
        glutBitmapCharacter(font_style, *s);
}
