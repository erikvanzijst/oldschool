# Homework assignment for the Internet Programming course at the Vrije
# Universiteit of Amsterdam, The Netherlands.
#
# Erik van Zijst, erik@marketxs.com
# Sander van Loo, sander@marketxs.com
# 21.feb.2003

CC=cc
JAVAC=javac
SH1=mysh1
SH2=mysh2
SH3=mysh3
SYN1=syn1
SYN2=syn2
SYN3=synthread1
SYN4=synthread2

SH1_SRC=mysh1.c
SH1_OBJ=$(SH1_SRC:.c=.o)
SH2_SRC=mysh2.c
SH2_OBJ=$(SH2_SRC:.c=.o)
SH3_SRC=mysh3.c
SH3_OBJ=$(SH3_SRC:.c=.o)
SYN1_SRC=syn1.c
SYN1_OBJ=$(SYN1_SRC:.c=.o)
SYN2_SRC=syn2.c
SYN2_OBJ=$(SYN2_SRC:.c=.o)
SYN3_SRC=synthread1.c
SYN3_OBJ=$(SYN3_SRC:.c=.o)
SYN4_SRC=synthread2.c
SYN4_OBJ=$(SYN4_SRC:.c=.o)

.c.o:
	$(CC) -Wall -c $<

all: $(SH1) $(SH2) $(SH3) $(SYN1) $(SYN2) $(SYN3) $(SYN4) java


$(SH1): $(SH1_OBJ)
	$(CC) -o $(SH1) $(SH1_OBJ)

$(SH2): $(SH2_OBJ)
	$(CC) -o $(SH2) $(SH2_OBJ)

$(SH3): $(SH3_OBJ)
	$(CC) -o $(SH3) $(SH3_OBJ)

$(SYN1): $(SYN1_OBJ)
	$(CC) -o $(SYN1) $(SYN1_OBJ)

$(SYN2): $(SYN2_OBJ)
	$(CC) -o $(SYN2) $(SYN2_OBJ)

$(SYN3): $(SYN3_OBJ)
	$(CC) -o $(SYN3) $(SYN3_OBJ) -lpthread

$(SYN4): $(SYN4_OBJ)
	$(CC) -o $(SYN4) $(SYN4_OBJ) -lpthread

java:
	$(JAVAC) Syn1.java Syn2.java

clean:
	@echo "Removing object files and targets:"
	@rm -vf *.o $(SH1) $(SH2) $(SH3) $(SYN1) $(SYN2) $(SYN3) $(SYN4) *~ core* *.class
