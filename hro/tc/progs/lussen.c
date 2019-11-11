#include <stdio.h>
#define MAX 8

void ToonTabel ( int tabel [MAX] );
void FunctieA ();

int main ()
{
	int tabelF [MAX];
	int tabelW [MAX];
	int tabelD [MAX];
	int nX;
	int index;

clrscr ();
for (nX=0; nX<MAX; ++nX)
	tabelF[nX] = 1;

ToonTabel (tabelF);

nX = 0;
while (nX<MAX) {
	tabelW[nX] = 2;
	++nX; }

ToonTabel (tabelW);

nX = 0;
do {
	tabelD[nX] = 3;
	++nX; }
while (nX < (MAX));

ToonTabel (tabelD);

printf ("\nGeef index: ");
scanf ("%d", &index);
if (index < 0)
index = 0;

for (nX=index; nX<MAX; ++nX)
	tabelF[nX] = 4;

nX = index;
while (nX < MAX) {
	tabelW[nX] = 5;
	++nX; }

nX = index;
if (nX >= MAX) {
	do {
		tabelD[nX] = 6;
		++nX; }
	while (nX < MAX);
	}

ToonTabel (tabelF);
ToonTabel (tabelW);
ToonTabel (tabelD);

FunctieA ();
return 0;
}

void ToonTabel ( int tabel [MAX] ) {
	int i;
	printf ("\n");
	for (i=0; i<MAX; ++i)
		printf ("%d\t", tabel[i]); }

void FunctieA () {
	int i;
	printf ("\nÉ");
	for (i=0; i<48; ++i)
		printf ("Í");
	printf ("»\n");
	printf ("º X * Y |	1	2	4	5 º\nÇ");
	for (i=0; i<48; ++i)
		printf ("Ä");
	printf ("¶\nº");
	printf (" %d\t |\t%d\t%d\t%d\t%d\t%d\t", a,b,c,d,e,f
}