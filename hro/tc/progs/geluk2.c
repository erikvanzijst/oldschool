#include <stdio.h>

void main ()
{
	int nX, tel3=1, tel7=1, tel9=1, geluk[1000];
	long A;

for (A=0; A<1000; ++A)
	geluk[A] = 0;

for (A=1; A<1000; (A=A+2))
{	if (tel3 != 3)
	{	if (tel7 != 7)
		{	if (tel9 != 9)
			{	(geluk[nX] = A);
				printf (" %d", geluk[nX]);
				++nX;
				++tel9;
			}
			else (tel9 = 1);
		++tel7; }
		else (tel7 = 1);
	++tel3; }
	else (tel3 = 1);
}
}