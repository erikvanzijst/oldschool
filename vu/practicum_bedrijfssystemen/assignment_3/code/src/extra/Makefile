# Makefile for profile

# Directories
u = /usr
i = $u/include
s = $i/sys

# Programs, flags, etc.
CC =	exec cc
CFLAGS = -I$i -D_POSIX_SOURCE
LDFLAGS = -i

STUBOBJ =	alarmstub.o
ALARM2A_OBJ =	alarm2a.o $(STUBOBJ)
ALARM2B_OBJ =	alarm2b.o $(STUBOBJ)
SIGALRM_OBJ =	sigalrm.o

all:		alarm2a \
		alarm2b \
                sigalrm

alarm2a:	$(ALARM2A_OBJ)
		$(CC) -g -o $@ $(LDFLAGS) $(ALARM2A_OBJ)

alarm2b:	$(ALARM2B_OBJ)
		$(CC) -g -o $@ $(LDFLAGS) $(ALARM2B_OBJ)

sigalrm:	$(SIGALRM_OBJ)
		$(CC) -g -o $@ $(LDFLAGS) $(SIGALRM_OBJ)

all install:	# Nothing.

clean:
	rm -f *.o *.bak \
	core \
	alarm2a \
	alarm2b \
	sigalrm

# Dependencies
a =	$i/math.h $i/stdio.h $i/stdlib.h $i/string.h \
	$s/types.h $s/wait.h $i/unistd.h

alarm2a.o:	$a

alarm2b.o:	$a

alarmstub.o:	$a

sigalrm.o:	$a

