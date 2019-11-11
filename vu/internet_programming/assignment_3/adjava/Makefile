# Homework assignment for the Internet Programming course at the Vrije
# Universiteit of Amsterdam, The Netherlands.
#
# Erik van Zijst, erik@marketxs.com
# Sander van Loo, sander@marketxs.com
# 07.apr.2003

JAVAC=javac
RMIC=rmic
JAVADOC=javadoc

all: compile javadoc

compile:
	mkdir -p build/
	$(JAVAC) -d build `find src/ -type f -name "*.java"`
	$(RMIC) -d build/ -classpath build/ nl.vu.ip.AdServer

javadoc:
	@echo "Generating JavaDoc..."
	mkdir -p doc/javadoc
	$(JAVADOC) -author -windowtitle "Internet Programming - assignment 3" -d doc/javadoc -classpath build/ -sourcepath src/ nl.vu.ip

clean:
	@rm -rvf build/* core* doc/javadoc
