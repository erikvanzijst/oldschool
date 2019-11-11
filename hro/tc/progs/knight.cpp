#include <common.h>
#include <conio.h>

	// Knight Move. by ERIK VAN ZIJST. 02/05/97

int board[8][8];		// schaakbord wordt Global gedefinieerd
void printboard(void);	// print het schaakbord
int nextstep(int x, int y, int count);

void main(void)
{	int x, y, nX, mX;

printf("\n\n	Knight Move.  by Erik van Zijst.\n\n");
printf("Calculates the route of a knight over the checkerboard, when visiting each\n");
printf("field exactly once.\n");
printf("\nSpecify the field to start the knight's travel (exmpl: 2,6): ");
scanf("%d,%d", &x, &y);
printf("Now get a cup of coffee. REAL HEAVY calculating is done now...\n");

for (nX=0; nX<8; ++nX)		// Schaakbord leegmaken
{	for (mX=0; mX<8; ++mX)
	board[mX][nX] = 0;
}

if (nextstep(x,y,0))
{	printboard(); printf("\nCompleted succesfully!"); }
else printf("\nNo path found: impossible to solve the problem.");
getch();
return;
}

void printboard(void)
{	int nX, mX;
for (nX=0; nX<8; ++nX)
{	for (mX=0; mX<8; ++mX)
	{	printf("%3d", board[mX][nX]); }
	printf("\n");
}
return;
}

int nextstep(int x, int y, int count)
{
if (count == 64) return 1;		// bord is vol! Gelukt!
if ((x<0)||(y<0)||(x>7)||(y>7)) return 0;
if (board[x][y] != 0) return 0;
board[x][y] = ++count;
if (nextstep(x+1, y-2, count)) return 1;
if (nextstep(x+2, y-1, count)) return 1;
if (nextstep(x+2, y+1, count)) return 1;
if (nextstep(x+1, y+2, count)) return 1;
if (nextstep(x-1, y+2, count)) return 1;
if (nextstep(x-2, y+1, count)) return 1;
if (nextstep(x-2, y-1, count)) return 1;
if (nextstep(x-1, y-2, count)) return 1;
board[x][y] = 0;		// doodlopend, backtracking activeert
return 0;
}