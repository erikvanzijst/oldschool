/*      life.c          Erik van Zijst           1997    */

/* Simulation of Conway's game of Life on a bounded grid


Pre:  The user must supply an initial configuration of living cells.
Post: The program prints a sequence of maps showing the changes in
    the configuration of living cells according to the rules for the
    game of Life.
Uses: functions Initialize, WriteMap, NeighborCount, and UserSaysYes
 */

#include    "common.h"   /* common include files and definitions */
#include    "life.h"     /* Life's defines, typedefs, and prototypes */

void main(void)
{
    int  row, col;
    Grid map;                       /* current generation   */
    Grid newmap;                    /* next generation      */

    Initialize(map);
    WriteMap(map);
    printf("This is the initial configuration you have chosen.\n"
           "Press <Enter> to continue.\n");
    while(getchar() != '\n')
        ;
    do {
        for (row = 1; row <= MAXROW;    row++)
            for (col = 1; col <= MAXCOL; col++)
                switch(NeighborCount(map, row, col)) {
                case 0:
                case 1:
                    newmap[row][col]= DEAD;
                    break;
                case 2:
                    newmap[row][col]= map[row][col];
                    break;
                case 3:
                    newmap[row][col]= ALIVE;
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    newmap[row][col]= DEAD;
                    break;
                }
        CopyMap(map, newmap);
        WriteMap(map);
        printf("Do you wish to continue viewing the new generations");
    } while (UserSaysYes());
}


/* NeighborCount:  count neighbors of row,col.
Pre:   The pair row, col is a valid cell in a Life configuration.
Post: The function returns the number of living neighbors of the living cell.
*/
int NeighborCount(Grid map, int row, int col)
{
    int i;              /* row of a neighbor of the cell (row,col)  */
    int j;              /* column of a neighbor of the cell (row,col)   */
    int count = 0;      /* counter of living neighbors                  */
    for (i = row - 1; i <= row + 1; i++)
        for (j = col - 1; j <= col + 1; j++)
            if (map[i][j] == ALIVE)
                count++;
    if (map[row][col] == ALIVE)
        count--;
    return count;
}


/* Initialize:  initialize grid map.
Pre:   None.
Post: All the cells in the grid map have been set to
    initial configuration of living cells.
*/
void Initialize(Grid map)
{
    int row, col;               /* coordinates of a cell    */
    printf("This program is a simulation of the game of Life.\n"
           "The grid has a size of %d rows and "
           " %d columns.\n", MAXROW, MAXCOL);
    for (row =  0; row <= MAXROW + 1; row++)
        for (col = 0; col <= MAXCOL + 1; col++)
            map[row][col] = DEAD; /* Set all cells empty, including the hedge. */
    printf("On each line give a pair of coordinates for a living cell.\n"
           "Terminate the list with the special pair 0 0.\n");
    scanf("%d %d", &row, &col);
    while (row != 0 || col != 0) {  /* Check termination condition. */
        if (row >= 1 && row <= MAXROW && col >= 1 && col <= MAXCOL)
            map[row][col] = ALIVE;
        else
            printf("Values are not within range.\n");
        scanf("%d %d", &row, &col);
    }
    while (getchar() != '\n')       /* Discard remaining characters. */
        ;
}


/* WriteMap:  display grid map.
Pre:   The rectangular array  map contains the current Life configuration.
Post: The current Life configuration is written for the user.
*/
void WriteMap(Grid map)
{
    int row, col;

    putchar('\n');
        printf ("   ");
    for (col = 1; col <= MAXCOL; col++)
                printf ("%d", col%10); /* ter identificatie */
    putchar('\n');

    for (row = 1; row <= MAXROW; row++)
        {
                printf ("%c  ", row + 'A' - 1); /* ter identificatie */
        for (col = 1; col <= MAXCOL; col++)
            if (map[row][col] == ALIVE)
                    putchar('*');
            else
                    putchar('-');
                printf ("  %c", row + 'A' - 1); /* ter identificatie */
        putchar('\n');
    } 
        putchar('\n');

}


/* CopyMap:  copy newmap into map.
Pre:   The grid newmap has the current Life configuration.
Post: The grid map has a copy of newmap.
*/
void CopyMap(Grid map, Grid newmap)
{
    int row, col;

    for (row = 0; row <= MAXROW + 1; row++)
        for (col = 0; col <= MAXCOL + 1; col++)
            map[row][col] = newmap[row][col];
}


/* UserSaysYes:  TRUE if the user wants to continue execution.
Pre:   None.
Post: Returns TRUE if the user's answer begins with either y or Y,
    FALSE if the user responds with any response beginning with either
    n or .
*/
Boolean UserSaysYes(void)
{
     int  c;

    printf(" (y,n)? ");
    do {
        while ((c = getchar()) == '\n')
            ;           /* Ignore new line character.   */
        if (c == 'y' || c == 'Y' || c == 'n' || c == 'N')
            return (c == 'y' || c == 'Y');
        printf("Please respond by typing one of the letters y or n\n");
    } while (1);
}
