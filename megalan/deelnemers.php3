<?
	require("admin/config.inc");
	require("admin/layout.inc");
	require("admin/popup1.inc");
	
        $conn = mysql_connect($DBHOST, $DBUSER, $DBPASS);
        echo(mysql_error());
        mysql_select_db('megalan');


	include "admin/javascript.inc";
	echo( PrintUpper("Deelnemers") );

        $query = "select nickname, betaald from deelnemers where geplaatst = 1 order by nickname";
        echo(mysql_error());
        $result = mysql_query($query, $conn);
        echo(mysql_error());


	if( mysql_num_rows($result) == 0 )
		echo "<TR><TD align=\"right\" colspan=2><font color=\"ffffff\"><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\">Er zijn nog geen inschrijvingen voor MegaLAN 12<BR></TD></TR>";
	else
	{
?>
            	<tr><td align="left"><font color="ffffff"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">
		<A HREF="login.php3" ONMOUSEOVER="javascript:window.status='Schrijf je in of uit door hier in te loggen.'; toggle_on('loginpopup');">Inloggen</A>
	</td>
	<td align="right"><font color="ffffff"><font face="Verdana, Arial, Helvetica, sans-serif" size="2">
<?		$count = mysql_num_rows($result);
		echo "$count/$MAXDEELNEMERS deelnemers</td></tr>";

                echo "<TR><TD width=\"50%\" align=\"left\" valign=\"top\">\n<FONT COLOR=\"FFFFFF\" FACE=\"Verdana, Helvetica, Arial\" SIZE=2>\n<OL>\n";
		for( $nX = 0; $nX < $count/2; $nX++ )
		{	echo "<P>";
			$record = mysql_fetch_object($result);
                        if( $record->betaald == 1 ) /* betaald :) */
                               echo "<LI><FONT COLOR=\"00FF00\" FACE=\"Verdana, Helvetica, Arial\" SIZE=2>$record->nickname<FONT COLOR=\"#FFFFFF\">\n";
                        else
                                echo "<LI><FONT COLOR=\"FFFFFF\" FACE=\"Verdana, Helvetica, Arial\" SIZE=2>$record->nickname\n";
			echo "</P>\n";
                }
		$OLStart=$nX+1;
		echo "</TD><TD width=\"50%\" align=\"left\" valign=\"top\"><FONT COLOR=\"FFFFFF\" FACE=\"Verdana, Helvetica, Arial\" SIZE=2>\n<OL START=".$OLStart.">\n";
                for( $nX = $count/2; $nX < $count; $nX++ ) /* de rest */
		{	echo "<P>";
			if( $record = mysql_fetch_object($result) )
                        {	if( $record->betaald == 1 ) /* betaald :) */
                                	echo "<LI><FONT COLOR=\"00FF00\" FACE=\"Verdana, Helvetica, Arial\" SIZE=2>$record->nickname<FONT COLOR=\"#FFFFFF\">\n";
   	                     else
        	                        echo "<LI><FONT COLOR=\"FFFFFF\" FACE=\"Verdana, Helvetica, Arial\" SIZE=2>$record->nickname\n";
				echo "</P>\n";
			}
		}
		echo "</OL></TD></TR>\n";
	}
?>
              <tr>
                <td colspan="3">
                  <div align="right">
                    <font size="1" color="00ff00" face="Verdana, Arial, Helvetica, sans-serif">(groen is betaald)</font><br>
                    <font color="ffffff" face="Verdana, Arial, Helvetica, sans-serif" size="1">(wit is ingeschreven)<br>
                    <A HREF="http://icehawk.megalan.nl/~icehawk/megalan/admin/deelnemer.php3">Deelnemer admin</A><br>
                  </div>
                </td>
              </tr>
<?
	echo (PrintLower()); 
	include "admin/loginpopup.inc";
	echo "<A NAME=\"leeg\"> </A>\n";
	echo "</BODY></HTML>";
?>
