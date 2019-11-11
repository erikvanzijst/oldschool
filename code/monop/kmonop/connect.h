#ifndef PERSONAL_H
#define	PERSONAL_H

#include <qstring.h>
#include <qmultilinedit.h>
#include <qlineedit.h>
#include "globals.h"

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
	QLabel *ageLabel;
	QLineEdit *ageLineEdit;

public:
	MPersonalOptions(QWidget *parent=0, const char *name=0);
	~MPersonalOptions();
    void resizeEvent (QResizeEvent *e);
private:
public slots:
};

#endif
