REM ***   DIGICLOCK VERSION 1.0�                                          ***

REM ***   COPYRIGHT 1995 BY A.P.J. VAN LOO                                ***
REM ***   MODIFICATION OF THE PROGRAM (OTHER THAN CHANGING ACCESSCODES)   ***
REM ***   IS NOT ALLOWED!!                                                ***

REM ***   FOR COMMENTS ON THIS PROGRAM WRITE TO:                          ***
REM ***   A.P.J. VAN LOO                                                  ***
REM ***   BAETENBURG 23                                                   ***
REM ***   1852 TP  HEILOO                                                 ***
REM ***   THE NETHERLANDS                                                 ***
REM ***   E-MAIL: LOO@STUDENT.TN.TUDELFT.NL                               ***

SCREEN 12
CLS
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
REM LOCATE 25, 20: COLOR 8: PRINT "PRESS X TO EXIT AND + OR - TO CHANGE COLOR"

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

KEY$ = INKEY$
IF KEY$ = "c" THEN C1 = C1 + 1
IF KEY$ = "C" THEN C1 = C1 + 1
IF KEY$ = "X" THEN SYSTEM
IF KEY$ = "x" THEN SYSTEM
IF C1 > 15 THEN C1 = 1
REM IF C1 < 1 THEN C1 = 15
IF C1 = C AND KEY$ = "c" THEN C1 = C + 1
IF C1 = C AND KEY$ = "C" THEN C1 = C + 1
LOOP

