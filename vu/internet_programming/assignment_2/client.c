#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <errno.h>

#define BUFLEN 1024
#define PORTNR 2468

int main(int argc, char *argv[], char *envp[])
{
	int sockfd, len;
	char buf[BUFLEN];
	struct hostent* resolv;
	struct in_addr* addr;
	struct sockaddr_in server_addr;

	if(argc != 2)
	{
		fprintf(stderr, "	Usage: %s <hostname>\n", argv[0]);
		exit(1);
	}

	if( (resolv = gethostbyname(argv[1])) == NULL)
	{
		fprintf(stderr, "%s: unknown hostname or address %s\n", argv[0], argv[1]);
		exit(1);
	}

	addr = (struct in_addr *)resolv->h_addr_list[0];
	server_addr.sin_family = AF_INET;
	server_addr.sin_port = htons(PORTNR);
	server_addr.sin_addr.s_addr = addr->s_addr;

	if( (sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1 ||
		connect(sockfd, (struct sockaddr *) &server_addr, sizeof(struct sockaddr_in)) == -1)
	{
		snprintf(buf, sizeof(buf), "%s unable to connect to %s on port %d", argv[0], inet_ntoa(server_addr.sin_addr), PORTNR);
		perror(buf);
		exit(1);
	}

	while( (len = read(sockfd, buf, (BUFLEN - 1) * sizeof(char))) != 0)
	{
		buf[len] = '\0';
		fprintf(stdout, "I received: %s", buf);
	}

	close(sockfd);
	return 0;
}
