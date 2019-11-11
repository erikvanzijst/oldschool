REM ** WEKKER 30-DECEMBER-1995, 01:07H **
CLS
PRINT "Dit programma is actief sinds "; TIME$; "h."
10 IF TIME$ = "09:30:00" THEN GOTO 20
GOTO 10
20 BEEP
LOCATE 15, 20
PRINT "Het is "; TIME$; "h, tijd om eruit te komen dus!"
FOR I = 1 TO 20000: NEXT I
KEY$ = INKEY$
IF KEY$ <> "" THEN SLEEP
BEEP
GOTO 20
END


