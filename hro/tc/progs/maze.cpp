#include <common.h>
#include <conio.h>
#include <time.h>
#define WALL 'Û'
#define PATH '°'
#define USED '±'

char board[20][20];	// doolhof veld is Global voor gemakkelijke bewerking
void makemaze(int boardsize);   // user tekent het doolhof
void printmaze(int boardsize);	// om het doolhof steeds te printen
int nextstep(int x, int y, int boardsize);			// recursieve functie
void dlay( clock_t time );	// Functie voor wachten van x clocktikken
int startx = 1;			// co”rdinaten van begin...
int starty = 1;
int exitx = 1;			// ...en eindpunt
int exity = 1;

void main (void)
{	int boardsize;

clrscr();
printf("\n                  Maze Runner.  by Erik van Zijst.\n\n");
printf("Searches a way through a user-specified maze via recursive backtracking.");
while ((boardsize < 4)||(boardsize > 20))
{	printf("\nHow big do you want the (square) maze to be? (4-20): ");
	scanf("%d", &boardsize);
	if ((boardsize < 4)||(boardsize > 20))
		printf("ERROR: value out of range!\n");
}
makemaze(boardsize);
printf("Sit back and enjoy!");
if (nextstep(startx,starty,boardsize))
	printf("\nCompleted succesfully!\n\nAny key to party...");
else
	printf("\nNo valid route found: cannot be solved without a hammer.\n\nPress a key to quit...");
return;
}

int nextstep(int x, int y, int boardsize)
{
if (board[x][y] == 'F')												// eindpunt bereikt!
{	board[x][y] = USED;
	gotoxy(1,1); printmaze(boardsize);
	return 1;
}
if ((board[x][y] != PATH)&&(board[x][y] != 'S')) return 0;	// we zitten in een muur
board[x][y] = USED;
gotoxy(1,1); printmaze(boardsize);
dlay(5);
if (nextstep(x, y-1, boardsize)) return 1;							// stap naar boven,
if (nextstep(x+1, y, boardsize)) return 1;							// naar rechts,
if (nextstep(x, y+1, boardsize)) return 1;							// beneden,
if (nextstep(x-1, y, boardsize)) return 1;							// of naar links.
board[x][y] = PATH;		// doodlopend, backtracking activeert
gotoxy(1,1); printmaze(boardsize);
return 0;
}


void makemaze(int boardsize)
{	int nX, mX, UserSaysYes=1, x,y;

for(mX=0; mX<boardsize; ++mX)		// ff het veld leegmaken
{	for(nX=0; nX<boardsize; ++nX)
		board[nX][mX] = PATH;
}
clrscr();
for (nX=0; nX<boardsize; ++nX)	// buitenmuren worden neergezet
{	board[nX][0] = WALL;
	board[nX][boardsize-1] = WALL;
	board[0][nX] = WALL;
	board[boardsize-1][nX] = WALL;
}
gotoxy(1,1);
printmaze(boardsize);
while (board[startx][starty] != WALL)
{	printf("\nWhere do you want the entrance? (coordinate, exmpl: 0,6): ");
	scanf("%d,%d", &startx, &starty);
	if (board[startx][starty] != WALL) printf("ERROR: coordinate not on the edge!");
}
board[startx][starty] = 'S';
clrscr();
gotoxy(1,1);
printmaze(boardsize);
while (board[exitx][exity] != WALL)
{	printf("\nWhere do you want the exit? (coordinate, exmpl: 0,6): ");
	scanf("%d,%d", &exitx, &exity);
	if (board[exitx][exity] != WALL) printf("ERROR: coordinate not on the edge!");
}
board[exitx][exity] = 'F';
clrscr();
gotoxy(1,1);
printmaze(boardsize);

printf("\nNow define the walls in the maze. Enter a coordinate to build a wall.\n");
printf("Enter 0,0 to stop.\n");
while (UserSaysYes)
{	printf("Place a wall in point (0,0 when done): ");
	scanf("%d,%d", &x, &y);
	if ((x==0)&&(y==0)) UserSaysYes=0;
	else
	{ if((x>0)&&(y>0)&&(x<boardsize-1)&&(y<boardsize-1))
		{	board[x][y] = WALL;
			clrscr(); gotoxy(1,1); printmaze(boardsize);
		}
		else printf("ERROR: coordinate out of range!\n");
	}
}

return;
}

void printmaze(int boardsize)
{	int nX, mX, count=0;

printf("  ");
for(nX=0; nX<boardsize; ++nX)
{	printf("%2d", count++);
	if (count==10) count=0;
} printf("\n");
count=0;
for(mX=0; mX<boardsize; ++mX)
{	printf("%2d", count++);
	if (count==10) count=0;
	for(nX=0; nX<boardsize; ++nX)
	{	printf("%c", board[nX][mX]); printf("%c", board[nX][mX]);}
	printf("\n");
}
return;
}

void dlay ( clock_t time )
{	clock_t start = clock();
	do
	{;}
	while (clock()<start+time);
}