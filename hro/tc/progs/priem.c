#include <stdio.h>
#include <conio.h>
main ()
{
/*PRIEM.C is geschreven door Erik van Zijst op 23-9-96. Studie#: 0515287*/

	int aantal=0, priem=1, getal=3, x, invoer;

printf ("\nHoeveel priemgetallen wil je berekenen? (1-32767): ");
scanf ("%d", &invoer);

printf ("\n \n \n       2");
while (aantal<=(invoer - 2)) {
	priem = 1;
	for (x=((getal-1)/2); x>1; --x)
		{
		if ((getal % x)==0)
			priem=0;
		}
	if (priem == 1)	{ printf ("%8d", getal);
	++aantal; }
	getal = getal + 2;
	}

printf ("\n \nDe bovenstaande getallen zijn de eerste %d priemgetallen.\n \n \n", invoer);
getch ();
}
