<?
Function PrintFont()
{
$output = "<FONT FACE=\"Verdana, Helvetica, Arial\" SIZE=\"2\" COLOR=\"FFFFFF\">\n";
return $output;
}

Function PrintUpper($titel)
{
	return PrintHead($titel, "");
}

Function PrintHead($titel, $onload)
{
$layout = "
<html>
<head>
<title>main</title>
<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">
</head>

<!-- php3 - MySQL - Linux - apache -->


<body ";

if($onload && $onload != "")
	$layout = $layout . "onLoad=\"$onload\" ";

$layout = $layout ."bgcolor=\"#FFFFFF\" link=\"#FF0000\" alink=\"#FFFF00\" vlink=\"#FF0000\">
<table width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"right\"
height=\"162\">
  <tr> 
    <td> 
      <table width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">
        <tr> 
          <td height=\"20\" colspan=\"5\"> 
            <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"102\">
              <tr> 
                <td height=\"20\" width=\"20\"><img src=\"pics/item_top_links.gif\" width=\"20\" height=\"20\" name=\"Image1\"></td>
                <td height=\"20\" bgcolor=\"#000000\" width=\"72\"><b><font size=\"2\" face=\"Verdana, Arial, Helvetica, sans-serif\" color=\"ff0000\">".$titel."</font></b></td>
                <td height=\"20\" width=\"87\"><img src=\"pics/item_top_rechts.gif\" width=\"30\" height=\"20\"></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr> 
          <td height=\"10\" colspan=\"5\"><img src=\"pics/item_vak_top.gif\" width=\"500\" height=\"10\"></td>
        </tr>
        <tr> 
          <td colspan=\"5\" background=\"pics/item_vak_bg.gif\"> 
            <table width=\"490\" border=\"0\" cellspacing=\"2\" cellpadding=\"2\">
";

return $layout;
}


Function PrintLower()
{
$layout = "
            </table>
          </td>
        </tr>
        <tr> 
          <td colspan=\"5\" height=\"15\"><img src=\"pics/item_vak_bottom.gif\" width=\"500\" height=\"15\"></td>
        </tr>
      </table>
      <p align=\"center\"><font size=\"1\">Deze site is geoptimaliseerd voor Netscape 
        browsers met 800x600 resolutie in 16bits kleur<br>
        Copyright &copy; 1999 MegaLAN</font></p>
      </td>
  </tr>
</table>
";

//</body>
//</html>
//";

return $layout;
}

?>

