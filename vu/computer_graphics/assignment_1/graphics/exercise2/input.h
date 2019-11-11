/*
 * Computer Graphics Course 2004
 *
 * Exercise 2
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com - 06.oct.2004
 */

#ifndef __INPUT_H
#define __INPUT_H

void keyboard(unsigned char key, int x, int y);
void mouse(int button, int state, int x, int y);
void passiveMotionTracking(int x, int y);
void motionTracking(int x, int y);

#endif __INPUT_H
