# Homework assignment for the Internet Programming course at the Vrije
# Universiteit of Amsterdam, The Netherlands.
#
# Erik van Zijst, erik@marketxs.com
# Sander van Loo, sander@marketxs.com
# 24.feb.2003

CC=cc

CLIENT=client
CLIENT_SRC=client.c
CLIENT_OBJ=$(CLIENT_SRC:.c=.o)

SERV1=serv1
SERV1_SRC=serv1.c
SERV1_OBJ=$(SERV1_SRC:.c=.o)

SERV2=serv2
SERV2_SRC=serv2.c
SERV2_OBJ=$(SERV2_SRC:.c=.o)

SERV3=serv3
SERV3_SRC=serv3.c
SERV3_OBJ=$(SERV3_SRC:.c=.o)

TALK1=talk1
TALK1_SRC=talk1.c
TALK1_OBJ=$(TALK1_SRC:.c=.o)

TALK2=talk2
TALK2_SRC=talk2.c
TALK2_OBJ=$(TALK2_SRC:.c=.o)

TALK3=talk3
TALK3_SRC=talk3.c
TALK3_OBJ=$(TALK3_SRC:.c=.o)

.c.o:
	$(CC) -Wall -c $<

all: $(CLIENT) $(SERV1) $(SERV2) $(SERV3) $(TALK1) $(TALK2) $(TALK3)

$(CLIENT): $(CLIENT_OBJ)
	$(CC) -o $(CLIENT) $(CLIENT_OBJ)

$(SERV1): $(SERV1_OBJ)
	$(CC) -o $(SERV1) $(SERV1_OBJ)

$(SERV2): $(SERV2_OBJ)
	$(CC) -o $(SERV2) $(SERV2_OBJ)

$(SERV3): $(SERV3_OBJ)
	$(CC) -o $(SERV3) $(SERV3_OBJ)

$(TALK1): $(TALK1_OBJ)
	$(CC) -o $(TALK1) $(TALK1_OBJ)

$(TALK2): $(TALK2_OBJ)
	$(CC) -o $(TALK2) $(TALK2_OBJ)

$(TALK3): $(TALK3_OBJ)
	$(CC) -o $(TALK3) $(TALK3_OBJ) -lncurses

clean:
	@echo "Removing object files and targets:"
	@rm -vf *.o $(CLIENT) $(SERV1) $(SERV2) $(SERV3) $(TALK1) $(TALK2) $(TALK3) *~ core*
