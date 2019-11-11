#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

#include <profstub.h>


int main(int argc, const char* argv[])
{
	int retval = 0;

	if (argc > 3)
	{
		int pid = atoi(argv[1]);
		int start = strtol(argv[2], 0, 16);
		int end = strtol(argv[3], 0, 16);

		retval = profile(pid, start, end);
		if (retval) perror("profile call failed");
	}

	return retval;
}


