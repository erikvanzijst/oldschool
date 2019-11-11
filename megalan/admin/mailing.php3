<?
	require("layout.inc");
	require("config.inc");

$IedereenText ="Beste NICKNAME,\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'--8<---cut here-----\\n' +\n" .
		"\t'Typ hier je mail. Als je NICKNAME gebruikt, wordt dat automatisch vervangen \\n' +\n" .
		"\t'door iemands nickname. Zo lijken de mails persoonlijker (dit werkt niet in \\n' +\n" .
		"\t'het subject).\\n' +\n" .
		"\t'  Deze mail gaat naar iedereen in de database.\\n' +\n" .
		"\t'--8<---cut here-----\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'Reageren kan via email op staff@megalan.nl\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'Met vriendelijke groet,\\n' +\n" .
		"\t'MegaLAN organisatie.\\n' +\n" .
		"\t'http://www.megalan.nl";

$DeelnemersText = "Beste NICKNAME,\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'--8<---cut here-----\\n' +\n" .
		"\t'Typ hier je mail. Als je NICKNAME gebruikt, wordt dat automatisch vervangen \\n' +\n" .
		"\t'door iemands nickname. Zo lijken de mails persoonlijker (dit werkt niet in \\n' +\n" .
		"\t'het subject).\\n' +\n" .
		"\t'  Dit zijn de mensen die op de deelnemerspage staan.\\n' +\n" .
		"\t'--8<---cut here-----\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'Reageren kan via email op staff@megalan.nl\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'Met vriendelijke groet,\\n' +\n" .
		"\t'MegaLAN organisatie.\\n' +\n" .
		"\t'http://www.megalan.nl";

$BetaaldText = "Beste NICKNAME,\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'--8<---cut here-----\\n' +\n" .
		"\t'Typ hier je mail. Als je NICKNAME gebruikt, wordt dat automatisch vervangen \\n' +\n" .
		"\t'door iemands nickname. Zo lijken de mails persoonlijker (dit werkt niet in \\n' +\n" .
		"\t'het subject).\\n' +\n" .
		"\t'  Deze mensen hebben al betaald (vezekerd van een plaats).\\n' +\n" .
		"\t'--8<---cut here-----\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'Reageren kan via email op staff@megalan.nl\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'Met vriendelijke groet,\\n' +\n" .
		"\t'MegaLAN organisatie.\\n' +\n" .
		"\t'http://www.megalan.nl";

$NietBetaaldText = "Beste NICKNAME,\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'--8<---cut here-----\\n' +\n" .
		"\t'Typ hier je mail. Als je NICKNAME gebruikt, wordt dat automatisch vervangen \\n' +\n" .
		"\t'door iemands nickname. Zo lijken de mails persoonlijker (dit werkt niet in \\n' +\n" .
		"\t'het subject).\\n' +\n" .
		"\t'  Deze mensen hebben nog _NIET_ betaald.\\n' +\n" .
		"\t'--8<---cut here-----\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'Reageren kan via email op staff@megalan.nl\\n' +\n" .
		"\t'\\n' +\n" .
		"\t'Met vriendelijke groet,\\n' +\n" .
		"\t'MegaLAN organisatie.\\n' +\n" .
		"\t'http://www.megalan.nl";


	if($versturen)
	{
		if(!$message || $message == "")
		{
			echo(PrintUpper("Spam-O-Matic"));
			echo("\t\t<TR><TD>".PrintFont()."\t\tJe hebt geen tekst getypt om te versturen.<BR>\n");
			echo("\t\tKlik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om terug te gaan naar de Spam-O-Matic</TD></TR>.\n");
			echo(PrintLower());
			echo "</BODY></HTML>";
			exit();
		}
		if(!$subject || $subject == "")
		{
			echo(PrintUpper("Spam-O-Matic"));
			echo("\t\t<TR><TD>".PrintFont()."\t\tJe hebt geen subject gegeven.<BR>\n");
			echo("\t\tKlik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om terug te gaan naar de Spam-O-Matic</TD></TR>.\n");
			echo(PrintLower());
			echo "</BODY></HTML>";
			exit();
		}
		if(!$aan || ($aan != "iedereen" && $aan != "deelnemers" && $aan != "betaald" && $aan != "nietbetaald"))
		{
			echo(PrintUpper("Spam-O-Matic"));
			echo("\t\t<TR><TD>".PrintFont()."\t\tJe hebt geen geadresseerden geselecteerd.<BR>\n");
			echo("\t\tKlik <A HREF=\"JavaScript:history.go(-1)\">hier</A> om terug te gaan naar de Spam-O-Matic</TD></TR>.\n");
			echo(PrintLower());
			echo "</BODY></HTML>";
			exit();
		}

		$mailmessage = "Subject: ".$subject."\n\n".$message;
		$conn = mysql_connect($DBHOST, $DBUSER, $DBPASS);
		echo(mysql_error());
		mysql_select_db("megalan");
		
		$query = "select nickname, email from deelnemers";
		
		if($aan == "deelnemers")
			$query = $query . " where geplaatst = 1";
		else if($aan == "betaald")
			$query = $query . " where betaald = 1";
		else if($aan == "nietbetaald")
			$query = $query . " where betaald = 0 and geplaatst = 1";
		
//		$query = "select nickname, email from deelnemers where nickname = 'Icehawk' or nickname = 'Frits'";
		echo("<HTML>\n<TITLE>MegaLAN Spam-O-Matic</TITLE>\n");
		echo("\n<!-- php3 - MySQL - Linux - apache -->\n\n");
		echo("<BODY VLINK=\"#FF0000\" LINK=\"#FF0000\" ALINK=\"#FFFF00\" BGCOLOR=000000>".PrintFont());
		
		$result = mysql_query($query, $conn);
		echo(mysql_error());
		
		echo("<P><FONT SIZE=3><B>Mailing wordt verstuurd</B></P>\n");
		echo("<FONT SIZE=2><B>Bericht:</B><BR>\n");
		echo("<HR><FONT FACE=\"Courier\"><PRE>\n".$mailmessage."\n</PRE><HR><BR>".PrintFont());
		echo("<B>Het bericht wordt nu verstuurd aan ".mysql_num_rows($result)." mensen:</B><BR><OL>\n");
		
		while($record = mysql_fetch_object($result))
		{	echo("<LI><FONT COLOR=\"009F9C\"><B>$record->nickname</B><FONT COLOR=\"FFFFFF\">: ($record->email) ... ");
			$message2 = ereg_replace("NICKNAME", $record->nickname, $message);
			flush();
			mail($record->email, $subject, $message2, "From: staff@megalan.nl\nReply-to: staff@megalan.nl");
			echo("done<BR>\n");
//			sleep(1);
		}
		echo("</OL>\n");
		echo("<P><A HREF=\"index.php3\">Terug</A> naar het admin menu</P>\n");
		echo("</BODY></HTML>");
		mysql_close($conn);	/* gewoon netjes */
	}
	
	else
	{	?>
<SCRIPT LANGUAGE="JavaScript">
<!--
/**********************************************************
JavaScript voor de dynamische content van de mailtekst.

Autheur:		Erik van Zijst, icehawk@megalan.nl
Laatste wijziging:	26-juli-1999
Laatste wijziging door:	Erik van Zijst
**********************************************************/

IedereenText = '<? echo($IedereenText); ?>';

DeelnemersText = '<? echo($DeelnemersText); ?>';

BetaaldText = '<? echo($BetaaldText); ?>';

NietBetaaldText = '<? echo($NietBetaaldText); ?>';

function StartFunction(thisForm)	/* run deze onload */
{
	thisForm.subject.focus();	/* cursor op de subject line zetten */
	Iedereen(thisForm);	/* en tekstvak vullen */
}

function Iedereen(thisForm)
{
	if((thisForm.message.value == '') || (thisForm.message.value == DeelnemersText) || (thisForm.message.value == BetaaldText) || (thisForm.message.value == NietBetaaldText))
		thisForm.message.value = IedereenText;
}

function Deelnemers(thisForm)
{
	if((thisForm.message.value == '') || (thisForm.message.value == IedereenText) || (thisForm.message.value == BetaaldText) || (thisForm.message.value == NietBetaaldText))
		thisForm.message.value = DeelnemersText;
}

function Betaald(thisForm)
{
	if((thisForm.message.value == '') || (thisForm.message.value == IedereenText) || (thisForm.message.value == DeelnemersText) || (thisForm.message.value == NietBetaaldText))
		thisForm.message.value = BetaaldText;
}

function NietBetaald(thisForm)
{
	if((thisForm.message.value == '') || (thisForm.message.value == IedereenText) || (thisForm.message.value == DeelnemersText) || (thisForm.message.value == BetaaldText))
		thisForm.message.value = NietBetaaldText;
}

function Validate(thisForm)
{
	if(thisForm.message.value.length == 0)
	{
		alert("Je hebt geen tekst getypt om te versturen.\n" +
		"Het heeft weinig zin om een lege mail te versturen.");
		thisForm.message.focus();	/* cursor in het tekstvak plaatsen */
		return false;
	}
	else if(thisForm.subject.value.length == 0)
	{
		alert("Je hebt geen subject voor de mail getypt.\n" +
		"Typ even een subject, staat zo dom anders.");
		thisForm.subject.focus();	/* cursor op de subject line zetten */
		return false;
	}
	else
		return true;
}
//-->
</SCRIPT>
		<?
		echo(PrintHead("Spam-O-Matic", "StartFunction(document.forms[0])"));
		?>
		<TR><TD COLSPAN=2><? echo(PrintFont()); ?>		<P>
		Hier kun je een mail maken die wordt verstuurd aan iedereen, of aan een selectie
		van personen uit de database.</P>
		<HR>
		Mail bericht:<BR></TD></TR>
		<FORM ONSUBMIT="return Validate(HoofdForm)" NAME="HoofdForm" METHOD="POST" ACTION="mailing.php3">
		<TR><TD VALIGN="CENTER"><? echo(PrintFont()) ?>		Aan:</TD>
		<TD><? echo(PrintFont()) ?>
			<INPUT TYPE="RADIO" NAME="aan" VALUE="iedereen" ONCLICK="Iedereen(HoofdForm);" CHECKED>&nbsp Iedereen in de database<BR>
			<INPUT TYPE="RADIO" NAME="aan" VALUE="deelnemers" ONCLICK="Deelnemers(HoofdForm);">&nbsp Deelnemers huidige MegaLAN<BR>
			<INPUT TYPE="RADIO" NAME="aan" VALUE="betaald" ONCLICK="Betaald(HoofdForm);">&nbsp Deelnemers die al betaald hebben<BR>
			<INPUT TYPE="RADIO" NAME="aan" VALUE="nietbetaald" ONCLICK="NietBetaald(HoofdForm);">&nbsp Deelnemers die nog niet betaald hebben<BR>
		</TD></TR>
		<TR><TD VALIGN="CENTER"><? echo(PrintFont()) ?>		Subject:</TD><TD><FONT SIZE=2><INPUT TYPE="TEXT" NAME="subject" VALUE="Subject" SIZE="57" MAXLENGTH="70">
		</TD></TR>
		<TR><TD COLSPAN=2><FONT SIZE=2><TEXTAREA COLS="63" ROWS="18" NAME="message"></TEXTAREA><BR>
		<BR><INPUT TYPE="SUBMIT" NAME="versturen" VALUE="versturen">
		</TD></TR>
<?
		echo(PrintLower());
		echo "</BODY></HTML>";
	}
?>
