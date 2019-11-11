#include <stdio.h>
main ()
{
/* Programma "kwartje", gemaakt op 9-9-96 door Erik van Zijst */

	int INPUT;
	int KWARTJES, DUBBELTJES, STUIVERS;

printf (" \nVoer een willekeurig bedrag in centen in: ");
scanf ( "%d" , &INPUT);

KWARTJES = INPUT / 25;
INPUT = INPUT % 25;

DUBBELTJES = INPUT / 10;
INPUT = INPUT % 10;

STUIVERS = INPUT / 5;

printf ("Kwartjes: %d \nDubbeltjes: %d \nStuivers: %d \n" , KWARTJES, DUBBELTJES, STUIVERS);
}