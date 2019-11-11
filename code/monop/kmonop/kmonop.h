#ifndef KMONOP_H 
#define KMONOP_H 

#ifdef HAVE_CONFIG_H
#include <config.h>
#endif 

#include <kapp.h>
#include <ktmainwindow.h>

#include "network.h"
#include "kmonopwidget.h"
#include "personal.h"

/**
 * This class serves as the main window for Kmonop.  It handles the
 * menus, toolbars, and status bars.
 *
 * @short Main window class
 * @author Leon van Zantvoort <cybor@prutser.cx>
 * @version 0.1
 */
class Kmonop : public KTMainWindow
{
	Q_OBJECT
public:
	/**
	 * Default Constructor
	 */

	Kmonop();

	/**
	 * Default Destructor
	 */
	virtual ~Kmonop();

public slots:
	void slotPersonalOptions();
	/**
	 * This is called whenever the user Drag n' Drops something into our
	 * window
	 */
	void slotDropEvent(KDNDDropZone *);
	void slotMakeConnection();

protected:
	/**
	 * This function is called when it is time for the app to save its
	 * properties for session management purposes.
	 */
	void saveProperties(KConfig *);

	/**
	 * This function is called when this app is restored.  The KConfig
	 * object points to the session management config file that was saved
	 * with @ref saveProperties
	 */
	void readProperties(KConfig *);

private:
	KmonopWidget *view;
	MPersonalOptions *personalWidget;
};

#endif // KMONOP_H 
