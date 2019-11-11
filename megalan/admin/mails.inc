<?
	require("admin/config.inc");

Function StaffMail ($nickname, $voornaam, $achternaam, $adress, $postcode, $woonplaats, $telefoon, $email, $snelheid, $zaal, $opmerkingen, $added)
{
$message = "Subject: WWW Form Submission\n\nNieuwe inschrijving van $nickname @ ".date("l dS of F Y h:i:s A")."

Gegevens:

  Nickname:	$nickname
  Email:	$email
  Voornaam:	$voornaam
  Achternaam:	$achternaam
  Straat:	$adress
  Postcode:	$postcode
  Woonplaats:	$woonplaats
  Telefoon:	$telefoon
  Snelheid:	$snelheid mbps
  Zaal:		$zaal
  Opmerkingen:	$opmerkingen


";

if($added == 1)
	$message = $message."De deelnemer staat al op de deelnemers page en mailinglist.\n";
else
	$message = $message."De deelnemer is niet toegevoegd aan de deelnemers page omdat we al vol zitten. Maar wel toegevoegd aan de mailinglist.\n";

$message = $message."De deelnemer heeft een bevestigingsmail gekregen.\n\n";

$message = $message."Ga naar
http://prutser.cx/~icehawk/megalan/admin/deelnemer.php3?deelnemer=".urlencode($nickname)."\n";
$message = $message."om aan te geven dat hij betaald heeft of om um van de deelnemerspage af te halen.\n\n--\n";

/* ff een vage fortune msg eronder prutten */
/*$fortune = passthru("fortune", $output_array);
$message = $message.$fortune;
*/
return $message;
}

Function Bevestiging ($nickname, $password, $voornaam, $achternaam, $adress, $postcode, $woonplaats, $telefoon, $email, $snelheid, $zaal, $opmerkingen, $added)
{
  $message = "Subject: Bedankt voor je aanmelding\n\nBedankt voor je inschrijving!

Je hebt je zojuist aangemeld voor MegaLAN ".$EDITION." en we hebben je geregistreerd met de volgende gegevens:

  Nickname:	$nickname
  Email:	$email
  Wachtwoord:	$password
  Voornaam:	$voornaam
  Achternaam:	$achternaam
  Straat:	$adress
  Postcode:	$postcode
  Woonplaats:	$woonplaats
  Telefoon:	$telefoon
  Snelheid:	$snelheid mbps
  Zaal:		$zaal
  Opmerkingen:	$opmerkingen

* Je gegevens worden aan niemand vrijgegeven en dienen alleen voor registratie zodat de organisatie eventueel contact met je op kan nemen.
Je zal nu ook af en toe de MegaLAN mailings ontvangen met informatie.

Bewaar de mail, je hebt je nickname en wachtwoord nodig bij het wijzigen van je gegevens en bij het inschrijven voor volgende MegaLANs.

";

if($added == 1)
  $message = $message."Hierbij ben je ook aangemeld voor MegaLAN ".$EDITION." en je naam is reeds toegevoegd op de deelnemers page.

Let op dat je op tijd betaalt. Aan de deur betalen kan, maar kost 35 ipv 25,-
Onthoud ook dat in geval van veel aanmeldingen een onbetaalde plek weggegeven kan worden aan iemand die wel direct betaalt.
Betalen via overmaking kan. Maak dan f25,- over op rekeningnummer 34.15.40.943 op naam van B. van Kuik, o.v.v. MegaLAN 14 en je nickname.

";
else
  $message = $message."MegaLAN ".$EDITION." is VOL en daarom ben je niet toegevoegd aan de deelnemers page. Je staat in onze database nu als reserve, zodat we je op de hoogte stellen als er zich iemand terug trekt en er een plek voor je is.
Ook zul je nu af en toe de MegaLAN mailings ontvangen via Email.

";

$message = $message."
Voor meer informatie of vragen kun je ons mailen op staff@megalan.nl

--
";

/* ff een vage fortune msg eronder prutten */
/*$fortune = passthru("fortune", $output_array);
$message = $message.$output_array;
*/
  return $message;
}
