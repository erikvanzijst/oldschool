PROGRAM MijnProcedures;
{Bla bla}

Uses
   Crt;

Const
   uitvoermedium = 'prn';   {'con': uitvoer naar scherm
                             'prn': uitvoer naar printer}
   rijmax        = 100;
   aantal        = 13;
   veld          = 9;
   dec           = 0;
   spaties       = '                      ';
Type
   RealRij       =       ARRAY[1..rijmax] OF Real;
   IntegerRij    =       ARRAY[1..rijmax] Of Real;

Var
   beginaantalKernen,i    : Integer;
   vervconst              : Real;
   tijdstip               : RealRij;
   aantalKernen           : IntegerRij;


Procedure LeesDingenVanSchijf(var n0        : Integer;
                              var rVerval   : Real;
                              var tijd      : RealRij;
                              var nTijd     : IntegerRij);

var
   i       : Integer;
   invoer  : Text;

begin
   Assign(invoer,'pp7.dat');
   Reset(invoer);
   ReadLn(invoer,n0);
   Readln(invoer,rVerval);
   for i := 1 to aantal do
     ReadLn(invoer, tijd[i], nTijd[i]);
   Close(invoer);
end;

Procedure ZetGegevensMooiNeer;
var
  i             : integer;
  uitvoer       : text;

begin
Assign(uitvoer,uitvoermedium);
Rewrite(uitvoer);
WriteLn(uitvoer,spaties,'      RADIOACTIEF VERVAL');
WriteLn(uitvoer,spaties,'      ==================');
WriteLn(uitvoer,spaties,'       N(0) = ',beginaantalKernen);
WriteLn(uitvoer,spaties,'       labda = ',vervconst:veld);
WriteLn;
WriteLn(uitvoer,spaties,'      tijd           N');
WriteLn(uitvoer,spaties,'       jr');
WriteLn;
for i := 1 to aantal do
   WriteLn(uitvoer,spaties,tijdstip[i]:veld:dec,'      ', aantalKernen[i]:veld:dec);
end;



begin
ClrScr;
LeesDingenVanSchijf(beginaantalKernen, vervconst, tijdstip, aantalKernen);
ZetGegevensMooiNeer;
Repeat Until Keypressed;
end.
