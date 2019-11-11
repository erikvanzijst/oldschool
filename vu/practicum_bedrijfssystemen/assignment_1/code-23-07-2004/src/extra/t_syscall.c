#include <stdlib.h>
#include <time.h>

int main(void)
{
	long i;

	for(i = 0; i < 1000000; i++)
	{
		time(NULL);
	}

	return 0;
}

