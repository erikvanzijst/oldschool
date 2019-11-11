# Makefile for quick-compiling monop-server by its developers.
# written by Erik van Zijst, erik@prutser.cx
# Only capable of compiling sources
#
# Usage: just type "make"
# "make clean" removes object files and binary,
#
# Remove the -g flag for a binary without debug information. (WAY smaller!)

#LIBS=-Wmissing-prototypes
LIBS=
CC=cc
BINDEST=/usr/local/bin
MANDEST=/usr/local/man
TARGET=monop

SRC=monop.c init.c network.c parsers.c misc.c strings.c config.c game.c cards.c io.c
#OBJS=monop.o init.o network.o parsers.o misc.o strings.o config.o game.o cards.o io.o
OBJS=$(SRC:.c=.o)

.c.o:
	$(CC) -c $<

$(TARGET): $(OBJS)
	$(CC) -o $(TARGET) $(LIBS) $(OBJS)

clean:
	@echo "Deleting the following files:"
	@rm -vf *.o $(TARGET) core
