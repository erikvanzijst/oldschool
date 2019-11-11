<?
include 'setup.php';	// for the base url

// These are some strings used on all pages
$title = "Ted's Teddy Toko";
$stylesheet ="
<style type=\"text/css\">
<!--
body {  font-family: Verdana, Helvetica, sans-serif; font-size: 8pt;}
th   {  font-family: Verdana, Helvetica, sans-serif; font-size: 8pt;}
td   {  font-family: Verdana, Helvetica, sans-serif; font-size: 8pt;}
pre  {  font-family: Courier; font-size: 9pt; }
-->
</style>
";

$header = "<H1>$title</H1><HR>";
$footer = "<HR><A HREF=\"$BASEPHP/index.php\">Home</A><BR>".date("D M j G:i:s T Y");


/*
 * Below is the template for the index.php page. It doesn't have any dynamic
 * elements, but is defined here anyway to be consistent with the other pages
 * and to easily allow for dynamic elements in the future.
 */
$template_index = "
<HTML>
<HEAD>
<TITLE>$title</TITLE>
<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">
$stylesheet
</HEAD>
<BODY BGCOLOR=\"white\">
<CENTER>
<IMG ALT=\"advertisement\" SRC=\"$BASECGI/adget.cgi?ad=ad1.gif\"><BR>
$header
</CENTER>

<P>
On this site we sell teddy bears.
</P>
<P>
<A HREF=\"list.php\">Go to the online shop!</A><BR>
If you are an administrator, you may <A HREF=\"admin/admin.php\">manage the stocks</A>.
</P>
<BR><BR>
<P>
Authors:<BR>
Erik van Zijst - <A HREF=\"mailto:erik@prutser.cx\">erik@prutser.cx</A> - 1351745<BR>
Sander van Loo - <A HREF=\"mailto:sander@marketxs.com\">sander@marketxs.com</A> - 1351753<BR>
</P>
<CENTER>
$footer
</CENTER>
</BODY>
</HTML>
";


/*
 * Below is the template for the standard error page that is shown by all
 * pages if something goes wrong.
 *
 * Parameters used:
 * __MESSAGE__	replaced by the detailed error message
 */
$template_error = "
<HTML>
<HEAD>
<TITLE>$title - An Error Occured</TITLE>
<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">
$stylesheet
</HEAD>
<BODY BGCOLOR=\"white\">
<CENTER>
<IMG ALT=\"advertisement\" SRC=\"$BASECGI/adget.cgi?nocache=1\"><BR>
$header
<P>
<B>__MESSAGE__</B>
</P>
<P>
Contact an administrator if you think this is inappropriate.
</P>
$footer
</CENTER>
</BODY>
</HTML>
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
<HTML>
<HEAD>
<TITLE>$title - Detailed Product Information</TITLE>
<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">
$stylesheet
</HEAD>
<BODY BGCOLOR=\"white\">
<CENTER>
<IMG ALT=\"advertisement\" SRC=\"$BASECGI/adget.cgi?nocache=1\"><BR>
$header

<P>
<IMG SRC=\"__BIGIMAGE__\">
</P>

<P>
Name: __NAME__<BR>
Price: __PRICE__<BR>
Description: __DESC__<BR>
<FORM METHOD=\"GET\" ACTION=\"buy.php\">
<INPUT TYPE=\"HIDDEN\" NAME=\"id\" VALUE=\"__ID__\">
<INPUT TYPE=\"SUBMIT\" VALUE=\"I want one!\">
</FORM>
</P>

$footer
</CENTER>
</BODY>
</HTML>
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
<HTML>
<HEAD>
<TITLE>$title - Online Product Catalogue</TITLE>
<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">
$stylesheet
</HEAD>

<BODY BGCOLOR=\"white\">
<CENTER>
<IMG ALT=\"advertisement\" SRC=\"$BASECGI/adget.cgi?ad=ad2.gif\"><BR>
$header

<P>
<H2>Please select a bear below.</H2>
</P>
__LIST__

$footer
</CENTER>
</BODY>
</HTML>
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
<TABLE BORDER=1 WIDTH=\"180\">
<TR>
<TD>
	<CENTER>
	<B>__NAME__</B><BR>
	<A HREF=\"__ITEMLINK__\"><IMG BORDER=0 SRC=\"__SMALLIMAGE__\"></A>
	</CENTER>
</TD>
</TR>
<TR>
<TD>
	<UL>
		<LI>Name: __NAME__
		<LI>Price: __PRICE__
		<LI><A HREF=\"__ITEMLINK__\">Click here for details</A>
	</UL>
</TD>
</TR>
</TABLE>
<P>
";


/*
 * Below is the template for the admin.php page.
 *
 * Parameters used:
 * __LIST__	is replaced by the product listing
 */
$template_admin = "
<HTML>
<HEAD>
<TITLE>$title - Stock Administration</TITLE>
<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">
$stylesheet
</HEAD>

<BODY BGCOLOR=\"white\">
<CENTER>
<IMG ALT=\"advertisement\" SRC=\"$BASECGI/adget.cgi?nocache=1\"><BR>
$header
This page allows you (the administrator) to specify the number of items we have
in stock for each product.

<FORM METHOD=\"GET\" ACTION=\"admin.php\">
<P>
__LIST__
</P>
<INPUT TYPE=\"SUBMIT\" VALUE=\"commit\">
<INPUT TYPE=\"RESET\" VALUE=\"undo my changes\">
</FORM>
<!-- This button/form is only to reload the page. -->
<FORM METHOD=\"GET\" ACTION=\"admin.php\">
<INPUT TYPE=\"SUBMIT\" VALUE=\"just refresh\">
</FORM>

$footer
</CENTER>
</BODY>
</HTML>
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
<TABLE BORDER=1 WIDTH=\"180\">
<TR>
<TD>
	<CENTER>
	<B>__NAME__</B><BR>
	<IMG SRC=\"__SMALLIMAGE__\">
	</CENTER>
</TD>
</TR>
<TR>
<TD>
	In stock: <INPUT TYPE=\"TEXT\" SIZE=\"8\" NAME=\"__ID__\" VALUE=\"__AMOUNT__\"><BR>
	Popularity: __SOLD__ item(s) sold.
</TD>
</TR>
</TABLE>
<P>
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
<HTML>
<HEAD>
<TITLE>$title - Buy a Teddy</TITLE>
<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">
$stylesheet
</HEAD>

<BODY BGCOLOR=\"white\">
<CENTER>
<IMG ALT=\"advertisement\" SRC=\"$BASECGI/adget.cgi?nocache=1\"><BR>
$header

<P>
<IMG SRC=\"__BIGIMAGE__\">
</P>
Name: __NAME__<BR>
In stock: __STOCK__ items<BR>
<FORM METHOD=\"GET\" ACTION=\"buy.php\">
<INPUT TYPE=\"HIDDEN\" NAME=\"id\" VALUE=\"__ID__\">
Buy me <INPUT TYPE=\"TEXT\" SIZE=\"4\" NAME=\"quantity\" VALUE=\"1\">
of these for __PRICE__ each.<BR>
Please enter your bank account number: <INPUT TYPE=\"TEXT\" SIZE=\"8\" NAME=\"account\" VALUE=\"\"><BR>
<INPUT TYPE=\"SUBMIT\" VALUE=\"Buy!\">
</FORM>

$footer
</CENTER>
</BODY>
</HTML>
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
<HTML>
<HEAD>
<TITLE>$title - Buy a Teddy - Purchase Successful</TITLE>
<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">
$stylesheet
</HEAD>

<BODY BGCOLOR=\"white\">
<CENTER>
<IMG ALT=\"advertisement\" SRC=\"$BASECGI/adget.cgi?nocache=1\"><BR>
$header

<P>
<H2><B>The product is yours!</B></H2>
</P>
<P>
We have transferred __AMOUNT__ (__QUANTITY__ products of __PRICE__ each) from your account
(__SRCACCOUNT__) to our account (__DESTACCOUNT__). The certificate of payment is __CERTIFICATE__.<BR>
Although we don't have your address, we'll mail the goods to you as soon as possible.
</P>
<P>
Now please <A HREF=\"list.php\">return to the shop</A> and buy some more of our products.
</P>
$footer
</CENTER>
</BODY>
</HTML>
";

?>
