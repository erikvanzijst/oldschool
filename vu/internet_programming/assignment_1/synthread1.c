#include <pthread.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include <errno.h>


void display(char *str);
void *display_hello_world_thread_impl(void *arg);
void *display_bonjour_monde_thread_impl(void *arg);


int main()
{
  int retval = 0;
  pthread_mutex_t mutex;
  pthread_t hello_world_thread;
  pthread_t bonjour_monde_thread;
  char *errorstr = 0;

  retval = pthread_mutex_init(&mutex, 0);
  if (!retval)
  {
    retval = pthread_create(&hello_world_thread, 0, display_hello_world_thread_impl, (void *) &mutex);
    if (!retval)
    {
      retval = pthread_create(&bonjour_monde_thread, 0, display_bonjour_monde_thread_impl, (void *) &mutex);
      if (!retval)
      {
        pthread_join(bonjour_monde_thread, 0);
      }
      else // pthread_create_failed
      {
        errorstr = (char *) strerror(retval);
        fprintf(stderr, "synthread1: %s\n", errorstr ? errorstr : "Failed to start thread");
      }
      pthread_join(hello_world_thread, 0);
    }
    else // pthread_create_failed
    {
      errorstr = (char *) strerror(retval);
      fprintf(stderr, "synthread1: %s\n", errorstr ? errorstr : "Failed to start thread");
    }
    pthread_mutex_destroy(&mutex);
  }
  else // pthread_mutex_init failed
  {
    errorstr = (char *) strerror(retval);
    fprintf(stderr, "synthread1: %s\n", errorstr ? errorstr : "Failed to initialize mutex");
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


void *display_hello_world_thread_impl(void *arg)
{
  pthread_mutex_t *mutex = (pthread_mutex_t *)arg;
  int i;

  if (mutex)
  {
    for (i=0;i<10;i++)
    {
      pthread_mutex_lock(mutex);
      display("Hello world\n");
      pthread_mutex_unlock(mutex);
    }
  }

  return 0;
}


void *display_bonjour_monde_thread_impl(void *arg)
{
  pthread_mutex_t *mutex = (pthread_mutex_t *)arg;
  int i;

  if (mutex)
  {
    for (i=0;i<10;i++)
    {
      pthread_mutex_lock(mutex);
      display("Bonjour monde\n");
      pthread_mutex_unlock(mutex);
    }
  }

  return 0;
}

