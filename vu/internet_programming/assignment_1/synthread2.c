#include <pthread.h>
#include <string.h>
#include <stdio.h>
#include <errno.h>
#include <unistd.h>


struct display_thread_arg
{
  pthread_mutex_t mutex;
  pthread_cond_t cond;
  int turn;
};

#define TURN_AB 1
#define TURN_CD 2

void display(char *str);
void *display_ab_thread_impl(void *arg);
void *display_cd_thread_impl(void *arg);


int main()
{
  int retval = 0;
  char *errorstr = 0;
  struct display_thread_arg display_thread_arg;
  pthread_t ab_thread;
  pthread_t cd_thread;

  // set the thread that takes the first turn
  display_thread_arg.turn = TURN_AB;
  // initialize the mutex
  retval = pthread_mutex_init(&display_thread_arg.mutex, 0);
  if (!retval) // mutex initialization ok
  {
    // initialize the condition variable
    retval = pthread_cond_init(&display_thread_arg.cond, 0);
    if (!retval) // condition variable initialization ok
    {
      // start the ab printing thread
      retval = pthread_create(&ab_thread, 0, display_ab_thread_impl, (void *) &display_thread_arg);
      if (!retval) // thread started
      {
        // start the cd printing thread
        retval = pthread_create(&cd_thread, 0, display_cd_thread_impl, (void *) &display_thread_arg);
        if (!retval) // thread started
        {
          // wait for the cd printing thread to complete
          pthread_join(cd_thread, 0);
        }
        else // pthread_create_failed
        {
          errorstr = (char *) strerror(retval);
          fprintf(stderr, "synthread2: %s\n", errorstr ? errorstr : "Failed to start thread");
        }
        // wait for the ab printing thread to complete
        pthread_join(ab_thread, 0);
      }
      else // pthread_create_failed
      { 
        errorstr = (char *) strerror(retval);
        fprintf(stderr, "synthread2: %s\n", errorstr ? errorstr : "Failed to start thread");
      }
      // destroy the condition variable
      pthread_cond_destroy(&display_thread_arg.cond);
    }
    else // pthread_cond_init failed
    {
      errorstr = (char *) strerror(retval);
      fprintf(stderr, "synthread2: %s\n", errorstr ? errorstr : "Failed to initialize condition variable");
    }
    // destroy the mutex
    pthread_mutex_destroy(&display_thread_arg.mutex);
  }
  else // pthread_mutex_init failed
  {
    errorstr = (char *) strerror(retval);
    fprintf(stderr, "synthread2: %s\n", errorstr ? errorstr : "Failed to initialize mutex");
  }

  return retval;
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


void *display_ab_thread_impl(void *arg)
{
  int i;
  struct display_thread_arg *display_thread_arg = (struct display_thread_arg *) arg;

  if (display_thread_arg)
  {
    // lock the mutex
    pthread_mutex_lock(&display_thread_arg->mutex);
    for (i=0;i<10;i++)
    {
      // check if it is my turn, if not block on the condition variable (and implicitly unlock the mutex)
      while (display_thread_arg->turn != TURN_AB) pthread_cond_wait(&display_thread_arg->cond, &display_thread_arg->mutex);
      // returning from the blocking pthread_cond_wait also implies that the mutex is locked again, I have control
      display("ab");
      // set the thread that takes the next tern
      display_thread_arg->turn = TURN_CD;
      // signal the other thread, broadcast is not needed, only one thread can be woken up 
      pthread_cond_signal(&display_thread_arg->cond);
    }
    // I am done, unlock the mutex
    pthread_mutex_unlock(&display_thread_arg->mutex);
  }

  return 0;
}


void *display_cd_thread_impl(void *arg)
{
  int i;
  struct display_thread_arg *display_thread_arg = (struct display_thread_arg *) arg;

  if (display_thread_arg)
  {
    // lock the mutex
    pthread_mutex_lock(&display_thread_arg->mutex);
    for (i=0;i<10;i++)
    { 
      // check if it is my turn, if not block on the condition variable (and implicitly unlock the mutex)
      while (display_thread_arg->turn != TURN_CD) pthread_cond_wait(&display_thread_arg->cond, &display_thread_arg->mutex);
      // returning from the blocking pthread_cond_wait also implies that the mutex is locked again, I have control
      display("cd\n");
      // set the thread that takes the next tern
      display_thread_arg->turn = TURN_AB;
      // signal the other thread, broadcast is not needed, only one thread can be woken up
      pthread_cond_signal(&display_thread_arg->cond);
    }
    // I am done, unlock the mutex
    pthread_mutex_unlock(&display_thread_arg->mutex);
  }

  return 0;
}


