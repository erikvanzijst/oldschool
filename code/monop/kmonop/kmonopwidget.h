#ifndef KMONOPWIDGET_H 
#define KMONOPWIDGET_H 

#ifdef HAVE_CONFIG_H
#include <config.h>
#endif 

#include <qwidget.h>
#include "street.h"
#include "board.h"

/**
 * This class is the main view for Kmonop.  Most non-menu, non-toolbar,
 * and non-status bar GUI code should go here.
 *
 * @short Main view
 * @author Leon van Zantvoort <cybor@prutser.cx>
 * @version 0.1
 */
class KmonopWidget : public QWidget
{
	Q_OBJECT
public:
	/**
	 * Default constructor
	 */
	KmonopWidget(QWidget *parent = 0, const char *name = 0);
 	void resizeEvent (QResizeEvent *e);

	/**
	 * Destructor
	 */
	virtual ~KmonopWidget();
private:
	MBoard *board;
	MStreetFrame *streetFrame[40];
	QFrame *terminalView;
	QFrame *playerView;

	/*temp*/
	QPushButton *setReady;
	QPushButton *rollDice;
private slots:
	void slotSetReady();
	void slotRollDice();
};

#endif // KMONOPWIDGET_H 
