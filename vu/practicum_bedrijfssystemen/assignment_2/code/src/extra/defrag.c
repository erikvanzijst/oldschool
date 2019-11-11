#include "defrag.h"


int main(int argc, const char *argv[])
{
  int r = OK;

  if (argc == 3)
  {
    /* correct number of arguments provided */
    fs_t oldfs;
    fs_t newfs;

    /* open the input filesystem */
    fs_open(&oldfs, argv[1], O_RDONLY);

    /* open the ouput filesystem */
    fs_open(&newfs, argv[2], O_RDWR);

    /* check whether one of the filesystems is mounted */
    fs_check_mtab(&oldfs, &newfs);

    /* verify output filesystem is empty */
    if (!fs_is_empty(&newfs))
    {
      fprintf(stdout, "Output filesystem is not empty\n");
      exit(ERR_TARGET_NOT_EMPTY);
    }

    /* we need at least as many inodes on newfs as on oldfs */
    if(newfs.sb.s_ninodes < oldfs.highest_inode)
    {
      fprintf(stdout, "Output filesystem has not enough inodes\n");
      exit(ERR_TARGET_TOO_SMALL);
    }

    /* we need as least that many free blocks/zones on newfs as in use
     * on oldfs
     */
    if(newfs.sb.s_zones - newfs.sb.s_firstdatazone < oldfs.zones_used)
    {
      fprintf(stdout, "Output filesystem has not enough blocks\n");
      exit(ERR_TARGET_TOO_SMALL);
    }

    /* defrag */
    fs_defrag(&oldfs, &newfs);

    /* close output file system */
    fs_close(&newfs);

    /* close input filesystem */
    fs_close(&oldfs);

    fprintf(stdout, "Defragmented filesystem written to %s\n", newfs.pathname);
  }
  else
  {
    /* incorrect number of arguments provided */
    fprintf(stdout, "Usage: %s <oldfs> <newfs>\n", argv[0]);
    r = ERR_INVALID_ARG;
  }

  return r;
}


int devopen(const char *pathname, int flags)
{
  int fd = open(pathname, flags);
  if (fd < 0)
  {
    /* failed to device */
    const char *errorstr = strerror(errno);
    fprintf(stdout, "Failed to open %s: %s\n", pathname, errorstr);
    exit (ERR_FILE_NOT_FOUND);
  }

  return fd;
}


void devclose(int fd)
{
  if (close(fd))
  {
    /* error closing device */
    const char *errorstr = strerror(errno);
    fprintf(stdout, "Failed to close device %d: %s\n", fd, errorstr);
    exit (ERR_GENERIC);
  }
}


/* Read or write a block. */
void devio(int fd, block_t block_nr, char *block_data, int dir)
{
  lseek(fd, (off_t) btoa(block_nr), SEEK_SET);
  if (dir == READING) 
  {
    /* read from device */
    if (read(fd, block_data, BLOCK_SIZE) != BLOCK_SIZE) 
    {
      /* read failed */
      const char *errorstr = strerror(errno);
      fprintf(stdout, "Failed to read block %d from device %d: %s\n", block_nr, fd, errorstr);
      exit (ERR_GENERIC);
    }
  }
  else
  {
    /* write to device */
    if (write(fd, block_data, BLOCK_SIZE) != BLOCK_SIZE) 
    {
      /* write failed */
      const char *errorstr = strerror(errno);
      fprintf(stdout, "Failed to write block %d to device %d: %s\n", block_nr, fd, errorstr);
      exit (ERR_GENERIC);
    }
  }
}


void devread(int fd, off_t offset, char *buf, size_t size)
{
  char block_buffer[BLOCK_SIZE];
  devio(fd, (block_t) atob(offset), block_buffer, READING);
  memcpy(buf, &block_buffer[(offset % BLOCK_SIZE)], size);
}


void devwrite(int fd, off_t offset, char *buf, size_t size)
{
  char block_buffer[BLOCK_SIZE];
  if (size < BLOCK_SIZE) 
    devio(fd, (block_t) atob(offset), block_buffer, READING);
  memcpy(&block_buffer[(offset % BLOCK_SIZE)], buf, size);
  devio(fd, (block_t) atob(offset), block_buffer, WRITING);
}


/* Allocate block_cnt number of blocks for the bitmap. */
bitchunk_t *allocbitmap(block_t block_cnt)
{
  bitchunk_t *bitmap;

  /* allocate memory */
  bitmap = (bitchunk_t *) malloc(block_cnt * BLOCK_SIZE);
  if (!bitmap)
  {
    /* malloc failed */
    const char *errorstr = strerror(errno);
    fprintf(stdout, "Failed to allocate bitmap of %u blocks: %s\n", 
            block_cnt, errorstr);
    exit (ERR_GENERIC);
  }

  /* the first entry in the bitmap is a special case */
  *bitmap |= 1;

  return(bitmap);
}


/* Load the bitmap starting at block block_nr and block_cnt blocks in size 
 * from device fd. 
 */
void loadbitmap(int fd, bitchunk_t *bitmap, block_t block_nr, block_t block_cnt)
{
  block_t itr;
  bitchunk_t *bitmap_ptr = bitmap;

  /* devread reads at most one block, call devread block_cnt times */
  for (itr = 0; itr < block_cnt; itr++, block_nr++, 
       bitmap_ptr += WORDS_PER_BLOCK)
    devread(fd, btoa(block_nr), (char *) bitmap_ptr, BLOCK_SIZE);

  /* the first entry in the bitmap is a special case */
  *bitmap |= 1;
}


/* Write the bitmap starting at block block_nr and block_cnt blocks in size 
 * to device fd. 
 */
void dumpbitmap(int fd, bitchunk_t *bitmap, block_t block_nr, block_t block_cnt)
{
  block_t itr;
  bitchunk_t *bitmap_ptr = bitmap;

  /* devwrite writes at most one block, call devwrite block_cnt times */
  for (itr = 0; itr < block_cnt; itr++, block_nr++, 
       bitmap_ptr += WORDS_PER_BLOCK)
    devwrite(fd, btoa(block_nr), (char *) bitmap_ptr, BLOCK_SIZE);
}


void fs_open(fs_t *fs, const char *pathname, int flags)
{
  size_t itr;

  /* set pathname */
  fs->pathname = pathname;

  /* open device containing the filesystem */
  fs->fd = devopen(fs->pathname, flags);

  /* read the super block */
  devread(fs->fd, btoa(SUPER_BLOCK), (char *) &fs->sb, sizeof(fs->sb));

  /* verify type of filesystem, SUPER_V2_REV (non-native byte
   * ordering) is not supported
   */
  if (fs->sb.s_magic != SUPER_V2)
  {
    /* filesystem is not a Minix V2 filesystem */
    fprintf(stdout, "Filesystem %s is not a Minix V2 filesystem\n", 
                     fs->pathname);
    exit(ERR_FS_TYPE);
  }

  /* allocate and read the bitmaps for the input filesystem */
  fs->imap = allocbitmap(fs->sb.s_imap_blocks);
  loadbitmap(fs->fd, fs->imap, SUPER_BLOCK + SUPER_BLOCK_CNT, 
             fs->sb.s_imap_blocks);
  fs->zmap = allocbitmap(fs->sb.s_zmap_blocks);
  loadbitmap(fs->fd, fs->zmap, 
             SUPER_BLOCK + SUPER_BLOCK_CNT + fs->sb.s_imap_blocks, 
             fs->sb.s_zmap_blocks);

  /* count the number of data blocks in use */
  fs->zones_used = 0;
  for(itr = 0; itr < fs->sb.s_zmap_blocks * WORDS_PER_BLOCK; itr++)
  {
    bitchunk_t chunk = fs->zmap[itr];
    for(; chunk != 0; chunk >>= 1) 
      if(chunk & 1) fs->zones_used++;
  }

  /* find the inode with the highest address */
  fs->highest_inode = 0;
  for(itr = 1; itr < fs->sb.s_ninodes; itr++)
  {
    /* check if inode is in use */
    if (bitset(fs->imap, (bit_t) itr))
    {
      fs->highest_inode = itr;
    }
  }
}


void fs_close(fs_t *fs)
{
  free(fs->imap);
  free(fs->zmap);
  devclose(fs->fd);
}


int fs_is_empty (const fs_t *fs)
{
  int is_empty = 1;
  int itr;

  for(itr = 0; itr < fs->sb.s_imap_blocks * WORDS_PER_BLOCK && is_empty; itr++)
  {
    if (itr == 0 && fs->imap[itr] != 3) is_empty = 0;
    else if (itr != 0 && fs->imap[itr] != 0) is_empty = 0;
  }

  return is_empty;
}


void fs_defrag  (const fs_t *oldfs, fs_t *newfs)
{ 
  const block_t oldfs_first_inode = SUPER_BLOCK + SUPER_BLOCK_CNT + 
                                    oldfs->sb.s_imap_blocks + 
                                    oldfs->sb.s_zmap_blocks;
  const block_t newfs_first_inode = SUPER_BLOCK + SUPER_BLOCK_CNT + 
                                    newfs->sb.s_imap_blocks + 
                                    newfs->sb.s_zmap_blocks;
  ino_t inode_nr = 1;
  block_t newfs_next_free = newfs->sb.s_firstdatazone;

  /* iterate through the inodes */
  do
  {
    /* check if inode is in use */
    if (bitset(oldfs->imap, (bit_t) inode_nr))
    {
      /* inode is in use */
      d2_inode inode;
      zone_t dzone_itr;

      /* read inode from input fs */
      devread(oldfs->fd, inoaddr(inode_nr, oldfs_first_inode), (char *) &inode, 
              sizeof(inode));

      /* determine what to do base on inode type */
      switch (inode.d2_mode & I_TYPE)
      {
        case I_REGULAR:
        case I_DIRECTORY:
          /* inodes that are files or directories use the block pointers
           * to refer to data blocks, these need to be defragmented 
           */
          for (dzone_itr = 0; dzone_itr < V2_NR_TZONES; dzone_itr++)
          {
            if (dzone_itr < V2_NR_DZONES)
            {
              /* process data block */
              inode.d2_zone[dzone_itr] = 
                fs_defrag_dblock(oldfs, newfs, &newfs_next_free, 
                                 inode.d2_zone[dzone_itr]);
            }
            else if (dzone_itr < V2_NR_DZONES + V2_NR_IDZONES)
            {
              /* process indirect block */
              inode.d2_zone[dzone_itr] = 
                fs_defrag_idblock(oldfs, newfs, &newfs_next_free, 
                                  inode.d2_zone[dzone_itr]);
            }
            else if (dzone_itr < V2_NR_DZONES + V2_NR_IDZONES + V2_NR_DIDZONES)
            {
              /* process double indirect block */
              inode.d2_zone[dzone_itr] = 
                fs_defrag_didblock(oldfs, newfs, &newfs_next_free, 
                                  inode.d2_zone[dzone_itr]);
            }
            else 
            {
              /* triple indirect zone not implemented */
            }
          }
          break;
        default:
          /* other types use the block pointers to store other information,
           * leave the inode as-is 
           */
          break;
      }

      /* write inode to output fs */
      devwrite(newfs->fd, inoaddr(inode_nr, newfs_first_inode), 
               (char *) &inode, sizeof(inode));

      /* update inode map on output fs */
      setbit(newfs->imap, (bit_t) inode_nr);
    }
  }
  while (++inode_nr < oldfs->sb.s_ninodes);

  /* write imap and zmap to output fs */
  dumpbitmap(newfs->fd, newfs->imap, SUPER_BLOCK + SUPER_BLOCK_CNT, 
             newfs->sb.s_imap_blocks);
  dumpbitmap(newfs->fd, newfs->zmap, 
             SUPER_BLOCK + SUPER_BLOCK_CNT + newfs->sb.s_imap_blocks, 
             newfs->sb.s_zmap_blocks);
}


zone_t fs_defrag_dblock (const fs_t *oldfs, fs_t *newfs, 
                        block_t *newfs_next_free, zone_t dzone_nr)
{
  zone_t new_dzone_nr = dzone_nr;

  if (dzone_nr != NO_ZONE)
  { 
    if (bitset(oldfs->zmap, dzone_nr - oldfs->sb.s_firstdatazone + 1))
    {
      block_t dblock_nr = (block_t) dzone_nr;
      char dblock_buffer[BLOCK_SIZE];

      /* no hole, read block from input fs */
      devread(oldfs->fd, btoa(dblock_nr), dblock_buffer, 
              sizeof(dblock_buffer));

      /* set the new block number */
      dblock_nr = (*newfs_next_free)++;
#ifdef DEBUG
      fprintf(stdout, "data block (%u)\n", dblock_nr);
#endif

      /*  write the block to the next free block on output fs */
      devwrite(newfs->fd, btoa(dblock_nr), dblock_buffer, 
               sizeof(dblock_buffer));

      /* set bit in the zone bitmap */
      setbit(newfs->zmap, dblock_nr - newfs->sb.s_firstdatazone + 1);

      /* set return value to zone nr of data block on newfs */
      new_dzone_nr = (zone_t) dblock_nr;
    }
    else
    {
      /* inconsistent filesystem */
      fprintf(stdout, "Inode not cleared on input filesystem\n");
      exit(ERR_INPUT_INCONSISTENT);
    }
  }

  return new_dzone_nr;
}


zone_t fs_defrag_idblock (const fs_t *oldfs, fs_t *newfs, 
                          block_t *newfs_next_free, zone_t idzone_nr)
{
  zone_t new_idzone_nr = idzone_nr;

  if(idzone_nr != NO_ZONE)
  {
    if(bitset(oldfs->zmap, idzone_nr - oldfs->sb.s_firstdatazone + 1))
    {
      zone_t idblock_itr;
      zone_t idblock_buffer[V2_INDIRECTS];
      block_t idblock_nr = (block_t) idzone_nr;

      /* read the indirect block */
      devread(oldfs->fd, btoa(idblock_nr), (char *)idblock_buffer,
              sizeof(idblock_buffer));

      /* set the new block number for the indirect block */
      idblock_nr = (*newfs_next_free)++;
#ifdef DEBUG
      fprintf(stdout, "indirect block (%u)\n", idblock_nr);
#endif

      /* iterate through all the zone pointers */
      for(idblock_itr = 0; idblock_itr < V2_INDIRECTS; idblock_itr++)
      {
        /* defrag data block */
        idblock_buffer[idblock_itr] = 
          fs_defrag_dblock(oldfs, newfs, newfs_next_free,
                           idblock_buffer[idblock_itr]);
      }

      /* store the new indirect block */
      devwrite(newfs->fd, btoa(idblock_nr), (char *)idblock_buffer, 
               sizeof(idblock_buffer));

      /* set bit in the zone bitmap */
      setbit(newfs->zmap, idblock_nr - newfs->sb.s_firstdatazone + 1);

      /* set return value to zone nr of indirect block on newfs */
      new_idzone_nr = (zone_t) idblock_nr;
    }
    else
    {
      /* inconsistent filesystem */
      fprintf(stdout, 
        "Inode's indirect zone pointer not cleared on input filesystem\n");
      exit(ERR_INPUT_INCONSISTENT);
    }
  }

  return new_idzone_nr;
}


zone_t fs_defrag_didblock (const fs_t *oldfs, fs_t *newfs, 
                           block_t *newfs_next_free, zone_t didzone_nr)
{
  zone_t new_didzone_nr = didzone_nr;

  if(didzone_nr != NO_ZONE)
  {
    if(bitset(oldfs->zmap, didzone_nr - oldfs->sb.s_firstdatazone + 1))
    {
      zone_t didblock_itr;  /* iterator for the double indirect block */
      zone_t didblock_buffer[V2_INDIRECTS];
      block_t didblock_nr = (block_t) didzone_nr;

      /* read the double indirect block */
      devread(oldfs->fd, btoa((block_t)didblock_nr), (char *)didblock_buffer, 
              sizeof(didblock_buffer));

      /* obtain the new block number for the double indirect block */
      didblock_nr = (*newfs_next_free)++;
#ifdef DEBUG
      fprintf(stdout, "double indirect block (%u)\n", didblock_nr);
#endif

      /* iterate through the double indirect block */
      for(didblock_itr = 0; didblock_itr < V2_INDIRECTS; didblock_itr++)
      {
        /* process indirect block */
        didblock_buffer[didblock_itr] = 
          fs_defrag_idblock(oldfs, newfs, newfs_next_free, 
                            didblock_buffer[didblock_itr]);
      }

      /* store the new double indirect block */
      devwrite(newfs->fd, btoa(didblock_nr), (char *)didblock_buffer, 
               sizeof(didblock_buffer));

      /* set the bit in the zone bitmap */
      setbit(newfs->zmap, didblock_nr - newfs->sb.s_firstdatazone + 1);

      /* set return value to zone nr of double indirect block on newfs */
      new_didzone_nr = (zone_t) didblock_nr;
    }
    else
    {
      /* fs inconsistent */
      fprintf(stdout, 
        "Double indirect zone %u contains uncleared zone pointer\n", 
        didzone_nr);
      exit(ERR_INPUT_INCONSISTENT);
    }
  }

  return new_didzone_nr;
}


void fs_check_mtab(const fs_t *oldfs, const fs_t *newfs)
{
  /* Check to see if the filesystem is mounted */
  char special[PATH_MAX + 1];
  char mounted_on[PATH_MAX + 1]; 
  char version[10];
  char rw_flag[10];

  if (load_mtab("defrag") >= 0) 
  {
    while (get_mtab_entry(special, mounted_on, version, rw_flag) >= 0)
    {
      if (strcmp(oldfs->pathname, special) == 0)
      {
        fprintf(stdout, "Cannot defragment a mounted filesystem: %s is mounted on %s\n",
          oldfs->pathname, mounted_on);
        exit(ERR_GENERIC);
      }
      else if (strcmp(newfs->pathname, special) == 0)
      {
        fprintf(stdout, "Cannot defragment to a mounted filesystem: %s is mounted on %s\n",
          newfs->pathname, mounted_on);
        exit(ERR_GENERIC);
      }
    }
  }
}


