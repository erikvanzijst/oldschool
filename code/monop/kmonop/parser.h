#ifndef PARSER_H
#define	PARSER_H

#include "globals.h"
#include "qstring.h"

void MParser(QString incoming);
void MParserPlayer(QString incoming);
void MParserBoard(QString incoming);
void MParserGame(QString incoming);

void PlayerNick(QString incoming);
void PlayerToken(QString incoming);
void PlayerFqdn(QString incoming);
void PlayerNames(QString playerNames);
void PlayerReady(QString incoming);
void PlayerQuit(QString incoming);

void BoardPos(QString incoming);

void GameStartgame(QString incoming);

QString MGetCommand(QString incoming);
QString MRemoveCommand(QString incoming);
QString MGetFirstWord(QString incoming);
QString MRemoveFirstWord(QString incoming);

#endif
