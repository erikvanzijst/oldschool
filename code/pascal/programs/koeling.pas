PROGRAM Afkoeling;
{Dit programma berekent de temperatuur van een afkoelend voorwerp als
functie van de tijd.
Raadpleeg de bij dit programma horende opdracht.
Er wordt een tabel afgedrukt waarin met tijdstappen van een halve tijd-
constante tijden en temperaturen worden gegeven.
Aan het programma ontbreekt de procedure waarin de temperaturen numeriek
berekend worden.
Gebruikt de unit Crt.
Programmeurs      : Medewerkers NP.
Laatste wijziging : 14 juni 1995.
Gecompleteerd door: Erik van Zijst
Datum             : 17 december 1995                                      }

USES
  Crt;

CONST
  rijMax    = 100;
  begintijd =   0;  {van afkoelingsproces}
  factor    =   5;  {factor * tijdconstante = tijdsduur}
  floatveld =  11;  {aantal velden bij floating point notatie}
  veld      =   6;  {aantal velden bij decimale notatie}
  dec       =   2;  {aantal decimalen achter de komma}
  schmarge  =  16;  {schermmarge}

TYPE
  RealRij= ARRAY[0..rijMax] OF Real;

VAR
  volume         : Real;     {volume voorwerp in m^3}
  oppervlak      : Real;     {oppervlak voorwerp in mý}
  dichtheid      : Real;     {dichtheid in kg/m^3}
  soortwarmte    : Real;     {soortelijke warmte in J/(kg.K)}
  begintemp      : Real;     {temperatuur voorwerp in øC op t=0}
  omgevtemp      : Real;     {temperatuur omgeving in øC}
  warmteovcoeff  : Real;     {warmteoverdrachtco‰ffici‰nt voorwerp-lucht
                             in W/(mý.K)}
  aantalInterval : Integer;  {aantal intervallen}
  aantalStappen  : Integer;  {bij integratie binnen interval}
  tijdconst      : Real;     {voor het afkoelen karakteristieke tijdcon-
                             stante in s}
  eindtijd       : Real;     {periode van volgen afkoeling}
  tabeltijd      : RealRij;  {array met tabelwaarden van de tijd}
  tempAnalyt     : RealRij;  {array met analyt. berekende temperaturen}
  tempEuler      : RealRij;  {array met numeriek berekende temperaturen}
  keuzenr        : Integer;  {van menukaart}
  pauzetoets     : Char;


{----------------------------------------------------------------------}
FUNCTION Sp(n: Integer): STRING;
{Functie retourneert een string van n (n>=1) spaties.}

VAR
  i: Integer;
  wit: STRING;

BEGIN
  wit:= '';  {startwaarde: geen spaties}
  FOR i:= 1 TO n DO wit:= wit + ' ';
  Sp:= wit;
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
PROCEDURE GeefTestwaarden(VAR vol     : Real;      {volume voorwerp}
                          VAR opp     : Real;      {oppervlak voorwerp}
                          VAR rho     : Real;      {dichtheid}
                          VAR sw      : Real;      {soortelijke warmte}
                          VAR tempBeg : Real;      {begintemperatuur}
                          VAR tempOmg : Real;      {omgev. temperatuur}
                          VAR wocff   : Real;      {warmteov. co‰ff.}
                          VAR nInt    : Integer;   {aantal intervallen}
                          VAR nStap   : Integer);  {aantal stappen}
{Deze procedure vervangt in de testfase van het programma de invoer van
gegevens via het toetsenbord.}

BEGIN
  vol:=      0.12E-3;   {m^3}
  opp:=      0.023;     {mý}
  rho:=      1.0E+03;   {kg/m^3}
  sw:=       4.18E+03;  {J/(kg.K)}
  tempBeg:= 83;         {C}
  tempOmg:= 22;         {C}
  wocff:=   10.0;       {W/(mý.K)}
  nInt:=    10;
  nStap:=  100;
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
PROCEDURE LeesGegevensIn(VAR vol     : Real;      {                    }
                         VAR opp     : Real;      {                    }
                         VAR rho     : Real;      { zie voor betekenis }
                         VAR sw      : Real;      {                    }
                         VAR tempBeg : Real;      {     procedure      }
                         VAR tempOmg : Real;      {                    }
                         VAR wocff   : Real;      {  GeefTestwaarden   }
                         VAR nInt    : Integer;   {                    }
                         VAR nStap   : Integer);  {                    }
{Deze procedure laat de gebruiker de vereiste gegevens via het toetsen-
bord invoeren.
Gebruikt de globaal gedeclareerde constante schmarge.
Gebruikt de functie Sp en de unit Crt.}

BEGIN
  ClrScr;
  WriteLn(Sp(schmarge), 'Raadpleeg de opdracht bij dit programma.');
  WriteLn;
  WriteLn(Sp(schmarge), 'AFKOELING');
  WriteLn;
  WriteLn;
  WriteLn(Sp(schmarge), 'Gegevens voorwerp:');
  WriteLn(Sp(schmarge), '------------------');
  Write  (Sp(schmarge), 'Volume voorwerp in m^3                    : ');
  ReadLn(vol);
  Write  (Sp(schmarge), 'Oppervlak voorwerp in mý                  : ');
  ReadLn(opp);
  Write  (Sp(schmarge), 'Dichtheid in kg/m^3                       : ');
  ReadLn(rho);
  Write  (Sp(schmarge), 'Soortelijke warmte in J/(kg.K)            : ');
  ReadLn(sw);
  Write  (Sp(schmarge), 'Begintemperatuur voorwerp in øC           : ');
  ReadLn(tempBeg);
  WriteLn;
  WriteLn(Sp(schmarge), 'Overige gegevens:');
  WriteLn(Sp(schmarge), '-----------------');
  Write  (Sp(schmarge), 'Warmte-overdrachtco‰ffici‰nt in W/(mý.K)  : ');
  ReadLn(wocff);
  Write  (Sp(schmarge), 'Omgevingstemperatuur in øC                : ');
  ReadLn(tempOmg);
  Write  (Sp(schmarge), 'Aantal intervallen                        : ');
  ReadLn(nInt);
  Write  (Sp(schmarge), 'Aantal iteraties per interval             : ');
  ReadLn(nStap);
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
PROCEDURE EchoInvoer(VAR vol     : Real;      {                        }
                     VAR opp     : Real;      {                        }
                     VAR rho     : Real;      {   zie voor betekenis   }
                     VAR sw      : Real;      {                        }
                     VAR tempBeg : Real;      {       procedure        }
                     VAR tempOmg : Real;      {                        }
                     VAR wocff   : Real;      {    GeefTestwaarden     }
                     VAR nInt    : Integer;   {                        }
                     VAR nStap   : Integer);  {                        }
{Deze procedure toont de ingevoerde gegevens op het scherm.
Gebruikt de globale constanten schmarge en floatveld,
de globale variabele pauze-toets en de unit Crt.}

BEGIN
  ClrScr;
  WriteLn;
  WriteLn(Sp(schmarge), 'OVERZICHT INGEVOERDE GEGEVENS');
  WriteLn;
  WriteLn;
  WriteLn(Sp(schmarge), 'Gegevens voorwerp:');
  WriteLn(Sp(schmarge), '------------------');
  WriteLn(Sp(schmarge), 'Volume voorwerp                : ',
           vol:floatveld,' m^3');
  WriteLn(Sp(schmarge), 'Oppervlak voorwerp             : ',
           opp:floatveld,' mý');
  WriteLn(Sp(schmarge), 'Dichtheid voorwerp             : ',
           rho:floatveld,' kg/m^3');
  WriteLn(Sp(schmarge), 'Soortelijke warmte             : ',
           sw:floatveld,' J/(kg.K)');
  WriteLn(Sp(schmarge), 'Begintemperatuur voorwerp      : ',
           tempBeg:floatveld,' øC');
  WriteLn;
  WriteLn(Sp(schmarge), 'Overige gegevens:');
  WriteLn(Sp(schmarge), '-----------------');
  WriteLn(Sp(schmarge), 'Omgevingstemperatuur           : ',
           tempOmg:floatveld,' øC');
  WriteLn(Sp(schmarge), 'Warmte-overdrachtco‰ffici‰nt   : ',
           wocff:floatveld,' W/(mý.K)');
  WriteLn(Sp(schmarge), 'Aantal intervallen             :  ',
           nInt);
  WriteLn(Sp(schmarge), 'Aantal iteraties per interval  :  ',
           nStap);
  WriteLn;
  WriteLn;
  Write(Sp(schmarge), 'Druk op een toets ... ');
  pauzetoets:= ReadKey;
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
PROCEDURE VerschafTijdsduren
          (    sw      : Real;  {soortelijke warmte}
               rho     : Real;  {dichtheid}
               vol     : Real;  {volume}
               wocff   : Real;  {warmte-overdrachtco‰ffici‰nt}
               opp     : Real;  {oppervlak}
           VAR tcnst   : Real;  {tijdconstante van afkoelingsproces}
           VAR tijdvak : Real); {waarin afkoeling gevolgd wordt}
{Deze procedure verschaft de grootheden tijdconstante en tijdvak, deze
grootheden bepalen samen met begintijd de duur van het volgen van het
afkoelingsproces.
Gebruikt de globale constante factor.}

BEGIN
  tcnst:= (sw*rho*vol)/(wocff*opp);
  tijdvak:= factor*tcnst;
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
FUNCTION AnalytischeTemperatuur(tijd      : Real;
                                tempBegin : Real;  {begintemperatuur}
                                tempOmg   : Real;  {omgevingstemp.}
                                tau       : Real)  {tijdconstante}
                                          : Real;
{De oplossing van de differentiaalvergelijking die de afkoeling be-
schrijft luidt

  T = Tomg + (Tbegin - Tomg).exp(-tijd/tau),

waarin T: temperatuur,
       Tbegin: begintemperatuur,
       Tomg: omgevingstemperatuur
       tau: tijdconstante.

Deze functie retourneert de analytisch berekende temperatuur.}

BEGIN
  AnalytischeTemperatuur:=   tempOmg
                           + (tempBegin-tempOmg) * Exp(-tijd/tau);
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
PROCEDURE BerekenTemperaturenAnalytisch
                 (    tBegin     : Real;      {startpunt in de tijd}
                      tEind      : Real;      {eindpunt in de tijd}
                      tempBegin  : Real;      {begintemperatuur}
                      tempOmg    : Real;      {temperatuur omgeving}
                      tau        : Real;      {tijdconstante}
                      aantal     : Integer;   {intervallen}
                  VAR tijdstip   : RealRij;   {van gegevensopslag}
                  VAR temperatuur: RealRij);  {analytisch berekende
                                              temperaturen}

{In deze procedure wordt op een aantal tijdstippen de temperatuur analy-
tisch berekend. Tijdstippen en temperaturen worden in een array opgesla-
gen.
Gebruikt de globaal gedeclareerde constante
  rijMax,
en het globaal gedeclareerde type
  RealRij met definitie RealRij= ARRAY[0..rijMax] OF Real;}

VAR
  i            : Integer;
  tijd         : Real;
  tijdinterval : Real;

BEGIN
  tijdinterval:= (tEind-tBegin)/aantal;
  FOR i:= 0 TO aantal DO
    BEGIN
      tijd:= tBegin + i * tijdinterval;
      tijdstip[i]:= tijd;
      temperatuur[i]:= AnalytischeTemperatuur
                                  (tijd, tempBegin, tempOmg, tau);
    END;
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
PROCEDURE BerekenMetEuler (x0:            Real;
                           xLaatste:      Real;
                           y0:            Real;
                           aantalx:       Integer;
                           aantalIntergr: Integer;
                           VAR xWaarde:   RealRij;
                           VAR yWaarde:   RealRij);

{Raadpleeg voor de specificaties de opdracht bij dit programma.
Let op: de te schrijven euler-integratieprocedure moet algemeen toepas-
baar zijn en maakt dus geen gebruik van informatie van het afkoelings-
proces.
In deze procedure wordt uitgaande van beginwaarden x0 en y0 voor een
aantal x-waarden uit het interval (x0, xLaatste) de bijbehorende
y-waarde numeriek met behulp van de methode van Euler berekend.
De berekende x- en y-waarden worden in een array opgeslagen.
Voor de berekening wordt het totale x-interval opgedeeld in een aantal
gelijke deelintervallen. De y-waarde aan het eind van elk deelinterval
wordt met behulp van een aantal opeenvolgende integratiestappen geba-
seerd op de eulermethode berekend.
De vereiste specifieke functie voor de eulermethode wordt als aparte
functie Dydx in de procedure opgenomen. De voor dit programma benodigde
functie is al geprogrammeerd.
Er wordt geen vooraf vastgestelde nauwkeurigheid vereist.
Een eis aan de procedure is dat hij algemeen toepasbaar is, d.w.z. dat
hij na eventuele aanpassing van de functie ook in een ander programma
gebruikt kan worden. Gebruik dus identifiers die geen relatie met dit
programma hebben.

Voltooid door     : Erik van Zijst
Datum             : 17-12-1995                                         }

VAR
  i:          Integer;     {loop variabele 1}
  j:          Integer;     {loop variabele 2}
  h:          Real;        {Integratie-stap-grootte}
  xVoorlopig: Real;
  yVoorlopig: Real;


  {--------------------------------------------------------------------}
  FUNCTION Dydx(xVariabele : Real;  {algemene vorm van functie}
                yVariabele : Real)  {voor integratie met eulermethode}
                           : Real;
  {De algemene vorm van een eerste orde differentiaalvergelijking is
    f(x,y,dy/dx).
  De hier gebruikte specifieke eerste orde differentiaalvergelijking
  is van de vorm
     a.dy/dx + y = b,
  met als variabelen x en y en als constanten a(=tijdconst) en
  b(=omgevtemp).
  De functie retourneert het differentiaalquoti‰nt dy/dx.
  Maakt gebruik van de globale variabelen
    tijdconst en omgevtemp.
  Merk op dat in deze differentiaalvergelijking x niet voorkomt. De te
  programmeren procedure Eulermethode moet echter wel zo algemeen zijn,
  dat de procedure ook te gebruiken is voor differentiaalvergelijkingen,
  waarin x wel voorkomt.}

  BEGIN
    Dydx:= -(yVariabele - omgevtemp)/tijdconst;  {specifieke functie}
  END;
  {--------------------------------------------------------------------}

BEGIN
  xWaarde[0]:=x0;
  yWaarde[0]:=y0;
  xVoorlopig:=x0;
  yVoorlopig:=y0;
  h:=(xLaatste-x0)/(aantalx*aantalIntergr);
  FOR i:=1 TO aantalx DO
    BEGIN
      FOR j:=1 TO aantalIntergr DO
        BEGIN
          yVoorlopig:=yVoorlopig+Dydx(xVoorlopig, yVoorlopig)*h;
          xVoorlopig:=xVoorlopig+h;
        END;
      xWaarde[i]:=xVoorlopig;
      yWaarde[i]:=yVoorlopig;
    END;
END;

{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
PROCEDURE SchrijfTabel(aantal : Integer;   {intervallen}
                       tijd   : RealRij;
                       temp1  : RealRij;   {anal. berekende temp.}
                       temp2  : RealRij);  {num. berekende temp.}
{Deze procedure drukt een tabel met tijden en temperaturen af.
Gebruikt de unit Crt.
Gebruikt de globaal gedeclareerde constanten rijMax, factor en schmarge;
het globaal gedeclareerde type
  RealRij met definitie RealRij= ARRAY[0..rijMax] OF Real
de globaal gedeclareerde variabele pauzetoets en de functie Sp.}

VAR
  i: Integer;

PROCEDURE SchrijfTabelkop;

BEGIN
  WriteLn;
  WriteLn(Sp(schmarge), '    OVERZICHT BEREKENDE RESULTATEN   ');
  WriteLn;
  WriteLn;
  WriteLn(Sp(schmarge), '-------------------------------------');
  WriteLn(Sp(schmarge), '   tijd             temperatuur       ');
  WriteLn(Sp(schmarge), '    min                 øC            ');
  WriteLn(Sp(schmarge), '               analyt       numeriek  ');
  WriteLn(Sp(schmarge), '-------------------------------------');
END;

PROCEDURE SchrijfTabelvoet;

BEGIN
  WriteLn(Sp(schmarge), '-------------------------------------');
  WriteLn;
  Write(Sp(schmarge), 'Druk op een toets ... ');
  pauzetoets:= ReadKey;
END;

BEGIN
  ClrScr;
  SchrijfTabelKop;
  FOR i:= 0 TO aantal DO  {aantal afgedrukte regels = aantal + 1}
    BEGIN
      Write(Sp(schmarge));
      WriteLn(Sp(2), tijd[i]/60:veld:dec,  {tijd in min}
              Sp(6), temp1[i]:veld:dec,
              Sp(8), temp2[i]:veld:dec);
    END;
  SchrijfTabelvoet;
END;
{----------------------------------------------------------------------}

{----------------------------------------------------------------------}
PROCEDURE ToonMenukaart(VAR keuze : Integer);
{Deze procedure toont de menukaart en nodigt de gebruiker uit een keuze
te maken.}

CONST
  linkerMarge = 20;  {van menukaart}
  bovenMarge  =  8;  {van menukaart}

BEGIN
  GotoXY(linkerMarge, bovenMarge);
  WriteLn('MENU');
  GotoXY(linkerMarge, bovenMarge+2);
  WriteLn('1. Invoer testwaarden');
  GotoXY(linkerMarge, bovenMarge+3);
  WriteLn('2. Invoer gegevens');
  GotoXY(linkerMarge, bovenMarge+4);
  WriteLn('3. Echo van de invoer');
  GotoXY(linkerMarge, bovenMarge+5);
  WriteLn('4. Tabel');
  GotoXY(linkerMarge, bovenMarge+6);
  WriteLn('5. Einde');
  GotoXY(linkerMarge, bovenMarge+8);
  Write  ('Uw keuze is ... ');
  ReadLn(keuze);
END;
{----------------------------------------------------------------------}

{--- Hoofdprogramma ---------------------------------------------------}

BEGIN
  REPEAT
    ClrScr;
    ToonMenukaart(keuzenr);
    CASE keuzenr OF
      1: BEGIN
           ClrScr;
           GeefTestwaarden(volume, oppervlak, dichtheid, soortwarmte,
                           beginTemp, omgevTemp, warmteovcoeff,
                           aantalInterval, aantalStappen);
           GotoXY(20,12);
           WriteLn('De testwaarden zijn ingevoerd.');
           GotoXY(20,24);
           Write  ('Druk een toets in ... ');
           pauzetoets:= ReadKey;
         END;
      2: BEGIN
            LeesGegevensIn(volume, oppervlak, dichtheid, soortwarmte,
                           beginTemp, omgevTemp, warmteovcoeff,
                           aantalInterval, aantalStappen);
         END;
      3: BEGIN
           ClrScr;
           EchoInvoer(volume, oppervlak, dichtheid, soortwarmte,
                      beginTemp, omgevTemp, warmteovcoeff,
                      aantalInterval, aantalStappen);
         END;
      4: BEGIN
           ClrScr;
           VerschafTijdsduren(soortwarmte, dichtheid, volume,
                              warmteovcoeff, oppervlak,
                              tijdconst, eindtijd);
           BerekenTemperaturenAnalytisch
                  (begintijd, eindtijd, begintemp, omgevtemp,
                   tijdconst, aantalInterval,
                   tabeltijd, tempAnalyt);
           BerekenMetEuler(begintijd, eindtijd, begintemp,
                           aantalInterval, aantalStappen,
                           tabeltijd, tempEuler);
           SchrijfTabel(aantalInterval,
                        tabeltijd, tempAnalyt, tempEuler);
         END;
    END;
  UNTIL keuzenr=5;
END.