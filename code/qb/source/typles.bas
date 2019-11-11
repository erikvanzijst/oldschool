DECLARE SUB WERKSCHERM ()
DECLARE SUB SCHERMWIT ()

REM ** TYPLES PROGRAMMA DOOR ERIK VAN ZIJST **
CLS

REM LE$ = 1
REM ONDERDEEL$ = 1

SCHERMWIT
WERKSCHERM

FOR H = 1 TO 10
IF H = 1 THEN GOSUB LES1: LOCATE 3, 7: COLOR 0, 11: PRINT "1"
IF H = 2 THEN GOSUB LES2: LOCATE 3, 7: COLOR 0, 11: PRINT "2"
REM IF H = 3 THEN GOSUB LES3: LOCATE 3, 7: COLOR 0, 11: PRINT "3"
REM IF H = 4 THEN GOSUB LES4: LOCATE 3, 7: COLOR 0, 11: PRINT "4"
REM IF H = 5 THEN GOSUB LES5: LOCATE 3, 7: COLOR 0, 11: PRINT "5"
REM IF H = 6 THEN GOSUB LES6: LOCATE 3, 7: COLOR 0, 11: PRINT "6"
REM IF H = 7 THEN GOSUB LES7: LOCATE 3, 7: COLOR 0, 11: PRINT "7"
REM IF H = 8 THEN GOSUB LES8: LOCATE 3, 7: COLOR 0, 11: PRINT "8"
REM IF H = 9 THEN GOSUB LES9: LOCATE 3, 7: COLOR 0, 11: PRINT "9"
REM IF H = 10 THEN GOSUB LES10: LOCATE 3, 7: COLOR 0, 11: PRINT "10"

FOR I = 1 TO 2
        LOCATE 7, 21: COLOR 14, 1
        PRINT "                                       "
        LOCATE 7, 21: COLOR 14, 1
        PRINT ONDERDEEL1$            'Print de opdracht

        LOCATE 14, 15: COLOR 0, 11
        PRINT "                                                          "
        LOCATE 14, 15: COLOR 0, 11
        PRINT TOELICHTING1$          'Geef een toelichting

NEXT I

DO
LOOP WHILE INKEY$ = ""

FOR J = 1 TO 2
        LOCATE 7, 21: COLOR 14, 1
        PRINT "                                       "
        LOCATE 7, 21: COLOR 14, 1
        PRINT ONDERDEEL2$            'Print de opdracht

        LOCATE 14, 15: COLOR 0, 11
        PRINT "                                                          "
        LOCATE 14, 15: COLOR 0, 11
        PRINT TOELICHTING2$          'Geef een toelichting

NEXT J

DO
LOOP WHILE INKEY$ = ""
NEXT H


LES1:
ONDERDEEL1$ = "qwert qwert qwert qwert qwert"
ONDERDEEL2$ = "poiuy poiuy poiuy poiuy poiuy"
ONDERDEEL3$ = "qwert poiuy qwert poiuy qwert"
TOELICHTING1$ = "Voor de linkerhand. Spatie om te beginnen."
TOELICHTING2$ = "Nu de rechterhand. Spatie om te beginnen."
TOELICHTING3$ = "Nu beide handen tegelijk! Laatste onderdeel."
RETURN

LES2:
ONDERDEEL1$ = "as aas eer eed was waas rad vet dwaas"
ONDERDEEL2$ = "dag ras das gas gaas vaas weer teer"
ONDERDEEL3$ = "dwerg cassa cadet xeres gevaar abces"
TOELICHTING1$ = "Woordjes met links. Spatie om te beginnen."
TOELICHTING2$ = "Kijk niet naar het toetsenbord! Leer hun plaatsen."
TOELICHTING3$ = "Nu wat langere woorden. Doe het langzaam maar foutloos!"
RETURN

SUB SCHERMWIT
COLOR 0, 15
CLS
END SUB

SUB WERKSCHERM
LOCATE 2, 37
COLOR 0, 15
PRINT "TYPLES"

COLOR 11, 11
FOR I = 1 TO 80
        LOCATE 3, I
        PRINT " "
NEXT I
LOCATE 3, 1
COLOR 0, 11
PRINT "ณLES:       ณONDERDEEL:                                            ณVERSIE: 1.0"

COLOR 14, 1
LOCATE 6, 20
PRINT "ษอออออออออออออออออออออออออออออออออออออออป"
LOCATE 7, 20
PRINT "บ                                       บ"
LOCATE 8, 20
PRINT "ศอออออออออออออออออออออออออออออออออออออออผ"
LOCATE 7, 10
COLOR 0, 15
PRINT "Opdracht:"

COLOR 14, 4
LOCATE 10, 20
PRINT "ษอออออออออออออออออออออออออออออออออออออออป"
LOCATE 11, 20
PRINT "บ                                       บ"
LOCATE 12, 20
PRINT "ศอออออออออออออออออออออออออออออออออออออออผ"
LOCATE 11, 10
COLOR 0, 15
PRINT "Typ hier:"

COLOR 11, 11
FOR I = 1 TO 80
        LOCATE 14, I
        PRINT " "
NEXT I
LOCATE 14, 1
COLOR 0, 11
PRINT "ณToelichting: "

END SUB

