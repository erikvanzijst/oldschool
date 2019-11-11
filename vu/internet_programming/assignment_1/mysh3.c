/**
 * mysh3.c
 *
 * Homework assignment for the Internet Programming course at the Vrije
 * Universiteit of Amsterdam, The Netherlands.
 *
 * Erik van Zijst, erik@marketxs.com
 * Sander van Loo, sander@marketxs.com
 * 21.feb.2003
 */

#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>
#include <errno.h>

#define COMMAND_BUFFER_SIZE	4096
#define SHELL_NAME		"mysh3"

/**
 * A command datastructure is a three dimensional character array. Its first
 * dimension is build up of a series of 2-dimensional arrays. One for each
 * subcommand (for example "ls -l"), while these subcommands are made up of a
 * set of strings, one for each argument.
 * This method takes the whole datastructure and frees it. Obviously the whole
 * structure is assumed to be generated dynamically with malloc's.
 */
void free_cmd(char*** cmd)
{
	int nX, mX;

	for(nX = 0; cmd[nX] != 0; nX++)
		for(mX = 0; cmd[nX][mX] != 0; mX++)
			free(cmd[nX][mX]);
}

/**
 * This function parses a string with a single command (for example "ls -al")
 * and returns a datastructure suitable for execv().
 * Note that the returned datastructure has to be freed!
 */
char** parse_args(char* cmd)
{
	int program_argc = 0;
	char arg_delimiters[] = " \n";
	char* token;
	char** program_argv = 0;

	for (token = strtok(cmd, arg_delimiters); token; token = strtok(0, arg_delimiters))
	{
		if ( (program_argv = realloc(program_argv, (program_argc + 2)  * sizeof(char *))) )
		{
			if( (program_argv[program_argc] = malloc((strlen(token) + 1) * sizeof(char *))) )
			{
				strcpy(program_argv[program_argc], token);
				program_argv[++program_argc] = 0;
			}
			else // memory allocation error
			{
				perror(SHELL_NAME);
				exit(-1);
			}
		}
		else // memory allocation error
		{
			perror(SHELL_NAME);
			exit(-1);
		}
	}

	if(program_argc == 0)	// empty command
		return NULL;

	return program_argv;
}

/**
 * Takes a 3-dimensional datastructure. Its first dimension is build up of a
 * series of 2-dimensional arrays. One for each subcommand (for example
 * "ls -l"), while these subcommands are made up of a set of strings, one for
 * each argument. The second argument "count" specifies the number of
 * individual commands the datastructure holds.
 * This method will fork and exec every command and chain them with pipes. The
 * method returns when the last child has exited.
 */
int spawn_and_wait(char*** programs, int count)
{
	int nX, mX, retval = 0;
	int* pipes = 0;
	pid_t pid;

	if( (pipes = (int *)malloc((count - 1) * 2 * sizeof(int))) )
	{
		for(nX = 0; nX < (count - 1) ; nX++)	// create all inter-process pipes
		{
			if(pipe((int *)&pipes[nX*2]) == -1)
			{
				perror(SHELL_NAME);
				retval = -1;
				break;
			}
		}

		for(nX = 0; nX < count && retval == 0; nX++)
		{
			pid = fork();
			if(pid == -1)
			{
				perror(SHELL_NAME);
				retval = -1;
				break;
			}
			else if(pid == 0)	// child
			{
				if(nX > 0)	// use pipe for input
				{
					dup2(pipes[ ((nX - 1) * 2) ], STDIN_FILENO);
					close(pipes[ ((nX - 1) * 2) + 1 ]);
				}
				if(nX < (count-1))	// use pipe for output
				{
					dup2(pipes[ (nX * 2) + 1 ], STDOUT_FILENO);
					close(pipes[nX * 2]);
				}
				for(mX = 0; mX < (count - 1); mX++)	// close the others
				{
					if(mX != nX && mX != nX - 1)
					{
						close(pipes[mX * 2]);
						close(pipes[(mX * 2) + 1]);
					}
				}
				if(execvp(programs[nX][0], programs[nX]) == -1)
				{
					perror(SHELL_NAME);
					exit(-1);
				}
				exit(0);
			}
		}
		if(retval == 0)
		{
			for(nX = 0; nX < (count - 1); nX++)	// close all pipes
			{
				close(pipes[(nX * 2)]);
				close(pipes[(nX * 2) + 1]);
			}

			for(nX = 0; nX < count; nX++)	// wait for all children to finish
				wait(0);
		}
	}
	else // memory allocation error
	{
		perror(SHELL_NAME);
		retval = -1;
	}

	free(pipes);
	return retval;
}

int main(int argc, char *argv[], char *envp[])
{
	char command_buffer[COMMAND_BUFFER_SIZE];
	int programc, nX, buflen;
	char ***programs = 0;
	char *buf = 0;

	fprintf(stdout, "%s$ ", SHELL_NAME);
	while (fgets(command_buffer, sizeof(command_buffer), stdin)) // read from stdin
	{
		programc = 0;
		buf = (char *)malloc(sizeof(char *));
		buf[0] = buflen = 0;

		for(nX = strspn(command_buffer, " \n"); command_buffer[nX] != '\0'; nX++)
		{
			if(command_buffer[nX] == '|' || command_buffer[nX] == '\n')
			{
				if( (programs = (char ***)realloc(programs, (programc + 2) * sizeof(char *))) )
				{
					if( (programs[programc] = parse_args(buf)) )
					{
						programs[++programc] = '\0';
						buf[0] = buflen = 0;
					}
					else
					{
						fprintf(stderr, "%s: syntax error\n", SHELL_NAME);
						break;
					}
				}
				else
				{
					perror(SHELL_NAME);
					exit(-1);
				}
			}
			else
			{
				if( (buf = realloc(buf, (buflen + 1) * sizeof(char *))) )
				{
					buf[buflen] = command_buffer[nX];
					buf[++buflen] = '\0';
				}
				else
				{
					perror(SHELL_NAME);
					exit(-1);
				}
			}
		}

		// command expanded; pipe, fork and exec now
		if(programc != 0)
		{
			if (strcasecmp(programs[0][0], "exit") == 0) exit(0);
			spawn_and_wait(programs, programc);
			free_cmd(programs);
		}

		free(buf);
		fprintf(stdout, "%s$ ", SHELL_NAME);
	}

	return 0;
}
