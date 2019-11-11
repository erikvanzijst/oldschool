<?
	require("admin/layout.inc");
	require("admin/config.inc");
	require("admin/edit.inc");

	$conn = mysql_connect($DBHOST, $DBUSER, $DBPASS);
	echo(mysql_error());
	mysql_select_db('megalan');
	echo(mysql_error());

	if($opslaan == "opslaan") {
		if(!$nickname || !$password1 || !$voornaam || !$achternaam || !$adress || !$postcode || !$woonplaats || !$telefoon || !$email) {
			echo(PrintUpper("Fout"));
			echo("<TR><TD><P>".PrintFont()."Het invullen van wachtwoord, voor- en achternaam, adres, postcode, woonplaats, telefoonnr en email adres is verplicht.</P>
				Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om de velden te controleren.</TD></TR>\n");
			echo(PrintLower());
			echo "</BODY></HTML>";
			exit();
		}

		if($password1 != $password2) {
			echo(PrintUpper("Fout"));
			echo("<TR><TD><P>".PrintFont()."Je wachtwoorden komen niet overeen.</P>
				Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om de velden te controleren.</TD></TR>\n");
			echo(PrintLower());
			echo "</BODY></HTML>";
			exit();
		}

		if(!CheckEmail($email)) { /* ongeldig email adres ingevoerd */
			echo(PrintUpper("Fout"));
			echo("<TR><TD><P>".PrintFont()."Ongeldig Email adres ingevoerd.</P>
				Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om de velden te controleren.</TD></TR>\n");
			echo(PrintLower());
			echo "</BODY></HTML>";
			exit();
		}
	
		if( strstr($snelheid, "100")) $speed = "100";
		else $speed = "10";
		if( strstr($zaal, "peaker")) $zaal = "Speakers";
		else $zaal = "Koptelefoons";
		
		$query = "update deelnemers set wachtwoord = '".$password1."', voornaam = '".$voornaam."', achternaam = '".$achternaam."', straat = '".$adress."', postcode = '".$postcode."', woonplaats = '".$woonplaats."', telefoon = '".$telefoon."', telefoon2 = '".$telefoon2."', email = '".$email."', snelheid = ".$speed.", zaal = '".$zaal."', opmerkingen = '".$comments."' where nickname = '".$nickname."'";
		mysql_query($query, $conn);
		echo(mysql_error());
		echo(PrintUpper("OK"));
		echo("<TR><TD><P>".PrintFont()."Je nieuwe gegevens zijn opgeslagen.</P>
			Klik <A HREF=\"login.php3\">hier</A> om terug te gaan naar het login scherm.</TD></TR>\n");
		echo(PrintLower());
		echo "</BODY></HTML>";
		exit();
	}

?>
