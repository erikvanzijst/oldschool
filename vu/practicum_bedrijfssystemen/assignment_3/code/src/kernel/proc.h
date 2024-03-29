#ifndef PROC_H
#define PROC_H

/* Here is the declaration of the process table.  It contains the process'
 * registers, memory map, accounting, and message send/receive information.
 * Many assembly code routines reference fields in it.  The offsets to these
 * fields are defined in the assembler include file sconst.h.  When changing
 * 'proc', be sure to change sconst.h to match.
 */

struct proc {
  struct stackframe_s p_reg;	/* process' registers saved in stack frame */

#if (CHIP == INTEL)
  reg_t p_ldt_sel;		/* selector in gdt giving ldt base and limit*/
  struct segdesc_s p_ldt[2];	/* local descriptors for code and data */
				/* 2 is LDT_SIZE - avoid include protect.h */
#endif /* (CHIP == INTEL) */

#if (CHIP == M68000)
  reg_t p_splow;		/* lowest observed stack value */
  int p_trap;			/* trap type (only low byte) */
#if (SHADOWING == 0)
  char *p_crp;			/* mmu table pointer (really struct _rpr *) */
#else
  phys_clicks p_shadow;		/* set if shadowed process image */
  int align;			/* make the struct size a multiple of 4 */
#endif
  int p_nflips;			/* statistics */
  char p_physio;		/* cannot be (un)shadowed now if set */
#if defined(FPP)
  struct fsave p_fsave;		/* FPP state frame and registers */
  int align2;			/* make the struct size a multiple of 4 */
#endif
#endif /* (CHIP == M68000) */

  reg_t *p_stguard;		/* stack guard word */

  int p_nr;			/* number of this process (for fast access) */

  int p_int_blocked;		/* nonzero if int msg blocked by busy task */
  int p_int_held;		/* nonzero if int msg held by busy syscall */
  struct proc *p_nextheld;	/* next in chain of held-up int processes */

  int p_flags;			/* P_SLOT_FREE, SENDING, RECEIVING, etc. */
  struct mem_map p_map[NR_SEGS];/* memory map */
  pid_t p_pid;			/* process id passed in from MM */

  clock_t user_time;		/* user time in ticks */
  clock_t sys_time;		/* sys time in ticks */
  clock_t child_utime;		/* cumulative user time of children */
  clock_t child_stime;		/* cumulative sys time of children */
  clock_t p_alarm[NR_ALARMS];	/* time of next alarm in ticks, or 0 */

  struct proc *p_callerq;	/* head of list of procs wishing to send */
  struct proc *p_sendlink;	/* link to next proc wishing to send */
  message *p_messbuf;		/* pointer to message buffer */
  int p_getfrom;		/* from whom does process want to receive? */
  int p_sendto;

  struct proc *p_nextready;	/* pointer to next ready process */
  sigset_t p_pending;		/* bit map for pending signals */
  unsigned p_pendcount;		/* count of pending and unfinished signals */

  char p_name[16];		/* name of the process */
};

/* Guard word for task stacks. */
#define STACK_GUARD	((reg_t) (sizeof(reg_t) == 2 ? 0xBEEF : 0xDEADBEEF))

/* Bits for p_flags in proc[].  A process is runnable iff p_flags == 0. */
#define P_SLOT_FREE      001	/* set when slot is not in use */
#define NO_MAP           002	/* keeps unmapped forked child from running */
#define SENDING          004	/* set when process blocked trying to send */
#define RECEIVING        010	/* set when process blocked trying to recv */
#define PENDING          020	/* set when inform() of signal pending */
#define SIG_PENDING      040	/* keeps to-be-signalled proc from running */
#define P_STOP		0100	/* set when process is being traced */

/* Magic process table addresses. */
#define BEG_PROC_ADDR (&proc[0])
#define END_PROC_ADDR (&proc[NR_TASKS + NR_PROCS])
#define END_TASK_ADDR (&proc[NR_TASKS])
#define BEG_SERV_ADDR (&proc[NR_TASKS])
#define BEG_USER_ADDR (&proc[NR_TASKS + LOW_USER])

#define NIL_PROC          ((struct proc *) 0)
#define isidlehardware(n) ((n) == IDLE || (n) == HARDWARE)
#define isokprocn(n)      ((unsigned) ((n) + NR_TASKS) < NR_PROCS + NR_TASKS)
#define isoksrc_dest(n)   (isokprocn(n) || (n) == ANY)
#define isoksusern(n)     ((unsigned) (n) < NR_PROCS)
#define isokusern(n)      ((unsigned) ((n) - LOW_USER) < NR_PROCS - LOW_USER)
#define isrxhardware(n)   ((n) == ANY || (n) == HARDWARE)
#define issysentn(n)      ((n) == FS_PROC_NR || (n) == MM_PROC_NR)
#define istaskp(p)        ((p) < END_TASK_ADDR && (p) != proc_addr(IDLE))
#define isuserp(p)        ((p) >= BEG_USER_ADDR)
#define proc_addr(n)      (pproc_addr + NR_TASKS)[(n)]
#define cproc_addr(n)     (&(proc + NR_TASKS)[(n)])
#define proc_number(p)    ((p)->p_nr)
#define proc_vir2phys(p, vir) \
			  (((phys_bytes)(p)->p_map[D].mem_phys << CLICK_SHIFT) \
							+ (vir_bytes) (vir))
#if (SHADOWING == 1)
#define isshadowp(p)      ((p)->p_shadow != 0)
#endif

EXTERN struct proc proc[NR_TASKS + NR_PROCS];	/* process table */
EXTERN struct proc *pproc_addr[NR_TASKS + NR_PROCS];
/* ptrs to process table slots; fast because now a process entry can be found
   by indexing the pproc_addr array, while accessing an element i requires
   a multiplication with sizeof(struct proc) to determine the address */
EXTERN struct proc *bill_ptr;	/* ptr to process to bill for clock ticks */
EXTERN struct proc *rdy_head[NQ];	/* pointers to ready list headers */
EXTERN struct proc *rdy_tail[NQ];	/* pointers to ready list tails */

#endif /* PROC_H */
