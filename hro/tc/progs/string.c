#include <stdio.h>
#include <string.h>
main ()
{
	char voornaam [20];
	char achternaam [30];
	char naam [50];

clrscr ();
strcpy ( voornaam , "Erik");
strcpy (achternaam, "van Zijst");
printf ("\n%s %s\n", voornaam, achternaam);
strcpy (naam, "");
strcat (naam, voornaam);
strcat (naam, " ");
strcat (naam, achternaam);
printf ("\n%s\n", naam);
printf ("Lengte voornaam: %d\n", strlen(voornaam));
printf ("lengte achternaam: %d\n", strlen(achternaam));
printf ("lengte hele naam: %d\n", strlen(naam));
getch ();
}

