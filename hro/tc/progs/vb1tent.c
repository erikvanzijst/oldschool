#include <stdio.h>
main ()
{

	int x, y;

x = 100;
y = 0;

while (x > 16)
{
	--y;
	x = x + y;
}

printf ("X en Y zijn %d en %d \n", x, y);
getch ();
}