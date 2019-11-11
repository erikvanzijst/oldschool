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
#include <errno.h>

#define PORTNR 2468

int shmid;
long* shared_long;

void sig_child(int sig)
{
	while(waitpid(0, NULL, WNOHANG) > 0)
	{
	}
}

/**
 * This method detaches from the shared memory segment and destroys it. It is
 * also a signal handler for SIGINT (ctrl-c), to make sure shared memory is
 * always released (after all, the server process does not have a graceful
 * shutdown option).
 */
void shmrm(int sig)
{
	char buf[1024];

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
	exit(0);
}

int main(int argc, char *argv[], char *envp[])
{
	int sockfd, fd;
	char buf[1024];
	long counter;
	pid_t pid;
	struct sockaddr_in addr;
	struct sockaddr_in client_addr;
	socklen_t client_addr_len;

	addr.sin_family = AF_INET;
	addr.sin_port = htons(PORTNR);
	addr.sin_addr.s_addr = htonl(INADDR_ANY);
	client_addr_len = sizeof(struct sockaddr_in);

	signal(SIGCHLD, sig_child);
	signal(SIGINT, shmrm);

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

	if( (sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1 ||
		bind(sockfd, (struct sockaddr *) &addr, sizeof(struct sockaddr_in)) == -1 ||
		listen(sockfd, 5) == -1)
	{
		snprintf(buf, sizeof(buf), "%s unable to listen on port %d", argv[0], PORTNR);
		perror(buf);
		shmrm(0);
		exit(1);
	}

	*shared_long = 0;
	while( (fd = accept(sockfd, (struct sockaddr *) &client_addr, &client_addr_len)) != -1)
	{
		pid = fork();
		if(pid == -1)
		{
			snprintf(buf, sizeof(buf), "%s: error spawning process", argv[0]);
			perror(buf);
		}
		else if(pid == 0)	// child process
		{
			counter = ++(*shared_long);
			fprintf(stdout, "Connection received from %s, returning %ld\n", inet_ntoa(client_addr.sin_addr), counter);
			snprintf(buf, sizeof(buf), "%ld\n", counter);
			write(fd, buf, strlen(buf) * sizeof(char));
			close(fd);
			
			if(shmdt((void *)shared_long) == -1)
			{
				snprintf(buf, sizeof(buf), "Child %d unable to detach from shared memory segment %d", getpid(), shmid);
				perror(buf);
			}

			exit(0);
		}

		close(fd);
	}

	close(sockfd);
	shmrm(0);
	return 0;	// to satisfy cc; never reached
}
