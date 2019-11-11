CLS
LOCATE 5, 35: COLOR 3: PRINT "TETRIS SVGA"
LOCATE 15, 6: COLOR 7: PRINT "Choose the size of the Tetris field:"
LOCATE 17, 4: COLOR 15: PRINT "1. Normal size: 10x18. (Default)"
LOCATE 18, 4: COLOR 15: PRINT "2. Wide: 13x18."
LOCATE 20, 6: COLOR 7: PRINT "Press 1 or 2. <Enter> takes default."
LOCATE 21, 6: COLOR 7: PRINT "Esc to quit"
LOCATE 20, 20: COLOR 15: PRINT "<Enter>"
DO
  KEY$ = INKEY$
  IF KEY$ = CHR$(13) THEN SHELL "tetris.exe": END
  IF KEY$ = CHR$(49) THEN SHELL "tetris.exe": END
  IF KEY$ = CHR$(50) THEN SHELL "tetrisw.exe": END
  IF KEY$ = CHR$(27) THEN CLS : END
LOOP
END

