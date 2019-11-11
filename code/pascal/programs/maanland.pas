PROGRAM Maanlander;

{Een maanlander nadert het maanoppervlak. Op zekere hoogte worden
bij een bepaalde verticale snelheid de motoren uitgezet.
Dit programma berekent na het stoppen van de motoren de daalsnelheid
van een maanlander op een op te geven hoogte boven het maanoppervlak.
Deze daalsnelheid wordt zowel analytisch als numeriek berekend.
Raadpleeg voor de theorie hoofdstuk 3 van deze opdracht.
Tenslotte wordt de procentuele afwijking van de numeriek berekende
snelheid ten opzichte van de analytisch berekende snelheid berekend.
Programmeurs      : medewerkers NP.
Laatste wijziging : 16 juni 1993.
Voltooid door     : Erik van Zijst
Datum             : 17 december 1995                                }

USES
  Crt;

CONST
  wit   = '      ';   {zes spaties}
  gGrav = 1.62;       {gravitatieversnelling op de maan in m/s^2}

VAR
  aantalIntervallen  : Integer;  {voor numerieke berekening}
  beginhoogte        : Real;     {afstand lander-maan in m als motoren
                                  stoppen}
  daalhoogte         : Real;     {hoogte in m ten opzichte van het
                                  maanoppervlak waarvoor de daalsnelheid
                                  berekend wordt}
  beginsnelheid      : Real;     {snelheid maanlander in m/s als motoren
                                  stoppen}
  daalsnelheidAnalyt : Real;     {analytisch berekende daalsnelheid
                                  in m/s}
  daalsnelheidNum    : Real;     {numeriek berekende daalsnelheid
                                  in m/s}
  perc               : Real;     {procentueel verschil numeriek bere-
                                  kende snelheid t.o.v. analytisch
                                  berekende snelheid}


{----------------------------------------------------------------------}
PROCEDURE GeefTestwaarden
          (VAR  h0    : Real;      {beginhoogte t.o.v. maanoppervlak}
           VAR  v0    : Real;      {beginsnelheid van maanlander}
           VAR  xAfst : Real;      {hoogte voor snelheidsberekening}
           VAR  nInt  : Integer);  {aantal intervallen}
{Deze procedure geeft waarden, waarmee het programma getest kan wor-
den. Te zijner tijd moet deze procedure door een invoerprocedure
vervangen worden.}

BEGIN
  h0:=    5;     {meter boven maanoppervlak}
  v0:=    1;     {m/s}
  xAfst:= 3;     {m}
  nInt:=  100;
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
PROCEDURE LeesGegevensIn
          (VAR  h0    : Real;     {beginhoogte t.o.v. maanoppervlak}
           VAR  v0    : Real;     {beginsnelheid van maanlander}
           VAR  xAfst : Real;     {hoogte voor snelheidsberekening}
           VAR  nInt  : Integer); {aantal intervallen}
{Deze procedure vraagt om invoer van de gegevens via het toetsenbord.
Gebruikt de unit Crt.}

BEGIN
  ClrScr;
  WriteLn;
  WriteLn('DAALSNELHEID MAANLANDER');
  WriteLn;
  Write  ('Dit programma berekent de snelheid na stoppen van ');
  WriteLn('de motoren.');
  Write  ('Er vindt zowel een analytische als een numerieke ');
  WriteLn('berekening plaats.');
  WriteLn;
  Write  ('De hoogte (m) waarop de motoren worden gestopt is : ');
  ReadLn(h0);
  Write  ('De snelheid (m/s) is dan                          : ');
  ReadLn(v0);
  Write  ('Hoogte (m) voor berekening daalsnelheid           : ');
  ReadLn(xAfst);
  Write  ('Het aantal stappen voor de numerieke berekening is: ');
  ReadLn(nInt);
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
FUNCTION SnelheidMaanlander
         (v0       : Real;  {beginsnelheid van maanlander}
          sAfstand : Real)  {afstand in m door maanlander afgelegd}
                   : Real;

{Functie retourneert de verticale daalsnelheid waarmee de maanlander
het maanoppervlak nadert.
Raadpleeg voor de gebruikte formule hoofdstuk 3 van deze opdracht.
Gebruikt de globaal gedefinieerde constante
  gGrav, de gravitatieversnelling bij het maanoppervlak.}

BEGIN
  SnelheidMaanlander:= Sqrt(Sqr(v0) + 2*gGrav*sAfstand);
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
PROCEDURE BerekenSnelheidNumeriekMetAfwijking;
{Raadpleeg de bij dit programma horende opdracht. Voeg zelf procedure-
parameters toe.}

  {-------------------------------------------------------------------}
  FUNCTION ProcentueelVerschil : Real;
  {Raadpleeg de bij dit programma horende opdracht. Voeg zelf procedu-
  reparameters toe.}

  BEGIN
   perc:=((daalsnelheidNum-daalsnelheidAnalyt)/daalsnelheidAnalyt)*100
  END;
  {--------------------------------------------------------------------}
VAR
  intervalgrootte : Real;
  teller          : Integer;
  snelheid        : Real;
  tijd            : Real;

BEGIN
  intervalgrootte:=(beginhoogte-daalhoogte)/aantalIntervallen;
  snelheid:=beginsnelheid;
  teller:=0;
  REPEAT
    BEGIN
      tijd:=intervalgrootte/snelheid;
      snelheid:=snelheid+gGrav*tijd;             {v(t)=v(0)+g*t}
      teller:=teller+1;
    END;
  UNTIL teller=aantalIntervallen;
  daalsnelheidNum:=snelheid;
  ProcentueelVerschil;
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
PROCEDURE SchrijfWaardenNaarScherm
          (h0        : Real;    {maanlander t.o.v.maan}
           v0        : Real;    {maanlander in m/s}
           xAfst     : Real;    {hoogte voor snelheidsberekening}
           vDaalA    : Real;    {analytische berekening}
           nInt      : Integer; {voor numerieke berekening}
           vDaalN    : Real;    {numerieke berekening}
           deltaPerc : Real);   {verschilperc. anal. en numerieke ber.}
{Deze procedure voert de berekende waarden naar het scherm uit.
Gebruikt de unit Crt.}

BEGIN
  ClrScr;
  WriteLn;
  WriteLn;
  WriteLn(wit,'HOOGTE EN SNELHEID MAANLANDER BIJ UITSCHAKELEN MOTOR');
  WriteLn;
  WriteLn(wit,'Beginhoogte  : ',h0,' m');
  WriteLn(wit,'Beginsnelheid: ',v0,' m/s');
  WriteLn;
  WriteLn;
  WriteLn;
  WriteLn(wit,'BEREKENDE SNELHEDEN  OP HOOGTE ',xAfst,' m');
  WriteLn;
  Write  (wit,'ANALYTISCH                   : ');
  WriteLn(vDaalA,' m/s');
  Write  (wit,'NUMERIEK (',nInt:5,' INTERVALLEN) : ');
  WriteLn(vDaalN,' m/s');
  WriteLn(wit);
  Write  (wit,'PROCENTUEEL VERSCHIL         : ');
  WriteLn(deltaPerc,' %');
  WriteLn;
  WriteLn;
  WriteLn;
  Write (wit,'Druk op <enter> ...');
  ReadLn;
END;
{----------------------------------------------------------------------}

{--- Hoofdprogramma ---------------------------------------------------}

BEGIN
  (* GeefTestwaarden(beginhoogte, beginsnelheid, daalhoogte,
                  aantalIntervallen); *)
  LeesGegevensIn(beginhoogte, beginsnelheid, daalhoogte,
                   aantalIntervallen);
  daalsnelheidAnalyt:= SnelheidMaanlander
                              (beginsnelheid, beginhoogte - daalhoogte);
  BerekenSnelheidNumeriekMetAfwijking{procedureparameters};
  SchrijfWaardenNaarScherm
                (beginhoogte, beginsnelheid, daalhoogte,
                 daalsnelheidAnalyt, aantalIntervallen,
                 daalsnelheidNum, perc);
END.

