#include <stdio.h>
#define MAXX 30
#define MAXY 10
main ()
{
	int matrix [MAXX] [MAXY];
	int x, y;

clrscr ();
for (y=0; y<MAXY; ++y) {
	for (x=0; x<MAXX; ++x)
		matrix [x] [y] = 0;
	}

for (y=0; y<MAXY; ++y) {
	for (x=0; x<MAXX; ++x)
		printf ("%d", matrix [x] [y]);
	printf ("\n");
	}
printf ("\n");

for (y=0; y<MAXY; ++y) {
	for (x=0; x<MAXX; ++x)
		if (x < y) {matrix [x] [y] = 1; }
	}

for (y=0; y<MAXY; ++y) {
	for (x=0; x<MAXX; ++x)
		printf ("%d", matrix[x][y]);
	printf ("\n");
	}
}