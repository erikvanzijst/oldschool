#ifndef DEFRAG_H
#define DEFRAG_H

#include <sys/types.h>
#include <sys/dir.h>
#include <sys/stat.h>
#include <limits.h>
#include <errno.h>
#include <time.h> /* ctime et al */
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
/* Regular minix headers */
#include <minix/config.h>
#include <minix/const.h>
#include <minix/type.h>
/* Required for mtab access */
#include <minix/minlib.h>

/* Include minix fs-headers for definitions of various fs datastructures */
#undef EXTERN
#define EXTERN /* get rid of extern by making it NIL */
/* Avoid problems defining printf (it is defined to printk in const.h) */
#undef printf
#include "/usr/src/fs/const.h"
#include "/usr/src/fs/type.h"
#include "/usr/src/fs/super.h"

#undef printf /* define printf to normal printf */ 
#include <stdio.h>

/* include definitions for application specific error codes */
#include "errno.h"

/* structure to hold all relevant file system information */
typedef struct {
  const char *pathname;
  int fd;
  struct super_block sb;
  bitchunk_t *imap;
  bitchunk_t *zmap;
  size_t zones_used;
  size_t highest_inode; /* address of the highest inode in use */
} fs_t;

#define WORDS_PER_BLOCK (BLOCK_SIZE / (int) sizeof(bitchunk_t))
#define SUPER_BLOCK_CNT	1
#define V2_NR_IDZONES   1
#define V2_NR_DIDZONES  1

/* operators on bitmasks */
#define BITSHIFT        4  /* = log2(#bits(int)) */
#define BITMASK		((1 << BITSHIFT) - 1)
#define WORDOFBIT(b)	((b) >> BITSHIFT)
#define POWEROFBIT(b)	(1 << ((int) (b) & BITMASK))
#define setbit(w, b)	(w[WORDOFBIT(b)] |= POWEROFBIT(b))
#define clrbit(w, b)	(w[WORDOFBIT(b)] &= ~POWEROFBIT(b))
#define bitset(w, b)	(w[WORDOFBIT(b)] & POWEROFBIT(b))

/* filesystem operations */
void fs_open              (fs_t *fs, const char *pathname, int flags);
void fs_close             (fs_t *fs);
int fs_is_empty           (const fs_t *fs);
void fs_check_mtab        (const fs_t *oldfs, const fs_t *newfs);
void fs_defrag            (const fs_t *oldfs, fs_t *newfs);
zone_t fs_defrag_dblock   (const fs_t *oldfs, fs_t *newfs, 
                           block_t *newfs_next_free, zone_t dzone_nr);
zone_t fs_defrag_idblock  (const fs_t *oldfs, fs_t *newfs, 
                           block_t *newfs_next_free, zone_t idzone_nr);
zone_t fs_defrag_didblock (const fs_t *oldfs, fs_t *newfs, 
                           block_t *newfs_next_free, zone_t didzone_nr);

/* device operations */
int devopen      (const char *pathname, int flags);
void devread     (int fd, off_t offset, char *buf, size_t size);
void devwrite    (int fd, off_t offset, char *buf, size_t size);
void devio       (int fd, block_t block_nr, char *block_data, int dir);
void devclose    (int fd);

/* bitmap operations */
bitchunk_t *allocbitmap (block_t block_cnt);
void loadbitmap         (int fd, bitchunk_t *bitmap, block_t block_nr, 
                         block_t block_cnt);
void dumpbitmap         (int fd, bitchunk_t *bitmap, block_t block_nr, 
                         block_t block_cnt);

/* conversion operators */
#define btoa(b)         ((off_t) (b) * BLOCK_SIZE)
#define atob(a)         ((block_t) (a) / BLOCK_SIZE)
#define inoaddr(inode,first_inode_block) \
                        ((off_t) (inode - 1) * V2_INODE_SIZE +  \
                         (off_t) btoa(first_inode_block))

#endif
