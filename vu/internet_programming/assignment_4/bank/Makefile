# Homework assignment for the Internet Programming course at the Vrije
# Universiteit of Amsterdam, The Netherlands.
#
# Erik van Zijst, erik@marketxs.com
# Sander van Loo, sander@marketxs.com
# 21.feb.2003

CC=cc
BANK1=bankd
BANK2=bankcheck
BANK3=bankpay

BANK1_SRC=bankd.c bank.c network.c protocol.c
BANK1_OBJ=$(BANK1_SRC:.c=.o)
BANK2_SRC=bankcheck.c bank_client.c network.c protocol.c
BANK2_OBJ=$(BANK2_SRC:.c=.o)
BANK3_SRC=bankpay.c bank_client.c network.c protocol.c
BANK3_OBJ=$(BANK3_SRC:.c=.o)

.c.o:
	$(CC) -Wall -c $<

all: $(BANK1) $(BANK2) $(BANK3) 


$(BANK1): $(BANK1_OBJ)
	$(CC) -o $(BANK1) $(BANK1_OBJ) -lpthread

$(BANK2): $(BANK2_OBJ)
	$(CC) -o $(BANK2) $(BANK2_OBJ)

$(BANK3): $(BANK3_OBJ)
	$(CC) -o $(BANK3) $(BANK3_OBJ)

clean:
	@echo "Removing object files and targets:"
	@rm -vf *.o $(BANK1) $(BANK2) $(BANK3) *~ core
