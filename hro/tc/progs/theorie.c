#include <stdio.h>
main ()
{
	int getal_a = -27;
	int getal_b = 3;
	int getal_c = 4;
	int getal_d = 2;
	int getal_f = 9;
	int som1, som2, som3;

som1 = ( getal_a * getal_b );
som2 = ( som1 + getal_c );
som3 = ( getal_b * ( getal_d - getal_f ));

printf (" \n " );
printf ("THEORIE (TM) is copyright (C) 1996 by MacroBugs, All Rights Reserved.\n " );
printf (" \n " );
printf ("Oplossing van %d maal %d: %d \n " , getal_a, getal_b, som1 );
printf ("Oplossing van %d maal %d + %d: %d \n " , getal_a, getal_b, getal_c, som2 );
printf ("Oplossing van %d maal (%d min %d): %d \n " , getal_b, getal_d, getal_f, som3 );
}

