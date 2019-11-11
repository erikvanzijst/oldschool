#include <stdio.h>
#include <conio.h>
main ()
{
/* TENT2.C is geschreven door Erik van Zijst. Studie#: 0515287.
   Datum laatste wijziging 18-10-96.
   Dit programma leest maximaal 10 waarden van het toetsenbord in en die
   waarden moeten kleiner of gelijk zijn aan 10 en groter of gelijk -10 zijn.
   Vervolgens wordt een histogram van deze waarden getekend.
   Als je je invoer eerder wilt be‰indigen, voer je een waarde buiten de
   grenzen in. */

	int y, x, i, a, totaal=0, waarde[11], aantal, d, pixel[80][25];
	int Xrichting, Yrichting;

clrscr ();

while (totaal < 10) {					/* Lees de invoer */
	printf ("  Voer een waarde in (-10..10): ");
	scanf ("%d", &a);
	if ((a >= -10)&&(a <= 10)) { ++totaal;
		waarde[totaal] = a; }
	else break; }

printf ("De ingevoerde waarden zijn: ");
for (i=1; i<=(totaal); ++i) { printf ("%4d", waarde[i]); }
printf ("\n\nDruk op een toets om het histogram te tekenen. . .");
getch ();

for (Xrichting=1; Xrichting<=80; ++Xrichting)
{
	for (Yrichting=1; Yrichting<=25; ++Yrichting)
	{
		pixel[Xrichting][Yrichting] = 0;
	}
}

clrscr ();
for (y=3; y<=23; ++y)
	{ pixel[1][y] = 1; }     /* Teken Y-as */
for (x=2; x<=46; ++x)
	{ pixel[x][13] = 1; }    /* Teken X-as */
/* gotoxy(11, 48);	printf (" X-as"); */

for (aantal=1; aantal<=totaal; ++aantal)
if (waarde[aantal] >= 0)
{
	for (d=0; d<=waarde[aantal]; ++d)	/* rechtse vert. balk */
	{	pixel[aantal * 4 + 1][13 - d] = 1; }
	for (d=0; d<=waarde[aantal]; ++d)	/* linkse vert. balk */
	{	pixel[(aantal * 4 + 1) - 4][13 -d] = 1; }
	for (d=0; d<=4; ++d)			/* bovenste plaatje */
	{	pixel[(aantal * 4 + 1) - d][13 - waarde[aantal]]; printf ("+"); }
	gotoxy((aantal * 4 - 1), (13 - waarde[aantal] - 1)); printf ("%d", waarde[aantal]);
}
else
{
	for (d=0; d>=waarde[aantal]; --d)       /* rechtse vert. balk */
	{	gotoxy((aantal * 4 + 1), (13 - d)); printf ("+"); }
	for (d=0; d>=waarde[aantal]; --d)	/* linkse vert. balk */
	{	gotoxy(((aantal * 4 + 1) - 4), (13 - d)); printf ("+"); }
	for (d=0; d<=4; ++d)			/* hor. streepje */
	{	gotoxy(((aantal * 4 + 1) - d), (13 - waarde[aantal])); printf ("+"); }
	gotoxy((aantal * 4 - 2), (13 - waarde[aantal] + 1)); printf ("%d", waarde[aantal]);
}

gotoxy(50, 1); printf ("Ingevoerde waarden:");
for (i=1; i<=(totaal); ++i) { gotoxy(56, (i + 2)); printf ("%3d ", waarde[i]); }
gotoxy(1, 25);
getch ();
}
