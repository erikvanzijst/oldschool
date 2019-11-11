#include <stdio.h>
#include <conio.h>
main ()
{
/*RIJ1.C gemaakt door Erik van Zijst op 20-9-96. Studie#: 0515287*/

	int L[100], a, c, i, h, l;

clrscr ();
printf ("Voer een rij positieve gehele getallen in, met als laatste een negatief\n");
printf ("getal, daarmee wordt de invoer afgesloten.\n \n");

i = 0;
while (scanf("%d", &a), (a >= 0)) L[++i]=a;
printf ("\nJe getallen zijn:\n");

c = 1;
for (c=1; c<=i; ++c) printf (" %d,", L[c]);
printf("\b \n");

c=1;
h=L[i];
l=L[i];
for (c=1; c<=i; ++c) {
	if (L[c]>h) h=L[c];
	if (L[c]<l) l=L[c];
	}

printf("\nJe laagste getal is %d en je hoogste is %d.", l, h);

getch ();
}