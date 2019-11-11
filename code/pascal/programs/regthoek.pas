PROGRAM PP9onderdeel32OverGraph;
{In dit programma wordt een viewport gedefinieerd en een stapel gekleurde
rechthoeken getekend.
Programmeur      : Erik van Zijst
Laatste wijziging: 17 december 1995}

USES
  Crt , Graph;

VAR
  gd : Integer;
  ch : Char;
  gm : Integer;
  errorcode : Integer;

BEGIN
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
  SetColor(2);
  SetViewPort((GetMaxX Div 4), (GetMaxY Div 4), (3*GetMaxX Div 4), (3*GetMaxY Div 4), True);
  Rectangle(0, 0, (479), (359));
  ch:=ReadKey;
  ClearDevice;
END.