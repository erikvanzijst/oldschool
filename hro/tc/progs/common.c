/*      common.c        Erik van Zijst           1997    */

#include "common.h"

/* Error: report program error.
Pre:    s points to the message to be printed.
Post:  The function prints the message and terminates the program.
*/

void Error(char *s)
{
    fprintf(stderr, "%s\n", s);
    exit(1);
}
