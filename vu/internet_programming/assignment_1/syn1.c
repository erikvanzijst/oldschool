#include <sys/types.h>
#include <sys/wait.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <stdio.h>
#include <unistd.h>
#include <errno.h>


void display(char *str);


int main()
{
  int i;
  int semsetid;
  struct sembuf up = {0, 1, 0};
  struct sembuf down = {0, -1, 0};

  semsetid = semget(IPC_PRIVATE, 1, 0600);
  if (semsetid != -1) // semaphore set created
  {
    semctl(semsetid, 0, SETVAL, 1); // set semaphore to 1
    if (fork())
    {
      for (i=0;i<10;i++)
      {
        semop(semsetid, &down, 1); // enter critical section
        display("Hello world\n");
        semop(semsetid, &up, 1); // exit critical section
      }
      while(wait(0) == 0); // wait for child process to exit
      semctl(semsetid, 0, IPC_RMID); // destroy the semaphore set
    } 
    else
    { 
      for (i=0;i<10;i++)
      { 
        semop(semsetid, &down, 1); // enter critical section
        display("Bonjour monde\n"); 
        semop(semsetid, &up, 1); // exit critical section
      }
    }
  }
  else // failed to create semaphore set
  { 
    perror("syn1");
    exit (-1);
  }
  
  return 0;
}


void display(char *str)
{ 
  char *tmp;
  
  for (tmp=str;*tmp;tmp++)
  { 
    write(1,tmp,1);
    usleep(100);
  }
}

