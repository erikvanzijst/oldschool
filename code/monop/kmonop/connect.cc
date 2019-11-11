
#include <qmultilinedit.h>
#include "globals.h"
#include "personal.h"

MPersonalOptions::MPersonalOptions(QWidget *parent, const char *name):QWidget(parent,name)
{
	setCaption("Personal settings");
	setMaximumSize(500,300);
	setMinimumSize(500,300);

	pictureFrame = new QFrame(this);
	pictureFrame->setFrameStyle( QFrame::Panel | QFrame::Raised );
	identFrame = new QFrame(this);
	identFrame->setFrameStyle( QFrame::Panel | QFrame::Sunken );

	nameLineEdit = new QLineEdit(this,"name");
	nameLabel = new QLabel(nameLineEdit,"&Real Name",this);
	nickLineEdit = new QLineEdit(this,"nick name");
	nickLabel = new QLabel(nickLineEdit,"&Nick Name",this);
	emailLineEdit = new QLineEdit(this,"email address");
	emailLabel = new QLabel(emailLineEdit,"&Email address",this);
	ageLineEdit = new QLineEdit(this,"age");
	ageLabel = new QLabel(ageLineEdit,"&Age",this);

	nameLineEdit->setFocus();
}

MPersonalOptions::~MPersonalOptions()
{
}


void MPersonalOptions::resizeEvent (QResizeEvent *e)
{
	pictureFrame->setGeometry(10,10,175,height()-20);
	identFrame->setGeometry(200,10,280,175);
	nameLabel->setGeometry(210,20,80,30);
	nameLineEdit->setGeometry(300,20,150,25);
	nickLabel->setGeometry(210,60,80,30);
	nickLineEdit->setGeometry(300,60,150,25);
	emailLabel->setGeometry(210,100,80,30);
	emailLineEdit->setGeometry(300,100,150,25);
	ageLabel->setGeometry(210,140,80,30);
	ageLineEdit->setGeometry(300,140,150,25);
}

#include "personal.moc"
