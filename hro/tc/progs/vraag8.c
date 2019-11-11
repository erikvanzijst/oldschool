#include <stdio.h>
main ()

/* VRAAG8.C   Build 1   13/9/96   Martijn Klootwijk	0513216 */

{
float getal1, getal2, oplossing;

printf ("VRAAG8.C   Build 1   13/9/96\n");
printf ("Copyright (C) Martijn Klootwijk 1996.\n\n");
printf ("Geef getal 1: ");
scanf ("%f", &getal1);
printf ("\nGeef getal 2:");
scanf ("%f", &getal2);

oplossing = (getal1 + getal2) / 2;

printf ("(%.2f + %.2f) / 2 = %.2f", getal1, getal2, oplossing);
}