SCREEN 13
CLS
PLAY "P2"
FOR R = 0 TO 63
PALETTE 4, R
LOCATE 12, 17: COLOR 4: PRINT "TETRIS"
NEXT R
PLAY "P1"
FOR R = 63 TO 0 STEP -1
PALETTE 4, R
LOCATE 12, 17: COLOR 4: PRINT "TETRIS"
NEXT R

FOR R = 0 TO 63
PALETTE 4, R
LOCATE 12, 15: COLOR 4: PRINT "WRITTEN BY"
NEXT R
PLAY "P1"
FOR R = 63 TO 0 STEP -1
PALETTE 4, R
LOCATE 12, 15: COLOR 4: PRINT "WRITTEN BY"
NEXT R

FOR R = 0 TO 63
PALETTE 4, R
LOCATE 12, 3: COLOR 4: PRINT "ERIK VAN ZIJST & ALEXANDER VAN LOO"
NEXT R
PLAY "P1"
FOR R = 63 TO 0 STEP -1
PALETTE 4, R
LOCATE 12, 3: COLOR 4: PRINT "ERIK VAN ZIJST & ALEXANDER VAN LOO"
NEXT R
END

