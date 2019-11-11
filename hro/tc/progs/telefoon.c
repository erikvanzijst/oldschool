#include <stdio.h>
#include <conio.h>
#include <time.h>
main ()
{
/* Telefoonkosten teller. Gemaakt door Erik van Zijst. 16/11/96 */

	int c, d, tikken, tiktarief, h;
	float kosten;
	clock_t start, huidig, stop;

clrscr();
printf ("\n	Telefoonkosten teller. Door Erik van Zijst.\n\n\n");
do 	{
	printf ("\r(T)imer starten of zelf een tijd (I)ngeven? (t/i): ");
	c = getchar();
	}
while ((c != 't') && (c != 'i') && (c != 'T') && (c != 'I'));

do	{
	printf ("\rBel je (L)okaal of (I)nterlokaal? (l/i): ");
	d = getchar();
	}
while ((d != 'l') && (d != 'i') && (d != 'L') && (d != 'I'));
if ((d == 'l') || (d == 'L')) tiktarief = 300;
else tiktarief = 94;

if ((c == 't') || (c == 'T')) {
	start = clock();
	while ((h = getchar()) != 's') {
		gotoxy(1,8);
		printf ("De timer loopt.\n");
		huidig = clock();
		printf ("\n  	Online: %.0f sec.", (huidig/ CLK_TCK)-(start/ CLK_TCK));
		tikken = ((((huidig/ CLK_TCK)-(start/ CLK_TCK)) / tiktarief) + 1);
		printf ("\n	Aantal telefoon-tikken: %d", tikken);
		kosten = (tikken * 0.165);
		printf ("\n	Kosten tot nu toe: f %.2f", kosten);
		printf ("\n\nDruk op 's' om de teller te stoppen, elke andere toets voor nieuw overzicht. ");
		}
	stop = clock ();
	printf ("De teller is gestopt. De kosten bedragen:\n");
	printf ("\n	Totaal online: %.0f sec.", (stop/ CLK_TCK)-(start/ CLK_TCK));
	tikken = ((((stop/ CLK_TCK)-(start/ CLK_TCK)) / tiktarief) + 1);
	printf ("\n	Totaal aantal tikken: %d", tikken);
	kosten = (tikken * 0.165);
	printf ("\n	Totale kosten: f %.2f", kosten);
	}
printf ("\n\nBedankt voor het gebruik van de kosten teller.\n");
}