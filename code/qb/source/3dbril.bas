SCREEN 13
WINDOW (0, 0)-(100, 100)
REM *** M.A. de Ruyterstraat 5, 4695 EL Sint-Maartensdijk, 01666-3745.
REM *** Draadfiguur kubus die om z'n as draait. Bekijk de figuur met een
REM *** zgn. 3D-bril, rood en groen glas. Door Erik van Zijst.
REM *** Draag de bril met het rode glas links en het groene rechts.
LOCATE 25, 1
PRINT "Gemaakt door Erik van Zijst 25-7-95."
PRINT "Druk op "; CHR$(34); "ESC"; CHR$(34); " om te stoppen."
DO
FOR I = 1 TO 100
        A = (45 + (SIN(I / 15.9) * 25))
        B = (45 - (COS(I / 15.9) * 25))
        C = (45 - (SIN(I / 15.9) * 25))
        D = (45 + (COS(I / 15.9) * 25))
        E = (50 + (SIN((I + 2) / 15.9) * 25))
        F = (50 - (COS((I + 2) / 15.9) * 25))
        G = (50 - (SIN((I + 2) / 15.9) * 25))
        H = (50 + (COS((I + 2) / 15.9) * 25))
REM ** Teken onderste vlak van de kubus. **
        LINE (A, 35)-(B, 25), 91
        LINE (E, 35)-(F, 25), 100
        LINE (B, 25)-(C, 20), 91
        LINE (F, 25)-(G, 20), 100
        LINE (C, 20)-(D, 30), 91
        LINE (G, 20)-(H, 30), 100
        LINE (D, 30)-(A, 35), 91
        LINE (H, 30)-(E, 35), 100
REM ** Teken bovenste vlak van de kubus. **
        LINE (A, 85)-(B, 75), 91
        LINE (E, 85)-(F, 75), 100
        LINE (B, 75)-(C, 70), 91
        LINE (F, 75)-(G, 70), 100
        LINE (C, 70)-(D, 80), 91
        LINE (G, 70)-(H, 80), 100
        LINE (D, 80)-(A, 85), 91
        LINE (H, 80)-(E, 85), 100
REM ** Teken de verbindingslijnen van het bovenste met het onderste vlak. **
        LINE (A, 85)-(A, 35), 91
        LINE (E, 85)-(E, 35), 100
        LINE (B, 75)-(B, 25), 91
        LINE (F, 75)-(F, 25), 100
        LINE (D, 80)-(D, 30), 91
        LINE (H, 80)-(H, 30), 100
        LINE (C, 20)-(C, 70), 91
        LINE (G, 20)-(G, 70), 100
                FOR C = 1 TO 500: NEXT C
        LINE (0, 16)-(100, 100), 15, BF
        IF INKEY$ = CHR$(27) THEN END
NEXT I
LOOP











