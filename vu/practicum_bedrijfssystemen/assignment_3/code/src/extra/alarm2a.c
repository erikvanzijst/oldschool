#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <signal.h>
#include <string.h>
#include <unistd.h>
#include <alarm2.h>
#include <time.h>

/*
 * Operating Systems Practicum 2004
 * Vrije Universiteit Amsterdam
 *
 * This user program is part of the third practical assignment "Alternate
 * Alarms". It allows users to experiment with the newly added alarm SIGALRM2
 * in combination with the standard SIGALRM signal.
 *
 * 04.aug.2004
 * Initial version.
 *
 * Erik van Zijst - 1351745 - erik@marketxs.com
 * Sander van Loo - 1351753 - sander@marketxs.com
 */

int alarm_counter = 0;

void handler(int sig)
{
  time_t systime = time(0);
  char *timestr = ctime(&systime);
  timestr[strlen(timestr) - 1] = 0;

  switch (sig)
  {
    case SIGALRM:
      printf("%s: Alarm 1 received\n", timestr);
      alarm_counter++;
      break;
    case SIGALRM2:
      printf("%s: Alarm 2 received\n", timestr);
      alarm_counter++;
      break;
    default:
      printf("%s: Signal %d caught!\n", timestr, sig);
      break;
  }
}


int main(int argc, const char* argv[])
{
  struct sigaction act_alarm1;
  struct sigaction act_alarm2;

  /* retrieve current signal handlers */
  sigaction(SIGALRM, NULL, &act_alarm1);
  sigaction(SIGALRM2, NULL, &act_alarm2);

  /* modify the default handlers */
  act_alarm1.sa_handler = handler;
  act_alarm2.sa_handler = handler;

  /* install the signal handlers */
  sigaction(SIGALRM, &act_alarm1, NULL);
  sigaction(SIGALRM2, &act_alarm2, NULL);

  if(argc == 3)
  {
    unsigned int seconds1;
    unsigned int seconds2;
    unsigned int remaining;
    time_t systime;
    char *timestr;

    seconds1 = atoi(argv[1]);
    seconds2 = atoi(argv[2]);

    remaining = alarm(seconds1);
    systime = time(0);
    timestr = ctime(&systime);
    timestr[strlen(timestr) - 1] = 0;
    printf("%s: Alarm 1 is scheduled to arrive in %u seconds\n", timestr, seconds1);

    remaining = alarm2(seconds2);
    systime = time(0);
    timestr = ctime(&systime);
    timestr[strlen(timestr) - 1] = 0;
    printf("%s: Alarm 2 is scheduled to arrive in %u seconds\n", timestr, seconds2);
    while (alarm_counter < 2) pause();
  }
  else
  {
    printf("Usage: %s <seconds> <seconds>\n", argv[0]);
  }
}


