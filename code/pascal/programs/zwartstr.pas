     PROGRAM ZwarteStraler;
     {Dit programma berekent de door een zwarte straler ge‰mitteerde
     straling als functie van golflengte en temperatuur.
     De berekende emittanties worden in de vorm van een tabel getoond.
     Raadpleeg de bij dit programma horende opdracht.
     Programmeurs      : medewerkers NP.
     Laatste wijziging : 24 mei 1994.
     Voltooid door     : Erik van Zijst
     Datum             : 17 december 1995                                                 }

     USES
       Crt;

     CONST
       wit = '           ';

     VAR
       absTemp    : Real;  {absolute temperatuur in K}
       beginGolfl : Real;  {golflengte aan begin interval in m}
       eindGolfl  : Real;  {golflengte aan einde interval in m}
       deltaGolfl : Real;  {stapgrootte golflengte door interval in m}

     {--------------------------------------------------------------------}
     FUNCTION NanomtrNaarMtr(lengte: Real)
                                   : Real;

     BEGIN
       NanomtrNaarMtr:= lengte*1.0E-09;
     END;
     {--------------------------------------------------------------------}

     {--------------------------------------------------------------------}
     FUNCTION MtrNaarMicromtr(lengte: Real)
                                    : Real;

     BEGIN
       MtrNaarMicromtr:= lengte*1.0E+06;
     END;
     {--------------------------------------------------------------------}

     {--------------------------------------------------------------------}
     PROCEDURE GeefTestwaarden(VAR kelvintemp : Real;  {abs. temperatuur}
                               VAR labdaBegin : Real;  {begingolflengte}
                               VAR labdaEind  : Real;  {eindgolflengte}
                               VAR deltaLabda : Real); {golflengte-stap}
     {Deze procedure geeft waarden om het programma te testen. De procedure
     wordt in de definitieve vorm van het programma vervangen door
     LeesGegevensIn.}

     BEGIN
       kelvintemp:=      4000; {K}
       labdaBegin:=   200E-09; {m}
       labdaEind :=  2600E-09; {m}
       deltaLabda:=   200E-09; {m}
     END;
     {--------------------------------------------------------------------}

     {--------------------------------------------------------------------}
     PROCEDURE LeesGegevensIn(VAR kelvintemp : Real;   {zie voor betekenis}
                              VAR labdaBegin : Real;   {van de variabelen }
                              VAR labdaEind  : Real;   {   de procedure   }
                              VAR deltaLabda : Real);  {  GeefTestwaarden }
     {Deze procedure vraagt om gevevensinvoer en zet de ingevoerde golf-
     lengten in nm om in golflengten in m.}

     VAR
       labdaBegin_nano : Real;  {golflengte aan begin interval in nm}
       labdaEind_nano  : Real;  {golflengte aan einde interval in nm}
       deltaLabda_nano : Real;  {stapgrootte golflengte door interval in nm}

     BEGIN
       Write  ('Voer de gegevens voor het afdrukken ');
       WriteLn('van de emissietabel in.');
       WriteLn;
       Write('Absolute temperatuur (in K) : ');
       ReadLn(kelvintemp);
       WriteLn;
       Write('Beginwaarde van het golflengte-interval (in nm) : ');
       Readln(labdaBegin_nano);
       Write('Eindwaarde van het golflengte-interval (in nm)  : ');
       ReadLn(labdaEind_nano);
       Write('Stapgrootte door het interval (in nm)           : ');
       ReadLn(deltaLabda_nano);
       labdaBegin:= NanomtrNaarMtr(labdaBegin_nano);
       labdaEind:= NanomtrNaarMtr(labdaEind_nano);
       deltaLabda:= NanomtrNaarMtr(deltaLabda_nano);
     END;
     {--------------------------------------------------------------------}

     {--------------------------------------------------------------------}
     FUNCTION GeheleMacht(grondtal : Real;
                          exponent : Integer)
                                   : Real;
     {Functie retourneert grondtal tot de macht exponent (exponent groter
     dan of gelijk aan nul en geheel).}

     VAR
       macht  : Real;
       teller : Integer;

     BEGIN
       macht:=1;
       FOR teller:=1 TO exponent DO
         BEGIN
         macht:=macht*grondtal
         END;
       GeheleMacht:=macht;
     END;
     {--------------------------------------------------------------------}

     {--------------------------------------------------------------------}
     FUNCTION EmissieZwarteStraler(labda      : Real;  {golflengte in m}
                                   kelvintemp : Real)  {K}
                                              : Real;  {W/(m^2.m}
     {Functie retourneert de emittantie van een zwarte straler voor
     gegeven golflengte en absolute temperatuur.
     Raadpleeg de bij dit programma horende opdracht.}

     CONST
       h=6.62608E-34;
       c=2.99792458E+8;
       k=1.38066E-23;
       c1=2*Pi*h*c*c;
       c2=(h*c)/k;

     VAR
       labda5 : Real;

     BEGIN
       labda5:=GeheleMacht(labda,5);
       EmissieZwarteStraler:=c1/(labda5*(Exp(c2/(labda*kelvintemp))-1));
     END;
     {--------------------------------------------------------------------}

     {--------------------------------------------------------------------}
     PROCEDURE SchrijfTabel(kelvintemp : Real;         {zie voor betekenis}
                            labdaBegin : Real;         {van de variabelen }
                            labdaEind  : Real;         {   de procedure   }
                            deltaLabda : Real);        {  GeefTestwaarden }

     {Deze procedure geeft een tabel van de emittantie van een zwarte
     straler in 10 MW/(mý.æm) als functie van de golflengte in æm.}

     CONST
       factor = 1.0E-13;    {conversiefactor voor omrekening van emittantie
                             in W/(mý.m) naar emittantie in 10 MW/(mý.æm)}

     VAR
       golfl       : Real;  {golflengte straling zwarte straler}
       golfl_micro : Real;  {idem in micrometer}
       emittantie  : Real;  {emittantie zwarte straler in W/(mý.m)}
       emit        : Real;  {idem in 10 MW/(mý.æm)}

       PROCEDURE SchrijfTabelkop;

       {Deze procedure schrijft de kop van de emissietabel.}

       BEGIN
         WriteLn('        Emissie van een zwarte straler bij');
         WriteLn('        T=',kelvintemp:5:0,' K');
         WriteLn;
         WriteLn('           golfl          emittantie       ');
         WriteLn('            æm           10 MW/(mý.æm)       ');
         WriteLn;
       END;

     BEGIN
       ClrScr;
       SchrijfTabelkop;
       golfl:= labdaBegin;
       REPEAT
         golfl_micro:= MtrNaarMicromtr(golfl);
         emittantie:= EmissieZwarteStraler(golfl, kelvintemp);
         emit:= factor*emittantie;
         WriteLn(wit, golfl_micro:5:3, wit, emit:6:4);
         golfl:= golfl+deltaLabda;
       UNTIL golfl> labdaEind;
       WriteLn;
       WriteLn;
       WriteLn('        Druk een toets in.');
       REPEAT UNTIL KeyPressed;
     END;

     {---Hoofdprogramma---------------------------------------------------}

     BEGIN
       ClrScr;
       (* GeefTestwaarden(absTemp, beginGolfl, eindGolfl, deltaGolfl); *)
       LeesGegevensIn(absTemp, beginGolfl, eindGolfl, deltaGolfl);
       SchrijfTabel(absTemp, beginGolfl, eindGolfl, deltaGolfl);
     END.


