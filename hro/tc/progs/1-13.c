#include <stdio.h>
main ()
{
/* Blz 32, vraag 1-13. Drukt histogram af van lengte van de ingevoerde
   woorden. Erik van Zijst, 0515287, HI1C, 14/11/96. */

	int i, c, woord=1, spatie=0, lengte[1000];

for (i=0; i<1000; ++i)
	lengte[i] = 0;

while ((c = getchar()) != EOF)
	if (c != ' ') {
		spatie=0;
		++lengte[woord];
		}
	else {
		if (spatie == 0) {
			spatie = 1;
			++woord; }
		}