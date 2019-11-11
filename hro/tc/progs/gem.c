#include <stdio.h>
main ()
{
/* GEM.C is gemaakt door Erik van Zijst op 17-9-96. */

	float a, b;
	float gem;

printf ("\nVoer 2 getallen in om het gemiddelde te berekenen.\n \nA = ");
scanf ("%f", &a);
printf ("B = ");
scanf ("%f", &b);

gem = ((a + b) / 2);

printf ("\nHet gemiddelde is: %f.\n", gem);
}