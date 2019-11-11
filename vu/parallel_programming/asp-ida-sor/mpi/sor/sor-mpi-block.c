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

void table_do_sor(struct table_t *table, double omega, double stopdiff, int my_rank, int processors, int row_offset, int row_lb, int row_ub, int col_offset, int col_lb, int col_ub, int n);

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


void table_do_sor(struct table_t *table, double omega, double stopdiff, int my_rank, int processors, int row_offset, int row_lb, int row_ub, int col_offset, int col_lb, int col_ub, int n)
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
  double col_buffer[row_ub - row_lb];
  int sqrt_processors = sqrt(processors);


  if (table)
  {
    gettimeofday(&start, 0);
    do
    {
      maxdiff = 0.0;
      for ( phase = 0; phase < 2 ; phase++)
      {
        // send lower boundary to previous processor
        if (row_lb != 0) MPI_Send(&table->storage[row_offset][col_offset], col_ub - col_lb, MPI_DOUBLE, my_rank - sqrt_processors, 0, MPI_COMM_WORLD);
        // send upper boundary to next processor
        if (row_ub != n) MPI_Send(&table->storage[row_ub - row_lb - 1 + row_offset][col_offset], col_ub - col_lb, MPI_DOUBLE, my_rank + sqrt_processors, 0, MPI_COMM_WORLD);
        // send left boundary to previous processor
        if (col_lb != 0)
        {
          for (i = row_lb; i < row_ub; i++)
          {
            col_buffer[i - row_lb] = table->storage[i - row_lb + row_offset][col_offset];
          }
          MPI_Send(col_buffer, row_ub - row_lb, MPI_DOUBLE, my_rank - 1, 0, MPI_COMM_WORLD);
        }
        // send right boundary to next processor
        if (col_ub != n)
        {
          for (i = row_lb; i < row_ub; i++)
          {
            col_buffer[i - row_lb] = table->storage[i - row_lb + row_offset][col_ub - col_lb - 1 + col_offset];
          }
          MPI_Send(col_buffer, row_ub - row_lb, MPI_DOUBLE, my_rank + 1, 0, MPI_COMM_WORLD);
        }

        // receive next processor's lower boundary
        if (row_ub != n) MPI_Recv(&table->storage[row_ub - row_lb + row_offset][col_offset], col_ub - col_lb, MPI_DOUBLE, my_rank + sqrt_processors, 0, MPI_COMM_WORLD, &status);
        // receive previous processor's upper boundary
        if (row_lb != 0) MPI_Recv(&table->storage[0][col_offset], col_ub - col_lb, MPI_DOUBLE, my_rank - sqrt_processors, 0, MPI_COMM_WORLD, &status);
        // receive next processor's left boundary
        if (col_ub != n)
        {
          MPI_Recv(col_buffer, row_ub - row_lb, MPI_DOUBLE, my_rank + 1, 0, MPI_COMM_WORLD, &status);
          for (i = row_lb; i < row_ub; i++)
          {
            table->storage[i - row_lb + row_offset][col_ub - col_lb + col_offset] = col_buffer[i - row_lb];
          }
        }
        // receive previous processor's right boundary
        if (col_lb != 0)
        {
          MPI_Recv(col_buffer, row_ub - row_lb, MPI_DOUBLE, my_rank - 1, 0, MPI_COMM_WORLD, &status);
          for (i = row_lb; i < row_ub; i++)
          {
            table->storage[i - row_lb + row_offset][0] = col_buffer[i - row_lb];
          }
        }
       
        for ( i = row_lb + (row_lb == 0 ? 1 : 0) ; i < row_ub - (row_ub == n ? 1 : 0) ; i++ )
        {
          for ( j = col_lb + (col_lb == 0 ? 1 : 0) + (even(i) ^ phase); j < col_ub - (col_ub == n ? 1 : 0) ; j += 2 )
          {
            Gnew = stencil(table->storage, i - row_lb + row_offset, j - col_lb + col_offset);
            diff = abs_d(Gnew - table->storage[i - row_lb + row_offset][j - col_lb + col_offset]);
            if ( diff > maxdiff )
              maxdiff = diff;
            table->storage[i - row_lb + row_offset][j - col_lb + col_offset] = table->storage[i - row_lb + row_offset][j - col_lb + col_offset] + omega * (Gnew - table->storage[i - row_lb + row_offset][j - col_lb + col_offset]);
          }
        }
      }
      MPI_Allreduce(&maxdiff, &global_maxdiff, 1, MPI_DOUBLE, MPI_MAX, MPI_COMM_WORLD);
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
  int sqrt_processors = sqrt(processors);
  int rows_per_processor = n / sqrt_processors;  // number of rows per processor
  int cols_per_processor = n / sqrt_processors;  // number of cols per processor
  int row_lb = 0;
  int row_ub = rows_per_processor;
  int col_lb = 0;
  int col_ub = cols_per_processor;
  int rank;
  int rank_row_lb;
  int rank_row_ub;
  int rank_col_lb;
  int rank_col_ub;
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
      rank_row_lb = (rank / sqrt_processors) * rows_per_processor;  // lower bound for rows to be computed
      rank_row_ub = rank_row_lb + rows_per_processor;               // upper bound for rows to be computed,
      rank_col_lb = (rank % sqrt_processors) * cols_per_processor;  // lower bound for rows to be computed
      rank_col_ub = rank_col_lb + cols_per_processor;               // upper bound for rows to be computed,
      for (i = rank_row_lb; i < rank_row_ub; i++)
      {
        MPI_Send(&grid.storage[i][rank_col_lb], rank_col_ub - rank_col_lb, MPI_DOUBLE, rank, 0, MPI_COMM_WORLD);
      }
    }

    printf("Processor %d: ready to start SOR\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    table_do_sor(&grid, omega, stopdiff, my_rank, processors, 0, row_lb, row_ub, 0, col_lb, col_ub, n);

    printf("Processor %d: SOR done\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    // receive results
    for (rank = 1; rank < processors; rank++)
    {
      rank_row_lb = (rank / sqrt_processors) * rows_per_processor;  // lower bound for rows to be computed
      rank_row_ub = rank_row_lb + rows_per_processor;               // upper bound for rows to be computed,
      rank_col_lb = (rank % sqrt_processors) * cols_per_processor;  // lower bound for rows to be computed
      rank_col_ub = rank_col_lb + cols_per_processor;               // upper bound for rows to be computed,
      for (i = rank_row_lb; i < rank_row_ub; i++)
      {
        MPI_Recv(&grid.storage[i][rank_col_lb], rank_col_ub - rank_col_lb, MPI_DOUBLE, rank, 0, MPI_COMM_WORLD, &status);
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
  int sqrt_processors = sqrt(processors);
  int rows_per_processor = n / sqrt_processors;  // number of rows per processor
  int cols_per_processor = n / sqrt_processors;  // number of cols per processor
  int row_lb = (my_rank / sqrt_processors) * rows_per_processor;  // lower bound for rows to be computed
  int row_ub = row_lb + rows_per_processor;                       // upper bound for rows to be computed,
  int col_lb = (my_rank % sqrt_processors) * cols_per_processor;  // lower bound for rows to be computed
  int col_ub = col_lb + cols_per_processor;                       // upper bound for rows to be computed,
  int row_offset = (row_lb == 0 ? 0 : 1);         // offset for lower row bound (lb - 1 is row from neighbor processor)
  int col_offset = (col_lb == 0 ? 0 : 1);         // offset for lower col bound (lb - 1 is col from neighbor processor)
  struct table_t grid_partition;
  double r;
  double omega;
  double stopdiff;
  MPI_Status status;

  memset(&grid_partition, 0, sizeof(grid_partition));
  grid_partition.cols = ((col_ub - col_lb) > 0 ? col_ub - col_lb : 0) + row_offset;
  grid_partition.rows = ((row_ub - row_lb) > 0 ? row_ub - row_lb : 0) + row_offset;
  if (col_ub != n) grid_partition.cols += 1;
  if (row_ub != n) grid_partition.rows += 1;
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
    for (i = row_lb; i < row_ub; i++)
    {
      // receive with offset for pivot row of upper neighbor
      MPI_Recv(&grid_partition.storage[i - row_lb + row_offset][col_offset], col_ub - col_lb, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, &status);
    }

    printf("Processor %d: ready to start SOR\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    table_do_sor(&grid_partition, omega, stopdiff, my_rank, processors, row_offset, row_lb, row_ub, col_offset, col_lb, col_ub, n);

    printf("Processor %d: SOR done\n", my_rank);

    // synchronize
    MPI_Barrier(MPI_COMM_WORLD);

    // send results
    for (i = row_lb; i < row_ub; i++)
    {
      MPI_Send(&grid_partition.storage[i - row_lb + row_offset][col_offset], col_ub - col_lb, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);
    }
  }
  else // table_alloc failed
  {
    fprintf(stderr, "int slave_process(int my_rank, int processors, int n): Processor %d failed to allocate grid partition\n", my_rank);
  }

  return retval;
}


int main (int argc, char *argv[]){

  int N;                            /* problem size */
  int n;
  int my_rank;
  int processors;
  int sqrt_processors;
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

  n = N + 2;
  sqrt_processors = sqrt(processors);
  if (sqrt(processors) == (double) sqrt_processors && ((n / sqrt_processors) * sqrt_processors) == n)
  {
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
  }
  else // problem size and sqrt(p) cannot be equally divided
  {
    if (my_rank == 0) fprintf(stderr, "int main(int argc, char *argv[]): Problem size cannot be equally divided by sqrt(processors)\n");
  }

  printf("Processor %d: done\n", my_rank);

  /* Shut down MPI */
  MPI_Finalize();

  return retval;
}
