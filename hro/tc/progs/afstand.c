#include <stdio.h>
#include <math.h>
#include <conio.h>
main ()
{
/* Afstand.C is geschreven door Erik van Zijst op 10-9-96.
   StudieNummer: 0515287 */

	float x1;
	float y1;
	float x2;
	float y2;
	float afstand;

clrscr();

printf ("\nVoer 2 co”rdinaat-paren in, waartussen de afstand berekend zal worden. \n");

printf ( " \nX-co”rdinaat 1e punt, x1: " );
scanf ("%f" , &x1);
printf ( "Y-co”rdinaat 1e punt, y1: " );
scanf ("%f" , &y1);
printf ( "X-co”rdinaat 2e punt, x2: " );
scanf ( "%f" , &x2);
printf ( "Y-co”rdinaat 2e punt, y2: " );
scanf ("%f" , &y2);

printf ( "\n" );
printf ( "\nHet eerste co”rdinaat is: (%.2f, %.2f) " , x1, y1 );
printf ( "\nHet tweede co”rdinaat is: (%.2f, %.2f) \n" , x2, y2 );

afstand = (sqrt( pow((x2 - x1),2) + pow((y2 - y1),2) ));

printf ("De afstand tussen beide co”rdinaten bedraagt: %.2f \n" , afstand );

getch();
}