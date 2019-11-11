#include "network.h"


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

