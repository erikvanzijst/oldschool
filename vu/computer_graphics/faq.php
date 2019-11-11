

        
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

		<A CLASS="menu" HREF="index.php">Main</A> | <A CLASS="menu" HREF="course.php">Course</A> | <A CLASS="menu" HREF="excursion.php">Excursion</A> | <A CLASS="menu" HREF="practicum.php">Practicum</A> | <A CLASS="menu" HREF="exercises.php">Exercises</A> | <A CLASS="menu" HREF="grading.php">Grading</A> | <A CLASS="activemenuitem" HREF="faq.php"><B>F.A.Q.</B></A> | <A CLASS="menu" HREF="links.php">Links</A> | <A CLASS="menu" HREF="halloffame.php">Hall of Fame</A>
		<HR NOSHADE>
		</B>
		</DIV>

		
<H2>Frequently Asked Questions</H2>

<UL>
<LI>
	<H3>How can I create my own RGB textures?</H3></LI>
    <OL>
    <LI>Find or create a nice picture (can be any format)</LI>
    <LI>Make sure the width and height are powers of 2 (for example 128 x 128 or 64 x 256 pixels).<BR>
        Available tools to accomplish this are:
        <UL>
        <LI>Adobe Photoshop (in Windows)</LI>
        <LI>the GIMP (in Unix)</LI>
        <LI><CODE>mogrify -geometry &lt;width&gt;x&lt;height&gt; &lt;file(s)&gt;</CODE> (in Unix)</LI>
        </UL></LI>
    <LI>Convert it to an SGI RGB image. You can use the Unix tool <CODE>convert</CODE> to do this.
        Example: 
        <BLOCKQUOTE><CODE>convert foobar.jpg sgi:foobar.rgb</CODE></BLOCKQUOTE>
        creates a file <CODE>foobar.rgb</CODE> from your <CODE>foobar.jpg</CODE> image.
    </OL>
    </LI>
<LI>
	<H3><CODE>glGenTextures()</CODE> and <CODE>glBindTexture()</CODE> are not in the book; how do I use them?</H3>
	These functions are not part of the original OpenGL 1.0 specs and are not in the book for that reason. 
	Ways to find out how they work:
	<UL>
	<LI>Read the <A HREF="course.php">course slides</A> about texturing</LI>
	<LI>Use the MSDN: type the function name in Visual Studio and press F1</LI>
	<LI>Use <A HREF="http://www.3dlabs.com/support/developer/glmanpage_index.htm">online OpenGL documentation</A></LI>
	</UL>
    </LI>
<LI>
	<H3>When I add lighting I do not see colors anymore</H3>
	When lighting is enabled, OpenGL does not use the values set with <CODE>glColor()</CODE>,
	but only looks at the material properties set with <CODE>glMaterial()</CODE>. The exact lighting equation, used by OpenGL 
	to calculate the color of each vertex in the scene, can be found in the
	<A HREF="http://fly.cc.fer.hr/~unreal/theredbook/chapter06.html">Red Book</A>. A slightly simpler version of it
	(leaving out some spotlight- and ambient lightmodel stuff) looks like this:
	<BLOCKQUOTE>
	<CODE>
	vertex color =<BR>
	&nbsp;&nbsp;&nbsp;&nbsp;emissionmaterial +<BR>
	&nbsp;&nbsp;&nbsp;&nbsp;ambientlight * ambientmaterial +<BR>
	&nbsp;&nbsp;&nbsp;&nbsp;sum over all the lights 0 to N - 1:<BR>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;max( l * n , 0 ) * diffuselight * diffusematerial +<BR>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;max( s * n , 0 ) * shininess * specularlight * specularmaterial<BR>
	</CODE>
	</BLOCKQUOTE>
	with
	<BLOCKQUOTE>
	<CODE>N</CODE> = the number of lights in the scene<BR>
	<CODE>l</CODE> = (lx, ly, lz), the unit vector that points from the vertex to the light position<BR>
	<CODE>n</CODE> = (nx, ny, nz), the unit normal vector at the vertex<BR>
	<CODE>s</CODE> = (sx, sy, sz), the normalized sum of the vector from the vertex to the lightposition and the vertex to the viewpoint<BR>
	</BLOCKQUOTE>
	&lt;phew&gt;...so the simple guidelines you can learn from this are:
	<UL>
	<LI>Fiddle with ambient-, emissive-, diffuse- and shininess (using <CODE>glMaterial()</CODE>)
	    to set the final color of a vertex.</LI>
	<LI>Be careful with large values; the final color is a sum of its parts, and a color value larger
		than 1 will be clamped to 1. This means that, for example, when both <CODE>ambientlight</CODE>
		and <CODE>ambientmaterial</CODE> are set to (1, 1, 1)
		everything will be white, no matter what the other material properties are.</LI>
	</UL>
    </LI>
<LI>
	<H3>Where should and where shoudn't I use <CODE>gl*</CODE> and <CODE>glut*</CODE> calls?</H3>
	<UL>
	<LI><B><CODE>main()</CODE></B>: only <CODE>glut*</CODE> calls</LI>
	<LI><B><CODE>idle()</CODE></B>: only <CODE>glut*</CODE> calls (most likely only <CODE>glutPostRedisplay()</CODE>)</LI>
	<LI><B><CODE>display()</CODE></B>: only <CODE>gl*</CODE> calls</LI>
	</UL>
    </LI>
<LI>
	<H3>The book is not correct!?</H3>
    Yes, the book (Angel's book, 3th edition) is not perfect. Some of the important errors we spotted so far:
    <UL>
    <LI><B>pg. 333</B>: <CODE>glTexParameteri(GL_TEXTURE_WRAP_S, GL_REPEAT)</CODE> should be 
        <CODE>glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)</CODE>.</LI>
    <LI><B>pg. 336</B>: <CODE>glTexEnvi(GL_TEX_ENV, GL_TEX_ENV_MODE, GL_MODULATE)</CODE> should be 
        <CODE>glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE)</CODE>.</LI>
    <LI><B>pg. 432</B>: The picture 9.15b does not represent 9.15a and contains many errors as well. A picture that explains left-child right-sibling and properly represents 9.15a, can be found in the slides of the lecture on scene graphs and rendering hierarchies.</LI>
    </UL>
	The author has published some <A HREF="http://www.cs.unm.edu/~angel/BOOK/THIRD_EDITION/errata.txt">other errata</A> on his website.
    </LI>
<LI>
	<H3>Jumpy color shifts with light and smooth shading?</H3>
    When using light and materials, make sure you set the shininess to a reasonable
    (greather than 1) value. The default value is 0, which can produce strange 
    jumpy color changes on moving surfaces. Setting the shininess is done with, 
    for example, <CODE>glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, 12)</CODE>. <BR>
    </LI>

<LI><P>
	<H3>I use glutSwapBuffers() but the screen still flickers.</H3>
    To enable double buffering, you must change the call <CODE>glutInitDisplayMode(GLUT_RGB)</CODE>
    in <CODE>main()</CODE> to <CODE>glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE)</CODE>, otherwise
    you won't be using any double buffering at all. <BR>
    </LI>

<LI>	
	<H3>Why does my mouse cursor flicker?</H3>
    The PCs on the VU seem to have the weird problem that the mouse cursor sometimes flickers
	when using animations (like exercise 1, step 4). So far we have not figured out how to avoid this.
	Just ignore it.
    </LI>

<LI>
	<H3>How do I use the idle function correctly in exercise 1 step 4?</H3>
    First of all, you should not use any <CODE>gl*</CODE> calls in the idle function, only
    <CODE>glut*</CODE> calls. Second, you should put your &quot;program logic&quot; in the idle function.
    This means, for example, updating rotation angle variables. Third, you should always load the
    identity matrix at the beginning of the display function. Now figure out yourself how to implement
    the rotating square ;).<BR>
    </LI>
<LI>
	<H3>How can I add my own files to the Visual Studio Workspace?</H3>
	To add a C or C++ file to a project, right-click on the project and select 'Add files to project'. In the directory browser, select the proper directory for your new file (the directory that also contains the <CODE>main.c</CODE> for that project. Now you can type in the name of a new file (or select an existing file if you already copied it there). Now the file has been added to the project. <I>Do not forget to add the proper '<CODE>#include</CODE>' statements in your files or the new files will not be compiled.</I> <BR>
	</LI>
<LI>
	<H3>The answer to my question isn't in this list or on this website. Where can I find more information?</H3>
	Mail your question to <A HREF="mailto:graphics@cs.vu.nl">graphics@cs.vu.nl</A>.
</LI>
</UL>
        

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
</tr><tr><td colspan="8"><div align="center">If you spot a mistake, please <a href="mailto:graphics&#64;few.vu.nl?subject=About%3A%20%2Fhome%2Fgraphics%2Fwww%2Ffaq.php">e-mail the maintainer</a> of this page.</div>
<div align="center" style="visibility:hidden"><b>Your browser does not fully support CSS. This may result in visual artifacts.</b></div></td></tr></table>
			</TD></TR>
		</TABLE>
		</BODY>
        </HTML>

        

