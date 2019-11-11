#ifndef BOARD_H
#define BOARD_H

#include <qwidget.h>
#include "street.h"
#include "playertable.h"

/**
 * This class is the main view for Kmonop.  Most non-menu, non-toolbar,
 * and non-status bar GUI code should go here.
 *
 * @short Main view
 * @author Leon van Zantvoort <cybor@prutser.cx>
 * @version 0.1
 */
class MBoard : public QWidget
{
	Q_OBJECT
public:
	/**
	 * Default constructor
	 */
	MBoard(QWidget *parent = 0, const char *name = 0);
 	void resizeEvent (QResizeEvent *e);

	/**
	 * Destructor
	 */
	virtual ~MBoard();
private:
	MStreetFrame *streetFrame[40];
	QMultiLineEdit *info;
	/*temp*/
	//QPushButton *setReady;
	//QPushButton *rollDice;
private slots:
	void slotSetReady();
	void slotRollDice();
};

#endif
