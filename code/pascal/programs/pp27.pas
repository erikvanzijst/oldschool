program PP27_VrijeVal;
{Dit programma berekent de valtijd bij een vrije val zonder beginsnel-
heid uit de valhoogte. Luchtwrijving wordt verwaarloosd.
Programmeurs      : medewerkers NP
Laatste wijziging : 14 augustus 1995
Verbeterd door    :
Datum             :                                                   }

const
  gGrav= 9.81;    {gravitatieversnelling in m/s^2 op 61 graden N.Br.}
var Valhoogte,        {valhoogte in m}
Valtijd: real;        {valtijd in s}

begin
Write('De valhoogte in m is: ');
ReadLn(Valhoogte);
valtijd:= Sqrt(2*Valhoogte/gGrav);   {formule voor de valtijd}
WriteLn('De valtijd in s is:  ',Valtijd);
WriteLn('Druk op Enter...');
ReadLn;     {stopt programma tot entertoets wordt ingedrukt}
end.
