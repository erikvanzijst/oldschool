#include "player.h"
#include "globals.h"
#include "street.h"

extern MStreet *street[40];
extern MPlayer *mplayer[MAXPLAYERS];

MPlayer::MPlayer(QObject *parent, const char *name):QObject(parent,name)
{
	flushData();
}

MPlayer::~MPlayer()
{
}

void MPlayer::flushData()
{
	pos=-1;
	ready=false;
	tokenNr=-1;
	nickName="empty";	
	domainName="empty";
}

void MPlayer::setNick(QString nick)
{
 	nickName = nick.copy();
}

void MPlayer::setToken(int token)
{
 	tokenNr=token;
}

void MPlayer::setDomainName(QString fqdn)
{
	domainName=fqdn;	
}

void MPlayer::setPos(int streetNr)
{
 	int oldPos=pos;
 	pos=streetNr;
 	street[pos]->refresh();
 	if (oldPos != -1)
	 	street[oldPos]->refresh();
	
}

void MPlayer::setReady(bool value)
{
	ready=value;
}

QString MPlayer::getNick()
{
 	return nickName;
}

int MPlayer::getToken()
{
 	return tokenNr;
}

QString MPlayer::getDomainName()
{
 	return domainName;
}

int MPlayer::getPos()
{
 	return pos;
}

bool MPlayer::getReady()
{
	return ready;
}

int MGivePlayer(QString playerName)
{
	int index;
	for (index=0; index < MAXPLAYERS; ++index)
	{
          	if (playerName == mplayer[index]->getNick())
			return index;
	}
	// player not found
	return -1;
}
#include "player.moc"
