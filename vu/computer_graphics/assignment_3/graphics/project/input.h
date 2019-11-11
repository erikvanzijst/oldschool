#ifndef __INPUT_H
#define __INPUT_H

void createMenu(void);
void keyboard(unsigned char key, int x, int y);
void specialFunc(int key, int x, int y);
void keyboardUp(unsigned char key, int x, int y);
void specialUpFunc(int key, int x, int y);
void mouse(int button, int state, int x, int y);
void passiveMotionTracking(int x, int y);
void motionTracking(int x, int y);

#endif __INPUT_H
