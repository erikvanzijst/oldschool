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


#include <ncurses.h>
#include <sys/time.h>
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
#define BUFFER_SIZE	100

#define RETVAL_EOF	1


int prepare_server_fd(int port, int backlog);
int connect_to_server(char *hostname, int port);
int read_and_forward(int srcfd, int dstfd);
int chat_with_fd(int fd);
int init_screen(void);
void destroy_screen(void);


// global variables for the ncurses windows. Since our program is single 
// thread / single process no additional synchronization is needed.
WINDOW *remote;
WINDOW *local;


int main(int argc, char *argv[])
{
  int retval = 0;
  int server_fd = -1;
  int client_fd = -1;
  struct sockaddr_in address;
  socklen_t address_len = sizeof(address);
  char *errorstr = 0;

  retval = init_screen();
  if (!retval)
  {
    if (argc <= 1) // act as a server
    {
      // no hostname wa provided, we are the server
      server_fd = prepare_server_fd(LISTEN_PORT, LISTEN_BACKLOG);
      if (server_fd >= 0) // valid server fd
      {
        mvwprintw(stdscr, 0, 2, "[ Waiting for connections on port %d... ]", LISTEN_PORT);
        refresh();
        client_fd = accept(server_fd, &address, &address_len);
        if (client_fd >= 0)
        {
          mvwhline(stdscr, 0, 2, ACS_HLINE, COLS - 3);
          mvwprintw(stdscr, 0, 2, "[ %s:%d ]", inet_ntoa(address.sin_addr), ntohs(address.sin_port));
          refresh();
        }
        else // invalid client fd
        {
          retval = -1;
          wprintw(local, "ERROR: Failed to accept connection\n");
          wrefresh(local);
        }
        if (close(server_fd)) perror("int main(int argc, char *argv)"); // close the server_fd, we do not need it anymore
        server_fd = -1;
      }
      else // prepare_server_fd failed
      {
        retval = -1;
        wprintw(local, "ERROR: Failed to prepare server socket\n");
        wrefresh(local);
      }
    }
    else // act as a client
    {
      // set up a connection to the remote server
      client_fd = connect_to_server(argv[1], LISTEN_PORT);
      if (client_fd < 0)
      {
        retval = -1;
        wprintw(local, "ERROR: Failed to connect to server\n");
        wrefresh(local);
      }
    }
  
    if (!retval) // if no previous errors occurred we have a valid client_fd
    {
      // start a chat with the remote application represented by client_fd
      retval = chat_with_fd(client_fd);
      // if the retvarn value was RETVAL_EOF the session was terminated as expected: do not print an error
      if (retval && retval != RETVAL_EOF)
      {
        wprintw(local , "ERROR: Unexpected end of chat\n");
        wrefresh(local);
      }
      mvwhline(stdscr, 0, 2, ACS_HLINE, COLS - 3);
      mvwprintw(stdscr, 0, 2, "[ Closing connection... ]");
      refresh();
      if (close(client_fd)) // close the client_fd, we are done
      {
        errorstr = strerror(errno);
        wprintw(local, "ERROR: %s\n", errorstr ? errorstr : "Failed to close file descriptor"); 
        wrefresh(local);
      }
    }
  
    // cleanup 
    destroy_screen();
  }
  else // screen_init failed
  {
    fprintf(stderr, "Failed to initialize screen\n");
  }

  return retval;
}


int prepare_server_fd(int port, int backlog)
{
  int server_fd = -1;
  int reuse = 1;
  struct sockaddr_in address;
  char *errorstr = 0;

  // this funtion creates a file descriptor that can be used to accept new
  // connections made to the designated port. It returns -1 on error

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
          errorstr = strerror(errno);
          wprintw(local, "ERROR: %s\n", errorstr ? errorstr : "Failed to set socket to listen mode");
          wrefresh(local);
          if (close(server_fd)) 
          {
            errorstr = strerror(errno);
            wprintw(local, "ERROR: %s\n", errorstr ? errorstr : "Failed to close file descriptor");
            wrefresh(local);
          }
          server_fd = -1;
        }
      }
      else // bind failed
      {
        errorstr = strerror(errno);
        wprintw(local, "ERROR: %s\n", errorstr ? errorstr : "Failed to bind socket to port");
        wrefresh(local);
        if (close(server_fd)) 
        {
          errorstr = strerror(errno);
          wprintw(local, "ERROR: %s\n", errorstr ? errorstr : "Failed to close file descriptor");
          wrefresh(local);
        }
        server_fd = -1;
      }
    }
    else // setsockopt failed
    {
      errorstr = strerror(errno);
      wprintw(local, "ERROR: %s\n", errorstr ? errorstr : "Failed to set socket options");
      wrefresh(local);
      if (close(server_fd)) 
      {
        errorstr = strerror(errno);
        wprintw(local, "ERROR: %s\n", errorstr ? errorstr : "Failed to close file descriptor");
        wrefresh(local);
      }
      server_fd = -1;
    }
  }
  else // socket failed
  {
    errorstr = strerror(errno);
    wprintw(local, "ERROR: %s\n", errorstr ? errorstr : "Failed to create socket");
    wrefresh(local);
  }

  return server_fd;
}


int connect_to_server(char *hostname, int port)
{
  int client_fd = -1;
  struct hostent *hp = 0;
  struct sockaddr_in address;
  char *errorstr = 0;

  // this function tries to setup a connection to the designated host and 
  // port. It returns a filedescriptor on success or -1 on error.

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
        mvwprintw(stdscr, 0, 2, "[ Connecting to %s:%d... ]", inet_ntoa(address.sin_addr), ntohs(address.sin_port));
        refresh();
        if (!connect(client_fd, (struct sockaddr *) &address, sizeof(address))) 
        {
          mvwhline(stdscr, 0, 2, ACS_HLINE, COLS - 3);
          mvwprintw(stdscr, 0, 2, "[ %s:%d ]", inet_ntoa(address.sin_addr), ntohs(address.sin_port));
          refresh();
        }
        else // failed to connect to destination address
        {
          errorstr = strerror(errno);
          wprintw(local, "ERROR: %s\n", errorstr ? errorstr : "Failed to connect to remote host");
          wrefresh(local);
          if (close(client_fd)) 
          {
            errorstr = strerror(errno);
            wprintw(local, "ERROR: %s\n", errorstr ? errorstr : "Failed to close file descriptor");
            wrefresh(local);
        
          }
          client_fd = -1;
        }
      }
      else // socket failed
      {
        errorstr = strerror(errno);
        wprintw(local, "ERROR: %s\n", errorstr ? errorstr : "Failed to create socket");
        wrefresh(local);
      }
    }
    else // gethostbyname failed
    {
      switch (h_errno)
      {
        case HOST_NOT_FOUND:
          wprintw(local, "ERROR: The specified host is unknown\n");
          wrefresh(local);
          break;
        case NO_DATA:
          wprintw(local, "ERROR: The requested name is valid but does not have an IP address\n");
          wrefresh(local);
          break;
        case NO_RECOVERY:
          wprintw(local, "ERROR: A non-recoverable name server error occurred\n");
          wrefresh(local);
          break;
        case TRY_AGAIN:
          wprintw(local, "ERROR: A temporary error occurred on an authoritative name server, try again later\n");
          wrefresh(local);
          break;
        default:
          wprintw(local, "ERROR: Failed to resolve hostname\n");
          wrefresh(local);
          break;
      }
    }
  }

  return client_fd;
}


int read_and_forward(int srcfd, int dstfd)
{
  int retval = 0;
  char buffer[BUFFER_SIZE];
  ssize_t readlen;
  ssize_t writelen;
  size_t written = 0;

  // this function read data from srcfd and then writes data to dstfd. Special
  // cases are introduced if srcfd or dstfd is one of stdin, stdout or stderr
  // to facilitate correct ncurses output

  // read from the srcfd to the buffer
  // if srcfd is stdin use ncurses routines
  if (srcfd == STDIN_FILENO)
  {
    buffer[0] = wgetch(local);
    buffer[1] = 0;
    readlen = 1;
    wprintw(local, "%c", buffer[0]);
    wclrtoeol(local);
    wrefresh(local);
  }
  else
  {
    readlen = read(srcfd, buffer, sizeof(buffer) - 1);
    buffer[readlen] = 0;
  }
  if (readlen > 0)
  {
    // if dstfd is stdout or stderr use ncurses routines
    if (dstfd == STDOUT_FILENO || dstfd == STDERR_FILENO) // output through ncurses
    {
      wprintw(remote, "%s", buffer);
      wclrtoeol(remote);
      wrefresh(remote);
    }
    else // normal output
    {
      // start writing until the entire contents of the buffer are written to 
      // dstfd
      do
      {
        writelen = write(dstfd, &buffer[written], readlen - written);
        if (writelen > 0)
        {
          written += writelen;
        }
        else if (writelen < 0) // error in write
        {
          retval = -1;
        }
      }
      while (written < readlen && !retval);
    }
  }
  else if (readlen == 0) // eof
  {
    // this is a normal condition, do not print an error and set the return 
    // value to a recognizable value
    retval = RETVAL_EOF;
  }
  else if (readlen == -1) // error in read
  {
    retval = -1;
  }

  return retval;
}


int chat_with_fd(int fd)
{
  int retval = 0;
  int cnt;
  int highest;
  fd_set readfds;
  fd_set exceptfds;

  // we wait for data on either stdin or the remote fd with select. When 
  // select returns we first check for errors. If no errors have occurred we 
  // handle any waiting data on either stdin or the fd.
  while (!retval)
  {
    // prepare the file descriptor sets for select
    FD_ZERO(&readfds);
    FD_ZERO(&exceptfds);
    FD_SET(STDIN_FILENO, &readfds);
    FD_SET(fd, &readfds);
    FD_SET(STDIN_FILENO, &exceptfds);
    FD_SET(fd, &exceptfds);
    highest = (STDIN_FILENO > fd ? STDIN_FILENO : fd) + 1;
    // wait for data (or exceptions) on stdin and/or the fd
    cnt = select(highest, &readfds, 0, &exceptfds, 0);
    if (cnt > 0) // at least one file descriptor changed
    {
      if (FD_ISSET(STDIN_FILENO, &exceptfds)) // error on stdin
      {
        retval = -1;
      }
      else if (FD_ISSET(fd, &exceptfds)) // error on file descriptor
      {
        retval = -1;
      }
      else // no errors
      {
        if (FD_ISSET(STDIN_FILENO, &readfds)) // data on stdin
        {
          retval = read_and_forward(STDIN_FILENO, fd);
          if (retval == RETVAL_EOF)
          {
            wprintw(local, ">> Terminating session <<\n");
            wrefresh(local);
          }
        }
        if (FD_ISSET(fd, &readfds)) // data on file descriptor
        {
          retval = read_and_forward(fd, STDOUT_FILENO);
          if (retval == RETVAL_EOF)
          {
            wprintw(remote, ">> Remote closed connection <<\n");
            wrefresh(remote);
          }
        }
      }
    }
    else if (cnt == -1) // select failed
    {
      retval = -1;
    }
  }

  return retval;
}


int init_screen(void)
{
  int retval = 0;

  // if an error is detected this function will roll back any allocations or 
  // initializations, there is no need to call destroy_screen(). Failure of 
  // ncurses initialization is in principle not fatal, the program can in 
  // theory switch back to non-ncurses mode, although we have not implemented 
  // this behaviour
  if (initscr())
  {
    if (noecho() != ERR) // no input echo, we want control over output
    {
      if (cbreak() != ERR) // interrupt after each character instead of after each newline
      {
        if (intrflush(stdscr, FALSE) != ERR)
        {
          if (keypad(stdscr, TRUE) != ERR)
          {
            // draw a box around the screen
            // i don't use box, its window is too large
            // we don't check return values of the following functions
            // it is not critical if they fail
            mvwhline(stdscr, 0, 0, ACS_ULCORNER, 1);
            mvwhline(stdscr, 0, 1, ACS_HLINE, COLS - 2);
            mvwhline(stdscr, 0, COLS - 1, ACS_URCORNER, 1);
            mvwvline(stdscr, 1, 0, ACS_VLINE, LINES - 3);
            mvwvline(stdscr, 1, COLS - 1, ACS_VLINE, LINES - 3);
            mvwhline(stdscr, LINES - 2, 0, ACS_LLCORNER, 1);
            mvwhline(stdscr, LINES - 2, 1, ACS_HLINE, COLS - 2);
            mvwhline(stdscr, LINES - 2, COLS - 1, ACS_LRCORNER, 1);
            // split screen, lower screen is local
            mvwhline(stdscr, (LINES/2) - 1, 0, ACS_LTEE, 1);
            mvwhline(stdscr, (LINES/2) - 1, 1, ACS_HLINE, COLS - 2);
            mvwprintw(stdscr, (LINES/2) - 1, 2, "[ Local ]");
            mvwhline(stdscr, (LINES/2) - 1, COLS - 1, ACS_RTEE, 1);
            remote = newwin((LINES/2) - 2, COLS - 2, 1, 1);
            if (remote)
            {
              if (scrollok(remote, TRUE) != ERR)
              {
                local = newwin((LINES/2) - 2 + (LINES%2), COLS - 2, (LINES/2), 1);
                if (local)
                {
                  if (scrollok(local, TRUE) != ERR)
                  {
                    refresh();
                  }
                  else // scrollok failed
                  {
                    retval = -1;
                    delwin(local);
                    delwin(remote);
                    endwin();
                  }
                }
                else // newwin failed
                {
                  retval = -1;
                  delwin(remote);
                  endwin();
                }
              }
              else // scrollok failed
              {
                retval = -1;
                delwin(remote);
                endwin();
              }
            }
            else // newwin failed
            {
              retval = -1;
              endwin();
            }
          }
          else // keypad failed
          {
            retval = -1;
            endwin();
          }
        }
        else // intrflush failed
        {
          retval = -1;
          endwin();
        }
      }
      else // cbreak failed
      {
        retval = -1;
        endwin();
      }
    }
    else // noecho failed
    {
      retval = -1;
      endwin();
    }
  }
  else // initscr failed
  {
    retval = -1;
  }
 
  return retval;
}


void destroy_screen(void)
{
  delwin(local);
  delwin(remote);
  endwin();
}
