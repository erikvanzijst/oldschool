#include <stdio.h>
main ()
{

	int c;

while ((c = getchar()) != EOF) {
	if (c == '\b') printf ("\\b");
	if (c == '\t') printf ("\\t");
	if (c == '\\') printf ("\\\\");
	if ((c != '\t') && (c != '\b') && (c != '\\')) printf ("%c", c);
	}
}