PROGRAM BRIL3D;

USES
  crt,video,graph;

CONST
    oogafstand : Longint= 150;
    distance   = 30;

TYPE
  Pointtype3d = RECORD
                  realX,realY,realZ : LongInt;
                  alfa1,alfa2 : Real;
                  straal1,straal2 : LongInt;
                  screenX1,screenY1,screenX2,screenY2 : Integer;
                  translatedX,translatedY,translatedZ : Real;
                END;

VAR
 i,n               : Integer;
 alfa1,alfa2       : Real;
 ch                : Char;
 punten            : ARRAY [1..8] of Pointtype3d;
 zAfst             : Integer;
 aantalstappen     : Integer;
 s                 : String;
 pause             : Integer;
 Normal            : boolean;

PROCEDURE SPLine ( x1,y1,x2,y2,c : Integer);
BEGIN
  IF Normal=true then IF c=1 then SetColor(91) Else SetColor(100) Else
    IF c=1 then SetColor(66) Else SetColor(52);
  Line (x1,y1,x2,y2);
END;


BEGIN
Normal:=false;
aantalstappen:=50;
If ParamCount>0 then
BEGIN
  Val(Paramstr(1),aantalstappen,i);
  if ParamCount>1 then
    if Paramstr(2)='normaal' then Normal:=true else normal:=false;
END;
zAfst:=30;
punten[1].realX:=-50;
punten[1].realY:=-50;
punten[1].realZ:=-50;

punten[2].realX:=50;
punten[2].realY:=-50;
punten[2].realZ:=-50;

punten[3].realX:=50;
punten[3].realY:=-50;
punten[3].realZ:=50;

punten[4].realX:=-50;
punten[4].realY:=-50;
punten[4].realZ:=50;

punten[5].realX:=-50;
punten[5].realY:=50;
punten[5].realZ:=-50;

punten[6].realX:=50;
punten[6].realY:=50;
punten[6].realZ:=-50;

punten[7].realX:=50;
punten[7].realY:=50;
punten[7].realZ:=50;

punten[8].realX:=-50;
punten[8].realY:=50;
punten[8].realZ:=50;

FOR i:=1 TO 8 DO
 BEGIN
   punten[i].straal1:=Round(sqrt(sqr(punten[i].realX)+sqr(punten[i].realZ)));
   punten[i].alfa1:=Abs(ArcTan(punten[i].realZ/punten[i].realX));
   IF punten[i].realZ<0 then punten[i].alfa1:=-punten[i].alfa1;
   IF punten[i].realX<0 then punten[i].alfa1:=Pi-punten[i].alfa1;
 END;

VGA256Init;
    Asm
      mov ax,0a000h
      mov es,ax
      mov ax,00F0Fh
      mov di,0
      mov cx,320*100
  @l: repnz stosw
    END;
{
SetColor(Yellow);
For i:=1 to 255 DO
BEGIN
    Asm
      mov ax,0a000h
      mov es,ax
      mov ax,00F0Fh
      mov di,0
      mov cx,320*100
  @l: repnz stosw
    END;
    Asm
      mov ax,0a000h
      mov es,ax
      mov ax,i
      mov di,90*320+140
      Mov cx,20
  @r: mov bx,cx
      mov cx,20
  @l: repnz stosb
      add di,320-20
      mov cx,bx
      loop @r
    END;
  Str(i,s);
  OutTextXY(10,10,s);
  Readkey;
END;}

 ch:=#0;
REPEAT
 FOR I := 1 TO aantalstappen DO
  BEGIN
    FOR n:=1 TO 8 DO
     BEGIN
      alfa1:=punten[n].alfa1+2*Pi*I/aantalstappen;
       punten[n].translatedX:=punten[n].straal1*Cos(alfa1);
      punten[n].translatedY:=punten[n].RealY;
      punten[n].translatedZ:=punten[n].straal1*Sin(alfa1);
      punten[n].screenX1:=160+Round((oogafstand*punten[n].translatedX         ) /(oogafstand+punten[n].translatedZ+zAfst));
      punten[n].screenY1:=100-Round((oogafstand*punten[n].translatedY           ) /(oogafstand+punten[n].translatedZ+zAfst));
      punten[n].screenX2:=160+Round((oogafstand*(punten[n].translatedX+distance)) /(oogafstand+punten[n].translatedZ+zAfst));
      punten[n].screenY2:=100-Round((oogafstand*punten[n].translatedY           ) /(oogafstand+punten[n].translatedZ+zAfst));
     END;
    Delay(Pause);
    Asm
      mov ax,0a000h
      mov es,ax
      mov ax,00F0Fh
      mov di,0
      mov cx,320*100
  @l: repnz stosw
    END;
    WaitRetrace;
{ ** Teken onderste vlak van de kubus. **}
    SpLine(punten[1].screenx1,punten[1].screeny1,punten[2].screenx1,punten[2].screeny1,1);
    SpLine(punten[1].screenx2,punten[1].screeny2,punten[2].screenx2,punten[2].screeny2,2);

    SpLine(punten[2].screenx1,punten[2].screeny1,punten[3].screenx1,punten[3].screeny1,1);
    SpLine(punten[2].screenx2,punten[2].screeny2,punten[3].screenx2,punten[3].screeny2,2);

    SpLine(punten[3].screenx1,punten[3].screeny1,punten[4].screenx1,punten[4].screeny1,1);
    SpLine(punten[3].screenx2,punten[3].screeny2,punten[4].screenx2,punten[4].screeny2,2);

    SpLine(punten[4].screenx1,punten[4].screeny1,punten[1].screenx1,punten[1].screeny1,1);
    SpLine(punten[4].screenx2,punten[4].screeny2,punten[1].screenx2,punten[1].screeny2,2);

{** Teken bovenste vlak van de kubus. **}
    SpLine(punten[5].screenx1,punten[5].screeny1,punten[6].screenx1,punten[6].screeny1,1);
    SpLine(punten[5].screenx2,punten[5].screeny2,punten[6].screenx2,punten[6].screeny2,2);

    SpLine(punten[6].screenx1,punten[6].screeny1,punten[7].screenx1,punten[7].screeny1,1);
    SpLine(punten[6].screenx2,punten[6].screeny2,punten[7].screenx2,punten[7].screeny2,2);

    SpLine(punten[7].screenx1,punten[7].screeny1,punten[8].screenx1,punten[8].screeny1,1);
    SpLine(punten[7].screenx2,punten[7].screeny2,punten[8].screenx2,punten[8].screeny2,2);

    SpLine(punten[8].screenx1,punten[8].screeny1,punten[5].screenx1,punten[5].screeny1,1);
    SpLine(punten[8].screenx2,punten[8].screeny2,punten[5].screenx2,punten[5].screeny2,2);
{ ** Teken de verbindingslijnen van het bovenste met het onderste vlak. **}
    SpLine(punten[1].screenx1,punten[1].screeny1,punten[5].screenx1,punten[5].screeny1,1);
    SpLine(punten[1].screenx2,punten[1].screeny2,punten[5].screenx2,punten[5].screeny2,2);

    SpLine(punten[2].screenx1,punten[2].screeny1,punten[6].screenx1,punten[6].screeny1,1);
    SpLine(punten[2].screenx2,punten[2].screeny2,punten[6].screenx2,punten[6].screeny2,2);

    SpLine(punten[3].screenx1,punten[3].screeny1,punten[7].screenx1,punten[7].screeny1,1);
    SpLine(punten[3].screenx2,punten[3].screeny2,punten[7].screenx2,punten[7].screeny2,2);

    SpLine(punten[4].screenx1,punten[4].screeny1,punten[8].screenx1,punten[8].screeny1,1);
    SpLine(punten[4].screenx2,punten[4].screeny2,punten[8].screenx2,punten[8].screeny2,2);
  END;
IF Keypressed then ch:=Readkey;
UNTIL ch=#27;

END.









