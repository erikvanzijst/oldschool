#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>
#include <signal.h>
#include <wait.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <errno.h>

#define PORTNR 2468
#define POOLSIZE 5

int listenfd, shmid, semaphore;
long* shared_long;

void sigint_child(int sig)
{
	char buf[1024];

	if(shmdt((void *)shared_long) == -1)
	{
		snprintf(buf, sizeof(buf), "Unable to detach from shared memory segment %d", shmid);
		perror(buf);
	}
	close(listenfd);
	exit(0);
}

/**
 * This method detaches from the shared memory segment and destroys it. It is
 * also a signal handler for SIGINT (ctrl-c), to make sure shared memory is
 * always released (after all, the server process does not have a graceful
 * shutdown option).
 */
void sigint_parent(int sig)
{
	char buf[1024];

	fprintf(stderr, "Shutting down server\n");
	if(semctl(semaphore, 0, IPC_RMID) == -1)
	{
		snprintf(buf, sizeof(buf), "Unable to remove the semaphore (%d)", semaphore);
		perror(buf);
	}

	if(shmdt((void *)shared_long) == -1)
	{
		snprintf(buf, sizeof(buf), "Unable to detach from shared memory segment %d", shmid);
		perror(buf);
	}
	if(shmctl(shmid, IPC_RMID, NULL) == -1)
	{
		snprintf(buf, sizeof(buf), "Unable to destroy shared memory segment %d", shmid);
		perror(buf);
	}
	close(listenfd);
	exit(0);
}

int main(int argc, char *argv[], char *envp[])
{
	int fd, nX, children = 0;
	char buf[1024];
	long counter;
	pid_t pid;
	struct sembuf up = {0, 1, 0};
	struct sembuf down = {0, -1, 0};
	struct sockaddr_in addr;
	struct sockaddr_in client_addr;
	socklen_t client_addr_len;

	addr.sin_family = AF_INET;
	addr.sin_port = htons(PORTNR);
	addr.sin_addr.s_addr = htonl(INADDR_ANY);
	client_addr_len = sizeof(struct sockaddr_in);

	if( (semaphore = semget(IPC_PRIVATE, 1, 0600)) == -1)
	{
		snprintf(buf, sizeof(buf), "%s unable to create semaphore", argv[0]);
		perror(buf);
		exit(1);
	}
	semctl(semaphore, 0, SETVAL, 1);

	signal(SIGINT, sigint_parent);
	if( (shmid = shmget(IPC_PRIVATE, sizeof(long), 0600)) == -1 )
	{
		snprintf(buf, sizeof(buf), "%s unable to create shared memory segment for IPC", argv[0]);
		perror(buf);
		exit(1);
	}

	if( (shared_long = (long *)shmat(shmid, NULL, 0)) == NULL )
	{
		snprintf(buf, sizeof(buf), "%s unable to attach to shared memory segment", argv[0]);
		perror(buf);
		if(shmctl(shmid, IPC_RMID, NULL) == -1)
		{
			snprintf(buf, sizeof(buf), "%s unable to destroy shared memory segment %d", argv[0], shmid);
			perror(buf);
		}
		exit(1);
	}
	*shared_long = 0;

	if( (listenfd = socket(AF_INET, SOCK_STREAM, 0)) == -1 ||
		bind(listenfd, (struct sockaddr *) &addr, sizeof(struct sockaddr_in)) == -1 ||
		listen(listenfd, 5) == -1)
	{
		snprintf(buf, sizeof(buf), "%s unable to listen on port %d", argv[0], PORTNR);
		perror(buf);
		sigint_parent(0);
		exit(1);
	}

	for(nX = 0; nX < POOLSIZE; nX++)
	{
		pid = fork();
		if(pid == -1)
		{
			snprintf(buf, sizeof(buf), "%s: error spawning process", argv[0]);
			perror(buf);
		}
		else if(pid == 0)	// child process
		{
			signal(SIGINT, sigint_child);
			pid = getpid();
			while( (fd = accept(listenfd, (struct sockaddr *) &client_addr, &client_addr_len)) != -1)
			{
				semop(semaphore, &down, 1);
				counter = ++(*shared_long);
				semop(semaphore, &up, 1);

				fprintf(stdout, "Child %d received connection from %s, returning %ld\n", pid, inet_ntoa(client_addr.sin_addr), counter);
				snprintf(buf, sizeof(buf), "%ld\n", counter);
				write(fd, buf, strlen(buf) * sizeof(char));
				close(fd);
			}
			sigint_child(0);
		}
		else
			children++;
	}
	
	for(nX = 0; nX < children; nX++)
	{
		wait(0);
	}

	sigint_parent(0);
	return 0;	// to satisfy cc; never reached
}
