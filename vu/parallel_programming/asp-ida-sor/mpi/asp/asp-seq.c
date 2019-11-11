/*
 * asp.c:
 * 	All-pairs shortest path implementation based on Floyd's
 * 	algorithms.
 *
 *      Sequential version.
 */

#include "stdio.h"
#include "stdlib.h"

/******************** ASP stuff *************************/


#define MAX_DISTANCE 42

/* malloc and initialize the table with some random distances       */
/* we never use srand() so rand() weill always use the same seed    */
/* and will hence yields reproducible results (for timing purposes) */

void init_tab(int n, int ***tabptr)
{
    int **tab;
    int i, j;

    tab = (int **)malloc(n * sizeof(int *));
    if (tab == (int **)0) {
        fprintf(stderr,"cannot malloc distance table\n");
        exit (42);
    }

    for (i = 0; i < n; i++) {
      tab[i]    = (int *)malloc(n * sizeof(int));
      if (tab[i] == (int *)0) {
	fprintf(stderr,"cannot malloc distance table\n");
	exit (42);
      }
      for (j = 0; j < n; j++) {
	tab[i][j] = (i == j ? 0 : 
		     1+(int) ((double)MAX_DISTANCE*rand()/(RAND_MAX+1.0)));
      }
    }

    *tabptr    = tab;
}

void
free_tab(int **tab, int n)
{
    int i;
    
    for (i = 0; i < n; i++) {
	free(tab[i]);
    }
    free(tab);
}




static void
do_asp(int **tab, int n)
{
    int i, j, k, tmp;

    for (k = 0; k < n; k++) {
        for (i = 0; i < n; i++) {
	    if (i != k) {
		for (j = 0; j < n; j++) {
		    tmp = tab[i][k] + tab[k][j];
		    if (tmp < tab[i][j]) {
			tab[i][j] = tmp;
		    }
		}
            }
        }
    }
}

/******************** Main program *************************/

int
main(int argc, char **argv)
{
    int n, lb, ub;
    int **tab;

    n = 0;
    if ( argc > 1 )
      n = atoi(argv[1]);
    if (n == 0)
      n = 4000;

    printf("Running asp with %d rows\n", n);

    lb = 0;    /* lower bound for rows to be computed */
    ub = n;  /* upper bound for rows to be computed */

    init_tab(n, &tab);

    do_asp(tab, n);

    free_tab(tab, n);

    return 0;
}



