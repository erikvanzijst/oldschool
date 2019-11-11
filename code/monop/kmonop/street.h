#ifndef STREET_H
#define	STREET_H

#include <qstring.h>
#include "network.h"
#include <qmultilinedit.h>
#include <qlineedit.h>
#include "parser.h"
#include "terminal.h"
#include "globals.h"
#include <qsize.h>

class MStreet:public QFrame
{
	Q_OBJECT
private:
public:
	MStreet(QWidget *parent, int *id);
	~MStreet();
	
	void initStreet(int id);
	void resizeEvent (QResizeEvent *e);
	virtual void mousePressEvent(QMouseEvent *);
	void fillToken();
	void paintEvent(QPaintEvent *);
	void refresh();
private:
	int streetId;
	int value;
	int interest[6];
	int housePrice;
	bool forsale;
	int type;
	int serie;
	
	int owner;
	
//	Grafical part

	
	int borderWidth;
	QPixmap *token[MAXPLAYERS];
	
	QString titel;
	QLabel *titelLabel;
	QLabel *subTitelLabel;
	QLabel *priceLabel;
		
	QFrame *titelFrame;
	QFrame *subTitelFrame;
	QFrame *veldFrame;
	QFrame *line;
	QFrame *bes;
	
	QRect* titelRect;
	QRect* titelLabelRect;
	QRect* priceLabelRect;
	QRect* subTitelLabelRect;
	QRect* subTitelRect;
	QRect* besRect;
	
	QSize* streetSize;
	
	QPopupMenu *popup;
public slots:
	void slotInfo();
};

class MStreetFrame:public QFrame
{
	Q_OBJECT
private:
	int streetId;
public:
	MStreetFrame(QWidget *parent, int *id);
	~MStreetFrame();
	virtual void mousePressEvent(QMouseEvent *);
//	void paintEvent(QPaintEvent *);
};

#endif
