# Makefile for profile

# Directories
u = /usr
i = $u/include
s = $i/sys

# Programs, flags, etc.
CC =	exec cc
CFLAGS = -I$i -D_POSIX_SOURCE
LDFLAGS = -i

STUB_OBJ = 	profstub.o
OBJ = 		profile.o $(STUB_OBJ)

all:		profile \
		t_empty \
		t_hotspots \
		t_hotspots-c \
		t_syscall \
		t_blocking \
		t_profile \
		t_getprof

profile:	$(OBJ)
		$(CC) -g -o $@ $(LDFLAGS) $(OBJ)

t_empty:	# Nothing.
		$(CC) -g -o $@ $(LDFLAGS) t_empty.c

t_hotspots:	# Nothing.
		$(CC) -g -o $@ $(LDFLAGS) t_hotspots.c

t_hotspots-c:	# Nothing.
		$(CC) -g -o $@ t_hotspots.c

t_syscall:	# Nothing.
		$(CC) -g -o $@ $(LDFLAGS) t_syscall.c

t_blocking:	# Nothing.
		$(CC) -g -o $@ $(LDFLAGS) t_blocking.c

t_profile:	t_profile.o $(STUB_OBJ)
		$(CC) -g -o $@ $(LDFLAGS) t_profile.o $(STUB_OBJ)

t_getprof:	t_getprof.o $(STUB_OBJ)
		$(CC) -g -o $@ $(LDFLAGS) t_getprof.o $(STUB_OBJ)

all install:	# Nothing.

clean:
	rm -f *.o *.bak \
	profile \
	t_empty \
	t_hotspots \
	t_hotspots-c \
	t_syscall \
	t_blocking \
	t_getprof \
	t_profile

# Dependencies
a =	$i/profstub.h $i/math.h $i/stdio.h $i/stdlib.h $i/string.h \
	$s/types.h $s/wait.h $i/unistd.h

profile.o:	$a

profstub.o:	$a

t_profile.o:	$a

t_getprof.o:	$a

