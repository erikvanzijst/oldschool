#ifndef NETWORK_H
#define	NETWORK_H

/*
	network
*/

#include "globals.h"
#include "qstring.h"
#include "qsocketnotifier.h"
#include <errno.h>

//int h_errno;

class MNetwork:public QObject
{
	Q_OBJECT
private:

	int clientSocket;

	QSocketNotifier *socketNotifier;
	QString lastmsg;
public:
	MNetwork(QObject *parent, const char *name);
	~MNetwork();
 	
	int makeConnection(QString host, int port);
	int send2Server(QString sendString);
public slots:
	int slotDataFromServer();
};

QString byteStuffing(QString string, bool stuff);
int MSetNonBlocking(int sockfd);
void MDisconnect(int sockfd);
QString MUncook(char * buffer);
QString MGetFirstString(QString incoming);
QString MRemoveFirstString(QString incoming);

#endif
