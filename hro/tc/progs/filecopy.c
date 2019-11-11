#include <stdio.h>
#define MAXLINE 81

/* Kopieer programma. Gemaakt door ERIK VAN ZIJST 0515287 op 30/01/97
   Leest een gegeven file in en kopieert de inhoud naar een gegeven file.
*/

void main ()
{
	FILE *leesfile;
	FILE *schrijffile;
	char regel[MAXLINE], readfile[100], copyfile[100], c;

leesfile = NULL; schrijffile = NULL;
while (leesfile == NULL)
{	printf ("\n\nType a file to load: ");
	scanf ("%s",readfile);
	leesfile = fopen(readfile, "r");
	if (leesfile != NULL) break;
	printf ("\nERROR File not found!\a");
}

while (schrijffile == NULL)
{	printf ("Give a destination-file to copy to: ");
	scanf ("%s",copyfile);
	schrijffile = fopen(copyfile, "r");
	if (schrijffile == NULL)
	{	fclose (schrijffile);
		schrijffile = fopen (copyfile, "a");
		break; /* file didn't exists.. now created. */
	}
	else
	{	printf ("\nWARNING Output-file already exists! Overwrite? (Y/N): \a");
		if (((c = getchar()) == 'Y')||((c = getchar()) == 'y'))
		{	fclose (schrijffile);
			schrijffile = fopen (copyfile, "w");
			printf ("Original file destroyed...\n");
			break;
		}
		else
		{	fclose (schrijffile);
			schrijffile = NULL;
		}
	}
}


printf ("  Copying, please wait...\n");
while (fgets(regel, MAXLINE, leesfile)!=NULL)
	{       fputs (regel, schrijffile);
	}
fclose(leesfile);
fclose(schrijffile);

printf ("\nPress any key to continue...");
getch (); return 0;
}