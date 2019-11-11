
#ifndef __PROFSTUB_H
#define __PROFSTUB_H

/*
 * This is the data structure used to transfer profiling
 * information between the user program and the kernel.
 */
struct prof_data
{
  int start_addr;
  int end_addr;
  int bins_used;
  unsigned long buffer[1024];
};

/*
 * These are the prototypes for the functions used to
 * invoke the respective system calls.
 */
int profile(int pid, int start_addr, int end_addr);
int getprof(struct prof_data *profdata);

#endif

