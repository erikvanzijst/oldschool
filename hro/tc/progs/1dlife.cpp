#include <stdio.h>
#include <common.h>
#include <conio.h>
#define MAX 30	// Definieert de lengte v/h simulatieveld. Aanpasbaar.

void printfield(char cell[MAX]);
int neighborcount(char cell[MAX], int cellnr);

					// One Dimensional Life. ERIK VAN ZIJST - 01/05/97

void main (void)
{	int number=1, nX, count=1;
	char cell[MAX], nexgen[MAX], UserConfirm='Y';

for(nX=0; nX<MAX; ++nX)		// ff leegmaken (alle cellen dood)
{	cell[nX] = '.'; nexgen[nX] = '.'; }

clrscr();
printf("  One Dimensional Life simulation, by ERIK VAN ZIJST");
printf("\n\nEnter the numbers of the cells to make alive.\n\n");
printfield(cell);
printf("  <- simulation field\n  ");
for (nX=0; nX<MAX; ++nX)			// ff nummertjes eronder zetten.
	printf("%d", count++ % 10);

while (number != 0)
{ printf("                                   "); gotoxy(1,8);
	printf("Which cell should become alive (0 when done)? ");
	scanf("%d", &number);
	if ((number > 0)&&(number < MAX+1))
	{	cell[number-1] = '*';
		gotoxy(1,5);
		printfield(cell);
	}
	gotoxy(1,8);
}
gotoxy(1,10);

for(;;)
{	for (nX=0; nX<MAX; ++nX)
	{	if (cell[nX] == '*')			// als ie levend is
		{	count = neighborcount(cell, nX);	// Hoeveel buren heeft de cel?
			if ((count == 0)||(count == 1)||(count == 3)) nexgen[nX] = '.';
			else nexgen[nX] = '*';
		}
		if (cell[nX] == '.')			// als ie dood is
		{	count = neighborcount(cell, nX);	// Hoeveel buren heeft de dode cel?
			if ((count == 2)||(count == 3)) nexgen[nX] = '*';
		}
	}		// Nieuwe generatie staat in nexgen[]. nu terugkopieren naar cell[].

	for (nX=0; nX<MAX; ++nX) cell[nX] = nexgen[nX];
	for (nX=0; nX<MAX; ++nX) nexgen[nX] = '.';

	printfield(cell); UserConfirm = '0'; count=0;
	for(nX=0; nX<MAX; ++nX)
	{	if (cell[nX] == '.') ++count;
	}
	if (count==MAX) { printf("\nAll died. Program terminates."); return; }


	while ((UserConfirm != 'y')&&(UserConfirm != 'Y'))
	{ printf(" View next generation? ");
		UserConfirm = getche();
		printf("\n");
		if ((UserConfirm == 'n')||(UserConfirm == 'N')) return;	// Program Terminates
	}
}
}

void printfield(char cell[MAX])		// Print het speelveld.
{	int nX;
printf("  ");
for (nX=0; nX<MAX; ++nX)
	printf("%c", cell[nX]);

}

int neighborcount(char cell[MAX], int cellnr)
{	int count=0;
if ((cell[cellnr-2] == '*')&&(cellnr-2 >= 0)) ++count;
if ((cell[cellnr-1] == '*')&&(cellnr-1 >= 0)) ++count;
if ((cell[cellnr+1] == '*')&&(cellnr+1 < MAX)) ++count;
if ((cell[cellnr+2] == '*')&&(cellnr+2 < MAX)) ++count;
return count;
}