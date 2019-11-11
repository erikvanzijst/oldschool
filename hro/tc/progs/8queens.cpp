#include <common.h>
#include <conio.h>
#define BOARDSIZE 8
#define DIAGONAL (2*BOARDSIZE-1)
#define DOWNOFFSET (BOARDSIZE-1)

// Eight Queens door ERIK VAN ZIJST - 06/05/97.
// Zoekt alle mogelijkheden om 8 koninginnen onafhankelijk van elkaar op een
// schaakbord te zetten.

void WriteBoard(void);
void AddQueen(void);
int queencol[BOARDSIZE];		// collum with the queen
Boolean colfree[BOARDSIZE];
Boolean upfree[DIAGONAL];
Boolean downfree[DIAGONAL];
int queencount = -1, numsol = 0;
float counter = 0.0;
int solutions = 0;

int main(void)
{
	int i;

clrscr();
printf("                  Eight Queens. by Erik van Zijst.\n");
printf("\nCalculates all possibilities of placing 8 queens on a checkerboard, without\n");
printf("quarded queens.\n");
printf("\nAny key to start the search...");
getch();
for (i=0; i<BOARDSIZE; i++) colfree[i] = TRUE;
for (i=0; i<DIAGONAL; i++) { upfree[i] = TRUE; downfree[i] = TRUE; }
AddQueen();
printf("\n\nAll done. %.0f possibilities were tried.", counter);
printf("\n%d possibilities proved to be effective.", solutions);
return 0;
}

void AddQueen(void)
{
	int col;

++counter;
queencount++;
for (col=0; col<BOARDSIZE; col++)
	if (colfree[col] && upfree[queencount + col] && downfree[queencount - col + DOWNOFFSET])
	{ queencol[queencount] = col;
		colfree[col] = FALSE;
		upfree[queencount + col] = FALSE;
		downfree[queencount - col + DOWNOFFSET] = FALSE;
		if (queencount == BOARDSIZE - 1)
			WriteBoard();
		else
			AddQueen();		// proceed recursive
		colfree[col] = TRUE;
		upfree[queencount + col] = TRUE;
		downfree[queencount - col + DOWNOFFSET] = TRUE;
	}
queencount--;
}

void WriteBoard(void)
{
	int nX, mX;
	char board[BOARDSIZE][BOARDSIZE];

++solutions;
printf("\n\n");
for (nX=0; nX<BOARDSIZE; ++nX)
{	for (mX=0; mX<BOARDSIZE; ++mX)
	{ if (queencol[nX] == mX)
			board[nX][mX] = 'Q';
		else
			board[nX][mX] = '.';
	}
}

for (mX=0; mX<BOARDSIZE; ++mX)
{	for (nX=0; nX<BOARDSIZE; ++nX)
		printf("%2c", board[mX][nX]);
	printf("\n");
}
printf("Press a key for the next solution...");
getch();
return;
}