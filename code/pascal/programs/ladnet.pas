PROGRAM VervangingsweerstandLaddernetwerk;
{Met dit programma kan de weerstand van een Ladder-netwerk uitgerekend worden.
Programmeur       : Erik van Zijst
Laatste wijziging : 14 november 1995
}

USES
  Crt;

VAR
  weerstand: Real;
  vervangingsweerstand: Real;
  aantal: Integer;
  teller: Integer;

BEGIN
  ClrScr;
  Write('Geef nu de weestandswaarde van 1 weerstand in Ohm : ');
  ReadLn(weerstand);
  Write('Geef nu het aantal weerstanden die in het laddernetwerk zitten: ');
  ReadLn(aantal);
  vervangingsweerstand:= weerstand;
  FOR teller:= 2 to aantal DO
    BEGIN
      IF Odd(teller)
      THEN
      vervangingsweerstand:= 1 / ((1 / weerstand) + (1 / vervangingsweerstand))
      ELSE
      vervangingsweerstand:= weerstand + vervangingsweerstand;
    END;
  WriteLn('De vervangingsweerstand is: ',vervangingsweerstand);
  ReadLn;
END.