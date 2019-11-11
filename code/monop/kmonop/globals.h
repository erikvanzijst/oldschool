/*
Declaration of all the includes.
Check the globals.cc, this file is not related to this one, but
it implements all the global variables. If you wish to use them
in other files, declarate them as "extern".

Leon van Zantvoort cybor@prutser.cx 30/7/99
*/

#ifndef GLOBALS_H
#define GLOBALS_H

// Qt includes
#include <qlineedit.h>
#include <qpopmenu.h>
#include <qmenubar.h>
#include <qapp.h>
#include <qpushbt.h>
#include <qkeycode.h>
#include <qtableview.h>
#include <qdialog.h>
#include <qtimer.h>
#include <qlabel.h>
#include <qobject.h>
#include <qgrpbox.h>
#include <qevent.h>
#include <qcombo.h>
#include <qlined.h>
#include <qradiobt.h>
#include <qchkbox.h>
#include <qtabdlg.h>
#include <qtooltip.h>
#include <qmsgbox.h>
#include <qlistbox.h>
#include <qwidget.h>
#include <qspinbox.h>
#include <qstring.h>
#include <qpixmap.h>
#include <qsocknot.h>
#include <qregexp.h>
#include <qlistview.h>
#include <qdialog.h>
#include <qfont.h>
#include <qbuttongroup.h>
#include <qmultilinedit.h>
#include <qmainwindow.h>
#include <qstatusbar.h>
#include <qtoolbar.h>
#include <qpixmap.h>

// Normal includes
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/utsname.h>
#include <fcntl.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <netinet/in.h>
#include <errno.h>

// K includes
#include <kapp.h>
#include <kbutton.h>
#include <kconfig.h>
#include <klocale.h>
#include <mediatool.h>
#include <kaudio.h>
#include <kwm.h>
#include <kmenubar.h>
#include <ktopwidget.h>
#include <ktoolbar.h>
#include <kiconloader.h>
#include <kstatusbar.h>

// Custom defines
#define SA struct sockaddr
#define _UTS_NAMESIZE 16
#define _UTS_NODESIZE 256
#define MAXLINE 1024
#define MAXPLAYERS 6

// Street defines
#define STREET 0
#define GO 1
#define FUNDS 2
#define INCTAX 3
#define STATION 4
#define CHANCE 5
#define JAIL 6
#define ELECTRA 7
#define PARK 8
#define AQUA 9
#define GOTOJAIL 10
#define EXTRATAX 11
// Street owner defines
#define BANK -2
#define NOBODY -1
#define PLAYER1 0
#define PLAYER2 1
#define PLAYER3 2
#define PLAYER4 3
#define PLAYER5 4
#define PLAYER6 5

#define KMONOP_NAME	"Kmonop"
#define KMONOP_VERSION 	"0.2"
#define KMONOP_DATE		"16/02/99"
#define KMONOP_AUTHORS 	"Erik van Zijst, Leon van Zantvoort"

//player vars
//bool GReady = false;
//QString GNick;

extern KConfig *config;

#endif
