#include <errno.h>
#include <process.h>
#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <time.h>
#include <dos.h>
#include <string.h>
#define MAXNAME 15			// Maximale lengte van een playernaam in HiScore

typedef struct					// HiScore wordt opgeslagen in Structures
{
		int points;												// Punten v/d betreffende player
		int lines;												// Aantal lijnen
		char playernaam[MAXNAME];					// de naam v/d player in een string
}	hiscore;														// naam van de structure: hiscore

/* 					Gebruikte functies: */

int ChooseBlock(void);	/* Kiest een random blok */
void Init(void);	/* Initialiseren v/h speelveld met achtergrond */
void p2Init(void);	/* Initialiseren van 2-player speelveld */
void dlay( clock_t time );	/* Functie voor wachten voor x clocktikken */
int DrawBlock(int x, int y, int block, int rotate, int players);		/* Different types of TertrisBlocks*/
int rmblock(int x, int y, int block, int rotate, int players); /* Verwijdert een blok */
int checklinks(int x, int y, int block, int rotate, int players); /*Kijk of er links ruimte is*/
int checkrechts(int x, int y, int block, int rotate, int players); /* en rechts ruimte is.*/
int checkrotate(int x, int y, int block, int rotate, int players); /* Kijk of een rotatie mogelijk is */
int checkonder(int x, int y, int block, int rotate, int players); /* Blok op de bodem? */
char array[20][18];		/* Global variabele. Hierin staat het speelveld. */
char p2array[20][18]; /* idem, maar voor evt. 2e player */
int kleur[20][18];		/* Global array. Hierin staat de kleurcode van elke schermpositie. */
int p2kleur[20][18];	/* idem, maar voor evt. 2e player */
int cleararray(void);	/* Vult het Global array met spaties. */
int addblock2array(int x, int y, int block, int rotate, int players);	// ingewikkeld
int check4lines(int players);	/* Spoort gevormde lijnen op en verwijdert ze. */
void pauze(void);			// Pauzeert het spel.. pop-up window verschijnt
void snorkey(void);		/* Hidden and seldom used feature... */
void movefieldup(int player);		// Voegt lege regel toe in Death-Match
void dumpscreen(int player);		// Pleurt speelveld op het scherm
int menu(void);			//Hoofdmenu, keuze uit multiplayer etc.
void helpscreen(void);		//Online Help. Op te roepen met F1 of vanuit Menu
void printrank(int p1score, int p2score);	//Those funny lines in Multiplayer?
void hiscoreedit(int score, int lines); 	// HiScore functie: printen en editen
void printshit(int rank);							// Als 't maar een naam heeft
void printhiscore(void);							// spreekt voor zich
void printwinner(int winner, int mode, int p1score, int p2score);	// multiplayer einde

int main(void)						// Geen invoer via command-line
{
	int BlockNo, block[7], i, x, px, y, py, rotate=1, protate, nX, mX, totallines=0;
	int p2drop=0, drop=0, nextblock, lines=0, score=0, linescore=0, players;
	int p1score=0, p2score=0, p1nextblock, p2nextblock,p1totallines=0;
	int p2totallines=0, p1BlockNo, p2BlockNo, p1rotate, p2rotate, p1protate;
	int p2protate, p1x, p2x, p1y, p2y, p1px, p2px, p1py, p2py, p1drop=0;
	int newblockp2=0, newblockp1=0, mode, winner=0;
	clock_t start;
	clock_t p1start;
	clock_t p2start;
	char key;

	gamestart:
	textbackground(0);
	mode = menu();			// Hoofdmenu verschijnt en returnt de spelerskeuze.
	nomenugamestart:
	p1score =0; p2score =0; p1totallines =0; p2totallines =0; p1drop =0; p2drop = 0;
	newblockp1 =0; newblockp2 =0; score =0; linescore = 0, totallines=0, winner=0;
	_setcursortype(_NOCURSOR);		// Onzichtbare cursor (fucked up via telnet)
	randomize();				/* Randomize Timer */
	cleararray();				/* Maak het speelveld in het geheugen leeg */

if (mode == 6){ textcolor(7); clrscr(); _setcursortype(_NORMALCURSOR); return 0; }	/* Quit tetris */
if ((mode == 3)||(mode == 2))		// Multiplayer game gestart
{
p2Init();												// Teken speelveld voor 2 players
pauze();
p1nextblock = ChooseBlock(); p2nextblock = ChooseBlock();
p1BlockNo = p1nextblock; p2BlockNo = p2nextblock;
p1nextblock = ChooseBlock(); p2nextblock = ChooseBlock();
p1x=p1px=8; p1y=p1py=0; p1rotate=p1protate=1;
p2x=p2px=8; p2y=p2py=0; p2rotate=p2protate=1;
DrawBlock(p1x,p1y,p1BlockNo,p1rotate, 1);		//Nieuw blok is in het veld
DrawBlock(p2x,p2y,p2BlockNo,p2rotate, 2);		//Nieuw blok is in het veld
DrawBlock(23,7,p1nextblock,1,1);		//Teken preview player 1
DrawBlock(-11,7,p2nextblock,1,2);		//Teken preview player 2
p1start = clock();
p2start = clock();

for(;;)
{
			if (kbhit() != 0)		/* Lees Keyboard buffer uit */
			{	key = getch();
				switch (key)  		/* Voer toets uit */
				{ case 'A':
					case 'a': { if (!checklinks(p1x,p1y,p1BlockNo,p1rotate, 1) )
											{ p1px = p1x; p1x=(p1x-2);
												rmblock(p1px,p1py,p1BlockNo,p1protate, 1); /* Remove block */
												DrawBlock(p1x,p1y,p1BlockNo,p1rotate, 1);	/* Draw new */
												p1px = p1x;
												break; }
											else break;
										}
					case 'D':
					case 'd': { if (!checkrechts(p1x,p1y,p1BlockNo,p1rotate, 1) )
											{ p1px = p1x; p1x=(p1x+2);
												rmblock(p1px,p1py,p1BlockNo,p1protate, 1); /* Remove block */
												DrawBlock(p1x,p1y,p1BlockNo,p1rotate, 1);	/* Draw new */
												p1px = p1x;
												break; }
											else break;
										}
					case 'W':
					case 'w': { if (!checkrotate(p1x,p1y,p1BlockNo,p1rotate, 1) )
											{	p1protate = p1rotate++; if(p1rotate > 4) p1rotate=1;
												rmblock(p1px,p1py,p1BlockNo,p1protate, 1); /* Remove block */
												DrawBlock(p1x,p1y,p1BlockNo,p1rotate, 1);	/* Draw new */
												p1protate = p1rotate;
												break; }
											else break;
										}
					case 0x4B: { if (!checklinks(p2x,p2y,p2BlockNo,p2rotate, 2) )
											{ p2px = p2x; p2x=(p2x-2);
												rmblock(p2px,p2py,p2BlockNo,p2protate, 2); /* Remove block */
												DrawBlock(p2x,p2y,p2BlockNo,p2rotate, 2);	/* Draw new */
												p2px = p2x;
												break; }
											else break;
										}
					case 0x4D: { if (!checkrechts(p2x,p2y,p2BlockNo,p2rotate, 2) )
											{ p2px = p2x; p2x=(p2x+2);
												rmblock(p2px,p2py,p2BlockNo,p2protate, 2); /* Remove block */
												DrawBlock(p2x,p2y,p2BlockNo,p2rotate, 2);	/* Draw new */
												p2px = p2x;
												break; }
											else break;
										}
					case 0x48: { if (!checkrotate(p2x,p2y,p2BlockNo,p2rotate, 2) )
											{	p2protate = p2rotate++; if(p2rotate > 4) p2rotate=1;
												rmblock(p2px,p2py,p2BlockNo,p2protate, 2); /* Remove block */
												DrawBlock(p2x,p2y,p2BlockNo,p2rotate, 2);	/* Draw new */
												p2protate = p2rotate;
												break; }
											else break;
										}
					case 0x50: { p2start = p2start-7; break; }	// player2 drukt down
					case 'S':
					case 's': { p1start = p1start-7; break; }		// player1 drukt down
					case 0x1B: { if( (mode=menu()) != 7) goto nomenugamestart; else break; }
					case ' ': { p2drop = 1; p2start = p2start-7; break; } //Spatie
					case '`':
					case 0x09: { p1drop = 1; p1start = p1start-7; break; } //Spatie
//          case 'P':
					case 'p': { pauze(); break; }
					case 0x3B: { helpscreen(); break; }			// F1
					case 0x0D: {snorkey(); break; }
					default: {;}
				}
			}			// Toetsafhandeling klaar

	if(clock()>p1start+7-(p1totallines/15))		// Blok player 1 valt 1 positie
	{	if ((!checkonder(p1x,p1y,p1BlockNo,p1rotate, 1))&&(!newblockp1))
		{ p1py = p1y++;
			rmblock(p1px,p1py,p1BlockNo,p1protate, 1); /* Remove block */
			DrawBlock(p1x,p1y,p1BlockNo,p1rotate, 1);	/* Draw new */
			if (!p1drop) p1start = clock();
			p1py = p1y;
		}
		else 																		// Blok player 1 is op de bodem!
		{
			if(!newblockp1) addblock2array(p1x,p1y,p1BlockNo,p1rotate, 1);
			newblockp1=0;
			lines = check4lines(1);							//Gevormde lijnen worden verwijderd
			if(mode==3){												// Alleen in Death-Match
			for(nX=0; nX<lines; ++nX)
			{	if(!checkonder(p2x,p2y,p2BlockNo,p2rotate,2))	//Kijk of het kan
				{	movefieldup(2); }														//Veld schuift op
				else                                          //anders:
				{	addblock2array(p2x,p2y,p2BlockNo,p2rotate,2); //blok vastzetten
					for(mX=0; mX<(lines-nX); ++mX)							//de resterende lijnen
						movefieldup(2);														//naar boven schuiven
					newblockp2=1; p2start = p2start-7; 			//player 2 needs new block
					dumpscreen(2);															//dump nieuw scherm
					break;																			//en klaar
				}
				dumpscreen(2);
				DrawBlock(p2x,p2y,p2BlockNo,p2rotate,2);			//en blok terugzetten
			}
			}
			p1totallines = p1totallines + lines;
			gotoxy(37,14); printf("%d", p1totallines); //Print player 1's lines
			switch (lines)
			{	case 1:	{ linescore=10; break;}
				case 2: { linescore=30; break;}
				case 3: { linescore=60; break;}
				case 4: { linescore=100; break;}
			}
			p1score = p1score + linescore; linescore=0;
			gotoxy(34,6); printf("%d", p1score);			//Print player 1's score
			p1BlockNo = p1nextblock;		//Schuif voorspelde blok in het veld
			p1score = p1score + 5;
			printrank(p1score,p2score);
			p1nextblock = ChooseBlock();	//Voorspel nieuw blok
			gotoxy(31,10); printf("        "); gotoxy(31,11); printf("        ");
			DrawBlock(23,7,p1nextblock,1,1);		//Teken preview player 1
			p1x=p1px=8; p1y=p1py=0; p1rotate=p1protate=1;
			if (checkonder(p1x,p1y,p1BlockNo,p1rotate, 1)) {printf("\a"); winner = 2; break;}
			p1drop = 0;
			DrawBlock(p1x,p1y,p1BlockNo,p1rotate, 1);		//Nieuw blok is in het veld
			p1start = clock();													//Start de timer
		}
	}

	if(clock()>p2start+7-(p2totallines/15))		// Blok player 2 valt 1 positie
	{	if ((!checkonder(p2x,p2y,p2BlockNo,p2rotate, 2))&&(!newblockp2))
		{ p2py = p2y++;
			rmblock(p2px,p2py,p2BlockNo,p2protate, 2); /* Remove block */
			DrawBlock(p2x,p2y,p2BlockNo,p2rotate, 2);	/* Draw new */
			if (!p2drop) p2start = clock();
			p2py = p2y;
		}
		else 																		// Blok player 2 is op de bodem!
		{
			if(!newblockp2) addblock2array(p2x,p2y,p2BlockNo,p2rotate, 2);
			newblockp2=0;
			lines = check4lines(2);							//Gevormde lijnen worden verwijderd
			if(mode==3) {
			for(nX=0; nX<lines; ++nX)
			{	if(!checkonder(p1x,p1y,p1BlockNo,p1rotate,1))	//Kijk of het kan
				{	movefieldup(1); }														//Veld schuift op
				else                                          //anders:
				{	addblock2array(p1x,p1y,p1BlockNo,p1rotate,1); //blok vastzetten
					for(mX=0; mX<(lines-nX); ++mX)							//de resterende lijnen
						movefieldup(1);														//naar boven schuiven
					newblockp1=1; p1start = p1start-7; 			//player 1 needs new block
					dumpscreen(1);															//dump nieuw scherm
					break;																			//en klaar
				}
				dumpscreen(1);
				DrawBlock(p1x,p1y,p1BlockNo,p1rotate,1);			//en blok terugzetten
			}
			}
			p2totallines = p2totallines + lines;
			gotoxy(48,14); printf("%d", p2totallines); //Print player 2's lines
			switch (lines)
			{	case 1:	{ linescore=10; break;}
				case 2: { linescore=30; break;}
				case 3: { linescore=60; break;}
				case 4: { linescore=100; break;}
			}
			p2score = p2score + linescore; linescore=0;
			gotoxy(45,6); printf("%d", p2score);			//Print player 2's score
			p2score = p2score + 5;
			gotoxy(41,10); printf("        "); gotoxy(41,11); printf("        ");
			printrank(p1score,p2score);
			p2BlockNo = p2nextblock;
			p2nextblock = ChooseBlock();	//Voorspel nieuw blok
			DrawBlock(-11,7,p2nextblock,1,2);		//Teken preview player 2
			p2x=p2px=8; p2y=p2py=0; p2rotate=p2protate=1;
			if (checkonder(p2x,p2y,p2BlockNo,p2rotate, 2)) {printf("\a"); winner = 1; break;}
			p2drop = 0;
			DrawBlock(p2x,p2y,p2BlockNo,p2rotate, 2);		//Nieuw blok is in het veld
			p2start = clock();													//Start de timer
		}
	}
}
printwinner(winner, mode, p1score, p2score);
}

if (mode == 1)			// Single Player Tetris gestart
{

Init();
pauze();
nextblock = ChooseBlock();
for(nX=0; nX<7; ++nX) block[nX] = 0;	//Reset de Statistics.
for(;;)
{ BlockNo = nextblock;		/* Voorspel het volgende blok */
	nextblock = ChooseBlock(); score=score+5;
	gotoxy(15,10); printf("        "); gotoxy(15,11); printf("        ");
	DrawBlock(-15,7,nextblock,1, 0);		/* Print het volgende blok */
	++block[BlockNo]; drop = 0;			/* Print statistics */
	gotoxy(68, (8 + 2*BlockNo)); printf("%d", block[BlockNo]);
	x=px=8; y=py=0; rotate=protate=1;
	if (checkonder(x,y,BlockNo,rotate, 0)) {printf("\a"); break;}	// veld vol!
	for (;;)
	{
		if (i>0) rmblock(px,py,BlockNo,protate, 0);	/* Verwijder... */
		DrawBlock(x,y,BlockNo,rotate, 0);						/* ...en teken nieuw blok. */
		px=x; py=y; protate=rotate;
		start = clock();
		if (drop) (start = start-7);
		do
		{	if (kbhit() != 0)		/* Lees Keyboard buffer uit */
			{	key = getch();
				switch (key)
				{	case 0x4B: { if (!checklinks(x,y,BlockNo,rotate, 0) )
											{ px = x; x=(x-2); break; }
											else break;
										}
					case 0x4D: { if (!checkrechts(x,y,BlockNo,rotate, 0) )
											{ px = x; x=(x+2); break; }
											else break;
										}
					case 0x48: { if (!checkrotate(x,y,BlockNo,rotate, 0) )
											{	protate = rotate++; if(rotate > 4) rotate=1; break; }
											else break;
										}
					case 0x50: { start = start-7; break; }
					case 0x1B: { if((mode=menu()) != 7) goto nomenugamestart; else break; }
					case ' ': { drop = 1; start = start-7; break; }
					case 0x3B: { helpscreen(); break; }	// F1 voor Help
					case 'p': { pauze(); break; }       // p voor pauze
					case 0x0D: {snorkey(); break; }
					default: {;}
				}
			rmblock(px,py,BlockNo,protate, 0);     /* Remove block */
			DrawBlock(x,y,BlockNo,rotate, 0);			/* Draw new */
			px=x; py=y; protate=rotate;
			}
		}	while (clock()<start+7-(totallines/15));	/* Pause for x clockticks */
		if ( !checkonder(x,y,BlockNo,rotate, 0) ) py = y++;
		else break;
	}
	addblock2array(x,y,BlockNo,rotate, 0);
	lines = check4lines(0);
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
hiscoreedit(score, totallines);		// HiScore wordt aangeroepen, playerscore wordt meegegeven
}
	goto gamestart;
}											// Tetris klaar

int check4lines(int players)
{	int nX, mX, oX, mY, by, plaats, line[5], i=0;
	int veldx = 30, veldy = 2, kleurbuffer[20][18];
	char buffer[20][18], veldarray[20][18];
	int veldkleur[20][18];

if((players==1)||(players==0))
{	veldx=8;
	for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = array[mX][nX];
			veldkleur[mX][nX] = kleur[mX][nX];
		}
	}
}

if(players==2)
{	veldx=52;
	for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = p2array[mX][nX];
			veldkleur[mX][nX] = p2kleur[mX][nX];
		}
	}
}
if (players==0) veldx=30;
for(nX=0; nX<5; ++nX) line[nX] = -1;

for (nX=0; nX<18; ++nX)
{ plaats = 0;
	for (mX=0; mX<20; (mX=mX+2))
	{ if (veldarray[mX][nX] != ' ') ++plaats;
		else break;
		if (plaats == 10) line[i++] = nX;		/* in line[] staan nu de y-waarden van de gevonden lijnen */
	}
}

	if (i>0)		/* De gevormde lijn(nen) knipperen even */
	{ if (players==0)
		{	for (oX=0; oX<3; ++oX)
			{	for (nX=0; nX<i; ++nX)
				{ gotoxy(veldx, veldy+line[nX]+1);
					printf("                    ");		/* lijnen worden weggehaald...*/
				}
				dlay(2);		/* 2 clockpulsen pauze */
				for (nX=0; nX<i; ++nX)
				{	gotoxy(veldx, veldy+line[nX]+1);
					mY = line[nX];										/* ...en weer getekend */
					for(mX=0; mX<20; ++mX) printf("%c", veldarray[mX][mY]);
				}
				dlay(2);
			}
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
		{	for(mX=0; mX<20; ++mX){ buffer[mX][by] = veldarray[mX][nX];
															kleurbuffer[mX][by] = veldkleur[mX][nX]; }
			--by;
		}
	}

	for(nX=0; nX<18; ++nX)		/* kopieer de buffer naar array */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = buffer[mX][nX];
			veldkleur[mX][nX] = kleurbuffer[mX][nX]; /* en de kleurbuffer naar kleur*/
		}
	}

	for(nX=0; nX<18; ++nX)		/* print de array, de lijnen zijn nu verdwenen */
	{ gotoxy(veldx, veldy+nX+1);
		for(mX=0; mX<20; ++mX)
		{	textcolor(veldkleur[mX][nX]);
			cprintf("%c", veldarray[mX][nX]);
		}
	}
}

if((players==1)||(players==0))
{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
	{	for(mX=0; mX<20; ++mX)
		{	array[mX][nX] = veldarray[mX][nX];
			kleur[mX][nX] = veldkleur[mX][nX];
		}
	}
}
if(players==2)
{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
	{	for(mX=0; mX<20; ++mX)
		{	p2array[mX][nX] = veldarray[mX][nX];
			p2kleur[mX][nX] = veldkleur[mX][nX];
		}
	}
}

return i;				// Return het aantal gevonden en verwijderde lijnen
}

int cleararray(void)
{	int nX, mX;

for (nX=0; nX<20; ++nX)
{	for (mX=0; mX<18; ++mX)
	{	array[nX][mX] = ' ';
		kleur[nX][mX] = 0;
		p2array[nX][mX] = ' ';
		p2kleur[nX][mX] = 0;
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
	do
	{;}
	while (clock()<start+time);
}

void p2Init(void)		// 2 Player scherm
{
textcolor(7);
clrscr();
printf("                                    TETRIS\n");
printf("\n");
printf("      บ                    ฬออออออออออออออออออออออน                    บ\n");
printf("      บ                    บ  POINTS:    POINTS:  บ                    บ\n");
printf("      บ                    บ                      บ                    บ\n");
printf("      บ                    บ     0          0     บ                    บ\n");
printf("      บ                    ฬออออออออออออออออออออออน                    บ\n");
printf("      บ                    บ   NEXT:      NEXT:   บ                    บ\n");
printf("      บ                    บ                      บ                    บ\n");
printf("      บ                    บ                      บ                    บ\n");
printf("      บ                    บ                      บ                    บ\n");
printf("      บ                    บ                      บ                    บ\n");
printf("      บ                    ฬออออออออออออออออออออออน                    บ\n");
printf("      บ                    บ lines: 0   lines: 0  บ                    บ\n");
printf("      บ                    ฬออออออออออออออออออออออน                    บ\n");
printf("      บ                    บCONTROLS:             บ                    บ\n");
printf("      บ                    บ      playerI playerIIบ                    บ\n");
printf("      บ                    บRight:   d     arrow  บ                    บ\n");
printf("      บ                    บLeft:    a     arrow  บ                    บ\n");
printf("      บ                    บRotate:  w     arrow  บ                    บ\n");
printf("      ฬออออออออออออออออออออนDown:    s     arrow  ฬออออออออออออออออออออน\n");
printf("      บ     PLAYER I       บDrop:    `     space  บ     PLAYER II      บ\n");
printf("      ศออออออออออออออออออออสออออออออออออออออออออออสออออออออออออออออออออผ\n");
textcolor(7);
}



void Init(void)		/* Teken 1malig het speelveld */
{
//	int n;
textcolor(7);
clrscr();
printf("                                     TETRIS\n");
printf("\n");
printf("      ษอออออออออออออออออออออน                    ฬอออออออออออออออออออออป\n");
printf("      บ       POINTS:       บ                    บ     STATISTICS:     บ\n");
printf("      บ                     บ                    บ                     บ\n");
printf("      บ         0           บ                    บ lines:..........0   บ\n");
printf("      ฬอออออออออออออออออออออน                    บ                     บ\n");
printf("      บ     NEXT BLOCK:     บ                    บ ÛÛ..............0   บ\n");
printf("      บ                     บ                    บ                     บ\n");
printf("      บ                     บ                    บ ÜÜÜÜ............0   บ\n");
printf("      บ                     บ                    บ                     บ\n");
printf("      บ                     บ                    บ ÜÛ฿.............0   บ\n");
printf("      ฬอออออออออออออออออออออน                    บ                     บ\n");
printf("      บ      CONTROLS:      บ                    บ ฿ÛÜ.............0   บ\n");
printf("      บ                     บ                    บ                     บ\n");
printf("      บ Left:........'left' บ                    บ ÜÜÛ.............0   บ\n");
printf("      บ Right:......'right' บ                    บ                     บ\n");
printf("      บ Rotate:........'up' บ                    บ ÛÜÜ.............0   บ\n");
printf("      บ Push down:...'down' บ                    บ                     บ\n");
printf("      บ Drop:.......'space' บ                    บ ฿Û฿.............0   บ\n");
printf("      ฬอออออออออออออออออออออสออออออออออออออออออออสอออออออออออออออออออออน\n");
printf("      บ  F1 = Help    p = Pause    Esc = Menu    Enter = Teacher Alert บ\n");
printf("      ศออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออผ\n");
textcolor(7);
}

int checkrotate(int x, int y, int block, int rotate, int players)
{	char veldarray[20][18];
	int nX, mX;

if((players==1)||(players==0))
{ for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = array[mX][nX];
		}
	}
}

if(players==2)
{	for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = p2array[mX][nX];
		}
	}
}

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
			{	if ((veldarray[x+2][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(veldarray[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x+4][y] != ' ')||(veldarray[x+6][y] != ' ')) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x+2][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(veldarray[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x+4][y] != ' ')||(veldarray[x+6][y] != ' ')) return 1;
				else return 0;
			}
		}
	}
	case 2:		/* Verschoven blok */
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x+2][y] != ' ')||(veldarray[x+4][y] != ' ')) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+2][y] != ' ')||(veldarray[x+4][y] != ' ')) return 1;
				else return 0;
			}
		}
	}
	case 3:		/* Verschoven blok */
	{	switch (rotate)		/* Bepaal de rotatie-variabele */
		{	case 1:
			{	if ((veldarray[x][y+1] != ' ')||(veldarray[x][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x+4][y+1] != ' ')) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y+1] != ' ')||(veldarray[x][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x+4][y+1] != ' ')) return 1;
				else return 0;
			}
		}
	}
	case 4:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x+2][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x][y+1] != ' ')||(veldarray[x+4][y+1] != ' ')) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x][y+2] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+2][y] != ' ')||(veldarray[x+4][y] != ' ')) return 1;
				else return 0;
			}
		}
	}
	case 5:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x+2][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(veldarray[x][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x][y+1] != ' ')||(veldarray[x+4][y+1] != ' ')) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x+2][y] != ' ')||(veldarray[x][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+2][y+1] != ' ')||(veldarray[x+4][y+1] != ' ')) return 1;
				else return 0;
			}
		}
	}
	case 6:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if (veldarray[x+4][y+1] != ' ') return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+2][y] != ' ')||(veldarray[x+4][y] != ' ')) return 1;
				else return 0;
			}
		}
	}
}
return 1;
}



int checklinks(int x, int y, int block, int rotate, int players)
{	char veldarray[20][18];
	int nX, mX;

if((players==1)||(players==0))
{ for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = array[mX][nX];
		}
	}
}
if(players==2)
{	for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = p2array[mX][nX];
		}
	}
}

switch (block)
{	case 0:		/* Voor het vierkant geldt: */
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
	case 1: /* Langwerpig blok: */
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x-1][y] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x][y+1] != ' ')||(veldarray[x][y+2] != ' ')||(veldarray[x][y+3] != ' ')||(x<0)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x-1][y] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x][y+1] != ' ')||(veldarray[x][y+2] != ' ')||(veldarray[x][y+3] != ' ')||(x<0)) return 1;
				else return 0;
			}
		}
	}
	case 2:		/* Verschoven blok */
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(veldarray[x][y+3] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(veldarray[x][y+3] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
	case 3:		/* Verschoven blok */
	{	switch (rotate)		/* Bepaal de rotatie-variabele */
		{	case 1:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(veldarray[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(veldarray[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
	case 4:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x][y+1] != ' ')||(veldarray[x][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x+3][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(veldarray[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
	case 5:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x+3][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x][y+1] != ' ')||(veldarray[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(veldarray[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
	case 6:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(veldarray[x][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(x<1)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x-1][y] != ' ')||(veldarray[x-1][y+1] != ' ')||(veldarray[x-1][y+2] != ' ')||(x<1)) return 1;
				else return 0;
			}
		}
	}
}
return 1;
}

int checkrechts(int x, int y, int block, int rotate, int players)
{	char veldarray[20][18];
	int nX, mX;

if((players==1)||(players==0))
{ for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = array[mX][nX];
		}
	}
}
if(players==2)
{	for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = p2array[mX][nX];
		}
	}
}

switch (block)
{	case 0:		/* Voor het vierkant geldt: */
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
	case 1: /* Langwerpig blok: */
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x+8][y] != ' ')||(x>11)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+4][y+2] != ' ')||(veldarray[x+4][y+3] != ' ')||(x>16)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x+8][y] != ' ')||(x>11)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+4][y+2] != ' ')||(veldarray[x+4][y+3] != ' ')||(x>16)) return 1;
				else return 0;
			}
		}
	}
	case 2:		/* Verschoven blok */
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x+6][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x+2][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+4][y+3] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x+6][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+2][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+4][y+3] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
	case 3:		/* Verschoven blok */
	{	switch (rotate)		/* Bepaal de rotatie-variabele */
		{	case 1:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
	case 4:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x+6][y] != ' ')||(veldarray[x+2][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+4][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x+6][y] != ' ')||(veldarray[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+4][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
	case 5:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x+6][y] != ' ')||(veldarray[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+4][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x+2][y] != ' ')||(veldarray[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+2][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
	case 6:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x+6][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+4][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x+4][y] != ' ')||(veldarray[x+6][y+1] != ' ')||(x>13)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+2][y] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(x>15)) return 1;
				else return 0;
			}
		}
	}
}
return 1;
}

int checkonder(int x, int y, int block, int rotate, int players)
{	char veldarray[20][18];
	int nX, mX;

if((players==1)||(players==0))
{ for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = array[mX][nX];
		}
	}
}
if(players==2)
{	for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = p2array[mX][nX];
		}
	}
}

switch (block)
{	case 0:		/* Voor het vierkant geldt: */
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;  /* Alles OK */
			}
			case 2:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
		}
	}
	case 1: /* Langwerpig blok: */
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x][y+1] != ' ')||(veldarray[x+2][y+1] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+6][y+1] != ' ')||(y>16)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x+2][y+4] != ' ')||(y>13)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y+1] != ' ')||(veldarray[x+2][y+1] != ' ')||(veldarray[x+4][y+1] != ' ')||(veldarray[x+6][y+1] != ' ')||(y>16)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x+2][y+4] != ' ')||(y>13)) return 1;
				else return 0;
			}
		}
	}
	case 2:		/* Verschoven blok */
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+2] != ' ')||(veldarray[x+4][y+1] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+2] != ' ')||(veldarray[x+4][y+1] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
		}
	}
	case 3:		/* Verschoven blok */
	{	switch (rotate)		/* Bepaal de rotatie-variabele */
		{	case 1:
			{	if ((veldarray[x][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(veldarray[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y+3] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(veldarray[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x][y+3] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>14)) return 1;
				else return 0;
			}
		}
	}
	case 4:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+1] != ' ')||(veldarray[x+4][y+1] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y+1] != ' ')||(veldarray[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+2] != ' ')||(veldarray[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x][y+3] != ' ')||(veldarray[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
		}
	}
	case 5:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x][y+1] != ' ')||(veldarray[x+2][y+1] != ' ')||(veldarray[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y+3] != ' ')||(veldarray[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+2] != ' ')||(veldarray[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x][y+3] != ' ')||(veldarray[x+2][y+1] != ' ')||(y>14)) return 1;
				else return 0;
			}
		}
	}
	case 6:
	{	switch (rotate)
		{	case 1:
			{	if ((veldarray[x][y+1] != ' ')||(veldarray[x+2][y+2] != ' ')||(veldarray[x+4][y+1] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 2:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+3] != ' ')||(y>14)) return 1;
				else return 0;
			}
			case 3:
			{	if ((veldarray[x][y+2] != ' ')||(veldarray[x+2][y+2] != ' ')||(veldarray[x+4][y+2] != ' ')||(y>15)) return 1;
				else return 0;
			}
			case 4:
			{	if ((veldarray[x][y+3] != ' ')||(veldarray[x+2][y+2] != ' ')||(y>14)) return 1;
				else return 0;
			}
		}
	}
}
return 1;
}



int DrawBlock(int x, int y, int block, int rotate, int players)
{
	int veldx =30, veldy =3;

if(players==1) veldx=8;
if(players==2) veldx=52;
switch (block)
	{	case 0:		/* Vierkant */
		{ textcolor(1);

			switch (rotate)
			{
				case 1:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 3:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
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
						cprintf("ÛÛÛÛÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
						cprintf("ÛÛ");
						gotoxy(veldx+x+2,veldy+y+1);
						cprintf("ÛÛ");
						gotoxy(veldx+x+2,veldy+y+2);
						cprintf("ÛÛ");
						gotoxy(veldx+x+2,veldy+y+3);
						cprintf("ÛÛ");
						textcolor(7);
						break;
					}
				case 3:
					{

						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛÛÛÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x+2,veldy+y);
						cprintf("ÛÛ");
						gotoxy(veldx+x+2,veldy+y+1);
						cprintf("ÛÛ");
						gotoxy(veldx+x+2,veldy+y+2);
						cprintf("ÛÛ");
						gotoxy(veldx+x+2,veldy+y+3);
						cprintf("ÛÛ");
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
							cprintf("ÛÛÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x+2,veldy+y+2);
						cprintf("ÛÛ");
						textcolor(7);
						break;
					}
				case 3:
					{

						gotoxy(veldx+x+2,veldy+y);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x+2,veldy+y+2);
						cprintf("ÛÛ");
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
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x+2,veldy+y+1);
							cprintf("ÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
							cprintf("ÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x,veldy+y+2);
						cprintf("ÛÛ");
						textcolor(7);
						break;
					}
				case 3:
					{

						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x+2,veldy+y+1);
							cprintf("ÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x+2,veldy+y);
							cprintf("ÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x,veldy+y+2);
						cprintf("ÛÛ");
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
						cprintf("ÛÛÛÛÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛ");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x+2,veldy+y+1);
							cprintf("ÛÛ");
						gotoxy(veldx+x+2,veldy+y+2);
							cprintf("ÛÛ");
						textcolor(7);
						break;
					}
				case 3:
					{

						gotoxy(veldx+x+4,veldy+y);
								cprintf("ÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛ");
						gotoxy(veldx+x,veldy+y+2);
						cprintf("ÛÛÛÛ");
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
						cprintf("ÛÛÛÛÛÛ");
						gotoxy(veldx+x+4,veldy+y+1);
										cprintf("ÛÛ",1, 1);
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
								cprintf("ÛÛ",1, 1);
						gotoxy(veldx+x+2,veldy+y+1);
								cprintf("ÛÛ",1, 1);
						gotoxy(veldx+x,veldy+y+2);
						cprintf("ÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 3:
					{

						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛ");
						gotoxy(veldx+x,veldy+y+2);
						cprintf("ÛÛ");
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
						cprintf("ÛÛÛÛÛÛ");
						gotoxy(veldx+x+2,veldy+y+1);
								cprintf("ÛÛ");
						textcolor(7);
						break;
					}
				case 2:
					{
						gotoxy(veldx+x+2,veldy+y);
							cprintf("ÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x+2,veldy+y+2);
							cprintf("ÛÛ");
						textcolor(7);
						break;
					}
				case 3:
					{
						gotoxy(veldx+x+2,veldy+y);
						cprintf("ÛÛ", 2, 2);
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛÛÛ");
						textcolor(7);
						break;
					}
				case 4:
					{
						gotoxy(veldx+x,veldy+y);
						cprintf("ÛÛ");
						gotoxy(veldx+x,veldy+y+1);
						cprintf("ÛÛÛÛ");
						gotoxy(veldx+x,veldy+y+2);
						cprintf("ÛÛ");
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

int rmblock(int x, int y, int block, int rotate, int players)
{
	int veldx =30, veldy =3;

if(players==1) veldx=8;
if(players==2) veldx=52;
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

int addblock2array(int x, int y, int block, int rotate, int players)
{
	int nX, mX, veldkleur[20][18];
	char veldarray[20][18];

if((players==1)||(players==0))
{ for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = array[mX][nX];
			veldkleur[mX][nX] = kleur[mX][nX];
		}
	}
}

if(players==2)
{	for(nX=0; nX<18; ++nX)		/* Maak exacte kopie van player's veld in MEM */
	{	for(mX=0; mX<20; ++mX)
		{	veldarray[mX][nX] = p2array[mX][nX];
			veldkleur[mX][nX] = p2kleur[mX][nX];
		}
	}
}

switch (block)
	{	case 0:		/* Vierkant */
		{

			switch (rotate)
			{
				case 1:
					{ for (nX=0; nX<4; ++nX) veldarray[x+nX][y] = 'Û';
						for (nX=0; nX<4; ++nX) veldarray[x+nX][y+1] = 'Û';

						for (nX=0; nX<4; ++nX) veldkleur[x+nX][y] = 1;
						for (nX=0; nX<4; ++nX) veldkleur[x+nX][y+1] = 1;

						break;
					}
				case 2:
					{ for (nX=0; nX<4; ++nX) veldarray[x+nX][y] = 'Û';
						for (nX=0; nX<4; ++nX) veldarray[x+nX][y+1] = 'Û';
						for (nX=0; nX<4; ++nX) veldkleur[x+nX][y] = 1;
						for (nX=0; nX<4; ++nX) veldkleur[x+nX][y+1] = 1;
						break;
					}
				case 3:
					{ for (nX=0; nX<4; ++nX) veldarray[x+nX][y] = 'Û';
						for (nX=0; nX<4; ++nX) veldarray[x+nX][y+1] = 'Û';
						for (nX=0; nX<4; ++nX) veldkleur[x+nX][y] = 1;
						for (nX=0; nX<4; ++nX) veldkleur[x+nX][y+1] = 1;
						break;
					}
				case 4:
					{ for (nX=0; nX<4; ++nX) veldarray[x+nX][y] = 'Û';
						for (nX=0; nX<4; ++nX) veldarray[x+nX][y+1] = 'Û';
						for (nX=0; nX<4; ++nX) veldkleur[x+nX][y] = 1;
						for (nX=0; nX<4; ++nX) veldkleur[x+nX][y+1] = 1;
						break;
					}
			}
			if((players==1)||(players==0))
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	array[mX][nX] = veldarray[mX][nX];
						kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			if(players==2)
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	p2array[mX][nX] = veldarray[mX][nX];
						p2kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}

			return 0;
		}
		case 1:			/* Langwerpig blok */
		{
			switch (rotate)
			{
				case 1:
					{ for (nX=0; nX<8; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 2; }
						break;
					}
				case 2:
					{ for (nX=0; nX<4; ++nX)
						{	for (mX=0; mX<2; ++mX)
							{	veldarray[x+2+mX][y+nX] = 'Û'; veldkleur[x+2+mX][y+nX] = 2; }
						} break;
					}
				case 3:
					{ for (nX=0; nX<8; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 2; }
						break;
					}
				case 4:
					{ for (nX=0; nX<4; ++nX)
						{	for (mX=0; mX<2; ++mX)
							{	veldarray[x+2+mX][y+nX] = 'Û'; veldkleur[x+2+mX][y+nX] = 2;
							}
						} break;
					}
			}
			if((players==1)||(players==0))
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	array[mX][nX] = veldarray[mX][nX];
						kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			if(players==2)
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	p2array[mX][nX] = veldarray[mX][nX];
						p2kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			return 0;
		}

		case 2:			/* Verschoven blok */
		{
			switch (rotate)
			{
				case 1:
					{ for(nX=0; nX<4; ++nX) { veldarray[x+2+nX][y] = 'Û'; veldkleur[x+2+nX][y] = 3; }
						for(nX=0; nX<4; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 3; }
						break;
					}
				case 2:
					{ for(nX=0; nX<2; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 3; }
						for(nX=0; nX<4; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 3; }
						for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y+2] = 'Û'; veldkleur[x+2+nX][y+2] = 3; }
						break;
					}
				case 3:
					{ for(nX=0; nX<4; ++nX) { veldarray[x+2+nX][y] = 'Û'; veldkleur[x+2+nX][y] = 3; }
						for(nX=0; nX<4; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 3; }
						break;
					}
				case 4:
					{ for(nX=0; nX<2; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 3; }
						for(nX=0; nX<4; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 3; }
						for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y+2] = 'Û'; veldkleur[x+2+nX][y+2] = 3; }
						break;
					}
			}
			if((players==1)||(players==0))
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	array[mX][nX] = veldarray[mX][nX];
						kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			if(players==2)
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	p2array[mX][nX] = veldarray[mX][nX];
						p2kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			return 0;
		}

		case 3:		/* Verschoven blok */
		{
			switch (rotate)
			{
				case 1:
					{ for(nX=0; nX<4; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 4; }
						for(nX=0; nX<4; ++nX) { veldarray[x+2+nX][y+1] = 'Û'; veldkleur[x+2+nX][y+1] = 4; }
						break;
					}
				case 2:
					{ for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y] = 'Û'; veldkleur[x+2+nX][y] = 4; }
						for(nX=0; nX<4; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 4; }
						for(nX=0; nX<2; ++nX) { veldarray[x+nX][y+2] = 'Û'; veldkleur[x+nX][y+2] = 4; }
						break;
					}
				case 3:
					{ for(nX=0; nX<4; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 4; }
						for(nX=0; nX<4; ++nX) { veldarray[x+2+nX][y+1] = 'Û'; veldkleur[x+2+nX][y+1] = 4; }
						break;
					}
				case 4:
					{ for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y] = 'Û'; veldkleur[x+2+nX][y] = 4; }
						for(nX=0; nX<4; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 4; }
						for(nX=0; nX<2; ++nX) { veldarray[x+nX][y+2] = 'Û'; veldkleur[x+nX][y+2] = 4; }
						break;
					}
			}
			if((players==1)||(players==0))
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	array[mX][nX] = veldarray[mX][nX];
						kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			if(players==2)
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	p2array[mX][nX] = veldarray[mX][nX];
						p2kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			return 0;
		}

		case 4:		/* Hoeksteen */
		{
			switch (rotate)
			{
				case 1:
					{ for(nX=0; nX<6; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 5; }
						for(nX=0; nX<2; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 5; }
						break;
					}
				case 2:
					{ for(nX=0; nX<4; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 5; }
						for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y+1] = 'Û'; veldkleur[x+2+nX][y+1] = 5; }
						for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y+2] = 'Û'; veldkleur[x+2+nX][y+2] = 5; }
						break;
					}
				case 3:
					{ for(nX=0; nX<2; ++nX) { veldarray[x+4+nX][y] = 'Û'; veldkleur[x+4+nX][y] = 5; }
						for(nX=0; nX<6; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 5; }
						break;
					}
				case 4:
					{ for(nX=0; nX<2; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 5; }
						for(nX=0; nX<2; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 5; }
						for(nX=0; nX<4; ++nX) { veldarray[x+nX][y+2] = 'Û'; veldkleur[x+nX][y+2] = 5; }
						break;
					}
			}
			if((players==1)||(players==0))
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	array[mX][nX] = veldarray[mX][nX];
						kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			if(players==2)
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	p2array[mX][nX] = veldarray[mX][nX];
						p2kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			return 0;
		}

		case 5:		/* Hoeksteen */
		{
			switch (rotate)
			{
				case 1:
					{ for(nX=0; nX<6; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 6; }
						for(nX=0; nX<2; ++nX) { veldarray[x+4+nX][y+1] = 'Û'; veldkleur[x+4+nX][y+1] = 6; }
						break;
					}
				case 2:
					{ for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y] = 'Û'; veldkleur[x+2+nX][y] = 6; }
						for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y+1] = 'Û'; veldkleur[x+2+nX][y+1] = 6; }
						for(nX=0; nX<4; ++nX) { veldarray[x+nX][y+2] = 'Û'; veldkleur[x+nX][y+2] = 6; }
						break;
					}
				case 3:
					{ for(nX=0; nX<2; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 6; }
						for(nX=0; nX<6; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 6; }
						break;
					}
				case 4:
					{ for(nX=0; nX<4; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 6; }
						for(nX=0; nX<2; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 6; }
						for(nX=0; nX<2; ++nX) { veldarray[x+nX][y+2] = 'Û'; veldkleur[x+nX][y+2] = 6; }
						break;
					}
			}
			if((players==1)||(players==0))
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	array[mX][nX] = veldarray[mX][nX];
						kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			if(players==2)
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	p2array[mX][nX] = veldarray[mX][nX];
						p2kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			return 0;
		}

		case 6:		/* Driehoek */
		{
			switch (rotate)
			{
				case 1:
					{ for(nX=0; nX<6; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 7; }
						for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y+1] = 'Û'; veldkleur[x+2+nX][y+1] = 7; }
						break;
					}
				case 2:
					{ for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y] = 'Û'; veldkleur[x+2+nX][y] = 7; }
						for(nX=0; nX<4; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 7; }
						for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y+2] = 'Û'; veldkleur[x+2+nX][y+2] = 7; }
						break;
					}
				case 3:
					{ for(nX=0; nX<2; ++nX) { veldarray[x+2+nX][y] = 'Û'; veldkleur[x+2+nX][y] = 7; }
						for(nX=0; nX<6; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 7; }
						break;
					}
				case 4:
					{ for(nX=0; nX<2; ++nX) { veldarray[x+nX][y] = 'Û'; veldkleur[x+nX][y] = 7; }
						for(nX=0; nX<4; ++nX) { veldarray[x+nX][y+1] = 'Û'; veldkleur[x+nX][y+1] = 7; }
						for(nX=0; nX<2; ++nX) { veldarray[x+nX][y+2] = 'Û'; veldkleur[x+nX][y+2] = 7; }
						break;
					}
			}
			if((players==1)||(players==0))
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	array[mX][nX] = veldarray[mX][nX];
						kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			if(players==2)
			{	for(nX=0; nX<18; ++nX)		/* zet het bewerkte MEM terug in Global array */
				{	for(mX=0; mX<20; ++mX)
					{	p2array[mX][nX] = veldarray[mX][nX];
						p2kleur[mX][nX] = veldkleur[mX][nX];
					}
				}
			}
			return 0;
		}

}
return 0;
}

void pauze(void)
{	char waitmenu[4096];
textcolor(14);
textbackground(1);
gettext(1,1,80,25, &waitmenu);
gotoxy(25,10); cprintf("ษออออออออออออออออออออออออออออป");
gotoxy(25,11); cprintf("บ         Game paused        บ");
gotoxy(25,12); cprintf("บ                            บ");
gotoxy(25,13); cprintf("บ       Press any key...     บ");
gotoxy(25,14); cprintf("ศออออออออออออออออออออออออออออผ");
getch();
puttext(1,1,80,25, &waitmenu);
textbackground(0);
textcolor(7);
return;
}

void snorkey(void)		// Ode to Snorremans! ;-)
{	char screendump[4096], key;
	int result=0;
gettext(1,1,80,25, &screendump);
clrscr();
_setcursortype(_NORMALCURSOR);
result = system("command.com");
if (result == -1)
{	printf("ERROR loading shell: ");
	if (errno == ENOENT) printf("Command.com not found.\a");
	if (errno == ENOEXEC) printf("Command.com is not an executable.\a");
	if (errno == ENOMEM) printf("Not enough memory.\a");
	// shell kan niet gestart worden: dan maar een fake-shell... ;->
	clrscr();
	printf("Microsoft(R) Memphis\n");
	printf("   (C)Copyright Microsoft Corp 1981-1997.\n\n");
	printf("C:\\Windows>");
	while ( ((key = getch()) != 0x0D)&&(key != 0x1B)) {;} // enter of Esc

}
puttext(1,1,80,25, &screendump);
_setcursortype(_NOCURSOR);
}

void movefieldup(int player)
{	int nX,mX,kleurtje,randplaats;

if(player==1)								//verneuk scherm van player 1
{	for(mX=0; mX<17; ++mX)		//behalve de onderste regel
	{	for(nX=0; nX<20; ++nX)
		{	array[nX][mX] = array[nX][mX+1];
			kleur[nX][mX] = kleur[nX][mX+1];
		}
	}

	for(nX=0; nX<20; (nX=nX+2))		//onderste regel wordt leeg
	{ kleurtje = ((rand() / 4682)+1);	// een kleur van 1 t/m 7
		array[nX][17] = 'Û';
		array[nX+1][17] = 'Û';
		kleur[nX][17] = kleurtje;			// random kleurtje
		kleur[nX+1][17] = kleurtje;		// random kleurtje
	}
	randplaats = (rand() / 3277);
	array[randplaats*2][17] = ' '; kleur[randplaats*2][17] = 0;
	array[randplaats*2+1][17] = ' '; kleur[randplaats*2+1][17] = 0;
}

if(player==2)								//verneuk scherm van player 2
{	for(mX=0; mX<17; ++mX)		//behalve de onderste regel
	{	for(nX=0; nX<20; ++nX)
		{	p2array[nX][mX] = p2array[nX][mX+1];
			p2kleur[nX][mX] = p2kleur[nX][mX+1];
		}
	}

	for(nX=0; nX<20; (nX=nX+2))		//onderste regel wordt leeg
	{ kleurtje = ((rand() / 4682)+1);	// een kleur van 1 t/m 7
		p2array[nX][17] = 'Û';
		p2array[nX+1][17] = 'Û';
		p2kleur[nX][17] = kleurtje;
		p2kleur[nX+1][17] = kleurtje;
	}
	randplaats = (rand() / 3277);
	p2array[randplaats*2][17] = ' '; p2kleur[randplaats*2][17] = 0;
	p2array[randplaats*2+1][17] = ' '; p2kleur[randplaats*2+1][17] = 0;
}

}

void dumpscreen(int player)
{	int nX, mX, veldy=2, veldx;

if(player==1)
{ veldx=8;
	for(nX=0; nX<18; ++nX)		/* print het scherm van player 1 */
	{ gotoxy(veldx, veldy+nX+1);
		for(mX=0; mX<20; ++mX)
		{	textcolor(kleur[mX][nX]);
			cprintf("%c", array[mX][nX]);
		}
	}
}

if(player==2)
{ veldx=52;
	for(nX=0; nX<18; ++nX)		/* print het scherm van player 2 */
	{ gotoxy(veldx, veldy+nX+1);
		for(mX=0; mX<20; ++mX)
		{	textcolor(p2kleur[mX][nX]);
			cprintf("%c", p2array[mX][nX]);
		}
	}
}

}

int menu(void)
{	int screendump[4096], choice=1;
	char key;

gettext(1,1,80,25, &screendump);
textbackground(1);
textcolor(14);
_setcursortype(_NOCURSOR);
gotoxy(9,5); cprintf("ษออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออป");
gotoxy(9,6); cprintf("บ                                                            บ");
gotoxy(9,7); cprintf("บ                          Main Menu                         บ");
gotoxy(9,8); cprintf("บ                                                            บ");
gotoxy(9,9); cprintf("บ    1. Single Player (Traditional Tetris)                   บ");
gotoxy(9,10); cprintf("บ                                                            บ");
gotoxy(9,11); cprintf("บ    2. Multiplayer (Get your points as fast as possible)    บ");
gotoxy(9,12); cprintf("บ                                                            บ");
gotoxy(9,13); cprintf("บ    3. Multiplayer Death-Match (Lines go to other player)   บ");
gotoxy(9,14); cprintf("บ                                                            บ");
gotoxy(9,15); cprintf("บ    4. View Highscore                                       บ");
gotoxy(9,16); cprintf("บ                                                            บ");
gotoxy(9,17); cprintf("บ    5. Online Help                                          บ");
gotoxy(9,18); cprintf("บ                                                            บ");
gotoxy(9,19); cprintf("บ    6. Quit                                                 บ");
gotoxy(9,20); cprintf("บ                                           7. Cancel (Esc)  บ");
gotoxy(9,21); cprintf("ศออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออผ");
textcolor(4);
gotoxy(17,9); cprintf("Single Player");
for(;;) {
do
{	key = getch();
	switch (key)
	{	case 0x50: { ++choice; if(choice==8) choice=1; break; } //down
		case 0x48: { --choice; if(choice==0) choice=7; break; } //up
		default: {;}
	}
	textcolor(14);
	gotoxy(17,9); cprintf("Single Player");
	gotoxy(17,11); cprintf("Multiplayer");
	gotoxy(17,13); cprintf("Multiplayer Death-Match");
	gotoxy(17,15); cprintf("View Highscore");
	gotoxy(17,17); cprintf("Online Help");
	gotoxy(17,19); cprintf("Quit");
	gotoxy(56,20); cprintf("Cancel");
	textcolor(4);
	switch (choice)
	{	case 1: { gotoxy(17,9); cprintf("Single Player"); break; }
		case 2: { gotoxy(17,11); cprintf("Multiplayer"); break; }
		case 3: { gotoxy(17,13); cprintf("Multiplayer Death-Match"); break; }
		case 4: { gotoxy(17,15); cprintf("View Highscore"); break; }
		case 5: { gotoxy(17,17); cprintf("Online Help"); break; }
		case 6: { gotoxy(17,19); cprintf("Quit"); break; }
		case 7: { gotoxy(56,20); cprintf("Cancel"); break; }
	}
} while((key != 0x0D)&&(key != 0x1B)&&(key != '1')&&(key != '2')&&(key != '3')&&(key != '4')&&(key != '5')&&(key != '6')&&(key != '7'));
if(key == 0x1B) choice=7;
if(key == '1') choice = 1;
if(key == '2') choice = 2;
if(key == '3') choice = 3;
if(key == '4') choice = 4;
if(key == '5') choice = 5;
if(key == '6') choice = 6;
if(key == '7') choice = 7;

if((choice==5)||(choice==4))
{	if (choice==5) helpscreen();
	if (choice==4) printhiscore();
	textbackground(1);
}
else
{	textcolor(7);
	textbackground(0);
	puttext(1,1,80,25, &screendump);
	return choice;
}
}
}

void helpscreen(void)
{	int helpscreen[4096];
	char key;

gettext(1,1,80,25,&helpscreen);
textbackground(BLUE);
textcolor(WHITE);
clrscr();
cprintf("                               Online Help\n\r");
cprintf("\n\r");
cprintf(" The goal of the game is to drop the blocks and fit them into each other, but\n\r");
cprintf(" you already knew that.\n\r");
cprintf(" In "); textcolor(YELLOW); cprintf("Single Player"); textcolor(WHITE); cprintf(" mode, there's nothing more to it than just score points and\n\r");
cprintf(" try to make it into the high score. The more lines you form at once, the\n\r");
cprintf(" more points you earn: 1 line  -- 10 points\n\r");
cprintf("                       2 lines -- 30 points\n\r");
cprintf("                       3 lines -- 60 points\n\r");
cprintf("                       4 lines -- 100 points\n\r");
cprintf("\n\r");
cprintf(" 2nd game-option is "); textcolor(YELLOW); cprintf("Multiplayer"); textcolor(WHITE); cprintf(". You play in splitscreen mode until one player\n\r");
cprintf(" dies. The one with the highest score wins.\n\r");
cprintf(" When playing in "); textcolor(YELLOW); cprintf("Death-Match"); textcolor(WHITE); cprintf(" mode, every line you form is added at the bottom\n\r");
cprintf(" of your opponent's screen. Just try to survive longer than him. If you die,\n\r");
cprintf(" you lose.\n\n\r");
cprintf(" Controls:     Single Player or       ณ  Player I in multiplayer\n\r");
cprintf("            player II in multiplayer  ณ\n\r");
cprintf("            ฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤลฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤ\n\r");
cprintf("            cursor keys + spacebar    ณ  a,s,d,w + tab to drop\n\r");
cprintf("\n\r");
cprintf(" More info is in the README.TXT\n\r");
cprintf("                                                                        (Esc)");
textbackground(0);
textcolor(7);
do
{ key=getch();
} while (key != 0x1B);
puttext(1,1,80,25,&helpscreen);
}

void printrank(int p1score, int p2score)
{ int regel;
	char best[21], worst[21];

regel = (rand() / 4682);
switch (regel)
{	case 0: {strcpy(best, "      MESSIAH      "); break;}
	case 1: {strcpy(best, "     THE BEST!     "); break;}
	case 2: {strcpy(best, " MASTER OF THE GAME"); break; }
	case 3: {strcpy(best, "      NUMBER 1     "); break; }
	case 4: {strcpy(best, "    GOD HIMSELF    "); break; }
	case 5: {strcpy(best, "      SUPERMAN     "); break; }
	case 6: {strcpy(best, "     SUPERHERO     "); break; }
}
regel = (rand() / 4682);
switch (regel)
{	case 0: {strcpy(worst, "       weener      "); break;}
	case 1: {strcpy(worst, "     don't cry!    "); break; }
	case 2: {strcpy(worst, "  one big loooser  "); break; }
	case 3: {strcpy(worst, "   does it hurt?   "); break; }
	case 4: {strcpy(worst, "push those buttons!"); break; }
	case 5: {strcpy(worst, "    ZZZzzzzzz...   "); break; }
	case 6: {strcpy(worst, "   ri ra rollade!  "); break; }
}

if (p1score > p2score)
{	textcolor(2);  	//Groen voor beste player
	textbackground(0);	//Achtergrond gewoon zwart
	gotoxy(8,22); cprintf("%s", best);
	textcolor(4);
	gotoxy(52,22); cprintf("%s", worst);
}

if (p2score > p1score)
{	textcolor(2);  	//Groen voor beste player
	gotoxy(52,22); cprintf("%s", best);
	textcolor(4);
	gotoxy(8,22); cprintf("%s", worst);
}

if (p1score == p2score)
{	textcolor(7);
	gotoxy(52,22); cprintf("     PLAYER II     ");
	gotoxy(8,22); cprintf("      PLAYER I      ");
}
textcolor(7);
}

void printshit(int rank)
{	char place[10];
if (rank == 1) strcpy(place, "FIRST!");
if (rank == 2) strcpy(place, "SECOND!");
if (rank == 3) strcpy(place, "THIRD!");
if (rank == 4) strcpy(place, "FOURTH!");
if (rank == 5) strcpy(place, "FIFTH!");
if (rank == 6) strcpy(place, "SIXTH!");
if (rank == 7) strcpy(place, "SEVENTH!");
if (rank == 8) strcpy(place, "EIGHTH!");
if (rank == 9) strcpy(place, "NINTH!");
if (rank == 10) strcpy(place, "TENTH!");
textcolor(14);
textbackground(1);
gotoxy(19,10); cprintf("ษออออออออออออออออออออออออออออออออออออออออป");
gotoxy(19,11); cprintf("บ    Congratulations, you're %-10s  บ", place);
gotoxy(19,12); cprintf("บ            Enter your name!            บ");
gotoxy(19,13); cprintf("บ     (no spaces, max 15 characters)     บ");
gotoxy(19,14); cprintf("บ   Your name:                           บ");
gotoxy(19,15); cprintf("ศออออออออออออออออออออออออออออออออออออออออผ");
_setcursortype(_NORMALCURSOR);
gotoxy(34,14);
return;
}

void hiscoreedit(int score, int lines)		// Pointerfeest
{ int screendump[4096], count;
	char name[MAXNAME+1], key;
	FILE *hiscorefile;		// pointer naar de HiScore file
	hiscore player1, player2, player3, player4, player5, player6, player7;
	hiscore player8, player9, player10;

gettext(1,1,80,25,&screendump);	//ff screen grabben, aan eind weer terugzetten
if ( (hiscorefile = fopen("HISCORE.ICE", "r")) == NULL)
{	strcpy (player1.playernaam, "Icehawk");
	strcpy (player2.playernaam, "Hellraid");
	strcpy (player3.playernaam, "Myth");
	strcpy (player4.playernaam, "proudly");
	strcpy (player5.playernaam, "present:");
	strcpy (player6.playernaam, "-=TETRIS=-");
	strcpy (player7.playernaam, "Coded_in_C++");
	strcpy (player8.playernaam, "20/05/97");
	strcpy (player9.playernaam, "readme.txt");
	strcpy (player10.playernaam, "for_info");
	player1.points = 100;
	player2.points = 90;
	player3.points = 80;
	player4.points = 70;
	player5.points = 60;
	player6.points = 50;
	player7.points = 40;
	player8.points = 30;
	player9.points = 20;
	player10.points = 10;
	player1.lines = 1;
	player2.lines = 1;
	player3.lines = 1;
	player4.lines = 1;
	player5.lines = 1;
	player6.lines = 1;
	player7.lines = 1;
	player8.lines = 1;
	player9.lines = 1;
	player10.lines = 1;
}
else
{
fscanf(hiscorefile, "%4d%15s%3d", &player1.points, &player1.playernaam, &player1.lines);
fscanf(hiscorefile, "%4d%15s%3d", &player2.points, &player2.playernaam, &player2.lines);
fscanf(hiscorefile, "%4d%15s%3d", &player3.points, &player3.playernaam, &player3.lines);
fscanf(hiscorefile, "%4d%15s%3d", &player4.points, &player4.playernaam, &player4.lines);
fscanf(hiscorefile, "%4d%15s%3d", &player5.points, &player5.playernaam, &player5.lines);
fscanf(hiscorefile, "%4d%15s%3d", &player6.points, &player6.playernaam, &player6.lines);
fscanf(hiscorefile, "%4d%15s%3d", &player7.points, &player7.playernaam, &player7.lines);
fscanf(hiscorefile, "%4d%15s%3d", &player8.points, &player8.playernaam, &player8.lines);
fscanf(hiscorefile, "%4d%15s%3d", &player9.points, &player9.playernaam, &player9.lines);
fscanf(hiscorefile, "%4d%15s%3d", &player10.points, &player10.playernaam, &player10.lines);
}
fclose(hiscorefile);	// meteen sluiten voor evt. multitasking

strcpy (name, " ");
if (score > player10.points)
{	if (score > player9.points)
	{	if (score > player8.points)
		{	if (score > player7.points)
			{	if (score > player6.points)
				{	if (score > player5.points)
					{	if (score > player4.points)
						{	if (score > player3.points)
							{	if (score > player2.points)
								{	if (score > player1.points)
									{ printshit(1);
										count=0;
										while (( (key=getch()) != 0x0D)&&(count != 15))
										{ if ((key != ' ')&&(key != 0x08)) { cprintf("%c", key); name[count] = key; ++count; }
											if ((key == 0x08)&&(count>0)) { cprintf("%c %c",key, key); --count; }
										}
										name[count] = '\0';
										puttext(1,1,80,25,&screendump);
										player10.points = player9.points;
										player9.points = player8.points;
										player8.points = player7.points;
										player7.points = player6.points;
										player6.points = player5.points;
										player5.points = player4.points;
										player4.points = player3.points;
										player3.points = player2.points;
										player2.points = player1.points;
										player1.points = score;
										player10.lines = player9.lines;
										player9.lines = player8.lines;
										player8.lines = player7.lines;
										player7.lines = player6.lines;
										player6.lines = player5.lines;
										player5.lines = player4.lines;
										player4.lines = player3.lines;
										player3.lines = player2.lines;
										player2.lines = player1.lines;
										player1.lines = lines;
										strcpy(player10.playernaam, player9.playernaam);
										strcpy(player9.playernaam, player8.playernaam);
										strcpy(player8.playernaam, player7.playernaam);
										strcpy(player7.playernaam, player6.playernaam);
										strcpy(player6.playernaam, player5.playernaam);
										strcpy(player5.playernaam, player4.playernaam);
										strcpy(player4.playernaam, player3.playernaam);
										strcpy(player3.playernaam, player2.playernaam);
										strcpy(player2.playernaam, player1.playernaam);
										strcpy(player1.playernaam, name);
									}
									else
									{ printshit(2);
										count=0;
										while (( (key=getch()) != 0x0D)&&(count != 15))
										{ if ((key != ' ')&&(key != 0x08))	{ cprintf("%c", key); name[count] = key; ++count; }
											if ((key == 0x08)&&(count>0)) { cprintf("%c %c",key, key); --count; }
										}
										name[count] = '\0';
										puttext(1,1,80,25,&screendump);
										player10.points = player9.points;
										player9.points = player8.points;
										player8.points = player7.points;
										player7.points = player6.points;
										player6.points = player5.points;
										player5.points = player4.points;
										player4.points = player3.points;
										player3.points = player2.points;
										player2.points = score;
										player10.lines = player9.lines;
										player9.lines = player8.lines;
										player8.lines = player7.lines;
										player7.lines = player6.lines;
										player6.lines = player5.lines;
										player5.lines = player4.lines;
										player4.lines = player3.lines;
										player3.lines = player2.lines;
										player2.lines = lines;
										strcpy(player10.playernaam, player9.playernaam);
										strcpy(player9.playernaam, player8.playernaam);
										strcpy(player8.playernaam, player7.playernaam);
										strcpy(player7.playernaam, player6.playernaam);
										strcpy(player6.playernaam, player5.playernaam);
										strcpy(player5.playernaam, player4.playernaam);
										strcpy(player4.playernaam, player3.playernaam);
										strcpy(player3.playernaam, player2.playernaam);
										strcpy(player2.playernaam, name);
									}
								}
								else
								{ printshit(3);
									count=0;
									while (( (key=getch()) != 0x0D)&&(count != 15))
									{ if ((key != ' ')&&(key != 0x08))	{ cprintf("%c", key); name[count] = key; ++count; }
										if ((key == 0x08)&&(count>0)) { cprintf("%c %c",key, key); --count; }
									}
									name[count] = '\0';
									puttext(1,1,80,25,&screendump);
									player10.points = player9.points;
									player9.points = player8.points;
									player8.points = player7.points;
									player7.points = player6.points;
									player6.points = player5.points;
									player5.points = player4.points;
									player4.points = player3.points;
									player3.points = score;
									player10.lines = player9.lines;
									player9.lines = player8.lines;
									player8.lines = player7.lines;
									player7.lines = player6.lines;
									player6.lines = player5.lines;
									player5.lines = player4.lines;
									player4.lines = player3.lines;
									player3.lines = lines;
									strcpy(player10.playernaam, player9.playernaam);
									strcpy(player9.playernaam, player8.playernaam);
									strcpy(player8.playernaam, player7.playernaam);
									strcpy(player7.playernaam, player6.playernaam);
									strcpy(player6.playernaam, player5.playernaam);
									strcpy(player5.playernaam, player4.playernaam);
									strcpy(player4.playernaam, player3.playernaam);
									strcpy(player3.playernaam, name);
								}
							}
							else
							{ printshit(4);
								count=0;
								while (( (key=getch()) != 0x0D)&&(count != 15))
								{ if ((key != ' ')&&(key != 0x08))	{ cprintf("%c", key); name[count] = key; ++count; }
									if ((key == 0x08)&&(count>0)) { cprintf("%c %c",key, key); --count; }
								}
								name[count] = '\0';
								puttext(1,1,80,25,&screendump);
								player10.points = player9.points;
								player9.points = player8.points;
								player8.points = player7.points;
								player7.points = player6.points;
								player6.points = player5.points;
								player5.points = player4.points;
								player4.points = score;
								player10.lines = player9.lines;
								player9.lines = player8.lines;
								player8.lines = player7.lines;
								player7.lines = player6.lines;
								player6.lines = player5.lines;
								player5.lines = player4.lines;
								player4.lines = lines;
								strcpy(player10.playernaam, player9.playernaam);
								strcpy(player9.playernaam, player8.playernaam);
								strcpy(player8.playernaam, player7.playernaam);
								strcpy(player7.playernaam, player6.playernaam);
								strcpy(player6.playernaam, player5.playernaam);
								strcpy(player5.playernaam, player4.playernaam);
								strcpy(player4.playernaam, name);
							}
						}
						else
						{ printshit(5);
							count=0;
							while (( (key=getch()) != 0x0D)&&(count != 15))
							{ if ((key != ' ')&&(key != 0x08))	{ cprintf("%c", key); name[count] = key; ++count; }
								if ((key == 0x08)&&(count>0)) { cprintf("%c %c",key, key); --count; }
							}
							name[count] = '\0';
							puttext(1,1,80,25,&screendump);
							player10.points = player9.points;
							player9.points = player8.points;
							player8.points = player7.points;
							player7.points = player6.points;
							player6.points = player5.points;
							player5.points = score;
							player10.lines = player9.lines;
							player9.lines = player8.lines;
							player8.lines = player7.lines;
							player7.lines = player6.lines;
							player6.lines = player5.lines;
							player5.lines = lines;
							strcpy(player10.playernaam, player9.playernaam);
							strcpy(player9.playernaam, player8.playernaam);
							strcpy(player8.playernaam, player7.playernaam);
							strcpy(player7.playernaam, player6.playernaam);
							strcpy(player6.playernaam, player5.playernaam);
							strcpy(player5.playernaam, name);
						}
					}
					else
					{ printshit(6);
						count=0;
						while (( (key=getch()) != 0x0D)&&(count != 15))
						{ if ((key != ' ')&&(key != 0x08))	{ cprintf("%c", key); name[count] = key; ++count; }
							if ((key == 0x08)&&(count>0)) { cprintf("%c %c",key, key); --count; }
						}
						name[count] = '\0';
						puttext(1,1,80,25,&screendump);
						player10.points = player9.points;
						player9.points = player8.points;
						player8.points = player7.points;
						player7.points = player6.points;
						player6.points = score;
						player10.lines = player9.lines;
						player9.lines = player8.lines;
						player8.lines = player7.lines;
						player7.lines = player6.lines;
						player6.lines = lines;
						strcpy(player10.playernaam, player9.playernaam);
						strcpy(player9.playernaam, player8.playernaam);
						strcpy(player8.playernaam, player7.playernaam);
						strcpy(player7.playernaam, player6.playernaam);
						strcpy(player6.playernaam, name);
					}
				}
				else
				{ printshit(7);
					count=0;
					while (( (key=getch()) != 0x0D)&&(count != 15))
					{ if ((key != ' ')&&(key != 0x08))	{ cprintf("%c", key); name[count] = key; ++count; }
						if ((key == 0x08)&&(count>0)) { cprintf("%c %c",key, key); --count; }
					}
					name[count] = '\0';
					puttext(1,1,80,25,&screendump);
					player10.points = player9.points;
					player9.points = player8.points;
					player8.points = player7.points;
					player7.points = score;
					player10.lines = player9.lines;
					player9.lines = player8.lines;
					player8.lines = player7.lines;
					player7.lines = lines;
					strcpy(player10.playernaam, player9.playernaam);
					strcpy(player9.playernaam, player8.playernaam);
					strcpy(player8.playernaam, player7.playernaam);
					strcpy(player7.playernaam, name);
				}
			}
			else
			{ printshit(8);
				count=0;
				while (( (key=getch()) != 0x0D)&&(count != 15))
				{ if ((key != ' ')&&(key != 0x08))	{ cprintf("%c", key); name[count] = key; ++count; }
					if ((key == 0x08)&&(count>0)) { cprintf("%c %c",key, key); --count; }
				}
				name[count] = '\0';
				puttext(1,1,80,25,&screendump);
				player10.points = player9.points;
				player9.points = player8.points;
				player8.points = score;
				player10.lines = player9.lines;
				player9.lines = player8.lines;
				player8.lines = lines;
				strcpy(player10.playernaam, player9.playernaam);
				strcpy(player9.playernaam, player8.playernaam);
				strcpy(player8.playernaam, name);
			}
		}
		else
		{ printshit(9);
			count=0;
			while (( (key=getch()) != 0x0D)&&(count != 15))
			{ if ((key != ' ')&&(key != 0x08))	{ cprintf("%c", key); name[count] = key; ++count; }
				if ((key == 0x08)&&(count>0)) { cprintf("%c %c",key, key); --count; }
			}
			name[count] = '\0';
			puttext(1,1,80,25,&screendump);
			player10.points = player9.points;
			player9.points = score;
			player10.lines = player9.lines;
			player9.lines = lines;
			strcpy(player10.playernaam, player9.playernaam);
			strcpy(player9.playernaam, name);
		}
	}
	else
	{ printshit(10);
		count=0;
		while (( (key=getch()) != 0x0D)&&(count != 15))
		{ if ((key != ' ')&&(key != 0x08))	{ cprintf("%c", key); name[count] = key; ++count; }
			if ((key == 0x08)&&(count>0)) { cprintf("%c %c",key, key); --count; }
		}
		name[count] = '\0';
		puttext(1,1,80,25,&screendump);
		player10.points = score;
		player10.lines = lines;
		strcpy(player10.playernaam, name);
	}
}
textcolor(7);
//cprintf("p1naam= %s - p1points= %d - lines= %d - p2points= %d", player1.playernaam, player1.points, player1.lines, player2.points); //testregel

	// Nieuwe High Score tabel goed in memory nu. Nog wegschrijven...
hiscorefile = fopen("HISCORE.ICE", "w");
fprintf(hiscorefile, "%d %s %d ", player1.points, player1.playernaam, player1.lines);
fprintf(hiscorefile, "%d %s %d ", player2.points, player2.playernaam, player2.lines);
fprintf(hiscorefile, "%d %s %d ", player3.points, player3.playernaam, player3.lines);
fprintf(hiscorefile, "%d %s %d ", player4.points, player4.playernaam, player4.lines);
fprintf(hiscorefile, "%d %s %d ", player5.points, player5.playernaam, player5.lines);
fprintf(hiscorefile, "%d %s %d ", player6.points, player6.playernaam, player6.lines);
fprintf(hiscorefile, "%d %s %d ", player7.points, player7.playernaam, player7.lines);
fprintf(hiscorefile, "%d %s %d ", player8.points, player8.playernaam, player8.lines);
fprintf(hiscorefile, "%d %s %d ", player9.points, player9.playernaam, player9.lines);
fprintf(hiscorefile, "%d %s %d ", player10.points, player10.playernaam, player10.lines);
fclose(hiscorefile);	// meteen sluiten voor evt. multitasking
printhiscore();
textbackground(0);
return;
}

void printhiscore(void)
{ int screendump[4096];
	FILE *hiscorefile;		// pointer naar de HiScore file
	hiscore player1, player2, player3, player4, player5, player6, player7;
	hiscore player8, player9, player10;

gettext(1,1,80,25,&screendump);	//ff screen grabben, aan eind weer terugzetten

if ( (hiscorefile = fopen("HISCORE.ICE", "r")) == NULL)
{	strcpy (player1.playernaam, "Icehawk");
	strcpy (player2.playernaam, "Hellraid");
	strcpy (player3.playernaam, "Myth");
	strcpy (player4.playernaam, "proudly");
	strcpy (player5.playernaam, "present:");
	strcpy (player6.playernaam, "-=TETRIS=-");
	strcpy (player7.playernaam, "Coded_in_C++");
	strcpy (player8.playernaam, "20/05/97");
	strcpy (player9.playernaam, "readme.txt");
	strcpy (player10.playernaam, "for_info");
	player1.points = 100;
	player2.points = 90;
	player3.points = 80;
	player4.points = 70;
	player5.points = 60;
	player6.points = 50;
	player7.points = 40;
	player8.points = 30;
	player9.points = 20;
	player10.points = 10;
	player1.lines = 1;
	player2.lines = 1;
	player3.lines = 1;
	player4.lines = 1;
	player5.lines = 1;
	player6.lines = 1;
	player7.lines = 1;
	player8.lines = 1;
	player9.lines = 1;
	player10.lines = 1;
}
else
{
fscanf(hiscorefile, "%4d %15s %3d", &player1.points, &player1.playernaam, &player1.lines);
fscanf(hiscorefile, "%4d %15s %3d", &player2.points, &player2.playernaam, &player2.lines);
fscanf(hiscorefile, "%4d %15s %3d", &player3.points, &player3.playernaam, &player3.lines);
fscanf(hiscorefile, "%4d %15s %3d", &player4.points, &player4.playernaam, &player4.lines);
fscanf(hiscorefile, "%4d %15s %3d", &player5.points, &player5.playernaam, &player5.lines);
fscanf(hiscorefile, "%4d %15s %3d", &player6.points, &player6.playernaam, &player6.lines);
fscanf(hiscorefile, "%4d %15s %3d", &player7.points, &player7.playernaam, &player7.lines);
fscanf(hiscorefile, "%4d %15s %3d", &player8.points, &player8.playernaam, &player8.lines);
fscanf(hiscorefile, "%4d %15s %3d", &player9.points, &player9.playernaam, &player9.lines);
fscanf(hiscorefile, "%4d %15s %3d", &player10.points, &player10.playernaam, &player10.lines);
fclose(hiscorefile);	// meteen sluiten voor evt. multitasking
}
// nu nog gaan printen:
textbackground(BLUE);
textcolor(YELLOW);
_setcursortype(_NOCURSOR);
gotoxy(17,4);  cprintf("ษออออออออออออออออออTop  Tenออออออออออออออออออป");
gotoxy(17,5);  cprintf("บ                                            บ");
gotoxy(17,6);  cprintf("บ         Playername       Lines  Score      บ");
gotoxy(17,7);  cprintf("บ   ฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤฤ   บ");
gotoxy(17,8);  cprintf("บ     1   %-15s   %3d   %4d       บ", player1.playernaam, player1.lines, player1.points);
gotoxy(17,9);  cprintf("บ     2   %-15s   %3d   %4d       บ", player2.playernaam, player2.lines, player2.points);
gotoxy(17,10); cprintf("บ     3   %-15s   %3d   %4d       บ", player3.playernaam, player3.lines, player3.points);
gotoxy(17,11); cprintf("บ     4   %-15s   %3d   %4d       บ", player4.playernaam, player4.lines, player4.points);
gotoxy(17,12); cprintf("บ     5   %-15s   %3d   %4d       บ", player5.playernaam, player5.lines, player5.points);
gotoxy(17,13); cprintf("บ     6   %-15s   %3d   %4d       บ", player6.playernaam, player6.lines, player6.points);
gotoxy(17,14); cprintf("บ     7   %-15s   %3d   %4d       บ", player7.playernaam, player7.lines, player7.points);
gotoxy(17,15); cprintf("บ     8   %-15s   %3d   %4d       บ", player8.playernaam, player8.lines, player8.points);
gotoxy(17,16); cprintf("บ     9   %-15s   %3d   %4d       บ", player9.playernaam, player9.lines, player9.points);
gotoxy(17,17); cprintf("บ     10  %-15s   %3d   %4d       บ", player10.playernaam, player10.lines, player10.points);
gotoxy(17,18); cprintf("ศออออออออออออออออออออออออออออออออออออออออออออผ");
while ((getch()) != 0x0D) {;}	// zolang er geen enter gedrukt wordt loop
puttext(1,1,80,25,&screendump);
return;
}

void printwinner(int winner, int mode, int p1score, int p2score)
{ int screendump[4096];
	char key;

gettext(1,1,80,25,&screendump);		// screen grabben
if ((winner == 1)&&(mode == 3)||(p1score > p2score)&&(mode == 2))  // als player 1 gewonnen heeft:
{ textcolor(2);					// groen
	textbackground(0);		// zwart
	gotoxy(8,10); cprintf("ษออออออออออออออออออป");
	gotoxy(8,11); cprintf("บ                  บ");
	gotoxy(8,12); cprintf("บ     YOU WIN      บ");
	gotoxy(8,13); cprintf("บ                  บ");
	gotoxy(8,14); cprintf("ศออออออออออออออออออผ");
	textcolor(RED);				// rood
	gotoxy(52,10); cprintf("ษออออออออออออออออออป");
	gotoxy(52,11); cprintf("บ                  บ");
	gotoxy(52,12); cprintf("บ   you looozzz    บ");
	gotoxy(52,13); cprintf("บ                  บ");
	gotoxy(52,14); cprintf("ศออออออออออออออออออผ");
	for (;;)
	{	key = getch();   						// Press a key
		if ((key == 0x0D)||(key == 0x1B))
		{	textbackground(0);
			puttext(1,1,80,25,&screendump);
			return;
		}	// alleen als dat enter was:
	}
}

if ((winner == 2)&&(mode == 3)||(p2score > p1score)&&(mode == 2))		// als player 2 gewonnen heeft:
{ textcolor(2);					// groen
	textbackground(0);		// zwart
	gotoxy(52,10); cprintf("ษออออออออออออออออออป");
	gotoxy(52,11); cprintf("บ                  บ");
	gotoxy(52,12); cprintf("บ     YOU WIN      บ");
	gotoxy(52,13); cprintf("บ                  บ");
	gotoxy(52,14); cprintf("ศออออออออออออออออออผ");
	textcolor(RED);				// rood
	gotoxy(8,10); cprintf("ษออออออออออออออออออป");
	gotoxy(8,11); cprintf("บ                  บ");
	gotoxy(8,12); cprintf("บ   you looozzz    บ");
	gotoxy(8,13); cprintf("บ                  บ");
	gotoxy(8,14); cprintf("ศออออออออออออออออออผ");
	for (;;)
	{	key = getch();   						// Press a key
		if ((key == 0x0D)||(key == 0x1B))
		{	textbackground(0);
			puttext(1,1,80,25,&screendump);
			return;
		}	// alleen als dat enter was:
	}
}

if ((p1score==p2score)&&(mode == 2))		// bij gelijke stand:
{ textcolor(7);					// groen
	textbackground(0);		// zwart
	gotoxy(52,10); cprintf("ษออออออออออออออออออป");
	gotoxy(52,11); cprintf("บ                  บ");
	gotoxy(52,12); cprintf("บ    no winner     บ");
	gotoxy(52,13); cprintf("บ                  บ");
	gotoxy(52,14); cprintf("ศออออออออออออออออออผ");
	gotoxy(8,10); cprintf("ษออออออออออออออออออป");
	gotoxy(8,11); cprintf("บ                  บ");
	gotoxy(8,12); cprintf("บ    no winner     บ");
	gotoxy(8,13); cprintf("บ                  บ");
	gotoxy(8,14); cprintf("ศออออออออออออออออออผ");
	for (;;)
	{	key = getch();   						// Press a key
		if ((key == 0x0D)||(key == 0x1B))
		{	textbackground(0);
			puttext(1,1,80,25,&screendump);
			return;
		}	// alleen als dat enter was:
	}
}
}	// Einde broncode