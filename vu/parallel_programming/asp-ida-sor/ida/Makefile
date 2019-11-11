JAVAC=javac
RMIC=rmic
#MANTAC=mantac -debug -myrinet
MANTAC=mantac -myrinet -no_bounds -no_cast_checks -fast_math
JAVADOC=javadoc

DIST_SRC = Job.java \
	Move.java \
	WorkerImpl.java \
	Worker.java \
	DasInfo.java \
	JobManagerImpl.java \
	JobManager.java \
	JobQueue.java \
	LocalQueue.java \
	WaiterThread.java \
	OutOfWorkException.java
DIST_MAIN = FifteenPuzzle.java
DIST_OUT = 15puzzle-distributed

MT_SRC = Job.java \
	Move.java \
	JobQueue.java \
	Worker.java
MT_MAIN = FifteenPuzzle.java
MT_OUT = 15puzzle-multithreaded

SEQ_SRC = Move.java \
	Job.java
SEQ_MAIN = FifteenPuzzle.java
SEQ_OUT = 15puzzle-sequential

all: java javadoc manta_seq manta_mt manta_dist

java:
	@echo "Compiling..."
	mkdir -p build/
	$(JAVAC) -d build/ `find src/ -name "*.java" -type f`
	$(RMIC) -d build/ -classpath build/ nl.vu.pp.distributed.JobManagerImpl nl.vu.pp.distributed.WorkerImpl

manta_seq:
	@echo "Compiling native x86 binary 15puzzle-sequential with manta..."
	mkdir -p build/seq
	find src/nl/vu/pp/sequential -type f -name "*.java" -exec cp {} build/seq \;
	( cd build/seq && for i in $(SEQ_SRC); do $(MANTAC) -c $$i; done )
	( cd build/seq && $(MANTAC) $(SEQ_MAIN) -o ../../$(SEQ_OUT) *.o )

manta_mt:
	@echo "Compiling native x86 binary 15puzzle-multithreaded with manta..."
	mkdir -p build/mt
	find src/nl/vu/pp/multithreaded -type f -name "*.java" -exec cp {} build/mt \;
	( cd build/mt && for i in $(MT_SRC); do $(MANTAC) -c $$i; done )
	( cd build/mt && $(MANTAC) $(MT_MAIN) -o ../../$(MT_OUT) *.o )

manta_dist:
	@echo "Compiling native x86 binary 15puzzle-distributed with manta..."
	mkdir -p build/dist
	find src/nl/vu/pp/distributed -type f -name "*.java" -exec cp {} build/dist \;
	( cd build/dist && for i in $(DIST_SRC); do $(MANTAC) -c $$i; done )
	( cd build/dist && $(MANTAC) $(DIST_MAIN) -o ../../$(DIST_OUT) *.o )

javadoc:
	@echo "Generating JavaDoc..."
	mkdir -p doc/javadoc
	$(JAVADOC) -author -windowtitle "Sequential, multithreaded and distributed implementations for the 15-puzzle" -d doc/javadoc -classpath build/ -sourcepath src/ nl.vu.pp.sequential nl.vu.pp.multithreaded nl.vu.pp.distributed

clean:
	@echo "Removing compiled classes and documentation..."
	rm -rf build/* doc/javadoc $(SEQ_OUT) $(MT_OUT) $(DIST_OUT)
