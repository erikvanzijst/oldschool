10 SCREEN 12
WINDOW (0, -50)-(260, 150)
LOCATE 25, 2
PRINT "Bekijk deze twee roterende kubussen stereoscopisch en je zult een 3-dimensio-"
PRINT "nale, rechtsom-draaiende kubus waarnemen."
PRINT "Gemaakt door Erik van Zijst op 24-7-'95."
PRINT "Druk op 'Ctrl-Break' om het programma te beeindigen."
REM *** Draadfiguur kubus die om z'n as draait. ***
FOR I = 1 TO 100
A = (50 + (SIN(I / 15.9) * 25))
B = (50 - (COS(I / 15.9) * 25))
C = (50 - (SIN(I / 15.9) * 25))
D = (50 + (COS(I / 15.9) * 25))
E = (120 + (SIN((I + 2) / 15.9) * 25))
F = (120 - (COS((I + 2) / 15.9) * 25))
G = (120 - (SIN((I + 2) / 15.9) * 25))
H = (120 + (COS((I + 2) / 15.9) * 25))
REM ** Teken onderste vlak van de kubus. **
LINE (A, 30)-(B, 20), 3
LINE (E, 30)-(F, 20), 3
LINE (B, 20)-(C, 20), 3
LINE (F, 20)-(G, 20), 3
LINE (C, 20)-(D, 30), 3
LINE (G, 20)-(H, 30), 3
LINE (D, 30)-(A, 30), 3
LINE (H, 30)-(E, 30), 3
REM ** Teken bovenste vlak van de kubus. **
LINE (A, 80)-(B, 70), 3
LINE (E, 80)-(F, 70), 3
LINE (B, 70)-(C, 70), 3
LINE (F, 70)-(G, 70), 3
LINE (C, 70)-(D, 80), 3
LINE (G, 70)-(H, 80), 3
LINE (D, 80)-(A, 80), 3
LINE (H, 80)-(E, 80), 3
REM ** Teken de verbindingslijnen van het bovenste met het onderste vlak. **
LINE (A, 80)-(A, 30), 3
LINE (E, 80)-(E, 30), 3
LINE (B, 70)-(B, 20), 3
LINE (F, 70)-(F, 20), 3
LINE (D, 80)-(D, 30), 3
LINE (H, 80)-(H, 30), 3
LINE (C, 20)-(C, 70), 3
LINE (G, 20)-(G, 70), 3
FOR C = 1 TO 500: NEXT C
LINE (0, 0)-(260, 150), 0, BF
NEXT I
GOTO 10











