#include "kmonop.h"
#include "terminal.h"
#include "network.h"
#include <kapp.h>

MNetwork *mnetwork;

int main(int argc, char *argv[])
{
	KApplication app(argc, argv, "kmonop");
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
