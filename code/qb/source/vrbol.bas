SCREEN 13
WINDOW (0, 0)-(100, 100)
REM ** Draaiende, 3 dimensionale bol.
FOR I = 1 TO 100
R1 = (ABS(SIN(I / 15.9)))
IF R1 = 0 THEN R1 = .01
R2 = (ABS(COS(I / 15.9)))
IF R2 = 0 THEN R2 = .01
S1 = (ABS(SIN((I + 2) / 15.9)))
IF S1 = 0 THEN S1 = .01
S2 = (ABS(COS((I + 2) / 15.9)))
IF S2 = 0 THEN S2 = .01
CIRCLE (45, 50), (R1 * 25), 91, 0, 6.28, (1 / R1)
CIRCLE (45, 50), (R2 * 25), 91, 0, 6.28, (1 / R2)
FOR C = 1 TO 2000: NEXT C
CLS
NEXT I






