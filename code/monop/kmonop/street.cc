#include "kmonopwidget.h"
#include "network.h"
#include <qmultilinedit.h>
#include <qlineedit.h>
#include "globals.h"
#include "parser.h"
#include "street.h"
#include "player.h"
#include <qsize.h>
#include <qpainter.h>
#include <qevent.h>

extern MPlayer *mplayer[MAXPLAYERS];
extern MTerminal *terminal;

MStreet::MStreet(QWidget *parent, int *id):QFrame(parent,"")
{
	streetId=*id;
	setFrameStyle(QFrame::Box | QFrame::Plain);	
}

MStreet::~MStreet()
{
}

void MStreet::initStreet(int id)
{

	borderWidth=2;
//	setBackgroundColor (QColor(145,235,145));
	setBackgroundColor (QColor(207,255,207));
	setLineWidth(0);
  	
	if (id % 10 > 0)
	{
		titelFrame=new QFrame(this);
		titelFrame->setBackgroundColor(QColor(125,0,125));
	}	
	
/*	titelLabel=new QLabel(this);
	titelLabel->setBackgroundColor(QColor(145,235,145));
	titelLabel->setAlignment(AlignCenter);
	titelLabel->setFont(QFont("latin1",10));
	titelLabel->setText("Kalver");

	subTitelLabel=new QLabel(this);
	subTitelLabel->setBackgroundColor(QColor(145,235,145));
	subTitelLabel->setAlignment(AlignCenter);
	subTitelLabel->setFont(QFont("latin1",10));
	subTitelLabel->setText("Straat");

	if (id % 10 > 0)
	{
    		priceLabel=new QLabel(this);
		priceLabel->setBackgroundColor(QColor(145,235,145));
		priceLabel->setAlignment(AlignCenter);
		priceLabel->setFont(QFont("latin1",10));
		priceLabel->setText("f 40000,-");
	}*/
	if (id % 10 > 0)
	{
	    	line = new QFrame(this,"");
    		line->setLineWidth(borderWidth);
		if ( (id > 0 && id < 10) || ( id > 20 && id < 30))
			line->setFrameStyle(QFrame::HLine | QFrame::Plain);
		else
		{
			line->setFrameStyle(QFrame::VLine | QFrame::Plain);
		//	subTitelLabel->setFont(QFont("latin1",8));
		//	priceLabel->setFont(QFont("latin1",8));
		}
	}
	
	popup = new QPopupMenu;
	popup->insertItem("Info",this,SLOT(slotInfo()))	;
	fillToken();
}


void MStreet::resizeEvent (QResizeEvent *e)
{
	int index;
	int width=this->width();
	int height=this->height();

	if (streetId == 0 || streetId == 10 || streetId == 20 || streetId == 30)
	{
//		titelLabel->setGeometry(borderWidth,borderWidth+(height *0.23)+borderWidth+1,width -(2*borderWidth),(height/5)-5);
//		subTitelLabel->setGeometry(borderWidth,(height* 0.5)+1,width-(2*borderWidth),(height/5)-5);
	}
	if (streetId > 0 && streetId < 10)
	{
		titelFrame->setGeometry(0,height * 0.77 ,width, (height * 0.23)+1);
		line->setGeometry(0, (height * 0.77) - borderWidth,width,borderWidth);
//		titelLabel->setGeometry(0,0+borderWidth,width ,(height/5)-5);
//	        subTitelLabel->setGeometry(0,(height* 0.3),width,(height/5)-5);
//		priceLabel->setGeometry(0,(height*0.6),width,(height/5)-5);
	}
	if (streetId > 10 && streetId < 20)
	{
		line->setGeometry(width*0.23, 0,borderWidth,height);
		titelFrame->setGeometry(0,0, width*0.23,height);
//		titelLabel->setGeometry(borderWidth+(width*0.23),		0,				(width * 0.77) - borderWidth -2,20);
//	        subTitelLabel->setGeometry(borderWidth+(width*0.23),		(height* 0.35),		(width * 0.77) - borderWidth-2,20);
//		priceLabel->setGeometry(borderWidth+(width*0.23),		(height*0.7),	(width * 0.77) - borderWidth-2,20);
	}
	if (streetId > 20 && streetId < 30)
	{
		titelFrame->setGeometry(0,0,width, height * 0.23);
		line->setGeometry(0, 0+(height*0.23),width,borderWidth);
//		titelLabel->setGeometry(0,0+(height *0.23)+borderWidth,width ,(height/5)-5);
//	        subTitelLabel->setGeometry(0,(height* 0.5),width,(height/5)-5);
//		priceLabel->setGeometry(0,(height*0.8),width,(height/5)-5);
	}
	if (streetId > 30 && streetId < 40)
	{
		line->setGeometry(width*0.77-borderWidth, 0,borderWidth,height);
		titelFrame->setGeometry(width * 0.77,0, (width * 0.23)+1 ,height);
//		titelLabel->setGeometry(borderWidth,			0,				(width * 0.77) - borderWidth -2,20);
//	        subTitelLabel->setGeometry(borderWidth,			(height* 0.35),		(width * 0.77) - borderWidth-2,20);
//		priceLabel->setGeometry(borderWidth,			(height*0.7),		(width * 0.77) - borderWidth-2,20);
	}

	for (index=0; index < MAXPLAYERS; ++index)
	{
//		token[index]->setGeometry(10,10);
	}
}


void MStreet::mousePressEvent(QMouseEvent *event)
{
//rintf("%d\n",event->globalX());
	popup->popup( QPoint(event->globalX(),event->globalY()), 0);	
}

void MStreet::slotInfo()
{
}

void MStreet::fillToken()
{
	int index, nr;
	for (index=0; index < MAXPLAYERS; ++index)
	{
/*	stukje code is voor toekomstig gebruik!
	 	if (player[index]->position=streetId)
	 	{
			nr=player[index]->tokenNr	 		
	 		token[index]=player[index]->tokenNr
	 	}
*/
		token[index] = new QPixmap();
		token[index]->load("token01.xpm");
//		p->drawPixmap(10,10,*token[index]);	
	}
}

MStreetFrame::MStreetFrame(QWidget *parent, int *id):QFrame(parent,"")
{
	streetId=*id;
	setFrameStyle(QFrame::Box | QFrame::Plain);	
	setLineWidth(1);
}

MStreetFrame::~MStreetFrame()
{
}

void MStreet::paintEvent(QPaintEvent *)
{
	int index;
	QPainter p;
	QPixmap *token = new QPixmap();

	p.begin(this);
		for (index=0; index < MAXPLAYERS; ++index)
		{
			if (mplayer[index]->getPos() == streetId)
			{
				QString temp;
				temp.setNum(mplayer[index]->getToken());
				token->load("token0"+temp+".xpm");
				p.drawPixmap(25,25,*token);
			}
		}
	p.end();	
}

void MStreet::refresh()
{
	repaint();
}

void MStreetFrame::mousePressEvent(QMouseEvent *event)
{
	printf("%d\n",event->globalX());
}

#include "street.moc"
