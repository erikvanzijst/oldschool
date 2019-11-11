/*
   Note:

   I decided to keep the complex error checking. It is my _personal_ opinion
   that a program or function should have only a single exit point. Although
   exiting or returning immediately after an error may result in a less
   complex if/else structure, I don't think it produces more readable code.
   I think having more than one exit point in a single function has some
   similarities to the use of the goto statement. It also increases the risk
   of ommitting cleanup operations (ie. removing shared memory segments or 
   semaphores from the system) that are required to guarantee the continuity 
   of the system. 

   A.P.J. van Loo
*/


#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <netdb.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <inttypes.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>

#define LISTEN_PORT	2145
#define LISTEN_BACKLOG	1


int prepare_server_fd(int port, int backlog);
int connect_to_server(char *hostname, int port);


int main(int argc, char *argv[])
{
  int retval = 0;
  int server_fd = -1;
  int client_fd = -1;
  struct sockaddr_in address;
  socklen_t address_len = sizeof(address);

  if (argc <= 1) // act as a server
  {
    server_fd = prepare_server_fd(LISTEN_PORT, LISTEN_BACKLOG);
    if (server_fd >= 0) // valid server fd
    {
      fprintf(stdout, "Waiting for connections on port %d...\n", LISTEN_PORT);
      client_fd = accept(server_fd, &address, &address_len);
      if (client_fd >= 0)
      {
        fprintf(stdout, "Connection from %s:%d\n", inet_ntoa(address.sin_addr), ntohs(address.sin_port));
      }
      else // invalid client fd
      {
        retval = -1;
        fprintf(stderr, "int main(int argc, char *argv): Failed to accept connection\n");
      }
      if (close(server_fd)) perror("int main(int argc, char *argv)"); // close the server_fd, we do not need it anymore
      server_fd = -1;
    }
    else // prepare_server_failed
    {
      retval = -1;
      fprintf(stderr, "int main(int argc, char *argv): Failed to prepare server socket\n");
    }
  }
  else // act as a client
  {
    client_fd = connect_to_server(argv[1], LISTEN_PORT);
    if (client_fd < 0)
    {
      retval = -1;
      fprintf(stderr, "int main(int argc, char *argv): Failed to connect to server\n");
    }
  }

  if (!retval) // if no previous errors occurred we have a valid client_fd
  {
    fprintf(stdout, "Closing connection\n");
    if (close(client_fd)) perror("int main(int argc, char *argv)"); // close the client_fd, we are done
  }

  return retval;
}


int prepare_server_fd(int port, int backlog)
{
  int server_fd = -1;
  int reuse = 1;
  struct sockaddr_in address;

  // create a communication endpoint
  // for TCP stream based communication
  server_fd = socket(AF_INET, SOCK_STREAM, 0);
  if (server_fd >= 0) // we have obtained a valid file descriptor
  {
    // release the socket after program crash, avoid TIME_WAIT
    if (!setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &reuse, sizeof(reuse)))
    {
      address.sin_addr.s_addr = htonl(INADDR_ANY); // any host may connect
      address.sin_port = htons(port); // set the port to listen to
      // bind the file descriptor to the specified address (port)
      if (!bind(server_fd, (struct sockaddr *) &address, sizeof(address)))
      {
        // prepare socket to accept incoming connections
        if (listen(server_fd, backlog)) // listen failed
        {
          perror("int prepare_server_fd(int port, int backlog)");
          if (close(server_fd)) perror("int prepare_server_fd(int port, int backlog)");
          server_fd = -1;
        }
      }
      else // bind failed
      {
        perror("int prepare_server_fd(int port, int backlog)");
        if (close(server_fd)) perror("int prepare_server_fd(int port, int backlog)");
        server_fd = -1;
      }
    }
    else // setsockopt failed
    {
      perror("int prepare_server_fd(int port, int backlog)");
      if (close(server_fd)) perror("int prepare_server_fd(int port, int backlog)");
      server_fd = -1;
    }
  }
  else // socket failed
  {
    perror("int prepare_server_fd(int port, int backlog)");
  }

  return server_fd;
}


int connect_to_server(char *hostname, int port)
{
  int client_fd = -1;
  struct hostent *hp = 0;
  struct sockaddr_in address;

  // skip everything if for some reason a null pointer was provided
  // the return value will indicate an error
  if (hostname) 
  {
    hp = gethostbyname(hostname);
    if (hp) // successful lookup
    {
      // set the destination address and port
      memcpy(&address.sin_addr.s_addr, hp->h_addr_list[0], hp->h_length);
      address.sin_port = htons(port); // set the port to listen to
      address.sin_family = AF_INET;
      // create a communication endpoint
      // for TCP stream based communication
      client_fd = socket(AF_INET, SOCK_STREAM, 0);
      if (client_fd >= 0) // we have obtained a valid file descriptor
      {
        fprintf(stdout, "Connecting to %s:%d...\n", inet_ntoa(address.sin_addr), ntohs(address.sin_port));
        if (!connect(client_fd, (struct sockaddr *) &address, sizeof(address))) 
        {
          fprintf(stdout, "Connected!\n");
        }
        else // failed to connect to destionation address
        {
          perror("int connect_to_server(char *hostname, int port)");
          if (close(client_fd)) perror("int connect_to_server(char *hostname, int port)");
          client_fd = -1;
        }
      }
      else // socket failed
      {
        perror("int connect_to_server(char *hostname, int port)");
      }
    }
    else // gethostbyname failed
    {
      switch (h_errno)
      {
        case HOST_NOT_FOUND:
          fprintf(stderr, "int connect_to_server(char *hostname, int port): The specified host is unknown\n");
          break;
        case NO_DATA:
          fprintf(stderr, "int connect_to_server(char *hostname, int port): The requested name is valid but does not have an IP address\n");
          break;
        case NO_RECOVERY:
          fprintf(stderr, "int connect_to_server(char *hostname, int port): A non-recoverable name server error occurred\n");
          break;
        case TRY_AGAIN:
          fprintf(stderr, "int connect_to_server(char *hostname, int port): A temporary error occurred on an authoritative name server, try again later\n");
          break;
        default:
          fprintf(stderr, "int connect_to_server(char *hostname, int port): Failed to resolve hostname\n");
          break;
      }
    }
  }

  return client_fd;
}


