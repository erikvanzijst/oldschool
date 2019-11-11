
#include <qmultilinedit.h>
#include "network.h"
#include "globals.h"
#include "personal.h"
#include "seltoken.h"
#include "kbutton.h"

extern MNetwork *mnetwork;
extern QString tokennr;

MPersonalOptions::MPersonalOptions(QWidget *parent, const char *name):QWidget(parent,name)
{
	setCaption("Personal settings");
	setMaximumSize(500,300);
	setMinimumSize(500,300);

	pictureFrame = new QFrame(this);
	pictureFrame->setFrameStyle( QFrame::Panel | QFrame::Sunken );
	pictureFrame->setBackgroundColor (QColor(207,255,207));

	identFrame = new QFrame(this);
	identFrame->setFrameStyle( QFrame::Panel | QFrame::Raised );

	nameLineEdit = new QLineEdit(this,"name");
	nameLabel = new QLabel(nameLineEdit,"&Real Name",this);
	nickLineEdit = new QLineEdit(this,"nick name");
	nickLabel = new QLabel(nickLineEdit,"&Nick Name",this);
	emailLineEdit = new QLineEdit(this,"email address");
	emailLabel = new QLabel(emailLineEdit,"&Email address",this);
//	ageLineEdit = new QLineEdit(this,"age");
//	ageLabel = new QLabel(ageLineEdit,"&Age",this);

	tokenButton = new KButton(this,"token");
	QPixmap pics(tokennr);
	tokenButton->setPixmap(pics);
	connect(tokenButton,SIGNAL(clicked()), SLOT(slotSelectToken() ));
	tokenLabel = new QLabel(tokenButton,"&Token",this);

	saveButton = new QPushButton(this,"save");
	saveButton->setText("Save settings");
	connect(saveButton,SIGNAL(clicked()), SLOT(slotSaveSettings() ));

	nameLineEdit->setFocus();
	
	KConfig *config = KApplication::getKApplication()->getConfig();;
	nameLineEdit->setText(config->readEntry("name"));
	nickLineEdit->setText(config->readEntry("nick"));
	emailLineEdit->setText(config->readEntry("email"));
}

MPersonalOptions::~MPersonalOptions()
{
}

void MPersonalOptions::resizeEvent (QResizeEvent *e)
{
	pictureFrame->setGeometry(10,10,175,280);
	identFrame->setGeometry(200,10,280,225);
	nameLabel->setGeometry(210,20,90,30);
	nameLineEdit->setGeometry(310,20,150,25);
	nickLabel->setGeometry(210,60,90,30);
	nickLineEdit->setGeometry(310,60,150,25);
	emailLabel->setGeometry(210,100,90,30);
	emailLineEdit->setGeometry(310,100,150,25);
//	ageLabel->setGeometry(210,140,90,30);
//	ageLineEdit->setGeometry(310,140,150,25);
	tokenLabel->setGeometry(210,180,90,30);
	tokenButton->setGeometry(310,180,40,40);

	saveButton->setGeometry(300,height()-40,150,25);
}

void MPersonalOptions::slotSelectToken()
{
	SelectToken selectToken(0, "token");
	selectToken.exec();
	QPixmap pics(tokennr);
	tokenButton->setPixmap(pics);
}

void MPersonalOptions::slotSaveSettings()
{
	QString nickCommand, nameCommand, emailCommand;

	nameCommand.append("/name set ");
	nameCommand.append(nameLineEdit->text());
	mnetwork->send2Server(nameCommand);	
	nickCommand.append("/nick set ");
	nickCommand.append(nickLineEdit->text());
	mnetwork->send2Server(nickCommand);	
	emailCommand.append("/email set ");
	emailCommand.append(emailLineEdit->text());
	mnetwork->send2Server(emailCommand);	
	config->writeEntry("name",nameLineEdit->text());
	config->writeEntry("nick",nickLineEdit->text());
	config->writeEntry("email",emailLineEdit->text());
}

#include "personal.moc"
