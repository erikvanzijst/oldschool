<?
	require("admin/config.inc");

	function PrintLogin()
	{	$output = "

<TR><TD COLSPAN=\"5\">".PrintFont()."<P><FONT COLOR=\"FF0000\">Registreren</P>
<FONT COLOR=\"FFFFFF\">
<P>Als je je wilt inschrijven voor MegaLAN ".$EDITION.", moet je je eerst eenmalig registreren in
onze database.<BR>
Het voordeel is dat je bij de volgende editie van MegaLAN niet opnieuw het form hoeft in te
vullen, maar direct met je wachtwoord kunt inschrijven. Je gegevens blijven bewaard.<BR></P></TD></TR>

<TR><TD COLSPAN=5><HR></TD></TR>

<FORM METHOD=\"POST\" ACTION=\"form.php3\">
<TR><TD WIDTH=5> </TD><TD COLSPAN=3>".PrintFont()."
Ik sta nog niet in de database.</TD>
<TD>".PrintFont()."<INPUT TYPE=\"SUBMIT\" NAME=\"fromLogin\" VALUE=\"registreer\"></TD></TR>
</FORM>

<TR><TD COLSPAN=5><HR></TD></TR>

<TR><TD WIDTH=5> </TD><TD COLSPAN=4>".PrintFont()."
<FORM METHOD=\"POST\" ACTION=\"login.php3\">
<B>In- en uitschrijven</B> (je hebt al een login en wachtwoord)</TD></TR>
<TR><TD WIDTH=5> </TD> <TD WIDTH=5> </TD><TD>".PrintFont()."Login (nickname):</TD>
  <TD COLSPAN=2>".PrintFont()."<INPUT TEXT=\"TEXT\" NAME=\"login\" MAXLENGTH=20></TD></TR>
<TR><TD WIDTH=5> </TD> <TD WIDTH=5> </TD><TD>".PrintFont()."Wachtwoord:</TD>
  <TD COLSPAN=2>".PrintFont()."<INPUT TYPE=\"PASSWORD\" NAME=\"password\" MAXLENGTH=20></TD></TR>
<TR><TD WIDTH=5> </TD> <TD WIDTH=5> </TD><TD COLSPAN=3>".PrintFont()."
  <INPUT TYPE=\"RADIO\" NAME=\"action\" VALUE=\"inschrijven\" CHECKED>inschrijven</RADIO><BR>
  <INPUT TYPE=\"RADIO\" NAME=\"action\" VALUE=\"uitschrijven\">uitschrijven</RADIO></TD></TR>
<TR><TD WIDTH=5> </TD> <TD WIDTH=5> </TD><TD COLSPAN=2 VALIGN=\"TOP\">".PrintFont()."
  <INPUT TYPE=\"RADIO\" NAME=\"action\" VALUE=\"wijzigen\">gegevens wijzigen</RADIO></TD>
  <TD>".PrintFont()."<INPUT TYPE=\"submit\" NAME=\"submit\" VALUE=\"enter\"></TD></TR>
</FORM>

<TR><TD COLSPAN=5><HR></TD></TR>

<TR><TD COLSPAN=5>".PrintFont()."<P>
Ben je je wachtwoord vergeten, vul je nickname in en hij wordt je onmiddellijk toegemaild.<BR>
<FORM METHOD=\"POST\" ACTION=\"login.php3\">
Nickname: <INPUT NAME=\"nickname\" TYPE=\"TEXT\" SIZE=10> <INPUT TYPE=\"SUBMIT\" VALUE=\"mail wachtwoord\" NAME=\"mailpw\">
<BR></P></TD></TR>


";
	
		return $output;
	}

?>
