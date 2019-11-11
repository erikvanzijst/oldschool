#ifndef CONNECTED_H
#define CONNECTED_H

#include "globals.h"

/*
 * This class has nothing to do with the network conn,
 * it's only a class which shows all connected players
 */


class MPlayerTable:public QWidget
{
	Q_OBJECT
private:
public:
	MPlayerTable(QWidget *parent=0, const char *name=0);
	~MPlayerTable();
	void resizeEvent (QResizeEvent *e);
	void refresh();
private:
//	QListView *playerTable;
	QFrame *borderFrame;
	QFrame *readyFrame;
	QFrame *playerFrame;
	KButton *readyButton[MAXPLAYERS];
	QLabel *numberLabel[MAXPLAYERS];
	QLabel *nameLabel[MAXPLAYERS];
	QLabel *hostLabel[MAXPLAYERS];
	KButton *tokenButton[MAXPLAYERS];
public slots:
	void slotReadyClicked();
	void slotTokenClicked();
};

#endif
