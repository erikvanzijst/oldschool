
<!-- layer met het gele popup window erin -->
<!-- via cascading style sheets, geschikt voor NS4 en IE4 -->
<DIV ID="loginpopup" STYLE="position:absolute; top:43px; left:218px; visibility:hidden;">
<SCRIPT LANGUAGE="JavaScript1.2">
<!--

function login_t(thisForm)
{
	document.loginForm.submit();
}

//-->
</SCRIPT>
<TABLE BORDER=0 CELLPADDING=0 CELLSPACING=0 BACKGROUND="pics/spacer.gif">
<TR VALIGN=top>
	<TD ROWSPAN=3 BACKGROUND="pics/spacer.gif" VALIGN="TOP"><BR><BR>
		<IMG SRC="pics/bubbles-up.gif">
	</TD>
	<TD WIDTH=9 HEIGHT=10>
		<IMG SRC="pics/linksboven.gif" WIDTH=9 HEIGHT=10 BORDER=0><BR>
	</TD>
	<TD COLSPAN=2 BACKGROUND="pics/boven.gif" HEIGHT=10>
		<IMG SRC="pics/spacer.gif" WIDTH=1 HEIGHT=10 BORDER=0><BR>
	</TD>
	<TD WIDTH=10 HEIGHT=10>
		<IMG SRC="pics/rechtsboven.gif" WIDTH=10 HEIGHT=10 BORDER=0><BR>
	</TD>
</TR>
<TR VALIGN=top>
	<TD BACKGROUND="pics/links.gif" width=9>
		<IMG SRC="pics/spacer.gif" WIDTH=9 HEIGHT=1 BORDER=0>
	</TD>
	<TD WIDTH=100 HEIGHT=100 BGCOLOR="#EFEF52"><FONT FACE="Verdana, Arial, Helvetica" SIZE=2>
		<TABLE BORDER=0 BGCOLOR="#EFEF52">
			<TR>
				<TD COLSPAN=2><FONT FACE="Verdana, Arial, Helvetica" SIZE=2 COLOR="000000">
					<P><CENTER><H4>Inloggen</H1></CENTER></B>
				</TD>
			</TR>
			<TR>
				<FORM NAME="loginForm" METHOD="POST" ACTION="login.php3">
				<INPUT TYPE="HIDDEN" NAME="verstuur" VALUE="enter">
				<TD><FONT FACE="Verdana, Arial, Helvetica" SIZE=2><FONT COLOR="000000">
					Nickname:
				</TD>
				<TD><FONT FACE="Verdana, Arial, Helvetica" SIZE=2>
					<INPUT TEXT="TEXT" SIZE=10 NAME="login" MAXLENGTH=20>
				</TD>
			</TR>
			<TR>
				<TD><FONT FACE="Verdana, Arial, Helvetica" SIZE=2 COLOR="000000">
					Wachtwoord:
				</TD>
				<TD><FONT FACE="Verdana, Arial, Helvetica" SIZE=2>
					<INPUT TYPE="PASSWORD" SIZE=10 NAME="password" MAXLENGTH=20>
				</TD>
			</TR>
			<TR>
				<TD COLSPAN=2>

				<TABLE BORDER=0 WIDTH="100%">
				<TR>
				<TD><FONT COLOR="000000">
					<INPUT TYPE="RADIO" NAME="action" VALUE="inschrijven">&nbsp;inschrijven</RADIO><BR>
					<INPUT TYPE="RADIO" NAME="action" VALUE="uitschrijven">&nbsp;uitschrijven</RADIO><BR>
					<INPUT CHECKED TYPE="RADIO" NAME="action" VALUE="wijzigen">&nbsp;wijzigen</RADIO>
				</TD>
				<TD ALIGN="RIGHT" VALIGN="BOTTOM"><FONT FACE="Verdana, Arial, Helvetica" SIZE=2 COLOR="000000">
					<INPUT TYPE="BUTTON" VALUE="login" ONCLICK="login_t(loginForm);">
				</TD>
				</TR>
				</TABLE>

				</TD>
				</FORM>
			</TR>
		</TABLE>
	</TD>
	<TD BACKGROUND="pics/geel.gif" ALIGN="RIGHT">
		<A HREF="" ONCLICK="javascript:toggle_off('loginpopup'); return false;"
		ONMOUSEOVER="javascript:window.status='Sluit dit popup window.';">
		<IMG SRC="pics/sluiten.gif" BORDER=0></A>
	</TD>
	<TD BACKGROUND="pics/rechts.gif" WIDTH=10>
		<IMG SRC="pics/spacer.gif" WIDTH=10 HEIGHT=1 BORDER=0>
	</TD>
</TR>
<TR VALIGN=top>
	<TD WIDTH=9>
		<IMG SRC="pics/linksonder.gif" WIDTH=9 HEIGHT=10 BORDER=0><BR>
	</TD>
	<TD COLSPAN=2 BACKGROUND="pics/onder.gif">
		<IMG SRC="pics/spacer.gif" WIDTH=1 HEIGHT=10 BORDER=0><BR>
	</TD>
	<TD WIDTH=10>
		<IMG SRC="pics/rechtsonder.gif" WIDTH=10 HEIGHT=10 BORDER=0><BR>
	</TD>
</TR>
</TABLE>
</DIV>

