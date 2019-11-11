#include <stdio.h>
#include <time.h>
main ()
{

	clock_t start, stop;
	int tijd;
	/*double difftime(time_t start, time_t stop)*/

start = clock();
printf ("\n\nStart: %.2f", (start/ CLK_TCK));
getch();
stop = clock();
printf ("\neind: %.0f", (stop/ CLK_TCK));
tijd = ((stop - start)/ CLK_TCK);
printf ("\nverschil: %d", tijd);
printf ("\n\nTijd: %.2f", (stop - start)/ CLK_TCK);
}