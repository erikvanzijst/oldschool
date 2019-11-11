#include <stdio.h>
#include <string.h>
#define MAX 60
int Klinkers ( char s[MAX] );

main ()
{
clrscr ();
printf ("\n%d", Klinkers("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
printf ("\n%d", Klinkers("AaEeIiOoUu"));
printf ("\n%d", Klinkers(""));

}

int Klinkers ( char s[MAX] )
{
	int aantal_klinkers = 0, c=0, letter;

for (c=0; c<strlen(s); ++c)  {
	letter = s[c];
	switch (letter) {
	case 'u': case 'i': case 'o': case 'a': case 'e':
	case 'U': case 'I': case 'O': case 'A': case 'E':
		++aantal_klinkers;
		break;}
	}
return aantal_klinkers;
}