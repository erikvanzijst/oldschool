# Makefile for defrag

# Directories
u = /usr
i = $u/include
s = $i/sys
m = $i/minix
f = $u/src/fs

# Programs, flags, etc.
CC =	exec cc
CFLAGS = -I$i -D_POSIX_SOURCE -D_MINIX #-DDEBUG
LDFLAGS = -i 

OBJ = 		defrag.o 

all:		defrag

defrag:		$(OBJ)
		$(CC) -g -o $@ $(LDFLAGS) $(OBJ)

all install:	# Nothing.

clean:
	rm -f core \
	*.o \
	*.bak \
	defrag

# Dependencies
a =	$s/types.h $s/dir.h $s/stat.h $i/limits.h $i/errno.h $i/time.h \
	$i/stdlib.h $i/fcntl.h $m/config.h $m/const.h $m/type.h \
	$f/const.h $f/type.h $f/super.h $i/stdio.h

defrag.o:	$a
defrag.o:	defrag.h
defrag.o:	errno.h

