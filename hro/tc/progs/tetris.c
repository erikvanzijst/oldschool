#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <time.h>
#include <dos.h>
#include <string.h>

#define MAXX 80
#define MAXY 25

/* 					Gebruikte functies: */

int ChooseBlock(void);	/* Kiest een random blok */
void Init(void);	/* Initialiseren v/h speelveld met achtergrond */
void dlay( clock_t time );	/* Functie voor wachten voor x clocktikken */
int DrawBlock(int x, int y, int block, int rotate);		/* Different types of TertrisBlocks*/
int rmblock(int x, int y, int block, int rotate); /* Verwijdert een blok */
int checklinks(int x, int y, int block, int rotate); /*Kijk of er links ruimte is*/
int checkrechts(int x, int y, int block, int rotate); /* en rechts ruimte is.*/
int checkrotate(int x, int y, int block, int rotate); /* Kijk of een rotatie mogelijk is */
int checkonder(int x, int y, int block, int rotate); /* Blok op de bodem? */
char array[20][18];		/* Global variabele. Hierin staat het speelveld. */
int kleur[20][18];		/* Global array. Hierin staat de kleurcode van elke schermpositie. */
int cleararray(void);	/* Vult het Global array met spaties. */
int addblock2array(int x, int y, int block, int rotate);
int check4lines(void);	/* Spoort gevormde lijnen op en verwijdert ze. */
void pauze(void);

int main(void)
{
	int BlockNo, block[7], i, x, px, y, py, rotate=1, protate, nX, mX, totallines=0;
	int drop=0, nextblock, lines=0, score=0, linescore=0;
	clock_t start;
	char key;

	clrscr();
	_setcursortype(_NOCURSOR);
	randomize();				/* Randomize Timer */
	Init();  				    /* Initialize speelveld*/
	cleararray();				/* Maak het speelveld in het geheugen leeg */
	nextblock = ChooseBlock();
for(nX=0; nX<7; ++nX) block[nX] = 0;

for(;;)
{ BlockNo = nextblock;		/* Voorspel het volgende blok */
	nextblock = ChooseBlock(); score=score+5; /*gotoxy(18,6); printf("%2d", score);*/
	gotoxy(15,10); printf("        "); gotoxy(15,11); printf("        ");
	DrawBlock(-15,7,nextblock,1);		/* Print het volgende blok */
	++block[BlockNo]; drop = 0;			/* Print statistics */
	gotoxy(68, (8 + 2*BlockNo)); printf("%d", block[BlockNo]);
	x=px=8; y=py=0; rotate=protate=1;
	if (checkonder(x,y,BlockNo,rotate)) {printf("\a"); break;}
	for (;;)
	{
		if (i>0) rmblock(px,py,BlockNo,protate);	/* Verwijder... */
		DrawBlock(x,y,BlockNo,rotate);						/* ...en teken nieuw blok. */
		px=x; py=y; protate=rotate;
		start = clock();
		if (drop) (start = start-7);
		do
		{	if (kbhit() != 0)		/* Lees Keyboard buffer uit */
			{	key = getch();
				switch (key)
				{	case 0x4B: { if (!checklinks(x,y,BlockNo,rotate) )
											{ px = x; x=(x-2); break; }
											else break;
										}
					case 0x4D: { if (!checkrechts(x,y,BlockNo,rotate) )
											{ px = x; x=(x+2); break; }
											else break;
										}
					case 0x48: { if (!checkrotate(x,y,BlockNo,rotate) )
											{	protate = rotate++; if(rotate > 4) rotate=1; break; }
											else break;
										}
					case 0x50: { start = start-7; break; }
					case 'q': { textcolor(7); return 0; }	/* Quit tetris */
					case ' ': { drop = 1; start = start-7; break; }
					case 'p': { pauze(); break; }
					default: {;}
				}
				rmblock(px,py,BlockNo,protate);     /* Remove block */
				DrawBlock(x,y,BlockNo,rotate);			/* Draw new */
				px=x; py=y; protate=rotate;
			} 
		}	while (clock()<start+7-(totallines/15));	/* Pause for x clockticks */
		if ( !checkonder(x,y,BlockNo,rotate) ) py = y++;
		else break;
	}
	addblock2array(x,y,BlockNo,rotate);
	lines = check4lines();
	totallines = totallines + lines;
	gotoxy(68,6); printf("%d", totallines);
	switch (lines)
	{	case 1:	{ linescore=10; break;}
		case 2: { linescore=30; break;}
		case 3: { linescore=60; break;}
		case 4: { linescore=100; break;}
	}
	score = score + linescore; linescore=0;
	gotoxy(17,6); printf("%d", score);

}
	textcolor(7);
	gotoxy(1,25);
	getch();		//Einde - terug naar OS
  return 0;
}

int check4lines(void)
{	int nX, mX, oX, mY, by, plaats, line[5], i=0;
	int veldx = 30, veldy = 2, kleurbuffer[20][18];
	char buffer[20][18];

for(nX=0; nX<5; ++nX) line[nX] = -1;

for (nX=0; nX<18; ++nX)
{ plaats = 0;
	for (mX=0; mX<20; (mX=mX+2))
	{ if (array[mX][nX] != ' ') ++plaats;
		else break;
		if (plaats == 10) line[i++] = nX;		/* in line[] staan nu de y-waarden van de gevonden lijnen */
	}
}
if (i>0)		/* De gevormde lijn(nen) knipperen even */
{ for (oX=0; oX<3; ++oX)
	{	for (nX=0; nX<i; ++nX)
		{ gotoxy(veldx, veldy+line[nX]+1);
			printf("                    ");		/* lijnen worden weggehaald...*/
		}
		dlay(2);		/* 2 clockpulsen pauze */
		for (nX=0; nX<i; ++nX)
		{	gotoxy(veldx, veldy+line[nX]+1);
			mY = line[nX];										/* ...en weer getekend */
			for(mX=0; mX<20; ++mX) printf("%c", array[mX][mY]);
		}
		dlay(2);
	}


	for(nX=0; nX<18; ++nX)		/* reset het buffer-array */
	{	for(mX=0; mX<20; ++mX)
		{	buffer[mX][nX] = ' ';
			kleurbuffer[mX][nX] = 0;
		}
	}

	by = 17;
	for (nX=17; nX>=0; --nX)
	{	if ((line[0] == nX)||(line[1] == nX)||(line[2] == nX)||(line[3] == nX))
		;
		else
		{	for(mX=0; mX<20; ++mX){ buffer[mX][by] = array[mX][nX];
															kleurbuffer[mX][by] = kleur[mX][nX]; }
			--by;
		}
	}

	for(nX=0; nX<18; ++nX)		/* kopieer de buffer naar array */
	{	for(mX=0; mX<20; ++mX)
		{	array[mX][nX] = buffer[mX][nX];
			kleur[mX][nX] = kleurbuffer[mX][nX]; /* en de kleurbuffer naar kleur*/
		}
	}

	for(nX=0; nX<18; ++nX)		/* print de array, de lijnen zijn nu verdwenen */
	{ gotoxy(veldx, veldy+nX+1);
		for(mX=0; mX<20; ++mX)
		{	textcolor(kleur[mX][nX]);
			cprintf("%c", array[mX][nX]);
		}
	}
}

return i;
}

int cleararray(void)
{	int nX, mX;

for (nX=0; nX<20; ++nX)
{	for (mX=0; mX<18; ++mX)
	{	array[nX][mX] = ' ';
	}
}
return 0;
}

int ChooseBlock(void)		/* Choose random block (1-7)*/
{	int randomblock;
	do {
	randomblock = (rand() / 4682);
	} while ((randomblock < 0)||(randomblock >= 7));
  return randomblock;
}

void dlay ( clock_t time )
{	clock_t start = clock();
	gotoxy(80,25);
	do
	{;}
	while (clock()<start+time);
}



void Init(void)		/* Teken 1malig het speelveld */
{
	int n;
textcolor(7);
clrscr();
printf("                                     TETRIS\n");
printf("\n");
printf("      …ÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕπ                    ÃÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕª\n");
printf("      ∫       POINTS:       ∫                    ∫     STATISTICS:     ∫\n");
printf("      ∫                     ∫                    ∫                     ∫\n");
printf("      ∫         0           ∫                    ∫ lines:..........0   ∫\n");
printf("      ÃÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕπ                    ∫                     ∫\n");
printf("      ∫     NEXT BLOCK:     ∫                    ∫ €€..............0   ∫\n");
printf("      ∫                     ∫                    ∫                     ∫\n");
printf("      ∫                     ∫                    ∫ ‹‹‹‹............0   ∫\n");
printf("      ∫                     ∫                    ∫                     ∫\n");
printf("      ∫                     ∫                    ∫ ‹€ﬂ.............0   ∫\n");
printf("      ÃÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕπ                    ∫                     ∫\n");
printf("      ∫      CONTROLS:      ∫                    ∫ ﬂ€‹.............0   ∫\n");
printf("      ∫                     ∫                    ∫                     ∫\n");
printf("      ∫ Left:........'left' ∫                    ∫ ‹‹€.............0   ∫\n");
printf("      ∫ Right:......'right' ∫                    ∫                     ∫\n");
printf("      ∫ Rotate:........'up' ∫                    ∫ €‹‹.............0   ∫\n");
printf("      ∫ Push down:...'down' ∫                    ∫                     ∫\n");
printf("      ∫ Drop:.......'space' ∫                    ∫ ﬂ€ﬂ.............0   ∫\n");
printf("      ÃÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕ ÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕ ÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕπ\n");
printf("      ∫  Stephan van Beerschoten - Erik van Zijst - Martijn Klootwijk  ∫\n");
printf("      »ÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕº\n");
textcolor(4);
cprintf("                                                  Beta release! 16/03/97\n\r");
cprintf("                        Bug-reports/comment/etc to: icehawk@penta.ml.org");
textcolor(7);
}

int checkrotate(int x, int y, int block, int rotate)
{
switch (block)
{	case 0:		/* Voor het vierkant geldt: */
	{	switch (rotate)
		{	case 1: return 0;
			case 2: return 0;
			case 3: return 0;
			case 4: return 0;
		}
	}
	case 1: /* Langwerpig blok: */
	{	switch (rotate)
		{	case 1:
			{	if ((array[x+2][y+1] != ' ')||(array[x+2][y+2] != ' ')||(array[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y] != ' ')||(array[x+4][y] != ' ')||(array[x+6][y] != ' ')) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x+2][y+1] != ' ')||(array[x+2][y+2] != ' ')||(array[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x][y] != ' ')||(array[x+4][y] != ' ')||(array[x+6][y] != ' ')) return 1;
				else return 0;
			}
		}
	}
	case 2:		/* Verschoven blok */
	{	switch (rotate)
		{	case 1:
			{	if ((array[x][y] != ' ')||(array[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x+2][y] != ' ')||(array[x+4][y] != ' ')) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y] != ' ')||(array[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+2][y] != ' ')||(array[x+4][y] != ' ')) return 1;
				else return 0;
			}
		}
	}
	case 3:		/* Verschoven blok */
	{	switch (rotate)		/* Bepaal de rotatie-variabele */
		{	case 1:
			{	if ((array[x][y+1] != ' ')||(array[x][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y] != ' ')||(array[x+4][y+1] != ' ')) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y+1] != ' ')||(array[x][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x][y] != ' ')||(array[x+4][y+1] != ' ')) return 1;
				else return 0;
			}
		}
	}
	case 4:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x+2][y+1] != ' ')||(array[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x+4][y] != ' ')||(array[x][y+1] != ' ')||(array[x+4][y+1] != ' ')) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y] != ' ')||(array[x][y+2] != ' ')||(array[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+2][y] != ' ')||(array[x+4][y] != ' ')) return 1;
				else return 0;
			}
		}
	}
	case 5:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x+2][y+1] != ' ')||(array[x+2][y+2] != ' ')||(array[x][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y] != ' ')||(array[x][y+1] != ' ')||(array[x+4][y+1] != ' ')) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x+2][y] != ' ')||(array[x][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+2][y+1] != ' ')||(array[x+4][y+1] != ' ')) return 1;
				else return 0;
			}
		}
	}
	case 6:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x][y+1] != ' ')||(array[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if (array[x+4][y+1] != ' ') return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y] != ' ')||(array[x][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+2][y] != ' ')||(array[x+4][y] != ' ')) return 1;
				else return 0;
			}
		}
	}
}
return 1;
}



int checklinks(int x, int y, int block, int rotate)
{
switch (block)
{	case 0:		/* Voor het vierkant geldt: */
	{	switch (rotate)
		{	case 1:
			{	if ((array[x-1][y] != ' ')||(array[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x-1][y] != ' ')||(array[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x-1][y] != ' ')||(array[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x-1][y] != ' ')||(array[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
	case 1: /* Langwerpig blok: */
	{	switch (rotate)
		{	case 1:
			{	if ((array[x-1][y] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y] != ' ')||(array[x][y+1] != ' ')||(array[x][y+2] != ' ')||(array[x][y+3] != ' ')||(x<0)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x-1][y] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x][y] != ' ')||(array[x][y+1] != ' ')||(array[x][y+2] != ' ')||(array[x][y+3] != ' ')||(x<0)) return 1;
				else return 0;
			}
		}
	}
	case 2:		/* Verschoven blok */
	{	switch (rotate)
		{	case 1:
			{	if ((array[x][y] != ' ')||(array[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x-1][y] != ' ')||(array[x-1][y+1] != ' ')||(array[x][y+3] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y] != ' ')||(array[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x-1][y] != ' ')||(array[x-1][y+1] != ' ')||(array[x][y+3] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
	case 3:		/* Verschoven blok */
	{	switch (rotate)		/* Bepaal de rotatie-variabele */
		{	case 1:
			{	if ((array[x-1][y] != ' ')||(array[x][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y] != ' ')||(array[x-1][y+1] != ' ')||(array[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x-1][y] != ' ')||(array[x][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x][y] != ' ')||(array[x-1][y+1] != ' ')||(array[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
	case 4:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x-1][y] != ' ')||(array[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x-1][y] != ' ')||(array[x][y+1] != ' ')||(array[x][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x+3][y] != ' ')||(array[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x-1][y] != ' ')||(array[x-1][y+1] != ' ')||(array[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
	case 5:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x-1][y] != ' ')||(array[x+3][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y] != ' ')||(array[x][y+1] != ' ')||(array[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x-1][y] != ' ')||(array[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x-1][y] != ' ')||(array[x-1][y+1] != ' ')||(array[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
	case 6:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x-1][y] != ' ')||(array[x][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y] != ' ')||(array[x-1][y+1] != ' ')||(array[x][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y] != ' ')||(array[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x-1][y] != ' ')||(array[x-1][y+1] != ' ')||(array[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
}
return 1;
}

int checkrechts(int x, int y, int block, int rotate)
{
switch (block)
{	case 0:		/* Voor het vierkant geldt: */
	{	switch (rotate)
		{	case 1:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
	case 1: /* Langwerpig blok: */
	{	switch (rotate)
		{	case 1:
			{	if ((array[x+8][y] != ' ')||(x>11)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(array[x+4][y+2] != ' ')||(array[x+4][y+3] != ' ')||(x>16)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x+8][y] != ' ')||(x>11)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(array[x+4][y+2] != ' ')||(array[x+4][y+3] != ' ')||(x>16)) return 1;
				else return 0;
			}
		}
	}
	case 2:		/* Verschoven blok */
	{	switch (rotate)
		{	case 1:
			{	if ((array[x+6][y] != ' ')||(array[x+4][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x+2][y] != ' ')||(array[x+4][y+1] != ' ')||(array[x+4][y+3] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x+6][y] != ' ')||(array[x+4][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+2][y] != ' ')||(array[x+4][y+1] != ' ')||(array[x+4][y+3] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
	case 3:		/* Verschoven blok */
	{	switch (rotate)		/* Bepaal de rotatie-variabele */
		{	case 1:
			{	if ((array[x+4][y] != ' ')||(array[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(array[x+2][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x+4][y] != ' ')||(array[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(array[x+2][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
	case 4:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x+6][y] != ' ')||(array[x+2][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(array[x+4][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x+6][y] != ' ')||(array[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(array[x+4][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
	case 5:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x+6][y] != ' ')||(array[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(array[x+4][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x+2][y] != ' ')||(array[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+4][y] != ' ')||(array[x+2][y+1] != ' ')||(array[x+2][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
	case 6:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x+6][y] != ' ')||(array[x+4][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x+4][y] != ' ')||(array[x+4][y+1] != ' ')||(array[x+4][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x+4][y] != ' ')||(array[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+2][y] != ' ')||(array[x+4][y+1] != ' ')||(array[x+2][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
}
return 1;
}

int checkonder(int x, int y, int block, int rotate)
{
switch (block)
{	case 0:		/* Voor het vierkant geldt: */
	{	switch (rotate)
		{	case 1:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;  /* Alles OK */
			}
			case 2:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
		}
	}
	case 1: /* Langwerpig blok: */
	{	switch (rotate)
		{	case 1:
			{	if ((array[x][y+1] != ' ')||(array[x+2][y+1] != ' ')||(array[x+4][y+1] != ' ')||(array[x+6][y+1] != ' ')||(y>16)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x+2][y+4] != ' ')||(y>13)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y+1] != ' ')||(array[x+2][y+1] != ' ')||(array[x+4][y+1] != ' ')||(array[x+6][y+1] != ' ')||(y>16)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x+2][y+4] != ' ')||(y>13)) return 1;
				else return 0;
			}
		}
	}
	case 2:		/* Verschoven blok */
	{	switch (rotate)
		{	case 1:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+2] != ' ')||(array[x+4][y+1] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+2] != ' ')||(array[x+4][y+1] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
		}
	}
	case 3:		/* Verschoven blok */
	{	switch (rotate)		/* Bepaal de rotatie-variabele */
		{	case 1:
			{	if ((array[x][y+1] != ' ')||(array[x+2][y+2] != ' ')||(array[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y+3] != ' ')||(array[x+2][y+2] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y+1] != ' ')||(array[x+2][y+2] != ' ')||(array[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x][y+3] != ' ')||(array[x+2][y+2] != ' ')||(y>14)) return 1;
				else return 0;
			}
		}
	}
	case 4:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+1] != ' ')||(array[x+4][y+1] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y+1] != ' ')||(array[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+2] != ' ')||(array[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x][y+3] != ' ')||(array[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
		}
	}
	case 5:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x][y+1] != ' ')||(array[x+2][y+1] != ' ')||(array[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y+3] != ' ')||(array[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+2] != ' ')||(array[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x][y+3] != ' ')||(array[x+2][y+1] != ' ')||(y>14)) return 1;
				else return 0;
			}
		}
	}
	case 6:
	{	switch (rotate)
		{	case 1:
			{	if ((array[x][y+1] != ' ')||(array[x+2][y+2] != ' ')||(array[x+4][y+1] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 3:
			{	if ((array[x][y+2] != ' ')||(array[x+2][y+2] != ' ')||(array[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((array[x][y+3] != ' ')||(array[x+2][y+2] != ' ')||(y>14)) return 1;
				else return 0;
			}
		}
	}
}
return 1;
}



int DrawBlock(int x, int y, int block, int rotate)
{
	int veldx =30, veldy =3;

switch (block)
	{	case 0:		/* Vierkant */
		{ textcolor(1);

			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						textcolor(7);
						break;
					}
				case 3:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						textcolor(7);
						break;
					}
			}
			return 0;
		}

		case 1:			/* Langwerpig blok */
		{	textcolor(2);
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€€€€€");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
						cprintf("€€");
						gotoxy(veldx+x+2,veldy+y+1);
						cprintf("€€");
						gotoxy(veldx+x+2,veldy+y+2);
						cprintf("€€");
						gotoxy(veldx+x+2,veldy+y+3);
						cprintf("€€");
						textcolor(7);
						break;
					}
				case 3:
					{

						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€€€€€");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x+2,veldy+y);
						cprintf("€€");
						gotoxy(veldx+x+2,veldy+y+1);
						cprintf("€€");
						gotoxy(veldx+x+2,veldy+y+2);
						cprintf("€€");
						gotoxy(veldx+x+2,veldy+y+3);
						cprintf("€€");
						textcolor(7);
						break;
					}
			}
			return 0;
		}

		case 2:			/* Verschoven blok */
		{ textcolor(3);
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x+2,veldy+y);
							cprintf("€€€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						gotoxy(veldx+x+2,veldy+y+2);
						cprintf("€€");
						textcolor(7);
						break;
					}
				case 3:
					{

						gotoxy(veldx+x+2,veldy+y);
						cprintf("€€€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						gotoxy(veldx+x+2,veldy+y+2);
						cprintf("€€");
						textcolor(7);
						break;
					}
			}
			return 0;
		}

		case 3:		/* Verschoven blok */
		{ textcolor(4);
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€");
						gotoxy(veldx+x+2,veldy+y+1);
							cprintf("€€€€");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
							cprintf("€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						gotoxy(veldx+x,veldy+y+2);
						cprintf("€€");
						textcolor(7);
						break;
					}
				case 3:
					{

						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€");
						gotoxy(veldx+x+2,veldy+y+1);
							cprintf("€€€€");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x+2,veldy+y);
							cprintf("€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						gotoxy(veldx+x,veldy+y+2);
						cprintf("€€");
						textcolor(7);
						break;
					}
			}
			return 0;
		}

		case 4:		/* Hoeksteen */
		{ textcolor(5);
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€");
						gotoxy(veldx+x+2,veldy+y+1);
							cprintf("€€");
						gotoxy(veldx+x+2,veldy+y+2);
							cprintf("€€");
						textcolor(7);
						break;
					}
				case 3:
					{

						gotoxy(veldx+x+4,veldy+y);
								cprintf("€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€€€");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€");
						gotoxy(veldx+x,veldy+y+2);
						cprintf("€€€€");
						textcolor(7);
						break;
					}
			}
			return 0;
		}

		case 5:		/* Hoeksteen */
		{ textcolor(6);
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€€€");
						gotoxy(veldx+x+4,veldy+y+1);
										cprintf("€€",1, 1);
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
								cprintf("€€",1, 1);
						gotoxy(veldx+x+2,veldy+y+1);
								cprintf("€€",1, 1);
						gotoxy(veldx+x,veldy+y+2);
						cprintf("€€€€");
						textcolor(7);
						break;
					}
				case 3:
					{

						gotoxy(veldx+x,veldy+y);
						cprintf("€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€€€");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€");
						gotoxy(veldx+x,veldy+y+2);
						cprintf("€€");
						textcolor(7);
						break;
					}
			}
			return 0;
		}

		case 6:		/* Driehoek */
		{ textcolor(7);
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€€€€€");
						gotoxy(veldx+x+2,veldy+y+1);
								cprintf("€€");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
							cprintf("€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						gotoxy(veldx+x+2,veldy+y+2);
							cprintf("€€");
						textcolor(7);
						break;
					}
				case 3:
					{
						gotoxy(veldx+x+2,veldy+y);
						cprintf("€€", 2, 2);
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€€€");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("€€");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("€€€€");
						gotoxy(veldx+x,veldy+y+2);
						cprintf("€€");
						textcolor(7);
						break;
					}
			}
			return 0;
		}

//return 0;
}
return 0;
}





int rmblock(int x, int y, int block, int rotate)
{
	int veldx =30, veldy =3;

switch (block)
	{	case 0:
		{

			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						printf("    ");
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						break;
					}
				case 2:
					{
						gotoxy(veldx+x,veldy+y);
						printf("    ");
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						break;
					}
				case 3:
					{
						gotoxy(veldx+x,veldy+y);
						printf("    ");
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						printf("    ");
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						break;
					}
			}
			return 0;
		}

		case 1:
		{
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						printf("        ");
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
						printf("  ");
						gotoxy(veldx+x+2,veldy+y+1);
						printf("  ");
						gotoxy(veldx+x+2,veldy+y+2);
						printf("  ");
						gotoxy(veldx+x+2,veldy+y+3);
						printf("  ");
						break;
					}
				case 3:
					{

						gotoxy(veldx+x,veldy+y);
						printf("        ");
						break;
					}
				case 4:
					{
						gotoxy(veldx+x+2,veldy+y);
						printf("  ");
						gotoxy(veldx+x+2,veldy+y+1);
						printf("  ");
						gotoxy(veldx+x+2,veldy+y+2);
						printf("  ");
						gotoxy(veldx+x+2,veldy+y+3);
						printf("  ");
						break;
					}
			}
			return 0;
		}

		case 2:
		{
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x+2,veldy+y);
						printf("    ");
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						break;
					}
				case 2:
					{
						gotoxy(veldx+x,veldy+y);
						printf("  ");
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						gotoxy(veldx+x+2,veldy+y+2);
						printf("  ");
						break;
					}
				case 3:
					{

						gotoxy(veldx+x+2,veldy+y);
						printf("    ");
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						printf("  ");
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						gotoxy(veldx+x+2,veldy+y+2);
						printf("  ");
						break;
					}
			}
			return 0;
		}

		case 3:
		{
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						printf("    ");
						gotoxy(veldx+x+2,veldy+y+1);
						printf("    ");
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
							printf("  ");
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						gotoxy(veldx+x,veldy+y+2);
						printf("  ");
						break;
					}
				case 3:
					{

						gotoxy(veldx+x,veldy+y);
						printf("    ");
						gotoxy(veldx+x+2,veldy+y+1);
							printf("    ");
						break;
					}
				case 4:
					{
						gotoxy(veldx+x+2,veldy+y);
							printf("  ");
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						gotoxy(veldx+x,veldy+y+2);
						printf("  ");
						break;
					}
			}
			return 0;
		}

		case 4:
		{
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						printf("      ");
						gotoxy(veldx+x,veldy+y+1);
						printf("  ");
						break;
					}
				case 2:
					{
						gotoxy(veldx+x,veldy+y);
						printf("    ");
						gotoxy(veldx+x+2,veldy+y+1);
							printf("  ");
						gotoxy(veldx+x+2,veldy+y+2);
							printf("  ");
						break;
					}
				case 3:
					{

						gotoxy(veldx+x+4,veldy+y);
								printf("  ");
						gotoxy(veldx+x,veldy+y+1);
						printf("      ");
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						printf("  ");
						gotoxy(veldx+x,veldy+y+1);
						printf("  ");
						gotoxy(veldx+x,veldy+y+2);
						printf("    ");
						break;
					}
			}
			return 0;
		}

		case 5:
		{
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						printf("      ");
						gotoxy(veldx+x+4,veldy+y+1);
						printf("  ");
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
							printf("  ");
						gotoxy(veldx+x+2,veldy+y+1);
							printf("  ");
						gotoxy(veldx+x,veldy+y+2);
						printf("    ");
						break;
					}
				case 3:
					{

						gotoxy(veldx+x,veldy+y);
						printf("  ",1, 1);
						gotoxy(veldx+x,veldy+y+1);
						printf("      ");
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						printf("    ");
						gotoxy(veldx+x,veldy+y+1);
						printf("  ");
						gotoxy(veldx+x,veldy+y+2);
						printf("  ");
						break;
					}
			}
			return 0;
		}

		case 6:
		{
			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						printf("      ");
						gotoxy(veldx+x+2,veldy+y+1);
						printf("  ");
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
						printf("  ");
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						gotoxy(veldx+x+2,veldy+y+2);
						printf("  ");
						break;
					}
				case 3:
					{
						gotoxy(veldx+x+2,veldy+y);
						printf("  ");
						gotoxy(veldx+x,veldy+y+1);
						printf("      ");
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						printf("  ", 2, 2);
						gotoxy(veldx+x,veldy+y+1);
						printf("    ");
						gotoxy(veldx+x,veldy+y+2);
						printf("  ");
						break;
					}
			}
			return 0;
		}

}
return 0;
}

int addblock2array(int x, int y, int block, int rotate)
{
	int nX, mX;

switch (block)
	{	case 0:		/* Vierkant */
		{

			switch (rotate)
			{
				case 1:
					{ for (nX=0; nX<4; ++nX) array[x+nX][y] = '€';
						for (nX=0; nX<4; ++nX) array[x+nX][y+1] = '€';

						for (nX=0; nX<4; ++nX) kleur[x+nX][y] = 1;
						for (nX=0; nX<4; ++nX) kleur[x+nX][y+1] = 1;

						break;
					}
				case 2:
					{ for (nX=0; nX<4; ++nX) array[x+nX][y] = '€';
						for (nX=0; nX<4; ++nX) array[x+nX][y+1] = '€';
						for (nX=0; nX<4; ++nX) kleur[x+nX][y] = 1;
						for (nX=0; nX<4; ++nX) kleur[x+nX][y+1] = 1;
						break;
					}
				case 3:
					{ for (nX=0; nX<4; ++nX) array[x+nX][y] = '€';
						for (nX=0; nX<4; ++nX) array[x+nX][y+1] = '€';
						for (nX=0; nX<4; ++nX) kleur[x+nX][y] = 1;
						for (nX=0; nX<4; ++nX) kleur[x+nX][y+1] = 1;
						break;
					}
				case 4:
					{ for (nX=0; nX<4; ++nX) array[x+nX][y] = '€';
						for (nX=0; nX<4; ++nX) array[x+nX][y+1] = '€';
						for (nX=0; nX<4; ++nX) kleur[x+nX][y] = 1;
						for (nX=0; nX<4; ++nX) kleur[x+nX][y+1] = 1;
						break;
					}
			}
			return 0;
		}
		case 1:			/* Langwerpig blok */
		{
			switch (rotate)
			{
				case 1:
					{ for (nX=0; nX<8; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 2; }
						break;
					}
				case 2:
					{ for (nX=0; nX<4; ++nX)
						{	for (mX=0; mX<2; ++mX)
							{	array[x+2+mX][y+nX] = '€'; kleur[x+2+mX][y+nX] = 2; }
						} break;
					}
				case 3:
					{ for (nX=0; nX<8; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 2; }
						break;
					}
				case 4:
					{ for (nX=0; nX<4; ++nX)
						{	for (mX=0; mX<2; ++mX)
							{	array[x+2+mX][y+nX] = '€'; kleur[x+2+mX][y+nX] = 2;
							}
						} break;
					}
			}
			return 0;
		}

		case 2:			/* Verschoven blok */
		{
			switch (rotate)
			{
				case 1:
					{ for(nX=0; nX<4; ++nX) { array[x+2+nX][y] = '€'; kleur[x+2+nX][y] = 3; }
						for(nX=0; nX<4; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 3; }
						break;
					}
				case 2:
					{ for(nX=0; nX<2; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 3; }
						for(nX=0; nX<4; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 3; }
						for(nX=0; nX<2; ++nX) { array[x+2+nX][y+2] = '€'; kleur[x+2+nX][y+2] = 3; }
						break;
					}
				case 3:
					{ for(nX=0; nX<4; ++nX) { array[x+2+nX][y] = '€'; kleur[x+2+nX][y] = 3; }
						for(nX=0; nX<4; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 3; }
						break;
					}
				case 4:
					{ for(nX=0; nX<2; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 3; }
						for(nX=0; nX<4; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 3; }
						for(nX=0; nX<2; ++nX) { array[x+2+nX][y+2] = '€'; kleur[x+2+nX][y+2] = 3; }
						break;
					}
			}
			return 0;
		}

		case 3:		/* Verschoven blok */
		{
			switch (rotate)
			{
				case 1:
					{ for(nX=0; nX<4; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 4; }
						for(nX=0; nX<4; ++nX) { array[x+2+nX][y+1] = '€'; kleur[x+2+nX][y+1] = 4; }
						break;
					}
				case 2:
					{ for(nX=0; nX<2; ++nX) { array[x+2+nX][y] = '€'; kleur[x+2+nX][y] = 4; }
						for(nX=0; nX<4; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 4; }
						for(nX=0; nX<2; ++nX) { array[x+nX][y+2] = '€'; kleur[x+nX][y+2] = 4; }
						break;
					}
				case 3:
					{ for(nX=0; nX<4; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 4; }
						for(nX=0; nX<4; ++nX) { array[x+2+nX][y+1] = '€'; kleur[x+2+nX][y+1] = 4; }
						break;
					}
				case 4:
					{ for(nX=0; nX<2; ++nX) { array[x+2+nX][y] = '€'; kleur[x+2+nX][y] = 4; }
						for(nX=0; nX<4; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 4; }
						for(nX=0; nX<2; ++nX) { array[x+nX][y+2] = '€'; kleur[x+nX][y+2] = 4; }
						break;
					}
			}
			return 0;
		}

		case 4:		/* Hoeksteen */
		{
			switch (rotate)
			{
				case 1:
					{ for(nX=0; nX<6; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 5; }
						for(nX=0; nX<2; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 5; }
						break;
					}
				case 2:
					{ for(nX=0; nX<4; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 5; }
						for(nX=0; nX<2; ++nX) { array[x+2+nX][y+1] = '€'; kleur[x+2+nX][y+1] = 5; }
						for(nX=0; nX<2; ++nX) { array[x+2+nX][y+2] = '€'; kleur[x+2+nX][y+2] = 5; }
						break;
					}
				case 3:
					{ for(nX=0; nX<2; ++nX) { array[x+4+nX][y] = '€'; kleur[x+4+nX][y] = 5; }
						for(nX=0; nX<6; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 5; }
						break;
					}
				case 4:
					{ for(nX=0; nX<2; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 5; }
						for(nX=0; nX<2; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 5; }
						for(nX=0; nX<4; ++nX) { array[x+nX][y+2] = '€'; kleur[x+nX][y+2] = 5; }
						break;
					}
			}
			return 0;
		}

		case 5:		/* Hoeksteen */
		{
			switch (rotate)
			{
				case 1:
					{ for(nX=0; nX<6; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 6; }
						for(nX=0; nX<2; ++nX) { array[x+4+nX][y+1] = '€'; kleur[x+4+nX][y+1] = 6; }
						break;
					}
				case 2:
					{ for(nX=0; nX<2; ++nX) { array[x+2+nX][y] = '€'; kleur[x+2+nX][y] = 6; }
						for(nX=0; nX<2; ++nX) { array[x+2+nX][y+1] = '€'; kleur[x+2+nX][y+1] = 6; }
						for(nX=0; nX<4; ++nX) { array[x+nX][y+2] = '€'; kleur[x+nX][y+2] = 6; }
						break;
					}
				case 3:
					{ for(nX=0; nX<2; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 6; }
						for(nX=0; nX<6; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 6; }
						break;
					}
				case 4:
					{ for(nX=0; nX<4; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 6; }
						for(nX=0; nX<2; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 6; }
						for(nX=0; nX<2; ++nX) { array[x+nX][y+2] = '€'; kleur[x+nX][y+2] = 6; }
						break;
					}
			}
			return 0;
		}

		case 6:		/* Driehoek */
		{
			switch (rotate)
			{
				case 1:
					{ for(nX=0; nX<6; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 7; }
						for(nX=0; nX<2; ++nX) { array[x+2+nX][y+1] = '€'; kleur[x+2+nX][y+1] = 7; }
						break;
					}
				case 2:
					{ for(nX=0; nX<2; ++nX) { array[x+2+nX][y] = '€'; kleur[x+2+nX][y] = 7; }
						for(nX=0; nX<4; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 7; }
						for(nX=0; nX<2; ++nX) { array[x+2+nX][y+2] = '€'; kleur[x+2+nX][y+2] = 7; }
						break;
					}
				case 3:
					{ for(nX=0; nX<2; ++nX) { array[x+2+nX][y] = '€'; kleur[x+2+nX][y] = 7; }
						for(nX=0; nX<6; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 7; }
						break;
					}
				case 4:
					{ for(nX=0; nX<2; ++nX) { array[x+nX][y] = '€'; kleur[x+nX][y] = 7; }
						for(nX=0; nX<4; ++nX) { array[x+nX][y+1] = '€'; kleur[x+nX][y+1] = 7; }
						for(nX=0; nX<2; ++nX) { array[x+nX][y+2] = '€'; kleur[x+nX][y+2] = 7; }
						break;
					}
			}
			return 0;
		}

}
return 0;
}


void pauze(void)
{	getch();
}