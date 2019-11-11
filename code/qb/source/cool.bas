'COOL.BAS By Paul Metselaar. Versie 1.00 Freeware & testversie.
'Als het programma te lang bij 1 onderdeel (cirkels, lijnen, open vierkanten
'of ingekleurde vierkanten) blijft zitten, verander dan de constante Stopermee
'in een lager getal.
DEFINT A-Z
DECLARE SUB setrgb (clr%, r%, g%, b%)
DECLARE SUB getrgb (clr%, r%, g%, b%)
DECLARE SUB Cirkel ()
DECLARE SUB Effect (w1%, w2%)
DECLARE SUB FadeOut ()
DECLARE SUB SlideColors ()
DECLARE SUB InitColors ()
DECLARE FUNCTION max% (w1%, w2%)
RANDOMIZE TIMER
CONST Stopermee = 1000
DIM SHARED r, g, b, laatste, voorlaatste, r1, g1, b1, ru, gu, bu, pal, kleurtje
SCREEN 13
InitColors
Cirkel
laatste = 9
Herh:
klr = 1: x1 = INT(RND * 320): x2 = INT(RND * 320): y1 = INT(RND * 200): y2 = INT(RND * 200): t = 0
form = INT(RND * 3) + 1
DO
 sx1 = sx1: sx1 = INT(RND * 3) + 1: sx1 = sx1 - 1: IF sx1 = 0 THEN sx1 = ox1
 ox2 = sx2: sx2 = INT(RND * 3) + 1: sx2 = sx2 - 1: IF sx2 = 0 THEN sx2 = ox2
 oy1 = sy1: sy1 = INT(RND * 3) + 1: sy1 = sy1 - 1: IF sy1 = 0 THEN sy1 = oy1
 oy2 = sy2: sy2 = INT(RND * 3) + 1: sy2 = sy2 - 1: IF sy2 = 0 THEN sy2 = oy2
 hmdo = INT(RND * 15) + 1
 FOR h = 1 TO hmdo
  t = t + (1 * klr)
  IF t = 255 THEN klr = -1
  IF t = 1 THEN klr = 1
  IF sx1 = 1 THEN x1 = x1 + 1
  IF sx1 = 2 THEN x1 = x1 - 1
  IF sx2 = 1 THEN x2 = x2 + 1
  IF sx2 = 2 THEN x2 = x2 - 1
  IF sy1 = 1 THEN y1 = y1 + 1
  IF sy1 = 2 THEN y1 = y1 - 1
  IF sy2 = 1 THEN y2 = y2 + 1
  IF sy2 = 2 THEN y2 = y2 - 1
  IF x1 >= 320 THEN x1 = 320
  IF x1 <= 0 THEN x1 = 0
  IF x2 >= 320 THEN x2 = 320
  IF x2 <= 0 THEN x2 = 0
  IF y1 >= 200 THEN y1 = 200
  IF y1 <= 0 THEN y1 = 0
  IF y2 >= 200 THEN y2 = 200
  IF y2 <= 0 THEN y2 = 0
  SELECT CASE form
   CASE 1
    LINE (x1, y1)-(x2, y2), t
    LINE (x1, 200 - y1)-(x2, 200 - y2), t
    LINE (320 - x1, y1)-(320 - x2, y2), t
    LINE (320 - x1, 200 - y1)-(320 - x2, 200 - y2), t
    LINE (y1, x1)-(y2, x2), t
    LINE (y1, 200 - x1)-(y2, 200 - x2), t
    LINE (320 - y1, x1)-(320 - y2, x2), t
    LINE (320 - y1, 200 - x1)-(320 - y2, 200 - x2), t
   CASE 2
    LINE (x1, y1)-(x2, y2), t, BF
    LINE (x1, 200 - y1)-(x2, 200 - y2), t, BF
    LINE (320 - x1, y1)-(320 - x2, y2), t, BF
    LINE (320 - x1, 200 - y1)-(320 - x2, 200 - y2), t, BF
    LINE (y1, x1)-(y2, x2), t, BF
    LINE (y1, 200 - x1)-(y2, 200 - x2), t, BF
    LINE (320 - y1, x1)-(320 - y2, x2), t, BF
    LINE (320 - y1, 200 - x1)-(320 - y2, 200 - x2), t, BF
   CASE 3
    LINE (x1, y1)-(x2, y2), t, B
    LINE (x1, 200 - y1)-(x2, 200 - y2), t, B
    LINE (320 - x1, y1)-(320 - x2, y2), t, B
    LINE (320 - x1, 200 - y1)-(320 - x2, 200 - y2), t, B
    LINE (y1, x1)-(y2, x2), t, B
    LINE (y1, 200 - x1)-(y2, 200 - x2), t, B
    LINE (320 - y1, x1)-(320 - y2, x2), t, B
    LINE (320 - y1, 200 - x1)-(320 - y2, 200 - x2), t, B
  END SELECT
  SlideColors
  IF INKEY$ <> "" THEN
   FadeOut
   SCREEN 0: WIDTH 80: COLOR 7, 0: CLS : END
  END IF
  stp = INT(RND * Stopermee) + 1: IF stp = 20 THEN stoppen = 1 ELSE stoppen = 0
  IF stoppen THEN EXIT DO
 NEXT h
LOOP
KiesEenAndere:
e% = INT(RND * 10) + 1
IF laatste = e% OR voorlaatste = e% THEN GOTO KiesEenAndere
voorlaatste = laatste
laatste = e%
SELECT CASE e%
 CASE 1: Effect 0, 0
 CASE 2: Effect 1, 0
 CASE 3: Effect 1, 1
 CASE 4: Effect 2, 0
 CASE 5: Effect 2, 1
 CASE 6: Effect 3, 0
 CASE 7: Effect 3, 1
 CASE 8: Effect 4, 0
 CASE 9: Cirkel
 CASE ELSE: GOTO Herh
END SELECT
GOTO Herh

SUB Cirkel
 kleur1 = 0
 FOR i% = 200 TO 0 STEP -1
  kleur1 = kleur1 + 1
  CIRCLE (160, 100), i%, kleur1
  CIRCLE (161, 100), i%, kleur1
  SlideColors
 NEXT i%
 DO
  SlideColors
  IF INKEY$ <> "" THEN
   FadeOut
   SCREEN 0: WIDTH 80: COLOR 7, 0: CLS : END
  END IF
  stp = INT(RND * Stopermee) + 1: IF stp = 20 THEN stoppen = 1 ELSE stoppen = 0
  IF stoppen THEN EXIT DO
 LOOP
END SUB

SUB Effect (w1%, w2%)
 SELECT CASE w1%
  CASE 0
   kleur1 = 0
   FOR i% = 320 TO -1 STEP -1
    IF INT(i% / 2) = i% / 2 THEN kleur1 = kleur1 + 1
    t% = ABS(i% - 320)
    LINE (0, 0)-(t, 200), kleur1
    LINE (320, 200)-(i%, 0), kleur1
    IF INT(i% / 2) = i% / 2 THEN SlideColors
   NEXT i%
   EXIT SUB
  CASE 1
   kleur1 = 0
   IF w2% = 1 THEN F% = 160: t% = 0: s% = -1
   IF w2% = 0 THEN F% = 0: t% = 160: s% = 1
   FOR i% = F% TO t% STEP s%
    kleur1 = kleur1 + 1
    t% = ABS(i% - 320)
    LINE (t%, 0)-(t%, 199), kleur1
    LINE (i%, 0)-(i%, 199), kleur1
    SlideColors
   NEXT i%
  CASE 2
   kleur1 = 0
   IF w2% = 1 THEN F% = 100: t% = 0: s% = -1
   IF w2% = 0 THEN F% = 0: t% = 100: s% = 1
   FOR i% = F% TO t% STEP s%
    kleur1 = kleur1 + 1
    t% = ABS(i% - 200)
    LINE (0, t%)-(319, t%), kleur1
    LINE (0, i%)-(319, i%), kleur1
    SlideColors
   NEXT i%
  CASE 3
   IF w2% = 1 THEN x2 = 320: y2 = 200: y1 = 0: sp = 1: ep = 100: st = 1: np1 = 1: np2 = 1
   IF w2% = 0 THEN x2 = 220: y2 = 100: y1 = 100: sp = 100: ep = 1: st = -1: np1 = -1: np2 = -1
   kleur1 = 0
   FOR x1 = sp TO ep STEP st
    kleur1 = kleur1 + 1
    y1 = y1 + np1
    x2 = x2 - np2
    y2 = y2 - np2
    LINE (x1, y1)-(x2, y2), kleur1, B
    SlideColors
   NEXT x1
  CASE 4
   FadeOut
   CLS
   InitColors
 END SELECT
END SUB

SUB FadeOut
  FOR i = 1 TO 63
   FOR j = 0 TO 255
    getrgb j, r, g, b
    setrgb j, max(r - 1, 0), max(g - 1, 0), max(b - 1, 0)
   NEXT j
  NEXT i
END SUB

SUB getrgb (clr, r, g, b)
 OUT &H3C6, &HFF
 OUT &H3C7, clr
 r = INP(&H3C9)
 g = INP(&H3C9)
 b = INP(&H3C9)
END SUB

SUB InitColors
 kleurtje = 1
 r = 0: g = 0: b = 0
 OUT &H3C8, 1
 OUT &H3C9, r
 OUT &H3C9, g
 OUT &H3C9, b
 RANDOMIZE TIMER
 ru = INT(RND * 2) + 1
 gu = INT(RND * 2) + 1
 bu = INT(RND * 2) + 1
 ru = ru - 1: gu = gu - 1: bu = bu - 1
 FOR i% = 0 TO 255
  OUT &H3C8, i%
  OUT &H3C9, 0
  OUT &H3C9, 0
  OUT &H3C9, 0
 NEXT i%
 DO
  FOR i = 1 TO 63
   IF ru = 1 THEN r = r + 1 ELSE r = r - 1
   IF gu = 1 THEN g = g + 1 ELSE g = g - 1
   IF bu = 1 THEN b = b + 1 ELSE b = b - 1
   IF r > 63 THEN r = 63
   IF r < 0 THEN r = 0
   IF g > 63 THEN g = 63
   IF g < 0 THEN g = 0
   IF b > 63 THEN b = 63
   IF b < 0 THEN b = 0
   OUT &H3C8, k
   OUT &H3C9, r
   OUT &H3C9, g
   OUT &H3C9, b
   k = k - 1
   IF k < 2 THEN pal = i: EXIT SUB
  NEXT i
  ruo = ru: guo = gu: buo = bu
opniw:
  RANDOMIZE TIMER
  ru = INT(RND * 2) + 1
  gu = INT(RND * 2) + 1
  bu = INT(RND * 2) + 1
  ru = ru - 1: gu = gu - 1: bu = bu - 1
  IF ruo = ru AND guo = gu AND buo = bu THEN GOTO opniw
 LOOP
END SUB

FUNCTION max (w1, w2)
 IF w1 > w2 THEN max = w1 ELSE max = w2
END FUNCTION

SUB setrgb (clr, r, g, b)
 OUT &H3C6, &HFF
 OUT &H3C8, clr
 OUT &H3C9, r
 OUT &H3C9, g
 OUT &H3C9, b
END SUB

SUB SlideColors
  pal = pal + 1
  IF pal > 63 THEN
   ruo = ru: guo = gu: buo = bu
opnw:
   RANDOMIZE TIMER
   ru = INT(RND * 2) + 1
   gu = INT(RND * 2) + 1
   bu = INT(RND * 2) + 1
   ru = ru - 1: gu = gu - 1: bu = bu - 1
   IF ru = ruo AND gu = guo AND bu = buo THEN GOTO opnw
   pal = 1
  END IF
  IF ru = 1 THEN r = r + 1 ELSE r = r - 1
  IF gu = 1 THEN g = g + 1 ELSE g = g - 1
  IF bu = 1 THEN b = b + 1 ELSE b = b - 1
  IF r > 63 THEN r = 63
  IF r < 0 THEN r = 0
  IF g > 63 THEN g = 63
  IF g < 0 THEN g = 0
  IF b > 63 THEN b = 63
  IF b < 0 THEN b = 0
  OUT &H3C8, kleurtje
  OUT &H3C9, r
  OUT &H3C9, g
  OUT &H3C9, b
  kleurtje = kleurtje - 1
  IF kleurtje < 2 THEN
   getrgb 255, r, g, b
   FOR j% = 255 TO 2 STEP -1
    getrgb j% - 1, r, g, b
    setrgb j%, r, g, b
   NEXT j%
   kleurtje = 1
  END IF
END SUB

