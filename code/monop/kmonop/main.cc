#include "player.h"
#include "kmonop.h"
#include "terminal.h"
#include "network.h"
#include "playertable.h"
#include "globals.h"
#include <kapp.h>

MNetwork *mnetwork;
MPlayer *mplayer[MAXPLAYERS];
MPlayerTable *playerTable;
KConfig *config;
QString tokennr;

void initmain()
{
	config = kapp->getConfig();
}

int main(int argc, char *argv[])
{
	int index;
	// player objects will be created now
	for (index=0; index < MAXPLAYERS; ++index)
		mplayer[index] = new MPlayer(0,"player");

	KApplication app(argc, argv, "kmonop");
	
	initmain();
	
	tokennr.setStr("token00.xpm");	

	// All session management is handled in the RESTORE macro
	if (app.isRestored())
	{
		RESTORE(Kmonop)
	}
	else
	{
		Kmonop *widget = new Kmonop;
		widget->show();
	}
	return app.exec();
}
