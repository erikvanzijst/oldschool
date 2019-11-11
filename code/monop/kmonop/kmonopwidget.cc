#include "board.h"
#include "kmonopwidget.h"
#include "street.h"
#include "network.h"
#include "terminal.h"

MTerminal *terminal;
extern MNetwork *mnetwork;


KmonopWidget::KmonopWidget(QWidget *parent, const char *name)
	: QWidget(parent, name)
{
	board = new MBoard(this,"board");
	terminalView = new QFrame (this, "playerview");
	terminalView->setLineWidth(2);
	terminalView->setFrameStyle(QFrame::Box | QFrame::Raised);

	playerView = new QFrame (this, "playerview");
	playerView->setLineWidth(2);
	playerView->setFrameStyle(QFrame::Box | QFrame::Raised);

	terminal = new MTerminal(this,"terminal");
	
	setReady = new QPushButton(this, "ready");
	connect(setReady,SIGNAL(clicked()), SLOT(slotSetReady()));
	setReady->setText("1 4M R34DY");
	rollDice = new QPushButton(this, "roll dice");
	connect(rollDice,SIGNAL(clicked()), SLOT(slotRollDice()));
	rollDice->setText("R()LL T|-|4 D1[3");
}

KmonopWidget::~KmonopWidget()
{
}

void KmonopWidget::resizeEvent (QResizeEvent *e)
{
	terminalView->setGeometry(3,0,(width()/3)-3,(height()/3*2)-3);
	playerView->setGeometry(3,(height()/3*2)+3,(width()/3)-3,(height()/3)-3);
	terminal->setGeometry(6,3,(width()/3)-9,(height()/3*2)-9);	
	board->setGeometry((width()/3)+5, 0, (width()/3*2)-10, height());
	
	rollDice->setGeometry(25,(height()/3*2)+20,125,30);
	setReady->setGeometry(25,(height()/3*2)+65,125,30);
}

void KmonopWidget::slotSetReady ()
{
	mnetwork->send2Server("/ready set 1");
}

void KmonopWidget::slotRollDice ()
{
	mnetwork->send2Server("/dice roll");
}

#include "kmonopwidget.moc"
