#include "playertable.h"
#include "player.h"
#include "globals.h"
#include "seltoken.h"
#include "network.h"

extern MPlayer *mplayer[MAXPLAYERS];
extern MNetwork *mnetwork;

MPlayerTable::MPlayerTable(QWidget *parent, const char *name):QWidget(parent,name)
{
	int index;
	
	setCaption("Players");
	setMinimumSize(400,45+(MAXPLAYERS*30));
	setMaximumSize(460,45+(MAXPLAYERS*30));
	
	borderFrame = new QFrame (this, "Border");
	borderFrame->setFrameStyle(QFrame::Panel | QFrame::Raised);
	borderFrame->setLineWidth(2);
		
	readyFrame = new QFrame (this, "Ready");
	readyFrame->setFrameStyle(QFrame::Box | QFrame::Raised);
	readyFrame->setLineWidth(2);
	
	playerFrame = new QFrame (this, "Player");
	playerFrame->setFrameStyle(QFrame::Box | QFrame::Raised);
	playerFrame->setLineWidth(2);
	
	KIconLoader *icon;
	icon = new KIconLoader;

	QString number;
			
	for (index =0 ; index < MAXPLAYERS; ++index)
	{
		readyButton[index] = new KButton(this,"button");
		readyButton[index]->setPixmap(icon->loadMiniIcon("mini-stylized-x-full.xpm"));
		connect(readyButton[index], SIGNAL(clicked()), SLOT(slotReadyClicked()) );

		number.setNum(index+1);	
		numberLabel[index]= new QLabel(this,"number");
		numberLabel[index]->setText(number);
		
		nameLabel[index]= new QLabel(this,"name");
		nameLabel[index]->setText("Empty");
	
		hostLabel[index]= new QLabel(this,"host");
		hostLabel[index]->setText("Empty");
		
		tokenButton[index] = new KButton(this,"button");
		tokenButton[index]->setPixmap(icon->loadMiniIcon("mini-stylized-x-full.xpm"));
		connect(tokenButton[index], SIGNAL(clicked()), SLOT(slotTokenClicked()) );
	}
}

MPlayerTable::~MPlayerTable()
{
}

void MPlayerTable::resizeEvent (QResizeEvent *e)
{
	int index;
	borderFrame->setGeometry(3,3,width()-6,height()-6);
	readyFrame->setGeometry(10,10,50,height()-20);
	playerFrame->setGeometry(65,10,width()-80,height()-20);
	
	for (index = 0 ; index < MAXPLAYERS; ++index)
	{
		readyButton[index]->setGeometry(20,20+(index*30),30,30);
		numberLabel[index]->setGeometry(80,20+(index*30),30,30);
		nameLabel[index]->setGeometry(120,20+(index*30),120,30);
		hostLabel[index]->setGeometry(250,20+(index*30),130,30);
		tokenButton[index]->setGeometry(390,20+(index*30),40,30);
	}
}

 /*
  * Updates playerTable
  */
void MPlayerTable::refresh()
{
	int index;

	QString number, tmp;
	QPixmap token;
			
	KIconLoader *icon;
	icon = new KIconLoader;
	
	for (index=0; index < MAXPLAYERS; index++)
	{
		if (strlen(mplayer[index]->getNick())>0)
		{
			if (mplayer[index]->getReady()==true)
				readyButton[index]->setPixmap(icon->loadMiniIcon("mini-exclam.xpm"));
			else
				readyButton[index]->setPixmap(icon->loadMiniIcon("mini-cross.xpm"));
			nameLabel[index]->setText(mplayer[index]->getNick());
			hostLabel[index]->setText(mplayer[index]->getDomainName());
			if (mplayer[index]->getToken() != -1)
			{
				number.setNum(mplayer[index]->getToken());
				tmp = "token0" + number + ".xpm";
				token.load(tmp);
				tokenButton[index]->setPixmap(token);
			}
			else
			{
				readyButton[index]->setPixmap(icon->loadMiniIcon("mini-stylized-x-full.xpm"));
				nameLabel[index]->setText("Empty");
				hostLabel[index]->setText("Empty");
				tokenButton[index]->setPixmap(icon->loadMiniIcon("mini-stylized-x-full.xpm"));
			}
		}
	}
}

void MPlayerTable::slotReadyClicked()
{
//	if (GReady == false)
		mnetwork->send2Server("/ready set 1");
//	else
//		mnetwork->send2Server("/ready set 0");
}

void MPlayerTable::slotTokenClicked()
{
	SelectToken selectToken(0, "token");
	selectToken.exec();
}


#include "playertable.moc"
