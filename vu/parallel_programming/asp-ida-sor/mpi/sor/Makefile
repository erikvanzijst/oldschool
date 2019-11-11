BIN=sor-mpi-block

all: ${BIN}

clean:
	rm -f *.o
	rm -f ${BIN}

tidy: clean
	rm -f *.core
	rm -f *~

sor-seq: sor-seq.c
	cc -o sor-seq sor-seq.c -lm

sor-mpi: sor-mpi.c
	/usr/local/bin/mpicc -O3 -o sor-mpi sor-mpi.c -lm

sor-mpi-block: sor-mpi-block.c
	/usr/local/bin/mpicc -O3 -o sor-mpi-block sor-mpi-block.c -lm
