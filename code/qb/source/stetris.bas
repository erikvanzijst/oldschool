DECLARE SUB sluitscherm ()
DECLARE SUB INTRO ()
DECLARE SUB TEXT ()
DECLARE SUB PAUZE ()
DECLARE SUB BLOK6 (X%, Y%, R!, V!, RC!)
DECLARE SUB BLOK5 (X%, Y%, R!, V!, RC!)
DECLARE SUB BLOK4 (X%, Y%, R!, V!, RC!)
DECLARE SUB BLOK3 (X%, Y%, R!, V!, RC!)
DECLARE SUB BLOK2 (X%, Y%, R!, V!, RC!)
DECLARE SUB BLOK1 (X%, Y%, R!, V!, RC!)
DECLARE SUB BLOK7 (X%, Y%, R!, V!, RC!)
ON ERROR GOTO FOUT

FOUT:
IF ERR = 53 THEN OPEN "TETRIS.DAT" FOR OUTPUT AS #1: NAME$ = ".": LINES = 0: WRITE #1, NAME$, LINES: CLOSE #1: OPEN "TETRIS.DAT" FOR INPUT AS #1: RESUME NEXT

INTRO

INIT: REM ** TETRIS SVGA VOOR BASIC VERSIE 3.0 **
REM ** GEMAAKT DOOR ERIK VAN ZIJST & SANDER VAN LOO, 13-1-1996 **
KEY(20) OFF
SCREEN 12
V = 0
LINES = 0
E = 20
RANDOMIZE TIMER
SOORTBLOK = INT(RND * 7) + 1
CLS

OPEN "TETRIS.DAT" FOR INPUT AS #1               'Lees de Highscore uit.
INPUT #1, NAME$, SCR
CLOSE #1
LOCATE 19, 63: COLOR 4
PRINT "Best Player"
LOCATE 20, 58: COLOR 15
PRINT "컴컴컴컴컴컴컴컴컴컴�"
LOCATE 21, 58: COLOR 15
PRINT "Name  = "; NAME$
LOCATE 22, 58: COLOR 15
PRINT "Lines = "; SCR

TEXT

WINDOW SCREEN (-220, -60)-(419, 419)            'Allereerst wordt een SCREEN-
LINE (-1, -1)-(201, 361), 15, B                 'SHOT gemaakt van het nog lege
DIM SCHERM(10000)                               'scherm.
GET (0, 0)-(200, 360), SCHERM
GOTO START

NIEUWBLOK:                         'Het op-de-bodem-gevallen blok wordt aan
GET (0, 0)-(200, 360), SCHERM      'het SCREENSHOT toegevoegd.
REM LINE (0, 0)-(200, 360), 0, BF
PUT (0, 0), SCHERM, PSET
RETURN

NAPAUZE:                           'Het scherm weer opnieuw opbouwen, nadat
PUT (0, 0), SCHERM, PSET           'het door het pauze-commando is gewist.
RETURN

REFRESH:                           'Het blok valt 1 positie naar beneden en
REM LINE (0, 0)-(200, 360), 0, BF      'daarom wordt het veld opnieuw opgebouwd.
PUT (0, 0), SCHERM, PSET
RETURN

START:                             'Er wordt een nieuw preview bepaald.
RANDOMIZE TIMER                    'Later wordt die omgezet in een blok.
PREVIEW = INT(RND * 7) + 1
XB% = 4                            'beginbreedte
YB% = 0                            'Beginhoogte
R = 1                              'Rotatie-variabele

REM ************                   'Dit is enkel gebruikt tijdens het program-
REM PREVIEW = 1                    'meren en het testen van een bepaald blok.
REM ************

IF PREVIEW = 1 THEN GOSUB TPREVIEW1     'Nu wordt bepaald welk preview gete-
IF PREVIEW = 2 THEN GOSUB TPREVIEW2     'kend moet worden.
IF PREVIEW = 3 THEN GOSUB TPREVIEW3
IF PREVIEW = 4 THEN GOSUB TPREVIEW4
IF PREVIEW = 5 THEN GOSUB TPREVIEW5
IF PREVIEW = 6 THEN GOSUB TPREVIEW6
IF PREVIEW = 7 THEN GOSUB TPREVIEW7

BEPAALSOORTBLOK:
IF SOORTBLOK = 1 THEN GOSUB TBLOK1      'Nu wordt verwezen naar de SUB van
IF SOORTBLOK = 2 THEN GOSUB TBLOK2      'het betreffende blok.
IF SOORTBLOK = 3 THEN GOSUB TBLOK3
IF SOORTBLOK = 4 THEN GOSUB TBLOK4
IF SOORTBLOK = 5 THEN GOSUB TBLOK5
IF SOORTBLOK = 6 THEN GOSUB TBLOK6
IF SOORTBLOK = 7 THEN GOSUB TBLOK7
GOTO EINDE

TBLOK1:
SPATIE = 0                          'Dit is het hart van het programma, het
KLEUR% = POINT(90, 10)             'bestuurt de vallende blokken en brengt ze
IF KLEUR% > 0 THEN GOTO SCORE       'tot stilstand.
KLEUR% = POINT(110, 10)             'Omdat er 7 blokken zijn, staat dit stuk
IF KLEUR% > 0 THEN GOTO SCORE       'er 7 keer in.
KLEUR% = POINT(110, 30)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(130, 10)
IF KLEUR% > 0 THEN GOTO SCORE
BLOK1 XB%, YB%, R, V, RC
V = 0
RC = 0
11 FOR I = 1 TO E
KEY$ = INKEY$
IF KEY$ = CHR$(32) THEN SPATIE = 1
IF SPATIE = 0 THEN PLAY "P64"
IF KEY$ = CHR$(0) + CHR$(72) THEN GOSUB REFRESH: R = R + 1: RC = 1
IF R > 4 THEN R = 1
BLOK1 XB%, YB%, R, V, RC
V = 0
RC = 0
IF YB% = 40 THEN SOORTBLOK = PREVIEW: RETURN
IF KEY$ = CHR$(0) + CHR$(75) THEN GOSUB REFRESH: XB% = XB% - 1: V = -1
IF KEY$ = CHR$(0) + CHR$(77) THEN GOSUB REFRESH: XB% = XB% + 1: V = 1
IF KEY$ = CHR$(0) + CHR$(80) THEN GOTO 12
IF KEY$ = CHR$(80) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(112) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(27) THEN sluitscherm
NEXT I
12 GOSUB REFRESH: YB% = YB% + 1
GOTO 11

TBLOK2:
SPATIE = 0
KLEUR% = POINT(90, 30)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(110, 10)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(110, 30)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(130, 10)
IF KLEUR% > 0 THEN GOTO SCORE
BLOK2 XB%, YB%, R, V, RC
V = 0
RC = 0
21 FOR I = 1 TO E
KEY$ = INKEY$
IF KEY$ = CHR$(32) THEN SPATIE = 1
IF SPATIE = 0 THEN PLAY "P64"
IF KEY$ = CHR$(0) + CHR$(72) THEN GOSUB REFRESH: R = R + 1: RC = 1
IF R > 4 THEN R = 1
BLOK2 XB%, YB%, R, V, RC
V = 0
RC = 0
IF YB% = 40 THEN SOORTBLOK = PREVIEW: RETURN
IF KEY$ = CHR$(0) + CHR$(75) THEN GOSUB REFRESH: XB% = XB% - 1: V = -1
IF KEY$ = CHR$(0) + CHR$(77) THEN GOSUB REFRESH: XB% = XB% + 1: V = 1
IF KEY$ = CHR$(0) + CHR$(80) THEN GOTO 22
IF KEY$ = CHR$(80) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(112) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(27) THEN sluitscherm
NEXT I
22 GOSUB REFRESH: YB% = YB% + 1
GOTO 21

TBLOK3:
SPATIE = 0
KLEUR% = POINT(90, 10)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(110, 10)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(110, 30)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(130, 30)
IF KLEUR% > 0 THEN GOTO SCORE
BLOK3 XB%, YB%, R, V, RC
V = 0
RC = 0
31 FOR I = 1 TO E
KEY$ = INKEY$
IF KEY$ = CHR$(32) THEN SPATIE = 1
IF SPATIE = 0 THEN PLAY "P64"
IF KEY$ = CHR$(0) + CHR$(72) THEN GOSUB REFRESH: R = R + 1: RC = 1
IF R > 4 THEN R = 1
BLOK3 XB%, YB%, R, V, RC
V = 0
RC = 0
IF YB% = 40 THEN SOORTBLOK = PREVIEW: RETURN
IF KEY$ = CHR$(0) + CHR$(75) THEN GOSUB REFRESH: XB% = XB% - 1: V = -1
IF KEY$ = CHR$(0) + CHR$(77) THEN GOSUB REFRESH: XB% = XB% + 1: V = 1
IF KEY$ = CHR$(0) + CHR$(80) THEN GOTO 32
IF KEY$ = CHR$(80) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(112) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(27) THEN sluitscherm
NEXT I
32 GOSUB REFRESH: YB% = YB% + 1
GOTO 31

TBLOK4:
SPATIE = 0
KLEUR% = POINT(90, 10)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(110, 10)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(130, 10)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(150, 10)
IF KLEUR% > 0 THEN GOTO SCORE
BLOK4 XB%, YB%, R, V, RC
V = 0
RC = 0
41 FOR I = 1 TO E
KEY$ = INKEY$
IF KEY$ = CHR$(32) THEN SPATIE = 1
IF SPATIE = 0 THEN PLAY "P64"
IF KEY$ = CHR$(0) + CHR$(72) THEN GOSUB REFRESH: R = R + 1: RC = 1
IF R > 4 THEN R = 1
BLOK4 XB%, YB%, R, V, RC
V = 0
RC = 0
IF YB% = 40 THEN SOORTBLOK = PREVIEW: RETURN
IF KEY$ = CHR$(0) + CHR$(75) THEN GOSUB REFRESH: XB% = XB% - 1: V = -1
IF KEY$ = CHR$(0) + CHR$(77) THEN GOSUB REFRESH: XB% = XB% + 1: V = 1
IF KEY$ = CHR$(0) + CHR$(80) THEN GOTO 42
IF KEY$ = CHR$(80) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(112) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(27) THEN sluitscherm
NEXT I
42 GOSUB REFRESH: YB% = YB% + 1
GOTO 41

TBLOK5:
SPATIE = 0
KLEUR% = POINT(90, 10)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(90, 30)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(90, 50)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(110, 10)
IF KLEUR% > 0 THEN GOTO SCORE
BLOK5 XB%, YB%, R, V, RC
V = 0
RC = 0
51 FOR I = 1 TO E
KEY$ = INKEY$
IF KEY$ = CHR$(32) THEN SPATIE = 1
IF SPATIE = 0 THEN PLAY "P64"
IF KEY$ = CHR$(0) + CHR$(72) THEN GOSUB REFRESH: R = R + 1: RC = 1
IF R > 4 THEN R = 1
BLOK5 XB%, YB%, R, V, RC
V = 0
RC = 0
IF YB% = 40 THEN SOORTBLOK = PREVIEW: RETURN
IF KEY$ = CHR$(0) + CHR$(75) THEN GOSUB REFRESH: XB% = XB% - 1: V = -1
IF KEY$ = CHR$(0) + CHR$(77) THEN GOSUB REFRESH: XB% = XB% + 1: V = 1
IF KEY$ = CHR$(0) + CHR$(80) THEN GOTO 52
IF KEY$ = CHR$(80) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(112) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(27) THEN sluitscherm
NEXT I
52 GOSUB REFRESH: YB% = YB% + 1
GOTO 51

TBLOK6:
SPATIE = 0
KLEUR% = POINT(90, 10)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(110, 10)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(110, 30)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(110, 50)
IF KLEUR% > 0 THEN GOTO SCORE
BLOK6 XB%, YB%, R, V, RC
V = 0
RC = 0
61 FOR I = 1 TO E
KEY$ = INKEY$
IF KEY$ = CHR$(32) THEN SPATIE = 1
IF SPATIE = 0 THEN PLAY "P64"
IF KEY$ = CHR$(0) + CHR$(72) THEN GOSUB REFRESH: R = R + 1: RC = 1
IF R > 4 THEN R = 1
BLOK6 XB%, YB%, R, V, RC
V = 0
RC = 0
IF YB% = 40 THEN SOORTBLOK = PREVIEW: RETURN
IF KEY$ = CHR$(0) + CHR$(75) THEN GOSUB REFRESH: XB% = XB% - 1: V = -1
IF KEY$ = CHR$(0) + CHR$(77) THEN GOSUB REFRESH: XB% = XB% + 1: V = 1
IF KEY$ = CHR$(0) + CHR$(80) THEN GOTO 62
IF KEY$ = CHR$(80) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(112) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(27) THEN sluitscherm
NEXT I
62 GOSUB REFRESH: YB% = YB% + 1
GOTO 61

TBLOK7:
SPATIE = 0
KLEUR% = POINT(90, 10)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(90, 30)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(110, 10)
IF KLEUR% > 0 THEN GOTO SCORE
KLEUR% = POINT(110, 30)
IF KLEUR% > 0 THEN GOTO SCORE
BLOK7 XB%, YB%, R, V, RC
V = 0
RC = 0
71 FOR I = 1 TO E
KEY$ = INKEY$
IF KEY$ = CHR$(32) THEN SPATIE = 1
IF SPATIE = 0 THEN PLAY "P64"
IF KEY$ = CHR$(0) + CHR$(72) THEN GOSUB REFRESH: R = R + 1: RC = 1
IF R > 4 THEN R = 1
BLOK7 XB%, YB%, R, V, RC
V = 0
RC = 0
IF YB% = 40 THEN SOORTBLOK = PREVIEW: RETURN
IF KEY$ = CHR$(0) + CHR$(75) THEN GOSUB REFRESH: XB% = XB% - 1: V = -1
IF KEY$ = CHR$(0) + CHR$(77) THEN GOSUB REFRESH: XB% = XB% + 1: V = 1
IF KEY$ = CHR$(0) + CHR$(80) THEN GOTO 72
IF KEY$ = CHR$(80) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(112) THEN PAUZE: GOSUB NAPAUZE
IF KEY$ = CHR$(27) THEN sluitscherm
NEXT I
72 GOSUB REFRESH: YB% = YB% + 1
GOTO 71

EINDE:                                  'Nu wordt gekeken, of er al rijen zijn
FOR C = 1 TO 18                         'gevormd. Zo ja, dan worden die direct
FOR I = 1 TO 10                         'weggehaald.
        KLEUR% = POINT(I * 20 - 10, C * 20 - 10)
        IF KLEUR% = 0 THEN GOTO 100
NEXT I
LINE (0, C * 20 - 20)-(10 * 20, C * 20 - 1), 0, BF
LINES = LINES + 1
IF LINES = 5 THEN E = E - 1             'In dit stuk wordt de moeilijkheids-
IF LINES = 10 THEN E = E - 1            'graad opgevoerd. (Snelheid)
IF LINES = 15 THEN E = E - 1
IF LINES = 20 THEN E = E - 1
IF LINES = 25 THEN E = E - 1
IF LINES = 30 THEN E = E - 1
IF LINES = 35 THEN E = E - 1
IF LINES = 40 THEN E = E - 1
IF LINES = 45 THEN E = E - 1
IF LINES = 55 THEN E = E - 1
IF LINES = 65 THEN E = E - 1
IF LINES = 60 THEN E = E - 1
IF LINES = 65 THEN E = E - 1
IF LINES = 75 THEN E = E - 1
IF LINES = 85 THEN E = E - 1
IF LINES = 95 THEN E = E - 1
IF LINES = 100 THEN E = E - 1
IF LINES = 120 THEN E = E - 1
IF LINES = 160 THEN E = E - 1
LOCATE 7, 66: COLOR 10: PRINT LINES     'Schrijf het nieuwe aantal rijen naar
GET (0, 0)-(10 * 20, C * 20 - 1), SCHERM    'het scherm.
LINE (0, 0)-(10 * 20, C * 20 - 1), 0, BF
PUT (0, 20), SCHERM
100 NEXT C
GOSUB NIEUWBLOK
GOTO START

SCORE:
LINE (-20, 160)-(220, 250), 0, BF       'Hier wordt gekeken, of de score hoog
LINE (-20, 160)-(220, 250), 13, B       'genoeg is, om in de Highscore te ko-
IF LINES > SCR THEN GOTO 200            'men.
LOCATE 16, 37: COLOR 15: PRINT "GAME OVER": GOTO 300
200 LOCATE 15, 26: COLOR 15: PRINT "Wow, you have a new highscore!"
LOCATE 16, 25: PRINT "You are not a beginner, are you?"
LOCATE 17, 29: PRINT "Please, enter your name:"
LOCATE 18, 29: INPUT NAME$
OPEN "TETRIS.DAT" FOR OUTPUT AS #1
WRITE #1, NAME$, LINES
CLOSE #1
LINES = 0
300 LOCATE 19, 36: PRINT "Play again?"
DO
KEY$ = INKEY$
LOOP WHILE UCASE$(KEY$) <> "Y" AND UCASE$(KEY$) <> "N"
IF UCASE$(KEY$) = "N" THEN sluitscherm
GOTO INIT

TPREVIEW1:
LINE (274, 140)-(356, 201), 0, BF
LINE (290, 140)-(350, 160), 6, BF
LINE (310, 160)-(330, 180), 6, BF
RETURN

TPREVIEW2:
LINE (274, 140)-(356, 201), 0, BF
LINE (310, 140)-(350, 160), 3, BF
LINE (290, 160)-(330, 180), 3, BF
RETURN

TPREVIEW3:
LINE (274, 140)-(356, 201), 0, BF
LINE (290, 140)-(330, 160), 12, BF
LINE (310, 160)-(350, 180), 12, BF
RETURN

TPREVIEW4:
LINE (274, 140)-(356, 201), 0, BF
LINE (275, 140)-(355, 160), 1, BF
RETURN

TPREVIEW5:
LINE (274, 140)-(356, 201), 0, BF
LINE (300, 140)-(340, 160), 14, BF
LINE (300, 160)-(320, 200), 14, BF
RETURN

TPREVIEW6:
LINE (274, 140)-(356, 201), 0, BF
LINE (290, 140)-(330, 160), 10, BF
LINE (310, 160)-(330, 200), 10, BF
RETURN

TPREVIEW7:
LINE (274, 140)-(356, 201), 0, BF
LINE (290, 140)-(330, 180), 4, BF
RETURN

SUB BLOK1 (X%, Y%, R, V, RC)
GOTO EXECUTION

VORM1:
IF X% > 7 THEN X% = 7
IF X% < 0 THEN X% = 0
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN R = 4: RC = 0: GOSUB VORM4: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 59, Y% * 20 + 19), 6, BF
LINE (X% * 20 + 20, Y% * 20 + 20)-(X% * 20 + 39, Y% * 20 + 39), 6, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM2:
IF X% > 8 THEN X% = 8
IF X% < 0 THEN X% = 0
IF RC = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN R = 1: RC = 0: GOSUB VORM1: RETURN
IF V = -1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20 + 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 59), 6, BF
LINE (X% * 20, Y% * 20 + 20)-(X% * 20 + 19, Y% * 20 + 39), 6, BF
IF Y% > 14 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM3:
IF X% > 7 THEN X% = 7
IF X% < 0 THEN X% = 0
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0) THEN R = 2: RC = 0: GOSUB VORM2: RETURN
IF V = -1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20 + 20)-(X% * 20 + 59, Y% * 20 + 39), 6, BF
LINE (X% * 20 + 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 19), 6, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM4:
IF X% > 8 THEN X% = 8
IF X% < 0 THEN X% = 0
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN R = 3: RC = 0: GOSUB VORM3: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 19, Y% * 20 + 59), 6, BF
LINE (X% * 20 + 20, Y% * 20 + 20)-(X% * 20 + 39, Y% * 20 + 39), 6, BF
IF Y% > 14 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

EXECUTION:
IF R = 1 THEN GOSUB VORM1
IF R = 2 THEN GOSUB VORM2
IF R = 3 THEN GOSUB VORM3
IF R = 4 THEN GOSUB VORM4
END SUB

SUB BLOK2 (X%, Y%, R, V, RC)
GOTO EXECUTION1

VORM11:
IF X% < 0 THEN X% = 0
IF X% > 7 THEN X% = 7
IF RC = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN R = 4: RC = 0: GOSUB VORM14: RETURN
IF V = -1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20 + 20, Y% * 20)-(X% * 20 + 59, Y% * 20 + 19), 3, BF
LINE (X% * 20, Y% * 20 + 20)-(X% * 20 + 39, Y% * 20 + 39), 3, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM12:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN R = 1: RC = 0: GOSUB VORM11: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 19, Y% * 20 + 39), 3, BF
LINE (X% * 20 + 20, Y% * 20 + 20)-(X% * 20 + 39, Y% * 20 + 59), 3, BF
IF Y% > 14 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM13:
IF X% < 0 THEN X% = 0
IF X% > 7 THEN X% = 7
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 10) > 0) THEN R = 2: RC = 0: GOSUB VORM12: RETURN
IF V = -1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20 + 20, Y% * 20)-(X% * 20 + 59, Y% * 20 + 19), 3, BF
LINE (X% * 20, Y% * 20 + 20)-(X% * 20 + 39, Y% * 20 + 39), 3, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM14:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN R = 3: RC = 0: GOSUB VORM13: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 19, Y% * 20 + 39), 3, BF
LINE (X% * 20 + 20, Y% * 20 + 20)-(X% * 20 + 39, Y% * 20 + 59), 3, BF
IF Y% > 14 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

EXECUTION1:
IF R = 1 THEN GOSUB VORM11
IF R = 2 THEN GOSUB VORM12
IF R = 3 THEN GOSUB VORM13
IF R = 4 THEN GOSUB VORM14
END SUB

SUB BLOK3 (X%, Y%, R, V, RC)
GOTO EXECUTION2

VORM21:
IF X% < 0 THEN X% = 0
IF X% > 7 THEN X% = 7
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN R = 4: RC = 0: GOSUB VORM24: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 19), 12, BF
LINE (X% * 20 + 20, Y% * 20 + 20)-(X% * 20 + 59, Y% * 20 + 39), 12, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM22:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN R = 1: RC = 0: GOSUB VORM21: RETURN
IF V = -1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20 + 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 39), 12, BF
LINE (X% * 20, Y% * 20 + 20)-(X% * 20 + 19, Y% * 20 + 59), 12, BF
IF Y% > 14 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM23:
IF X% < 0 THEN X% = 0
IF X% > 7 THEN X% = 7
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN R = 2: RC = 0: GOSUB VORM22: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 19), 12, BF
LINE (X% * 20 + 20, Y% * 20 + 20)-(X% * 20 + 59, Y% * 20 + 39), 12, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM24:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN R = 3: RC = 0: GOSUB VORM23: RETURN
IF V = -1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20 + 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 39), 12, BF
LINE (X% * 20, Y% * 20 + 20)-(X% * 20 + 19, Y% * 20 + 59), 12, BF
IF Y% > 14 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

EXECUTION2:
IF R = 1 THEN GOSUB VORM21
IF R = 2 THEN GOSUB VORM22
IF R = 3 THEN GOSUB VORM23
IF R = 4 THEN GOSUB VORM24
END SUB

SUB BLOK4 (X%, Y%, R, V, RC)
GOTO EXECUTION3

VORM31:
IF X% < 0 THEN X% = 0
IF X% > 6 THEN X% = 6
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 70, Y% * 20 + 10) > 0) THEN R = 4: RC = 0: GOSUB VORM34: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 70, Y% * 20 + 10) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 79, Y% * 20 + 19), 1, BF
IF Y% > 16 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 70, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM32:
IF X% < 0 THEN X% = 0
IF X% > 9 THEN X% = 9
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 70) > 0) THEN R = 1: RC = 0: GOSUB VORM31: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 70) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 70) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 19, Y% * 20 + 79), 1, BF
IF Y% > 13 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 90)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM33:
IF X% < 0 THEN X% = 0
IF X% > 6 THEN X% = 6
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 70, Y% * 20 + 10) > 0) THEN R = 2: RC = 0: GOSUB VORM32: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 70, Y% * 20 + 10) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 79, Y% * 20 + 19), 1, BF
IF Y% > 16 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 70, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM34:
IF X% < 0 THEN X% = 0
IF X% > 9 THEN X% = 9
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 70) > 0) THEN R = 3: RC = 0: GOSUB VORM33: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 70) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 70) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 19, Y% * 20 + 79), 1, BF
IF Y% > 13 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 90)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

EXECUTION3:
IF R = 1 THEN GOSUB VORM31
IF R = 2 THEN GOSUB VORM32
IF R = 3 THEN GOSUB VORM33
IF R = 4 THEN GOSUB VORM34
END SUB

SUB BLOK5 (X%, Y%, R, V, RC)
GOTO EXECUTION4

VORM41:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN R = 4: RC = 0: GOSUB VORM44: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 19), 14, BF
LINE (X% * 20, Y% * 20 + 20)-(X% * 20 + 19, Y% * 20 + 59), 14, BF
IF Y% > 14 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM42:
IF X% < 0 THEN X% = 0
IF X% > 7 THEN X% = 7
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN R = 1: RC = 0: GOSUB VORM41: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 59, Y% * 20 + 19), 14, BF
LINE (X% * 20 + 40, Y% * 20 + 20)-(X% * 20 + 59, Y% * 20 + 39), 14, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM43:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN R = 2: RC = 0: GOSUB VORM42: RETURN
IF V = -1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20 + 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 59), 14, BF
LINE (X% * 20, Y% * 20 + 40)-(X% * 20 + 19, Y% * 20 + 59), 14, BF
IF Y% > 14 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM44:
IF X% < 0 THEN X% = 0
IF X% > 7 THEN X% = 7
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN R = 3: RC = 0: GOSUB VORM43: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 19, Y% * 20 + 39), 14, BF
LINE (X% * 20 + 20, Y% * 20 + 20)-(X% * 20 + 59, Y% * 20 + 39), 14, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

EXECUTION4:
IF R = 1 THEN GOSUB VORM41
IF R = 2 THEN GOSUB VORM42
IF R = 3 THEN GOSUB VORM43
IF R = 4 THEN GOSUB VORM44
END SUB

SUB BLOK6 (X%, Y%, R, V, RC)
GOTO EXECUTION5

VORM51:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN R = 4: RC = 0: GOSUB VORM54: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 19), 10, BF
LINE (X% * 20 + 20, Y% * 20 + 20)-(X% * 20 + 39, Y% * 20 + 59), 10, BF
IF Y% > 14 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM52:
IF X% < 0 THEN X% = 0
IF X% > 7 THEN X% = 7
IF RC = 1 AND (POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN R = 1: RC = 0: GOSUB VORM51: RETURN
IF V = -1 AND (POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20 + 40, Y% * 20)-(X% * 20 + 59, Y% * 20 + 19), 10, BF
LINE (X% * 20, Y% * 20 + 20)-(X% * 20 + 59, Y% * 20 + 39), 10, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM53:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN R = 2: RC = 0: GOSUB VORM52: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 50) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 50) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 19, Y% * 20 + 59), 10, BF
LINE (X% * 20 + 20, Y% * 20 + 40)-(X% * 20 + 39, Y% * 20 + 59), 10, BF
IF Y% > 14 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 70)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM54:
IF X% < 0 THEN X% = 0
IF X% > 7 THEN X% = 7
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 50, Y% * 20 + 10) > 0) THEN R = 3: RC = 0: GOSUB VORM53: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 50, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 59, Y% * 20 + 19), 10, BF
LINE (X% * 20, Y% * 20 + 20)-(X% * 20 + 19, Y% * 20 + 39), 10, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 50, Y% * 20 + 30)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

EXECUTION5:
IF R = 1 THEN GOSUB VORM51
IF R = 2 THEN GOSUB VORM52
IF R = 3 THEN GOSUB VORM53
IF R = 4 THEN GOSUB VORM54
END SUB

SUB BLOK7 (X%, Y%, R, V, RC)
GOTO EXECUTION6

VORM61:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN R = 4: RC = 0: GOSUB VORM64: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 39), 4, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM62:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN R = 1: RC = 0: GOSUB VORM61: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 39), 4, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM63:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN R = 2: RC = 0: GOSUB VORM62: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 39), 4, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

VORM64:
IF X% < 0 THEN X% = 0
IF X% > 8 THEN X% = 8
IF RC = 1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN R = 3: RC = 0: GOSUB VORM63: RETURN
IF V = -1 AND (POINT(X% * 20 + 10, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 10, Y% * 20 + 30) > 0) THEN X% = X% + 1: V = 0
IF V = 1 AND (POINT(X% * 20 + 30, Y% * 20 + 10) > 0 OR POINT(X% * 20 + 30, Y% * 20 + 30) > 0) THEN X% = X% - 1: V = 0
LINE (X% * 20, Y% * 20)-(X% * 20 + 39, Y% * 20 + 39), 4, BF
IF Y% > 15 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 10, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
KLEUR% = POINT(X% * 20 + 30, Y% * 20 + 50)
IF KLEUR% <> 0 THEN Y% = 40: RETURN
RETURN

EXECUTION6:
IF R = 1 THEN GOSUB VORM61
IF R = 2 THEN GOSUB VORM62
IF R = 3 THEN GOSUB VORM63
IF R = 4 THEN GOSUB VORM64
END SUB

SUB INTRO                               'Bij de fadende letters aan het begin
SCREEN 13                               'wordt gebruik gemaakt van 63 ver-
CLS
PLAY "P2"                               'schillende kleuren rood.
FOR R = 0 TO 63
KEY$ = INKEY$
IF KEY$ = CHR$(27) THEN GOTO EINDSUB
PALETTE 4, R
LOCATE 12, 17: COLOR 4: PRINT "TETRIS"
NEXT R
PLAY "P1"
FOR R = 63 TO 0 STEP -1
KEY$ = INKEY$
IF KEY$ = CHR$(27) THEN GOTO EINDSUB
PALETTE 4, R
LOCATE 12, 17: COLOR 4: PRINT "TETRIS"
NEXT R

FOR R = 0 TO 63
KEY$ = INKEY$
IF KEY$ = CHR$(27) THEN GOTO EINDSUB
PALETTE 4, R
LOCATE 12, 15: COLOR 4: PRINT "WRITTEN BY"
NEXT R
PLAY "P1"
FOR R = 63 TO 0 STEP -1
KEY$ = INKEY$
IF KEY$ = CHR$(27) THEN GOTO EINDSUB
PALETTE 4, R
LOCATE 12, 15: COLOR 4: PRINT "WRITTEN BY"
NEXT R

FOR R = 0 TO 63
KEY$ = INKEY$
IF KEY$ = CHR$(27) THEN GOTO EINDSUB
PALETTE 4, R
LOCATE 12, 3: COLOR 4: PRINT "ERIK VAN ZIJST & ALEXANDER VAN LOO"
NEXT R
PLAY "P1"
FOR R = 63 TO 0 STEP -1
KEY$ = INKEY$
IF KEY$ = CHR$(27) THEN GOTO EINDSUB
PALETTE 4, R
LOCATE 12, 3: COLOR 4: PRINT "ERIK VAN ZIJST & ALEXANDER VAN LOO"
NEXT R

EINDSUB:
END SUB

SUB PAUZE                               'Dit spreekt voor zich.
LINE (0, 0)-(200, 360), 0, BF
COLOR 15
LOCATE 14, 34: PRINT "Program paused."
LOCATE 16, 33: PRINT "Press any key to"
LOCATE 18, 36: PRINT "continue..."
DO
LOOP WHILE INKEY$ = ""
LINE (0, 0)-(200, 360), 0, BF
END SUB

SUB sluitscherm
LINE (1, 160)-(199, 250), 0, BF
LINE (1, 160)-(199, 250), 13, B
LOCATE 15, 31: COLOR 15: PRINT "Are you sure you want"
LOCATE 16, 37: PRINT "to quit?"
LOCATE 18, 39: PRINT "Y/N"
DO
KEY$ = INKEY$
LOOP WHILE KEY$ <> "Y" AND KEY$ <> "y" AND KEY$ <> "N" AND KEY$ <> "n"
IF KEY$ = "N" OR KEY$ = "n" THEN GOTO 90
IF KEY$ = "Y" OR KEY$ = "y" THEN GOTO 80

80 CLS
COLOR 4: LOCATE 6, 13: PRINT "Tetris SVGA was written as SHAREWARE."

COLOR 15: LOCATE 8, 13: PRINT "You may distribute this program freely."
LOCATE 9, 13: PRINT "Decompiling or any other form of change to the program"
LOCATE 10, 13: PRINT "without permission of the authors is not allowed."

LOCATE 12, 13: PRINT "To keep our productivity at a high level, you are"
LOCATE 13, 13: PRINT "encouraged to pay a small fee."
LOCATE 14, 13: PRINT "Any comments about bugs or other improvements"
LOCATE 15, 13: PRINT "are always welcome!"
LOCATE 16, 13: PRINT "Special thanks go to Hans van Zijst!"

LOCATE 18, 13: PRINT "Erik van Zijst (zijst@student.tn.tudelft.nl) &"
LOCATE 19, 13: PRINT "Alexander van Loo (loo@student.tn.tudelft.nl)"

LOCATE 24, 13: COLOR 14: PRINT "Tetris SVGA v3.0, January 1996"
LOCATE 25, 13: COLOR 14: PRINT "Written and compiled in MS-QuickBasic v4.0"
LOCATE 28, 60: COLOR 15: PRINT "<ENTER>"
DO
LOOP UNTIL INKEY$ <> ""
END

90 LINE (1, 160)-(199, 250), 0, BF
END SUB

SUB TEXT
LINES = 0
LOCATE 2, 36
COLOR 14
PRINT "TETRIS SVGA"
COLOR 15
LOCATE 5, 1: COLOR 4: PRINT "TETRIS SVGA Version 3.0"
LOCATE 6, 3: COLOR 15: PRINT "Was Programmed By:"
LOCATE 8, 3: COLOR 9: PRINT "Erik Van Zijst &"
LOCATE 9, 3: COLOR 9: PRINT "Alexander Van Loo"
LOCATE 5, 66: COLOR 4: PRINT "Lines"
LOCATE 6, 65: COLOR 15: PRINT "旼컴컴�"
LOCATE 7, 65: COLOR 15: PRINT "�     �"
LOCATE 8, 65: COLOR 15: PRINT "읕컴컴�"
LOCATE 7, 66: COLOR 10: PRINT LINES
COLOR 4: LOCATE 15, 8: PRINT "Controls"
COLOR 15: LOCATE 16, 2: PRINT "컴컴컴컴컴컴컴컴컴컴�"
LOCATE 17, 2: PRINT "Up       = Rotate"
LOCATE 18, 2: PRINT "Left     = Move left"
LOCATE 19, 2: PRINT "Right    = Move right"
LOCATE 20, 2: PRINT "Down     = Move down"
LOCATE 21, 2: PRINT "Spacebar = Drop"
LOCATE 22, 2: PRINT "P        = Pause"
LOCATE 23, 2: PRINT "Esc      = Quit"
LOCATE 11, 63: COLOR 4: PRINT "Next block:"
LOCATE 12, 58: COLOR 15: PRINT "컴컴컴컴컴컴컴컴컴컴�"
END SUB

