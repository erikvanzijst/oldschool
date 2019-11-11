REM ***   COMPUTER LOCK VERSION 1.1                                       ***

REM ***   COPYRIGHT 1995 BY A.P.J. VAN LOO                                ***
REM ***   MODIFICATION OF THE PROGRAM (OTHER THAN CHANGING ACCESSCODES)   ***
REM ***   IS NOT ALLOWED!!                                                ***

REM ***   FOR COMMENTS ON THIS PROGRAM WRITE TO:                          ***
REM ***   A.P.J. VAN LOO                                                  ***
REM ***   BAETENBURG 23                                                   ***
REM ***   1852 TP  HEILOO                                                 ***
REM ***   THE NETHERLANDS                                                 ***
REM ***   E-MAIL: LOO@STUDENT.TN.TUDELFT.NL                               ***

DECLARE SUB LetterD (X!, Y!, co!)
DECLARE SUB LetterE (X!, Y!, co!)
DECLARE SUB LetterK (X!, Y!, co!)
DECLARE SUB LetterL (X!, Y!, co!)
DECLARE SUB LetterM (X!, Y!, co!)
DECLARE SUB LetterN (X!, Y!, co!)
DECLARE SUB LetterO (X!, Y!, co!)
DECLARE SUB LetterS (X!, Y!, co!)
DECLARE SUB LetterT (X!, Y!, co!)
DECLARE SUB LetterU (X!, Y!, co!)
DECLARE SUB LetterY (X!, Y!, co!)
DECLARE SUB LetterC (X!, Y!, co!)
DECLARE SUB TextSystem ()
DECLARE SUB TextUnlocked ()
DECLARE SUB TextLocked ()
DECLARE SUB LeesCodeIn (DC!)
DECLARE SUB DisplayText (DC!)
DECLARE SUB ScreenLocked ()
DECLARE SUB ScreenUnlocked ()
DECLARE SUB Count3 (DC!)
DECLARE SUB Count5 (DC!)
DECLARE SUB ScreenSaver (noc!)

SCREEN 12
10 CLS
CONST pi = 3.141592654#
ScreenLocked
LeesCodeIn 6
CODE$ = key1$ + key2$ + key3$ + key4$ + key5$ + key6$ + key7$ + key8$
ACCESS1$ = "19770113"
ACCESS2$ = "84216795"
ACCESS3$ = "19780627"
IF CODE$ <> ACCESS1$ AND CODE$ <> ACCESS2$ AND CODE$ <> ACCESS3$ THEN LOCATE 16, 28: COLOR 6: PRINT "ACCESS DENIED": PLAY "O3C8O3C8O3C8": Count5 6: GOTO 10
IF CODE$ = ACCESS1$ OR CODE$ = ACCESS2$ OR CODE$ = ACCESS3$ THEN LOCATE 16, 28: COLOR 6: PRINT "ACCESS GRANTED": PLAY "O3G24O3E24O3C24"
ScreenUnlocked
Count3 6
COLOR 7: SYSTEM

SUB Count3 (DC)
PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "3": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "2": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "1": PLAY "P2"
END SUB

SUB Count5 (DC)
PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "5": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "4": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "3": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "2": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "1": PLAY "P2"
END SUB

SUB DisplayText (DC)
LOCATE 15, 26: COLOR DC: PRINT "                              "
LOCATE 16, 26: COLOR DC: PRINT "                              "
LOCATE 15, 28: COLOR DC: PRINT "ENTER ACCESSCODE: ðððððððð"
END SUB

SUB LeesCodeIn (DC)
SHARED key1$, key2$, key3$, key4$, key5$, key6$, key7$, key8$
noc = 300
nos = 300 * noc

100 DO
key1$ = INKEY$
scrn = scrn + 1
IF scrn > nos THEN ScreenSaver noc: scrn = 0: GOTO 100
LOOP UNTIL key1$ <> ""
LOCATE 15, 46: COLOR DC: PRINT "þ"
scrn = 0

DO
key2$ = INKEY$
scrn = scrn + 1
IF scrn > nos THEN ScreenSaver noc: scrn = 0: GOTO 100
LOOP UNTIL key2$ <> ""
LOCATE 15, 47: COLOR DC: PRINT "þ"
scrn = 0

DO
key3$ = INKEY$
scrn = scrn + 1
IF scrn > nos THEN ScreenSaver noc: scrn = 0: GOTO 100
LOOP UNTIL key3$ <> ""
LOCATE 15, 48: COLOR DC: PRINT "þ"
scrn = 0

DO
key4$ = INKEY$
scrn = scrn + 1
IF scrn > nos THEN ScreenSaver noc: scrn = 0: GOTO 100
LOOP UNTIL key4$ <> ""
LOCATE 15, 49: COLOR DC: PRINT "þ"
scrn = 0

DO
key5$ = INKEY$
scrn = scrn + 1
IF scrn > nos THEN ScreenSaver noc: scrn = 0: GOTO 100
LOOP UNTIL key5$ <> ""
LOCATE 15, 50: COLOR DC: PRINT "þ"
scrn = 0

DO
key6$ = INKEY$
scrn = scrn + 1
IF scrn > nos THEN ScreenSaver noc: scrn = 0: GOTO 100
LOOP UNTIL key6$ <> ""
LOCATE 15, 51: COLOR DC: PRINT "þ"
scrn = 0

DO
key7$ = INKEY$
scrn = scrn + 1
IF scrn > nos THEN ScreenSaver noc: scrn = 0: GOTO 100
LOOP UNTIL key7$ <> ""
LOCATE 15, 52: COLOR DC: PRINT "þ"
scrn = 0

DO
key8$ = INKEY$
scrn = scrn + 1
IF scrn > nos THEN ScreenSaver noc: scrn = 0: GOTO 100
LOOP UNTIL key8$ <> ""
LOCATE 15, 53: COLOR DC: PRINT "þ"
scrn = 0

END SUB

SUB LetterC (X, Y, co)
CIRCLE (X + 20, Y + 20), 20, co, 0, pi
CIRCLE (X + 20, Y + 20), 15, co, 0, pi
CIRCLE (X + 20, Y + 55), 20, co, 1 * pi, 2 * pi
CIRCLE (X + 20, Y + 55), 15, co, 1 * pi, 2 * pi
LINE (X, Y + 20)-(X, Y + 55), co
LINE (X + 5, Y + 20)-(X + 5, Y + 55), co
LINE (X + 35, Y + 20)-(X + 40, Y + 20), co
LINE (X + 35, Y + 55)-(X + 40, Y + 55), co
PAINT (X + 20, Y + 2), co, co
END SUB

SUB LetterD (X, Y, co)
CIRCLE (X + 20, Y + 20), 20, co, 0, .5 * pi
CIRCLE (X + 20, Y + 20), 15, co, 0, .5 * pi
CIRCLE (X + 20, Y + 55), 20, co, 1.5 * pi, 2 * pi
CIRCLE (X + 20, Y + 55), 15, co, 1.5 * pi, 2 * pi
LINE (X, Y)-(X, Y + 75), co
LINE (X + 5, Y + 5)-(X + 5, Y + 70), co
LINE (X + 35, Y + 20)-(X + 35, Y + 55), co
LINE (X + 40, Y + 20)-(X + 40, Y + 55), co
LINE (X, Y)-(X + 20, Y), co
LINE (X + 5, Y + 5)-(X + 20, Y + 5), co
LINE (X + 5, Y + 70)-(X + 20, Y + 70), co
LINE (X, Y + 75)-(X + 20, Y + 75), co
PAINT (X + 2, Y + 2), co, co
END SUB

SUB LetterE (X, Y, co)
LINE (X, Y)-(X + 40, Y), co
LINE (X + 5, Y + 5)-(X + 40, Y + 5), co
LINE (X + 5, Y + 35)-(X + 40, Y + 35), co
LINE (X + 5, Y + 40)-(X + 40, Y + 40), co
LINE (X + 5, Y + 70)-(X + 40, Y + 70), co
LINE (X, Y + 75)-(X + 40, Y + 75), co
LINE (X, Y)-(X, Y + 75), co
LINE (X + 40, Y)-(X + 40, Y + 5), co
LINE (X + 5, Y + 5)-(X + 5, Y + 35), co
LINE (X + 40, Y + 35)-(X + 40, Y + 40), co
LINE (X + 5, Y + 40)-(X + 5, Y + 70), co
LINE (X + 40, Y + 70)-(X + 40, Y + 75), co
PAINT (X + 2, Y + 2), co, co
END SUB

SUB LetterK (X, Y, co)
LINE (X, Y)-(X, Y + 75), co
LINE (X + 5, Y)-(X + 5, Y + 75), co
LINE (X, Y)-(X + 5, Y), co
LINE (X, Y + 75)-(X + 5, Y + 75), co
LINE (X + 35, Y)-(X + 40, Y), co
LINE (X + 35, Y + 75)-(X + 40, Y + 75), co
LINE (X + 35, Y)-(X, Y + 37.5), co
LINE (X + 40, Y)-(X + 5, Y + 37.5), co
LINE (X + 35, Y + 75)-(X, Y + 37.5), co
LINE (X + 40, Y + 75)-(X + 5, Y + 37.5), co
PAINT (X + 2, Y + 2), co, co
PAINT (X + 2, Y + 37.5), co, co
PAINT (X + 2, Y + 73), co, co
PAINT (X + 37, Y + 2), co, co
PAINT (X + 37, Y + 73), co, co
END SUB

SUB LetterL (X, Y, co)
LINE (X, Y)-(X, Y + 75), co
LINE (X + 5, Y)-(X + 5, Y + 70), co
LINE (X + 5, Y + 70)-(X + 40, Y + 70), co
LINE (X, Y + 75)-(X + 40, Y + 75), co
LINE (X, Y)-(X + 5, Y), co
LINE (X + 40, Y + 70)-(X + 40, Y + 75), co
PAINT (X + 2, Y + 2), co, co
END SUB

SUB LetterM (X, Y, co)
LINE (X, Y)-(X, Y + 75), co
LINE (X + 40, Y)-(X + 40, Y + 75), co
LINE (X + 5, Y + 5)-(X + 5, Y + 75), co
LINE (X + 35, Y + 5)-(X + 35, Y + 75), co
LINE (X, Y)-(X + 5, Y), co
LINE (X + 35, Y)-(X + 40, Y), co
LINE (X + 17.5, Y + 37.5)-(X + 22.5, Y + 37.5), co
LINE (X, Y + 75)-(X + 5, Y + 75), co
LINE (X + 35, Y + 75)-(X + 40, Y + 75), co
LINE (X, Y)-(X + 17.5, Y + 37.5), co
LINE (X + 5, Y)-(X + 22.5, Y + 37.5), co
LINE (X + 35, Y)-(X + 17.5, Y + 37.5), co
LINE (X + 40, Y)-(X + 22.5, Y + 37.5), co
PAINT (X + 2, Y + 2), co, co
PAINT (X + 2, Y + 10), co, co
PAINT (X + 38, Y + 2), co, co
PAINT (X + 38, Y + 10), co, co
PAINT (X + 20, Y + 36), co, co
END SUB

SUB LetterN (X, Y, co)
LINE (X, Y)-(X, Y + 75), co
LINE (X + 5, Y + 5)-(X + 5, Y + 75), co
LINE (X, Y)-(X + 5, Y), co
LINE (X, Y + 75)-(X + 5, Y + 75), co
LINE (X + 40, Y)-(X + 40, Y + 75), co
LINE (X + 35, Y)-(X + 35, Y + 70), co
LINE (X + 35, Y)-(X + 40, Y), co
LINE (X + 35, Y + 75)-(X + 40, Y + 75), co
LINE (X, Y)-(X + 35, Y + 75), co
LINE (X + 5, Y)-(X + 40, Y + 75), co
PAINT (X + 2, Y + 73), co, co
PAINT (X + 20, Y + 37.5), co, co
PAINT (X + 38, Y + 2), co, co
END SUB

SUB LetterO (X, Y, co)
CIRCLE (X + 20, Y + 20), 20, co, 0, pi
CIRCLE (X + 20, Y + 20), 15, co, 0, pi
CIRCLE (X + 20, Y + 55), 20, co, 1 * pi, 2 * pi
CIRCLE (X + 20, Y + 55), 15, co, 1 * pi, 2 * pi
LINE (X, Y + 20)-(X, Y + 55), co
LINE (X + 5, Y + 20)-(X + 5, Y + 55), co
LINE (X + 35, Y + 20)-(X + 35, Y + 55), co
LINE (X + 40, Y + 20)-(X + 40, Y + 55), co
PAINT (X + 20, Y + 2), co, co
END SUB

SUB LetterS (X, Y, co)
CIRCLE (X + 20, Y + 20), 20, co, 0, (1.5 * pi)
CIRCLE (X + 20, Y + 55), 20, co, 0, (.5 * pi)
CIRCLE (X + 20, Y + 55), 20, co, (1 * pi), (2 * pi)
CIRCLE (X + 20, Y + 20), 15, co, 0, (1.5 * pi)
CIRCLE (X + 20, Y + 55), 15, co, 0, (.5 * pi)
CIRCLE (X + 20, Y + 55), 15, co, (1 * pi), (2 * pi)
LINE (X + 35, Y + 20)-(X + 40, Y + 20), co
LINE (X, Y + 55)-(X + 5, Y + 55), co
PAINT (X + 20, Y + 37.5), co, co
END SUB

SUB LetterT (X, Y, co)
LINE (X, Y)-(X, (Y + 5)), co
LINE ((X + 40), Y)-((X + 40), (Y + 5)), co
LINE ((X + 17.5), (Y + 75))-((X + 22.5), (Y + 75)), co
LINE (X, Y)-((X + 40), Y), co
LINE (X, Y + 5)-(X + 17.5, Y + 5), co
LINE (X + 22.5, Y + 5)-(X + 40, Y + 5), co
LINE (X + 17.5, Y + 5)-(X + 17.5, Y + 75), co
LINE (X + 22.5, Y + 5)-(X + 22.5, Y + 75), co
PAINT (X + 2, Y + 2), co, co
END SUB

SUB LetterU (X, Y, co)
LINE (X, Y)-(X, Y + 55), co
LINE (X + 5, Y)-(X + 5, Y + 55), co
LINE (X + 35, Y)-(X + 35, Y + 55), co
LINE (X + 40, Y)-(X + 40, Y + 55), co
LINE (X, Y)-(X + 5, Y), co
LINE (X + 35, Y)-(X + 40, Y), co
CIRCLE (X + 20, Y + 55), 20, co, 1 * pi, 2 * pi
CIRCLE (X + 20, Y + 55), 15, co, 1 * pi, 2 * pi
PAINT (X + 2, Y + 2), co, co
END SUB

SUB LetterY (X, Y, co)
LINE (X, Y)-((X + 5), Y), co
LINE ((X + 35), Y)-((X + 40), Y), co
LINE (X, (Y + 75))-((X + 5), (Y + 75)), co
LINE (X, Y)-((X + 17.5), (Y + 37.5)), co
LINE ((X + 40), Y)-((X + 5), (Y + 75)), co
LINE ((X + 35), Y)-(X, (Y + 75)), co
LINE ((X + 5), Y)-((X + 22.5), (Y + 37.5)), co
PAINT ((X + 2), (Y + 2)), co, co
PAINT ((X + 2), (Y + 73)), co, co
PAINT ((X + 38), (Y + 2)), co, co
END SUB

SUB ScreenLocked
PAINT (1, 1), 4
LINE (195, 212)-(445, 267), 0, BF
LINE (195, 212)-(445, 267), 12, B
TextSystem
TextLocked
DisplayText 6
END SUB

SUB ScreenSaver (noc)
SCREEN 12
CLS
STATUS$ = "LOCKED"
LOCATE 1, 1: COLOR 7: PRINT "EXIT     COLOR"
LOCATE 1, 2: COLOR 15: PRINT "X": LOCATE 1, 10: PRINT "C"
LOCATE 1, 68: COLOR 7: PRINT "DIGICLOCK 1.0"
LINE (0, 16)-(639, 16), 7
C = 8
C0 = 0
C1 = 15
GOTO 200

50 REM GRID
LINE (X, Y)-(X + 100, Y + 160), C, B
LINE (X + 20, Y + 20)-(X + 20, Y + 70), C
LINE (X + 20, Y + 90)-(X + 20, Y + 140), C
LINE (X + 80, Y + 20)-(X + 80, Y + 70), C
LINE (X + 80, Y + 90)-(X + 80, Y + 140), C
LINE (X + 20, Y + 20)-(X + 80, Y + 20), C
LINE (X + 20, Y + 70)-(X + 80, Y + 70), C
LINE (X + 20, Y + 90)-(X + 80, Y + 90), C
LINE (X + 20, Y + 140)-(X + 80, Y + 140), C
LINE (X, Y)-(X + 20, Y + 20), C
LINE (X + 80, Y + 20)-(X + 100, Y), C
LINE (X + 10, Y + 80)-(X + 20, Y + 70), C
LINE (X + 10, Y + 80)-(X + 20, Y + 90), C
LINE (X + 90, Y + 80)-(X + 80, Y + 70), C
LINE (X + 90, Y + 80)-(X + 80, Y + 90), C
LINE (X, Y + 160)-(X + 20, Y + 140), C
LINE (X + 80, Y + 140)-(X + 100, Y + 160), C
LINE (X, Y + 80)-(X + 10, Y + 80), C
LINE (X + 90, Y + 80)-(X + 100, Y + 80), C
RETURN

101 REM 1
PAINT (X + 10, Y + 40), C0, C
PAINT (X + 50, Y + 10), C0, C
PAINT (X + 90, Y + 40), C1, C
PAINT (X + 50, Y + 80), C0, C
PAINT (X + 10, Y + 120), C0, C
PAINT (X + 50, Y + 150), C0, C
PAINT (X + 90, Y + 120), C1, C
RETURN

102 REM 2
PAINT (X + 10, Y + 40), C0, C
PAINT (X + 50, Y + 10), C1, C
PAINT (X + 90, Y + 40), C1, C
PAINT (X + 50, Y + 80), C1, C
PAINT (X + 10, Y + 120), C1, C
PAINT (X + 50, Y + 150), C1, C
PAINT (X + 90, Y + 120), C0, C
RETURN

103 REM 3
PAINT (X + 10, Y + 40), C0, C
PAINT (X + 50, Y + 10), C1, C
PAINT (X + 90, Y + 40), C1, C
PAINT (X + 50, Y + 80), C1, C
PAINT (X + 10, Y + 120), C0, C
PAINT (X + 50, Y + 150), C1, C
PAINT (X + 90, Y + 120), C1, C
RETURN

104 REM 4
PAINT (X + 10, Y + 40), C1, C
PAINT (X + 50, Y + 10), C0, C
PAINT (X + 90, Y + 40), C1, C
PAINT (X + 50, Y + 80), C1, C
PAINT (X + 10, Y + 120), C0, C
PAINT (X + 50, Y + 150), C0, C
PAINT (X + 90, Y + 120), C1, C
RETURN

105 REM 5
PAINT (X + 10, Y + 40), C1, C
PAINT (X + 50, Y + 10), C1, C
PAINT (X + 90, Y + 40), C0, C
PAINT (X + 50, Y + 80), C1, C
PAINT (X + 10, Y + 120), C0, C
PAINT (X + 50, Y + 150), C1, C
PAINT (X + 90, Y + 120), C1, C
RETURN

106 REM 6
PAINT (X + 10, Y + 40), C1, C
PAINT (X + 50, Y + 10), C1, C
PAINT (X + 90, Y + 40), C0, C
PAINT (X + 50, Y + 80), C1, C
PAINT (X + 10, Y + 120), C1, C
PAINT (X + 50, Y + 150), C1, C
PAINT (X + 90, Y + 120), C1, C
RETURN

107 REM 7
PAINT (X + 10, Y + 40), C0, C
PAINT (X + 50, Y + 10), C1, C
PAINT (X + 90, Y + 40), C1, C
PAINT (X + 50, Y + 80), C0, C
PAINT (X + 10, Y + 120), C0, C
PAINT (X + 50, Y + 150), C0, C
PAINT (X + 90, Y + 120), C1, C
RETURN

108 REM 8
PAINT (X + 10, Y + 40), C1, C
PAINT (X + 50, Y + 10), C1, C
PAINT (X + 90, Y + 40), C1, C
PAINT (X + 50, Y + 80), C1, C
PAINT (X + 10, Y + 120), C1, C
PAINT (X + 50, Y + 150), C1, C
PAINT (X + 90, Y + 120), C1, C
RETURN

109 REM 9
PAINT (X + 10, Y + 40), C1, C
PAINT (X + 50, Y + 10), C1, C
PAINT (X + 90, Y + 40), C1, C
PAINT (X + 50, Y + 80), C1, C
PAINT (X + 10, Y + 120), C0, C
PAINT (X + 50, Y + 150), C1, C
PAINT (X + 90, Y + 120), C1, C
RETURN

110 REM 0
PAINT (X + 10, Y + 40), C1, C
PAINT (X + 50, Y + 10), C1, C
PAINT (X + 90, Y + 40), C1, C
PAINT (X + 50, Y + 80), C0, C
PAINT (X + 10, Y + 120), C1, C
PAINT (X + 50, Y + 150), C1, C
PAINT (X + 90, Y + 120), C1, C
RETURN

200 DO
X = 70: Y = 160: GOSUB 50: X = 190: GOSUB 50: X = 350: GOSUB 50: X = 470: GOSUB 50
CIRCLE (320, Y + 40), 10, C
CIRCLE (320, Y + 120), 10, C

250 REM TRANSLATION TIME$ TO DIGIFONT
H1$ = LEFT$(TIME$, 1)
H2$ = MID$(TIME$, 2, 1)
M1$ = MID$(TIME$, 4, 1)
M2$ = MID$(TIME$, 5, 1)
S1$ = MID$(TIME$, 7, 1)
S2$ = RIGHT$(TIME$, 1)

IF H1$ = "0" THEN X = 70: GOSUB 110
IF H1$ = "1" THEN X = 70: GOSUB 101
IF H1$ = "2" THEN X = 70: GOSUB 102

IF H2$ = "0" THEN X = 190: GOSUB 110
IF H2$ = "1" THEN X = 190: GOSUB 101
IF H2$ = "2" THEN X = 190: GOSUB 102
IF H2$ = "3" THEN X = 190: GOSUB 103
IF H2$ = "4" THEN X = 190: GOSUB 104
IF H2$ = "5" THEN X = 190: GOSUB 105
IF H2$ = "6" THEN X = 190: GOSUB 106
IF H2$ = "7" THEN X = 190: GOSUB 107
IF H2$ = "8" THEN X = 190: GOSUB 108
IF H2$ = "9" THEN X = 190: GOSUB 109

IF M1$ = "0" THEN X = 350: GOSUB 110
IF M1$ = "1" THEN X = 350: GOSUB 101
IF M1$ = "2" THEN X = 350: GOSUB 102
IF M1$ = "3" THEN X = 350: GOSUB 103
IF M1$ = "4" THEN X = 350: GOSUB 104
IF M1$ = "5" THEN X = 350: GOSUB 105
IF M1$ = "6" THEN X = 350: GOSUB 106

IF M2$ = "0" THEN X = 470: GOSUB 110
IF M2$ = "1" THEN X = 470: GOSUB 101
IF M2$ = "2" THEN X = 470: GOSUB 102
IF M2$ = "3" THEN X = 470: GOSUB 103
IF M2$ = "4" THEN X = 470: GOSUB 104
IF M2$ = "5" THEN X = 470: GOSUB 105
IF M2$ = "6" THEN X = 470: GOSUB 106
IF M2$ = "7" THEN X = 470: GOSUB 107
IF M2$ = "8" THEN X = 470: GOSUB 108
IF M2$ = "9" THEN X = 470: GOSUB 109

IF S2$ = "0" THEN PAINT (320, Y + 120), C0, C: PAINT (320, Y + 40), C1, C
IF S2$ = "1" THEN PAINT (320, Y + 40), C0, C: PAINT (320, Y + 120), C1, C
IF S2$ = "2" THEN PAINT (320, Y + 120), C0, C: PAINT (320, Y + 40), C1, C
IF S2$ = "3" THEN PAINT (320, Y + 40), C0, C: PAINT (320, Y + 120), C1, C
IF S2$ = "4" THEN PAINT (320, Y + 120), C0, C: PAINT (320, Y + 40), C1, C
IF S2$ = "5" THEN PAINT (320, Y + 40), C0, C: PAINT (320, Y + 120), C1, C
IF S2$ = "6" THEN PAINT (320, Y + 120), C0, C: PAINT (320, Y + 40), C1, C
IF S2$ = "7" THEN PAINT (320, Y + 40), C0, C: PAINT (320, Y + 120), C1, C
IF S2$ = "8" THEN PAINT (320, Y + 120), C0, C: PAINT (320, Y + 40), C1, C
IF S2$ = "9" THEN PAINT (320, Y + 40), C0, C: PAINT (320, Y + 120), C1, C

IF S2$ = "0" AND STATUS$ = "LOCKED" THEN LOCATE 1, 68: COLOR 4: PRINT "SYSTEM LOCKED": COLOR 7
IF S2$ = "1" AND STATUS$ = "LOCKED" THEN LOCATE 1, 68: COLOR 7: PRINT "DIGICLOCK 1.0"
IF S2$ = "2" AND STATUS$ = "LOCKED" THEN LOCATE 1, 68: COLOR 4: PRINT "SYSTEM LOCKED": COLOR 7
IF S2$ = "3" AND STATUS$ = "LOCKED" THEN LOCATE 1, 68: COLOR 7: PRINT "DIGICLOCK 1.0"
IF S2$ = "4" AND STATUS$ = "LOCKED" THEN LOCATE 1, 68: COLOR 4: PRINT "SYSTEM LOCKED": COLOR 7
IF S2$ = "5" AND STATUS$ = "LOCKED" THEN LOCATE 1, 68: COLOR 7: PRINT "DIGICLOCK 1.0"
IF S2$ = "6" AND STATUS$ = "LOCKED" THEN LOCATE 1, 68: COLOR 4: PRINT "SYSTEM LOCKED": COLOR 7
IF S2$ = "7" AND STATUS$ = "LOCKED" THEN LOCATE 1, 68: COLOR 7: PRINT "DIGICLOCK 1.0"
IF S2$ = "8" AND STATUS$ = "LOCKED" THEN LOCATE 1, 68: COLOR 4: PRINT "SYSTEM LOCKED": COLOR 7
IF S2$ = "9" AND STATUS$ = "LOCKED" THEN LOCATE 1, 68: COLOR 7: PRINT "DIGICLOCK 1.0"

COUNT% = COUNT% + 1
IF COUNT% > noc THEN COUNT% = 0: GOTO 500

KEY$ = INKEY$
IF KEY$ = "c" THEN C1 = C1 + 1
IF KEY$ = "C" THEN C1 = C1 + 1
IF KEY$ = "X" THEN GOTO 500
IF KEY$ = "x" THEN GOTO 500
IF C1 > 15 THEN C1 = 1
IF C1 = C AND KEY$ = "c" THEN C1 = C + 1
IF C1 = C AND KEY$ = "C" THEN C1 = C + 1
LOOP
500 CLS
ScreenLocked
END SUB

SUB ScreenUnlocked
LINE (195, 212)-(445, 267), 10, B
PAINT (1, 1), 2, 10
TextSystem
TextUnlocked
END SUB

SUB TextLocked
LetterL 175, 335, 15
LetterO 225, 335, 15
LetterC 275, 335, 15
LetterK 325, 335, 15
LetterE 375, 335, 15
LetterD 425, 335, 15
END SUB

SUB TextSystem
LetterS 175, 70, 15: REM SYSTEM
LetterY 225, 70, 15
LetterS 275, 70, 15
LetterT 325, 70, 15
LetterE 375, 70, 15
LetterM 425, 70, 15
END SUB

SUB TextUnlocked
LetterU 125, 335, 15
LetterN 175, 335, 15
LetterL 225, 335, 15
LetterO 275, 335, 15
LetterC 325, 335, 15
LetterK 375, 335, 15
LetterE 425, 335, 15
LetterD 475, 335, 15
END SUB

