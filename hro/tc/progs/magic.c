#include <stdio.h>
#include <conio.h>

// Magic square. Geschreven door ERIK VAN ZIJST 30/04/97 Borland C++ 3.1
void printsquare(int square[15][15], int size);
void squaretest(void);
void createsquare(void);

void main ()
{	int choice = 0;
printf("\n\nMagic Square, by ERIK VAN ZIJST.");
printf("\n\nWhat do you want to do today?");
printf("\n  1. Test a square.\n  2. Create a Magic Square.");
while ((choice != 1)&&(choice != 2))
{	printf("\nPress 1 or 2: ");
	scanf ("%d", &choice);
}

if (choice == 1) squaretest();
else createsquare();
printf("\n\nThanx for using.");
return;		// Program terminates.
}


void squaretest(void)
{	int size = 0, square[15][15], nX, mX, sum = 0, nomagic=0, test=0;

for (nX=0; nX<15; ++nX)		// ff leegmaken
{ for (mX=0; mX<15; ++mX)	square[nX][mX] = 0;
}

printf ("\n\nMagic Square testing facility.\n");
while ((size > 15)||(size < 2))
{	printf ("\nEnter the size of the square to test (2-15): ");
	scanf ("%d", &size);
}
printf ("\n  %d, Affirmative! Program continues...\n", size);

printf ("\n\nNow enter the values for the coordinates:\n");
for (nX=0; nX<size; ++nX)
{	for (mX=0; mX<size; ++mX)
	{	printf (" * Coordinate (%d,%d): ", nX+1, mX+1);
		scanf ("%d", &square[mX][nX]);
	}
}

printsquare(square, size);	// ff printen.

// Ga nu ff testen of het een magic square is...
for (nX=0; nX<size; ++nX)
	sum = sum + square[nX][0];		//Dit is de magische waarde. (bovenste rij)

// Ga nou de som van elke RIJ uitrekenen.
for (mX=0; mX<size; ++mX)			//Steeds een rijtje lager...
{	for (nX=0; nX<size; ++nX)		//...en een volgend vakje erbij tellen
		{	test = test + (square[nX][mX]);
		}
		if (test != sum) {nomagic=1; break;}
		test = 0;
}
// Als nomagic nog steeds 0 is, is het nog steeds een magic square.
// Nu de kolommen testen.
for (nX=0; nX<size; ++nX)
{	for (mX=0; mX<size; ++mX)
	{	test = test + square[nX][mX];
	}
	if (test != sum) {nomagic=1; break;}
	test = 0;
}
// Nou nog ff de 2 diagonalen testen.
for (nX=0; nX<size; ++nX)
test = test + square[nX][nX];
if (test != sum) nomagic = 1;


// Zo! Testing done. Als nomagic 0 is, is het een magic square.

if (nomagic == 0)
printf("\nThis is a Magic Square.\nThe Magic Value is %d.", sum);
else
printf("\nThis is NOT a Magic Square.");

getch();
return;
}

void printsquare(int square[15][15], int size)
{	int nX, mX;
// Ga nu ff het squaretje printen.
printf ("\nYour square:\n");
for (nX=0; nX<size; ++nX)
{	for (mX=0; mX<size; ++mX)
	{	printf ("%4d", square[mX][nX]);
	}
	printf ("\n");
}
return;
}

void createsquare(void)
{	int size = 0, square[15][15], nX, mX, amount, x, y, stop=0;

for (nX=0; nX<15; ++nX)		// ff leegmaken
{ for (mX=0; mX<15; ++mX)	square[nX][mX] = 0;
}

printf("\nMagic Square Creation Lab.\n");
printf("\n* You can only create squares with odd lengths!");
while ((size < 3)||(size > 15)||((size % 2) == 0))
{	printf("\nEnter the size of the square you want me to create (3-15): ");
	scanf("%d", &size);
}

printf("\nOK, creating a %dx%d Magic Square...\n", size, size);
amount = (size * size);
x = (size / 2);
nX = 1;
y = 0;
//square[x][y] = 1;

while (nX <= amount)
{	if ((y >= 0)&&(x < size)&&(square[x][y] == 0))
		{ square[x][y] = nX++; ++x; --y; }
	else
	{	if ((y < 0)&&(x < size))
			{ y = size-1; stop = 1;}
		if ((x >= size)&&(y >= 0)&&(stop==0))
			{ x = 0; stop = 1;}
		if ((y < 0)&&(x >= size)&&(stop==0))
			{ --x; y=y+2; stop = 1;}
		if ((square[x][y] != 0)&&(stop==0))
			{ y=y+2; --x; }
	} stop=0;
}

printsquare(square, size);
mX=0;
for (nX=0; nX<size; ++nX)
	mX = mX + square[nX][0];		//Dit is de magische waarde. (bovenste rij)
printf("The magic value is: %d", mX);

getch();
return;
}