PROGRAM Orgeltje;
{Dit programma bootst een orgeltje na.
Als orgeltoetsen worden de numerieke toetsen 1 t/m 8 gebruikt.
Deze corresponderen met de toonladder c, d, e, f, g, a, b, c.
Programmeurs      : Medewerkers NP
Laatste wijziging : 3 maart 1995                                           }

USES
  Crt;

CONST
  duur = 250;           {van de toon in miiliseconden}

VAR
  toets      : Char;    {van toetsenbord, wordt gebruikt om luidspreker
                        toon te laten produceren of programma te beeindigen;
                        toegestane toetsen: 1 t/m 8 en 0.}
  freq       : Integer; {frequentie van door luidspreker geproduceerde toon}
  pauzetoets : Char;


{--------------------------------------------------------------------------}
PROCEDURE SchrijfTekstNaarScherm(naamFile: STRING);
{Deze procedure zet een met de pascaltekstverwerker gemaakte tekst op het
scherm. De file die naar het scherm uitgevoerd wordt is te kiezen door het
specificeren van de filenaam. Alleen door DOS toegestane tekens mogen in de
filenaam gebruikt worden.}

VAR
  tekst : Text;         {filevariabele voor naar scherm uit te voeren tekst}
  teken : Char;         {van naar scherm uit te voeren tekst}

BEGIN
  Assign(tekst, naamFile);
  Reset(tekst);
  REPEAT
    Read(tekst, teken);
    Write(teken);
  UNTIL Eof(tekst);
  Close(tekst);
END;
{--------------------------------------------------------------------------}

{--------------------------------------------------------------------------}
FUNCTION Frequentie(noot: Char): Integer;
{Functie retourneert de bij een noot horende frequentie.}

BEGIN
  CASE noot OF
    '1': Frequentie:= 264;   {c}
    '2': Frequentie:= 297;   {d}
    '3': Frequentie:= 330;   {e}
    '4': Frequentie:= 352;   {f}
    '5': Frequentie:= 396;   {g}
    '6': Frequentie:= 440;   {a}
    '7': Frequentie:= 495;   {b}
    '8': Frequentie:= 528;   {c}
    ELSE Frequentie:= 0;     {einde programma}
  END;
END;
{--------------------------------------------------------------------------}

{--------------------------------------------------------------------------}
PROCEDURE GeefToon(tijdsduur  : Integer;    {van de toon in ms}
                   frequentie : Integer);   {van de toon in Hz}
{Deze procedure laat de luidspreker een toon produceren.
De duur van de toon wordt bepaald door de variabele tijdsduur, de frequentie
(in Hz) door de variabele frequentie.}

BEGIN
  Sound(frequentie);                        {Sound is een procedure uit Crt}
  Delay(tijdsduur);                         {Delay is een procedure uit Crt}
  NoSound;                                {NoSound is een procedure uit Crt}
END;
{--------------------------------------------------------------------------}

{---Hoofdprogramma---------------------------------------------------------}

BEGIN
  ClrScr;                                  {ClrScr is een procedure uit Crt}
  SchrijfTekstNaarScherm('pp26.txt');
  toets:= '1';                                                 {startwaarde}
  REPEAT
    REPEAT
      toets:= ReadKey;                      {ReadKey is een functie uit Crt}
    UNTIL toets IN ['0'..'8'];
    freq:= Frequentie(toets);
    GeefToon(duur,freq);
  UNTIL toets= '0';
  ClrScr;
  GotoXY(16,12);                           {GotoXY is een procedure uit Crt}
  WriteLn('Dank voor uw muzikale bijdrage.');
  GotoXY(16,20);
  Write('Druk op een toets ...');
  pauzetoets:= ReadKey;      {voorkomt dat boodschap over het scherm flitst}
END.

