#include <sys/types.h>
#include <sys/wait.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <stdio.h>
#include <unistd.h>

#define SEM_NUM_PARENT  0
#define SEM_NUM_CHILD   1

void display(char *str);


int main()
{
  int i;
  int semsetid;
  struct sembuf up = {0, 1, 0};
  struct sembuf down = {0, -1, 0};

  semsetid = semget(IPC_PRIVATE, 2, 0600);
  if (semsetid != -1) // semaphore set created
  {
    semctl(semsetid, 0, SETVAL, 0); // set semaphore to 0
    semctl(semsetid, 1, SETVAL, 0); // set semaphore to 0
    if (fork())
    {
      for (i=0;i<10;i++)
      {
        display("ab");
        up.sem_num = SEM_NUM_CHILD;
        semop(semsetid, &up, 1); // signal child
        down.sem_num = SEM_NUM_PARENT;
        semop(semsetid, &down, 1); // wait for child
      }
      while(wait(0) == 0); // wait for child process to exit
      semctl(semsetid, 0, IPC_RMID); // destroy the semaphore set
    }
    else
    {
      for (i=0;i<10;i++)
      {
        down.sem_num = SEM_NUM_CHILD;
        semop(semsetid, &down, 1); // wait for parent
        display("cd\n");
        up.sem_num = SEM_NUM_PARENT;
        semop(semsetid, &up, 1); // signal parent
      }
    }
  }
  else // failed to create semaphore set
  { 
    perror("syn2");
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

