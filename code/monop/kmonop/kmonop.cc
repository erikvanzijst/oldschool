/*
 * kmonop.cpp
 *
 * Copyright (C) 1999 Leon van Zantvoort <cybor@prutser.cx>
 */
#include "kmonop.h"
#include "network.h"
#include "globals.h"
#include "terminal.h"
#include "personal.h"
#include "playertable.h"

#include <qkeycode.h>

#include <kfm.h>
#include <kiconloader.h>


extern MNetwork *mnetwork;
extern MTerminal *terminal;
extern MPlayerTable *playerTable;

Kmonop::Kmonop()
	: view(new KmonopWidget(this))
{
	// tell the KTMainWindow that this is indeed the main widget
	mnetwork = new MNetwork(this,"network");

	setView(view);

	// create a DropZone over the entire window and connect it
	// to the slotDropEvent
	connect(new KDNDDropZone(this, DndURL), 
	        SIGNAL(dropAction(KDNDDropZone *)), 
	        SLOT(slotDropEvent(KDNDDropZone *)));


	// create a popup menu -- in this case, the File menu
	QPopupMenu* p = new QPopupMenu;
	QPopupMenu* options = new QPopupMenu;

	p->insertItem(i18n("&Quit"), kapp, SLOT(quit()), CTRL+Key_Q);
	options->insertItem(i18n("&Personal settings"), this, SLOT(slotPersonalOptions()), CTRL+Key_P);
	// put our newly created menu into the main menu bar
	menuBar()->insertItem(i18n("&File"), p);
	menuBar()->insertItem(i18n("&Options"), options);
	menuBar()->insertSeparator();

	// KDE will generate a short help menu automagically
	p = kapp->getHelpMenu(true, 
			 i18n("Kmonop\n\n"
					"(c) 1999 Leon van Zantvoort \n"
					));
	menuBar()->insertItem(i18n("&Help"), p);

	// insert a quit button.  the icon is the standard one in KDE
	toolBar()->insertButton(Icon("exit.xpm"),   // icon
	                        0,                  // button id
									SIGNAL(clicked()),  // action
									kapp, SLOT(quit()), // result
									i18n("Exit"));      // tooltip text
	toolBar()->insertButton(Icon("mini-connect.xpm"),   // icon
	                        0,                  // button id
									SIGNAL(clicked()),this,  // action
									SLOT(slotMakeConnection()), // result
									i18n("Conn"));      // tooltip text
	// we do want a status bar
	enableStatusBar();
	personalWidget = new MPersonalOptions(0,"personal options");
	playerTable = new MPlayerTable(0, "playerTable");
}

Kmonop::~Kmonop()
{
}

void Kmonop::slotPersonalOptions()
{
	personalWidget->show();
}

void Kmonop::slotDropEvent(KDNDDropZone *zone)
{
	// the user dropped something on our window.
	QString url, temp_file;

	// get the URL pointing to the dropped file
	url = zone->getURLList().first();

	// let KFM grab the file
	if (KFM::download(url, temp_file))
	{
		// 'temp_file' now contains the absolute path to a temp file
		// with the contents of the the dropped file.  You would presumably
		// handle it right now.

		// after you are done handling it, let KFM delete the temp file
		KFM::removeTempFile(temp_file);
	}
}

void Kmonop::slotMakeConnection()
{
	terminal->show();
	mnetwork->makeConnection("localhost",2525);
	playerTable->show();
}

void Kmonop::saveProperties(KConfig *config)
{
	// the 'config' object points to the session managed
	// config file.  anything you write here will be available
	// later when this app is restored
	
	// e.g., config->writeEntry("key", var); 
}

void Kmonop::readProperties(KConfig *config)
{
	// the 'config' object points to the session managed
	// config file.  this function is automatically called whenever
	// the app is being restored.  read in here whatever you wrote
	// in 'saveProperties'

	// e.g., var = config->readEntry("key"); 
}

#include "kmonop.moc"
