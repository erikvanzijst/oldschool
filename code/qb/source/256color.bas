REM - 256 kleuren Demo-programma. -
SCREEN 13
WINDOW (0, 0)-(100, 100)
FOR I = 0 TO 255
LINE (0, 0)-(100, 100), 15, BF
LINE (50, 50)-(60, 60), I, BF
LOCATE 1, 1
PRINT I
FOR C = 1 TO 10000: NEXT C
NEXT I
END
    

