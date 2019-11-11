/* robot-pick.c    */

// T. Kielmann, VU Amsterdam
// modified Angel's source code to allow picking instead of a menu
// october 2002

/* E. Angel, Interactive Computer Graphics */
/* A Top-Down Approach with OpenGL, Third Edition */
/* Addison-Wesley Longman, 2003 */

/* Robot program (Chapter 9). Cylinder for base, scaled cube for arms */
/* Shows use of instance transformation to define parts (symbols) */
/* The cylinder is a quadric object from the GLU library */
/* The cube is also obtained from GLU */

#include <GL/glut.h>

/* Let's start using #defines so we can better
interpret the constants (and change them) */

#define BASE_HEIGHT 2.0
#define BASE_RADIUS 1.0
#define LOWER_ARM_HEIGHT 5.0
#define LOWER_ARM_WIDTH 0.5
#define UPPER_ARM_HEIGHT 5.0
#define UPPER_ARM_WIDTH 0.5

typedef float point[3];

static GLfloat theta[] = {0.0,0.0,0.0};
static GLint axis = 0;
GLUquadricObj  *p; /* pointer to quadric object */

static int selected = 0;

/* Define the three parts */
/* Note use of push/pop to return modelview matrix
to its state before functions were entered and use
rotation, translation, and scaling to create instances
of symbols (cube and cylinder */

void base()
{
  if (selected == 1){
    glColor3f(0.0, 0.0, 1.0);
  }else{
    glColor3f(1.0, 0.0, 0.0);
  }
  glPushMatrix();

/* rotate cylinder to align with y axis */

  glRotatef(-90.0, 1.0, 0.0, 0.0);

/* cyliner aligned with z axis, render with
   5 slices for base and 5 along length */

  gluCylinder(p, BASE_RADIUS, BASE_RADIUS*3/4, BASE_HEIGHT, 25, 25);
  glPopMatrix();
}

void lower_arm()
{
  if (selected == 2){
    glColor3f(0.0, 0.0, 1.0);
  }else{
    glColor3f(1.0, 0.0, 0.0);
  }
  glPushMatrix();
  glTranslatef(0.0, 0.5*LOWER_ARM_HEIGHT, 0.0);
  glScalef(LOWER_ARM_WIDTH, LOWER_ARM_HEIGHT, LOWER_ARM_WIDTH);
  //   glutWireCube(1.0);
  glutSolidCube(1.0);
  glPopMatrix();
}

void upper_arm()
{
  if (selected == 3){
    glColor3f(0.0, 0.0, 1.0);
  }else{
    glColor3f(1.0, 0.0, 0.0);
  }
  glPushMatrix();
  glTranslatef(0.0, 0.5*UPPER_ARM_HEIGHT, 0.0);
  glScalef(UPPER_ARM_WIDTH, UPPER_ARM_HEIGHT, UPPER_ARM_WIDTH);
  //   glutWireCube(1.0);
  glutSolidCube(1.0);
  glPopMatrix();
}


void draw(GLenum mode)
{

/* Accumulate ModelView Matrix as we traverse tree */
  if (mode==GL_SELECT) glLoadName(1);
    glLoadIdentity();
    glRotatef(theta[0], 0.0, 1.0, 0.0);
    base();
  if (mode==GL_SELECT) glLoadName(2);

    glTranslatef(0.0, BASE_HEIGHT, 0.0);
    glRotatef(theta[1], 0.0, 0.0, 1.0);
    lower_arm();
  if (mode==GL_SELECT) glLoadName(3);
    glTranslatef(0.0, LOWER_ARM_HEIGHT, 0.0);
    glRotatef(theta[2], 0.0, 0.0, 1.0);
    upper_arm();
    glFlush();
    glutSwapBuffers();
}


void display(void){
    glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    draw(GL_RENDER);
}




void processHits(GLint hits, GLuint buffer[]){
  unsigned int i,j;
  GLuint names, *ptr;

  //  printf("hits = %d\n",hits);
  ptr = (GLuint *) buffer;
  if ( hits == 0 ) selected = 0;
  for (i=0; i<hits; i++){
    names = *ptr;
    ptr+=3;
    selected = *ptr;
    //    printf("now selected %d\n",selected);
    for (j=0; j<names; j++){
      if (*ptr==1) {
	// printf("base\n");
      }
      if (*ptr==2) {
	// printf("lower arm\n");
      }
      if (*ptr==3) {
	// printf("upper arm\n");
      }
      ptr++;
    }
  }
}


void mouse(int button, int state, int x, int y)
{
  GLuint selectBuf[128];
  GLint hits;
  GLint viewport[4];
  int w,h;

  if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN) {
    glGetIntegerv (GL_VIEWPORT, viewport);
    glSelectBuffer (128, selectBuf);
    glRenderMode(GL_SELECT);
    glInitNames(); glPushName(0);
    glMatrixMode (GL_PROJECTION);
    glPushMatrix();
    glLoadIdentity();
    gluPickMatrix((GLdouble) x, (GLdouble)(viewport[3] -y),
                  5.0, 5.0, viewport);
    w = glutGet(GLUT_WINDOW_WIDTH);
    h = glutGet(GLUT_WINDOW_HEIGHT);    
    // printf("w=%d, h=%d\n",w,h);
    if (w <= h)
        glOrtho(-10.0, 10.0, -5.0 * (GLfloat) h / (GLfloat) w,
            15.0 * (GLfloat) h / (GLfloat) w, -10.0, 10.0);
    else
        glOrtho(-10.0 * (GLfloat) w / (GLfloat) h,
            10.0 * (GLfloat) w / (GLfloat) h, -5.0, 15.0, -10.0, 10.0);
    glMatrixMode(GL_MODELVIEW);
    draw(GL_SELECT);
    glMatrixMode(GL_PROJECTION);
    glPopMatrix();
    glMatrixMode(GL_MODELVIEW);
    glFlush();
    glutSwapBuffers();
    hits = glRenderMode(GL_RENDER);
    processHits(hits,selectBuf);
    glutPostRedisplay();
  }
}



void 
myReshape(int w, int h)
{
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    if (w <= h)
        glOrtho(-10.0, 10.0, -5.0 * (GLfloat) h / (GLfloat) w,
            15.0 * (GLfloat) h / (GLfloat) w, -10.0, 10.0);
    else
        glOrtho(-10.0 * (GLfloat) w / (GLfloat) h,
            10.0 * (GLfloat) w / (GLfloat) h, -5.0, 15.0, -10.0, 10.0);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
}

void myinit()
{
   glClearColor(1.0, 1.0, 1.0, 1.0);
   //   glColor3f(1.0, 0.0, 0.0);
   selected = 0;

   p=gluNewQuadric(); /* allocate quadric object */
   //gluQuadricDrawStyle(p, GLU_FILL); /* render it as wireframe */
   gluQuadricDrawStyle(p, GLU_LINE); /* render it as wireframe */
   glEnable(GL_DEPTH_TEST);
}


void keyboard(unsigned char key, int x, int y){
  switch (key){
  case 'q':
  case 'Q':
  case 27: exit();
  }
}

void special_key(int key, int x, int y){
  switch (key){
  case GLUT_KEY_LEFT:
        theta[0] += 5.0;
        if( theta[0] > 360.0 ) theta[0] -= 360.0;
        glutPostRedisplay();
	break;
  case GLUT_KEY_RIGHT:
        theta[0] -= 5.0;
        if( theta[0] < 360.0 ) theta[0] += 360.0;
        glutPostRedisplay();
	break;
  case GLUT_KEY_UP:
    if ( selected ){
        theta[selected-1] += 5.0;
        if( theta[selected-1] > 360.0 ) theta[selected-1] -= 360.0;
        glutPostRedisplay();
        }
    break;
  case GLUT_KEY_DOWN:
    if ( selected ){
        theta[selected-1] -= 5.0;
        if( theta[selected-1] < 0.0 ) theta[selected-1] += 360.0;
        glutPostRedisplay();
        }
    break;

  }
}

int
main(int argc, char **argv)
{
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
    glutInitWindowSize(500, 500);
    glutCreateWindow("robot");
    myinit();
    glutReshapeFunc(myReshape);
    glutDisplayFunc(display);
    glutMouseFunc(mouse);

    glutKeyboardFunc(keyboard);
    glutSpecialFunc(special_key);

    glutMainLoop();
    return 0;   // not reached
}
