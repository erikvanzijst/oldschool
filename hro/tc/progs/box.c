#include <stdio.h>
#include <conio.h>
main ()
{
/* BOX.C is gemaakt door Erik van Zijst, op 17-9-96. Studienummer 0515287. */

	int x, y;
	int x1 = 1;
	int y1 = 1;

clrscr ();
printf ("Voer een lengte en vervolgens een hoogte in voor een te tekenen rechthoek.\n \n");
begin:
printf ("Breedte = ");
scanf ("%d", &x);
printf ("Hoogte = ");
scanf ("%d", &y);
printf ("\n \n \nDruk op een toets om de rechthoek te tekenen...");
getch ();

clrscr ();	      /*CLS opnieuw, om de rechthoek genoeg ruimte te geven*/
if ((x > 0)&&(x < 81)&&(y > 0)&&(y < 26))   /*Bepaal of ie niet te groot is*/
{
	do {			/*Teken de bovenste rij*/
		printf ("*");
		x1 = (x1 + 1);
	} while (x1 <= x);

	if (y > 2) {		/*Teken de vertikale lijnen*/
		do {
			x1 = 1;
			if (x < 80) printf ("\n"); /*Anders gaat ie vanzelf al naar de nieuwe regel*/
			printf ("*");
			do {
				if (x > 2) printf (" ");
				x1 = (x1 + 1);
			} while (x1 <= (x - 2));
			if (x > 1) printf ("*");
			y1 = y1 + 1;
		} while (y1 <= (y - 2));
	}

	if (y > 1) {		/*Teken de onderste rij*/
		x1 = 1;
		if (x < 80) printf ("\n");
		do {
			printf ("*");
			x1 = (x1 + 1);
		} while (x1 <= x);
	}

}
else
{
	printf ("Ho ho! Deze waarden liggen buiten de grenzen van het scherm!");
	printf ("\nHiertussen kan ik geen rechthoek tekenen. Voer 2 nieuwe waarden in...\n \n \n");
	goto begin;
}
getch (); 	/*Wacht met terugkeren, tot er op een toets gedrukt is*/

}