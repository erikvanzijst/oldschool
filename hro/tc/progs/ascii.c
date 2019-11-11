#include <stdio.h>
main ()
{
/* ASCII printend progje */

	int i;

clrscr ();
for (i=0; i<=255; ++i)
	{printf ("%4d = %2c ", i, i);
	if (i == 127) {printf ("\nPress NE key 2 continue...\n"); getch();}
	}
getch ();
}