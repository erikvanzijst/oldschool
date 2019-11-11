/*
 * asp.c:
 * 	All-pairs shortest path implementation based on Floyd's
 * 	algorithms.
 *
 *      Parallel version.
 */

#include "sys/time.h"
#include "unistd.h"
#include "stdio.h"
#include "errno.h"
#include "stdlib.h"
#include "mpi.h"

/******************** ASP stuff *************************/


#define MAX_DISTANCE 42

/* malloc and initialize the table with some random distances       */
/* we never use srand() so rand() weill always use the same seed    */
/* and will hence yields reproducible results (for timing purposes) */

struct table_t
{
  int **storage;
  int rows;
  int cols;
};


void table_init(struct table_t *table);

int table_alloc(struct table_t *table);
void table_free(struct table_t *table);

void table_do_asp(struct table_t *table, int my_rank, int processors, int lb, int ub, int n);

int minimum(int a, int b);

int master_process(int my_rank, int processors, int n);
int slave_process(int my_rank, int processors, int n);

int main(int argc, char *argv[]);


void table_init(struct table_t *table)
{
  int i;
  int j;

  if (table)
  {
    for (i = 0; i < table->rows; i++)
    {
      for (j = 0; j < table->cols; j++)
      {
        table->storage[i][j] = (i == j ? 0 : 1 + (int) ((double)MAX_DISTANCE*rand()/(RAND_MAX+1.0)));
      }
    }
  }
}


int table_alloc(struct table_t *table)
{
  int i;
  int retval = 0;

  if (table)
  {
    table->storage = (int **) malloc(table->rows * sizeof(int *));
    if (table->storage)
    {
      for (i = 0; i < table->rows && !retval; i++)
      {
        table->storage[i] = (int *) malloc(table->cols * sizeof(int));
        if (!table->storage[i]) // malloc failed
        {
          retval = errno;
          fprintf(stderr, "int table_alloc(struct table_t *table): Memory allocation failed for row %d\n", i);
        }
      }
    }
    else // malloc failed
    {
      retval = errno;
      fprintf(stderr, "int table_alloc(struct table_t *table): Memory allocation failed for table storage\n");
    }
  }

  return retval;
}


void table_free(struct table_t *table)
{
  int i;

  if (table)
  {
    for (i = 0; i < table->rows; i++)
    {
      free(table->storage[i]);
      table->storage[i] = 0;
    }
    free(table->storage);
    table->storage = 0;
    table->rows = 0;
    table->cols = 0;
  }
}



void table_do_asp(struct table_t *table, int my_rank, int processors, int lb, int ub, int n)
{
  int rowk[table->cols];
  int rows_per_processor = n / processors;        // number of rows per processor
  int unassigned_rows = n % processors;           // number of rows unassigned if n is not a multiple of processors
  int root;
  int i;
  int j;
  int k;
  //MPI_Status status;
  struct timeval start;
  struct timeval end;
  double start_sec;
  double end_sec;

  if (table)
  {
    gettimeofday(&start, 0);
    for (k = 0; k < n; k++)
    {
      if (k >= lb && k < ub) // i have row k
      {
        memcpy(rowk, table->storage[k - lb], table->cols * sizeof(int));
        //printf("Processor %d: broadcasting row %d\n", my_rank, k);
        MPI_Bcast(rowk, n, MPI_INT, my_rank, MPI_COMM_WORLD);
      }
      else // i do not have row k
      {
        if (k < rows_per_processor + unassigned_rows) root = 0;
        else root = 1 + (k - (rows_per_processor + unassigned_rows))/rows_per_processor;
        //printf("Processor %d: waiting for row %d from processor %d\n", my_rank, k, root);
        MPI_Bcast(rowk, n, MPI_INT, root, MPI_COMM_WORLD);
        //printf("Processor %d: received row %d\n", my_rank, k);
      }
      for (i = lb; i < ub; i++)
      {
        if (i != k)
        {
          for (j = 0; j < table->cols; j++)
          {
            table->storage[i - lb][j] = minimum(table->storage[i - lb][j], table->storage[i - lb][k] + rowk[j]);
	  }
        }
      }
      // synchronize after every iteration
      MPI_Barrier(MPI_COMM_WORLD);
    }
    gettimeofday(&end, 0);
    start_sec = (double) start.tv_sec + ((double)start.tv_usec / 1000000);
    end_sec = (double) end.tv_sec + ((double)end.tv_usec / 1000000);
    printf("Processor %d: ASP took %f seconds\n", my_rank, end_sec - start_sec);
  }
}


int minimum(int a, int b)
{
  if (a < b) return a;
  else return b;
}


int master_process(int my_rank, int processors, int n)
{
  int i;
  int retval = 0;
  int rows_per_processor = n / processors;        // number of rows per processor
  int unassigned_rows = n % processors;           // number of rows unassigned if n is not a multiple of processors
  int lb = 0;                                     // lower bound for rows to be computed
  int ub = rows_per_processor + unassigned_rows;  // upper bound for rows to be computed
  int rank;
  int rank_lb;
  int rank_ub;
  struct table_t distance_table;
  MPI_Status status;

  printf("Processor %d: row %d to %d\n", my_rank, lb, ub - 1);
  // allocate and initialize the matrix
  memset(&distance_table, 0, sizeof(distance_table));
  distance_table.rows = n;
  distance_table.cols = n;
  printf("Processor %d: %d rows\n", my_rank, ub - lb);
  retval = table_alloc(&distance_table);
  if (!retval)
  {
    table_init(&distance_table);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    // distribute data
    for (rank = 1; rank < processors; rank++)
    {
      rank_lb = (rank * rows_per_processor) + unassigned_rows;  // lower bound for rows to be computed
      rank_ub = rank_lb + rows_per_processor;  // upper bound for rows to be computed,
      for (i = rank_lb; i < rank_ub; i++)
      {
        MPI_Send(distance_table.storage[i], distance_table.cols, MPI_INT, rank, 0, MPI_COMM_WORLD);
      }
    }

    printf("Processor %d: ready to start ASP\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    table_do_asp(&distance_table, my_rank, processors, lb, ub, n);

    printf("Processor %d: ASP done\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    // receive results
    for (rank = 1; rank < processors; rank++)
    {
      rank_lb = (rank * rows_per_processor) + unassigned_rows;  // lower bound for rows to be computed
      rank_ub = rank_lb + rows_per_processor;                   // upper bound for rows to be computed,
      for (i = rank_lb; i < rank_ub; i++)
      {
        MPI_Recv(distance_table.storage[i], distance_table.cols, MPI_INT, rank, 0, MPI_COMM_WORLD, &status);
      }
    }

    // destroy the matrix
    table_free(&distance_table);
  }
  else // tab_alloc failed
  {
    fprintf(stderr, "int master_process(int my_rank, int processors, int n): Processor %d failed to allocate distance table\n", my_rank);
  }

  return retval;
}


int slave_process(int my_rank, int processors, int n)
{
  int i;
  int retval = 0;
  int rows_per_processor = n / processors;                    // number of rows per processor
  int unassigned_rows = n % processors;                       // number of rows unassigned if n is not a multiple of processors
  int lb = (my_rank * rows_per_processor) + unassigned_rows;  // lower bound for rows to be computed
  int ub = lb + rows_per_processor;                           // upper bound for rows to be computed
  struct table_t distance_table_partition;
  MPI_Status  status;

  if (lb < ub) printf("Processor %d: row %d to %d\n", my_rank, lb, ub - 1);
  else printf("Processor %d: no rows assigned\n", my_rank);
  memset(&distance_table_partition, 0, sizeof(distance_table_partition));
  distance_table_partition.rows = (ub - lb) > 0 ? ub - lb : 0;
  distance_table_partition.cols = n;
  printf("Processor %d: %d rows\n", my_rank, distance_table_partition.rows);
  // allocate the partition
  retval = table_alloc(&distance_table_partition);
  if (!retval)
  {
    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    // receive partition data
    for (i = lb; i < ub; i++)
    {
      MPI_Recv(distance_table_partition.storage[i-lb], distance_table_partition.cols, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
    }

    printf("Processor %d: ready to start ASP\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    table_do_asp(&distance_table_partition, my_rank, processors, lb, ub, n);

    printf("Processor %d: ASP done\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    // send results
    for (i = lb; i < ub; i++)
    {
      MPI_Send(distance_table_partition.storage[i-lb], distance_table_partition.cols, MPI_INT, 0, 0, MPI_COMM_WORLD);
    }

    // free the partition
    table_free(&distance_table_partition);
  }
  else // table_alloc failed
  {
    fprintf(stderr, "int slave_process(int my_rank, int processors, int n): Processor %d failed to allocate distance table partition\n", my_rank);
  }

  return retval;
}


/******************** Main program *************************/

int
main(int argc, char *argv[])
{
    int n;
    int my_rank;
    int processors;
    int retval = 0;

    /* Start up MPI */
    MPI_Init(&argc, &argv);

    /* Find out process rank  */
    MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

    /* Find out number of processes */
    MPI_Comm_size(MPI_COMM_WORLD, &processors);

    n = 0;
    if ( argc > 1 )
      n = atoi(argv[1]);
    if (n == 0)
      n = 4000;

    if (my_rank == 0) // master processor
    {
      retval = master_process(my_rank, processors, n);
      if (retval)
      {
        fprintf(stderr, "int main(int argc, char *argv[]): Master process on processor %d failed\n", my_rank);
      }
    }
    else // slave processor
    {
      retval = slave_process(my_rank, processors, n);
      if (retval)
      {
        fprintf(stderr, "int main(int argc, char *argv[]): Slave process on processor %d failed\n", my_rank);
      }
    }

    printf("Processor %d: done\n", my_rank);

    /* Shut down MPI */
    MPI_Finalize();

    return retval;
}



