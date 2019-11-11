#include <stdio.h>
#include <conio.h>
#include <time.h>
main ()
{
/* TENT1.C berekent zo snel mogelijk de 9-cijferige getallen waar elk getal 1
   keer in voorkomt en het kwadraat is van een geheel getal.
   Door Erik van Zijst Studie#: 0515287 - 16/10/96 */

	int cijfer[10], truefalse[10], y, i, geen = 0;
	long getal, dec, x;
	clock_t start, end;

clrscr ();
printf ("Dit programma berekent zo mogelijk snel alle 9-cijferige getallen waar elk\n");
printf ("getal 1 keer in voorkomt en het kwadraat is van een geheel getal.\n");
printf ("Het eerste getal is het bewuste 9-cijferig getal. Tussen haken staat de wortel.\n");
printf ("\n    Geschreven door Erik van Zijst. Studie#: 0515287 - Klas: HI1C 18-10-96\n\n");
start = clock();

for (x=11111; x<31427; ++x)
{	getal = (x * x);
	dec = 1;
	for (y=1; y<=9; ++y) truefalse[y] = 0;
	for (y=1; y<=9; ++y)
	{ 	cijfer[y] = i = ((getal / dec) - (10 * (getal / (dec * 10))));
		dec = (dec * 10);
		if (truefalse[i] == 0)
			truefalse[i] = 1;
		else
			{ geen = 1; y = 10; break; }
	}
	if (geen == 0) printf ("%ld [%d]   ", getal, x);
	else geen = 0;
}
end = clock();
printf ("\n\ntijd: %.2f sec",(end-start)/ CLK_TCK);

getch ();

/* TOELICHTING:
   Het programma kwadrateert alle waarden tussen 11111 en 31427. Het kwadraat
   van 11111 is namelijk het eerste getal voor 123456789 (het kleinste getal
   dat tot de doelgroep kan horen) en het kwadraat van 31427 is het eerste
   getal na 987654321 (het laatste getal dat interessant is).
   Vervolgens controleert het programma elk getal op de inhoud: als het een
   cijfer voor een tweede keer tegenkomt, staakt het de controle en neemt het
   volgende kwadraat onder de loep. Alleen als elk cijfer uniek is, wordt het
   op het scherm geprint.
   Het programma is niet het snelste, maar volgens mij is het algoritme wel
   een van de compactste en dat is ook wat waard...
								    Erik  */
}