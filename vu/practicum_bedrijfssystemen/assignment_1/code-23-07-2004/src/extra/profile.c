#include <errno.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>
#include <unistd.h>
#include <profstub.h>

/*
 * Operating Systems Practicum 2004
 * Vrije Universiteit Amsterdam
 *
 * This is the user program for the first practical assignment "Profile your
 * processes". It profiles the execution of a given program, and outputs some
 * data and a simple histogram (in ASCII) of the program's execution time.
 *
 * 12.may.2004
 * Erik van Zijst - 1351745 - erik@marketxs.com
 * Sander van Loo - 1351753 - sander@marketxs.com
 */

/* format string for usage information */
const char* usage = "Usage: %s [-n number_of_rows] [-r startaddr-endaddr] program_name [program_arguments ...]\n";


/* type for holding the configuration of the profile application */
typedef struct 
{
  int bar_count;
  int start_addr;
  int end_addr;
  char** program_args;  /* null-terminated set of strings for execvp() */
} config_t;


/* type representing a bar */
typedef struct 
{
  int start_addr;
  int end_addr;	/* inclusive */
  int counter;	/* aggregated counter */
} bar_t;


int parse_args(int argc, const char* argv[], config_t *config);
void print_stats(const struct prof_data *profdata, const config_t *config);
double round(double x);


int main(int argc, const char* argv[])
{
  int retval = 0;
  config_t config;
  pid_t pid;

  retval = parse_args(argc, argv, &config);
  if (retval == EINVAL)
  {
    /* display usage */
    fprintf(stdout, usage, argv[0]);
    exit(EXIT_FAILURE);
  }
  else if (retval != 0)
  {
    /* display error */
    fprintf(stderr, "Unable to parse arguments: %s\n", strerror(retval) ? 
                                                         strerror(retval) : 
                                                         "Unknown error");
    exit(EXIT_FAILURE);
  }

  /* Now fork a new process executing the code to be profiled, the 
   * parent process starts the profiler if the fork call is successful 
   * and wait for the child to terminate before collecting the 
   * profile.
   */ 
  pid = fork();
  if(pid == -1)
  {
    /* fork() failed */
    perror("Unable to fork");
    exit(EXIT_FAILURE);
  }
  else if(pid == 0)
  {
    /* this is the code for the child process */
    if(execvp(config.program_args[0], config.program_args) == -1)
    {
      /* failed to start the specified process */
      perror("Unable to start the process");
      exit(EXIT_FAILURE);
    }
    /* end of child process code */
  }
  else	
  {
    /* this is the code for the parent process */
    struct prof_data profdata;

    /* enable the profiler */
    if (profile(pid, config.start_addr, config.end_addr) != 0)
    {
      /* enabling of the profiler failed */
      perror("Error enabling profiling");
      /* kill the child */
      kill(pid, SIGKILL);
      /* wait for the child */
      wait(0);
      /* terminate */
      exit(EXIT_FAILURE);
    }

    /* the profiler was sucessfully enable wait for the child 
     * to terminate 
     */
    if(wait(0) == -1)
    {
      /* the wait call failed */
      perror("Error while waiting for the child process");
      exit(EXIT_FAILURE);
    }

    /* collect the profile data */
    if(getprof(&profdata) != 0)
    {
      /* failed to collect the profile data */
      perror("Error retrieving profiling data");
      exit(EXIT_FAILURE);
    }

    /* disable the profiler after collecting the data, otherwise a 
     * concurrent profile could reset the profile data before we
     * collected it
     */
    profile(0, 0, 0);	


    /* display the profile */
    print_stats(&profdata, &config);

    /* if arguments where provided in the configuration
     * free the resources now 
     */
    if(config.program_args != NULL) free(config.program_args);

    /* all done */
    return EXIT_SUCCESS;

    /* end of parent process code */
  }
}


/*
 * Reads the program's arguments and stores them in the config struct that was
 * passed by the caller.
 */
int parse_args(int argc, const char* argv[], config_t *config)
{
  int arg_itr;
  int prog_arg_itr = 0;
  int retval = 0;

  /* first set the default values */
  config->bar_count = 20;
  config->start_addr = config->end_addr = 0;
  config->program_args = NULL;

  /* iterate through the argument list */
  for(arg_itr = 1; arg_itr < argc && !retval; arg_itr++)
  {
    /* Parse the arguments: rows, address range, and program
     * + arguments. After encountering the first program argument 
     * all future -n or -r arguments belong the program arguments.
     */
    if(!prog_arg_itr && strcmp(argv[arg_itr], "-n") == 0)
    {
      /* parse number of rows to display */
      if(arg_itr < (argc - 1))
      {
        /* value available */
        config->bar_count = atoi(argv[++arg_itr]);
        if(config->bar_count < 1)
        {
          /* at least one row should be displayed */
          retval = EINVAL;
        }
      }
      else
      {
        /* else value to -n not provided */
        retval = EINVAL;
      }
    }
    else if(!prog_arg_itr && strcmp(argv[arg_itr], "-r") == 0)
    {
      /* Parse the address range that should be profiled.
       * The address range is given as 023E-0266 hexadecimal.
       */
      if(arg_itr < (argc - 1))
      {
        /* value for -r available */
        const char delimiter = '-';
        const int address_base = 16;
        const char* range = argv[++arg_itr];
        const int delim_pos = strchr(range, delimiter) - range;

        /* a range should have at least one byte before the 
         * delimiter and one byte after the delimiter
         */
        if(delim_pos >= 1 && strlen(range) - (delim_pos + 1) >= 1)
        {
          /* data before and after delimiter is available */
          config->start_addr = strtol(range, NULL, address_base);
          config->end_addr = strtol(&range[delim_pos + 1], NULL, address_base);
        }
        else
        {
          /* either the start address or the end address is missing */
          retval = EINVAL;	
        }
      }
      else
      {
        /* value to -r not provided */
        retval = EINVAL;
      }
    }
    else
    {
      /* no -n or -r was encountered, so this must be the target
       * name with its optional arguments 
       */
      /* expand the array of program arguments */
      void *result = realloc(config->program_args, (prog_arg_itr + 2) * 
                                                    sizeof(char *));
      if (result)
      {
        /* expansion successful */
        config->program_args = (char **) result;
        config->program_args[prog_arg_itr] = 
          (char *) argv[arg_itr + prog_arg_itr];
        config->program_args[prog_arg_itr + 1] = '\0';
        prog_arg_itr++;
      }
      else
      {
        /* memory allocation failed */
        retval = errno;
      }
    }
  }

  /* If no arguments provided set error */
  if(config->program_args == NULL) retval = EINVAL;

  return retval;
}


/*
 * This functions prints the ASCII histogram.
 */
void print_stats(const struct prof_data *profdata, const config_t *config)
{
  const double bar_width = 62; /* maximum width of a bar */ 
  int bins; 		       /* number of bins */
  int bytes;		       /* number of bytes profiled */
  int bytes_per_bin;	       /* number of bytes per bin */
  int bar_count;               /* number of bars to display */
  int bins_per_bar;	       /* number of bins per bar */
  int bins_in_last_bar;	       /* number of bins in the last bar */
  int bar_itr;
  double percentage;
  long total_count = 0;	       /* used to calculate percentage */
  long largest_bar = 0;	       /* used to determine scale */
  bar_t bars[sizeof(profdata->buffer) / sizeof(unsigned long)];            
                               /* array holding all bars, the maximum
                                * number being the maximum number of bins.
                                */
  const char one_digit_padding[] = ".."; 
                               /* padding for percentage < 10 */
  const char two_digit_padding[] = ".";
                               /* padding for 10 <= percentage < 100 */
  const char three_digit_padding[] = "";
                               /* padding for percentage == 100 */
  char *padding;

  /* derive the number of addresses per bin */
  bins = profdata->bins_used;
  bytes = (profdata->end_addr - profdata->start_addr) + 1;
  bytes_per_bin = bytes / bins;
  /* if the number of bytes_per_bin is not an integer, round up */
  if( bytes % bins != 0) bytes_per_bin++;

  /* the number of bars is the number of bars requested unless the
   * number of bins is smaller than the number of bars requested.
   */
  bar_count = config->bar_count < bins ? config->bar_count : bins;

  bins_per_bar = bins / bar_count;
  /* if the number of bins_per_bar is not an integer, round up */
  if(bins % bar_count != 0)
  {
    bins_per_bar++;	
    bins_in_last_bar = bins % bins_per_bar;
  }
  else
  {
    bins_in_last_bar = bins_per_bar;
  }

  /* truncate the number of bars if necessary */
  bar_count = bins / bins_per_bar;
  /* if the number of bars is not an integer, round up */
  if(bins % bins_per_bar != 0)  bar_count++;

  /* Fill the bar structs.
   * Each record represents one bar. A bar contains an aggregated number 
   * of bins and covers the sum of the address ranges of the aggregated 
   * bins.
   */
  for(bar_itr = 0; bar_itr < bar_count; bar_itr++)
  {
    bars[bar_itr].counter = 0;
    if(bar_itr == (bar_count - 1))
    {
      int bin_itr;

      /* this is the last bar */
      bars[bar_itr].start_addr = 
        profdata->start_addr + (bar_itr * bins_per_bar * bytes_per_bin);
      bars[bar_itr].end_addr = profdata->end_addr;

      /* compute the aggregated counter */
      for(bin_itr = 0; bin_itr < bins_in_last_bar; bin_itr++)
      {
        bars[bar_itr].counter += 
          profdata->buffer[bar_itr * bins_per_bar + bin_itr];
      }
    }
    else
    {
      int bin_itr;

      bars[bar_itr].start_addr = 
        profdata->start_addr + (bar_itr * bins_per_bar * bytes_per_bin);
      bars[bar_itr].end_addr = profdata->start_addr + 
                               ((bar_itr + 1) * bins_per_bar * bytes_per_bin) 
                               - 1;

      /* compute the aggregated counter */
      for(bin_itr = 0; bin_itr < bins_per_bar; bin_itr++)
      {
        bars[bar_itr].counter += 
          profdata->buffer[bar_itr * bins_per_bar + bin_itr];
      }
    }

    total_count += bars[bar_itr].counter;
    /* maintain the largest bin in the largest_bin variable */
    if(bars[bar_itr].counter > largest_bar) 
      largest_bar = bars[bar_itr].counter;
  }

  /* Display the title */
  printf("%s\n", config->program_args[0]);

  /* calculate the percentage corresponding to the largest bar */
  percentage = (total_count == 0 ? 0 : 
                ((double)largest_bar / (double)total_count) * (double)100);

  /* adapt the pading to the percentage */
  if(round(percentage) < 10) padding = (char *) one_digit_padding;
  else if(round(percentage) < 100) padding = (char *) two_digit_padding;
  else padding = (char *) three_digit_padding;

  /* Display the scale */
  printf("                  0%%........................................................%s%.0f%%\n", padding, percentage);

  /* Display the bars */
  for(bar_itr = 0; bar_itr < bar_count; bar_itr++)
  {
    int mark_itr;

    /* calculate the percentage of the current bar */
    percentage = (total_count == 0 ? 0 : 
                  ((double)bars[bar_itr].counter / (double)total_count) * 
                  (double)100);

    /* modify the bar header for special case 100% */
    /* special case: this bar has 100% of the ticks, skip the space between the right parenthesis and the colon */
    if(round(percentage) == 100) 
      printf("%04x-%04x (100%%): ", bars[bar_itr].start_addr, 
                                    bars[bar_itr].end_addr);
    /* normal case: put a space between the parenthese and the colon */
    else 
      printf("%04x-%04x (%02.0f%%) : ", bars[bar_itr].start_addr, 
                                        bars[bar_itr].end_addr, 
                                        percentage);

    /* the largest bin is worth bar_width asteriks, so a smaller bin is
    worth (bin / largest_bin) * bar_width asteriks */
    for(mark_itr = 0; 
        mark_itr < round(((double)bars[bar_itr].counter 
                         / (double)largest_bar) * bar_width); 
        mark_itr++) 
      printf("*");
    printf("\n");
  }
}


/* This function implements round() which appears to be missing from the 
 * C-library (C99). 
 */
double round(double x)
{
  /* round halfway cases away from zero */
  if (x >= 0)
  {
    /* positive number, round up */
    if (x - floor(x) >= 0.5) return ceil(x);
    else return floor(x);
  }
  else
  {
    /* negative number, round down */
    if (x - ceil(x) <= -0.5) return floor(x);
    else return ceil(x);
  }
}

