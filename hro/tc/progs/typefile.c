#include <stdio.h>
#define MAXLINE 80

/* Programma dat de inhoud van een file op het scherm zet.
   Gemaakt door ERIK VAN ZIJST 0515287 @ 30/01/97 */

main ()
{
	FILE *file;
	int nX;
	char regel[MAXLINE], naam[50];

printf ("\n\nGeef een filenaam om te openen: ");
scanf ("%s",naam);

file = fopen(naam, "r");
if (file == NULL)
	printf ("File not found.");
else
{	while (fgets(regel, MAXLINE, file)!=NULL)
	{	printf("%s", regel);
	}
fclose(file);
}
getch (); return 0;
}