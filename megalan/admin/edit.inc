<?
	function CheckEmail($email) {
		$email = trim($email);
		$tok = strtok($email, "@");
		if(!$tok) return 0;
		$tok = strtok(".");
		if(!$tok) return 0;
		$tok = strtok(".");
		if(!$tok) return 0;
		return 1;
	}

	function MakeUserEdit($record, $staff)
	{
		if(strstr($record->zaal, "peakers"))
			$speed = "100";
		else $speed = "10";

		echo("<TR><TD COLSPAN=\"2\">\n"); echo(PrintFont());
		echo("<P>Hieronder staan de gegevens van <B>".$record->nickname."</B>.<BR>\n Druk na het wijzigen op opslaan.</P><BR>\n</TD></TR>");

		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("<FORM METHOD=\"POST\" ACTION=\"deelnemer.php3\">\n");
		echo("Nickname:</TD><TD>".PrintFont()."<INPUT TYPE=\"HIDDEN\" NAME=\"nickname\" VALUE=\"".$record->nickname."\">".$record->nickname."</TD></TR>\n");

		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Wachtwoord:</TD><TD><INPUT TYPE=\"PASSWORD\" NAME=\"password1\" SIZE=\"30\" MAXLENGTH=\"35\" VALUE=\"".$record->wachtwoord."\"></TD></TR>\n");
		
		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Wachtwoord<BR>opnieuw:</TD><TD><INPUT TYPE=\"PASSWORD\" NAME=\"password2\" SIZE=\"30\" MAXLENGTH=\"35\" VALUE=\"".$record->wachtwoord."\"></TD></TR>\n");
		
		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Voornaam:</TD><TD><INPUT TYPE=\"text\" NAME=\"voornaam\" SIZE=\"30\" MAXLENGTH=\"35\" VALUE=\"".$record->voornaam."\"></TD></TR>\n");

		echo("<TR><TD>\n");
        echo(PrintFont());
		echo("Achternaam:</TD><TD><INPUT TYPE=\"text\" NAME=\"achternaam\" SIZE=\"30\" MAXLENGTH=\"35\" VALUE=\"".$record->achternaam."\"></TD></TR>\n");

		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Straat:</TD><TD><INPUT TYPE=\"text\" NAME=\"adress\" SIZE=\"30\" MAXLENGTH=\"35\" VALUE=\"".$record->straat."\"></TD></TR>\n");

		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Postcode:</TD><TD><INPUT TYPE=\"text\" NAME=\"postcode\" SIZE=\"30\" MAXLENGTH=\"35\" VALUE=\"".$record->postcode."\"></TD></TR>\n");

		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Woonplaats:</TD><TD><INPUT TYPE=\"text\" NAME=\"woonplaats\" SIZE=\"30\" MAXLENGTH=\"35\" VALUE=\"".$record->woonplaats."\"></TD></TR>\n");

		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Telefoon:</TD><TD><INPUT TYPE=\"text\" NAME=\"telefoon\" SIZE=\"30\" MAXLENGTH=\"35\" VALUE=\"".$record->telefoon."\"></TD></TR>\n");

		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Telefoon 2:</TD><TD><INPUT TYPE=\"text\" NAME=\"telefoon2\" SIZE=\"30\" MAXLENGTH=\"35\" VALUE=\"".$record->telefoon2."\"></TD></TR>\n");

		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("E-mail:</TD><TD><INPUT TYPE=\"text\" NAME=\"email\" SIZE=\"30\" MAXLENGTH=\"35\" VALUE=\"".$record->email."\"></TD></TR>\n");

		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Snelheid:</TD><TD><SELECT NAME=\"snelheid\"><OPTION");
		if($record->snelheid == 10) echo(" SELECTED");
		echo(">10Mbps</OPTION><OPTION");
		if($record->snelheid == 100) echo(" SELECTED");
		echo(">100Mbps</OPTION></SELECT>".PrintFont()."UTP</TD>");

		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Zaal:</TD><TD><SELECT NAME=\"zaal\"><OPTION");
		if(strstr($record->zaal, "peaker")) echo(" SELECTED");
		echo(">Speakers</OPTION><OPTION");
		if(strstr($record->zaal, "telefoon")) echo(" SELECTED");
		echo(">Koptelefoons</OPTION></SELECT>\n");

		echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Opmerkingen:</TD><TD><TEXTAREA NAME=\"comments\" cols=\"30\" rows=\"5\" wrap=\"VIRTUAL\">".$record->opmerkingen."</TEXTAREA></TD></TR>\n");

		if($staff == "1") /* extra velden voor admins */
		{
			echo("<TR><TD>\n");
			echo(PrintFont());
			echo("Geplaatst:</TD><TD><INPUT TYPE=\"CHECKBOX\" NAME=\"geplaatst\" VALUE=\"checked\"");
			if($record->geplaatst == 1) echo(" CHECKED");
			echo(">".PrintFont()."Geplaatst voor komende MegaLAN</TD></TR>");
		
			echo("<TR><TD>\n");
			echo(PrintFont());
			echo("Betaald:</TD><TD><INPUT TYPE=\"CHECKBOX\" NAME=\"betaald\" VALUE=\"checked\"");
			if($record->betaald == 1) echo(" CHECKED");
			echo(">".PrintFont()."Deze deelnemer heeft betaald</TD></TR>");
		}

		echo("<TR><TD> </TD>");
		echo("<TD>".PrintFont()."<INPUT TYPE=\"SUBMIT\" NAME=\"opslaan\" VALUE=\"opslaan\"></TD></TR>");
		
	}

?>
