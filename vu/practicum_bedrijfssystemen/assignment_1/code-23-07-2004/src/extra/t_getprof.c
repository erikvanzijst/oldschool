#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

#include <profstub.h>


int main(int argc, const char* argv[])
{
	int retval = 0;
	struct prof_data profdata;

	retval = getprof(&profdata);
	if (retval) perror("getprof call failed");

	return retval;
}

