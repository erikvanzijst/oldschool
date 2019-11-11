/* display-list-torus.c    */

// T. Kielmann, VU Amsterdam
// november 2001

// compares performance of display lists with immediate mode rendering
// with a rotating torus

#include <stdlib.h>

#if defined(WIN32)

#include <winsock.h>
#include <sys\timeb.h>
int gettimeofday(struct timeval *tp, void *tzp);
#define usleep(mseconds) Sleep(mseconds/1000)

#define M_PI 3.14159

#else

#include <unistd.h>
#include <sys/time.h>

#endif

#include <GL/gl.h>
#include <GL/glu.h>
#include <stdio.h>
#include <math.h>
#include <GL/glut.h>
#include <stdlib.h>

// Return the time in sec. in a double value.
double Wallclock(void);
// Random value within an interval
float frand(float min, float max);

GLuint theTorus;
int mode = 0;

int win_x,win_y;
	static double lasttime = 0.0;

double
Wallclock(void)
{
    struct timeval tv;

    gettimeofday(&tv, 0);

    return (((double) tv.tv_sec) + ((double) tv.tv_usec) / 1000000.0);
}

#if defined(WIN32)
int
gettimeofday(struct timeval *tp, void *tzp)
{
	struct _timeb t;

    _ftime(&t);
    tp->tv_sec = t.time;
    tp->tv_usec = t.millitm * 1000;
    return 0;
}
#endif

float frand(float min, float max)
{
        float rr;

#if defined(WIN32)
		rr = (float)rand() / (float)RAND_MAX;
#else
        rr = drand48();
#endif
        return min + rr * (max - min);
}


/* Draw a torus */
static void torus(int numc, int numt)
{
   int i, j, k;
   double s, t, x, y, z, twopi;
	float c;

   twopi = 2 * (double)M_PI;
   for (i = 0; i < numc; i++) {
      glBegin(GL_QUAD_STRIP);
      for (j = 0; j <= numt; j++) {
         for (k = 1; k >= 0; k--) {
            s = (i + k) % numc + 0.5;
            t = j % numt;

            x = (1+.1*cos(s*twopi/numc))*cos(t*twopi/numt);
            y = (1+.1*cos(s*twopi/numc))*sin(t*twopi/numt);
            z = .1 * sin(s * twopi / numc);
			c = (double) i / (double)numc;
			glColor3f(c,0,c);
            glVertex3f(x, y, z);
         }
      }
      glEnd();
   }
}

/* Create display list with Torus and initialize state*/
static void init(void)
{
   mode = 0;
   fprintf(stderr, "Display list enabled.\n");
   theTorus = glGenLists (1);
   glNewList(theTorus, GL_COMPILE);
   torus(64, 250);
   //   torus(32, 50);
   glEndList();

   glShadeModel(GL_SMOOTH);
   glClearColor(0.0, 0.0, 0.0, 0.0);
   lasttime = Wallclock();
}


void display(void)
{
	glClear(GL_COLOR_BUFFER_BIT);

	if (mode == 0)
	{
		glCallList(theTorus);
	}
	else
	{
		torus(64, 250);
	}

	glutSwapBuffers();
}

void reshape(int w, int h)
{
  win_x = w; win_y = h;
   glViewport(0, 0, (GLsizei) w, (GLsizei) h);
   glMatrixMode(GL_PROJECTION);
   glLoadIdentity();
   gluPerspective(30, (GLfloat) w/(GLfloat) h, 1.0, 20.0);
   glMatrixMode(GL_MODELVIEW);
   glLoadIdentity();
   gluLookAt(0, 0, 5, 0, 0, 0, 0, 1, 0);
}

/* Rotate about x-axis when "x" typed; rotate about y-axis
   when "y" typed; "i" returns torus to original view */
void keyboard(unsigned char key, int x, int y)
{
   switch (key) {
   case 'l':
   case 'L':
	   if (mode == 1) {
	     mode = 0;
	     fprintf(stderr, "Display list enabled.\n");
	   }
	   else {
	     mode = 1;
	     fprintf(stderr, "Display list disabled.\n");
	   }
	   break;
   case 27:
      exit(0);
      break;
   }
}


void idle(){
	static int nbframes = 0;
	double currenttime;

        glRotatef(5.0,1.0,0.0,0.0);
        glRotatef(5.0,0.0,1.0,0.0);

	glutPostRedisplay();
	nbframes ++;

	if ( nbframes > 30 )
	{
		currenttime = Wallclock();
 		fprintf(stderr, "FPS : %6.2f\n", 30.0 / ( currenttime - lasttime ) );
		lasttime = currenttime;
		nbframes = 0;
	}
}

int main(int argc, char **argv)
{
   win_x = 800;
   win_y = 600;
   glutInitWindowSize(win_x, win_y);
   glutInit(&argc, argv);
   glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
   glutCreateWindow(argv[0]);
   init();
   reshape(win_x,win_y);
   glutReshapeFunc(reshape);
   glutKeyboardFunc(keyboard);
   glutDisplayFunc(display);
   glutIdleFunc(idle);
   glutMainLoop();
   return 0;
}
