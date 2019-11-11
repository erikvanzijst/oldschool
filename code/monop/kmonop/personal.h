#ifndef PERSONAL_H
#define	PERSONAL_H
#ifdef HAVE_CONFIG_H
#include <config.h>
#endif 

#include <qstring.h>
#include <qmultilinedit.h>
#include <qlineedit.h>
#include "globals.h"
#include "kbutton.h"

class MPersonalOptions:public QWidget
{
	Q_OBJECT
private:
	QFrame *identFrame;
	QFrame *pictureFrame;
	QLabel *nameLabel;
	QLineEdit *nameLineEdit;
	QLabel *nickLabel;
	QLineEdit *nickLineEdit;
	QLabel *emailLabel;
	QLineEdit *emailLineEdit;
//	QLabel *ageLabel;
//	QLineEdit *ageLineEdit;
	QLabel *tokenLabel;
	KButton *tokenButton;
	QPushButton *saveButton;
public:
	MPersonalOptions(QWidget *parent=0, const char *name=0);
	~MPersonalOptions();
    void resizeEvent (QResizeEvent *e);
private:
public slots:
	void slotSaveSettings();
	void slotSelectToken();
};

#endif
