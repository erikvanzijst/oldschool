#include <stdio.h>
#define TRUE	1
#define FALSE	0
main ()
{

	int spatie=FALSE, c;

while ((c = getchar()) != EOF) {
	if (c != ' ') {printf ("%c", c); spatie = FALSE; }
	else
		if (spatie == FALSE) { printf ("%c", c); spatie = TRUE; }
}
}