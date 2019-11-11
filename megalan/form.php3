<?
	include("admin/config.inc");
	require("admin/mails.inc");
	require("admin/layout.inc");
	require("admin/edit.inc");

        $conn = mysql_connect($DBHOST, $DBUSER, $DBPASS);
        echo(mysql_error());
        mysql_select_db('megalan');

	Function Fout($reden)
	{?>
        <tr bgcolor="000000">
          <td width="50%" align="left" valign="top" colspan="2"> <font color="ffffff" size="2" face="Verdana, Arial, Helvetica, sans-serif">Je  
            inschrijving is <b><font color="#FF0000">NIET</font></b> verzonden.  
            <br>Reden: <B><? echo "$reden"; ?></B><BR><BR>
            Controleer de velden en probeer opnieuw. </font>  
            <p>&nbsp;</p> 
          </td> 
        </tr> 
<?	}

	Function Ok($extra)
	{?>
        <tr bgcolor="000000">
          <td width="50%" align="left" valign="top" colspan="2">  
            <p><font color="#000000"><font color="ffffff" face="Verdana, Arial, Helvetica, sans-serif" size="2">Je  
              registratie is verstuurd!</font></font></p>
<?	if($extra)
	{?>
	    <P><font color="ff0000" face="Verdana, Arial, Helvetica, sans-serif" size="2">
	    Let op: MegaLAN is al vol! Je staat op de reservelijst. Als
iemand zich afmeldt, laten we het je weten en kom je in aanmerking om alsnog
geplaatst te worden.</P><font color="FFFFFF"><?
	}
	else
	{
?>

            <p><font color="ffffff" face="Verdana, Arial, Helvetica, sans-serif" size="2">
              Je bent ook direct geplaatst voor MegaLAN 14, maar onthoud,
              je inschrijving is pas definitief op het moment dat betaling is  
              geschied. Achter de knop Deelnemers kun je je status verifieeren;  
              wit is ingeschreven, groen is betaald en verzekerd van deelname.</font></p> 
            <p><font color="#000000" face="Verdana, Arial, Helvetica, sans-serif" size="2"><font color="ffffff">Betaling  
              kan zowel contant als per overmaking: <br> 
              het rekeningnummer is: 34.15.40.943, op naam van B. van Kuik te Rotterdam,<br> 
              o.v.v. MegaLAN 14 en je nickname. </font></font></p> 
            <p><font color="#000000" face="Verdana, Arial, Helvetica, sans-serif" size="2"><font color="ffffff">Contant  
              kan betaald worden bij de CIMDROOM kamer, kamer 2.17, Acedemieplein  
              (G.J. de Jonghweg 4-6).<br> 
              </font></font></p> 
            <p><font color="#000000" face="Verdana, Arial, Helvetica, sans-serif" size="2"><font color="ffffff">Betaling  
              aan de deur tijdens MegaLAN 14 kan ook, echter kosten bedragen dan  
              Fl. 35,- en een plek is niet gegarandeerd! </font></font> </p> 
            <p>&nbsp;</p> 
<?
	}
?>
          </td>
        </tr> 
<?	}

	Function PrintForm()
	{?>
	              <tr bgcolor="000000">
	                <td width="50%" align="left" valign="top" colspan="2"> 
	                  <form method=POST action="form.php3">
	                    <p><font face="Verdana, Arial, Helvetica" color="#000000" size="4"> 
	                      <font size="2" color="ffffff" face="Verdana, Arial, Helvetica, sans-serif">
							<P>Hier registreer je je in de MegaLAN database. Indien mogelijk wordt
							je automatisch geplaatst voor MegaLAN 14.</P>
	                      Bij inschijving gaat de deelnemer automatisch accoord met de
	                      door ons gestelde voorwaarden, op te vragen onder de link 
	                      <a href="http://www.megalan.nl/regels.html">regels</a>.<br>
	                      Je staat pas definitief ingeschreven op het moment dat je 
	                      betaling door ons ontvangen is. <B>De kosten bedragen Fl. 25,- per deelnemer, per computer</B>. Betaling kan zowel contant 
	                      als per overmaking: <br>
	                      het rekeningnummer is: 34.15.40.943, op naam van B. van Kuik te Rotterdam, o.v.v. MegaLAN 14 en 
	                      je nickname. Contant kan betaald worden bij de CIMDROOM 
	                      kamer, kamer 2.17, Acedemieplein (G.J. de Jonghweg 4-6).<br>
	                      Betaling aan de deur tijdens MegaLAN 14 kan ook, echter 
	                      kosten bedragen dan Fl. 35,- en een plek is niet gegarandeerd! 
	                      </font></font> </p>
	                    <table width="33%" border="0" height="436" cellpadding="2" cellspacing="2">
	
	                      <tr align="left" valign="top">
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2"> 
	                          Nickname</font></td>
	                        <td width="77%"> <font face="Verdana, Arial, Helvetica, sans-serif"> 
	                          <input type="text" name="nickname" size="30" maxlength="35">
	                          </font></td>
	                      </tr>

	                      <tr align="left" valign="top">
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2">
	                          Wachtwoord</font></td>
	                        <td width="77%"> <font face="Verdana, Arial, Helvetica, sans-serif">
	                          <input type="PASSWORD" name="password1" size="30" maxlength="35">
	                          </font></td>
	                      </tr>

	                      <tr align="left" valign="top">
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2">
	                          Wachtwoord<BR>opnieuw</font></td>
	                        <td width="77%"> <font face="Verdana, Arial, Helvetica, sans-serif">
	                          <input type="PASSWORD" name="password2" size="30" maxlength="35">
	                          </font></td>
	                      </tr>

	                      <tr align="left" valign="top">
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2"> 
	                          Voornaam</font></td>
	                        <td width="77%"> <font face="Verdana, Arial, Helvetica, sans-serif"> 
	                          <input type="text" name="voornaam" size="30" maxlength="99">
	                          </font></td>
	                      </tr>
	
	                      <tr align="left" valign="top">
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2"> 
	                          Achternaam</font></td>
	                        <td width="77%"> <font face="Verdana, Arial, Helvetica, sans-serif"> 
	                          <input type="text" name="achternaam" size="30" maxlength="99">
	                          </font></td>
	                      </tr>
	
	                      <tr align="left" valign="top">
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2"> 
	                          Adres</font></td>
	                        <td width="77%"> <font face="Verdana, Arial, Helvetica, sans-serif"> 
	                          <input type="text" name="adress" size="30" maxlength="99">
	                          </font></td>
	                      </tr>
	
	                      <tr align="left" valign="top">
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2"> 
	                          Postcode</font></td>
	                        <td width="77%"> <font face="Verdana, Arial, Helvetica, sans-serif"> 
	                          <input type="text" name="postcode" size="30" maxlength="8">
	                          </font></td>
	                      </tr>
	
	                      <tr align="left" valign="top">
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2"> 
	                          Woonplaats</font></td>
	                        <td width="77%"> <font face="Verdana, Arial, Helvetica, sans-serif"> 
	                          <input type="text" name="woonplaats" size="30" maxlength="40">
	                          </font></td>
	                      </tr>
	
	                      <tr align="left" valign="top">
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2"> 
	                          Telefoon</font></td>
	                        <td width="77%"> <font face="Verdana, Arial, Helvetica, sans-serif"> 
	                          <input type="text" name="telefoon" size="30" maxlength="40">
	                          </font></td>
	                      </tr>
	
	                      <tr align="left" valign="top">
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2"> 
	                          Email</font></td>
	                        <td width="77%"> <font face="Verdana, Arial, Helvetica, sans-serif"> 
	                          <input type="text" name="email" size="30" maxlength="99">
	                          </font></td>
	                      </tr>
	
	                      <tr align="left" valign="top">
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2">Snelheid</font></td>
	                        <td width="77%"> <font color="ffffff" face="Verdana, Arial, Helvetica, sans-serif" size="2"> 
	                          <select name="speed">
	                            <option>10Mbps </option>
	                            <option>100Mbps </option>
	                          </select>
	                          UTP </font> </td>
	                      <tr align="left" valign="top"> 
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2">Zaal</font></td>
	                        <td width="77%"> <font color="ffffff" face="Verdana, Arial, Helvetica, sans-serif" size="2"> 
	                          <select name="zaal">
	                            <option>Speakers</option>
	                            <option>Koptelefoons</option>
	                          </select>
	                          </font></td>
	                      </tr>
	                      <tr align="left" valign="top"> 
	                        <td width="23%"> <font face="Verdana, Arial, Helvetica, sans-serif" color="ffffff" size="2"> 
	                          Opmerkingen</font></td>
	                        <td width="77%"> <font size="2" face="Verdana, Arial, Helvetica, sans-serif"> 
	                          <textarea name="comments" cols="30" rows="5" wrap="VIRTUAL"></textarea>
	                          </font></td>
	                      </tr>
	                      <tr align="left" valign="top"> 
	                        <td width="23%" height="17">&nbsp;</td>
	                        <td width="77%"> <font color="#000000" size="2" face="Verdana, Arial, Helvetica, sans-serif"> 
	                          <input type="submit" value="Verzenden" name="submit">
	                          </font></td>
	                      </tr>
	                    </table>
	                    <p>&nbsp;</p>
	                    </form>
	                  <p>&nbsp;</p>
	                </td>
	              </tr>

<?	}

if($submit)
{	$stop = 0;
	
	/* eerst kijken offie er al instaat */
	if(!$nickname || !$voornaam || !$achternaam || !$email || !$adress || !$postcode || !$woonplaats || !$password1 || !$password2 )
	{	echo(PrintUpper("Fout"));
		Fout("het invullen van je nickname, wachtwoord, voor- en achternaam, adres, postcode woonplaats en Email is verplicht.");
		echo(PrintLower());
		echo "</BODY></HTML>";
		$stop = 1;
	}
	else if($password1 != $password2)
		{	echo(PrintUpper("Fout"));
			Fout("Je wachtwoorden komen niet overeen");
			echo(PrintLower());
			echo "</BODY></HTML>";
			$stop = 1;
		}
	else	if( !CheckEmail($email) )
		{	echo(PrintUpper("Fout"));
			Fout("je hebt een ongeldig e-mail adres opgegeven.");
			echo(PrintLower());
			echo "</BODY></HTML>";
			$stop = 1;
		}
	if( !$stop )
	{
		/* eerst kijken of we al over de MAXDEELNEMERS zitten */
		$query = "select nickname from deelnemers where geplaatst = 1;";
		if( !($result = mysql_query($query)) )
			mysql_error();	/* print evt. errors */
		else
		{	$count = mysql_num_rows($result);
			if($count >= $MAXDEELNEMERS) $geplaatst = 0;
			else $geplaatst = 1;
			$query = "select nickname from deelnemers where nickname = '$nickname';";
			$result = mysql_query($query);
			if( mysql_num_rows($result) > 0 )
			{	echo(PrintUpper("Fout"));
				Fout("Er staat al iemand geregistreerd onder de nickname $nickname");
				echo(PrintLower());
				echo "</BODY></HTML>";
			}
			else
			{	if( $speed == "100Mbps" ) $speed = 100;
				else $speed = 10;
				$query = "insert into deelnemers values('$nickname',
		'$voornaam', '$achternaam', '$adress', '$postcode', '$woonplaats',
		'$telefoon', '', '$email', $speed, '$zaal', '$comments', 0, 1, $geplaatst, 0,
'$password1', NULL);";
				if( !mysql_query($query))
					mysql_error();
				else
				{	if($geplaatst == 1) {
						echo(PrintUpper("OK"));
						Ok("");
						echo(PrintLower());
						echo "</BODY></HTML>";
					}
					else {
						echo(PrintUpper("OK"));
						Ok("niet geplaatst");
						echo(PrintLower());
						echo "</BODY></HTML>";
					}

					/* nu nog ff mailen naar staff, kan die um "aanzetten" */
					$message = StaffMail($nickname, $voornaam, $achternaam, $adress, $postcode, $woonplaats, $telefoon, $email, $speed, $zaal, $comments, $geplaatst);
					exec("echo \"$message\" | sendmail -fwebsite@megalan.nl staff@megalan.nl");
					/* staff notified */

					/* nu een bevestiging naar de deelnemer */
					$message = Bevestiging($nickname, $password1, $voornaam, $achternaam, $adress, $postcode, $woonplaats, $telefoon, $email, $speed, $zaal, $comments, $geplaatst);
					exec("echo \"$message\" | sendmail -fstaff@megalan.nl $email");
					/* deelnemer gemaild */
				}
			}
		}
	}
}
else
{	echo(PrintUpper("Registreren"));
	PrintForm();
	echo(PrintLower());
	echo "</BODY></HTML>";
}

?>
