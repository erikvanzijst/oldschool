#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <signal.h>
#include <unistd.h>
#include <time.h>

/*
 * This program tries to trigger the situation where 2 signals arrive at a
 * process simultaneously. To this end, this process forks, with the child
 * installing its signal handlers and waiting for signals, while the parent
 * waits for the specified seconds before sending the signals to the child.
 */
int alarm_counter = 0;

void handler(int sig)
{
  switch (sig)
  {
    case SIGALRM:
      printf("SIGALRM received\n");
      alarm_counter++;
      break;
    case SIGALRM2:
      printf("SIGALRM2 received\n");
      alarm_counter++;
      break;
    default:
      printf("Unexpected signal %d caught!\n", sig);
      break;
  }
}


int main(int argc, const char* argv[])
{
  if(argc == 3)
  {
    pid_t pid = fork();

    if(pid == 0)
    {
      /* child - receiving the signals */
      struct sigaction a_ALRM;
      struct sigaction a_ALRM2;

      sigaction(SIGALRM, NULL, &a_ALRM);
      sigaction(SIGALRM2, NULL, &a_ALRM2);

      a_ALRM.sa_handler = handler;
      a_ALRM2.sa_handler = handler;

      /* install the signal handlers */
      sigaction(SIGALRM, &a_ALRM, NULL);
      sigaction(SIGALRM2, &a_ALRM2, NULL);

      printf("Child %d waiting for ALRM and ALRM2 signals from parent...\n", getpid());
      while (alarm_counter < 2) pause();
    }
    else
    {
      /* parent - sending the signals */
      unsigned int seconds1;
      unsigned int seconds2;

      seconds1 = atoi(argv[1]);
      seconds2 = atoi(argv[2]);

      if(seconds1 < seconds2)
      {
        /* first send ALRM, then ALRM2 */
        sleep(seconds1);
        printf("Parent sending SIGALRM to process %d...\n", pid);
        kill(pid, SIGALRM);
        sleep(seconds2 - seconds1);
        printf("Parent sending SIGALRM2 to process %d...\n", pid);
        kill(pid, SIGALRM2);
      }
      else if(seconds2 < seconds1)
      {
        /* first send ALRM2, then ALRM */
        sleep(seconds2);
        printf("Parent sending SIGALRM2 to process %d...\n", pid);
        kill(pid, SIGALRM2);
        sleep(seconds1 - seconds2);
        printf("Parent sending SIGALRM to process %d...\n", pid);
        kill(pid, SIGALRM);
      }
      else
      {
        /* simultaneous */
        sleep(seconds1);
        printf("Parent sending SIGALRM to process %d...\n", pid);
        printf("Parent sending SIGALRM2 to process %d...\n", pid);
        kill(pid, SIGALRM);
        kill(pid, SIGALRM2);
      }
    }
  }
  else
  {
    printf("Usage: %s <seconds> <seconds>\n", argv[0]);
  }
}


