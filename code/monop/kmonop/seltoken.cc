/*
Implementation of the select-token class.

Erik van Zijst, icehawk@xs4all.nl, 24/07/98
*/

#include "globals.h"
#include "player.h"
#include "seltoken.h"
#include "kbutton.h"
#include "network.h"

extern QString theme;
extern QString tokennr;	// Global, from main.cc
extern MNetwork *mnetwork;

SelectToken::SelectToken(QWidget *parent, const char *name) : QDialog(parent,name,TRUE)
{
	resize(320,190);
	setCaption("Select token");

	group = new QButtonGroup(this, "group");
	group->setTitle("Select token");

	QString tmp, number;
  char string[255];
	for(int n=0; n<10; n++)
	{
		tokenbt[n] = new KButton(group, "0");
		number.setNum(n);
		tmp = "token0" + number + ".xpm";
    strcpy(string, tmp);
    pics[n] = new QPixmap((const char*) string);
		tokenbt[n]->setPixmap(*pics[n]);
	}
  connect(tokenbt[0], SIGNAL(clicked()), SLOT(t0clicked()) );
  connect(tokenbt[1], SIGNAL(clicked()), SLOT(t1clicked()) );
  connect(tokenbt[2], SIGNAL(clicked()), SLOT(t2clicked()) );
  connect(tokenbt[3], SIGNAL(clicked()), SLOT(t3clicked()) );
  connect(tokenbt[4], SIGNAL(clicked()), SLOT(t4clicked()) );
  connect(tokenbt[5], SIGNAL(clicked()), SLOT(t5clicked()) );
  connect(tokenbt[6], SIGNAL(clicked()), SLOT(t6clicked()) );
  connect(tokenbt[7], SIGNAL(clicked()), SLOT(t7clicked()) );
  connect(tokenbt[8], SIGNAL(clicked()), SLOT(t8clicked()) );
  connect(tokenbt[9], SIGNAL(clicked()), SLOT(t9clicked()) );
	cancel = new QPushButton(this, "cancel");
  cancel->setText("&Cancel");
	connect(cancel, SIGNAL(clicked()), this, SLOT(cancel_clicked()) );
}

SelectToken::~SelectToken()
{
}

void SelectToken::resizeEvent(QResizeEvent *e)
{
	group->setGeometry(10,10, width()-20, height()-10-45);
	for(int n=0; n<10; n++)
	{
		if(n<5)
		{
			tokenbt[n]->setGeometry( ((group->width()-5*40)/10) + n*(((group->width()-5*40)/5)+40), ((group->height()-2*40)/4)+10,40,40);
   	}
		else
		{
			tokenbt[n]->setGeometry( ((group->width()-5*40)/10) + (n-5)*(((group->width()-5*40)/5)+40), 3*((group->height()-2*40)/4)+40, 40,40);
		}
	}
	cancel->setGeometry( (width()-90)/2, height()-35, 90,25);
}

void SelectToken::closeEvent(QCloseEvent *e)
{
	reject();
}

void SelectToken::cancel_clicked()
{
   reject();
}

void SelectToken::t0clicked()
{
   tokennr = "token00.xpm";
   mnetwork->send2Server("/token set 0");
   done(1);
}
void SelectToken::t1clicked()
{
   tokennr = "token01.xpm";
   mnetwork->send2Server("/token set 1");
   done(1);
}
void SelectToken::t2clicked()
{
   tokennr = "token02.xpm";
   mnetwork->send2Server("/token set 2");
   done(1);
}
void SelectToken::t3clicked()
{
   tokennr = "token03.xpm";
   mnetwork->send2Server("/token set 3");
   done(1);
}
void SelectToken::t4clicked()
{
   tokennr = "token04.xpm";
   mnetwork->send2Server("/token set 4");
   done(1);
}
void SelectToken::t5clicked()
{
   tokennr = "token05.xpm";
   mnetwork->send2Server("/token set 5");
   done(1);
}
void SelectToken::t6clicked()
{
   tokennr = "token06.xpm";
   mnetwork->send2Server("/token set 6");
   done(1);
}
void SelectToken::t7clicked()
{
   tokennr = "token07.xpm";
   mnetwork->send2Server("/token set 7");
   done(1);
}
void SelectToken::t8clicked()
{
   tokennr = "token08.xpm";
   mnetwork->send2Server("/token set 8");
   done(1);
}
void SelectToken::t9clicked()
{
   tokennr = "token09.xpm";
   mnetwork->send2Server("/token set 9");
   done(1);
}

#include "seltoken.moc"

