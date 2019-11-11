# Makefile for compiling kmonop by its developers.
# written by Erik van Zijst, icehawk@prutser.cx
# Only capable of compiling sources, removing object files
# and creating the .moc files.
# Only for use on Linux!
#
# Usage: just type "make"
# "make clean" removes object files and binary,
# "make moc" creates the moc files. (no need to use manually)

LIBS=-L/usr/X11R6/lib -lqt -lXext -lkdecore -lkdeui -lkfm -Wmissing-prototypes
OPT=-I$(QTDIR)/include -I$(KDEDIR)/include -L$(KDEDIR)/lib -L$(QTDIR)/lib
CC=g++
MOC=$(QTDIR)/bin/moc
BINDEST=/usr/local/bin
MANDEST=/usr/local/man
TARGET=kmonop

SRC=kmonop.cc network.cc kmonopwidget.cc main.cc parser.cc terminal.cc personal.cc street.cc player.cc seltoken.cc playertable.cc board.cc
OBJS=kmonop.o network.o kmonopwidget.o main.o parser.o terminal.o personal.o street.o player.o seltoken.o playertable.o board.o

.cc.o:
	$(CC) -c $(OPT) $<

$(TARGET): moc $(OBJS)
	$(CC) -o $(TARGET) $(OPT) $(LIBS) $(OBJS)

moc:
	@echo "Creating .moc files..."
	$(MOC) -o kmonop.moc kmonop.h;\
	$(MOC) -o network.moc network.h;\
	$(MOC) -o kmonopwidget.moc kmonopwidget.h;\
	$(MOC) -o terminal.moc terminal.h;\
	$(MOC) -o personal.moc personal.h;\
	$(MOC) -o street.moc street.h;\
	$(MOC) -o player.moc player.h;\
	$(MOC) -o seltoken.moc seltoken.h;\
	$(MOC) -o playertable.moc playertable.h;\
	$(MOC) -o board.moc board.h;

clean:
	@echo "Deleting the following files:"
	@rm -vf *.o *.moc $(TARGET) core
