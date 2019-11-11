#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>


#define COMMAND_BUFFER_SIZE	4096
#define SHELL_NAME		"mysh2"


int main(int argc, char *argv[], char *envp[])
{
  int retval = 0;
  char command_buffer[COMMAND_BUFFER_SIZE];
  char command_delimiters[] = " \n";
  char **program_argv = 0;
  int program_argc;
  void *result = 0;
  char *token;
  pid_t pid;

  fprintf(stdout, "%s$ ", SHELL_NAME);
  while (fgets(command_buffer, sizeof(command_buffer), stdin)) // read from stdin
  {
    // parse command into individual parameters tokenizing on SPACE and NEWLINE
    program_argc = 0;
    for (token = strtok(command_buffer, command_delimiters); token && !retval; token = strtok(0, command_delimiters))
    {
      // allocate additional space in the parameter array
      result = realloc(program_argv, (program_argc + 2)  * sizeof(char *));
      if (result)
      {
        program_argv = (char **) result;
        program_argv[program_argc] = token;
        program_argv[program_argc + 1] = 0;
        program_argc++;
      }
      else // memory allocation error
      {
        perror(SHELL_NAME);
        retval = -1;
      }
    }
    if (!retval)
    {
      if (program_argc > 0) // non-empty command
      {
        if (strcasecmp(program_argv[0], "exit") == 0) exit(0);
        pid = fork();
        if (pid == -1) // fork error
        {
          perror(SHELL_NAME);
          exit(-1);
        }
        else if (pid == 0) // child process
        {
          if (execvp(program_argv[0], program_argv) == -1) // failed to start program
          {
            perror(SHELL_NAME);
            exit(-1);
          }
        }
        else // parent process
        {
          if (wait(0) == -1) // wait returned an error
          {
            perror(SHELL_NAME);
            exit(-1);
          }
        }
      }
    }
    fprintf(stdout, "%s$ ", SHELL_NAME);
  }
  if (program_argv) free(program_argv);

  return retval;
}

