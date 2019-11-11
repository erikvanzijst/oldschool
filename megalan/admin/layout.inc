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
$layout = "<html>
<head>
<title>MegaLAN</title>
<meta http-equiv=\"Content-Type\" content=\"text/html\">

<style type=\"text/css\">
<!--

/* Anchor pseudo-classes for the links */
a:link    { background: transparent; color: #00CC00; decoration: none; text-decoration: none}
a:active  { background: transparent; color: #FF0000; decoration: none; text-decoration: none}
a:visited { background: transparent; color: #00CC00; decoration: none; text-decoration: none}
a:hover { color: #FF0000; decoration: none; text-decoration: underline}
-->
</style>
<script language=\"JavaScript\">

<!--

function MM_swapImgRestore() { //v2.0
  if (document.MM_swapImgData != null)
    for (var i=0; i<(document.MM_swapImgData.length-1); i+=2)
      document.MM_swapImgData[i].src = document.MM_swapImgData[i+1];
}

function MM_preloadImages() { //v2.0
  if (document.images) {
    var imgFiles = MM_preloadImages.arguments;
    if (document.preloadArray==null) document.preloadArray = new Array();
    var i = document.preloadArray.length;
    with (document) for (var j=0; j<imgFiles.length; j++) if (imgFiles[j].charAt(0)!=\"#\"){
      preloadArray[i] = new Image;
      preloadArray[i++].src = imgFiles[j];
  } }
}

function MM_swapImage() { //v2.0
  var i,j=0,objStr,obj,swapArray=new Array,oldArray=document.MM_swapImgData;
  for (i=0; i < (MM_swapImage.arguments.length-2); i+=3) {
    objStr = MM_swapImage.arguments[(navigator.appName == 'Netscape')?i:i+1];
    if ((objStr.indexOf('document.layers[')==0 && document.layers==null) ||
        (objStr.indexOf('document.all[')   ==0 && document.all   ==null))
      objStr = 'document'+objStr.substring(objStr.lastIndexOf('.'),objStr.length);
    obj = eval(objStr);
    if (obj != null) {
      swapArray[j++] = obj;
      swapArray[j++] = (oldArray==null || oldArray[j-1]!=obj)?obj.src:oldArray[j];
      obj.src = MM_swapImage.arguments[i+2];
  } }
  document.MM_swapImgData = swapArray; //used for restore
}

function MM_displayStatusMsg(msgStr) { //v2.0
  status=msgStr;
  document.MM_returnValue = true;
}
//-->

</script>
</head>

<!-- php3 - MySQL - Linux - apache -->


<body ";

if($onload && $onload != "")
	$layout = $layout . "onLoad=\"$onload\" ";

$layout = $layout ."bgcolor=\"#000000\" text=\"#FFFFFF\" link=\"#00CC00\" vlink=\"#006600\" alink=\"#00FF00\" onLoad=\"MM_displayStatusMsg('MegaLAN 13 : 11 - 12 December 1999');return document.MM_returnValue\" background=\"images/zero.gif\">

<img src=\"http://capsi.cx/bbstats/bbstats.php?bbstats_id=ice&img=1&trans=1\" width=1 height=1 alt=\"\">

<a name=\"top\"></a>
<table width=\"750\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\" height=\"100%\">
  <tr align=\"left\" valign=\"top\">
    <td width=\"125\">
      <p align=\"center\"><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\" color=\"#00FF00\">Hoofdsponsor
        :</font><br>
        <a href=\"http://www.intel.nl/\" onMouseOut=\"MM_swapImgRestore();MM_displayStatusMsg('MegaLAN 13 : 11 - 12 December 1999');return document.MM_returnValue\" onMouseOver=\"MM_swapImage('document.Intel','document.Intel','images/sponsors/intel_active.gif','#937956118630');MM_displayStatusMsg('Intel Networking Benelux B.V.');return document.MM_returnValue\" target=\"_blank\"><img name=\"Intel\" border=\"0\" src=\"images/sponsors/intel.gif\" width=\"75\" height=\"39\" alt=\"Intel Networking Benelux B.V.\"></a></p>
      <table width=\"115\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">
        <tr>
          <td width=\"12\" height=\"12\" background=\"images/table/corner1_green.gif\"><img src=\"images/table/corner1_green.gif\" width=\"12\" height=\"12\"></td>
          <td width=\"91\" height=\"12\"></td>
          <td width=\"12\" height=\"12\" background=\"images/table/corner2_green.gif\"><img src=\"images/table/corner2_green.gif\" width=\"12\" height=\"12\"></td>
        </tr>
        <tr>
          <td width=\"12\" background=\"images/table/line4_green.gif\">&nbsp;</td>
          <td width=\"91\" align=\"center\" valign=\"top\">
            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">
              <tr align=\"center\" valign=\"middle\">
		<td><font color=\"#E3E3E3\" face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\"><a href=\"http://www.megalan.nl/info.php3\" onMouseOver=\"MM_displayStatusMsg('Algemene info en achtergrond');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 14 : 18 - 19 Maart 2000');return document.MM_returnValue\">Informatie</a></font></td>
              </tr>
              <tr align=\"center\" valign=\"middle\">
		<td><font color=\"#E3E3E3\" face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\"><a href=\"http://www.megalan.nl/regels.php3\" onMouseOver=\"MM_displayStatusMsg('Regelgeving tijdens het evenement');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 14 : 18 - 19 Maart 2000');return document.MM_returnValue\">Regels</a></font></td>
              </tr>
              <tr align=\"center\" valign=\"middle\">
		<td><font color=\"#E3E3E3\" face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\"><a href=\"http://www.megalan.nl/lokatie.php3\" onMouseOver=\"MM_displayStatusMsg('Routebeschrijving om naar MegaLAN te komen');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 14 : 18 - 19 Maart 2000');return document.MM_returnValue\">Lokatie</a></font></td>
              </tr>
              <tr align=\"center\" valign=\"middle\">
                <td><font color=\"#E3E3E3\" face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\"><a href=\"http://prutser.cx/~icehawk/megalan/login.php3\" onMouseOver=\"MM_displayStatusMsg('Aanmelden voor de volgende editie');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 13 : 11 - 12 December 1999');return document.MM_returnValue\">Aanmelden</a></font></td>
              </tr>
              <tr align=\"center\" valign=\"middle\">
                <td><font color=\"#E3E3E3\" face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\"><a href=\"http://prutser.cx/~icehawk/megalan/deelnemers.php3\" onMouseOver=\"MM_displayStatusMsg('Overzicht van de mensen die zich al  aangemeld hebben');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 13 : 11 - 12 December 1999');return document.MM_returnValue\">Deelnemers</a></font></td>
              </tr>
              <tr align=\"center\" valign=\"middle\">
		<td><font color=\"#E3E3E3\" face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\"><a href=\"http://www.megalan.nl/sponsors.php3\" onMouseOver=\"MM_displayStatusMsg('Sponsoren overzicht');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 14 : 18 - 19 Maart 2000');return document.MM_returnValue\">Sponsors</a></font></td>
              </tr>
              <tr align=\"center\" valign=\"middle\">
		<td><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\"><a href=\"http://www.megalan.nl/phorum/\" onMouseOver=\"MM_displayStatusMsg('Discussies over MegaLAN');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 14 : 18 - 19 Maart 2000');return document.MM_returnValue\">Forum</a></font></td>
              </tr>
              <tr align=\"center\" valign=\"middle\">
		<td><font color=\"#E3E3E3\" face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\"><a href=\"http://www.megalan.nl/gallery.php3\" onMouseOver=\"MM_displayStatusMsg('Foto\'s en video\'s van vorige edities');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 14 : 18 - 19 Maart 2000');return document.MM_returnValue\">Gallery</a></font></td>
              </tr>
              <tr align=\"center\" valign=\"middle\">
		<td><font color=\"#E3E3E3\" face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\"><a href=\"http://www.megalan.nl/index.php3\" onMouseOver=\"MM_displayStatusMsg('Terug naar de hoofdpagina');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 14 : 18 - 19 Maart 2000');return document.MM_returnValue\">Home</a></font></td>
              </tr>
            </table>
          </td>
          <td width=\"12\" background=\"images/table/line2_green.gif\">&nbsp;</td>
        </tr>
        <tr>
          <td width=\"12\" height=\"12\" background=\"images/table/corner3_green.gif\"><img src=\"images/table/corner3_green.gif\" width=\"12\" height=\"12\"></td>
          <td width=\"91\" height=\"12\"></td>
          <td width=\"12\" height=\"12\" background=\"images/table/corner4_green.gif\"><img src=\"images/table/corner4_green.gif\" width=\"12\" height=\"12\"></td>
        </tr>
      </table>
      <p>&nbsp;</p>

    </td>
    <td width=\"525\">
      <p align=\"center\"><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"5\"><b><font size=\"6\">$titel</font></b></font></p>
      <table width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">
        <tr>
          <td width=\"500\" align=\"left\" valign=\"top\" colspan=\"2\"> <br>
            <table width=\"494\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">
              <tr>
                <td width=\"12\" height=\"12\"><img src=\"images/table/corner1_green.gif\" width=\"12\" height=\"12\"></td>
<td colspan=\"2\" width=\"470\" background=\"images/table/line1_green.gif\"><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">&nbsp;</font></td>
                <td width=\"12\" height=\"12\"><img src=\"images/table/corner2_green.gif\" width=\"12\" height=\"12\"></td>
              </tr>
              <tr>
                <td width=\"12\" background=\"images/table/line4_green.gif\">&nbsp;</td>
                <td colspan=\"2\" width=\"470\">
                  <table border=\"0\" cellspacing=\"5\" cellpadding=\"2\" width=\"100%\">
";

return $layout;
}


Function PrintLower()
{
$layout = "
                  </table>
                  <p>&nbsp;</p>
                  </td>
                <td width=\"12\" background=\"images/table/line2_green.gif\">&nbsp;</td>
              </tr>
              <tr>
                <td height=\"12\" width=\"12\"><img src=\"images/table/corner3_green.gif\" width=\"12\" height=\"12\"></td>
                <td colspan=\"2\" width=\"470\" background=\"images/table/line3_green.gif\"><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">&nbsp;</font></td>
                <td height=\"12\" width=\"12\"><img src=\"images/table/corner4_green.gif\" width=\"12\" height=\"12\"></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td width=\"250\" align=\"left\" valign=\"top\">&nbsp;</td>
          <td width=\"250\" align=\"left\" valign=\"top\">&nbsp;</td>
        </tr>
      </table>
    </td>
    <td width=\"100\" rowspan=\"2\">
      <table border=\"0\" cellspacing=\"5\" cellpadding=\"0\" align=\"left\">
        <tr align=\"center\" valign=\"middle\">
          <td>
            <div align=\"center\"><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"2\">Sponsors</font></div>
          </td>
        </tr>
        <tr align=\"center\" valign=\"middle\">
          <td>
            <div align=\"center\"><a href=\"http://www.grafix.nl\" onMouseOver=\"MM_displayStatusMsg('Grafix Internet Solutions');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 13 : 11 - 12 December 1999');return document.MM_returnValue\" target=\"_blank\"><img src=\"images/sponsors/grafix.gif\" width=\"90\" border=\"0\" alt=\"Grafix Internet Solutions\"></a></div>
          </td>
        </tr>
        <tr align=\"center\" valign=\"middle\">
          <td>
            <div align=\"center\"><a href=\"http://www.ism.nl\" target=\"_blank\" onMouseOver=\"MM_displayStatusMsg('Innovative Solutions in Media');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 13 : 11 - 12 December 1999');return document.MM_returnValue\"><img src=\"images/sponsors/ism.gif\" width=\"90\" border=\"0\" alt=\"Innovative Solutions in Media\"></a></div>
          </td>
        </tr>
        <tr align=\"center\" valign=\"middle\">
          <td>
            <div align=\"center\"><a href=\"http://cimdroom.ptf.hro.nl\" target=\"_blank\" onMouseOver=\"MM_displayStatusMsg('Studievereniging CIMDROOM');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 13 : 11 - 12 December 1999');return document.MM_returnValue\"><img src=\"images/sponsors/cimdroom.gif\" width=\"90\" border=\"0\" alt=\"Studievereniging CIMDROOM\"></a></div>
          </td>
        </tr>
        <tr align=\"center\" valign=\"middle\">
          <td>
            <div align=\"center\"><a href=\"http://www.hro.nl\" target=\"_blank\" onMouseOver=\"MM_displayStatusMsg('Hogeschool Rotterdam');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 13 : 11 - 12 December 1999');return document.MM_returnValue\"><img src=\"images/sponsors/hrologo.gif\" width=\"90\" border=\"0\" alt=\"Hogeschool Rotterdam\"></a></div>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr align=\"left\" valign=\"top\">
    <td width=\"125\" height=\"20\">
      <p align=\"center\"><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\"><a href=\"feedback.html\" onMouseOver=\"MM_displayStatusMsg('Commentaar en suggesties betreffende de site');return document.MM_returnValue\" onMouseOut=\"MM_displayStatusMsg('MegaLAN 13 : 11 - 12 December 1999');return document.MM_returnValue\">Webmaster</a></font><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">
        <br>
        &copy;1999 MegaLAN</font></p>
    </td>
    <td width=\"525\"><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"1\">De
      MegaLAN pagina's worden het best weergegeven in Netscape En Internet Explorer.
      In een resolutie van min. 800x600 met 16-bits kleuren.</font></td>
  </tr>
</table>
";

//</body>
//</html>
//";

return $layout;
}

?>

