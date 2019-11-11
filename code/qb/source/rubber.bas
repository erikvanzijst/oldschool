REM RUBBER.BAS IN QBASIC DOOR JURGEN KOBIERCZYNSKI DIEPENBEEK BELGIE
CLS : INPUT "Schermmodus:"; SCHERM
INPUT "Geluid aan:"; A$
IF A$ = "J" OR A$ = "j" THEN N = 1
REM VOORINSTELLINGEN
        KEY 15, CHR$(32) + CHR$(28)
        ON KEY(15) GOSUB EINDE
        KEY(15) ON
        CONST PI = 3.141592653#
               G = -9.81
              dt = .005
        DEF FNA (X) = SIN(X * 3) + .1 * X ^ 2
               X = 1
               Y = 3
               VX = .0001
               VY = .0001
REM SCHERMOPSTELLING
        SCREEN SCHERM
        WINDOW (-5, 5)-(5, -5)
        LINE (-5, 0)-(5, 0), 1
        LINE (0, 5)-(0, -5), 1
        FOR I = -5 TO 5 STEP .05
        :       JJ = J
        :       J = FNA(I)
        :       IF IND = 1 THEN LINE (I, J)-(II, JJ), 2
        :       II = I
        :       IND = 1
        :
        NEXT I
REM SIMULATIE
        WINDOW (-5, 5)-(5, -5)
        WHILE UIT = 0
        :       VY = VY + G * dt
        :       XX = X
        :       X = X + VX * dt
        :       YY = Y
        :       Y = Y + VY * dt
        :       H = Y - FNA(X)
        :       LINE (X, Y)-(XX, YY), 4
        :       V = SQR(VX ^ 2 + VY ^ 2)
        :       SOUND (400 + V ^ 3), .4 * N
        :       IF H < 0 THEN GOSUB WEERKAATS
        WEND
END
EINDE:
        UIT = 1
        RETURN
WEERKAATS:
        SOUND 1000, .1
        A = PI / 2 - ATN(.01 / (FNA(X) - FNA(X + .01)))
        B = -PI / 2 - ATN(VY / VX)
        IF VX < 0 THEN B = B + PI
        C = PI / 2 - 2 * A + B
        VX = V * COS(C)
        VY = V * SIN(C)
        RETURN

