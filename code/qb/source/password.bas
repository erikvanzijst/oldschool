SCREEN 13
REM ** Wachtwoord programma.
FOR I = 1 TO 3
LOCATE 1, 3
INPUT "ENTER PASSWORD FOR COMPUTER ACCES: "; A$
IF A$ = "KILL JP" THEN END
LOCATE 12, 10
IF I = 3 THEN GOTO 2
1 PRINT "IVALID PASSWORD, TYPE AGAIN."
GOTO 4
2 COLOR 65
BEEP
3 PRINT "         ! ACCES  DENIED !"
4 NEXT I
10 FOR I = 60 TO 5000 STEP 10
SOUND I, .1
NEXT I
GOTO 10







