10 CLS
ph$ = "00": pm$ = "00": as$ = "OFF"
GOTO 5000


11 a$ = DATE$

m1$ = LEFT$(a$, 1)
m2$ = MID$(a$, 2, 1)
mm$ = LEFT$(a$, 2)

d1$ = MID$(a$, 4, 1)
d2$ = MID$(a$, 5, 1)
dd$ = MID$(a$, 4, 2)

y1$ = MID$(a$, 7, 1)
y2$ = MID$(a$, 8, 1)
y3$ = MID$(a$, 9, 1)
y4$ = RIGHT$(a$, 1)
yyyy$ = RIGHT$(a$, 4)

IF mm$ = "01" THEN month$ = "January": py = 33
IF mm$ = "02" THEN month$ = "February": py = 32
IF mm$ = "03" THEN month$ = "March": py = 34
IF mm$ = "04" THEN month$ = "April": py = 34
IF mm$ = "05" THEN month$ = "May": py = 35
IF mm$ = "06" THEN month$ = "June": py = 34
IF mm$ = "07" THEN month$ = "July": py = 34
IF mm$ = "08" THEN month$ = "August": py = 33
IF mm$ = "09" THEN month$ = "September": py = 32
IF mm$ = "10" THEN month$ = "October": py = 33
IF mm$ = "11" THEN month$ = "November": py = 32
IF mm$ = "12" THEN month$ = "December": py = 32

RETURN


12 b$ = TIME$

h1$ = LEFT$(b$, 1)
h2$ = MID$(b$, 2, 1)
hh$ = LEFT$(b$, 2)
IF hh$ = "01" THEN hha = 1
IF hh$ = "02" THEN hha = 2
IF hh$ = "03" THEN hha = 3
IF hh$ = "04" THEN hha = 4
IF hh$ = "05" THEN hha = 5
IF hh$ = "06" THEN hha = 6
IF hh$ = "07" THEN hha = 7
IF hh$ = "08" THEN hha = 8
IF hh$ = "09" THEN hha = 9
IF hh$ = "10" THEN hha = 10
IF hh$ = "11" THEN hha = 11
IF hh$ = "12" THEN hha = 12
IF hh$ = "13" THEN hha = 13
IF hh$ = "14" THEN hha = 14
IF hh$ = "15" THEN hha = 15
IF hh$ = "16" THEN hha = 16
IF hh$ = "17" THEN hha = 17
IF hh$ = "18" THEN hha = 18
IF hh$ = "19" THEN hha = 19
IF hh$ = "20" THEN hha = 20
IF hh$ = "21" THEN hha = 21
IF hh$ = "22" THEN hha = 22
IF hh$ = "23" THEN hha = 23
IF hh$ = "24" THEN hha = 24
IF hh$ = "00" THEN hha = 24


min1$ = MID$(b$, 4, 1)
min2$ = MID$(b$, 5, 1)
min$ = MID$(b$, 4, 2)
IF min$ = "01" THEN mina = 1
IF min$ = "02" THEN mina = 2
IF min$ = "03" THEN mina = 3
IF min$ = "04" THEN mina = 4
IF min$ = "05" THEN mina = 5
IF min$ = "06" THEN mina = 6
IF min$ = "07" THEN mina = 7
IF min$ = "08" THEN mina = 8
IF min$ = "09" THEN mina = 9
IF min$ = "10" THEN mina = 10
IF min$ = "11" THEN mina = 11
IF min$ = "12" THEN mina = 12
IF min$ = "13" THEN mina = 13
IF min$ = "14" THEN mina = 14
IF min$ = "15" THEN mina = 15
IF min$ = "16" THEN mina = 16
IF min$ = "17" THEN mina = 17
IF min$ = "18" THEN mina = 18
IF min$ = "19" THEN mina = 19
IF min$ = "20" THEN mina = 20
IF min$ = "21" THEN mina = 21
IF min$ = "22" THEN mina = 22
IF min$ = "23" THEN mina = 23
IF min$ = "24" THEN mina = 24
IF min$ = "25" THEN mina = 25
IF min$ = "26" THEN mina = 26
IF min$ = "27" THEN mina = 27
IF min$ = "28" THEN mina = 28
IF min$ = "29" THEN mina = 29
IF min$ = "30" THEN mina = 30
IF min$ = "31" THEN mina = 31
IF min$ = "32" THEN mina = 32
IF min$ = "33" THEN mina = 33
IF min$ = "34" THEN mina = 34
IF min$ = "35" THEN mina = 35
IF min$ = "36" THEN mina = 36
IF min$ = "37" THEN mina = 37
IF min$ = "38" THEN mina = 38
IF min$ = "39" THEN mina = 39
IF min$ = "40" THEN mina = 40
IF min$ = "41" THEN mina = 41
IF min$ = "42" THEN mina = 42
IF min$ = "43" THEN mina = 43
IF min$ = "44" THEN mina = 44
IF min$ = "45" THEN mina = 45
IF min$ = "46" THEN mina = 46
IF min$ = "47" THEN mina = 47
IF min$ = "48" THEN mina = 48
IF min$ = "49" THEN mina = 49
IF min$ = "50" THEN mina = 50
IF min$ = "51" THEN mina = 51
IF min$ = "52" THEN mina = 52
IF min$ = "53" THEN mina = 53
IF min$ = "54" THEN mina = 54
IF min$ = "55" THEN mina = 55
IF min$ = "56" THEN mina = 56
IF min$ = "57" THEN mina = 57
IF min$ = "58" THEN mina = 58
IF min$ = "59" THEN mina = 59
IF min$ = "60" THEN mina = 60
IF min$ = "00" THEN mina = 60

s1$ = MID$(b$, 7, 1)
s2$ = RIGHT$(b$, 1)
ss$ = RIGHT$(b$, 2)
IF ss$ = "01" THEN ssa = 1
IF ss$ = "02" THEN ssa = 2
IF ss$ = "03" THEN ssa = 3
IF ss$ = "04" THEN ssa = 4
IF ss$ = "05" THEN ssa = 5
IF ss$ = "06" THEN ssa = 6
IF ss$ = "07" THEN ssa = 7
IF ss$ = "08" THEN ssa = 8
IF ss$ = "09" THEN ssa = 9
IF ss$ = "10" THEN ssa = 10
IF ss$ = "11" THEN ssa = 11
IF ss$ = "12" THEN ssa = 12
IF ss$ = "13" THEN ssa = 13
IF ss$ = "14" THEN ssa = 14
IF ss$ = "15" THEN ssa = 15
IF ss$ = "16" THEN ssa = 16
IF ss$ = "17" THEN ssa = 17
IF ss$ = "18" THEN ssa = 18
IF ss$ = "19" THEN ssa = 19
IF ss$ = "20" THEN ssa = 20
IF ss$ = "21" THEN ssa = 21
IF ss$ = "22" THEN ssa = 22
IF ss$ = "23" THEN ssa = 23
IF ss$ = "24" THEN ssa = 24
IF ss$ = "25" THEN ssa = 25
IF ss$ = "26" THEN ssa = 26
IF ss$ = "27" THEN ssa = 27
IF ss$ = "28" THEN ssa = 28
IF ss$ = "29" THEN ssa = 29
IF ss$ = "30" THEN ssa = 30
IF ss$ = "31" THEN ssa = 31
IF ss$ = "32" THEN ssa = 32
IF ss$ = "33" THEN ssa = 33
IF ss$ = "34" THEN ssa = 34
IF ss$ = "35" THEN ssa = 35
IF ss$ = "36" THEN ssa = 36
IF ss$ = "37" THEN ssa = 37
IF ss$ = "38" THEN ssa = 38
IF ss$ = "39" THEN ssa = 39
IF ss$ = "40" THEN ssa = 40
IF ss$ = "41" THEN ssa = 41
IF ss$ = "42" THEN ssa = 42
IF ss$ = "43" THEN ssa = 43
IF ss$ = "44" THEN ssa = 44
IF ss$ = "45" THEN ssa = 45
IF ss$ = "46" THEN ssa = 46
IF ss$ = "47" THEN ssa = 47
IF ss$ = "48" THEN ssa = 48
IF ss$ = "49" THEN ssa = 49
IF ss$ = "50" THEN ssa = 50
IF ss$ = "51" THEN ssa = 51
IF ss$ = "52" THEN ssa = 52
IF ss$ = "53" THEN ssa = 53
IF ss$ = "54" THEN ssa = 54
IF ss$ = "55" THEN ssa = 55
IF ss$ = "56" THEN ssa = 56
IF ss$ = "57" THEN ssa = 57
IF ss$ = "58" THEN ssa = 58
IF ss$ = "59" THEN ssa = 59
IF ss$ = "60" THEN ssa = 60
IF ss$ = "00" THEN ssa = 60
    
RETURN

13 IF ph$ = "01" THEN pha = 1
IF ph$ = "02" THEN pha = 2
IF ph$ = "03" THEN pha = 3
IF ph$ = "04" THEN pha = 4
IF ph$ = "05" THEN pha = 5
IF ph$ = "06" THEN pha = 6
IF ph$ = "07" THEN pha = 7
IF ph$ = "08" THEN pha = 8
IF ph$ = "09" THEN pha = 9
IF ph$ = "10" THEN pha = 10
IF ph$ = "11" THEN pha = 11
IF ph$ = "12" THEN pha = 12
IF ph$ = "13" THEN pha = 13
IF ph$ = "14" THEN pha = 14
IF ph$ = "15" THEN pha = 15
IF ph$ = "16" THEN pha = 16
IF ph$ = "17" THEN pha = 17
IF ph$ = "18" THEN pha = 18
IF ph$ = "19" THEN pha = 19
IF ph$ = "20" THEN pha = 20
IF ph$ = "21" THEN pha = 21
IF ph$ = "22" THEN pha = 22
IF ph$ = "23" THEN pha = 23
IF ph$ = "24" THEN pha = 24
IF ph$ = "00" THEN pha = 24

IF pm$ = "01" THEN pma = 1
IF pm$ = "02" THEN pma = 2
IF pm$ = "03" THEN pma = 3
IF pm$ = "04" THEN pma = 4
IF pm$ = "05" THEN pma = 5
IF pm$ = "06" THEN pma = 6
IF pm$ = "07" THEN pma = 7
IF pm$ = "08" THEN pma = 8
IF pm$ = "09" THEN pma = 9
IF pm$ = "10" THEN pma = 10
IF pm$ = "11" THEN pma = 11
IF pm$ = "12" THEN pma = 12
IF pm$ = "13" THEN pma = 13
IF pm$ = "14" THEN pma = 14
IF pm$ = "15" THEN pma = 15
IF pm$ = "16" THEN pma = 16
IF pm$ = "17" THEN pma = 17
IF pm$ = "18" THEN pma = 18
IF pm$ = "19" THEN pma = 19
IF pm$ = "20" THEN pma = 20
IF pm$ = "21" THEN pma = 21
IF pm$ = "22" THEN pma = 22
IF pm$ = "23" THEN pma = 23
IF pm$ = "24" THEN pma = 24
IF pm$ = "25" THEN pma = 25
IF pm$ = "26" THEN pma = 26
IF pm$ = "27" THEN pma = 27
IF pm$ = "28" THEN pma = 28
IF pm$ = "29" THEN pma = 29
IF pm$ = "30" THEN pma = 30
IF pm$ = "31" THEN pma = 31
IF pm$ = "32" THEN pma = 32
IF pm$ = "33" THEN pma = 33
IF pm$ = "34" THEN pma = 34
IF pm$ = "35" THEN pma = 35
IF pm$ = "36" THEN pma = 36
IF pm$ = "37" THEN pma = 37
IF pm$ = "38" THEN pma = 38
IF pm$ = "39" THEN pma = 39
IF pm$ = "40" THEN pma = 40
IF pm$ = "41" THEN pma = 41
IF pm$ = "42" THEN pma = 42
IF pm$ = "43" THEN pma = 43
IF pm$ = "44" THEN pma = 44
IF pm$ = "45" THEN pma = 45
IF pm$ = "46" THEN pma = 46
IF pm$ = "47" THEN pma = 47
IF pm$ = "48" THEN pma = 48
IF pm$ = "49" THEN pma = 49
IF pm$ = "50" THEN pma = 50
IF pm$ = "51" THEN pma = 51
IF pm$ = "52" THEN pma = 52
IF pm$ = "53" THEN pma = 53
IF pm$ = "54" THEN pma = 54
IF pm$ = "55" THEN pma = 55
IF pm$ = "56" THEN pma = 56
IF pm$ = "57" THEN pma = 57
IF pm$ = "58" THEN pma = 58
IF pm$ = "59" THEN pma = 59
IF pm$ = "60" THEN pma = 60
IF pm$ = "00" THEN pma = 60

IF ps$ = "01" THEN psa = 1
IF ps$ = "02" THEN psa = 2
IF ps$ = "03" THEN psa = 3
IF ps$ = "04" THEN psa = 4
IF ps$ = "05" THEN psa = 5
IF ps$ = "06" THEN psa = 6
IF ps$ = "07" THEN psa = 7
IF ps$ = "08" THEN psa = 8
IF ps$ = "09" THEN psa = 9
IF ps$ = "10" THEN psa = 10
IF ps$ = "11" THEN psa = 11
IF ps$ = "12" THEN psa = 12
IF ps$ = "13" THEN psa = 13
IF ps$ = "14" THEN psa = 14
IF ps$ = "15" THEN psa = 15
IF ps$ = "16" THEN psa = 16
IF ps$ = "17" THEN psa = 17
IF ps$ = "18" THEN psa = 18
IF ps$ = "19" THEN psa = 19
IF ps$ = "20" THEN psa = 20
IF ps$ = "21" THEN psa = 21
IF ps$ = "22" THEN psa = 22
IF ps$ = "23" THEN psa = 23
IF ps$ = "24" THEN psa = 24
IF ps$ = "25" THEN psa = 25
IF ps$ = "26" THEN psa = 26
IF ps$ = "27" THEN psa = 27
IF ps$ = "28" THEN psa = 28
IF ps$ = "29" THEN psa = 29
IF ps$ = "30" THEN psa = 30
IF ps$ = "31" THEN psa = 31
IF ps$ = "32" THEN psa = 32
IF ps$ = "33" THEN psa = 33
IF ps$ = "34" THEN psa = 34
IF ps$ = "35" THEN psa = 35
IF ps$ = "36" THEN psa = 36
IF ps$ = "37" THEN psa = 37
IF ps$ = "38" THEN psa = 38
IF ps$ = "39" THEN psa = 39
IF ps$ = "40" THEN psa = 40
IF ps$ = "41" THEN psa = 41
IF ps$ = "42" THEN psa = 42
IF ps$ = "43" THEN psa = 43
IF ps$ = "44" THEN psa = 44
IF ps$ = "45" THEN psa = 45
IF ps$ = "46" THEN psa = 46
IF ps$ = "47" THEN psa = 47
IF ps$ = "48" THEN psa = 48
IF ps$ = "49" THEN psa = 49
IF ps$ = "50" THEN psa = 50
IF ps$ = "51" THEN psa = 51
IF ps$ = "52" THEN psa = 52
IF ps$ = "53" THEN psa = 53
IF ps$ = "54" THEN psa = 54
IF ps$ = "55" THEN psa = 55
IF ps$ = "56" THEN psa = 56
IF ps$ = "57" THEN psa = 57
IF ps$ = "58" THEN psa = 58
IF ps$ = "59" THEN psa = 59
IF ps$ = "60" THEN psa = 60
IF ps$ = "00" THEN psa = 60
RETURN


100 LOCATE 5, g: PRINT " ‹€€€€    "
110 LOCATE 6, g: PRINT "€€ﬂ €€    "
120 LOCATE 7, g: PRINT "    €€    "
130 LOCATE 8, g: PRINT "    €€    "
140 LOCATE 9, g: PRINT "    €€    "
150 LOCATE 10, g: PRINT "    €€    "
160 LOCATE 11, g: PRINT "€€€€€€€€€€"
RETURN

200 LOCATE 5, g: PRINT " ‹€€€€€€‹ "
210 LOCATE 6, g: PRINT "€€ﬂ   ﬂ€€€"
220 LOCATE 7, g: PRINT "      ‹€€ﬂ"
230 LOCATE 8, g: PRINT "    ‹€€ﬂ  "
240 LOCATE 9, g: PRINT "  ‹€€ﬂ    "
250 LOCATE 10, g: PRINT "‹€€€      "
260 LOCATE 11, g: PRINT "€€€€€€€€€€"
RETURN

300 LOCATE 5, g: PRINT " ‹€€€€€€‹ "
310 LOCATE 6, g: PRINT "€€ﬂ    ﬂ€€"
320 LOCATE 7, g: PRINT "       ‹€€"
330 LOCATE 8, g: PRINT "    €€€€€ "
340 LOCATE 9, g: PRINT "       ﬂ€€"
350 LOCATE 10, g: PRINT "€€‹    ‹€€"
360 LOCATE 11, g: PRINT " ﬂ€€€€€€ﬂ "
RETURN

400 LOCATE 5, g: PRINT "   €€€€€  "
410 LOCATE 6, g: PRINT "  €€  €€  "
420 LOCATE 7, g: PRINT " €€   €€  "
430 LOCATE 8, g: PRINT "€€    €€  "
440 LOCATE 9, g: PRINT "€€€€€€€€€€"
450 LOCATE 10, g: PRINT "      €€  "
460 LOCATE 11, g: PRINT "      €€  "
RETURN

500 LOCATE 5, g: PRINT "€€€€€€€€€€"
510 LOCATE 6, g: PRINT "€€        "
520 LOCATE 7, g: PRINT "€€‹‹‹‹‹‹  "
530 LOCATE 8, g: PRINT "ﬂﬂﬂﬂﬂﬂﬂ€€‹"
540 LOCATE 9, g: PRINT "        €€"
550 LOCATE 10, g: PRINT "€€‹    ‹€€"
560 LOCATE 11, g: PRINT " ﬂ€€€€€€ﬂ "
RETURN

600 LOCATE 5, g: PRINT "       ‹€€"
610 LOCATE 6, g: PRINT "     ‹€€ﬂ "
620 LOCATE 7, g: PRINT "   ‹€€ﬂ   "
630 LOCATE 8, g: PRINT " ‹€€€€€€‹ "
640 LOCATE 9, g: PRINT "€€ﬂ    ﬂ€€"
650 LOCATE 10, g: PRINT "€€‹    ‹€€"
660 LOCATE 11, g: PRINT " ﬂ€€€€€€ﬂ "
RETURN

700 LOCATE 5, g: PRINT "€€€€€€€€€€"
710 LOCATE 6, g: PRINT "        €€"
720 LOCATE 7, g: PRINT "       ‹€€"
730 LOCATE 8, g: PRINT "     ‹€€ﬂ "
740 LOCATE 9, g: PRINT "   ‹€€ﬂ   "
750 LOCATE 10, g: PRINT " ‹€€ﬂ     "
760 LOCATE 11, g: PRINT "€€ﬂ       "
RETURN

800 LOCATE 5, g: PRINT " ‹€€€€€€‹ "
810 LOCATE 6, g: PRINT "€€ﬂ    ﬂ€€"
820 LOCATE 7, g: PRINT "€€‹    ‹€€"
830 LOCATE 8, g: PRINT " €€€€€€€€ "
840 LOCATE 9, g: PRINT "€€ﬂ    ﬂ€€"
850 LOCATE 10, g: PRINT "€€‹    ‹€€"
860 LOCATE 11, g: PRINT " ﬂ€€€€€€ﬂ "
RETURN

900 LOCATE 5, g: PRINT " ‹€€€€€€‹ "
910 LOCATE 6, g: PRINT "€€ﬂ    ﬂ€€"
920 LOCATE 7, g: PRINT "€€‹    ‹€€"
930 LOCATE 8, g: PRINT " ﬂ€€€€€€ﬂ "
940 LOCATE 9, g: PRINT "   ‹€€ﬂ   "
950 LOCATE 10, g: PRINT " ‹€€ﬂ     "
960 LOCATE 11, g: PRINT "€€ﬂ       "
RETURN

1000 LOCATE 5, g: PRINT " ‹€€€€€€‹ "
1010 LOCATE 6, g: PRINT "€€ﬂ    ﬂ€€"
1020 LOCATE 7, g: PRINT "€€      €€"
1030 LOCATE 8, g: PRINT "€€      €€"
1040 LOCATE 9, g: PRINT "€€      €€"
1050 LOCATE 10, g: PRINT "€€‹    ‹€€"
1060 LOCATE 11, g: PRINT " ﬂ€€€€€€ﬂ "
RETURN


5000 DO

COLOR 4, 0
GOSUB 12
IF h1$ = "1" THEN g = 3: GOSUB 100
IF h1$ = "2" THEN g = 3: GOSUB 200
IF h1$ = "3" THEN g = 3: GOSUB 300
IF h1$ = "4" THEN g = 3: GOSUB 400
IF h1$ = "5" THEN g = 3: GOSUB 500
IF h1$ = "6" THEN g = 3: GOSUB 600
IF h1$ = "7" THEN g = 3: GOSUB 700
IF h1$ = "8" THEN g = 3: GOSUB 800
IF h1$ = "9" THEN g = 3: GOSUB 900
IF h1$ = "0" THEN g = 3: GOSUB 1000
                 
IF h2$ = "1" THEN g = 15: GOSUB 100
IF h2$ = "2" THEN g = 15: GOSUB 200
IF h2$ = "3" THEN g = 15: GOSUB 300
IF h2$ = "4" THEN g = 15: GOSUB 400
IF h2$ = "5" THEN g = 15: GOSUB 500
IF h2$ = "6" THEN g = 15: GOSUB 600
IF h2$ = "7" THEN g = 15: GOSUB 700
IF h2$ = "8" THEN g = 15: GOSUB 800
IF h2$ = "9" THEN g = 15: GOSUB 900
IF h2$ = "0" THEN g = 15: GOSUB 1000

LOCATE 7, 27: PRINT "€": LOCATE 10, 27: PRINT "€"

IF min1$ = "1" THEN g = 30: GOSUB 100
IF min1$ = "2" THEN g = 30: GOSUB 200
IF min1$ = "3" THEN g = 30: GOSUB 300
IF min1$ = "4" THEN g = 30: GOSUB 400
IF min1$ = "5" THEN g = 30: GOSUB 500
IF min1$ = "6" THEN g = 30: GOSUB 600
IF min1$ = "7" THEN g = 30: GOSUB 700
IF min1$ = "8" THEN g = 30: GOSUB 800
IF min1$ = "9" THEN g = 30: GOSUB 900
IF min1$ = "0" THEN g = 30: GOSUB 1000
                
IF min2$ = "1" THEN g = 42: GOSUB 100
IF min2$ = "2" THEN g = 42: GOSUB 200
IF min2$ = "3" THEN g = 42: GOSUB 300
IF min2$ = "4" THEN g = 42: GOSUB 400
IF min2$ = "5" THEN g = 42: GOSUB 500
IF min2$ = "6" THEN g = 42: GOSUB 600
IF min2$ = "7" THEN g = 42: GOSUB 700
IF min2$ = "8" THEN g = 42: GOSUB 800
IF min2$ = "9" THEN g = 42: GOSUB 900
IF min2$ = "0" THEN g = 42: GOSUB 1000

LOCATE 7, 54: PRINT "€": LOCATE 10, 54: PRINT "€"

IF s1$ = "1" THEN g = 57: GOSUB 100
IF s1$ = "2" THEN g = 57: GOSUB 200
IF s1$ = "3" THEN g = 57: GOSUB 300
IF s1$ = "4" THEN g = 57: GOSUB 400
IF s1$ = "5" THEN g = 57: GOSUB 500
IF s1$ = "6" THEN g = 57: GOSUB 600
IF s1$ = "7" THEN g = 57: GOSUB 700
IF s1$ = "8" THEN g = 57: GOSUB 800
IF s1$ = "9" THEN g = 57: GOSUB 900
IF s1$ = "0" THEN g = 57: GOSUB 1000
                
IF s2$ = "1" THEN g = 69: GOSUB 100
IF s2$ = "2" THEN g = 69: GOSUB 200
IF s2$ = "3" THEN g = 69: GOSUB 300
IF s2$ = "4" THEN g = 69: GOSUB 400
IF s2$ = "5" THEN g = 69: GOSUB 500
IF s2$ = "6" THEN g = 69: GOSUB 600
IF s2$ = "7" THEN g = 69: GOSUB 700
IF s2$ = "8" THEN g = 69: GOSUB 800
IF s2$ = "9" THEN g = 69: GOSUB 900
IF s2$ = "0" THEN g = 69: GOSUB 1000


COLOR 2, 0
GOSUB 11
LOCATE 15, py: PRINT dd$; " "; month$; " "; yyyy$

GOSUB 13
LOCATE 19, 31: COLOR 3, 0: PRINT "ALARM TIME:   "; ph$; ":"; pm$
LOCATE 20, 31: COLOR 3, 0: PRINT "ALARM STATUS: "; as$
IF as$ = "ON" AND ph$ = hh$ AND pm$ = min$ THEN PLAY "o4d4"
KEY 1, CHR$(1) + CHR$(13)
LOCATE 22, 31: COLOR 14, 0: PRINT "Press <F1> for menu"
LOCATE 18, 1: COLOR 1, 0: PRINT "…ÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕª"
LOCATE 19, 1: COLOR 1, 0: PRINT "∫": LOCATE 19, 80: COLOR 1, 0: PRINT "∫"
LOCATE 20, 1: COLOR 1, 0: PRINT "∫": LOCATE 20, 80: COLOR 1, 0: PRINT "∫"
LOCATE 21, 1: COLOR 1, 0: PRINT "ÃÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕπ"
LOCATE 22, 1: COLOR 1, 0: PRINT "∫": LOCATE 22, 80: COLOR 1, 0: PRINT "∫"
LOCATE 23, 1: COLOR 1, 0: PRINT "»ÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕÕº"
t = t + 1
IF t = 500 THEN t = 0: GOTO 40000
LOOP UNTIL INKEY$ = CHR$(1)

CLS

COLOR 15, 9: CLS
6000 DO
newdate$ = DATE$
LOCATE 3, 18: PRINT "Time: "; TIME$: LOCATE 3, 42: PRINT "Date: "; dd$; " "; month$; " "; yyyy$
LOCATE 4, 18: PRINT "Alarm time: "; ph$; ":"; pm$: LOCATE 4, 42: PRINT "Alarm status: "; as$; " "
IF as$ = "ON" THEN as1$ = "OFF"
IF as$ = "OFF" THEN as1$ = "ON"
LOCATE 6, 25: PRINT "<F2>       Set alarm to "; as1$; " "
LOCATE 7, 25: PRINT "<F3>       Alarm set: increase hours"
LOCATE 8, 25: PRINT "<F4>       Alarm set: decrease hours"
LOCATE 9, 25: PRINT "<F5>       Alarm set: increase minutes"
LOCATE 10, 25: PRINT "<F6>       Alarm set: decrease minutes"
LOCATE 12, 25: PRINT "<F12>      Exit Clock"

KEY 2, CHR$(202) + CHR$(13)
KEY 3, CHR$(203) + CHR$(13)
KEY 4, CHR$(204) + CHR$(13)
KEY 5, CHR$(205) + CHR$(13)
KEY 6, CHR$(206) + CHR$(13)
KEY 7, CHR$(207) + CHR$(13)
KEY 31, CHR$(212) + CHR$(13)

LOCATE 22, 30: COLOR 15, 9: PRINT "Press <F1> for clock"
com$ = INKEY$
IF com$ = CHR$(1) THEN COLOR 7, 0: CLS : GOTO 5000
IF com$ = CHR$(202) THEN as$ = as1$
IF com$ = CHR$(203) THEN pha = pha + 1
IF com$ = CHR$(204) THEN pha = pha - 1
IF com$ = CHR$(205) THEN pma = pma + 1
IF com$ = CHR$(206) THEN pma = pma - 1
IF pha > 23 THEN pha = 0
IF pha < 0 THEN pha = 23
IF pma > 59 THEN pma = 0
IF pma < 0 THEN pma = 59
GOSUB 13000
IF com$ <> "" THEN t = 0
t = t + 1
IF t = 500 THEN t = 0: COLOR 7, 0: CLS : GOTO 5000
IF com$ = CHR$(212) THEN COLOR 7, 0: CLS : GOTO 10000
LOOP


10000 CLS
COLOR 14, 0
PRINT "Thanks for using CLOCK ˚1.02"
COLOR 3, 0
PRINT "(C) Copyright 1995 by Sander van Loo, SPI"
PLAY "p1"
SYSTEM

13000 IF pha = 1 THEN ph$ = "01"
IF pha = 2 THEN ph$ = "02"
IF pha = 3 THEN ph$ = "03"
IF pha = 4 THEN ph$ = "04"
IF pha = 5 THEN ph$ = "05"
IF pha = 6 THEN ph$ = "06"
IF pha = 7 THEN ph$ = "07"
IF pha = 8 THEN ph$ = "08"
IF pha = 9 THEN ph$ = "09"
IF pha = 10 THEN ph$ = "10"
IF pha = 11 THEN ph$ = "11"
IF pha = 12 THEN ph$ = "12"
IF pha = 13 THEN ph$ = "13"
IF pha = 14 THEN ph$ = "14"
IF pha = 15 THEN ph$ = "15"
IF pha = 16 THEN ph$ = "16"
IF pha = 17 THEN ph$ = "17"
IF pha = 18 THEN ph$ = "18"
IF pha = 19 THEN ph$ = "19"
IF pha = 20 THEN ph$ = "20"
IF pha = 21 THEN ph$ = "21"
IF pha = 22 THEN ph$ = "22"
IF pha = 23 THEN ph$ = "23"
IF pha = 24 THEN ph$ = "24"
IF pha = 0 THEN ph$ = "00"

IF pma = 1 THEN pm$ = "01"
IF pma = 2 THEN pm$ = "02"
IF pma = 3 THEN pm$ = "03"
IF pma = 4 THEN pm$ = "04"
IF pma = 5 THEN pm$ = "05"
IF pma = 6 THEN pm$ = "06"
IF pma = 7 THEN pm$ = "07"
IF pma = 8 THEN pm$ = "08"
IF pma = 9 THEN pm$ = "09"
IF pma = 10 THEN pm$ = "10"
IF pma = 11 THEN pm$ = "11"
IF pma = 12 THEN pm$ = "12"
IF pma = 13 THEN pm$ = "13"
IF pma = 14 THEN pm$ = "14"
IF pma = 15 THEN pm$ = "15"
IF pma = 16 THEN pm$ = "16"
IF pma = 17 THEN pm$ = "17"
IF pma = 18 THEN pm$ = "18"
IF pma = 19 THEN pm$ = "19"
IF pma = 20 THEN pm$ = "20"
IF pma = 21 THEN pm$ = "21"
IF pma = 22 THEN pm$ = "22"
IF pma = 23 THEN pm$ = "23"
IF pma = 24 THEN pm$ = "24"
IF pma = 25 THEN pm$ = "25"
IF pma = 26 THEN pm$ = "26"
IF pma = 27 THEN pm$ = "27"
IF pma = 28 THEN pm$ = "28"
IF pma = 29 THEN pm$ = "29"
IF pma = 30 THEN pm$ = "30"
IF pma = 31 THEN pm$ = "31"
IF pma = 32 THEN pm$ = "32"
IF pma = 33 THEN pm$ = "33"
IF pma = 34 THEN pm$ = "34"
IF pma = 35 THEN pm$ = "35"
IF pma = 36 THEN pm$ = "36"
IF pma = 37 THEN pm$ = "37"
IF pma = 38 THEN pm$ = "38"
IF pma = 39 THEN pm$ = "39"
IF pma = 40 THEN pm$ = "40"
IF pma = 41 THEN pm$ = "41"
IF pma = 42 THEN pm$ = "42"
IF pma = 43 THEN pm$ = "43"
IF pma = 44 THEN pm$ = "44"
IF pma = 45 THEN pm$ = "45"
IF pma = 46 THEN pm$ = "46"
IF pma = 47 THEN pm$ = "47"
IF pma = 48 THEN pm$ = "48"
IF pma = 49 THEN pm$ = "49"
IF pma = 50 THEN pm$ = "50"
IF pma = 51 THEN pm$ = "51"
IF pma = 52 THEN pm$ = "52"
IF pma = 53 THEN pm$ = "53"
IF pma = 54 THEN pm$ = "54"
IF pma = 55 THEN pm$ = "55"
IF pma = 56 THEN pm$ = "56"
IF pma = 57 THEN pm$ = "57"
IF pma = 58 THEN pm$ = "58"
IF pma = 59 THEN pm$ = "59"
IF pma = 60 THEN pm$ = "60"
IF pma = 0 THEN pm$ = "00"
RETURN

40000
DO
RANDOMIZE TIMER
xc = INT(RND * 23) + 1
yc = INT(RND * 35) + 1
col = INT(RND * 15) + 1
LOCATE xc, yc: COLOR col, 0: PRINT "Screensaving...    Time: "; TIME$
PLAY "p4"
CLS
GOSUB 12
IF as$ = "ON" AND ph$ = hh$ AND pm$ = min$ THEN PLAY "o4d4"
scan$ = INKEY$
IF scan$ <> "" THEN GOTO 5000
LOOP

