#include <stdio.h>
main ()
{

	int i, c, teken[256], j=0;

for (i=0; i<=255; ++i)
	teken[i] = 0;
while ((c = getchar()) != EOF)
	++teken[c];

while (j<=255) {
	if (teken[j] >= 1) printf ("%c komt %d keer voor.\n", j, teken[j]);
	++j;
	}
}


