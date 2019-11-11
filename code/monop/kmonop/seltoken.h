/*
Declaration of the select-token class.

Erik van Zijst, icehawk@xs4all.nl, 24/07/98
*/
#ifndef SELTOKEN_H
#define SELTOKEN_H

#include "globals.h"
#include "kbutton.h"

/**
 * This is the class where the player can select a token.
 *
 * @short SelectToken class
 * @author Erik van Zijst <icehawk@rans.aap.cx>
 * @version 0.1
 */

class SelectToken:public QDialog
{
	Q_OBJECT
private:
	int current;
	QButtonGroup *group;
	QPixmap *pics[10];
	KButton *tokenbt[10], *ok;
	QPushButton *cancel;
public:
	SelectToken(QWidget *parent, const char *name);
	~SelectToken();
	void resizeEvent(QResizeEvent *e);
	void closeEvent(QCloseEvent *e);
public slots:
	void cancel_clicked();
	void t0clicked();
	void t1clicked();
	void t2clicked();
	void t3clicked();
	void t4clicked();
	void t5clicked();
	void t6clicked();
	void t7clicked();
	void t8clicked();
	void t9clicked();
};
#endif

