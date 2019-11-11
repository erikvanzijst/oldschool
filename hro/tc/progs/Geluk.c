#include <stdio.h>
void Pauze();	/* Functie om het programma te kunnen pauzeren. */
void main ()

/* ERIK VAN ZIJST - HI1C - 0515287 - 06/01/97
   Dit programma is de laatste opgave van het tentamen programmeren in C van
   het 2e kwartaal; Opgave 3D - Over Geluksgetallen.
   De  geluksgetallen worden  gegenereerd door  van een  rij oneven getallen
   steeds het  volgende te pakken  en dit als  interval te gebruiken bij het
   verwijderen van getallen uit de immer kleiner wordende rij.
   In principe is dit proces  oneindig, maar in dit  programma wordt uitege-
   gaan van een rij van 10000 oneven getallen.
*/
{
	int nX, geluk[10000], buffer[10000], Teller=1, TaboeCijfer=3;
	int stap=1, a=0, vorige=0, som, dec, cijfer, studnr, aantal;
	int aantalcijfers, eerstekeer, bereik, leeg=1;
	long Som3Cijfer=0;

clrscr ();
printf ("Erik van Zijst, HI1C, 0515287, 06/01/97\n\nOpgave 3D - Over Geluksgetallen.\n\n");
printf ("   a) De eerste 100 Geluksgetallen zijn:");
printf ("\n   Even geduld, de getallen worden gegenereerd...\n");

for (nX=0; nX<10000; ++nX) 			      /* ff resetten */
{	buffer[nX] = 0; geluk[nX] = ((nX * 2) + 1); }

	/* Nu worden de Geluksgetallen berekend en in geluk[] gezet.*/
while (TaboeCijfer < 10000) {
TaboeCijfer = geluk[stap];
Teller = 1;
a = 0;
	for (nX=0; nX<10000; ++nX)
	{       if (Teller == TaboeCijfer)
			{ geluk[nX] = 0; Teller = 1; }
		else
			++Teller;
	}	/* Het cijfer is periodiek geschrapt. */

	for (nX=0; nX<10000; ++nX) /*Alles naar buffer, zonder lege plekken*/
	{	if (geluk[nX] != 0)
		{	buffer[a] = geluk[nX]; ++a; }
	}
	for (nX=0; nX<10000; ++nX) geluk[nX] = 0; /* Resetten... */
	for (nX=0; nX<10000; ++nX) /*Overstorten en buffer legen */
	{	geluk[nX] = buffer[nX]; buffer[nX] = 0; }

++stap;

}
/* ...........De Geluksgetallen zijn nu gegenereerd!!.............. */
printf ("\n\n");

for (nX=0; nX<100; ++nX)
	{ if (geluk[nX] != 0) printf ("%8d", geluk[nX]); }
Pauze();
printf ("   b) Het 1000e Geluksgetal is %d.\n\n", geluk[999]);
Pauze();
nX = 0;
while (geluk[nX] <= 10000) ++nX;
printf ("   c) Het eerste geluksgetal groter dan 10000 is: %d.\n", geluk[nX]);
printf ("      -Dit is het %de geluksgetal.\n\n", nX);
Pauze();

for (nX=24; nX<10000; ++nX)
{ 	if ((geluk[nX] >= 100) && (geluk[nX] <= 999))
	Som3Cijfer = Som3Cijfer + geluk[nX];
}
printf ("   d) De som van alle 3-cijferige getallen bedraagt %ld.\n\n", Som3Cijfer);
Pauze ();

for (nX=0; nX<10000; ++nX)
{	if ((vorige + geluk[nX]) > 500)
		break;
	else
		vorige = geluk[nX];
}
printf ("   e) De 2 kleinste, opeenvolgende, geluksgetallen die samen groter zijn \n      dan 500, zijn: %d en %d, de som is %d.\n\n", vorige, geluk[nX], (vorige + geluk[nX]));
Pauze ();

printf ("   f) Het laatste cijfer van mijn studentnr. is 7 (0515287)\n");
printf ("      De volgende geluksgetallen kleiner dan 10000 beginnen daarmee:\n\n");
for (nX=0; nX<1119; ++nX)
{	if (geluk[nX] < 10)
	{	if ((geluk[nX] % 7) == 0) printf ("%8d", geluk[nX]); }
	if ((geluk[nX] >= 10) && (geluk[nX] < 100))
	{	if (((geluk[nX] / 10) % 7) == 0) printf ("%8d", geluk[nX]); }
	if ((geluk[nX] >= 100) && (geluk[nX] < 1000))
	{	if (((geluk[nX]/100)%7) == 0) printf ("%8d", geluk[nX]); }
	if ((geluk[nX] >= 1000) && (geluk[nX] < 10000))
	{	if (((geluk[nX]/1000)%7) == 0) printf ("%8d", geluk[nX]); }
}
Pauze ();

printf ("   g) Van de volgende geluksgetallen is de som van de cijfers gelijk aan de\n");
printf ("      som van de cijfers van mijn studentennummer.\n");
printf ("      Aangezien de som van de cijfers van mijn studentennummer hoger is (28)\n");
printf ("      dat er maximaal met 3 cijfers kan worden behaald (27), kun je hier\n");
printf ("      een ander totaal ingeven: ");
scanf ("%d", &studnr);
for (nX=0; nX<154; ++nX)
{       if ((geluk[nX] >= 100) && (geluk[nX] < 1000))
	{       dec = 1; som = 0;
		for (a=0; a<=2; ++a)
		{	cijfer = ((geluk[nX] / dec) - (10 * (geluk[nX] / (dec*10))));
			som = som + cijfer;
			dec = dec * 10;
		}
		if (som == studnr) {printf ("%8d", geluk[nX]); leeg = 0;}
	}

	if ((geluk[nX] >= 10) && (geluk[nX] < 100))
	{	dec = 1; som = 0;
		for (a=0; a<=1; ++a)
		{	cijfer = ((geluk[nX] / dec) - (10 * (geluk[nX] / (dec * 10))));
			som = som + cijfer;
			dec = dec * 10;
		}
		if (som == studnr) {printf ("%8d", geluk[nX]); leeg=0;}
	}

	if (geluk[nX] < 10)
	{	if ((geluk[nX] % studnr) == 0)
		{printf ("%8d", geluk[nX]); leeg=0;}
	}

}
if (leeg == 1)
{	printf ("\n\nGeen getallen gevonden."); }
printf ("\n\n"); Pauze ();

printf ("   h) Nu worden de getallen gegeven waarin een bepaald getal voor die\n");
printf ("      bepaalde keer voorkomt...\n");
for (a=1; a<=10; ++a)
{	aantal = 1;
	for (nX=0; nX<10000; ++nX)
	{	if (geluk[nX] < 10) aantalcijfers = 1;
		if ((geluk[nX] >= 10) && (geluk[nX] < 100)) aantalcijfers = 2;
		if ((geluk[nX] >= 100) && (geluk[nX] < 1000)) aantalcijfers = 3;
		if ((geluk[nX] >= 1000)&&(geluk[nX] < 10000)) aantalcijfers = 4;
		dec = eerstekeer = 1;
		for (Teller=0; Teller<aantalcijfers; ++Teller)
		{	cijfer = ((geluk[nX] / dec) - (10*(geluk[nX] / (dec*10))));
			dec = dec * 10;
			if ((cijfer == a) && (aantal == a))
			{       printf ("Het %de getal met een %d erin is: ", a,a);
				printf ("%d\n", geluk[nX]);
				nX=10000;
				break; }
			if ((cijfer == a)&&(eerstekeer == 1))
			{++aantal; eerstekeer = 0;}
		}
	}
}
printf ("\n"); Pauze ();

/* Zelfverzonnen opgave: geef een getal en kijk of welke geluksgetallen
   deelbaar zijn door dit getal... */

printf ("   i) Zelfverzonnen opgave.\n");
printf ("      Alle geluksgetallen tot een bepaalde waarde (max. 10000) worden\n");
printf ("      gezocht die deelbaar zijn door een opgeven getal...\n");
printf ("\n      Geef een getal om door te delen: ");
scanf ("%d", &cijfer);
do {
printf ("      Geef de waarde tot waar de getallen moeten worden gezocht\n      (max. 10000): ");
scanf ("%d", &bereik);
}
while (bereik > 10000);

nX = 0; leeg = 1;
while (geluk[nX] <= bereik)
{	if ((geluk[nX] % cijfer) == 0) {printf ("%8d", geluk[nX]); leeg=0;}
	++nX;
}
if (leeg == 1) printf ("\n\nGeen getallen gevonden.");
printf ("\n\n");

Pauze ();
return 0;
}

void Pauze()
{
	printf("Druk op een toets om verder te gaan...");
	getch ();
	printf ("\n\n");
	return 0;
}

/* AAAaaaahhh... dat was 'm! :-) */