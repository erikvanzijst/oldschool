{$A+,B-,D+,E+,F-,G-,I+,L+,N+,O-,P-,Q+,R+,S+,T-,V+,X+,Y+}
{$M 16384,0,655360}
PROGRAM Massa_Energie_Conversie;
{Dit programma berekent met de formule E=mc^2 de energie die vrijkomt
bij de omzetting van massa in energie.
Er wordt een tabel getoond van massa en energie. Het massa-interval en
de stapgrootte, waarmee het interval doorlopen wordt, kunnen via het
toetsenbord ingevoerd worden.
Dit programma bevat twee opzettelijke fouten. Raadpleeg de bij dit
programma horende opdracht.
Gebruikt de unit Crt.
Programmeurs      : medewerkers NP.
Laatste wijziging : 31 mei 1995
Verbeterd door    : Erik van Zijst
Datum             : 17 december 1995                                                  }

USES
  Crt;

CONST
  lichtsnelheid = 2.9979E+08;   {m/s}
  rijMax        = 20;
  wit           = '          ';

TYPE
  RealRij = ARRAY[1..rijMax] OF Real;

VAR
  aantalBerekeningen : Integer;
  beginmassa         : Real;     {kg}
  eindmassa          : Real;     {kg}
  massatoename       : Real;     {kg}
  massa              : RealRij;  {in kg die omgezet wordt in energie}
  energie            : RealRij;  {in J na omzetting}


{---------------------------------------------------------------------}
FUNCTION MassaNaarEnergie(massa : Real)
                                : Real;
{Functie retourneert het energie-equivalent van een massa uitgaande van
de formule E=mc^2. Gebruikt de globale constante lichtsnelheid.}

BEGIN
  MassaNaarEnergie:= massa*Sqr(lichtsnelheid);
END;
{---------------------------------------------------------------------}

{---------------------------------------------------------------------}
PROCEDURE GeefStartwaarden;
{Geeft de volgende globale variabelen een startwaarde:
aantalBerekeningen, beginmassa, eindmassa, massatoename.
Verder krijgen alle geindiceerde variabelen massa en energie de waar-
de 0.}

VAR
  i : Integer;

BEGIN
  aantalBerekeningen:= 4;
  beginmassa:= 1.0E-12;
  eindmassa:= 9.5E-12;
  massatoename:= 1.0E-12;
  FOR i:= 1 TO rijMax DO
    BEGIN
      massa[i]:= 0;
      energie[i]:= 0;
    END;
END;
{---------------------------------------------------------------------}

{---------------------------------------------------------------------}
PROCEDURE LeesGegevensIn;
{Deze procedure vraagt om gegevensinvoer via het toetsenbord. De vol-
gende globale variabelen krijgen een waarde:
beginmassa, eindmassa, massatoename.}

BEGIN
  Write  ('Voer de gegevens voor het afdrukken van de');
  WriteLn('massa-energie conversietabel in:');
  WriteLn;
  Write  ('Beginmassa in kg                  : ');
  ReadLn(beginmassa);
  Write  ('Eindmassa in kg                   : ');
  ReadLn(eindmassa);
  Write  ('Massatoename in kg per tabelregel : ');
  ReadLn(massatoename);
END;
{---------------------------------------------------------------------}

{---------------------------------------------------------------------}
PROCEDURE BerekenEnergie(VAR mas     : RealRij;   {massa in kg}
                         VAR energ   : RealRij;   {energie in J}
                         VAR laatste : Integer);  {volgnummer}
{Deze procedure berekent voor een aantal massa's de energie die vrij-
komt als die massa omgezet wordt in energie. Massa's en energie‰n wor-
den opgeslagen in parallelle arrays. Het aantal massa-energie-paren
waarvoor de berekening is uitgevoerd, wordt opgeslagen in laatste.
Gebruikt de globaal gedefinieerde constanten rijMax, beginmassa,
eindmassa en deltamassa en het globaal gedefinieerde type RealRij met
als definitie RealRij= ARRAY[1..rijMax] OF Real;}

VAR
  i        : Integer;

BEGIN
  i:= 1;
  REPEAT
    mas[i]:= beginmassa + (i-1)*massatoename;
    energ[i]:= MassaNaarEnergie(mas[i]);
    i:= i+1;
  UNTIL (mas[i-1] > eindmassa) OR (i = rijMax);
  laatste:= i-1;
END;
{---------------------------------------------------------------------}

{---------------------------------------------------------------------}
PROCEDURE SchrijfTabel(laatste : Integer;   {volgnummer}
                       mas     : RealRij;   {massa in kg}
                       energ   : RealRij);  {energie in J}
{Deze procedure maakt op het scherm een tabel van de in twee parallelle
arrays opgeslagen massa-energie-paren.
Gebruikt de globaal gedefinieerde constanten
  rijMax en wit
en het globaal gedefinieerde type RealRij met als definitie
  RealRij= ARRAY[1..rijMax] OF Real;}

VAR
  i: Integer;

BEGIN
  ClrScr;
  Writeln;
  WriteLn;
  WriteLn(wit, ' MASSA-ENERGIE-OMZETTING');
  WriteLn;
  WriteLn(wit, '    massa        energie   ');
  WriteLn(wit, '      kg            J   ');
  WriteLn;
  FOR i:= 1 TO laatste DO
    WriteLn(wit, mas[i]:12,'  ',energ[i]:12 );
  WriteLn;
  Write  (wit, ' Druk een toets in ...');
  REPEAT UNTIL KeyPressed;
END;
{---------------------------------------------------------------------}

{---Hoofdprogramma----------------------------------------------------}

BEGIN
  ClrScr;
  GeefStartwaarden;
  (* LeesGegevensIn; *)
  BerekenEnergie(massa, energie, aantalBerekeningen);
  SchrijfTabel(aantalBerekeningen, massa, energie);;
END.
