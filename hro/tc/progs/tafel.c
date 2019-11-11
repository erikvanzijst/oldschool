#include <stdio.h>
#include <conio.h>
main ()
{
/*TAFEL.C is gemaakt door Erik van Zijst op 19-9-96. Studie#: 0515287*/

	int tafel, x;

clrscr ();
begin:
printf ("Geef een waarde van 2 tot 12 op, waarvan ik de tafel zal maken:");
printf ("\nMaak de tafel van: ");
scanf ("%d", &tafel);

if ((tafel >= 2)&&(tafel <= 12))
{
	for (x=1; x<=10;++x)
	{
	printf ("\n %2d * %d = %d", x, tafel, (x*tafel));
	}
}
else
{
	printf ("\nNee, deze waarde ligt niet tussen 1 en 13.");
	printf ("\nInvoer: 2, 3,...12");
	printf ("\nProbeer opnieuw...\a \n \n");
	goto begin;
}
getch ();
}
