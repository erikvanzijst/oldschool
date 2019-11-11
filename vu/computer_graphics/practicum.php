

        
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

		<A CLASS="menu" HREF="index.php">Main</A> | <A CLASS="menu" HREF="course.php">Course</A> | <A CLASS="menu" HREF="excursion.php">Excursion</A> | <A CLASS="activemenuitem" HREF="practicum.php"><B>Practicum</B></A> | <A CLASS="menu" HREF="exercises.php">Exercises</A> | <A CLASS="menu" HREF="grading.php">Grading</A> | <A CLASS="menu" HREF="faq.php">F.A.Q.</A> | <A CLASS="menu" HREF="links.php">Links</A> | <A CLASS="menu" HREF="halloffame.php">Hall of Fame</A>
		<HR NOSHADE>
		</B>
		</DIV>

		
<H2>General remarks</H2>

<a name="registration">A few words in advance:</a>
<p>

<UL>
<LI>
<P> 
You have to register for the practical work.
Unfortunately, using the TISVU system turned out to be too restrictive
to be useful. You can put you name on a sheet of paper we pass around
in the first few lectures.
</P>
</LI>
<LI>
<P> 
The programming work has to be done with the OpenGL graphics library. Programs 
are to be written in the C programming language. If you are unfamiliar with 
C, you may want to read the guide <a href="http://www.cs.vu.nl/~jason/college/dictaat-two-page.ps">C for 
Java Programmers</a> by Jason Maassen. Using C++ is allowed.
</P>
</LI>
<LI> 
<P>
<img src="images/jemagniks.jpg" width="301" height="155" border="1" alt="Obey the rules!" align="right">
You have to do the programming assignments individually.
Solutions handed in by groups of students will not be accepted!
</P>
<P> 
Every year we detect several students copying code from 
eachother. Whenever we suspect that code has been copied, all 
suspected students are immediately brought to the exam-committee and 
-if found guilty- are excluded from the practicum. Note that this normally
includes the poor student who wrote the original code. We also compare to 
exercises of previous years. 
</P>
</LI>
<LI>
<P>
You are only allowed to use (i.e. copy) code from internet sources, if you 
state the url in comment next to the code in your sources. If you 'forget' 
to state the source, it will be treated as fraud and the exam-committee 
will be notified as stated before. 
</P>
<P>
You are never allowed to use internet sources that publish submissions or 
parts of submissions to the VU Computer Graphics practicum.
</P>
</LI>
<LI>
<P>
You are strongly discouraged to use other libraries than the glut library 
that comes with the project. We will not debug your linking problems. If your 
program fails to compile immediately, your submission is considered incorrect.
</P>
</LI>
<LI> 
Documentation/comments have to be presented in English language.
Please keep in mind that some people reviewing your solutions do
not understand Dutch! (And you don't want to fail the course just
because of language problems? ;-) 
</P>
<P>
We do not expect "perfect prose" from you, but your writing should
be understandable...
</P>
</LI>
<LI> 
<P>
You may also do the programming work at home. (e.g. with a Linux PC).
But your solution finally
has to be on the VU Windows machines (and to run there!)
in order to get you credits for the assignments. 
</P>
</LI>
</UL>

<HR SIZE="1" NOSHADE>

<A NAME="deadlines"></A>
<H2>Deadlines</H2>

<P>
The deadlines for submitting your exercises and project are:</P>

<P>
<TABLE BORDER="0" CELLPADDING="5" CELLSPACING="2">
<TR><TD>
        Exercise 1:
    </TD>
    <TD>
      	Friday, October 15, 2004, 23:59        </TD></TR>
<TR><TD>
        Exercise 2:
    </TD>
    <TD>
      	Friday, November 26, 2004, 23:59        </TD></TR>
<TR><TD>
        Final project:
    </TD>
    <TD>
      	Friday, January 7, 2005, 23:59        </TD></TR>
</TABLE></P>

<P>
Note that these deadlines are <I>strict</I>. If you cannot make a deadline, 
<A HREF="mailto:kielmann@cs.vu.nl">ask Thilo Kielmann</A> for an extension <I>before</I> the deadline expires.
Have a very good excuse ready.</P>

<HR SIZE="1" NOSHADE>

<H2>Getting started</H2>
<P>
The practical work will be done in C using Microsoft Visual C++ 6.0. This means
you can use the computers in the rooms S-3.29, S-3.45, S-353, P-4.69 and P-4.23 of
the computer science department. If you don't have an account there, 
contact the helpdesk in room S-4.09A (opposite to S-4.08). The helpdesk is open 
from 09:30-11:30 and from 13:30-17:30. You can also send an email to 
<A HREF="mailto:helpdesk@cs.vu.nl">helpdesk@cs.vu.nl</A>. If the helpdesk is 
not able to help you, or if there is something very urgent at the time that 
the helpdesk is not available, you can contact systeembeheer in room S-4.08.</P>

<P>
When you've got an account, its time to start with the practical work.
<OL>
<LI><P>
	Login in one of the rooms mentioned above as &quot;student&quot;, password &quot;student&quot;.
	After that you also have to provide your UNIX username and password.</P></LI>
<LI><P>
	Download the file <CODE><A HREF="framework.zip">framework.zip</A></CODE>
	and unzip it somewhere in your home account on <NOBR>drive H.</NOBR>
	This creates a directory &quot;<CODE>graphics</CODE>&quot; with the 
	following subdirectories:</P>
	<TABLE BORDER="0" CELLPADDING="5" CELLSPACING="0">
	<TR><TD VALIGN="top"><CODE>bin</CODE></TD>
		<TD VALIGN="top">the executables of all exercises and the GLUT library</TD></TR>
	<TR><TD VALIGN="top"><CODE>Debug</CODE></TD>
		<TD VALIGN="top">used by Visual C++</TD></TR>
	<TR><TD VALIGN="top"><CODE>exercise1/2</CODE></TD>
		<TD VALIGN="top">the place for your exercise files</TD></TR>
	<TR><TD VALIGN="top"><CODE>project</CODE></TD>
		<TD VALIGN="top">the place for your final project</TD></TR>
	<TR><TD VALIGN="top"><CODE>textures</CODE></TD>
		<TD VALIGN="top">some textures that are needed in exercise 2 and can be	
			used in the final project</TD></TR>
	<TR><TD VALIGN="top"><CODE>models</CODE></TD>
		<TD VALIGN="top">the model of the f-16 for exercise 2 and the dino and pony models for the final project</TD></TR>
	<TR><TD VALIGN="top"><CODE>util</CODE></TD>
		<TD VALIGN="top">some utility code to load textures, manage timing etc.</TD></TR>
	<TR><TD VALIGN="top"><CODE>GL</CODE></TD>
		<TD VALIGN="top">directory for the glut header file</TD></TR>
	</TABLE></P></LI>
<LI><P>
	Start Microsoft Visual C++ (<CODE>Start -> Programs -> Microsoft Visual Studio 6.0 -> Microsoft Visual C++ 6.0)</CODE></P></LI>
<LI><P>
	Choose <CODE>File -> Open Workspace</CODE> and load the file <CODE>graphics/cg.dsw</CODE>.
	<P>
	This loads your computer graphics workspace, which contains three 
	(currently empty) projects: one for each exercise and one for the
	final project.</P></LI>
<LI><P>
	Right click on the entry &quot;exercise 1 files&quot; in the File View menu on the left. 
	Select &quot;Set as Active Project&quot; from the pop-up menu. The project will be in bold 
	face now, to indicate it is the project that will be compiled. Double-click on the project
	to unfold it. You will see several maps containing the different files for this specific
	project. &quot;Source Files&quot; and &quot;Header Files&quot; are the important ones. We 
	already added several files to the project: main.c (the main code for the exercise) and 
	glutil.c/glutil.h (containing several platform independant utility functions). Later on, 
	you can add your own files to the project. However, for the first exercise, you can 
	write all code in main.c.</P></LI>
<LI><P>
	Test if everything works by compiling and running exercise 1:
	<P>
	<OL>
	<LI>Press <CODE>F7</CODE> to build the program. The compiler output in the
		lower window should indicate 0 error(s) and 0 warning(s). <B> Remember that only 
		the active project will be compiled! </B> </LI>
	<LI>Press <CODE>F5</CODE> to run the program or run the executable in the <CODE>graphics/bin</CODE> directory.</LI>
	</OL></P>
	<P>
	(for more information about how to use Visual C++, see the <A HREF="faq.php">frequently asked questions</A>).</P>
	<P>
	You will now see a very interesting empty window called &quot;exercise 1&quot; and an empty DOS-prompt. It's time to do better. Lets go to 
	<A HREF="exercises.php">the exercises</A>.</P></LI>
</OL></P>

<HR SIZE="1" NOSHADE>

<A NAME="Submission"></A>
<H2>Submission</H2>

<P>
To submit exercises 1 and 2 or the final project, do the following:
<OL>
<LI><P>
	Run <CODE>Build -> Clean</CODE> for each exercise in your workspace to save diskspace.</P>
<LI><P>
	Add the (root) directory <CODE>graphics</CODE> to a zipfile<BR>
	(Right-click on it in Windows Explorer and choose <CODE>WinZip -> Add to zip</CODE>)</P>
<LI><P>
	Mail the zipfile to <A HREF="mailto:graphics@cs.vu.nl">graphics@cs.vu.nl</A> with the subject &quot;submission exercise 1&quot;,
	&quot;submission exercise 2&quot; or &quot;submission project&quot;, depending on what
	you're submitting. Don't forget to put your <I>full name</I> and 
	<I>student number</I> in the email.</P>
</OL></P>


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
</tr><tr><td colspan="8"><div align="center">If you spot a mistake, please <a href="mailto:graphics&#64;few.vu.nl?subject=About%3A%20%2Fhome%2Fgraphics%2Fwww%2Fpracticum.php">e-mail the maintainer</a> of this page.</div>
<div align="center" style="visibility:hidden"><b>Your browser does not fully support CSS. This may result in visual artifacts.</b></div></td></tr></table>
			</TD></TR>
		</TABLE>
		</BODY>
        </HTML>

        