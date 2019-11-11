#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <errno.h>

#define PORTNR 2468

int main(int argc, char *argv[], char *envp[])
{
	int sockfd, fd;
	int counter = 0;
	char buf[1024];
	struct sockaddr_in addr;
	struct sockaddr_in client_addr;
	socklen_t client_addr_len;

	addr.sin_family = AF_INET;
	addr.sin_port = htons(PORTNR);
	addr.sin_addr.s_addr = htonl(INADDR_ANY);
	client_addr_len = sizeof(struct sockaddr_in);

	if( (sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1 ||
		bind(sockfd, (struct sockaddr *) &addr, sizeof(struct sockaddr_in)) == -1 ||
		listen(sockfd, 5) == -1)
	{
		snprintf(buf, sizeof(buf), "%s unable to listen on port %d", argv[0], PORTNR);
		perror(buf);
		exit(1);
	}

	while( (fd = accept(sockfd, (struct sockaddr *) &client_addr, &client_addr_len)) != -1)
	{
		fprintf(stdout, "Connection received from %s, returning %d\n", inet_ntoa(client_addr.sin_addr), counter + 1);
		snprintf(buf, sizeof(buf), "%d\n", ++counter);
		write(fd, buf, strlen(buf) * sizeof(char));
		close(fd);
	}

	close(sockfd);
	return 0;
}
