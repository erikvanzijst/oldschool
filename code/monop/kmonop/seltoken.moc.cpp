/****************************************************************************
** SelectToken meta object code from reading C++ file 'seltoken.h'
**
** Created: Tue Jun 1 00:13:12 1999
**      by: The Qt Meta Object Compiler ($Revision: 2.25.2.11 $)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#if !defined(Q_MOC_OUTPUT_REVISION)
#define Q_MOC_OUTPUT_REVISION 2
#elif Q_MOC_OUTPUT_REVISION != 2
#error "Moc format conflict - please regenerate all moc files"
#endif

#include "seltoken.h"
#include <qmetaobject.h>


const char *SelectToken::className() const
{
    return "SelectToken";
}

QMetaObject *SelectToken::metaObj = 0;


#if QT_VERSION >= 200
static QMetaObjectInit init_SelectToken(&SelectToken::staticMetaObject);

#endif

void SelectToken::initMetaObject()
{
    if ( metaObj )
	return;
    if ( strcmp(QDialog::className(), "QDialog") != 0 )
	badSuperclassWarning("SelectToken","QDialog");

#if QT_VERSION >= 200
    staticMetaObject();
}

void SelectToken::staticMetaObject()
{
    if ( metaObj )
	return;
    QDialog::staticMetaObject();
#else

    QDialog::initMetaObject();
#endif

    typedef void(SelectToken::*m1_t0)();
    typedef void(SelectToken::*m1_t1)();
    typedef void(SelectToken::*m1_t2)();
    typedef void(SelectToken::*m1_t3)();
    typedef void(SelectToken::*m1_t4)();
    typedef void(SelectToken::*m1_t5)();
    typedef void(SelectToken::*m1_t6)();
    typedef void(SelectToken::*m1_t7)();
    typedef void(SelectToken::*m1_t8)();
    typedef void(SelectToken::*m1_t9)();
    typedef void(SelectToken::*m1_t10)();
    m1_t0 v1_0 = &SelectToken::cancel_clicked;
    m1_t1 v1_1 = &SelectToken::t0clicked;
    m1_t2 v1_2 = &SelectToken::t1clicked;
    m1_t3 v1_3 = &SelectToken::t2clicked;
    m1_t4 v1_4 = &SelectToken::t3clicked;
    m1_t5 v1_5 = &SelectToken::t4clicked;
    m1_t6 v1_6 = &SelectToken::t5clicked;
    m1_t7 v1_7 = &SelectToken::t6clicked;
    m1_t8 v1_8 = &SelectToken::t7clicked;
    m1_t9 v1_9 = &SelectToken::t8clicked;
    m1_t10 v1_10 = &SelectToken::t9clicked;
    QMetaData *slot_tbl = new QMetaData[11];
    slot_tbl[0].name = "cancel_clicked()";
    slot_tbl[1].name = "t0clicked()";
    slot_tbl[2].name = "t1clicked()";
    slot_tbl[3].name = "t2clicked()";
    slot_tbl[4].name = "t3clicked()";
    slot_tbl[5].name = "t4clicked()";
    slot_tbl[6].name = "t5clicked()";
    slot_tbl[7].name = "t6clicked()";
    slot_tbl[8].name = "t7clicked()";
    slot_tbl[9].name = "t8clicked()";
    slot_tbl[10].name = "t9clicked()";
    slot_tbl[0].ptr = *((QMember*)&v1_0);
    slot_tbl[1].ptr = *((QMember*)&v1_1);
    slot_tbl[2].ptr = *((QMember*)&v1_2);
    slot_tbl[3].ptr = *((QMember*)&v1_3);
    slot_tbl[4].ptr = *((QMember*)&v1_4);
    slot_tbl[5].ptr = *((QMember*)&v1_5);
    slot_tbl[6].ptr = *((QMember*)&v1_6);
    slot_tbl[7].ptr = *((QMember*)&v1_7);
    slot_tbl[8].ptr = *((QMember*)&v1_8);
    slot_tbl[9].ptr = *((QMember*)&v1_9);
    slot_tbl[10].ptr = *((QMember*)&v1_10);
    metaObj = new QMetaObject( "SelectToken", "QDialog",
	slot_tbl, 11,
	0, 0 );
}
