/**********************************************************************

	--- Dlgedit generated file ---

	File: TranslData.h
	Last generated: Fri Dec 26 13:38:08 1997

	DO NOT EDIT!!!  This file will be automatically
	regenerated by dlgedit.  All changes will be lost.

 *********************************************************************/

#ifndef TranslData_included
#define TranslData_included
#include <qregexp.h>
#include <klocale.h>
#include <qfile.h>
#include <qfiledlg.h>
#include <kapp.h>
#include <qcombo.h>
#include <qdialog.h>
#include <qlabel.h>
#include <qlistbox.h>
#include <qmlined.h>
#include <ktopwidget.h>
#include <ktoolbar.h>
#include "transconf.h"
class TranslData;
class KTranslator : public KTopLevelWidget
{
    Q_OBJECT
public:
 KTranslator(QWidget *parent=0,const char* name = NULL,QString filename = "");

private:
 KToolBar *toolbar;
 KApplication *app;
 KConfig *config;
 KMenuBar *menubar;
 KStatusBar *statusbar;
 TranslData *wid;
// QDialog *wid;
protected slots:
 void updateToolbar(); 
 void updateTot(int n); 
 void updateCur(int n);  
 void updateTran(int n); 
 void about();
 void HtmlHelp();
 };

class TranslData : public QDialog
{
    Q_OBJECT

public:

    TranslData(QWidget *parent,const char* name = NULL,QString filename = "");

    virtual ~TranslData();
private:    
    QList<QString> *Id[1500];
    QList<QString> *Str[1500]; 
    QList<QString> *Com[1500]; 
    QList<QString> *Head; 
    QPushButton *PushButton[8];
    bool scroll;
    int nmax;
    KLedLamp *lamp[6];
    KConfig *config;
    QLabel* labelID;
    QMultiLineEdit* editID;
    int ncat;
    KLocale *cat[150];
    QListBox* listCat;
    QListBox* listLang;
    QString   Langs[50];
    void updateEd();
    int isItTranslated(int ni);
    void openCats(QString lang);
 QFile theFile;
 QString nameFile; 

signals:
   void fileOpened();
   void total(int);
   void current(int);
   void tran(int);
protected slots:
     void removeFuzzy();    
     void header();    
     void scrollOn();    
     void setup();    
     void saveopt();
     void nextID();
     void prevID();
     void selectedCat(int);
     void selectedLang(int a);
     void storeSTR();
     void searchInCats();
     void storeAndNext();
     void storeAndPrev();
     void openPot();
     void makeFile();
protected:
    void setScroll(bool status,int event = 0);
    void scrollData(int sc);
    bool isMulti(int i);
    QString aMsgId(int i);
    void archive();
    void openFile(QString name);
    QMultiLineEdit* editSTR;
    QMultiLineEdit* editNSTR;    
    QMultiLineEdit* editC;

    int nid;
};

#endif // TranslData_included
