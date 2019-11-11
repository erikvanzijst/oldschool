#include <stdio.h>
#include <math.h>
#include <conio.h>
main ()
{
 /* ABC.C is geschreven door Erik van Zijst op 27-9-96. Studie#: 0515287 */

	int a, b, c;
	float x1, x2, discriminant;

clrscr ();
printf ("Geef 3 gehele getallen, î Z, zij zijn de variabelen van ax^2+bx+c=0\n");
printf ("De nulpunten worden alleen gegeven, als het re‰le getallen betreft.\n");
printf ("   a = "); scanf ("%d", &a);
printf ("   b = "); scanf ("%d", &b);
printf ("   c = "); scanf ("%d", &c);
printf ("\nDe vergelijking luidt: %dx^2 + %dx + %d \n", a, b, c);


discriminant = (pow((b),2) - (4*a*c));

if (discriminant >= 0) {

	x1 = ( ((-1 * b) + sqrt ( pow((b),2) - (4 * a * c) ) ) / (2 * a) );
	x2 = ( ((-1 * b) - sqrt ( pow((b),2) - (4 * a * c) ) ) / (2 * a) );

	printf ("\n   x1 is: %f \n   x2 is: %f", x1, x2);
}
else
	printf ("\nDeze vergelijking is niet oplosbaar! \a \n");


getch ();
}
