#include <stdio.h>
main ()
{

	int x, y, getal = 0;

for (x=0; x<10; ++x)
	for (y=10; y>0; --y)
		if (getal < (x+y))
			++getal;

printf ("Het onbekende getal is %d \n", getal);
getch ();
}