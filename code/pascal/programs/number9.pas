PROGRAM GrafischPruttelenInPascal;
{Programmeur      :Erik van Zijst
Laatste wijziging :17 december 1995}

USES
  Crt , Graph;

CONST
 Poly1: array[1..4] of PointType = ((X: 5; Y: 10), (X: 5; Y:100),
   (X: 300; Y: 300), (X: 500; Y: 2));
 Poly2: array[1..4] of PointType = ((X:20; Y: 10), (X:20; Y:90),
   (X: 300; Y: 280), (X: 500; Y: 2));

VAR
  gd : Integer;
  gm : Integer;
  ch : Char;
  Mode : STRING;
  MaxX : STRING;
  MaxY : STRING;
  errorcode : Integer;

BEGIN
  ClrScr;
  GotoXY(23,12);
  WriteLn('Dit Scherm is in textmode!');
  GotoXY(23,13);
  WriteLn('Druk een toets in om door te gaan ...');
  ch:=ReadKey;
  gd := detect;
  InitGraph(gd , gm , '');
  errorcode := GraphResult;
  IF (errorcode <> grOK)
    THEN
      BEGIN
        ClrScr;
        WriteLn(GraphErrorMsg(errorcode));
        ReadLn;
        Halt(1);
      END;
  OutTextXY(150,210, 'Dit scherm is in grafische mode!');
  OutTextXY(150,220, 'De gebruikte graphicsdriver is '+GetDriverName);
  Str(GetGraphMode, Mode);
  Str(GetMaxX, MaxX);
  Str(GetMaxY, MaxY);
  OutTextXY(150,230, 'De gebruikte mode is '+Mode);
  OutTextXY(150,240, 'De maximale pixelwaarde in X richting is '+MaxX);
  OutTextXY(150,250, 'De maximale pixelwaarde in Y richting is '+MaxY);
  OutTextXY(150,260, 'Druk een toets in om door te gaan ...');
  ch:=ReadKey;
  ClearDevice;
  Line(0,0,GetMaxX,GetMaxY);
  ch:=ReadKey;
  ClearDevice;
  DrawPoly(SizeOf(Poly1) div SizeOf(PointType), Poly1);
  ch:=ReadKey;
  SetLineStyle(DottedLn, 0, NormWidth);
  DrawPoly(SizeOf(Poly2) div SizeOf(PointType), Poly2);
  ch:=ReadKey;
  ClearDevice;
  CloseGraph;
  GotoXY(23,12);
  WriteLn('Dit scherm is weer in de textmode!');
  GotoXY(23,13);
  WriteLn('Druk een toets in om door te gaan ...');
  ch:=ReadKey;
  ClearDevice;
END.

