
#ifndef __KFINGER_H_
#define __KFINGER_H_
#include <qbttngrp.h>
#include <qradiobt.h>
#include <qlistbox.h>
#include <qlined.h>
#include <ktabctl.h>
#include <qkeycode.h> 
#include <kmenubar.h>
//#include <kpopmenu.h>
#include <qmlined.h>
#include <unistd.h>
//#include <qevent.h>
//#include <qpixmap.h>
#include <kapp.h>
#include <kslider.h>
#include <qchkbox.h>
#include <qcombo.h>
#include <qlabel.h>
#include <qpushbt.h>
#include <qtooltip.h>
#include <kmsgbox.h>
#include <ktopwidget.h>
#include <klined.h>
class Setup : public QDialog
{
    Q_OBJECT
public:
    Setup( QWidget *parent=0, const char *name=0 );
private:
    QMultiLineEdit* HLineEdit; 
    KLined* Lined[8];
    QCheckBox* CheckBox[8];
    KConfig* config;
    //QRadioButton* but[7];

private slots:
    void        quit();
    void        save();  
};





#endif
