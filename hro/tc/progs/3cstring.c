#include <stdio.h>
#include <string.h>
#define TRUE 1
#define FALSE 0

char MaxChar (const char s[]);
int SkipChar (const char s[], char c, char t[]);

void main ()
{
/* ERIK VAN ZIJST - HI1C - 0515287 - 09/01/97
   Dit is opave 3C van de praktikum opgaven voor het tentamen C programmeren
   tweede kwartaal.
*/

	char buffer[100], teken, rslt[100], c;

clrscr();
printf ("Erik van Zijst - HI1C - 0515287\n");
printf ("Praktikum tentamen C programmeren tweede kwartaal 97.\n");
printf ("Opgave 3C - Over strings.\n\n");
printf("Typ een regel (string): ");
gets(buffer);
printf("\n\nGeef een karakter op om te kijken of het voorkomt, dit karakter zal dan\nworden verwijderd. ");
printf("Karakter: ");
c = getche() ;


teken = MaxChar(buffer);
if (SkipChar(buffer, c, rslt)) printf("\n\nJa, het karakter %c komt inderdaad voor in de ingevoerde regel.", c);

printf("\n\nHet hoogste ASCII-teken in de string '%s' is '%c' ", buffer, teken);

getch();
}

char MaxChar (const char s[])
{
	int nX;
	int i = 0;

	for (nX=0 ; nX < (strlen(s)); nX++)
	{
		if (s[nX] > i) i = s[nX];
	}

	return i;

}

int SkipChar (const char s[], char c, char t[])
{
	int i, j;
	int search = 0;

	if (strchr(s, c) != NULL) search = 1;

	for (i = j = 0; s[i] != '\0'; i++)
	{
		if (s[i] != c)
			t[j++] = s[i];
	}
	t[j] = '\0';

	if (search) printf("\n\nHet resultaat, zonder '%c' is '%s'." , c, t);
		 else printf("\n\nHet gezochte teken bevindt zich niet in de ingevoerde string...");

	return (search);
}
