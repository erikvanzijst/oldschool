/**********************************************************************
TranslData.cpp
Ktranslator Version 0.3.1, main widget, top level widget

    KDE Translation Utility
    
    
    
    
    "Dedicated to the KDE Translators"
    
    
    
    
    
    
    program by Andrea Rizzi <bilibao@ouverture.it>  
    25 Feb 98 / 27 Apr 98 

*********************************************************************/
#include <kiconloader.h>
#include <kledlamp.h>
#include "TranslData.h"

#include <qtooltip.h>
#define Inherited QDialog
#include <qkeycode.h>
#include <glob.h>
#include <qmsgbox.h>
#include <qlabel.h>
#include <qpushbt.h>
#include <ksimpleconfig.h>


KTranslator::KTranslator(QWidget *,const char *name = 0,QString filename = "")
:KTopLevelWidget(name)
{
app=KApplication::getKApplication();
config=app->getConfig();
statusbar = new KStatusBar(this);

statusbar->insertItem(klocale->translate("Total ID:0              "),1);
statusbar->insertItem(klocale->translate("Current ID:0            "),2);
statusbar->insertItem(klocale->translate("OLD Translations:0      "),3);
statusbar->insertItem("Fuzzy Translations:(not implemented)    ",4);

wid = new TranslData(this,"Trans",filename);
connect(wid,SIGNAL(fileOpened()),this,SLOT(updateToolbar()));
connect(wid,SIGNAL(total(int)),this,SLOT(updateTot(int)));
connect(wid,SIGNAL(current(int)),this,SLOT(updateCur(int)));
connect(wid,SIGNAL(tran(int)),this,SLOT(updateTran(int)));
 
 menubar = new KMenuBar(this); //= menuBar(); 
  QPopupMenu *file = new QPopupMenu;
    file->insertItem(klocale->translate("Open"), wid,SLOT(openPot()));
    file->insertItem(klocale->translate("Save as.."), wid, SLOT(makeFile()));
    file->insertItem(klocale->translate("Quit"), qApp, SLOT(quit()));
 QPopupMenu *options = new QPopupMenu;  
    options->insertItem(klocale->translate("Setup"), wid,SLOT(setup()));
    options->insertItem(klocale->translate("Save Options"), wid,SLOT(saveopt()));
 QPopupMenu *help = new QPopupMenu;
    help->insertItem(klocale->translate("Help"), this, SLOT(HtmlHelp()) );
    help->insertItem(klocale->translate("About"), this, SLOT(about()) );
  menubar->insertItem( klocale->translate("&File"), file );
  menubar->insertItem( klocale->translate("&Options"), options );
  menubar->insertSeparator();
  menubar->insertItem( klocale->translate("&Help"), help);

  toolbar = new KToolBar(this);
  //toolBar();
  toolbar->setFullWidth(TRUE);
  toolbar->insertButton(Icon("fileopen.xpm"),1,SIGNAL(clicked()), wid,SLOT(openPot()),1,klocale->translate("open a .pot file"));
  toolbar->insertButton(Icon("filefloppy.xpm"),2,SIGNAL(clicked()), wid,SLOT(makeFile()),FALSE,klocale->translate("Make the po files"));
  toolbar->insertSeparator();     

  toolbar->insertButton(Icon("back.xpm"),3,SIGNAL(clicked()), wid,SLOT(prevID()),FALSE,klocale->translate("Previous message"));
  toolbar->insertButton(Icon("forward.xpm"),4,SIGNAL(clicked()), wid,SLOT(nextID()),FALSE,klocale->translate("Next message"));
  toolbar->insertButton(Icon("prev.xpm"),5,SIGNAL(clicked()), wid,SLOT(storeAndPrev()),FALSE,klocale->translate("Go to Previous message & Copy OldMsgStr to NewMsgStr"));
  toolbar->insertButton(Icon("next.xpm"),6,SIGNAL(clicked()), wid,SLOT(storeAndNext()),FALSE,klocale->translate("Go to Next message & Copy OldMsgStr to NewMsgStr"));
  toolbar->insertSeparator();
  toolbar->insertButton(Icon("viewzoom.xpm"),7,SIGNAL(clicked()), wid,SLOT(searchInCats()),1,klocale->translate("Look for a translation in old catalogues"));
   
addToolBar(toolbar);
setStatusBar(statusbar);
setMenu(menubar);
setView(wid);
wid->show();
enableStatusBar(KStatusBar::Show);

show();

}
void KTranslator::updateTot(int n)
{
QString st;
        st.sprintf(klocale->translate("Total ID:%i"),n);
	statusbar->changeItem(st,1);
}
void KTranslator::updateCur(int n)
{
QString st;
        st.sprintf(klocale->translate("Current ID:%i"),n);
	statusbar->changeItem(st,2);
}

void KTranslator::updateTran(int n)
{
QString st;
        st.sprintf(klocale->translate("Old Translations :%i"),n);
	statusbar->changeItem(st,3);	
	

}
void KTranslator::updateToolbar()
{
int n;
for(n=0;n<6;n++)  
	 toolbar->setItemEnabled(n+1,TRUE);

}

void KTranslator::HtmlHelp()
{ 
 KApplication::getKApplication()->invokeHTMLHelp("ktranslator/index.html", "");
}

void KTranslator::about()
{
QMessageBox :: information( this, "About KTranslator ", "Ktranslator v.0.3.1 \n (C) Andrea Rizzi <Bilibao@ouverture.it>\n27 Apr 1998\n Dedicated to KDE Translation Team\n" ,"Ok");      
}

TranslData::TranslData(QWidget *parent,const char* name,
			QString filename = ""):Inherited(parent,name)
{
KApplication *app=KApplication::getKApplication();
config=app->getConfig();


	Com[0]=new QList<QString>;
	Com[0]->append(new QString(""));
	Id[0]=new QList<QString>;
    	Id[0]->append(new QString(""));
	Str[0]=new QList<QString>;
    	Str[0]->append(new QString("NO FILE OPENED"));
	
	setFixedSize(780,400);        
	for(nid=0;nid<6;nid++)
	lamp[nid] = new KLedLamp(this);
	nid=0;
	nmax=0;
	
	editID = new QMultiLineEdit( this, "MultiLineEdit_1" );
	editID->setGeometry( 120, 30, 250, 120 );
	editID->insertLine( "" );
	editID->setReadOnly( TRUE );
	editID->setOverwriteMode( FALSE );

	editSTR = new QMultiLineEdit( this, "MultiLineEdit_2" );
	editSTR->setGeometry( 120, 260, 250, 120 );
	editSTR->insertLine( "" );
	editSTR->setReadOnly( TRUE );
	editSTR->setOverwriteMode( FALSE );

	editNSTR = new QMultiLineEdit( this, "MultiLineEdit_3" );
	editNSTR->setGeometry( 520, 260, 250, 120 );
	editNSTR->insertLine( "" );
	editNSTR->setReadOnly( FALSE );
	editNSTR->setOverwriteMode( FALSE );

	
	editC = new QMultiLineEdit( this, "MultiLineEdit_3" );
	editC->setGeometry( 520, 30, 250, 120 );
	editC->insertLine( "" );
	editC->setReadOnly( FALSE );
	editC->setOverwriteMode( FALSE );

	QLabel* tmpQLabel;
	tmpQLabel = new QLabel( this, "Label_1" );
	tmpQLabel->setGeometry( 120, 230, 150, 20 );
	tmpQLabel->setText(klocale->translate( "Old Msgstr" ));
	tmpQLabel->setMargin( -1 );

	lamp[0]->move( 120, 215);
	lamp[1]->move( 210, 215);
	lamp[2]->move( 290, 215);
	lamp[3]->move( 520, 215);
	lamp[4]->move( 610, 215);
	lamp[5]->move( 700, 215);

	tmpQLabel = new QLabel( this, "Label H" );
	tmpQLabel->setGeometry( 120, 160, 280, 20 );
	tmpQLabel->setText(klocale->translate( "Old Translations found in") );
	tmpQLabel->setAlignment(AlignLeft );
	
	tmpQLabel = new QLabel( this, "Label PO" );
	tmpQLabel->setGeometry( 120, 190, 80, 20 );
	tmpQLabel->setText(klocale->translate( ".po File") );
	tmpQLabel->setAlignment(AlignLeft );
	
	tmpQLabel = new QLabel( this, "Label lampTO" );
	tmpQLabel->setGeometry( 210, 190, 80, 20 );
	tmpQLabel->setText( klocale->translate("Catalogues" ));
	tmpQLabel->setAlignment(AlignLeft );

	tmpQLabel = new QLabel( this, "Label Fuzzy" );
	tmpQLabel->setGeometry( 520, 190, 80, 20 );
	tmpQLabel->setText( klocale->translate("Fuzzy") );
	tmpQLabel->setAlignment(AlignLeft );
	
	tmpQLabel = new QLabel( this, "Label C-Format" );
	tmpQLabel->setGeometry( 610, 190, 80, 20 );
	tmpQLabel->setText( klocale->translate("C-Format") );
	tmpQLabel->setAlignment(AlignLeft );


	tmpQLabel = new QLabel( this, "Label Same" );
	tmpQLabel->setGeometry( 700, 190, 80, 20 );
	tmpQLabel->setText( klocale->translate("Identical") );
	tmpQLabel->setAlignment(AlignLeft );


	tmpQLabel = new QLabel( this, "Label lampPL" );
	tmpQLabel->setGeometry( 290, 190, 80, 20 );
	tmpQLabel->setText( klocale->translate("This Cat") );
	tmpQLabel->setAlignment(AlignLeft );

	tmpQLabel = new QLabel( this, "Label" );
	tmpQLabel->setGeometry( 520, 230, 150, 20 );
	tmpQLabel->setText( klocale->translate("New Msgstr") );
	
	tmpQLabel = new QLabel( this, "Label_2" );
	tmpQLabel->setGeometry( 120, 10, 130, 20 );
	tmpQLabel->setText(klocale->translate( "Msgid") );
	tmpQLabel->setAlignment( 289 );
	tmpQLabel->setMargin( -1 );

	listCat = new QListBox( this, "ListBox_1" );
	listCat->setGeometry( 10, 220, 100, 160 );
	connect( listCat, SIGNAL(selected(int)), SLOT(selectedCat(int)) );
	listCat->setFrameStyle( 51 );
	listCat->setLineWidth( 2 );

	listLang = new QListBox( this, "ComboBox_2" );
	listLang->setGeometry( 10, 30, 100, 160 );
	connect( listLang, SIGNAL(selected(int)), SLOT(selectedLang(int)) );
	int n,m;
    QString s;
    QString kdelang;
    kdelang="";
    config->setGroup("Files");
        kdelang=config->readEntry("Lang").copy();
    if(kdelang.isEmpty()) 
    	   { 
	       warning("USing Env KDE_LANG");
	     kdelang=getenv("KDE_LANG");
              if(kdelang.isEmpty()) 
	         { 
	           KSimpleConfig config1(QDir::homeDirPath()+"/.kderc");
                   config1.setGroup("Locale");  	 
		   if(config1.readEntry("Language","C")!="C")
		       kdelang=config1.readEntry("Language");	
		   else
	            { 
	              if(getenv("LANG")!="")
      		      kdelang=getenv("LANG");   
	            } 
	         }
    	    }
	if(kdelang.find(":")>=0) 
	 kdelang.truncate(kdelang.find(":") );   
    


    QString ldir = getenv( "KDEDIR" );
    if ( ldir.isNull() )
    {
        QMessageBox::message( "KDE ERROR", "No KDEDIR !!!!!" );
        exit(1);
    }
    s=ldir.copy();
    s+="/share/apps/kcmlocale/pics/";
    ldir+="/share/locale/??";
    glob_t globbuf;
         globbuf.gl_offs = 0;
	 
         if (glob(ldir,GLOB_DOOFFS , NULL, &globbuf)==0) warning("found!!!!");
listCat->clear();
  QPixmap pm;
  m=0;
  for(n=0;n<globbuf.gl_pathc;n++) 
  {
    ldir=globbuf.gl_pathv[n];
    ldir.remove(0,ldir.findRev("/")+1); 
    
    if(pm.load(s+"flag_"+ldir+".gif")) 
		    listLang->insertItem(pm,n); 
		    else 
		    listLang->insertItem(ldir,n);	
    Langs[n]=ldir;
    if(ldir==kdelang) m=n;	
    }
    listLang->setCurrentItem(m);
    
	
	tmpQLabel = new QLabel( this, "Label_4" );
	tmpQLabel->setGeometry( 10, 200, 100, 20 );
	tmpQLabel->setText( klocale->translate("Catalogues:") );
	tmpQLabel = new QLabel( this, "Label_55" );
	tmpQLabel->setGeometry( 10, 10, 100, 20 );
	tmpQLabel->setText( klocale->translate("Lang:") );
	
	tmpQLabel->setAlignment( 289 );
	tmpQLabel->setMargin( -1 );

	QPushButton* tmpQPushButton;
	PushButton[0] = new QPushButton( this, "PushButton_2" );
	PushButton[0]->setGeometry( 400, 30, 100, 30 );
	connect( PushButton[0], SIGNAL(clicked()), SLOT(prevID()) );
	PushButton[0]->setText(klocale->translate( "Previous") );
	PushButton[0]->setAutoRepeat( TRUE );
	PushButton[0]->setAutoResize( FALSE );

	PushButton[1] = new QPushButton( this, "PushButton_3" );
	PushButton[1]->setGeometry( 400, 70, 100, 30 );
	connect( PushButton[1], SIGNAL(clicked()), SLOT(nextID()) );
	PushButton[1]->setText( klocale->translate("Next") );
	PushButton[1]->setAutoRepeat( TRUE );
	PushButton[1]->setAutoResize( FALSE );

	
	
	PushButton[4] = new QPushButton( this, "PushButton_4" );
	PushButton[4]->setGeometry( 400, 310, 100, 30 );
	connect( PushButton[4], SIGNAL(clicked()), SLOT(storeSTR()) );
	PushButton[4]->setText( "-->" );
	PushButton[4]->setAutoRepeat( FALSE );
	PushButton[4]->setAutoResize( FALSE );	

	PushButton[3] = new QPushButton( this, "PushButton_5" );
	PushButton[3]->setGeometry( 400, 270, 100, 30 );
	connect( PushButton[3], SIGNAL(clicked()), SLOT(storeAndNext()));
	PushButton[3]->setText( klocale->translate("-> && Next") );
	PushButton[3]->setAutoRepeat( TRUE );
	PushButton[3]->setAutoResize( FALSE );

	labelID = new QLabel( this, "Label_5" );
	labelID->setGeometry( 410, 380, 50, 30 );
	labelID->setText( "n/n" );
	labelID->setAlignment( 289 );
	labelID->setMargin( -1 );
	
	PushButton[2] = new QPushButton( this, "PushButton_5" );
	PushButton[2]->setGeometry( 400, 230, 100, 30 );
	connect( PushButton[2], SIGNAL(clicked()),this, SLOT(storeAndPrev()) );
	PushButton[2]->setText( klocale->translate("-> && Prev") );
	PushButton[5] = new QPushButton( this, "PushButton_6" );
	PushButton[5]->setGeometry( 400, 350, 100, 30 );
	connect( PushButton[5], SIGNAL(clicked()),this, SLOT(header()) );
	PushButton[5]->setText( klocale->translate("Header->"));

	PushButton[6] = new QPushButton( this, "PushButton_8" );
	PushButton[6]->setGeometry( 420, 170, 60, 30 );
	connect( PushButton[6], SIGNAL(clicked()),this, SLOT(scrollOn()) );
	PushButton[6]->setText( klocale->translate("Continue!"));
	
	PushButton[7] = new QPushButton( this, "PushButton_9" );
	PushButton[7]->setGeometry( 520, 150, 250, 20 );
	connect( PushButton[7], SIGNAL(clicked()),this, SLOT(removeFuzzy()) );
	PushButton[7]->setText( klocale->translate("Remove \"fuzzy\" string"));
	
	tmpQPushButton = new QPushButton( this, "PushButton_7" );
	tmpQPushButton->setGeometry( 400, 110, 100, 30 );
	connect( tmpQPushButton, SIGNAL(clicked()),this, SLOT(searchInCats()) );
	tmpQPushButton->setText( klocale->translate("Search in cats ") );

	int i;
	for(i=0;i<8;i++) 
	 PushButton[i]->setEnabled( FALSE );
	
	 openCats(Langs[listLang->currentItem()]);
	 if (!filename.isEmpty()) openFile(filename);
	 updateEd();
	 show();
}


TranslData::~TranslData()
{
}

void TranslData::scrollOn()
{
setScroll(TRUE);
}

void TranslData::removeFuzzy()
{
QString s;
s=editC->text().copy();
//warning(s);
s.replace(QRegExp(", fuzzy"),"");
editC->clear();
//warning(s);
editC->setText(s);
}

void TranslData::header()
{
unsigned int i;
QString s,st;
/*QDate thedate;
QTime thetime;
*/
st="";
editNSTR->clear();
//editNSTR->insertLine(st);
config->setGroup("Header");
for(i=0;i<(unsigned int)config->readNumEntry("NumLines");i++)
{
    s.sprintf("HLine%d",i+1);    
     st=config->readEntry(s).copy();
     config->setGroup("Identity");
     st.replace(QRegExp("%TIME%"),QTime::currentTime().toString().copy());
     st.replace(QRegExp("%DATE%"),QDate::currentDate().toString().copy());
     st.replace(QRegExp("%AUTHORNAME%"),config->readEntry("Name"));
     st.replace(QRegExp("%AUTHOREMAIL%"),config->readEntry("EMail"));
     st.replace(QRegExp("%LANGLIST%"),config->readEntry("MList"));
     st.replace(QRegExp("%LANGUAGE%"),config->readEntry("Language"));
       if(i<Str[nid]->count()) st.replace(QRegExp("%ORIGINAL%"),Str[nid]->at(i)->copy());
	    else  st.replace(QRegExp("%ORIGINAL%"),"");
     config->setGroup("Header");    
    editNSTR->insertLine(st);
  }      
}



void TranslData::makeFile()
{
QFile theSaveFile;
QString *sa;
config->setGroup("Files");
QString nameSaveFile = QFileDialog::getSaveFileName(config->readEntry("OutputDir"),"*.po*");
theSaveFile.setName(nameSaveFile);
 if ((theSaveFile.open(IO_WriteOnly) ) ) { 
	       QTextStream ts( &theSaveFile );        // use a text stream
	       QString s,st;               
               int a,f=0,n = 0;
               a=0;   
	       if(Com[0]->first()->find("# KTranslator Generated File")<0)
	           ts << "# KTranslator Generated File\n";
	       for(n=1;n<=nmax;n++) {       
   	          for ( sa=Com[n-1]->first(); sa != 0; sa=Com[n-1]->next() )
				{   
				 s=sa->copy();
				 ts << (QString) s;
 			         ts <<"\n";
				 } 
//Write msgid
		     f=0;
		     ts<< "msgid ";			    
		    if (Id[n]->isEmpty()){ ts << "\"\"\n";} else{
   	          for ( sa=Id[n]->first(); sa != 0; sa=Id[n]->next() )
				{   
				 if(f>0)ts << "\"\n";  
				 ts << "\""; 
				 s=sa->copy();
				 ts << (QString) s;
				
			         f++;	
 			        }
			    ts << "\"\n";  }

			    
//Write msgstr 
		    f=0;
		     ts<< "msgstr ";			    
		    if (Str[n]->isEmpty()){ ts << "\"\"\n";} else{
   	          for ( sa=Str[n]->first(); sa != 0; sa=Str[n]->next() )
				{   
				 if(f>0)ts << "\"\n";  
				 ts << "\""; 
				 s=sa->copy();
				 ts << (QString) s;
				
			         f++;	
 			        }
			    ts << "\"\n";  }

			    		     
	           st.sprintf("%i/%i",n,n);
		   labelID->setText(st) ;		 
	      }
	      f=0;
for ( sa=Com[n-1]->first(); sa != 0; sa=Com[n-1]->next() )
				{   
				 if(f>0) ts <<"\n";
				 f++;
				 s=sa->copy();
				 ts << (QString) s;
				 } 
               theSaveFile.close();
        }

}


void TranslData::openPot()
{
 openFile(QString()); 
}

void TranslData::openFile(QString name)
{
QString s,st;
nameFile=name.copy();
config->setGroup("Files");
if(nameFile.isNull())  nameFile=(QFileDialog::getOpenFileName(config->readEntry("SourceDir"),"*.po*" )).copy();
theFile.setName(nameFile); 
 update();
 show();
 if ( theFile.open(IO_ReadOnly) ) {
 int a,f,n,oldn = -10;
 nid=0;
 n=0;
 f=0;
 a=0;
    // file opened successfully

	             
	       QTextStream t( &theFile );        // use a text stream
               s.sprintf("KTranslator - %s",(const char *)nameFile);   
	       topLevelWidget()->setCaption(s);    
               a=0;   
	     
	       
	       while ( (!t.eof())&&(n>=0) ) {        // until end of file...                          
	            s = t.readLine();       // line of text excluding '\n'

		if ((s.find("#")==0)) a=0; else {
		if (s.find("msgid")>=0) { 
					   a=2;
					   n++;
					   f=0; 
					    Id[n]=new QList<QString>;
					    Com[n]=new QList<QString>;		    Id[n]=new QList<QString>;
					    }
	        if (s.find("msgstr")>=0) { 
					    if (n==oldn) n=-60;
					    oldn=n;
					    a=1; 
					    f=0; 
					    Str[n]=new QList<QString>;
					    }					
		
		if ((s.find("\"")<0)) a=0;}
		
	

		if (a>0) {
		    s.remove(0,s.find("\"")+1); 
                    s.truncate(s.findRev("\"")); 
		    if (a==1) Str[n]->append(new QString(s));
		    if (a==2) Id[n]->append(new QString(s));
		    f++;
		    } else
		    {
		     Com[n]->append(new QString(s));
		    } 
   
		    st.sprintf("%i/%i",n,n); //sizeof(Str));

	          labelID->setText(st) ;
		   nmax=n;      
	      }
	         theFile.close();
         
           
if ((n<1)||(a==2))  {
        QMessageBox :: information( this, "KTranslator ", "It is not a valid .po/.pot file!" ,"Ok");
 	for(nid=0;nid<7;nid++) 
	 PushButton[nid]->setEnabled( FALSE );

	} else {
     fileOpened();
     for(n=0;n<5;n++)  
              PushButton[n]->setEnabled( TRUE );	

	 nid=1;
//	 update();
	 updateEd();
//	 repaint();
	int k=0,ni;
	 
	 for(ni=1;ni<=nmax;ni++)
	 {
	   if(isItTranslated(ni)>-1) k++;         
	   tran(k);	  
	 
	 }
	total(nmax);
	current(nid);

    }
    }
updateEd();
}

int TranslData::isItTranslated(int ni)
{ 
	  int i,t=0,a;  
	  a=listCat->currentItem();
	  if (a<0) a=0;
	  for(i=a+1;i<ncat;i++)
	   if(aMsgId(ni)!=cat[i]->translate(aMsgId(ni))  )
 	    {t=i; return(t); }	
	  
	for(i=0;i<=a;i++)
	   if(aMsgId(ni)!=cat[i]->translate(aMsgId(ni))  )
 	    {t=i; return(t); }	
		
return(-1);
}

QString TranslData::aMsgId(int i)
{
if (nmax<i) return("");
QString s=0,sn=0;
QString *sa;
int n=0;

sa=Id[i]->first();
if(!isMulti(i)) 
  s=sa->copy(); 
   else
  for ( ;sa != 0; sa=Id[i]->next() )
     {
      sn=sa->copy();
      if(sn.find("\\n")>=0)          
      sn.truncate(sn.find("\\n")); 
      if(n>1) s+="\n";  
      s+=sn;
      n++;   
    }  
return (s);
}

bool TranslData::isMulti(int i)
{
return((Id[i]->count()>1)||((i==1)||(Id[i]->first()->isEmpty())));
}

void TranslData::updateEd() //Update everything in Main Dialog
{
config->setGroup("Translation");
QString s=0;
QString *sa;
int l,i,n=0,ca;
if (nid==1) 
	 PushButton[5]->setEnabled(TRUE);
	 else 
	 PushButton[5]->setEnabled(FALSE);
/*
Clear Multi Line Editors
*/
  editID->clear();
  editC->clear();
  editSTR->clear();
  editNSTR->clear();

/*
Copy MsgId in ID Multi Line Editor
*/
for ( sa=Id[nid]->first(); sa != 0; sa=Id[nid]->next() )
   editID->insertLine(sa->copy());

/*
Insert Comment before msgid
*/
if(nid>0) for ( sa=Com[nid-1]->first(); sa != 0; sa=Com[nid-1]->next() )
   editC->insertLine(sa->copy());
  else
   editC->insertLine("KTranslator 0.3.1\n(C) Andrea Rizzi <Bilibao@ouverture.it>\n27 April 1998");
 
/*
Get The ID string
Get Cat Number
*/
  s=aMsgId(nid);
  n=listCat->currentItem();
  ca=isItTranslated(nid);
   
   if(cat[n]->translate(s)!=s) 
	{
        lamp[2]->setState(KLedLamp::On);
	}   
     else
     {
        
        if ((ca>-1)&&(config->readNumEntry("AutoSearch")==1)) {
		    n=ca;
		    listCat->setCurrentItem(n);
		    lamp[2]->setState(KLedLamp::On);
		    } else {
		    setScroll(FALSE,5);
    		    lamp[2]->setState(KLedLamp::Off);
		    }
     } 
   if(ca>-1) 
{
        lamp[1]->setState(KLedLamp::On);
    	
	}   
     else
     {
         setScroll(FALSE,4);
        lamp[1]->setState(KLedLamp::Off);
//	setScroll(TRUE);
     }     


  if(isMulti(nid))  editSTR->insertLine(""); 
 
   editSTR->insertLine(cat[n]->translate(s));
  
  l=editSTR->numLines(); 
   if(l>2) 
  { for(i=2;i<l;i++)
    editSTR->insertAt("\\n",i-1,editSTR->text().length());
  }
 
   n=0;
   
     for ( sa=Str[nid]->first(); sa != 0; sa=Str[nid]->next())  
      editNSTR->insertLine(sa->copy());
     
  if(editNSTR->text()!="") {  
	lamp[0]->setState(KLedLamp::On);
	setScroll(FALSE,6);
	}
    else{
        lamp[0]->setState(KLedLamp::Off);
	setScroll(FALSE,3);
  }
  
  if(editC->text().find("fuzzy",0,FALSE)>0)  
	{
	PushButton[7]->setEnabled(TRUE);
	lamp[3]->setState(KLedLamp::On);
	setScroll(FALSE,1);
	}
	else {
	PushButton[7]->setEnabled(FALSE);
	lamp[3]->setState(KLedLamp::Off);
	}
	if(editC->text().find("c-format",0,FALSE)>0)  
	lamp[4]->setState(KLedLamp::On);
    else
        lamp[4]->setState(KLedLamp::Off);
 
 if(editNSTR->text()==editID->text()){  
	lamp[5]->setState(KLedLamp::On);
	setScroll(FALSE,2);
	}
    else{
        lamp[5]->setState(KLedLamp::Off);
	}
update();
}

void TranslData::selectedCat(int)
{
updateEd();
}

void TranslData::setScroll(bool status,int event = 0)
{
int i;
config->setGroup("Translation");
scroll=TRUE;
if (!status) {
 if (PushButton[1]->isDown() || PushButton[0]->isDown() ) //Normal Scrolling 
	{
	 if (  event == 1 )
	         scroll=scroll&&(!(bool)config->readNumEntry("NormalFuzzy"));
	 if (  event == 2 )
	         scroll=scroll&&(!(bool)config->readNumEntry("NormalSame"));
	 if (  event == 3 )
	         scroll=scroll&&(!(bool)config->readNumEntry("NormalNull"));
	}
	else
 if (PushButton[2]->isDown() || PushButton[3]->isDown() ) //--> & Scrolling 
	{
	 if (  event == 1 )
	         scroll=scroll&&(!(bool)config->readNumEntry("Fuzzy"));
	 if (  event == 4 )
	         scroll=scroll&&(!(bool)config->readNumEntry("NoTransl"));
	 if (  event == 5 )
	         scroll=scroll&&(!(bool)config->readNumEntry("NoTranslHere"));
	 if (  event == 6 )
	         scroll=scroll&&(!(bool)config->readNumEntry("TranslPoFile"));
	}
if(!scroll) {for (i=0;i<4;i++)
    PushButton[i]->setEnabled(FALSE);
    PushButton[6]->setEnabled(TRUE);
    }
    } else {for (i=0;i<4;i++)
     PushButton[i]->setEnabled(TRUE);
    PushButton[6]->setEnabled(FALSE);
     }
  
}

void TranslData::searchInCats()
{
int i;

i=isItTranslated(nid);
warning(" cat :%d",i);
if(i>=0) 
 listCat->setCurrentItem(i); 
    

updateEd();

}

void TranslData::selectedLang(int a)
{
openCats( Langs[a] );
updateEd();
}

void TranslData::prevID()
{
scrollData(-1);
current(nid);
updateEd();
}

void TranslData::scrollData(int sc)
{
QString st;
archive();
if(scroll) {
    nid+=sc;
    if(nid<1) nid=1;
    if(nid>nmax) nid=nmax; 
    st.sprintf("%i/%i",nid,nmax);
    labelID->setText(st) ;
 }


}

void TranslData::nextID()
{
scrollData(1);
current(nid);
updateEd();
}

void TranslData::archive()
{
Str[nid]->clear();
int l=editNSTR->numLines();
int i;
for(i=0;i<l;i++) 
 Str[nid]->append(new QString(editNSTR->textLine(i))); 

Com[nid-1]->clear();
 l=editC->numLines();
 for(i=0;i<l;i++) 
 Com[nid-1]->append(new QString(editC->textLine(i))); 

}

void TranslData::storeAndNext()
{
if (scroll) {
int i,l;
editNSTR->clear();
Str[nid]->clear();
l=editSTR->numLines();

for(i=0;i<l;i++) {
 Str[nid]->append(new QString(editSTR->textLine(i))); 
 editNSTR->insertLine(editSTR->textLine(i)); 
 }
scrollData(1);
current(nid);
updateEd();
}
}

void TranslData::storeAndPrev()
{
if(scroll) {
int i,l;
editNSTR->clear();
Str[nid]->clear();
l=editSTR->numLines();

for(i=0;i<l;i++) {
 Str[nid]->append(new QString(editSTR->textLine(i))); 
 editNSTR->insertLine(editSTR->textLine(i)); 
 }
scrollData(-1);
current(nid);
updateEd();
}
}

void TranslData::setup()
{
new Setup();
}
void TranslData::saveopt()
{
//config->rollback();
config->sync();
}

void TranslData::storeSTR()
{
int i,l;
editNSTR->clear();
Str[nid]->clear();
l=editSTR->numLines();

for(i=0;i<l;i++) {
 Str[nid]->append(new QString(editSTR->textLine(i))); 
 editNSTR->insertLine(editSTR->textLine(i)); 
 }
updateEd();
}



void TranslData::openCats(QString lang)
{
QString s;
    if ( lang.isNull() )
    {
        QMessageBox::message( "KDE ERROR", "No Language Specfied" );
        exit(1);
    }
    
    

setenv("KDE_LANG",lang,1);
warning(lang);
int n=lang.find(":");
if (n>0) lang.truncate(n);
    //warning(lang); 
    
    QString ldir = getenv( "KDEDIR" );
    if ( ldir.isNull() )
    {
        QMessageBox::message( "KDE ERROR", "No KDEDIR !!!!!" );
        exit(1);
    }
    ldir+="/share/locale/"+lang+"/LC_MESSAGES/*.mo";
    //warning(ldir);

    glob_t globbuf;
         globbuf.gl_offs = 0;
	 
         if (glob(ldir,GLOB_DOOFFS , NULL, &globbuf)==0) warning("found!!!!");
listCat->clear();
  for(n=0;n<globbuf.gl_pathc;n++) 
  {
    s=globbuf.gl_pathv[n];
    s.truncate(s.find("."));
    s.remove(0,s.findRev("/")+1); 
    listCat->insertItem(s+".mo (old)");
    cat[n] = new KLocale(s);
    //warning(s);
    }
ncat=n;
listCat->setCurrentItem(0);


}


#include "TranslData.moc"


int main( int argc, char **argv )
{
    KApplication *a=new KApplication(argc,argv,QString("ktranslator"));
KTranslator  *w;
    if (argc>=1) 
	w = new KTranslator(0,"Translator",argv[1]);
    else 
        w = new KTranslator(0,"Translator");
   
    a->setMainWidget( w );
    a->setTopWidget( w );
    w->show();
    return a->exec();
}
 