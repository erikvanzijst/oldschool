

        
        <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
		<html>
		<head>
		<title>Computer Graphics 2004</title>
		<link href="http://www.few.vu.nl/css/few.css" type="text/css" rel="StyleSheet">
		<link href="cg.css" type="text/css" rel="StyleSheet">
		<META NAME="author" CONTENT="Thilo Kielmann, Tom van der Schaaf and Mathijs den Burger">
        <META NAME="robots" CONTENT="index, follow">
		</head>

		<BODY>
		<TABLE WIDTH="900" CELLPADDING="10">
		<TR><TD>

            
		<img src="http://www.few.vu.nl/pics/head-cs-en.gif" width="880" height="120" border="0" alt="logo" usemap="#logomap"><br />
<map id="logomap" name="logomap">
<area shape="rect" coords="4,3,109,116" title="Go to the homepage of the Vrije Universiteit." alt="Go to the homepage of the Vrije Universiteit." href="http://www.english.vu.nl/">
<area shape="rect" coords="140,20,374,50" title="Goto the homepage of the faculty of sciences." alt="Goto the homepage of the faculty of sciences." href="http://www.few.vu.nl/index-en.html">
<area shape="rect" coords="140,55,390,74" title="Goto the homepage of the department of computer science." alt="Goto the homepage of the department of computer science." href="http://www.cs.vu.nl/index-en.html">
</map>

		<h1>Computer Graphics</h1>

		<DIV CLASS="menu">
		<HR NOSHADE>
		<B>

		<A CLASS="menu" HREF="index.php">Main</A> | <A CLASS="menu" HREF="course.php">Course</A> | <A CLASS="menu" HREF="excursion.php">Excursion</A> | <A CLASS="menu" HREF="practicum.php">Practicum</A> | <A CLASS="menu" HREF="exercises.php">Exercises</A> | <A CLASS="activemenuitem" HREF="grading.php"><B>Grading</B></A> | <A CLASS="menu" HREF="faq.php">F.A.Q.</A> | <A CLASS="menu" HREF="links.php">Links</A> | <A CLASS="menu" HREF="halloffame.php">Hall of Fame</A>
		<HR NOSHADE>
		</B>
		</DIV>

		
<H2>Final grading for the course</H2>
<P>
In accordance to the faculty's general rules, participants have to
pass both written exam <strong>and</strong> practical programming assignments
separately to finally get the credit points.
This means, both the exam and the programming assignments have to be marked
5,5 or better. (There will be no partial credit points for people who pass
only one of the two parts.)
</P>
<P>
However, it is possible (if one of the two parts are failed) to keep the
partial result for the next chance. So, if you passed the practical assignments
but failed the exam, you can keep the assignment mark for the next round of
exams. And if you made the exam but failed the assignments, you only have to
redo the practical work with the next course.
</P>

<HR SIZE="1" NOSHADE>

<H2>Practicum grading</H2>
<P>
Each exercise will be rated as a whole. The exercises are not graded, but you will 
get a 'passed' or 'not passed' on each of the exercises (1 and 2). 
The final project is graded, and this will be the overall grade for the 
practical work. The grade ranges from 1 (very poor) to 10 (very good), where greater 
or equal to 5.5 means you passed. 
Here are some things we will look at:</P>

<UL>
<LI><P>
	<B>It works</B></P>
	<P>
	Needless to say, important is that your program works.</P>
<LI><P>
	<B>Program design</B></P>
	<P>
	There are a couple of ways you can do the exercises. We'll look at the 
	way you did it and why. Is it well thought-out, or is it hacked 
	together? We look at modularity, efficiency (important in computer 
	graphics) and structure. Of all the program aspects listed here, this is the most 
	important one.</P>
<LI><P>
    <B>Obligations</B></P>
	<P><UL>
	<LI>Implement <A href="exercises.php#project">all 14 requirements</a> of the final project.</LI>
	<LI>Use the <A HREF="framework.zip">framework</A> workspace.</LI>
	<LI>Do <b><i>not</i></b> use the MFC (Microsoft Foundation Classes).</LI>
	</UL></P><P><I>If you do not meet these requirements, your exercises will not be graded at all!</I></P>
<LI><P>
	<B>Code layout</B></P>
	<P>
	The layout of code is very personal, but we give a few strong suggestions 
	here that will improve readability a lot (both for you and for us):</P>
	<P>
	<UL>
	<LI>Indent between a <CODE>glBegin()</CODE> and a corresponding <CODE>glEnd()</CODE>.
	<LI>Indent between a <CODE>glPushMatrix()</CODE> and corresponding <CODE>glPopMatrix()</CODE>.
	</UL></P>
	<P>
	Even better is to use seperate functions for each <CODE>glBegin()</CODE>/<CODE>glEnd()</CODE> block.</P>
<LI><P>
        <B>Utility functions</B></P>
	<P>
	We also strongly suggest to use the utility functions in <CODE>graphics/util/glutil.c</CODE> instead of
	the usual OS calls, since the former are platform-independent:
	<UL>
	<LI><CODE>double Wallclock()</CODE> returns the UNIX time (nr. seconds after 1-1-1970, 00:00:00) 
	    in double precision.</LI>
	<LI><CODE>void Wait(double val)</CODE> waits <CODE>val</CODE> seconds.</LI>
	<LI><CODE>float frand(float min, float max)</CODE> returns a random float between <CODE>min</CODE>
	    and <CODE>max</CODE>.</LI>
	</UL></P>
<LI><P>
	<B>Comments</B></P>
	<P>
	Again needless to say, document your source. Don't overdocument it 
	(this is not a datastructures-course), but make clear what happens. 
	It makes it easier for us, but most importantly for you to read. Also, 
	write clearly.</P>
	<P>
	Comments and all other documentation have to be written in english. 
	Also add a README file to your work (especially the project) that 
	explains the basics of your solutions.</P>
<LI><P>
	<B>Additional features</B></P>
	<P>
	If you make more than the exercise states, we'll look at it as well, 
	and if it really adds something nice and useful, you get awarded for it.
	<I>Note that this only applies to the final project, since the other exercises are not
	graded</I>. 
	<P>
	<I> Below picture shows a final project from a previous year that was awarded a 10. It contains
	two dinos instead of just one, many extra light sources, other-than-default textures and 
	moving objects. Check the <A HREF="halloffame.php">hall of fame</A> to see more screenshots of previous final projects.</I>
	</P>       
	<P ALIGN="center">
	<A HREF="images/10project.jpg">
	<IMG SRC="images/10project.jpg" WIDTH="250"></A>
	</P>
	<P>		 
	</P>
</UL>


<HR SIZE="1" NOSHADE>

<A NAME="grades"></A>
<H2>Grades</H2>
<P>
The grades for the 2004/05 practicum have been decided.
</P>

<P>
<table cellpadding="5" border="1">
 <tr>
  <th>Surname</th>
  <th>Student number</th>
  <th>Practicum grade</th>
 </tr>
 <col align="left">
 <col align="center">
 <col align="center">
 <tr>
  <td>Assaban</td>
  <td>1438506</td>
  <td>6.8</td>
 </tr>
 <tr>
  <td>Baaren</td>
  <td>1278967</td>
  <td>6.6</td>
 </tr>
 <tr>
  <td>Bestebreurtje</td>
  <td>1203932</td>
  <td>8.2</td>
 </tr>
 <tr>
  <td>Bleeker</td>
  <td>1282085</td>
  <td>10</td>
 </tr>
 <tr>
  <td>Boonstoppel</td>
  <td>1329200</td>
  <td>10</td>
 </tr>
 <tr>
  <td>Bosman</td>
  <td>1329642</td>
  <td>9.6</td>
 </tr>
 <tr>
  <td>Bouterse</td>
  <td>1142828</td>
  <td>7.5</td>
 </tr>
 <tr>
  <td>Chung</td>
  <td>1204009</td>
  <td>8.3</td>
 </tr>
 <tr>
  <td>Cranendonk</td>
  <td>1274961</td>
  <td>8.1</td>
 </tr>
 <tr>
  <td>Dakio</td>
  <td>1488589</td>
  <td>6.7</td>
 </tr>
 <tr>
  <td>Dincic</td>
  <td>1288172</td>
  <td>6.9</td>
 </tr>
 <tr>
  <td>Duyn</td>
  <td>1273310</td>
  <td>9.2</td>
 </tr>
 <tr>
  <td>Eenink</td>
  <td>1274996</td>
  <td>7.9</td>
 </tr>
 <tr>
  <td>Erdok</td>
  <td>1488597</td>
  <td>6.7</td>
 </tr>
 <tr>
  <td>Hagen</td>
  <td>1501917</td>
  <td>7.7</td>
 </tr>
 <tr>
  <td>Hughes</td>
  <td>1202626</td>
  <td>7.1</td>
 </tr>
 <tr>
  <td>Iwanicki</td>
  <td>1493817</td>
  <td>10</td>
 </tr>
 <tr>
  <td>Jaf</td>
  <td>1142976</td>
  <td>6.3</td>
 </tr>
 <tr>
  <td>Jaf</td>
  <td>1160044</td>
  <td>6.3</td>
 </tr>
 <tr>
  <td>Jansen</td>
  <td>1262602</td>
  <td>4</td>
 </tr>
 <tr>
  <td>Jonker</td>
  <td>1045717</td>
  <td>7.4</td>
 </tr>
 <tr>
  <td>Klencke</td>
  <td>1271229</td>
  <td>7.4</td>
 </tr>
 <tr>
  <td>Knelange</td>
  <td>1202650</td>
  <td>6.8</td>
 </tr>
 <tr>
  <td>Koldenhof</td>
  <td>1046330</td>
  <td>6.7</td>
 </tr>
 <tr>
  <td>Kolman</td>
  <td>1518240</td>
  <td>7.4</td>
 </tr>
 <tr>
  <td>Kovacs</td>
  <td>1432764</td>
  <td>6.7</td>
 </tr>
 <tr>
  <td>Kwiatkowski</td>
  <td>1488708</td>
  <td>6.6</td>
 </tr>
 <tr>
  <td>Landgraaf</td>
  <td>1256033</td>
  <td>8.8</td>
 </tr>
 <tr>
  <td>Landzaat</td>
  <td>1204165</td>
  <td>6.5</td>
 </tr>
 <tr>
  <td>Lippes</td>
  <td>1204181</td>
  <td>7</td>
 </tr>
 <tr>
  <td>Loo</td>
  <td>1351753</td>
  <td>8.9</td>
 </tr>
 <tr>
  <td>Maraikar</td>
  <td>1448473</td>
  <td>7.6</td>
 </tr>
 <tr>
  <td>Meersbergen</td>
  <td>1204246</td>
  <td>6</td>
 </tr>
 <tr>
  <td>Moolenbroek</td>
  <td>1275038</td>
  <td>10</td>
 </tr>
 <tr>
  <td>Nguyen</td>
  <td>1488651</td>
  <td>8.9</td>
 </tr>
 <tr>
  <td>Pienkowski</td>
  <td>1516841</td>
  <td>7.1</td>
 </tr>
 <tr>
  <td>Pikoulin</td>
  <td>1048961</td>
  <td>7.1</td>
 </tr>
 <tr>
  <td>Rehnstrom</td>
  <td>1516825</td>
  <td>6</td>
 </tr>
 <tr>
  <td>Slowinska</td>
  <td>1373536</td>
  <td>10</td>
 </tr>
 <tr>
  <td>Soumokil</td>
  <td>1204475</td>
  <td>7.7</td>
 </tr>
 <tr>
  <td>Streefkerk</td>
  <td>1259911</td>
  <td>6.7</td>
 </tr>
 <tr>
  <td>Tangali</td>
  <td>1275275</td>
  <td>6.6</td>
 </tr>
 <tr>
  <td>Veerman</td>
  <td>1329545</td>
  <td>8.7</td>
 </tr>
 <tr>
  <td>Verweij</td>
  <td>1204902</td>
  <td>8.1</td>
 </tr>
 <tr>
  <td>Vippola</td>
  <td>1516817</td>
  <td>6.7</td>
 </tr>
 <tr>
  <td>Wickramasinghe</td>
  <td>1488481</td>
  <td>7.9</td>
 </tr>
 <tr>
  <td>Wilde</td>
  <td>1248634</td>
  <td>6</td>
 </tr>
 <tr>
  <td>Winter</td>
  <td>1204572</td>
  <td>6.9</td>
 </tr>
 <tr>
  <td>Wissen</td>
  <td>1275313</td>
  <td>7.5</td>
 </tr>
 <tr>
  <td>Wong</td>
  <td>1204580</td>
  <td>7.4</td>
 </tr>
 <tr>
  <td>Zantvoort</td>
  <td>1517511</td>
  <td>8</td>
 </tr>
 <tr>
  <td>Zijst</td>
  <td>1351745</td>
  <td>10</td>
 </tr>
 <tr>
  <td>Zouskov</td>
  <td>1080458</td>
  <td>6.8</td>
 </tr>

</TABLE>
        

			<table border="0" cellpadding="0" cellspacing="0" summary="." width="880"><tr>
<td colspan="8"><hr></td></tr>
<tr>  <td width="16%" class="footer"><s><del>Nederlands</del></s></td>
  <td width="12%" class="footer"><a class="footer" href="http://www.vu.nl/personen/index.cfm" title="Goto the online phonebook of the Vrije Universiteit.">phonebook<img src="http://www.few.vu.nl/icon/blanco.gif" alt="" align="middle" width="2" height="14" border="0"></a></td>
  <td width="12%" class="footer"><a class="footer" href="http://www.cs.vu.nl/index-en.html" title="Goto the homepage of the department of computer science."><img src="http://www.few.vu.nl/icon/home.gif" width="14" height="14" border="0" align="middle"  alt="Goto the homepage of the department of computer science."><img src="http://www.few.vu.nl/icon/blanco.gif" alt="" align="middle" width="2" height="14" border="0">comp.sci.</a></td>
  <td width="12%" class="footer"><a class="footer" href="http://www.few.vu.nl/index-en.html" title="Goto the homepage of the faculty of sciences."><img src="http://www.few.vu.nl/icon/home.gif" width="14" height="14" border="0" align="middle"  alt="Goto the homepage of the faculty of sciences."><img src="http://www.few.vu.nl/icon/blanco.gif" alt="" align="middle" width="2" height="14" border="0">FEW</a></td>
  <td width="12%" class="footer"><a class="footer" href="http://www.english.vu.nl/" title="Goto the homepage of the Vrije Universiteit."><img src="http://www.few.vu.nl/icon/home.gif" width="14" height="14" border="0" align="middle"  alt="Goto the homepage of the Vrije Universiteit."><img src="http://www.few.vu.nl/icon/blanco.gif" alt="" align="middle" width="2" height="14" border="0">VU</a></td>
  <td width="12%" class="footer"><a class="footer" href="http://www.few.vu.nl/sitemap-en.html" title="Go to the sitemap of the faculty of sciences."><img src="http://www.few.vu.nl/icon/blanco.gif" alt="" align="middle" width="2" height="14" border="0">site map</a></td>
  <td width="12%" class="footer"><a class="footer" href="http://www.few.vu.nl/zoek-en.html" title="Search the site of the faculty of sciences."><img src="http://www.few.vu.nl/icon/blanco.gif" alt="" align="middle" width="2" height="14" border="0">search</a></td>
  <td width="12%" class="footer"><a class="footer" href="mailto:webmaster&#064;few.vu.nl"title="Send email to the Webmaster."><img src="http://www.few.vu.nl/icon/blanco.gif" alt="" align="middle" width="2" height="14" border="0">webmaster</a></td>
</tr><tr><td colspan="8"><div align="center">If you spot a mistake, please <a href="mailto:graphics&#64;few.vu.nl?subject=About%3A%20%2Fhome%2Fgraphics%2Fwww%2Fgrading.php">e-mail the maintainer</a> of this page.</div>
<div align="center" style="visibility:hidden"><b>Your browser does not fully support CSS. This may result in visual artifacts.</b></div></td></tr></table>
			</TD></TR>
		</TABLE>
		</BODY>
        </HTML>

        




