#include <stdio.h>

main ()
  {
  int c = (225 - '\0');
  int r;

  scanf ("%d", &c);
  r = c - '\0' ;
  clrscr ();
  printf ("%c", r);
  getch();
  return 0;
  }