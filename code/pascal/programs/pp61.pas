{$A+,B+,D+,E+,F-,G+,I+,L+,N-,O-,P+,Q+,R+,S+,T+,V+,X+}
{$M 16384,0,655360}
PROGRAM HorizontaleWorp;
{Programmeurs: Samenstellers Programmeren in Pascal
Datum laatste wijziging: 16 mei 1995
Dit programma geeft een tabel van de dracht als functie van de begin-
snelheid bij een horizontale worp voor een op te geven valhoogte en
een op te geven snelheidsinterval. Luchtwrijving wordt verwaarloosd.
Zie voor de betekenis van constanten en variabelen de declaraties.    }

USES
  Crt;

CONST
  gGrav           = 9.81;   {gravitatieversnelling in m/s^2}
                            {op 52 graden noorderbreedte}
  aantalvelden    = 6;      {van numerieke uitvoer}
  aantaldecimalen = 2;      {van numerieke uitvoer}
  aantalregels    = 5;      {van tabel}

VAR
  i            : Integer;
  vBeginEerst  : Real;      {eerste beginsnelheid in m/s}
  vBeginLaatst : Real;      {laatste beginsnelheid in m/s}
  vBegin       : Real;      {beginsnelheid in m/s}
  deltavBegin  : Real;      {verandering beginsnelheid in m/s}
  tWorp        : Real;      {valtijd in s}
  xWorp        : Real;      {dracht in m}
  yWorp        : Real;      {valhoogte in m}
  pauzetoets   : Char;

{---------------------------------------------------------------------}
PROCEDURE GeefStartwaarden;
{Deze procedure geeft startwaarden voor eerste en laatste beginsnelheid
en valhoogte.
Maakt gebruik van de globale variabelen vBeginEerst, vBeginLaatst en
yVal.}

BEGIN
  vBeginEerst:= 0;
  vBeginLaatst:= 10;
  yWorp:= 2;
END;
{---------------------------------------------------------------------}

{---------------------------------------------------------------------}
PROCEDURE LeesGegevensIn;
{Deze procedure leest waarden voor eerste en laatste beginsnelheid en
valhoogte in.
Maakt gebruik van de globale variabelen vBeginEerst, vBeginLaatst en
yVal.}

BEGIN
  Write('Eerste beginsnelheid in m/s: ');
  ReadLn(vBeginEerst);
  Write('Laatste beginsnelheid in m/s: ');
  ReadLn(vBeginLaatst);
  Write('Valhoogte in m:       ');
  ReadLn(yWorp);
END;
{---------------------------------------------------------------------}

{---------------------------------------------------------------------}
FUNCTION Valtijd(hVal : Real)   {valhoogte}
                      : Real;
{Functie retourneert uitgaande van valhoogte de valtijd bij een hori-
zontale worp. Luchtwrijving wordt verwaarloosd.
Gebruikt de globaal gedeclareerde constante gGrav.}

BEGIN
  Valtijd:= Sqrt(2*hVal/gGrav);
END;
{---------------------------------------------------------------------}

{---------------------------------------------------------------------}
FUNCTION Dracht(v0   : Real;   {beginsnelheid}
                hVal : Real)   {valhoogte}
                     : Real;
{Functie retourneert uitgaande van beginsnelheid en valhoogte de dracht
van een horizontale worp. Luchtwrijving wordt verwaarloosd.
Gebruikt de globaal gedeclareerde constante gGrav en de functie
Valtijd.}

VAR
  tVal : Real;  {valtijd}

BEGIN
  tVal:= Valtijd(hVal);
  Dracht:= v0*tVal;
END;
{---------------------------------------------------------------------}

{---------------------------------------------------------------------}
PROCEDURE Schrijftabelkop(hVal : Real;    {valhoogte}
                          tVal : Real);   {valtijd}

BEGIN
  WriteLn;
  WriteLn('  Dracht als functie van de beginsnelheid');
  WriteLn('  Valhoogte =', hVal:aantalvelden:aantaldecimalen, ' m');
  WriteLn('  Valtijd   =', tVal:aantalvelden:aantaldecimalen, ' s');
  WriteLn;
  WriteLn('  Beginsnelheid             Dracht');
  WriteLn('      m/s                      m  ');
  WriteLn;
END;
{---------------------------------------------------------------------}

{---------------------------------------------------------------------}
PROCEDURE Schrijftabelregel(v0    : Real;   {beginsnelheid}
                            dWorp : Real);  {dracht}
{Deze procedure schrijft een tabelregel met de berekende waarden voor
beginsnelheid en dracht.}

BEGIN
  Write  ('    ',v0:aantalvelden:aantaldecimalen, '                 ');
  WriteLn(dWorp:aantalvelden:aantaldecimalen);
END;
{---------------------------------------------------------------------}


{---------------------------------------------------------------------}
{Hoofdprogramma}
BEGIN
  ClrScr;
  GeefStartwaarden;
  (* LeesGegevensIn; *)
  (* ClrScr; *)
  tWorp:= Valtijd(yWorp);
  vBegin:= vBeginEerst;
  deltavBegin:= (vBeginLaatst - vBeginEerst) / aantalregels;
  Schrijftabelkop(yWorp, tWorp);
  FOR i:= 1 TO aantalregels DO
    BEGIN
      xWorp:= Dracht(vBegin, yWorp);
      Schrijftabelregel(vBegin, xWorp);
      vBegin:= vBegin + deltavBegin;
    END;
  WriteLn;
  Write('  Druk op een toets ... ');
  pauzetoets:= ReadKey;
END.