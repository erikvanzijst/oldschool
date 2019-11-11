
/* dialing example */
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int main()
{
   int modem;
   char *reset = "ATZ\r\n";
   char *number = "ATDT23453426546345\r\n";

   if (modem = open("/dev/modem", O_WRONLY))
   {
//      printf("Resetting modem: %s", reset);
//      write(modem, reset, strlen(reset));
      printf("Dialing on /dev/modem : %s", number);
      write(modem, number, strlen(number)); /* send AT command to modem for dialing */
      sleep(10); /* wait a couple of seconds */
      close(modem);
   }
   else
      fprintf(stderr, "Couldn't open modem for writing...\n");

   return 1;
}
