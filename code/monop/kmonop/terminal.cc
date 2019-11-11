/*
	network
*/

#include "network.h"
#include <qmultilinedit.h>
#include <qlineedit.h>
#include "parser.h"
#include "terminal.h"

extern MNetwork *mnetwork;

MTerminal::MTerminal(QWidget *parent, const char *name):QWidget(parent,name)
{
	setCaption("Debug Terminal");
	connTerminal = new QMultiLineEdit(this,"terminal");		
	connTerminal->setReadOnly(TRUE);
	lineEdit = new QLineEdit(this,"lineEdit");
	lineEdit->setFocus();

	connect(lineEdit,SIGNAL(returnPressed()),this,SLOT(slotSendIt()));
}

MTerminal::~MTerminal()
{
}

void MTerminal::send(QString sendString)
{
	sendString.prepend("Client: >");
	append(sendString);	
}

void MTerminal::incoming(QString sendString)
{
	sendString.prepend("Server: >");
	append(sendString);	
}

void MTerminal::slotSendIt()
{
	mnetwork->send2Server(lineEdit->text());
	lineEdit->setText("");
}

void MTerminal::resizeEvent (QResizeEvent *e)
{
	connTerminal->setGeometry(10,10,width()-20,height()-60);	
	lineEdit->setGeometry(10,height()-40,width()-20,25);
}

void MTerminal::append(QString message)
{
 	connTerminal->append(message);
	connTerminal->setCursorPosition(connTerminal->numLines(),0,false);
}
#include "terminal.moc"
