#include <stdio.h>
#include <string.h>
char MaxChar(const char s[]);
int SkipChar(const char s[], char c, char t[]);

void main ()
{
	int b;
	char string[10];

printf ("\n\nOpgave 3C\nHet hoogste ASCII-teken is: %c", MaxChar("azAxY"));

b = SkipChar("abcdef", 'c', "");
printf ("\n\nb heeft de waarde: %d", b);
getch ();
}

char MaxChar(const char s[])
{
	int max=0, nX, teken;

	for (nX=0; nX<strlen(s); ++nX)
	{
		teken = s[nX];
		if (teken > max) max = teken;
	}
	return max;
}

int SkipChar(const char s[], char c, char t[])
{
	int hit = 0, nX;
	char teken;

	for (nX=0; nX<strlen(s); ++nX)
	{
		teken = s[nX];
		if (teken == c)
			hit = 1;
/*		else
			strcat(t, teken);*/
	}
	return hit;
}