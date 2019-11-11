<?
	require("admin/layout.inc");
	require("admin/config.inc");
	require("admin/login.inc");
	require("admin/edit.inc");

	$stop == 0;
	$conn = mysql_connect($DBHOST, $DBUSER, $DBPASS);
	echo(mysql_error());
	mysql_select_db('megalan');
	echo(mysql_error());

	if($submit == "enter" || $verstuur == "enter") {

		$query = "select * from deelnemers where nickname = '$login'";
		$result = mysql_query($query, $conn);
		echo(mysql_error());
		
		if(mysql_num_rows($result) == 0) {
			$stop = 1;
			echo(PrintUpper("Fout"));
			echo("<TR><TD><P>".PrintFont()."Gebruiker <B>$login</B> niet gevonden.</P>
				Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om opnieuw aan te melden.</TD></TR>");
			echo(PrintLower());
			echo "</BODY></HTML>";
		}
		else {
			$record = mysql_fetch_object($result);
			if($record->wachtwoord != $password) {
				$stop = 1;
				echo(PrintUpper("Fout"));
				echo("<TR><TD><P>".PrintFont()."Onjuist wachtwoord voor gebruiker <B>$login</B>.</P>
					Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om opnieuw aan te melden.</TD></TR>");
				echo(PrintLower());
				echo "</BODY></HTML>";
			}
		}
		
		if(($action == "inschrijven")&&(!$stop)) {
			if($record->geplaatst == 1) {
				$stop = 1;
				echo(PrintUpper("Ingeschreven"));
				echo("<TR><TD><P>".PrintFont()."Je staat ingeschreven. Je naam staat nu op de deelnemers pagina</P>
					Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om terug te gaan naar het login scherm.</TD></TR>");
				echo(PrintLower());
				echo "</BODY></HTML>";
			}
			else {
				/* eerst kijken of er nog plek is */
				$query = "select nickname from deelnemers where geplaatst = 1";
				$result2 = mysql_query($query, $conn);
				echo(mysql_error());
				if(mysql_num_rows($result2) >= $MAXDEELNEMERS) { /* vol! */
					$stop = 1;
					echo(PrintUpper("Vol"));
					echo("<TR><TD><P>".PrintFont()."MegaLAN 14 is vol! Pas als er andere deelnemers uitschrijven kun je je inschrijven.</P>
						Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om terug te gaan naar het login scherm.</TD></TR>");
					echo(PrintLower());
					echo "</BODY></HTML>";
				}
				else { /* er is nog plek */
					$query = "update deelnemers set geplaatst = 1 where nickname = '$login'";
					$result = mysql_query($query, $conn);
					echo(mysql_error()); /* just in case */
					$stop = 1;
					echo(PrintUpper("Ingeschreven"));
					echo("<TR><TD><P>".PrintFont()."Je staat nu ingeschreven voor MegaLAN 14. Je naam staat nu op de deelnemers pagina</P>
						Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om terug te gaan naar het login scherm.</TD></TR>");
					echo(PrintLower());
					echo "</BODY></HTML>";
				}
			}
		}
		if(($action == "uitschrijven")&& !$stop) {
			if($record->geplaatst == 0) { /* je staat helemaal niet eens ingeschreven */
				$stop = 1;
				echo(PrintUpper("Uitgeschreven"));
				echo("<TR><TD><P>".PrintFont()."Je bent uitgeschreven voor MegaLAN 14.<P>
					Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om terug te gaan naar het login scherm.</TD></TR>");
				echo(PrintLower());
				echo "</BODY></HTML>";
			}
			else {
				$stop = 1;
				$query = "update deelnemers set geplaatst = 0 where nickname = '$login'";
				$result2 = mysql_query($query, $conn);
				echo(mysql_error());
				echo(PrintUpper("Uitgeschreven"));
				echo("<TR><TD><P>".PrintFont()."Je bent uitgeschreven voor MegaLAN 14.<P>
					Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om terug te gaan naar het login scherm.</TD></TR>");
				echo(PrintLower());
				echo "</BODY></HTML>";
			}
		}
		if(($action == "wijzigen")&& !$stop) {
			$stop = 1;
			echo(PrintUpper("Wijzigen"));
			echo("<FORM METHOD=\"POST\" ACTION=\"userEdit.php3\">\n");
			MakeUserEdit($record, "0");
			echo("</FORM>\n");
			echo(PrintLower());
			echo "</BODY></HTML>";
		}
	
		
	}
	else if($mailpw) {

		$query = "select nickname, email, wachtwoord from deelnemers where nickname = '$nickname'";
		$result = mysql_query($query, $conn);
		echo(mysql_error());
		
		if(mysql_num_rows($result) == 0) {
			$stop = 1;
			echo(PrintUpper("Login"));
			echo("<TR><TD><P>".PrintFont()."Gebruiker <B>$nickname</B> niet gevonden.
				<P>Controleer of je je nickname goed ingevuld hebt (let op, dit is niet case sensitive)
				<P>Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om terug te gaan.</TD></TR>");
			echo(PrintLower());
			echo "</BODY></HTML>";
		}
		else {
			$record = mysql_fetch_object($result);
			$adres = $record->email;
			
			$message = "Beste $record->nickname,

Je hebt op de MegaLAN website aangegeven dat je je wachtwoord kwijt was.

Je gegevens:

	Login (nickname):	$record->nickname
	Wachtwoord:		$record->wachtwoord

Met deze gegevens kun je je aanmelden op de MegaLAN website en je inschrijven voor de komende editie van MegaLAN.
Je doet dat door op de site te klikken op aanmelden, je nickname en wachtwoord te geven en aan te vinken dat je je in- of uit wil schrijven.
Ook kun je daar je gegevens zoals die in onze database staan wijzigen.

m.vr.gr,
organisatie MegaLAN
http://www.megalan.nl
";
			mail($adres, "Wachtwoord MegaLAN site", $message, "From: website@megalan.nl\nReply-To: staff@megalan.nl\nX-Mailer: PHP/".phpversion());
			echo(PrintUpper("Login"));
?>
<TR><TD><P><?PrintFont()?>
Je wachtwoord is naar je toe gemaild.<BR>
Hiervoor hebben we het adres gebruikt zoals je dat opgegeven hebt bij het registreren.</P>
</TD></TR>
<?			
			echo(PrintLower());
			echo "</BODY></HTML>";
		
		}
	}
	else {
		echo(PrintUpper("Login"));
		echo(PrintLogin());
		echo(PrintLower());
		echo "</BODY></HTML>";
	}

?>
