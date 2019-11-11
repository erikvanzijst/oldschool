#include <stdio.h>
#define MAX 10000  /*5000*/
#define GEKGETAL 1
#define WEG 0
main ()
{
	int getallen[MAX], nX, nTel, som=0, vorig=0;
	int eerste, tweede, eerstevantweede, tweedevantweede, achterstevoren;

clrscr ();
for (nX=0; nX<MAX; ++nX)
	getallen[nX] = GEKGETAL;

for (nX=0; nX<2; ++nX)
	getallen[nX] = WEG;

for (nX=4; nX<MAX; (nX=nX+2))
	getallen[nX] = WEG;

for (nX=6; nX<MAX; ++nX)
	if ((nX % 3) == 0)
		getallen[nX] = WEG;

for (nX=14; nX<MAX; ++nX)
	if ((nX % 7) == 0)
		getallen[nX] = WEG;

printf ("\nDe eerste 10 getallen zijn: ");
for (nX=0; nX<10; ++nX)
	printf ("%d ", getallen[nX]);

printf ("\n\nDe eerste 10 GEKKENGETALLEN zijn: \n");
nTel = 0;
nX = 0;
while (nTel < 11) {
	if (getallen[nX] == GEKGETAL)	++nTel;
		printf ("%d ", getallen[nX]);
		if (getallen[nX] == GEKGETAL) printf ("(%d) ", nX);
	++nX; }

for (nX=3201; nX<MAX; ++nX)
	if (getallen[nX] == GEKGETAL)
	{
		printf ("\n\n%d", nX);
		break;
	}

for (nX=3199; nX>0; --nX)
	if (getallen[nX] == GEKGETAL)
	{
		printf ("\n\nHet eertse getal VOOR 3200 is: %d ", nX);
		break;
	}

for (nX=10; nX<100; ++nX)
	if (getallen[nX] == GEKGETAL)
		(som = som + nX);
printf ("\n\nDe som is: %d ", som);

nX = 0;
nTel = 0;
while (nX < MAX) {
	if (getallen[nX] == GEKGETAL)
		++nTel;
	if (nTel == 50) break;
	++nX;
	}
printf ("\n\nHet 50ste GEKKENGETAL is: %d", nX);

som = 0;
nX = 0;
while (nX < MAX) {
	if (getallen[nX] == GEKGETAL)
		++nTel;
	if (nX == 3487) break;
	++nX;
	}
printf ("\n\n%d ", nTel);

nX = 0;
while (nX < MAX) {
	  if (getallen[nX] == GEKGETAL) {
	  if (nX + vorig > 400) break;
	  vorig = nX;}
	  ++nX; }

printf ("\n\n%d + %d = %d ", vorig, nX, (vorig+nX));

/*Huiswerk:
-Verander MAX in 10000
-Hoeveel gekkengetallen zijn er kleiner dan 10000?
-Welke gekkengetallen onder de 10000 zijn gelijk aan hun palindroom?
 zoals 9559
-Wat is de langste serie aaneengesloten gekkengetallen met verschil 2?
 dus x, x+2, x+4, x+6 etc.
-Verzin zelf zo'n vraag en programmeer het antwoord! */

nTel = 0;
for (nX=0; nX<=MAX; ++nX)
	if (getallen[nX] == GEKGETAL)	++nTel;
printf ("\n\nOnder de 10000 zijn er %d gekkengetallen.\n\n", nTel);

printf ("Deze gekkegetallen vormen een palindroom:\n");
for (nX=0; nX<=100; ++nX)
	if (getallen[nX] == GEKGETAL)
		{
		eerste = (nX / 10);
		tweede = (nX % 10);
		if (eerste == tweede) printf ("%d\t", nX);
		}

for (nX=0; nX<=MAX; ++nX)
	if (getallen[nX] == GEKGETAL)
		{
		eerste = (nX / 100);
		tweede = (nX % 100);
		eerstevantweede = (tweede / 10);
		tweedevantweede = (tweede % 10);
		achterstevoren = ((tweedevantweede * 10) + eerstevantweede);
		if (achterstevoren == eerste) { printf ("%d\t", nX); }
		}

for (nX=100; nX<=999; ++nX)
	if (getallen[nX] == GEKGETAL)
		{
		eerste = (nX % 10);
		tweede = ((nX / 100) % 10);
		if (eerste == tweede) printf ("%d\t", nX);
		}

/*
vorig = 0;
for (nX=0; nX<=MAX; ++nX)
	if (getallen[nX] == GEKGETAL)
		{
		if ((nX - vorig) == 2)
	*/


getch ();
return 0;
}