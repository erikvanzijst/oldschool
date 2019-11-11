#include "globals.h"
#include "board.h"
#include "street.h"
#include "network.h"
#include "playertable.h"

extern MNetwork *mnetwork;
extern MPlayerTable *playerTable;
MStreet *street[40];

MBoard::MBoard(QWidget *parent, const char *name)
	: QWidget(parent, name)
{
	int index;
	
	for (index=0 ; index < 40; ++index)
	{
		street[index] = new MStreet(this, &index);	
		street[index]->initStreet(index);
//		streetFrame[index] = new MStreetFrame(this, &index);
	}
//	setReady = new QPushButton("1 4m R34DY",this,"ready");
//	rollDice = new QPushButton("Roll tha Dice",this,"roll");
//	connect (setReady,SIGNAL(clicked()), SLOT(slotSetReady()));
//	connect (rollDice,SIGNAL(clicked()), SLOT(slotRollDice()));
//	playerTable = new MPlayerTable(0, "playerTable");
//	playerTable->show();
//	config->writeEntry("nick","cybor");
	info = new QMultiLineEdit(this,0);
	info->setReadOnly(true);
	info->append(
		"Kmonop info:\n\n\r"
		"Last update Aug 2\n\n\r"
		"New features since Jul 11:\n\r"
		"	-Playertable\n\r"
		"	-More parser features\n\r"
		"	-Player vars are now saved in a configfile\n\r"
		"\n\n\rAt the moment I have a lot of overhead when a player leaves."
		"\n\r(I'm requesting the whole player list from the server) I know it's stupid"
		"\n\rbut I'll fix it l8er. (it was the quickest way :-)  )"
		"\n\n\rIce, gimme a startgame function"
		"\n\n\n\rIf you have any suggestions, mail me @\n\rcybor@prutser.cx"
	);
}

MBoard::~MBoard()
{
}

void MBoard::resizeEvent (QResizeEvent *e)
{
	int index, index2;

	
	/* width is de breedte van een rechtopstaande straat, height is de daarbijbehorende hoogte */
	int Mheight,Mwidth, Mright, Mdown, Mborder;
	Mborder=2;
	Mwidth=this->width()/13;
	Mheight= int(Mwidth*1.62);

	/* right is het x cordinaat van de straten rechts, down is het y coordinaat van de straten beneden */
	Mright = Mheight + (9 * Mwidth) + (10 * Mborder)+1;
	Mdown = Mheight + (9 * Mwidth)+ (10 * Mborder)+1;	

	/* de hoekpunten */
	street[0]->setGeometry(1, 1, Mheight, Mheight);
//	streetFrame[0]->setGeometry(0, 0, Mheight+2, Mheight+2);
	street[10]->setGeometry(Mright,1, Mheight, Mheight);
//	streetFrame[10]->setGeometry(Mright-1, 0 , Mheight+2, Mheight+2);
	street[20]->setGeometry(Mright, Mdown, Mheight, Mheight);
//	streetFrame[20]->setGeometry(Mright-1, Mdown-1, Mheight+2, Mheight+2);
	street[30]->setGeometry(1, Mdown, Mheight, Mheight);
//	streetFrame[30]->setGeometry(0, Mdown-1, Mheight+2, Mheight+2);
	
	/* de overige straten */
	for (index=1; index <10; ++index)
	{
		street[index]->setGeometry(Mheight+ (Mborder * index)+(Mwidth*(index-1))+1,1,Mwidth,Mheight);
//		streetFrame[index]->setGeometry(Mheight+ (Mborder * index)+(Mwidth*(index-1)),0,Mwidth+2,Mheight+2);
	}
	for (index=11; index <20; ++index)
	{
		street[index]->setGeometry(Mright, Mheight + (Mwidth*(index-11)) + (Mborder *(index - 10))+1 ,Mheight,Mwidth );
//		streetFrame[index]->setGeometry(Mright-1, Mheight + (Mwidth*(index-11)) + (Mborder *(index - 10)) ,Mheight+2,Mwidth +2);
	}
	for (index=29; index > 20; --index)
	{
		index2 = 30 - index;

		street[index]->setGeometry(Mheight+ (Mborder * index2)+(Mwidth*(index2-1))+1,Mdown,Mwidth,Mheight);
//		streetFrame[index]->setGeometry(Mheight+ (Mborder * index2)+(Mwidth*(index2-1)),Mdown-1,Mwidth+2,Mheight+2);
	}
	for (index=39; index  > 30; --index)
	{
		index2 = 40 - index;

		street[index]->setGeometry(1, Mheight + (Mwidth*(index2-1)) + (Mborder *(index2))+1 ,Mheight,Mwidth );
//		streetFrame[index]->setGeometry(0, Mheight + (Mwidth*(index2-1)) + (Mborder *(index2)) ,Mheight+2,Mwidth+2 );
	}

	/*temp*/
//	setReady->setGeometry(100,100,100,25);
//	rollDice->setGeometry(100,150,100,25);
//	playerTable->setGeometry(Mheight+3,Mheight+3,Mright-Mheight-6,((Mdown-Mheight)/2)-6);
	info->setGeometry(Mheight+3,Mheight+3,Mright-Mheight-6,((Mdown-Mheight))-6);
}

void MBoard::slotSetReady()
{
	mnetwork->send2Server("/ready set 1");
}

void MBoard::slotRollDice()
{
	mnetwork->send2Server("/dice roll");
}


#include "board.moc"
