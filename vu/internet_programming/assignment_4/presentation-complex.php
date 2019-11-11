<?
include 'setup.php';	// for the base url

// These are some strings used on all pages
$title = "Ted's Teddy Toko";


/*
 * Below is the template for the index.php page. It doesn't have any dynamic
 * elements, but is defined here anyway to be consistent with the other pages
 * and to easily allow for dynamic elements in the future.
 */
$template_index = "
<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">
<html>
<head>

  <meta http-equiv=\"CONTENT-TYPE\"
 content=\"text/html; charset=iso-8859-15\">
  <title>$title</title>

  <meta name=\"GENERATOR\" content=\"StarOffice 6.0  (Linux)\">
  <meta name=\"AUTHOR\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CREATED\" content=\"20030510;23192100\">
  <meta name=\"CHANGEDBY\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CHANGED\" content=\"20030510;23273200\">

  <style>
	<!--
		@page { size: 21.59cm 27.94cm }
		TD P { color: #ffffff }
		TD P.western { font-family: sans-serif; so-language: en-US }
		H1 { color: #ffffff }
		H1.western { font-family: sans-serif; so-language: en-US }
		H1.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H1.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		P { color: #ffffff }
		P.western { font-family: sans-serif; so-language: en-US }
		H2 { color: #ffffff }
		H2.western { font-family: sans-serif; so-language: en-US }
		H2.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H2.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		TD P.next-western { font-family: sans-serif; so-language: en-US; text-align: right }
		TD P.next-cjk { text-align: right }
		TD P.next-ctl { text-align: right }
		A:link { color: #dc2300 }
		A:visited { color: #ff6633 }
	-->
	</style>
</head>
  <body lang=\"en-US\" text=\"#ffffff\" link=\"#dc2300\" vlink=\"#ff6633\"
 bgcolor=\"#33a3a3\">

<div align=\"center\"><img src=\"$BASECGI/adget.cgi?ad=ad1.gif\" name=\"Graphic1\" align=\"middle\"
 width=\"468\" height=\"60\" border=\"0\" ALT=\"advertisement\">
  <br>

<h1 class=\"western\">Ted's Teddy Toko</h1>
 </div>

<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"0\">
   	<col width=\"46*\"> 	<col width=\"210*\"> 	<thead> 		  	</thead> 	<tbody>
   		<tr valign=\"top\">
   			<td rowspan=\"4\" width=\"18%\">
      <p class=\"western\"><a href=\"$BASEPHP/index.php\"><img
 src=\"images/turhome.gif\" name=\"Home\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
  </a>     </p>

      <p class=\"western\"><font color=\"#000080\"><A HREF=\"$BASEPHP/index.php\">home</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"list.php\">products</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"admin/admin.php\">administration</A></font></p>
   				         			</td>
   			<td width=\"82%\"> 				              
      <h2 class=\"western\">Welcome</h2>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p><font face=\"Thorndale, serif\">On this page we sell teddy bears.<BR>
      Click <A HREF=\"list.php\">here</A> to be taken to the online store.</font></p>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <div align=\"justify\"> 				       </div>

      <p align=\"justify\"><font face=\"Thorndale, serif\">This site is our submission
for homework assignment 4 of the Internet Programming course at the <i>Vrije</i>
Universiteit in Amsterdam, the Netherlands. The document describing the assignment
can be found <A HREF=\"4.assignment.ps\">here</A>. For further information about the course, visit the course's
homepage at </font><A HREF=\"http://www.cs.vu.nl/~gpierre/courses/ip/\">http://www.cs.vu.nl/~gpierre/courses/ip/</A><font
 face=\"Thorndale, serif\">.<br>
       </font></p>

      <p><font face=\"Thorndale, serif\"><br>
       </font></p>

      <div align=\"right\"><font face=\"Thorndale, serif\">Erik van Zijst - <font
 color=\"#dc2300\"><a href=\"mailto:erik@prutser.cx\">erik@prutser.cx</a></font>
- 1351745</font><br>
       <font face=\"Thorndale, serif\">Sander van Loo - <a
 href=\"mailto:sander@marketxs.com\">sander@marketxs.com</a> - 1351753</font><br>
       </div>

      <div align=\"right\">  			</div>
       </td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p class=\"western\"><img src=\"images/turup.gif\" name=\"Top\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
        </p>
   			</td>
   		</tr>

  </tbody>
</table>
     
<p style=\"margin-bottom: 0cm;\"><br>
   </p>
<center>
<p style=\"margin-bottom: 0cm;\"><font face=\"Thorndale, serif\"><font
 size=\"1\">".date("D M j G:i:s T Y")."</font></font></p></center>
   <br>
   <br>
</body>
</html>
";


/*
 * Below is the template for the standard error page that is shown by all
 * pages if something goes wrong.
 *
 * Parameters used:
 * __MESSAGE__	replaced by the detailed error message
 */
$template_error = "
<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">
<html>
<head>

  <meta http-equiv=\"CONTENT-TYPE\"
 content=\"text/html; charset=iso-8859-15\">
  <title>$title - An Error Occured</title>

  <meta name=\"GENERATOR\" content=\"StarOffice 6.0  (Linux)\">
  <meta name=\"AUTHOR\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CREATED\" content=\"20030510;23192100\">
  <meta name=\"CHANGEDBY\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CHANGED\" content=\"20030510;23273200\">

  <style>
	<!--
		@page { size: 21.59cm 27.94cm }
		TD P { color: #ffffff }
		TD P.western { font-family: sans-serif; so-language: en-US }
		H1 { color: #ffffff }
		H1.western { font-family: sans-serif; so-language: en-US }
		H1.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H1.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		P { color: #ffffff }
		P.western { font-family: sans-serif; so-language: en-US }
		H2 { color: #ffffff }
		H2.western { font-family: sans-serif; so-language: en-US }
		H2.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H2.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		TD P.next-western { font-family: sans-serif; so-language: en-US; text-align: right }
		TD P.next-cjk { text-align: right }
		TD P.next-ctl { text-align: right }
		A:link { color: #dc2300 }
		A:visited { color: #ff6633 }
	-->
	</style>
</head>
  <body lang=\"en-US\" text=\"#ffffff\" link=\"#dc2300\" vlink=\"#ff6633\"
 bgcolor=\"#33a3a3\">

<div align=\"center\"><img src=\"$BASECGI/adget.cgi?\" name=\"Graphic1\" align=\"middle\"
 width=\"468\" height=\"60\" border=\"0\" ALT=\"advertisement\">
  <br>

<h1 class=\"western\">Ted's Teddy Toko</h1>
 </div>
     
<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"0\">
   	<col width=\"46*\"> 	<col width=\"210*\"> 	<thead> 		  	</thead> 	<tbody>
   		<tr valign=\"top\">
   			<td rowspan=\"4\" width=\"18%\"> 				              
      <p class=\"western\"><a href=\"$BASEPHP/index.php\"><img
 src=\"images/turhome.gif\" name=\"Home\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
  </a>     </p>

      <p class=\"western\"><font color=\"#000080\"><A HREF=\"$BASEPHP/index.php\">home</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"list.php\">products</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"admin/admin.php\">administration</A></font></p>
   				         			</td>
   			<td width=\"82%\"><p>
      <h2 class=\"western\">An error occured.</h2></P>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p><font face=\"Thorndale, serif\"></font></p>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <div align=\"justify\"> 				       </div>
       
      <p align=\"justify\"><font face=\"Thorndale, serif\">
      
      __MESSAGE__ </font>
<font
 face=\"Thorndale, serif\"><br>
       </font></p>

      <p><font face=\"Thorndale, serif\"><br>
       </font></p>


      <div align=\"right\">  			</div>
       </td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p class=\"western\"><img src=\"images/turup.gif\" name=\"Top\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
        </p>
   			</td>
   		</tr>

  </tbody>
</table>

<p style=\"margin-bottom: 0cm;\"><br>
   </p>
<center>
<p style=\"margin-bottom: 0cm;\"><font face=\"Thorndale, serif\"><font
 size=\"1\">".date("D M j G:i:s T Y")."</font></font></p></center>
    <br>
</body>
</html>
";


/*
 * Below is the template for the list.php page.
 * This page also uses a separate variable that describes how a stock item
 * should be displayed.
 *
 * Parameters used:
 * __LIST__	is replaced by the product listing
 */
$template_list = "
<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">
<html>
<head>

  <meta http-equiv=\"CONTENT-TYPE\"
 content=\"text/html; charset=iso-8859-15\">
  <title>$title - Online Product Catalogue</title>

  <meta name=\"GENERATOR\" content=\"StarOffice 6.0  (Linux)\">
  <meta name=\"AUTHOR\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CREATED\" content=\"20030510;23192100\">
  <meta name=\"CHANGEDBY\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CHANGED\" content=\"20030510;23273200\">

  <style>
	<!--
		@page { size: 21.59cm 27.94cm }
		TD P { color: #ffffff }
		TD P.western { font-family: sans-serif; so-language: en-US }
		H1 { color: #ffffff }
		H1.western { font-family: sans-serif; so-language: en-US }
		H1.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H1.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		P { color: #ffffff }
		P.western { font-family: sans-serif; so-language: en-US }
		H2 { color: #ffffff }
		H2.western { font-family: sans-serif; so-language: en-US }
		H2.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H2.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		TD P.next-western { font-family: sans-serif; so-language: en-US; text-align: right }
		TD P.next-cjk { text-align: right }
		TD P.next-ctl { text-align: right }
		A:link { color: #dc2300 }
		A:visited { color: #ff6633 }
	-->
	</style>
</head>
  <body lang=\"en-US\" text=\"#ffffff\" link=\"#dc2300\" vlink=\"#ff6633\"
 bgcolor=\"#33a3a3\">

<div align=\"center\"><img src=\"$BASECGI/adget.cgi?ad=ad2.gif\" name=\"Graphic1\" align=\"middle\"
 width=\"468\" height=\"60\" border=\"0\" ALT=\"advertisement\">
  <br>

<h1 class=\"western\">Ted's Teddy Toko</h1>
 </div>

<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"0\">
   	<col width=\"46*\"> 	<col width=\"210*\"> 	<thead> 		  	</thead> 	<tbody>
   		<tr valign=\"top\">
   			<td rowspan=\"4\" width=\"18%\">
      <p class=\"western\"><a href=\"$BASEPHP/index.php\"><img
 src=\"images/turhome.gif\" name=\"Home\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
  </a>     </p>

      <p class=\"western\"><font color=\"#000080\"><A HREF=\"$BASEPHP/index.php\">home</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"list.php\">products</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"admin/admin.php\">administration</A></font></p>
   				         			</td>
   			<td width=\"82%\">
      <h2 class=\"western\">Products</h2>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p><font face=\"Thorndale, serif\">Welcome to the online teddy store. Click on an item for a detailed
      product description.</font></p>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <div align=\"justify\"> 				       </div>

      <p align=\"justify\"><font face=\"Thorndale, serif\">
      __LIST__ </font><font
 face=\"Thorndale, serif\"><br>
       </font></p>

      <div align=\"right\">  			</div>
       </td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p class=\"western\"><img src=\"images/turup.gif\" name=\"Top\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
        </p>
   			</td>
   		</tr>

  </tbody>
</table>

<p style=\"margin-bottom: 0cm;\"><br>
   </p>
<center>
<p style=\"margin-bottom: 0cm;\"><font face=\"Thorndale, serif\"><font
 size=\"1\">".date("D M j G:i:s T Y")."</font></font></p></center>
   <br>
</body>
</html>
";

/*
 * This describes how one single item should be displayed on the list
 * (catalogue) page.
 *
 * Parameters used:
 * __SMALLIMAGE__	this is where the product's thumbnail image is placed
 * __ITEMLINK__	replaced by a link to the corresponding item.php page
 * __NAME__	replaced by the product's name
 * __PRICE__	replaced by the product's price
 */
$template_list_item = "
<P>
      <table cellpadding=\"5\" cellspacing=\"0\" border=\"1\" width=\"100%\"
 bgcolor=\"#138383\">
          <tbody>
            <tr>
              <th valign=\"top\">                            
            <table cellpadding=\"4\" cellspacing=\"0\" border=\"0\"
 width=\"100%\">
              <tbody>
                <tr>
                  <td valign=\"top\" align=\"center\"><b>__NAME__</b><br>
                  </td>
                  <td valign=\"top\"><br>
                  </td>
                </tr>
                <tr>
                  <td valign=\"top\" width=\"20%\"><A HREF=\"__ITEMLINK__\"><img
 src=\"__SMALLIMAGE__\"
 alt=\"product image\" border=\"0\"></A>
              <br>
              </td>
                  <td valign=\"bottom\">
                  <table cellpadding=\"2\" cellspacing=\"2\" border=\"0\"
 width=\"50%\">
                <tbody>
                  <tr>
                    <td width=\"10%\" valign=\"top\">Name:<br>
                    </td>
                    <td valign=\"top\">__NAME__<br>
                    </td>
                  </tr>
                  <tr>
                    <td valign=\"top\">Price:<br>
                    </td>
                    <td valign=\"top\">__PRICE__<br>
                    </td>
                  </tr>
                  <tr>
                    <td valign=\"top\">Details:<br>
                    </td>
                    <td valign=\"bottom\"><A HREF=\"__ITEMLINK__\">click here</A><br>
                    </td>
                  </tr>
                               
                    </tbody>                          
                  </table>
              </td>
                </tr>
              </tbody>
            </table>
               </th>
            </tr>
                   
        </tbody>              
      </table>
</P>
";


/*
 * Below is the template for the item.php page.
 *
 * Parameters used:
 * __BIGIMAGE__	replace by the location of a big image of the product
 * __NAME__	replaced by the name of the product
 * __PRICE__	replaced by the product's price
 * __DESC__	replaced by the product description
 * __ID__	replaced by the product ID (required)
 */
$template_item = "
<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">
<html>
<head>

  <meta http-equiv=\"CONTENT-TYPE\"
 content=\"text/html; charset=iso-8859-15\">
  <title>$title - Detailed Product Information</title>

  <meta name=\"GENERATOR\" content=\"StarOffice 6.0  (Linux)\">
  <meta name=\"AUTHOR\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CREATED\" content=\"20030510;23192100\">
  <meta name=\"CHANGEDBY\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CHANGED\" content=\"20030510;23273200\">

  <style>
	<!--
		@page { size: 21.59cm 27.94cm }
		TD P { color: #ffffff }
		TD P.western { font-family: sans-serif; so-language: en-US }
		H1 { color: #ffffff }
		H1.western { font-family: sans-serif; so-language: en-US }
		H1.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H1.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		P { color: #ffffff }
		P.western { font-family: sans-serif; so-language: en-US }
		H2 { color: #ffffff }
		H2.western { font-family: sans-serif; so-language: en-US }
		H2.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H2.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		TD P.next-western { font-family: sans-serif; so-language: en-US; text-align: right }
		TD P.next-cjk { text-align: right }
		TD P.next-ctl { text-align: right }
		A:link { color: #dc2300 }
		A:visited { color: #ff6633 }
	-->
	</style>
</head>
  <body lang=\"en-US\" text=\"#ffffff\" link=\"#dc2300\" vlink=\"#ff6633\"
 bgcolor=\"#33a3a3\">

<div align=\"center\"><img src=\"$BASECGI/adget.cgi?nocache=1\" name=\"Graphic1\" align=\"middle\"
 width=\"468\" height=\"60\" border=\"0\" ALT=\"advertisement\">
  <br>

<h1 class=\"western\">Ted's Teddy Toko</h1>
 </div>

<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"0\">
   	<col width=\"46*\"> 	<col width=\"210*\"> 	<thead> 		  	</thead> 	<tbody>
   		<tr valign=\"top\">
   			<td rowspan=\"4\" width=\"18%\">
      <p class=\"western\"><a href=\"$BASEPHP/index.php\"><img
 src=\"images/turhome.gif\" name=\"Home\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
  </a>     </p>

      <p class=\"western\"><font color=\"#000080\"><A HREF=\"$BASEPHP/index.php\">home</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"list.php\">products</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"admin/admin.php\">administration</A></font></p>
   				         			</td>
   			<td width=\"82%\">
			<p>
      <h2 class=\"western\">Detailed Product Information Sheet</h2></P>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p><font face=\"Thorndale, serif\"></font></p>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <div align=\"justify\"> 				       </div>

      <p align=\"justify\"><font face=\"Thorndale, serif\">
      

       <center>
      <table cellpadding=\"5\" cellspacing=\"0\" border=\"1\" width=\"50%\"
 bgcolor=\"#138383\">
                 <tbody>
                   <tr>
                     <th valign=\"top\">
                                                                        

            <table cellpadding=\"4\" cellspacing=\"0\" border=\"0\"
 width=\"100%\">
                     <tbody>
                       <tr>
                                               <td valign=\"top\"
 align=\"center\"><b>__NAME__</b><br>
                         </td>
                       </tr>
                       <tr>
                                               <td valign=\"bottom\"
 align=\"center\">
            <br>
                    <img
 src=\"__BIGIMAGE__\"
 alt=\"erwg\" >
                    <br>
                    <br>

                  <table cellpadding=\"2\" cellspacing=\"2\" border=\"0\"
 width=\"80%\">
                       <tbody>
                         <tr>
                           <td valign=\"top\">Name:<br>
                           </td>
                           <td valign=\"top\">__NAME__<br>
                           </td>
                         </tr>
                         <tr>
                           <td valign=\"top\">Price:<br>
                           </td>
                           <td valign=\"top\">__PRICE__<br>
                           </td>
                         </tr>
                         <tr>
                           <td valign=\"top\">Description:<br>
                           </td>
                           <td  width=\"100%\"valign=\"bottom\">__DESC__<br>
                           </td>
                         </tr>

                    </tbody>


                  </table>
                      <br>
			 <FORM METHOD=\"GET\" ACTION=\"buy.php\">
			<INPUT TYPE=\"HIDDEN\" NAME=\"id\" VALUE=\"__ID__\">
                    <input type=\"submit\" value=\"I need one!\">
		    </FORM>
        <br>
                     </td>
                       </tr>
              </tbody>
            </table>
                      </th>
                   </tr>
        </tbody>

      </table>
 </center>

      </font><font
 face=\"Thorndale, serif\"><br>
       </font></p>

      <div align=\"right\">  			</div>
       </td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p class=\"western\"><img src=\"images/turup.gif\" name=\"Top\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
        </p>
   			</td>
   		</tr>

  </tbody>
</table>

<p style=\"margin-bottom: 0cm;\"><br>
   </p>
<center>
<p style=\"margin-bottom: 0cm;\"><font face=\"Thorndale, serif\"><font
 size=\"1\">".date("D M j G:i:s T Y")."</font></font></p></center>
   <br>
</body>
</html>
";


/*
 * Parameters used:
 * __BIGIMAGE__	this is where the product's image is placed
 * __ID__	this is where the product's identifier is placed (required)
 * __STOCK__	represents the amount we have in stock
 * __PRICE__	replaced by the product's price
 * __NAME__	replaced by the product's name
 */
$template_buy = "
<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">
<html>
<head>

  <meta http-equiv=\"CONTENT-TYPE\"
 content=\"text/html; charset=iso-8859-15\">
  <title>$title - Buy a Teddy</title>

  <meta name=\"GENERATOR\" content=\"StarOffice 6.0  (Linux)\">
  <meta name=\"AUTHOR\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CREATED\" content=\"20030510;23192100\">
  <meta name=\"CHANGEDBY\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CHANGED\" content=\"20030510;23273200\">

  <style>
	<!--
		@page { size: 21.59cm 27.94cm }
		TD P { color: #ffffff }
		TD P.western { font-family: sans-serif; so-language: en-US }
		H1 { color: #ffffff }
		H1.western { font-family: sans-serif; so-language: en-US }
		H1.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H1.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		P { color: #ffffff }
		P.western { font-family: sans-serif; so-language: en-US }
		H2 { color: #ffffff }
		H2.western { font-family: sans-serif; so-language: en-US }
		H2.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H2.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		TD P.next-western { font-family: sans-serif; so-language: en-US; text-align: right }
		TD P.next-cjk { text-align: right }
		TD P.next-ctl { text-align: right }
		A:link { color: #dc2300 }
		A:visited { color: #ff6633 }
	-->
	</style>
</head>
  <body lang=\"en-US\" text=\"#ffffff\" link=\"#dc2300\" vlink=\"#ff6633\"
 bgcolor=\"#33a3a3\">

<div align=\"center\"><img src=\"$BASECGI/adget.cgi?nocache=1\" name=\"Graphic1\" align=\"middle\"
 width=\"468\" height=\"60\" border=\"0\" ALT=\"advertisement\">
  <br>

<h1 class=\"western\">Ted's Teddy Toko</h1>
 </div>

<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"0\">
   	<col width=\"46*\"> 	<col width=\"210*\"> 	<thead> 		  	</thead> 	<tbody>
   		<tr valign=\"top\">
   			<td rowspan=\"4\" width=\"18%\">
      <p class=\"western\"><a href=\"$BASEPHP/index.php\"><img
 src=\"images/turhome.gif\" name=\"Home\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
  </a>     </p>

      <p class=\"western\"><font color=\"#000080\"><A HREF=\"$BASEPHP/index.php\">home</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"list.php\">products</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"admin/admin.php\">administration</A></font></p>
   				         			</td>
   			<td width=\"82%\">
			<p>
      <h2 class=\"western\">Order Product</h2></P>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p><font face=\"Thorndale, serif\"></font>To order one or more of these items,
      please complete the form below and click buy.</p>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <div align=\"justify\"> 				       </div>

      <p align=\"justify\"><font face=\"Thorndale, serif\">
      

       <center>
 <FORM METHOD=\"GET\" ACTION=\"buy.php\">
<INPUT TYPE=\"HIDDEN\" NAME=\"id\" VALUE=\"__ID__\">
      <table cellpadding=\"5\" cellspacing=\"0\" border=\"1\" width=\"50%\"
 bgcolor=\"#138383\">
                 <tbody>
                   <tr>
                     <th valign=\"top\">
                                                                        

            <table cellpadding=\"4\" cellspacing=\"0\" border=\"0\"
 width=\"100%\">
                     <tbody>
                       <tr>
                                               <td valign=\"top\"
 align=\"center\"><b>__NAME__</b><br>
                         </td>
                       </tr>
                       <tr>
                                               <td valign=\"bottom\"
 align=\"left\">
            <br>
<center>
                    <img
 src=\"__BIGIMAGE__\"
 alt=\"picture\" >
                    <br>
                    <br>

                  <table cellpadding=\"2\" cellspacing=\"2\" border=\"0\"
 width=\"80%\">
                       <tbody>
                         <tr>
                           <td valign=\"top\">Name:<br>
                           </td>
                           <td valign=\"top\">__NAME__<br>
                           </td>
                         </tr>
                         <tr>
                           <td valign=\"top\">Price:<br>
                           </td>
                           <td valign=\"top\">__PRICE__<br>
                           </td>
                         </tr>
                         <tr>
                           <td valign=\"top\">Availability:<br>
                           </td>
                           <td  width=\"100%\"valign=\"bottom\">__STOCK__ items in stock<br>
                           </td>
                         </tr>

                    </tbody>


                  </table></center>
                     </td>
                       </tr>
              </tbody>
            </table>
                      </th>
                   </tr>
        </tbody>

      </table>
 </center><br>
<P>
I'd like to have <INPUT TYPE=\"TEXT\" SIZE=\"4\" NAME=\"quantity\" VALUE=\"1\">
of these for __PRICE__ each.
Please withdraw the money from my bank account number: <INPUT TYPE=\"TEXT\" SIZE=\"8\" NAME=\"account\" VALUE=\"\"><BR>
<p align=\"left\"><INPUT TYPE=\"SUBMIT\" VALUE=\"Buy!\"></p>
</p>
    </FORM>

      </font><font
 face=\"Thorndale, serif\"><br>
       </font></p>

      <div align=\"right\">  			</div>
       </td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p class=\"western\"><img src=\"images/turup.gif\" name=\"Top\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
        </p>
   			</td>
   		</tr>

  </tbody>
</table>

<p style=\"margin-bottom: 0cm;\"><br>
   </p>
<center>
<p style=\"margin-bottom: 0cm;\"><font face=\"Thorndale, serif\"><font
 size=\"1\">".date("D M j G:i:s T Y")."</font></font></p></center>
   <br>
</body>
</html>
";


/*
 * This page is displayed when a product has been sold successfully.
 *
 * Parameters used:
 * __AMOUNT__	replaced by the total price
 * __QUANTITY__	replaced by the number of items
 * __SRCACCOUNT__	replaced by the client's account number
 * __PRICE__	replaced by the product's price
 * __DESTACCOUNT__	replaced by our account number
 * __CERTIFICATE__	replaced by the certificate of the money transfer
 */
$template_buy_success = "
<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">
<html>
<head>

  <meta http-equiv=\"CONTENT-TYPE\"
 content=\"text/html; charset=iso-8859-15\">
  <title>$title - Buy a Teddy - Purchase Successful</title>

  <meta name=\"GENERATOR\" content=\"StarOffice 6.0  (Linux)\">
  <meta name=\"AUTHOR\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CREATED\" content=\"20030510;23192100\">
  <meta name=\"CHANGEDBY\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CHANGED\" content=\"20030510;23273200\">

  <style>
	<!--
		@page { size: 21.59cm 27.94cm }
		TD P { color: #ffffff }
		TD P.western { font-family: sans-serif; so-language: en-US }
		H1 { color: #ffffff }
		H1.western { font-family: sans-serif; so-language: en-US }
		H1.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H1.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		P { color: #ffffff }
		P.western { font-family: sans-serif; so-language: en-US }
		H2 { color: #ffffff }
		H2.western { font-family: sans-serif; so-language: en-US }
		H2.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H2.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		TD P.next-western { font-family: sans-serif; so-language: en-US; text-align: right }
		TD P.next-cjk { text-align: right }
		TD P.next-ctl { text-align: right }
		A:link { color: #dc2300 }
		A:visited { color: #ff6633 }
	-->
	</style>
</head>
  <body lang=\"en-US\" text=\"#ffffff\" link=\"#dc2300\" vlink=\"#ff6633\"
 bgcolor=\"#33a3a3\">

<div align=\"center\"><img src=\"$BASECGI/adget.cgi?\" name=\"Graphic1\" align=\"middle\"
 width=\"468\" height=\"60\" border=\"0\" ALT=\"advertisement\">
  <br>

<h1 class=\"western\">Ted's Teddy Toko</h1>
 </div>
     
<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"0\">
   	<col width=\"46*\"> 	<col width=\"210*\"> 	<thead> 		  	</thead> 	<tbody>
   		<tr valign=\"top\">
   			<td rowspan=\"4\" width=\"18%\"> 				              
      <p class=\"western\"><a href=\"$BASEPHP/index.php\"><img
 src=\"images/turhome.gif\" name=\"Home\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
  </a>     </p>

      <p class=\"western\"><font color=\"#000080\"><A HREF=\"$BASEPHP/index.php\">home</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"list.php\">products</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"admin/admin.php\">administration</A></font></p>
   				         			</td>
   			<td width=\"82%\"><p>
      <h2 class=\"western\">Transaction Succeeded.</h2></P>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p><font face=\"Thorndale, serif\"></font></p>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <div align=\"justify\"> 				       </div>
       
      <p align=\"justify\"><font face=\"Thorndale, serif\">
Your transaction was completed succesfully.
We have transferred __AMOUNT__ (__QUANTITY__ products of __PRICE__ each) from your account
(__SRCACCOUNT__) to our account (__DESTACCOUNT__). The certificate of payment is __CERTIFICATE__.
You might want to print this page as your receipt.<BR>
Although we don't have your address, we'll mail the goods to you as soon as possible.
</P>
<P>
Now please <A HREF=\"list.php\">return to the shop</A> and buy some more of our products.
</P>
      </font>
</p>

       </td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p class=\"western\"><img src=\"images/turup.gif\" name=\"Top\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
        </p>
   			</td>
   		</tr>

  </tbody>
</table>

<p style=\"margin-bottom: 0cm;\"><br>
   </p>
<center>
<p style=\"margin-bottom: 0cm;\"><font face=\"Thorndale, serif\"><font
 size=\"1\">".date("D M j G:i:s T Y")."</font></font></p></center>
    <br>
</body>
</html>
";


/*
 * Below is the template for the admin.php page.
 *
 * Parameters used:
 * __LIST__	is replaced by the product listing
 */
$template_admin = "
<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">
<html>
<head>

  <meta http-equiv=\"CONTENT-TYPE\"
 content=\"text/html; charset=iso-8859-15\">
  <title>$title - Stock Administration</title>

  <meta name=\"GENERATOR\" content=\"StarOffice 6.0  (Linux)\">
  <meta name=\"AUTHOR\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CREATED\" content=\"20030510;23192100\">
  <meta name=\"CHANGEDBY\" content=\"Erik van Zijst - Sander van Loo\">
  <meta name=\"CHANGED\" content=\"20030510;23273200\">

  <style>
	<!--
		@page { size: 21.59cm 27.94cm }
		TD P { color: #ffffff }
		TD P.western { font-family: sans-serif; so-language: en-US }
		H1 { color: #ffffff }
		H1.western { font-family: sans-serif; so-language: en-US }
		H1.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H1.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		P { color: #ffffff }
		P.western { font-family: sans-serif; so-language: en-US }
		H2 { color: #ffffff }
		H2.western { font-family: sans-serif; so-language: en-US }
		H2.cjk { font-family: \"HG Mincho Light J\", \"MS Mincho\", \"HG Mincho J\", \"HG Mincho L\", \"HG Mincho\", \"Mincho\", \"MS PMincho\", \"MS Gothic\", \"HG Gothic J\", \"HG Gothic B\", \"HG Gothic\", \"Gothic\", \"MS PGothic\", \"Andale Sans UI\", \"Arial Unicode MS\", \"Lucida Sans Unicode\", \"Tahoma\"; font-size: 12pt; font-weight: medium }
		H2.ctl { font-family: \"Arial Unicode MS\"; font-size: 12pt; font-weight: medium }
		TD P.next-western { font-family: sans-serif; so-language: en-US; text-align: right }
		TD P.next-cjk { text-align: right }
		TD P.next-ctl { text-align: right }
		A:link { color: #dc2300 }
		A:visited { color: #ff6633 }
	-->
	</style>
</head>
  <body lang=\"en-US\" text=\"#ffffff\" link=\"#dc2300\" vlink=\"#ff6633\"
 bgcolor=\"#33a3a3\">

<div align=\"center\"><img src=\"$BASECGI/adget.cgi?ad=ad2.gif\" name=\"Graphic1\" align=\"middle\"
 width=\"468\" height=\"60\" border=\"0\" ALT=\"advertisement\">
  <br>

<h1 class=\"western\">Ted's Teddy Toko</h1>
 </div>

<table width=\"100%\" border=\"0\" cellpadding=\"4\" cellspacing=\"0\">
   	<col width=\"46*\"> 	<col width=\"210*\"> 	<thead> 		  	</thead> 	<tbody>
   		<tr valign=\"top\">
   			<td rowspan=\"4\" width=\"18%\">
      <p class=\"western\"><a href=\"$BASEPHP/index.php\"><img
 src=\"../images/turhome.gif\" name=\"Home\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
  </a>     </p>

      <p class=\"western\"><font color=\"#000080\"><A HREF=\"$BASEPHP/index.php\">home</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"../list.php\">products</A></font></p>
      <p class=\"western\"><font color=\"#000080\"><A HREF=\"admin.php\">administration</A></font></p>
   				         			</td>
   			<td width=\"82%\">
      <h2 class=\"western\">Backoffice</h2>
   			</td>
   		</tr>
   		<tr>
   			<td align=\"justify\" width=\"82%\" valign=\"top\">
      <p><font face=\"Thorndale, serif\">Use this secured section of the site to
      set up our stocks. Please specify how much of each product we ave in stock
      and click the \"commit\" button.<BR>
      This page also tells you how many products we have sold so far and which item
      is most populair.</font></p>
   			</td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <div align=\"justify\"> 				       </div>

<FORM METHOD=\"GET\" ACTION=\"admin.php\">
      <p align=\"justify\"><font face=\"Thorndale, serif\">
      __LIST__ </font><font
 face=\"Thorndale, serif\"><br>
       </font></p>

       	<center>
 	<table border=\"0\">
	<tr\">
		<td>
			<INPUT TYPE=\"SUBMIT\" VALUE=\"commit\">
		</td>
		<td>
			<INPUT TYPE=\"RESET\" VALUE=\"undo my changes\">
		</td>
		<td>
			</FORM>
			<!-- This button/form is only to reload the page. -->
			<FORM METHOD=\"GET\" ACTION=\"admin.php\">
			<INPUT TYPE=\"SUBMIT\" VALUE=\"just refresh\">
			</FORM>
		</td>
	</tr>
	</table>
       	</center>

      <div align=\"right\">  			</div>
       </td>
   		</tr>
   		<tr>
   			<td width=\"82%\" valign=\"top\">
      <p class=\"western\"><img src=\"../images/turup.gif\" name=\"Top\" align=\"bottom\"
 width=\"30\" height=\"30\" border=\"0\">
        </p>
   			</td>
   		</tr>

  </tbody>
</table>

<p style=\"margin-bottom: 0cm;\"><br>
   </p>
<center>
<p style=\"margin-bottom: 0cm;\"><font face=\"Thorndale, serif\"><font
 size=\"1\">".date("D M j G:i:s T Y")."</font></font></p></center>
   <br>
</body>
</html>
";


/*
 * This describes how one single item should be displayed on the admin page.
 *
 * Parameters used:
 * __SMALLIMAGE__	this is where the product's thumbnail image is placed
 * __ID__	this is where the product's identifier is placed
 * __AMOUNT__	represents the amount we have in stock
 * __SOLD__	replaced by the number of items sold
 */
$template_admin_item = "
<P>
      <table cellpadding=\"5\" cellspacing=\"0\" border=\"1\" width=\"100%\"
 bgcolor=\"#138383\">
          <tbody>
            <tr>
              <th valign=\"top\">                            
            <table cellpadding=\"4\" cellspacing=\"0\" border=\"0\"
 width=\"100%\">
              <tbody>
                <tr>
                  <td valign=\"top\" width=\"20%\"><img
 src=\"__SMALLIMAGE__\"
 alt=\"product image\" >
              <br>
              </td>
                  <td valign=\"bottom\">
                  <table cellpadding=\"2\" cellspacing=\"2\" border=\"0\"
 width=\"80%\">
		<tbody>
                  <tr>
                    <td valign=\"top\">Popularity:<br>
                    </td>
                    <td width=\"100%\" valign=\"top\">__SOLD__ item(s) sold<br>
                    </td>
                  </tr>
                  <tr>
                    <td valign=\"top\">In stock:<br>
                    </td>
                    <td valign=\"bottom\"><INPUT TYPE=\"TEXT\" SIZE=\"8\" NAME=\"__ID__\" VALUE=\"__AMOUNT__\"><br>
                    </td>
                  </tr>
                               
                    </tbody>                          
                  </table>
              </td>
                </tr>
              </tbody>
            </table>
               </th>
            </tr>
                   
        </tbody>              
      </table>
</P>
";

?>
