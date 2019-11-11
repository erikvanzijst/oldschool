REM ***   COMPUTER SOFTWARE LOCK v1.1  ***

DECLARE SUB LetterD (x!, y!, co!)
DECLARE SUB LetterE (x!, y!, co!)
DECLARE SUB LetterK (x!, y!, co!)
DECLARE SUB LetterL (x!, y!, co!)
DECLARE SUB LetterM (x!, y!, co!)
DECLARE SUB LetterN (x!, y!, co!)
DECLARE SUB LetterO (x!, y!, co!)
DECLARE SUB LetterS (x!, y!, co!)
DECLARE SUB LetterT (x!, y!, co!)
DECLARE SUB LetterU (x!, y!, co!)
DECLARE SUB LetterY (x!, y!, co!)
DECLARE SUB LetterC (x!, y!, co!)
DECLARE SUB TextSystem ()
DECLARE SUB TextUnlocked ()
DECLARE SUB TextLocked ()

SCREEN 12
10 CLS

CONST pi = 3.141592654#
DC = 6: REM DISPLAY COLOR

REM RED
PAINT (1, 1), 4
LINE (195, 212)-(445, 267), 0, BF
LINE (195, 212)-(445, 267), 12, B
GOTO 150

150 REM DISPLAY TEXT
TextSystem
TextLocked
LOCATE 15, 26: COLOR DC: PRINT "                              "
LOCATE 16, 26: COLOR DC: PRINT "                              "
LOCATE 15, 28: COLOR DC: PRINT "ENTER ACCESSCODE: ðððððððð"

DO: REM INPUT ACCESSCODE
key1$ = INKEY$
LOOP UNTIL key1$ <> ""
LOCATE 15, 46: COLOR DC: PRINT "þ"

DO
key2$ = INKEY$
LOOP UNTIL key2$ <> ""
LOCATE 15, 47: COLOR DC: PRINT "þ"

DO
key3$ = INKEY$
LOOP UNTIL key3$ <> ""
LOCATE 15, 48: COLOR DC: PRINT "þ"

DO
key4$ = INKEY$
LOOP UNTIL key4$ <> ""
LOCATE 15, 49: COLOR DC: PRINT "þ"

DO
key5$ = INKEY$
LOOP UNTIL key5$ <> ""
LOCATE 15, 50: COLOR DC: PRINT "þ"

DO
key6$ = INKEY$
LOOP UNTIL key6$ <> ""
LOCATE 15, 51: COLOR DC: PRINT "þ"

DO
key7$ = INKEY$
LOOP UNTIL key7$ <> ""
LOCATE 15, 52: COLOR DC: PRINT "þ"

DO
key8$ = INKEY$
LOOP UNTIL key8$ <> ""
LOCATE 15, 53: COLOR DC: PRINT "þ"

REM CHECK ACCESSCODE
CODE$ = key1$ + key2$ + key3$ + key4$ + key5$ + key6$ + key7$ + key8$
ACCESS1$ = "19770113"
ACCESS2$ = "84216795"
ACCESS3$ = "19780627"
IF CODE$ = ACCESS1$ OR CODE$ = ACCESS2$ OR CODE$ = ACCESS3$ THEN LOCATE 16, 28: COLOR DC: PRINT "ACCESS GRANTED": PLAY "O3G24O3E24O3C24": GOSUB 250: GOTO 600
IF CODE$ <> ACCESS1$ AND CODE$ <> ACCESS2$ AND CODE$ <> ACCESS3$ THEN LOCATE 16, 28: COLOR DC: PRINT "ACCESS DENIED": PLAY "O3C8O3C8O3C8"
GOTO 500

250 REM GREEN
LINE (195, 212)-(445, 267), 10, B
PAINT (1, 1), 2, 10
TextSystem
TextUnlocked
RETURN

500 REM ACCESS DENIED
PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "5": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "4": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "3": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "2": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "1": PLAY "P2"
GOTO 10

600 REM ACCESS GRANTED
PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "3": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "2": PLAY "P2"
LOCATE 16, 53: COLOR DC: PRINT "1": PLAY "P2"
COLOR 7: SYSTEM

SUB LetterC (x, y, co)
CIRCLE (x + 20, y + 20), 20, co, 0, pi
CIRCLE (x + 20, y + 20), 15, co, 0, pi
CIRCLE (x + 20, y + 55), 20, co, 1 * pi, 2 * pi
CIRCLE (x + 20, y + 55), 15, co, 1 * pi, 2 * pi
LINE (x, y + 20)-(x, y + 55), co
LINE (x + 5, y + 20)-(x + 5, y + 55), co
LINE (x + 35, y + 20)-(x + 40, y + 20), co
LINE (x + 35, y + 55)-(x + 40, y + 55), co
PAINT (x + 20, y + 2), co, co
END SUB

SUB LetterD (x, y, co)
CIRCLE (x + 20, y + 20), 20, co, 0, .5 * pi
CIRCLE (x + 20, y + 20), 15, co, 0, .5 * pi
CIRCLE (x + 20, y + 55), 20, co, 1.5 * pi, 2 * pi
CIRCLE (x + 20, y + 55), 15, co, 1.5 * pi, 2 * pi
LINE (x, y)-(x, y + 75), co
LINE (x + 5, y + 5)-(x + 5, y + 70), co
LINE (x + 35, y + 20)-(x + 35, y + 55), co
LINE (x + 40, y + 20)-(x + 40, y + 55), co
LINE (x, y)-(x + 20, y), co
LINE (x + 5, y + 5)-(x + 20, y + 5), co
LINE (x + 5, y + 70)-(x + 20, y + 70), co
LINE (x, y + 75)-(x + 20, y + 75), co
PAINT (x + 2, y + 2), co, co
END SUB

SUB LetterE (x, y, co)
LINE (x, y)-(x + 40, y), co
LINE (x + 5, y + 5)-(x + 40, y + 5), co
LINE (x + 5, y + 35)-(x + 40, y + 35), co
LINE (x + 5, y + 40)-(x + 40, y + 40), co
LINE (x + 5, y + 70)-(x + 40, y + 70), co
LINE (x, y + 75)-(x + 40, y + 75), co
LINE (x, y)-(x, y + 75), co
LINE (x + 40, y)-(x + 40, y + 5), co
LINE (x + 5, y + 5)-(x + 5, y + 35), co
LINE (x + 40, y + 35)-(x + 40, y + 40), co
LINE (x + 5, y + 40)-(x + 5, y + 70), co
LINE (x + 40, y + 70)-(x + 40, y + 75), co
PAINT (x + 2, y + 2), co, co
END SUB

SUB LetterK (x, y, co)
LINE (x, y)-(x, y + 75), co
LINE (x + 5, y)-(x + 5, y + 75), co
LINE (x, y)-(x + 5, y), co
LINE (x, y + 75)-(x + 5, y + 75), co
LINE (x + 35, y)-(x + 40, y), co
LINE (x + 35, y + 75)-(x + 40, y + 75), co
LINE (x + 35, y)-(x, y + 37.5), co
LINE (x + 40, y)-(x + 5, y + 37.5), co
LINE (x + 35, y + 75)-(x, y + 37.5), co
LINE (x + 40, y + 75)-(x + 5, y + 37.5), co
PAINT (x + 2, y + 2), co, co
PAINT (x + 2, y + 37.5), co, co
PAINT (x + 2, y + 73), co, co
PAINT (x + 37, y + 2), co, co
PAINT (x + 37, y + 73), co, co
END SUB

SUB LetterL (x, y, co)
LINE (x, y)-(x, y + 75), co
LINE (x + 5, y)-(x + 5, y + 70), co
LINE (x + 5, y + 70)-(x + 40, y + 70), co
LINE (x, y + 75)-(x + 40, y + 75), co
LINE (x, y)-(x + 5, y), co
LINE (x + 40, y + 70)-(x + 40, y + 75), co
PAINT (x + 2, y + 2), co, co
END SUB

SUB LetterM (x, y, co)
LINE (x, y)-(x, y + 75), co
LINE (x + 40, y)-(x + 40, y + 75), co
LINE (x + 5, y + 5)-(x + 5, y + 75), co
LINE (x + 35, y + 5)-(x + 35, y + 75), co
LINE (x, y)-(x + 5, y), co
LINE (x + 35, y)-(x + 40, y), co
LINE (x + 17.5, y + 37.5)-(x + 22.5, y + 37.5), co
LINE (x, y + 75)-(x + 5, y + 75), co
LINE (x + 35, y + 75)-(x + 40, y + 75), co
LINE (x, y)-(x + 17.5, y + 37.5), co
LINE (x + 5, y)-(x + 22.5, y + 37.5), co
LINE (x + 35, y)-(x + 17.5, y + 37.5), co
LINE (x + 40, y)-(x + 22.5, y + 37.5), co
PAINT (x + 2, y + 2), co, co
PAINT (x + 2, y + 10), co, co
PAINT (x + 38, y + 2), co, co
PAINT (x + 38, y + 10), co, co
PAINT (x + 20, y + 36), co, co
END SUB

SUB LetterN (x, y, co)
LINE (x, y)-(x, y + 75), co
LINE (x + 5, y + 5)-(x + 5, y + 75), co
LINE (x, y)-(x + 5, y), co
LINE (x, y + 75)-(x + 5, y + 75), co
LINE (x + 40, y)-(x + 40, y + 75), co
LINE (x + 35, y)-(x + 35, y + 70), co
LINE (x + 35, y)-(x + 40, y), co
LINE (x + 35, y + 75)-(x + 40, y + 75), co
LINE (x, y)-(x + 35, y + 75), co
LINE (x + 5, y)-(x + 40, y + 75), co
PAINT (x + 2, y + 73), co, co
PAINT (x + 20, y + 37.5), co, co
PAINT (x + 38, y + 2), co, co
END SUB

SUB LetterO (x, y, co)
CIRCLE (x + 20, y + 20), 20, co, 0, pi
CIRCLE (x + 20, y + 20), 15, co, 0, pi
CIRCLE (x + 20, y + 55), 20, co, 1 * pi, 2 * pi
CIRCLE (x + 20, y + 55), 15, co, 1 * pi, 2 * pi
LINE (x, y + 20)-(x, y + 55), co
LINE (x + 5, y + 20)-(x + 5, y + 55), co
LINE (x + 35, y + 20)-(x + 35, y + 55), co
LINE (x + 40, y + 20)-(x + 40, y + 55), co
PAINT (x + 20, y + 2), co, co
END SUB

SUB LetterS (x, y, co)
CIRCLE (x + 20, y + 20), 20, co, 0, (1.5 * pi)
CIRCLE (x + 20, y + 55), 20, co, 0, (.5 * pi)
CIRCLE (x + 20, y + 55), 20, co, (1 * pi), (2 * pi)
CIRCLE (x + 20, y + 20), 15, co, 0, (1.5 * pi)
CIRCLE (x + 20, y + 55), 15, co, 0, (.5 * pi)
CIRCLE (x + 20, y + 55), 15, co, (1 * pi), (2 * pi)
LINE (x + 35, y + 20)-(x + 40, y + 20), co
LINE (x, y + 55)-(x + 5, y + 55), co
PAINT (x + 20, y + 37.5), co, co
END SUB

SUB LetterT (x, y, co)
LINE (x, y)-(x, (y + 5)), co
LINE ((x + 40), y)-((x + 40), (y + 5)), co
LINE ((x + 17.5), (y + 75))-((x + 22.5), (y + 75)), co
LINE (x, y)-((x + 40), y), co
LINE (x, y + 5)-(x + 17.5, y + 5), co
LINE (x + 22.5, y + 5)-(x + 40, y + 5), co
LINE (x + 17.5, y + 5)-(x + 17.5, y + 75), co
LINE (x + 22.5, y + 5)-(x + 22.5, y + 75), co
PAINT (x + 2, y + 2), co, co
END SUB

SUB LetterU (x, y, co)
LINE (x, y)-(x, y + 55), co
LINE (x + 5, y)-(x + 5, y + 55), co
LINE (x + 35, y)-(x + 35, y + 55), co
LINE (x + 40, y)-(x + 40, y + 55), co
LINE (x, y)-(x + 5, y), co
LINE (x + 35, y)-(x + 40, y), co
CIRCLE (x + 20, y + 55), 20, co, 1 * pi, 2 * pi
CIRCLE (x + 20, y + 55), 15, co, 1 * pi, 2 * pi
PAINT (x + 2, y + 2), co, co
END SUB

SUB LetterY (x, y, co)
LINE (x, y)-((x + 5), y), co
LINE ((x + 35), y)-((x + 40), y), co
LINE (x, (y + 75))-((x + 5), (y + 75)), co
LINE (x, y)-((x + 17.5), (y + 37.5)), co
LINE ((x + 40), y)-((x + 5), (y + 75)), co
LINE ((x + 35), y)-(x, (y + 75)), co
LINE ((x + 5), y)-((x + 22.5), (y + 37.5)), co
PAINT ((x + 2), (y + 2)), co, co
PAINT ((x + 2), (y + 73)), co, co
PAINT ((x + 38), (y + 2)), co, co
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

