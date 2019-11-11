#ifndef TERMINAL_H
#define	TERMINAL_H

/*
	network
*/

#include <qstring.h>
#include "network.h"
#include <qmultilinedit.h>
#include <qlineedit.h>
#include "parser.h"
#include "terminal.h"

class MTerminal:public QWidget
{
	Q_OBJECT
private:
public:
	MTerminal(QWidget *parent=0, const char *name=0);
	~MTerminal();
	void send(QString sendString);
	void incoming(QString sendString);
	void resizeEvent (QResizeEvent *e);
	void append(QString message);
private:
	QMultiLineEdit *connTerminal;	
	QLineEdit *lineEdit;
public slots:
	void slotSendIt();
};

#endif
