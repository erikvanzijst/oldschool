<?
	require("layout.inc");
	require("config.inc");
	require("edit.inc");
        $conn = mysql_connect($DBHOST, $DBUSER, $DBPASS);
        echo(mysql_error());
        mysql_select_db('megalan');


if((!$deelnemer)&&(!$opslaan))
{
	echo (PrintUpper("Deelnemer-admin"));
	PrintFont();
	echo("<TR><TD COLSPAN=\"3\">".PrintFont()."<CENTER>Kies een MegaLAN ".$EDITION." deelnemer<BR></TD></TR><BR><BR>\n");

	/* eerst de geplaatste deelnemers printen in 3 kolommen */
	$query = "select nickname, betaald from deelnemers where geplaatst = 1 order by nickname";
	$result = mysql_query($query, $conn);
	echo(mysql_error());
	if(mysql_num_rows($result) == 0) /* geen geplaatste deelnemers */
	{	echo("<TR><TD>".PrintFont()."Er zijn nog geen deelnemers geplaatst voor MegaLAN ".$EDITION."</TR></TD>\n");
	}
	else /* maak 3 kolommen met deelnemers (groen als ze betaald hebben) */
	{	$count = mysql_num_rows($result);
		$rest = $count%3; /* mag dit? */
		$count = $count - $rest;
		$col1 = $col2 = $col3 = $count/3;
		if(($rest == 1) || ($rest == 2))
			$col1 = $col1 + 1;
		if($rest == 2)
			$col2 = $col2 + 1;
		echo("<TR><TD width=\"33%\" align=\"left\" valign=\"top\">\n");
		PrintFont();
		for($nX = 0; $nX < $col1; $nX++)
		{	echo"<P>\n";
                        if( $record = mysql_fetch_object($result) )
				echo("<A HREF=\"deelnemer.php3?deelnemer=".urlencode($record->nickname)."\">".$record->nickname."</A>\n");
			echo"</P>\n";
		}
		echo("</TD><TD width=\"33%\" align=\"left\" valign=\"top\">\n");
                PrintFont();
                for($nX = 0; $nX < $col2; $nX++)
		{	echo"<P>\n";
                        if( $record = mysql_fetch_object($result) )
				echo("<A HREF=\"deelnemer.php3?deelnemer=".urlencode($record->nickname)."\">".$record->nickname."</A>\n");
                        echo"</P>\n";
		}
                echo("</TD><TD width=\"33%\" align=\"left\" valign=\"top\">\n");
                PrintFont();
                for($nX = 0; $nX < $col3; $nX++)
		{	echo"<P>\n";
                        if( $record = mysql_fetch_object($result) )
				echo("<A HREF=\"deelnemer.php3?deelnemer=".urlencode($record->nickname)."\">".$record->nickname."</A>\n");
                        echo"</P>\n";
		}
                echo "</TD></TR>\n";
	}

	?><TR><TD COLSPAN=3><HR></TD></TR>
	<TR><TD COLSPAN=3><?echo(PrintFont())?>
	<CENTER>Hieronder staan alle overige personen in ooit in de database zijn gekomen
	</TD></TR><?

	$query = "select nickname from deelnemers where geplaatst = 0 order by nickname";
	$result = mysql_query($query, $conn);
	echo(mysql_error());
	$count = mysql_num_rows($result);
	$rest = $count%3; /* mag dit? */
	$count = $count - $rest;
	$col1 = $col2 = $col3 = $count/3;
	if(($rest == 1) || ($rest == 2))
		$col1 = $col1 + 1;
	if($rest == 2)
		$col2 = $col2 + 1;

        echo("<TR><TD width=\"33%\" align=\"left\" valign=\"top\">\n");
        PrintFont();
	for($nX = 0; $nX < $col1; $nX++)
	{	echo"<P>\n";
                if( $record = mysql_fetch_object($result) )
			echo("<A HREF=\"deelnemer.php3?deelnemer=".urlencode($record->nickname)."\">".$record->nickname."</A>\n");
		echo"</P>\n";
	}
	echo("</TD><TD width=\"33%\" align=\"left\" valign=\"top\">\n");
        PrintFont();
	for($nX = 0; $nX < $col2; $nX++)
	{	echo"<P>\n";
                if( $record = mysql_fetch_object($result) )
			echo("<A HREF=\"deelnemer.php3?deelnemer=".urlencode($record->nickname)."\">".$record->nickname."</A>\n");
		echo"</P>\n";
	}
	echo("</TD><TD width=\"33%\" align=\"left\" valign=\"top\">\n");
        PrintFont();
	for($nX = 0; $nX < $col3; $nX++)
	{	echo"<P>\n";
                if( $record = mysql_fetch_object($result) )
			echo("<A HREF=\"deelnemer.php3?deelnemer=".urlencode($record->nickname)."\">".$record->nickname."</A>\n");
		echo"</P>\n";
	}
        echo "</TD></TR>\n";

}
if(($deelnemer) && (!$opslaan)) /* weergeven om te editen */
{
	echo (PrintUpper($deelnemer));
	$query = "select * from deelnemers where nickname = '".$deelnemer."'";
	$result = mysql_query($query, $conn);
	echo(mysql_error());
	$record = mysql_fetch_object($result);
	if(mysql_num_rows($result) == 0)
	{	echo("<TR><TD>\n");
		echo(PrintFont());
		echo("Deelnemer ".$deelnemer." niet gevonden.<BR></TD></TR>\n");
	}
	else {
		echo("<FORM METHOD=\"POST\" ACTION=\"deelnemer.php3\">\n");
		MakeUserEdit($record, "1"); /* edit form weergeven voor admins */
		echo("</FORM>\n");
	}
}
if($opslaan) {
	if(!$nickname || !$password1 || !$voornaam || !$achternaam || !$adress || !$postcode || !$woonplaats || !$email) {
		echo(PrintUpper("Fout"));
		echo("<TR><TD><P>".PrintFont()."Het invullen van wachtwoord, voor- en achternaam, adres, postcode, woonplaats en email adres is verplicht.</P>
			Klik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om de velden te controleren.</TD></TR>\n");
		echo(PrintLower());
		echo "</BODY></HTML>";
		exit();
	}

	if($password1 != $password2) {
		echo(PrintUpper("Fout"));
		echo("<TR><TD><P>".PrintFont()."De wachtwoorden komen niet overeen.</P>
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
	if( $geplaatst == "checked") $geplaatst = "1";
	else $geplaatst = "0";
	if( $betaald == "checked") $betaald = "1";
	else $betaald = "0";
	
	$query = "update deelnemers set wachtwoord = '".$password1."', voornaam = '".$voornaam."', achternaam = '".$achternaam."', straat = '".$adress."', postcode = '".$postcode."', woonplaats = '".$woonplaats."', telefoon = '".$telefoon."', telefoon2 = '".$telefoon2."', email = '".$email."', snelheid = ".$speed.", zaal = '".$zaal."', opmerkingen = '".$comments."', geplaatst = ".$geplaatst.", betaald = ".$betaald." where nickname = '".$nickname."'";
	mysql_query($query, $conn);
	echo(mysql_error());
	echo(PrintUpper("OK"));
	echo("<TR><TD><P>".PrintFont()."De nieuwe gegevens zijn opgeslagen.</P>
		Klik <A HREF=\"deelnemer.php3\">hier</A> om terug te gaan naar de deelnemerslijst.</TD></TR>\n");
}

echo (PrintLower());
echo "</BODY></HTML>";
mysql_close($conn);
?>
