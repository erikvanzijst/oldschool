#include <stdio.h>
#include <string.h>
#define MAXLEN 80
#define BOOL int
#define TRUE 1
#define FALSE 0
#define NULL '\0'

void ClearStr (char s[]);
void PrintStr (const char s[]);		/*Onveranderbaar array*/
BOOL IsEmptyStr (const char s[]);
void PrintBool (BOOL b);
void FillStr (char s[], char c, int len);
void AlfabetStr (char s[]);
BOOL IsEqualStr (const char s[], const char t[]);

int main ()
{
	char str[MAXLEN], line[MAXLEN];
	ClearStr ( str );
	PrintStr ( str );
	PrintBool ( IsEmptyStr (str) );
	FillStr ( str, 'X', 20 );
	PrintStr ( str );
	PrintBool ( IsEmptyStr (str) );
	strcpy ( str, "Dit is goed" );
	PrintStr ( str );
	AlfabetStr ( str );
	PrintStr ( str );
	strcpy ( line, str );
	PrintStr ( line );
	PrintBool ( IsEqualStr ( line, str ) );

}

void ClearStr (char s[])
{
	s[0]=NULL;
}
void PrintStr (const char s[])
{
	printf ("\n\"%s\"", s);
}
BOOL IsEmptyStr (const char s[])
{
if (s[0] == NULL) return TRUE;
else return FALSE;
/*return (s[] == NULL);*/
}
void PrintBool (BOOL b)
{
if (b)
	printf("\nTRUE ");
else
	printf("\nFALSE ");
}
void FillStr (char s[], char c, int len)
{
	int n;
	for (n=0; n<(len); ++n)	s[n] = c;
	s[n] = NULL;
}
void AlfabetStr (char s[])
{
	strcpy ( s, "ABCDEFGHIJKLMNOPQRTSUVWXYZ");

/* s[ix++] = letter;   ix pas verhogen NA de bewerkingen*/
}
BOOL IsEqualStr (const s[], const char t[])
{
	return (strcmp (s, t) == 0);
}