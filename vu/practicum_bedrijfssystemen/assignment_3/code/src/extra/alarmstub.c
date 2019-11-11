#include "lib.h"

unsigned int alarm2(unsigned int sec)
{
	message m;

	m.m1_i1 = sec;
	return _syscall(MM, ALARM2, &m);
}

