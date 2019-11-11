<?
	require("layout.inc");
	require("config.inc");

/***************************************
 Admin menu  -  Erik van Zijst  -  24.07.99
 **************************************/

	echo(PrintUpper("Administration"));
	echo("\t<TR><TD COLSPAN=3>".PrintFont()."\t<P>Dit is het beveiligde admin gedeelte van\n" .
	"\t de MegaLAN site.</P>\n" .
	"\tAchter \"Deelnemer admin\" kun je de gegevens van iedereen in de database opvragen\n " .
	"\ten wijzigen, je kunt een mail sturen naar alle deelnemers en de hele MegaLAN\n" .
	"\t database downloaden als excel file.<BR><HR></TD></TR>\n");
	
	echo("\t<TR><TD WIDTH=5> </TD>\n");
	echo("\t<TD VALIGN=\"CENTER\">".PrintFont()."\t<LI>Deelnemer administration</TD>\n");
	echo("\t<TD>".PrintFont()."\t<FORM ACTION=\"deelnemer.php3\" METHOD=\"POST\">".
	"<INPUT TYPE=\"SUBMIT\" NAME=\"submit\" VALUE=\"deelnemer admin\"></FORM></TD></TR>\n\n");

	echo("\t<TR><TD WIDTH=5> </TD>\n");
	echo("\t<TD VALIGN=\"CENTER\">".PrintFont()."\t<LI>Mailings sturen met de Spam-O-Matic</TD>\n");
	echo("\t<TD>".PrintFont()."\t<FORM ACTION=\"mailing.php3\" METHOD=\"POST\">".
	"<INPUT TYPE=\"SUBMIT\" NAME=\"submit\" VALUE=\"Spam-O-Matic\"></FORM></TD></TR>\n\n");

	echo("\t<TR><TD WIDTH=5> </TD>\n");
	echo("\t<TD VALIGN=\"CENTER\">".PrintFont()."\t<LI>MegaLAN statistieken</TD>\n");
	echo("\t<TD>".PrintFont()."\t<FORM ACTION=\"stats.php3\" METHOD=\"POST\">".
	"<INPUT TYPE=\"SUBMIT\" NAME=\"submit\" VALUE=\"stats\"></FORM></TD></TR>\n\n");
	
	echo("\t<TR><TD WIDTH=5> </TD>\n");
	echo("\t<TD VALIGN=\"CENTER\">".PrintFont()."\t<LI>Database downloaden</TD>\n");
	echo("\t<TD>".PrintFont()."\t<FORM ACTION=\"import.php3\" METHOD=\"POST\">".
	"<INPUT TYPE=\"SUBMIT\" NAME=\"submit\" VALUE=\"downloaden\"></FORM></TD></TR>\n\n");

	echo("\t<TR><TD COLSPAN=3><HR></TD></TR>\t<!-- horizontale lijn -->\n");

	echo(PrintLower());
	echo "</BODY></HTML>";
?>
