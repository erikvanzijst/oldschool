<?
	require("layout.inc");
	require("config.inc");
	$conn = mysql_connect($DBHOST, $DBUSER, $DBPASS);
	echo(mysql_error());
	mysql_select_db('megalan');

function addrow($col1, $col2)
{
	echo "<TR>\n\t<TD>\n".
		"\t\t$col1\n".
		"\t</TD>\n".
		"\t<TD>\n".
		"\t\t$col2\n".
		"\t</TD>\n".
		"</TD>\n";
}


?>
<HTML>
<HEAD>
<TITLE>MegaLAN statistieken</TITLE>

<!-- css voor kortere tabel code -->
<STYLE TYPE="text/css">
        TD {font-family: Verdana, Arial, Helvetica;}
        INPUT {font-family: Verdana, Arial, Helvetica;font-size: 10pt;}
        CENTER {font-family: Verdana, Arial, Helvetica;}
</STYLE>
</HEAD>

<BODY BGCOLOR="FFFFFF">
<FONT FACE="Verdana, Arial, Helvetica" SIZE="2">
<CENTER><H1>Statistieken</H1>
<BR><BR>

<TABLE BORDER=1>
<?

/* eerst totaal aantal deelnemers */
$result = mysql_query("select count(*) from deelnemers where geplaatst = '1'", $conn);
echo(mysql_error());
$count_all = mysql_result($result, 0, 0);
addrow("Totaal aantal deelnemers:", $count_all);

/* totaal aantal betaalde deelnemers */
$result = mysql_query("select count(*) from deelnemers where geplaatst = '1' and betaald = '1'", $conn);
echo(mysql_error());
$count_p = mysql_result($result, 0, 0);
$percent_p = $count_p / $count_all * 100;
addrow("Totaal betaalde deelnemers:", $count_p."&nbsp;(=".$percent_p."%)");

/* totaal aantal niet-betaalde deelnemers */
$result = mysql_query("select count(*) from deelnemers where geplaatst = '1' and betaald = '0'", $conn);
echo(mysql_error());
$count_np = mysql_result($result, 0, 0);
$percent_np = $count_np / $count_all * 100;
addrow("Totaal niet-betaalde deelnemers:", $count_np."&nbsp;(=".$percent_np."%)");

addrow("&nbsp;", "&nbsp;");

/* koptelefoons */
$result = mysql_query("select count(*) from deelnemers where geplaatst = '1' and zaal = 'Koptelefoons'", $conn);
echo(mysql_error());
$count_k = mysql_result($result, 0, 0);
$percent_k = $count_k / $count_all * 100;
addrow("Deelnemers met koptelefoon:", $count_k."&nbsp;(=".$percent_k."%)");

/* speakers */
$result = mysql_query("select count(*) from deelnemers where geplaatst = '1' and zaal = 'Speakers'", $conn);
echo(mysql_error());
$count_s = mysql_result($result, 0, 0);
$percent_s = $count_s / $count_all * 100;
addrow("Deelnemers met speakers:", $count_s."&nbsp;(=".$percent_s."%)");

addrow("&nbsp;", "&nbsp;");

/* 10mbps */
$result = mysql_query("select count(*) from deelnemers where geplaatst = 1 and snelheid = 10", $conn);
echo(mysql_error());
$count_10 = mysql_result($result, 0, 0);
$percent_10 = $count_10 / $count_all * 100;
addrow("Deelnemers op 10mbps:", $count_10."&nbsp;(=".$percent_10."%)");

/* 100mbps */
$result = mysql_query("select count(*) from deelnemers where geplaatst = 1 and snelheid = 100", $conn);
echo(mysql_error());
$count_100 = mysql_result($result, 0, 0);
$percent_100 = $count_100 / $count_all * 100;
addrow("Deelnemers op 100mbps:", $count_100."&nbsp;(=".$percent_100."%)");

addrow("&nbsp;", "&nbsp;");

/* aantal database entries */
$result = mysql_query("select count(*) from deelnemers", $conn);
echo(mysql_error());
$count_whole = mysql_result($result, 0, 0);
addrow("Totaal aantal mensen in de database:", $count_whole);

?>
</TABLE>

<FORM METHOD="GET" ACTION="stats.php3">
<INPUT TYPE="SUBMIT" VALUE="verversen">
</FORM>

</FONT>
</BODY>
</HTML>
