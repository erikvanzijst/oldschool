/*
 * Successive over relaxation
 * (red-black SOR)
 *
 */

#include "sys/time.h"
#include "unistd.h"
#include "stdio.h"
#include "stdlib.h"
#include "math.h"
#include "mpi.h"
#include "errno.h"

#define TOLERANCE      0.0002


struct table_t
{
  double **storage;
  int rows;
  int cols;
};


void table_init(struct table_t *table);

int table_alloc(struct table_t *table);
void table_free(struct table_t *table);

void table_do_sor(struct table_t *table, double omega, double stopdiff, int my_rank, int processors, int offset, int lb, int ub, int n);

int master_process(int my_rank, int processors, int N);
int slave_process(int my_rank, int processors, int N);

int even (int i);
double abs_d (double d);
double stencil (double**G, int row, int col);


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
        if (i == 0) table->storage[i][j] = 4.56;
        else if (i == table->rows-1) table->storage[i][j] = 9.85;
        else if (j == 0) table->storage[i][j] = 7.32;
        else if (j == table->cols-1) table->storage[i][j] = 6.88;
        else table->storage[i][j] = 0.0;
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
    table->storage = (double **) malloc(table->rows * sizeof(double *));
    if (table->storage)
    {
      for (i = 0; i < table->rows && !retval; i++)
      {
        table->storage[i] = (double *) malloc(table->cols * sizeof(double));
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


int even (int i)
{
  return ( ( ( i / 2 ) * 2 ) == i ) ? 1 : 0;
}

double abs_d (double d)
{
  return (d < 0.0) ? -d : d;
}

double stencil (double**G, int row, int col)
{
  return ( G[row-1][col] + G[row+1][col] + G[row][col-1] + G[row][col+1] )
         / 4.0;
}


void table_do_sor(struct table_t *table, double omega, double stopdiff, int my_rank, int processors, int offset, int lb, int ub, int n)
{
  int iteration = 0;
  int phase;
  int i;
  int j;
  double maxdiff;
  double global_maxdiff;
  double diff;
  double Gnew;
  MPI_Status status;
  struct timeval start;
  struct timeval end;
  double start_sec;
  double end_sec;


  if (table)
  {
    gettimeofday(&start, 0);
    do
    {
      //printf("Starting new iteration\n");
      maxdiff = 0.0;
      for ( phase = 0; phase < 2 ; phase++)
      {
        // send lower boundary to previous processor
        if (my_rank != 0) MPI_Send(table->storage[offset], table->cols, MPI_DOUBLE, my_rank - 1, 0, MPI_COMM_WORLD);
        // send upper boundary to next processor
        if (my_rank != (processors - 1)) MPI_Send(table->storage[ub - lb - 1 + offset], table->cols, MPI_DOUBLE, my_rank + 1, 0, MPI_COMM_WORLD);
        // receive next processor's lower boundary
        if (my_rank != (processors - 1)) MPI_Recv(table->storage[ub - lb + offset], table->cols, MPI_DOUBLE, my_rank + 1, 0, MPI_COMM_WORLD, &status);
        // receive previous processor's upper boundary
        if (my_rank != 0) MPI_Recv(table->storage[0], table->cols, MPI_DOUBLE, my_rank - 1, 0, MPI_COMM_WORLD, &status);
       
        //printf("Starting computation\n");
        for ( i = lb + (lb == 0 ? 1 : 0) ; i < ub - (ub == n ? 1 : 0) ; i++ )
        {
          for ( j = 1 + (even(i) ^ phase); j < table->cols - 1 ; j += 2 )
          {
            Gnew = stencil(table->storage, i - lb + offset, j);
            diff = abs_d(Gnew - table->storage[i - lb + offset][j]);
            if ( diff > maxdiff )
              maxdiff = diff;
            table->storage[i - lb + offset][j] = table->storage[i - lb + offset][j] + omega * (Gnew - table->storage[i - lb + offset][j]);
          }
        }
      }
      //printf("Synchronizing maxdiff\n");
      MPI_Allreduce(&maxdiff, &global_maxdiff, 1, MPI_DOUBLE, MPI_MAX, MPI_COMM_WORLD);
      //printf("Synchronization complete\n");
      iteration++;
    }
    while (global_maxdiff > stopdiff);

    gettimeofday(&end, 0);
    start_sec = (double) start.tv_sec + ((double)start.tv_usec / 1000000);
    end_sec = (double) end.tv_sec + ((double)end.tv_usec / 1000000);
    printf("Processor %d: SOR took %f seconds\n", my_rank, end_sec - start_sec);

    if (my_rank == 0)
    {
      printf("SOR %d x %d complete\n",n-2,n-2);
      printf("using %d iterations, diff is %f (allowed diff %f)\n", iteration, global_maxdiff, stopdiff);
    }
  }
}


int master_process(int my_rank, int processors, int N)
{
  struct table_t grid;
  int retval = 0;
  int i;
  int n = N + 2;
  int rows_per_processor = n / processors;        // number of rows per processor
  int unassigned_rows = n % processors;           // number of rows unassigned if n is not a multiple of processors
  int lb = 0;                                     // lower bound for rows to be computed
  int ub = rows_per_processor + unassigned_rows;  // upper bound for rows to be computed
  int rank;
  int rank_lb;
  int rank_ub;
  double r;
  double omega;
  double stopdiff;
  MPI_Status status;

  printf("Running SOR with %d rows\n", N);
  memset(&grid, 0, sizeof(grid));
  grid.cols = n;
  grid.rows = n;
  retval = table_alloc(&grid);
  if (!retval)
  {
    table_init(&grid);
    r        = 0.5 * ( cos( M_PI / n ) + cos( M_PI / n ) );
    omega    = 2.0 / ( 1 + sqrt( 1 - r * r ) );
    stopdiff = TOLERANCE / ( 2.0 - omega );
    omega   *= 0.8;                   /* magic factor */
  
    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    // distribute data
    for (rank = 1; rank < processors; rank++)
    {
      rank_lb = (rank * rows_per_processor) + unassigned_rows;  // lower bound for rows to be computed
      rank_ub = rank_lb + rows_per_processor;  // upper bound for rows to be computed,
      for (i = rank_lb; i < rank_ub; i++)
      {
        MPI_Send(grid.storage[i], grid.cols, MPI_DOUBLE, rank, 0, MPI_COMM_WORLD);
      }
    }

    printf("Processor %d: ready to start SOR\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    table_do_sor(&grid, omega, stopdiff, my_rank, processors, 0, lb, ub, n);

    printf("Processor %d: SOR done\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    // receive results
    for (rank = 1; rank < processors; rank++)
    {
      rank_lb = (rank * rows_per_processor) + unassigned_rows;  // lower bound for rows to be computed
      rank_ub = rank_lb + rows_per_processor;                   // upper bound for rows to be computed,
      for (i = rank_lb; i < rank_ub; i++)
      {
        MPI_Recv(grid.storage[i], grid.cols, MPI_DOUBLE, rank, 0, MPI_COMM_WORLD, &status);
      }
    }

    // destroy the grid
    table_free(&grid);
  }
  else // table_alloc failed
  {
    fprintf(stderr, "int master_process(int my_rank, int processors, int n): Processor %d failed to allocate grid\n", my_rank);
  }


  return retval;
}


int slave_process(int my_rank, int processors, int N)
{
  int retval = 0;
  int i;
  int n = N + 2;
  int rows_per_processor = n / processors;                    // number of rows per processor
  int unassigned_rows = n % processors;                       // number of rows unassigned if n is not a multiple of processors
  int lb = (my_rank * rows_per_processor) + unassigned_rows;  // lower bound for rows to be computed
  int ub = lb + rows_per_processor;                           // upper bound for rows to be computed
  int offset = (lb == 0 ? 0 : 1);                             // offset for lower bound (lb - 1 is row from neighbor processor)
  struct table_t grid_partition;
  double r;
  double omega;
  double stopdiff;
  MPI_Status status;

  memset(&grid_partition, 0, sizeof(grid_partition));
  grid_partition.cols = n;
  grid_partition.rows = ((ub - lb) > 0 ? ub - lb : 0) + offset;
  if (my_rank != (processors - 1)) grid_partition.rows += 1;
  retval = table_alloc(&grid_partition);
  if (!retval)
  {
    r        = 0.5 * ( cos( M_PI / n ) + cos( M_PI / n ) );
    omega    = 2.0 / ( 1 + sqrt( 1 - r * r ) );
    stopdiff = TOLERANCE / ( 2.0 - omega );
    omega   *= 0.8;                   /* magic factor */

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    // receive partition data
    for (i = lb; i < ub; i++)
    {
      // receive with offset for pivot row of upper neighbor
      MPI_Recv(grid_partition.storage[i - lb + offset], grid_partition.cols, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, &status);
    }

    printf("Processor %d: ready to start SOR\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    table_do_sor(&grid_partition, omega, stopdiff, my_rank, processors, offset, lb, ub, n);

    printf("Processor %d: SOR done\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    // send results
    for (i = lb; i < ub; i++)
    {
      MPI_Send(grid_partition.storage[i - lb + offset], grid_partition.cols, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);
    }

    // free the grid partition
    table_free(&grid_partition);
  }
  else // table_alloc failed
  {
    fprintf(stderr, "int slave_process(int my_rank, int processors, int n): Processor %d failed to allocate grid partition\n", my_rank);
  }

  return retval;
}


int main (int argc, char *argv[]){

  int N;                            /* problem size */
  int my_rank;
  int processors;
  int retval = 0;


  /* Start up MPI */
  MPI_Init(&argc, &argv);

  /* Find out process rank  */
  MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

  /* Find out number of processes */
  MPI_Comm_size(MPI_COMM_WORLD, &processors);

  /* set up problem size */
  N = 0;
  if ( argc > 1 )
    N = atoi(argv[1]);
  if (N == 0)
    N = 1000;


  if (my_rank == 0)
  {
    retval = master_process(my_rank, processors, N);
    if (retval)
    {
      fprintf(stderr, "int main(int argc, char *argv[]): Master process on processor %d failed\n", my_rank);
    }
  }
  else
  {
    retval = slave_process(my_rank, processors, N);
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
