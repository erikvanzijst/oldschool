/*
transconf.cpp

KTranslator v. 0.3.0, Setup Dialog 


(C) Andrea Rizzi <bilibao@ouverture.it>
27 Apr 1998



*/

#include "transconf.h"
#include "kiconloader.h"


// SETUP DIALOG
Setup::Setup( QWidget *parent, const char *name )
    : QDialog( parent, name,TRUE )
{
setFixedSize(350,340);
    KTabCtl *tab = new KTabCtl( this, "ktab" );
    setCaption( klocale->translate("KTransaltor Setup" ));
    KApplication *app=KApplication::getKApplication();
    config=app->getConfig();
    
    QString sstr;
    QWidget *w = new QWidget( tab, "page one" );    
    config->setGroup("Files");
    Lined[0] = new KLined( w, "LineEdit_1" );    
    Lined[0]->setGeometry( 10, 40, 200, 30 );
    Lined[0]->setText(config->readEntry("SourceDir"));
    
    Lined[1] = new KLined( w, "LineEdit_2" );    
    Lined[1]->setGeometry( 10, 120, 200, 30 );
    Lined[1]->setText(config->readEntry("OutputDir"));
    
    Lined[2] = new KLined( w, "LineEdit_3" );    
    Lined[2]->setGeometry( 10, 200, 200, 30 );
    Lined[2]->setText(config->readEntry("Lang"));

	QLabel* tmpQLabel;
	tmpQLabel = new QLabel( w, "Label_1" );
	tmpQLabel->setGeometry( 10, 10, 200, 30 );
	tmpQLabel->setText( klocale->translate("Sources Dir" ));
	
	tmpQLabel = new QLabel( w, "Label_2" );
	tmpQLabel->setGeometry( 10, 90, 200, 30 );
	tmpQLabel->setText( klocale->translate("Output Dir" ));
	
	tmpQLabel = new QLabel( w, "Label_3" );
	tmpQLabel->setGeometry( 10, 170, 280, 30 );
	tmpQLabel->setText( klocale->translate("Default Translation Language (it,de,fr,es..)" ));
	
	w->resize( 350, 300 );
	tab->addTab( w, klocale->translate("Files" ));

	
    w = new QWidget( tab, "page two" );    
    config->setGroup("Identity");
    
    Lined[3] = new KLined( w, "LineEdit_1a" );    
    Lined[3]->setGeometry( 10, 40, 200, 30 );
    Lined[3]->setText(config->readEntry("Name"));
    
    Lined[4] = new KLined( w, "LineEdit_2a" );    
    Lined[4]->setGeometry( 10, 100, 200, 30 );
    Lined[4]->setText(config->readEntry("EMail"));
    
    Lined[5] = new KLined( w, "LineEdit_3a" );    
    Lined[5]->setGeometry( 10, 160, 200, 30 );
    Lined[5]->setText(config->readEntry("MList"));
    
    Lined[6] = new KLined( w, "LineEdit_4a" );    
    Lined[6]->setGeometry( 10, 220, 200, 30 );
    Lined[6]->setText(config->readEntry("Language"));


	tmpQLabel = new QLabel( w, "Label_1a" );
	tmpQLabel->setGeometry( 10, 10, 200, 30 );
	tmpQLabel->setText( klocale->translate("Author Name" ));
	
	tmpQLabel = new QLabel( w, "Label_2a" );
	tmpQLabel->setGeometry( 10, 70, 200, 30 );
	tmpQLabel->setText( klocale->translate("Author E-Mail" ));
	
	tmpQLabel = new QLabel( w, "Label_3a" );
	tmpQLabel->setGeometry( 10, 130, 200, 30 );
	tmpQLabel->setText( klocale->translate("Language MailingList" ));
	
	tmpQLabel = new QLabel( w, "Label_4a" );
	tmpQLabel->setGeometry( 10, 190, 200, 30 );
	tmpQLabel->setText( klocale->translate("Full Language Name" ));
	
    	//QPushButton* tmpQPushButton;

	w->resize( 350, 300 );
    tab->addTab( w, klocale->translate("Identity" ));


    
        w = new QWidget( tab, "page three" );

    config->setGroup("Translation");

	
	CheckBox[0] = new QCheckBox( w, "CheckBox_1" );
	CheckBox[0]->setGeometry( 10, 30, 160, 20 );
	CheckBox[0]->setText( klocale->translate("Auto Search") );
	CheckBox[0]->setChecked(config->readNumEntry("AutoSearch"));	
	
	tmpQLabel = new QLabel( w, "Label_1b" );
	tmpQLabel->setGeometry( 10, 60, 300, 30 );
	tmpQLabel->setText( klocale->translate("Stop Normal Scrolling if New MsgStr is" ));
	
	CheckBox[1] = new QCheckBox( w, "CheckBox_2" );
	CheckBox[1]->setGeometry( 10, 90, 260, 20 );
	CheckBox[1]->setText( klocale->translate("Fuzzy") );
	CheckBox[1]->setChecked(config->readNumEntry("NormalFuzzy"));

	CheckBox[2] = new QCheckBox( w, "CheckBox_3" );
	CheckBox[2]->setGeometry( 10, 110, 260, 20 );
	CheckBox[2]->setText( klocale->translate("Identical to MsgId") );
	CheckBox[2]->setChecked(config->readNumEntry("NormalSame"));

	CheckBox[3] = new QCheckBox( w, "CheckBox_3" );
	CheckBox[3]->setGeometry( 10, 130, 260, 20 );
	CheckBox[3]->setText( klocale->translate("Null") );
	CheckBox[3]->setChecked(config->readNumEntry("NormalNull"));

	tmpQLabel = new QLabel( w, "Label_2b" );
	tmpQLabel->setGeometry( 10, 160, 300, 20 );
	tmpQLabel->setText( klocale->translate("Stop \"->\" Scrolling.." ));
	
	CheckBox[4] = new QCheckBox( w, "CheckBox_4" );
	CheckBox[4]->setGeometry( 10, 180, 260, 20 );
	CheckBox[4]->setText( klocale->translate("if Fuzzy") );
	CheckBox[4]->setChecked(config->readNumEntry("Fuzzy"));

	CheckBox[5] = new QCheckBox( w, "CheckBox_5" );
	CheckBox[5]->setGeometry( 10, 200, 280, 20 );
	CheckBox[5]->setText( klocale->translate("if there are no translations in catalogues") );	
	CheckBox[5]->setChecked(config->readNumEntry("NoTransl"));

	CheckBox[6] = new QCheckBox( w, "CheckBox_6" );
	CheckBox[6]->setGeometry( 10, 220, 280, 20 );
	CheckBox[6]->setText( klocale->translate("if there is no translation in current catalogue") );
	CheckBox[6]->setChecked(config->readNumEntry("NoTranslHere"));

	CheckBox[7] = new QCheckBox( w, "CheckBox_7" );
	CheckBox[7]->setGeometry( 10, 240, 280, 20 );
	CheckBox[7]->setText( klocale->translate("if .po file entry isn't Null") );
	CheckBox[7]->setChecked(config->readNumEntry("TranslPoFile"));
		


	resize( 350, 300 );
        tab->addTab( w, klocale->translate("Translation") );

	        w = new QWidget( tab, "page four" );

    config->setGroup("Header");

	HLineEdit = new  QMultiLineEdit(w,"MultiLine Header");
	HLineEdit->setGeometry(10,10,330,250);
	config->setGroup("Header");
         int i; 
         QString s,st;
config->setGroup("Header");
for(i=0;i<config->readNumEntry("NumLines");i++)
{
    s.sprintf("HLine%d",i+1);
    st=config->readEntry(s).copy();
    HLineEdit->insertLine(st);
}
	
	resize( 350, 300 );
        tab->addTab( w, klocale->translate("Header") );


    QPushButton *quitButton = new QPushButton( this, "cancelButton" );
    quitButton->setText( klocale->translate("Cancel") );
    quitButton->setGeometry( 275,305, 65,30 );
    connect( quitButton, SIGNAL(clicked()), SLOT(quit()) );
    
quitButton = new QPushButton( this, "OKButton" );
    quitButton->setText( klocale->translate("Ok" ));
    quitButton->setGeometry( 10,305, 65,30 );
    connect( quitButton, SIGNAL(clicked()), SLOT(save()) );
    tab->resize( 350, 300 );




show();
}



  

void Setup::quit()
{
close();
}

void Setup::save()
{
QString s,st;
int i;
config->setGroup("Header");
for(i=0;i<HLineEdit->numLines();i++)
{
    s.sprintf("HLine%d",i+1);
    st=HLineEdit->textLine(i);    
    config->writeEntry(s,st);
}
config->writeEntry("NumLines",HLineEdit->numLines());
config->setGroup("Files");
config->writeEntry("SourceDir",Lined[0]->text());
config->writeEntry("OutputDir",Lined[1]->text());
config->writeEntry("Lang",Lined[2]->text());
config->setGroup("Identity");
config->writeEntry("Name",Lined[3]->text());
config->writeEntry("EMail",Lined[4]->text());
config->writeEntry("MList",Lined[5]->text());
config->writeEntry("Language",Lined[6]->text());
config->setGroup("Translation");
config->writeEntry("AutoSearch",CheckBox[0]->isChecked());
config->writeEntry("NormalFuzzy",CheckBox[1]->isChecked());
config->writeEntry("NormalSame",CheckBox[2]->isChecked());
config->writeEntry("NormalNull",CheckBox[3]->isChecked());
config->writeEntry("Fuzzy",CheckBox[4]->isChecked());
config->writeEntry("NoTransl",CheckBox[5]->isChecked());
config->writeEntry("NoTranslHere",CheckBox[6]->isChecked());
config->writeEntry("TranslPoFile",CheckBox[7]->isChecked());
close();
}




#include "transconf.moc"
