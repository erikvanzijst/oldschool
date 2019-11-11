#include "globals.h"
#include "player.h"
#include "parser.h"
#include "network.h"
#include "playertable.h"

extern MNetwork *mnetwork;
extern MPlayer *mplayer[MAXPLAYERS];
extern MPlayerTable *playerTable;

void MParser(QString incoming)
{
	if (incoming.contains("Login:",FALSE)!=0)
	{
		KConfig *config = KApplication::getKApplication()->getConfig();;
		mnetwork->send2Server(config->readEntry("nick"));
	}
	if (incoming.contains("Password:",FALSE)!=0)
			mnetwork->send2Server("");
	if (MGetCommand(incoming) == "/names")
		MParserPlayer(incoming);
	if (MGetCommand(incoming) == "/nick")
		MParserPlayer(incoming);
	if (MGetCommand(incoming) == "/token")
		MParserPlayer(incoming);
	if (MGetCommand(incoming) == "/fqdn")
		MParserPlayer(incoming);
	if (MGetCommand(incoming) == "/ready")
		MParserPlayer(incoming);
	if (MGetCommand(incoming) == "/quit")
		MParserPlayer(incoming);
	if (MGetCommand(incoming) == "/startgame")
		MParserGame(incoming);
	if (MGetCommand(incoming) == "/pos")
		MParserBoard(incoming);
}

void MParserPlayer(QString incoming)
{
	if (MGetCommand(incoming) == "/nick")
		PlayerNick(MRemoveCommand(incoming));
	if (MGetCommand(incoming) == "/name");
	if (MGetCommand(incoming) == "/token")
		PlayerToken(MRemoveCommand(incoming));
	if (MGetCommand(incoming) == "/email");
	if (MGetCommand(incoming) == "/fqdn")
		PlayerFqdn(MRemoveCommand(incoming));
	if (MGetCommand(incoming) == "/ipnr");
	if (MGetCommand(incoming) == "/names")
		PlayerNames(MRemoveCommand(incoming));
	if (MGetCommand(incoming) == "/ready")
		PlayerReady(MRemoveCommand(incoming));
	if (MGetCommand(incoming) == "/quit")
		PlayerQuit(MRemoveCommand(incoming));
	playerTable->refresh();
}

void MParserBoard(QString incoming)
{
	if (MGetCommand(incoming) == "/pos")
		BoardPos(MRemoveCommand(incoming));
}

void MParserGame(QString incoming)
{
	if (MGetCommand(incoming) == "/startgame")
		GameStartgame(MRemoveCommand(incoming));
}

void PlayerNick(QString incoming)
{
	int player;
	player = MGivePlayer(MGetFirstWord(incoming));
	if (player != -1)
	{
		incoming = MRemoveFirstWord(incoming);	
		mplayer[player]->setNick(incoming);
	}
}

void PlayerToken(QString incoming)
{
	int player;
	player = MGivePlayer(MGetFirstWord(incoming));
	if (player != -1)
	{
		incoming = MRemoveFirstWord(incoming);	
		QString token=MGetFirstWord(incoming);
		mplayer[player]->setToken(token.toInt());
	}
}

void PlayerFqdn(QString incoming)
{
	int player;
	player = MGivePlayer(MGetFirstWord(incoming));
	if (player != -1)
	{
		incoming = MRemoveFirstWord(incoming);	
		mplayer[player]->setDomainName(MGetFirstWord(incoming));
	}
}

void PlayerNames(QString playerNames)
{
	int index;
	QString tempString;
	QString playerString=playerNames.copy();
	
	for (index=0; index < MAXPLAYERS; ++index)
	{
		mplayer[index]->flushData();	
	}
	index=0;
	
	while ((tempString = MGetFirstWord(playerString)) != "/error")
	{
		mplayer[index++]->setNick(tempString);
		//requesting player's token

		mnetwork->send2Server("/token get " +tempString);
		mnetwork->send2Server("/fqdn get " +tempString);
		mnetwork->send2Server("/ready get " +tempString);
		playerString=MRemoveFirstWord(playerString);
	}
}

void PlayerReady(QString incoming)
{
	int player;
	player = MGivePlayer(MGetFirstWord(incoming));
	if (player != -1)
	{
		incoming = MRemoveFirstWord(incoming);	
		if (MGetFirstWord(incoming)=="1")
			mplayer[player]->setReady(true);
		else
			mplayer[player]->setReady(false);
	}
}

void PlayerQuit(QString incoming)
{
	mnetwork->send2Server("/names get");
}

void BoardPos(QString incoming)
{
	int player;
	player = MGivePlayer(MGetFirstWord(incoming));
	if (player != -1)
	{
		incoming = MRemoveFirstWord(incoming);	
		QString pos=MGetFirstWord(incoming);
		mplayer[player]->setPos(pos.toInt());
	}
}

void GameStartgame(QString incoming)
{
	int index;
	for (index=0; index < MAXPLAYERS; ++index)
		if (mplayer[index]->getReady()==true)
			mplayer[index]->setPos(0);
}

QString MGetCommand(QString incoming)
{
	int pos;
	QString returnString;
	pos = incoming.find(" ",0,false);

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

QString MRemoveCommand(QString incoming)
{
	int pos, len;
	QString returnString;

	pos = incoming.find(" ",0,false);
        len = incoming.length();

	// geen commando gevonden
	if (pos == -1)
	{
		return "/error";
	}
	else
		returnString = incoming.right(len-pos-1);

	return returnString;
}

QString MGetFirstWord(QString incoming)
{
	int pos;
	QString returnString;
	pos = incoming.find(" ",0,false);

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

QString MRemoveFirstWord(QString incoming)
{
	int pos, len;
	QString returnString;

	pos = incoming.find(" ",0,false);
        len = incoming.length();

	// geen commando gevonden
	if (pos == -1)
	{
		return "/error";
	}
	else
		returnString = incoming.right(len-pos-1);

	return returnString;
}
