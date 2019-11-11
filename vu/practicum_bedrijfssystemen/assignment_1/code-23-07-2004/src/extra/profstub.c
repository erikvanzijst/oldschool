
#include "profstub.h"
#include "lib.h"

/*
 * This is the implementation for the function to
 * invoke the PROFILE system call.
 */
int profile(int pid, int start_addr, int end_addr)
{
  message m;

  m.m1_i1 = pid;
  m.m1_i2 = start_addr;
  m.m1_i3 = end_addr;

  return _syscall(MM, PROFILE, &m);
}


/*
 * This is the implementation for the function to
 * invoke the GETPROF system call.
 */
int getprof(struct prof_data *profdata)
{
  message m;

  m.m1_p1 = (void *)profdata;

  return _syscall(MM, GETPROF, &m);
}

