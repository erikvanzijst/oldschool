#include <stdio.h>
#include <conio.h>
main ()
{
/* BUZZ.C is geschreven door Erik van Zijst op 27-9-96 Studie#: 0515287 */

	int invoer = 11, x, tiental = 10, honderdtal = 100, rest;

clrscr ();
printf ("Voer een getal in van 1 tot 9. Dan worden alle getallen tot 1000 afgebeeld,\n");
printf ("behalve de getallen die deelbaar zijn door jouw getal en waar jouw getal in\n");
printf ("voorkomt.\n");

while ((invoer <= 0) || (invoer >= 10)) {
printf ("Invoer (1-9): ");
scanf ("%d", &invoer); }

for (x=1; x<1000; ++x) {
	rest = (x % invoer);
	if (x == 500) { printf ("\nDruk op een toets voor vervolg...\n"); getch (); }
	if (x >= 10) { tiental = (x / 10); }
	if (x >= 100) { honderdtal = (x / 100); }
	if ((((x - invoer) % 10) == 0) || (((tiental - invoer) % 10) == 0) || (((honderdtal - invoer) % 10) == 0) || (rest == 0))
		printf ("");        /* Doet niets, rechtvaardigt ELSE */
	else
		printf ("%3d ", x);
	}
getch ();
}