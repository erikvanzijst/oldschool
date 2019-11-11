#ifndef PLAYER_H
#define	PLAYER_H

#include "globals.h"
#include "qstring.h"

// Player object will be created in the main
class MPlayer:public QObject
{
	Q_OBJECT
private:
	QString nickName;
	QString realName;
	QString email;
	QString domainName;
	QString ipNr;
	
	int tokenNr;
	int pos;
	int ready;	
public:
	MPlayer(QObject *parent, const char *name);
	~MPlayer();
	void flushData();
	void setNick(QString nick); 	
	void setToken(int token); 	
	void setDomainName(QString fqdn); 	
	void setPos(int streetNr); 	
	void setReady(bool value); 	
	QString getNick();
	int getToken();
	QString getDomainName();
	int getPos();
	bool getReady();
public slots:
};

int MGivePlayer(QString playerName);
#endif
