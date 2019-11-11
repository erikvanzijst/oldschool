#include <stdio.h>
unsigned TestBit(unsigned x, int p);
unsigned SetBit(unsigned x, int p);
unsigned ResetBit(unsigned x, int p);
unsigned FlipBit(unsigned x, int p);
unsigned CrazyBit(unsigned x);

void main ()
{
	int waarde, bit, waarde2, bit2, waarde3, bit3, waarde4, bit4;

printf ("\n\nGeef een waarde: ");
scanf ("%d", &waarde);
printf ("Welke bit wil je testen? ");
scanf ("%d", &bit);
printf ("%d", TestBit(waarde, bit));

printf ("\n\nGeef waarde: ");
scanf ("%d", &waarde2);
printf ("Geef een bit om 1 van te maken: ");
scanf ("%d", &bit2);
printf ("%d", SetBit(waarde2, bit2));

printf ("\n\nGeef waarde voor Reset: ");
scanf ("%d", &waarde3);
printf ("Geef bit om te Resetten: ");
scanf ("%d", &bit3);
printf ("%d", ResetBit(waarde3, bit3));

printf ("Geef flipbit waarde: ");
scanf ("%d", &waarde4);
printf ("\n\nGeef bit om te flippen: ");
scanf ("%d", &bit4);
printf ("%d", FlipBit(waarde4, bit4));

getch ();
}

unsigned TestBit(unsigned x, int p)
{
	unsigned y;
	y = (x >> p) & 01;
	return y;
}

unsigned SetBit(unsigned x, int p)
{
	unsigned y;
	y = x | (01 << p);
	return y;
}

unsigned ResetBit(unsigned x, int p)
{
	unsigned y;
	y = x & ~(01 << p);
	return y;
}

unsigned FlipBit(unsigned x, int p)
{
	unsigned y;
	y = x^(01 << p);
	return y;
}

unsigned CrazyBit(unsigned x)
{
	unsigned y;

	x = x & 0xFF;	/*mask right byte*/

	if (TestBit (x,0))
		y = SetBit (x>>1, 7);
	else
		y = ResetBit(x>>1,7);

	return y;
}