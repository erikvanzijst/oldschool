#include "mm.h"
#include <minix/callnr.h>
#include <minix/com.h>
#include <signal.h>
#include "mproc.h"
#include "param.h"


/*===========================================================================*
 *				do_profile				     *
 *===========================================================================*/
PUBLIC int do_profile(void)
{
  /* Forward PROFILE system call to kernel, wait for kernel to complete the
   * operation (SET_PROFILE) and return the result.
   */

  int r = OK;
  message msg;

  msg.m_type = SET_PROFILE;
  msg.PROF_PROC = profile_pid;
  msg.PROF_ADDR1 = profile_start;
  msg.PROF_ADDR2 = profile_end;

  r = sendrec(CLOCK, &msg);
  if (r == OK) r = msg.m_type;

  return r;
}


/*===========================================================================*
 *				do_getprof				     *
 *===========================================================================*/
PUBLIC int do_getprof(void)
{
  /* Forward GETPROF system call to kernel, wait for kernel to complete the
   * operation (GET_PROFILE) and return the result.
   */

  int r = OK;
  message msg;

  msg.m_type = GET_PROFILE;
  msg.PROF_PROC = who;
  msg.PROF_DATA = profile_data;

  r = sendrec(CLOCK, &msg);
  if (r == OK) r = msg.m_type;

  return r;
}


