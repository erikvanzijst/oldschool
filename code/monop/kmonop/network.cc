/*
	Network
*/

#include "network.h"
#include "globals.h"
#include "parser.h"
#include "terminal.h"

#include <sys/socket.h>
#include <sys/types.h>
#include <sys/utsname.h>
#include <fcntl.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <netinet/in.h>
#include <errno.h>


extern MTerminal *terminal;

MNetwork::MNetwork(QObject *parent, const char *name):QObject(parent,name)
{
	
}

MNetwork::~MNetwork()
{
}

int MNetwork::makeConnection(QString host, int port)
{
   struct sockaddr_in remoteserv;
   struct sockaddr_in sin;
   struct hostent *hptr;

   if( (clientSocket = socket(AF_INET, SOCK_STREAM, 0)) < 0)
   {  // error!!
      switch (errno)
      {
      	case EACCES:
        	printf(
             	"Error while creating a socket.\nPermission to create a socket of the specified type\nand/or protocol is denied. Operation cancelled.\n\nThis is an unusual error, contact the system's Administrator\nor Leon van Zantvoort on cybor@prutser.cx (lead programmer)");
          break;
        case ENOBUFS:
        	printf(
             	"Error while creating a socket.\nInsufficient buffer space is available. The socket cannot be\ncreated until sufficient resources are freed.\n\nThis is an unusual error, contact the system's Administrator\nor Leon van Zantvoort on cybor@prutser.cx (lead programmer)");
          break;
        default:
           printf(
             	"Error while creating a socket.\nAn unknown error occured, server construction aborted.");
      }
      return 0;
   }
   bzero(&remoteserv, sizeof(remoteserv));
   remoteserv.sin_family = AF_INET;
   remoteserv.sin_port = htons(port);
   if(inet_aton(host, &remoteserv.sin_addr) == 0)
   {  // address is a dns, not ip
      if( (hptr = gethostbyname(host)) == NULL)
      {  // dns not found error!
         switch (h_errno)
         {
         	case HOST_NOT_FOUND:
             	printf(
                "Unable to locate the server:\n\nThe server does not have a DNS entry.\n\nCheck the server name and try again.");
             break;
          case TRY_AGAIN:
             printf(
                "No respons received from an authoritative DNS server.\nA retry at a later time may succeed.\nFor more information on this error, see \"man herror\"");
             break;
          case NO_RECOVERY:
             printf(
                "Some unexpected dns server failure was encountered.\nConnection failed.\nFor more information on this error, see \"man herror\"");
             break;
          case NO_DATA:
             printf(
                "The requested name exists, but no address for it is registered\nin the DNS server.\nAnother type of request to the name server using this domain\nname will result in an answer.\nFor more information on this error, see \"man herror\"");
             break;
          default:
             printf(
                "An unknown error occured when looking up the address for host:\nConnection failed.");
         }
         return 0;
      }
      bzero((char *)&sin, sizeof(sin));
      bcopy(hptr->h_addr, (char *)&sin.sin_addr, hptr->h_length);
      if(inet_aton(inet_ntoa(sin.sin_addr), &remoteserv.sin_addr) == 0) return 0;
   }
   if( (::connect(clientSocket, (SA *) &remoteserv, sizeof(remoteserv))) == -1)
   {
      switch (errno)
      {
      	case ECONNREFUSED:
           printf(
              "Connection refused by the server.\nIf the remote server has a kmonop game running, it is definately not at the specified port.");
           break;
        case ETIMEDOUT:
           printf(
              "Timeout while attempting connection.\nConnection failed.");
           break;
        case ENETUNREACH:
           printf(
              "Network is unreachable. Check your settings and media.\nConnection failed.");
           break;
        default:
           printf(
              "An unknown error occured when attempting to connect to the\nremote host.\nCheck your settings and try again.");
      }
      MDisconnect(clientSocket);
      return 0;
   }
   if(!MSetNonBlocking(clientSocket))
   {
      MDisconnect(clientSocket);
      return 0;
   }
   socketNotifier = new QSocketNotifier(clientSocket, QSocketNotifier::Read);
   connect(socketNotifier, SIGNAL(activated(int)),this, SLOT(slotDataFromServer()) );
   return 1;
}

int MNetwork::send2Server(QString sendString)
{
	int length;
	sendString = byteStuffing(sendString, true);
	terminal->send(sendString);
	sendString.append("\r\n");	
	length=sendString.length();
  	write(clientSocket, sendString,length);
}

int MNetwork::slotDataFromServer()
{
	char buffer[MAXLINE+1];
	int len, pos, index;
	bool waarde;
	QString parseString;
	if ( (len = read (clientSocket, buffer, MAXLINE)) != -1)
	{		
		if (len == 0)
		{
			//lost conn
			socketNotifier->~QSocketNotifier();
			printf("No more connection with server");
			return 0;
		}	
		buffer[len] = '\0';
		QString incoming = MUncook(buffer);
		//printf("%s\n",buffer);
		// catch double msgs
		index=0;
		QString tempString;
		QString inc=incoming.copy();
		while ((tempString = MGetFirstString(inc)) != "/error")
		{
			parseString=byteStuffing(tempString,false);
			lastmsg = parseString.copy();
			terminal->incoming(parseString);
			MParser(parseString);	
			inc=MRemoveFirstString(inc);
		}
	}
	return 1;
}

int MSetNonBlocking(int sockfd)
{
	int flags;
	if((flags = fcntl(sockfd, F_GETFL)) < 0)
	{
     	printf(
         "Unknown fcntl() error.\nCouldn't set the socket in nonblocking mode. Operation cancelled.");
      return 0;
	}
	flags = (flags | O_NONBLOCK);
	if((fcntl(sockfd, F_SETFL, flags)) < 0)
	{
      printf(
         "Unknown fcntl() error.\nCouldn't set the socket in nonblocking mode. Operation cancelled.");
      return 0;
	}
	return 1;
}

QString byteStuffing(QString string, bool stuff)
{
	int pos, len, index;

	index=1;
	len = string.length();		
	if (stuff == true)
	{
		while (index < len)
		{		
			if ((pos = string.find('/', index, false)) != -1)
			{
				string=string.insert(pos,'/');
				len++;
				index=pos+2;
			}
			else
				index++;
		}
	}
	else
	{
		while (index < len)
		{
		 	if ((pos = string.find("//", index, false)) != -1)
		 	{
		 	 	string=string.remove(pos,1);
		 	 	len--;
		 	 	index=pos+1;
		 	}
		 	else
		 		index++;
		}		
	}
	bool stop = FALSE;
	QString tmp=string;
	while(!stop)
	{
		if (strlen(tmp)>0)
		{
			if((tmp[strlen(tmp)-1] == '\r')||(tmp[strlen(tmp)-1] == '\n'))
			{
				tmp[strlen(tmp)-1] = '\0';
			}
			else
				stop = TRUE;
		}
		else
			stop = TRUE;
	}
	return tmp;
}

void MDisconnect(int sockfd)
{
	if (::close(sockfd) == -1)
		printf("WARNING: unable to close socket");
}

QString MUncook(char * buffer)
{
   bool stop = FALSE;
   char *tmp = buffer;
   while(!stop)
   {
      if((tmp[strlen(tmp)-1] == '\r')||(tmp[strlen(tmp)-1] == '\n'))
         tmp[strlen(tmp)-1] = '\0';
      else
         stop = TRUE;
   }
   QString string;
   string=tmp;
   return string;
};

QString MGetFirstString(QString incoming)
{
	int pos, tempPos;
	QString returnString;
	// where are the single slashes
	pos = incoming.find("/",1,false);
	tempPos = incoming.find("//",1,false);
	if (pos == tempPos) // geen enkele-slash gevonden
		pos = -1;

	// geen commando gevonden
	if (pos == -1)
	{
		if (incoming.length() <1)
			return "/error";
		else
			returnString=incoming.copy();
	}
	else
		returnString = incoming.left(pos);

	return returnString;
}

QString MRemoveFirstString(QString incoming)
{
	int pos, tempPos, len;
	QString returnString;

	pos = incoming.find("/",1,false);
	tempPos = incoming.find("//",1,false);
	if (pos == tempPos) // geen enkele-slash gevonden
		pos = -1;
        len = incoming.length();

	// geen commando gevonden
	if (pos == -1)
	{
			return "/error";
	}
	else
		returnString = incoming.right(len-pos);

	return returnString;
}


#include "network.moc"

