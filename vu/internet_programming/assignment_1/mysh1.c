#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>


#define COMMAND_BUFFER_SIZE	4096
#define SHELL_NAME		"mysh1"

int main(int argc, char *argv[], char *envp[])
{
  int retval = 0;
  char command_buffer[COMMAND_BUFFER_SIZE];
  char command_delimiters[] = " \n";
  size_t program_len;
  pid_t pid;

  fprintf(stdout, "%s$ ", SHELL_NAME);
  while (fgets(command_buffer, sizeof(command_buffer), stdin)) // read from stdin
  {
    program_len = strcspn(command_buffer, command_delimiters);
    if (program_len > 0) // non-empty command
    {
      command_buffer[program_len] = 0; // remove any trailing newline or space
      if (strcasecmp(command_buffer, "exit") == 0) exit(0);
      pid = fork();
      if (pid == -1) // fork error
      {
        perror(SHELL_NAME);
        exit(-1);
      }
      else if (pid == 0) // child process
      {
        if (execlp(command_buffer, command_buffer, 0) == -1) // failed to start program
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
    fprintf(stdout, "%s$ ", SHELL_NAME);
  }

  return retval;
}

