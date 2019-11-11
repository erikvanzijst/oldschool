#include <stdio.h>
unsigned inverteer(unsigned x, int p, int n);

void main ()
{
/* Opgave 3A. Eerste tentamen opgave tweede kwartaal. Voor deze opgave heb
   ik opgave 2.7 (blz. 68) uit het studieboek gemaakt.
   In dit programma worden de bits uit een deelrij met variabele lengte uit
   een opgegeven waarde ge‹nverteerd. Het resultaat wordt decimaal gegeven.
   Gemaakt door ERIK VAN ZIJST, 0515287, HI1C
*/

	int waarde, deelrij, positie;

printf ("\n\nOpgave 2.7\nGeef een waarde om te bewerken: ");
scanf ("%d", &waarde);
printf ("\nGeef de lengte van een deelrij om te inverteren: ");
scanf ("%d", &deelrij);
printf ("\nGeef de beginpositie van de deelrij: ");
scanf ("%d", &positie);

printf ("\nWaarde na bewerking: %d\n", inverteer(waarde, deelrij, positie) );
getch ();
}

unsigned inverteer(unsigned x, int n, int p)
{
		unsigned y, resultaat;

	y = 0xFF;
	y = (y >> (8 - n));
	y = (y << (p + 1 - n));
	resultaat = x^(y);
	return resultaat;
}