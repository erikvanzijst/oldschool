10 SCREEN 9
WINDOW (0, -50)-(200, 150)
REM *** Draadfiguur kubus die om z'n as draait. ***
FOR I = 1 TO 1000
A = (50 + (SIN(I / 15.9) * 25))
B = (50 - (COS(I / 15.9) * 25))
C = (50 - (SIN(I / 15.9) * 25))
D = (50 + (COS(I / 15.9) * 25))
REM ** Teken onderste vlak van de kubus. **
LINE (AE, 30)-(BE, 20), 0, BF        'Verwijder de oude lijn.
LINE (A, 30)-(B, 20), 3
LINE (BE, 20)-(CE, 20), 0, BF          'Verwijder de oude lijn.
LINE (B, 20)-(C, 20), 3
LINE (CE, 20)-(DE, 30), 0, BF          'Verwijder de oude lijn.
LINE (C, 20)-(D, 30), 3
LINE (DE, 30)-(AE, 30), 0, BF          'Verwijder de oude lijn.
LINE (D, 30)-(A, 30), 3
REM ** Teken bovenste vlak van de kubus. **
LINE (AE, 80)-(BE, 70), 0, BF
LINE (A, 80)-(B, 70), 3
LINE (BE, 70)-(CE, 70), 0, BF
LINE (B, 70)-(C, 70), 3
LINE (CE, 70)-(DA, 80), 0, BF
LINE (C, 70)-(D, 80), 3
LINE (DE, 80)-(AE, 80), 0, BF
LINE (D, 80)-(A, 80), 3
REM ** Teken de verbindingslijnen van het bovenste met het onderste vlak. **
LINE (AE, 80)-(AE, 30), 0, BF
LINE (A, 80)-(A, 30), 3
LINE (BE, 70)-(BE, 20), 0, BF
LINE (B, 70)-(B, 20), 3
LINE (DE, 80)-(DE, 30), 0, BF
LINE (D, 80)-(D, 30), 3
LINE (CE, 20)-(CE, 70), 0, BF
LINE (C, 20)-(C, 70), 3
FOR C = 1 TO 500: NEXT C
A = AE
B = BE
C = CE
D = DE
NEXT I
GOTO 10











