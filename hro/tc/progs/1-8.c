#include <stdio.h>
main ()
{

	int spaties=0, tabs=0, newlines=0, c;

while ((c = getchar()) != EOF) {
	if (c == ' ') ++spaties;
	if (c == '\t') ++tabs;
	if (c == '\n') ++newlines; }
printf ("\nspaties: %3d, tabs: %3d, newlines: %3d.", spaties, tabs, newlines);
getch ();
}


