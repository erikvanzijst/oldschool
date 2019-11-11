/*
 * Simple application that contains two time-wasting loops in different parts
 * of the program. The second loop is exactly twice as heavy as the first.
 * The program is used to test if the kernel's profiling feature is indeed
 * correctly incrementing its bins.
 *
 * Note: obviously this program's execution time depends on the hardware it
 * runs on. It is not timed. When run on a machine twice as fast as ours, it
 * will finish in half the time, making the profiling less accurate. Vice versa,
 * on a slow machine it may take longer than humanly convenient.
 */

#include <stdlib.h>
#include <sys/types.h>

int bogusa(void);
int bogusb(void);

int main(void)
{
  int value;

  value = bogusa();
  value = bogusb();

  return value;
}


int bogusa(void)
{
  int itr;
  int a;

  for (a = 0; a < 200000000; a++);

  return a;
}


int bogusb(void)
{
  int itr;
  int b;

  for (b = 0; b < 400000000; b++);

  return b;
}

