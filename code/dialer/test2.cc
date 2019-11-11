#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>

int main(void)
{
  int modem;
  char *number = "ATDT0123456789\r\n";
  if (modem = open("/dev/modem", O_WRONLY) == -1)
  {	printf("error opening /dev/modem...");
	exit(0);
  }
  ::write(modem, number, strlen(number));
  sleep(4);
  close(modem);
}
