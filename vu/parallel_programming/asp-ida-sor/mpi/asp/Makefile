BIN= asp-mpi

all: ${BIN}

clean:
	rm -f *.o
	rm -f ${BIN}

tidy: clean
	rm -f *.core
	rm -f *~

asp-seq: asp-seq.c
	cc -o asp-seq asp-seq.c

asp-mpi: asp-mpi.c
	/usr/local/bin/mpicc -o asp-mpi asp-mpi.c




















