

        
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

		<A CLASS="menu" HREF="index.php">Main</A> | <A CLASS="menu" HREF="course.php">Course</A> | <A CLASS="menu" HREF="excursion.php">Excursion</A> | <A CLASS="menu" HREF="practicum.php">Practicum</A> | <A CLASS="activemenuitem" HREF="exercises.php"><B>Exercises</B></A> | <A CLASS="menu" HREF="grading.php">Grading</A> | <A CLASS="menu" HREF="faq.php">F.A.Q.</A> | <A CLASS="menu" HREF="links.php">Links</A> | <A CLASS="menu" HREF="halloffame.php">Hall of Fame</A>
		<HR NOSHADE>
		</B>
		</DIV>

		
<P>
<A HREF="#exercise 1">Exercise 1</A> and <A HREF="#exercise 2">exercise 2</A> will introduce you step-by-step to OpenGL and 
GLUT. In the <A HREF="#project">final project</A>, you will use everything you have learned in 
the exercises (and probably more). 
Both exercises have to be submitted and approved before you may start with the project. 
See the <A HREF="grading.php">grading</A> page for more information on the grading of 
these exercises. Download <A HREF="exercises.doc">this document</A> to get this page 
in MS-Word format. </P>

<HR SIZE="1" NOSHADE>

<A NAME="exercises"></A>
<H2>Remarks</H2>

<UL>
<LI>To get started, first read the <A HREF="practicum.php">practicum</A> page.</LI>
<LI>All 'important functions' that are mentioned here are without OpenGL postfixes 
	such as f, i, v, etc.</LI>
<LI>Not all functions are described in the book: The <CODE>glutil</CODE> functions are 
	described in the glutil header file. Some texture functions are not in the book, 
	but are covered during the lectures. Use google or the MSDN library in Visual C++ 
	to find specifications for these functions. The stdio functions 
	(fprintf, fopen, etc...) can be found in the UNIX man pages or also using MSDN.</LI>
<LI>There are errors in the book. Check the <A HREF="faq.php">FAQ</A>. 
	If you don't care about errors in the book, check the FAQ anyway.</LI>
</UL></P>
<HR SIZE="1" NOSHADE>

<A NAME="exercise 1"></A>
<H2>Exercise 1: the basics</H2>
<P>
Deadline: Friday, October 15, 2004, 23:59</P>
<P>
Select the exercise 1 project in Visual Studio and compile it. When running the 
program, a transparent window appears.</P>

<DL>
<DT><H3>Step 1:</H3></DT>
<DD>Adapt the <CODE>display()</CODE> function such that it clears the window 
	to dark blue. Make sure the window responds to keyboard events, namely the 
	'escape' and 'q' keys. Both of these keys should cause the window to close 
	(and application to exit).
	<P>
	<B>Important functions</B>: <CODE>glutDisplayFunc()</CODE>, <CODE>glutKeyboardFunc()</CODE>.</P>
	</DD>
<DT><H3>Step 2:</H3></DT>
<DD>Adapt the program, such that it listens to the mouse events (motion and buttons). 
	Show that it works by printing (to stdout) the position of the mouse cursor, 
	and the status of the buttons. Now, add a menu with some options to the right 
	mouse button. What is put in the menu is not important, as long as each option 
	prints something to stdout.
	<P>
	<B>Important functions</B>: <CODE>glutCreateMenu()</CODE>, <CODE>glutAddMenuEntry()</CODE>, 
	<CODE>glutAttachMenu()</CODE>, <CODE>glutMouseFunc()</CODE>, <CODE>glutMotionFunc()</CODE> 
	or <CODE>glutPassiveMotionFunc()</CODE>.</P>
	</DD>
<DT><H3>Step 3:</H3></DT>
<DD>Add an orthographic viewing volume for the GLUT window. The window should view the 
	space between X = -10..10, Y = -10..10 and Z = -1..1. Now draw a solid red square 
	from the following points: (-5,-5), (-5,5), (5,5), (5,-5). In order to make sure 
	the window will always show the square correctly, add a reshape function that 
	adjusts the viewport (the orthographic viewing volume should remain the same). 
	In other words: when the user reshapes the window, the square should always 
	remain a perfect square. No matter how the window is reshaped, the square
	should always be completely visible.
	<P>
	<B>Important functions</B>: <CODE>glutReshapeFunc()</CODE>, <CODE>glOrtho()</CODE>,
	<CODE>glMatrixMode()</CODE>, <CODE>glViewport()</CODE>, <CODE>glBegin()</CODE>, 
	<CODE>glEnd()</CODE>, <CODE>glVertex3()</CODE>.</P>
	</DD>
<DT><H3>Step 4:</H3></DT>
<DD>Add a blue wire frame square in front of the red square. 
	Now, make only the wire frame square rotate in the xy-plane. Update any 
	variables used for this animation in the idle-function. Make sure the full rotation takes 
	exactly 3 seconds. This can be achieved by using the <CODE>glutTimerFunc()</CODE> 
	callback or the <CODE>Wallclock()</CODE> function provided in the <CODE>glutil.h</CODE> 
	file (in the framework). Using the <CODE>Wallclock()</CODE> function is more reliable 
	than the <CODE>glutTimerFunc()</CODE> callback, since the latter will not be called often enough 
	when your PC is too slow. The user should be able to stop the animation in the scene by 
	pressing the spacebar. When user presses the spacebar again, the animation should resume where 
	it was paused. </P>
	<P>To make the motion smooth, double buffering will be required. 
	Beware: if your square is flickering, you do not use double buffering correctly!
	<P>
	<B>Important functions</B>: <CODE>glutIdleFunc()</CODE>, <CODE>glutSwapBuffers()</CODE>, 
	<CODE>glutInitDisplayMode()</CODE>, <CODE>glColor3()</CODE>, <CODE>glRotate()</CODE>, 
	<CODE>glutTimerFunc()</CODE>, <CODE>Wallclock()</CODE>.</P>
	</DD>
</DL>

<HR SIZE="1" NOSHADE>
<P ALIGN="center">
Do not forget to <A HREF="grading.php#Submission">submit</A> exercise 1 before Friday, October 15, 2004, 23:59</P>
<HR SIZE="1" NOSHADE>

<A NAME="exercise 2"></A>
<H2>Exercise 2: solar system</H2>
<P>
Deadline: Friday, November 26, 2004, 23:59</P>

<P>
Now we can proceed with perspective projection. First apply step 1.1 and 1.2 
to the main of the exercise 2 project. Like step 1.4 all following steps must:
<UL>
<LI> Use double-buffering. </LI>
<LI> Relate animation to the clock and not to the CPU. This will allow animation 
that is independent of the CPU and graphics card hardware.</LI>
<LI> Pause or resume all animation when the spacebar is pressed.</LI>
</UL> 
</P>

<DL>
<DT><H3>Step 1:</H3></DT>
<DD>Write a program that implements a perspective viewing volume. Note that 
	<CODE>glOrtho()</CODE> should not be used anymore and that you must now choose  
	some proper values for either <CODE>glFrustum()</CODE> or 
	<CODE>gluPerspective()</CODE> (whichever you think is easier to use).
	<P>
	Now, make a solid pyramid (with a square ground face) that rotates and has 
	a different color for each face. To make sure the pyramid looks correct, 
	turn on depth-buffering. Rotation of the pyramid should be connected to the 
	left mouse button: pressing this button makes the pyramid rotate around a
	certain axis (which one does not matter), releasing it should stop this rotation. 
	</P><P>    
	<B>Important functions</B>: <CODE>glFrustum()</CODE>, <CODE>gluPerspective()</CODE>, 
	<CODE>glEnable()</CODE>.</P>
	</DD>
<DT><H3>Step 2:</H3></DT>
<DD>Add a solid cube to the scene. Again, each face of the cube should have 
	a different color. This cube should be rotating in orbit around the 
	pyramid (i.e. rotating around it, at a reasonable distance, independent 
	from the rotation of the pyramid). Additionally, the cube should rotate 
	around its own (x-)axis.
	<P> 	
	<B>Important functions</B>: <CODE>glPushMatrix()</CODE>, <CODE>glPopMatrix()</CODE>, 
	<CODE>glTranslate()</CODE>.</P>
	</DD>
<DT><H3>Step 3:</H3></DT>
<DD>Now we will add a more complex shape to the scene. This shape is defined in 
	the <CODE>models/f-16/</CODE> directory by several <CODE>.sgf</CODE> files. 
	Write a loader for this file format (it is described in <CODE>models/sgf_format.txt</CODE>).
	For now, the 'normal' values can be ignored. The f-16 should be in orbit around the 
	spinning cube. The rotation of the f-16 should be independent of the rotation of 
	the cube. This loader will be used and perhaps altered in your final project, 
	so make sure it is programmed nicely and works well.
	<P> 
	<B>Important functions</B>: <CODE>fopen()</CODE>, <CODE>fscanf()</CODE>, <CODE>fclose()</CODE>.</P>
	</DD>
<DT><H3>Step 4:</H3></DT>
<DD>Add lights to the scene. To enable lighting, several OpenGL calls must be made 
	to setup the light(s) and for each object material properties must be defined. 
	Finally, the vertices of the objects must be given 'normal' vectors, which are 
	used by OpenGL to calculate the reflection angles of the light. For the f-16, 
	use the normal values specified in the <CODE>.sgf</CODE> file. Also, add a 
	menu to switch between 'smooth' and 'flat' shading.</CODE>
	<P ALIGN="center">
	<IMG SRC="images/f16-1.jpg" WIDTH="250" HEIGHT="228" BORDER="1" ALT="F-16"> 
	<IMG SRC="images/f16-2.jpg" WIDTH="195" HEIGHT="228" BORDER="1" ALT="F-16"><BR>
	<SPAN CLASS="caption">Two pictures of the f-16 object.</SPAN></P>
	<P>		
	<B>Important functions</B>: <CODE>glShadeModel()</CODE>, <CODE>glLight()</CODE>, 
	<CODE>glMaterial()</CODE>, <CODE>glNormal()</CODE>.</P>
	</DD>
<DT><H3>Step 5:</H3></DT>
<DD>Add textures to the scene. Use the texture <CODE>textures/text.rgb</CODE> 
	(in the framework file) to map onto the faces of the pyramid. rgb files can be 
	loaded with the <CODE>LoadRGB()</CODE> call provided in <CODE>util/readtex.c</CODE> file. 
	The bottom face (the square) should then show the text &quot;Computer Graphics&quot;. 
	Now, add a menu to the right mouse button that allows switching between 3 modes:
	<UL>
	<LI>Display the full texture on the bottom face.</LI>
	<LI>Display only the word &quot;Computer&quot; on the bottom face by changing 
		the texture coordinates accordingly.</LI>
	<LI>Display 8 times &quot;Computer Graphics&quot; on the bottom face 
		(a 4x2 grid).</LI>
	</UL>
	How the texture is attached to the triangular faces of the pyramid is up to you.
	In order to display the texture correctly, you will need to setup a texture environment. 
	Add another menu where you can choose between:
	<UL>
	<LI>Modulate (multiply the face colors with the texture colors).</LI>
	<LI>Replace (show only the texture colors).</LI>
	</UL>
	<P>
	<B>Important functions</B>: <CODE>LoadRGB()</CODE>, <CODE>glGenTextures()</CODE>, 
	<CODE>glTexEnv()</CODE>, <CODE>glBindTexture()</CODE>, <CODE>glTexParameter()</CODE>, 
	<CODE>glTexImage2D()</CODE>, <CODE>glTexCoord()</CODE>.</P>
	</DD>
</DL>

<HR SIZE="1" NOSHADE>
<P ALIGN="center">
Do not forget to <A HREF="grading.php#Submission">submit</A> exercise 2 before Friday, November 26, 2004, 23:59</P>
<HR SIZE="1" NOSHADE>

<A NAME="project"></A>
<H2>Final Project</H2>
<B>(graded program)</B>

<P>
Deadline: Friday, January 7, 2005, 23:59</P>
<P>
You are not allowed to start with the final project before exercise 1 and 2 have 
been submitted and approved. For this project, you can choose between two themes: 
dinosaur or pony. The difficulty is exactly the same. You get plenty of freedom 
implementing it: choice of colors, textures, landscape.</P>

<P>
<TABLE WIDTH="100%" BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR><TD ALIGN="left" VALIGN="top">
		<P>
		The following parts are mandatory:
		<OL>
		<LI>Use the model(s) available in the <CODE>models/dino/</CODE> and <CODE>models/pony/</CODE>
			directories (use the <CODE>.sgf</CODE> loader from exercise 2).</LI>
		<LI>Build the model(s): combine the different body parts by translations, rotations, etc.</LI>
		<LI>Animate the model(s) in a simple manner (legs and arms should move, 
			in a cycling manner, using some 'sin/cos' functions).</LI>
		<LI>Apply texture(s) to the model (you are allowed to use random texture coordinates. 
			You may need to change your <CODE>.sgf</CODE> loader to support these random 
			texture coordinates).</LI>
		<LI>Build a 'random' city (e.g. a set of boxes) onto a plane.</LI>
		<LI>Put a texture onto the buildings and the ground.</LI>
		<LI>Give the character a trajectory (line, circle, sinus function, 
			data from a file, user control, etc.).</LI>
		<LI>Put in a background color (e.g. sky blue).</LI>
		<LI>Make the viewpoint (camera) interactive: translation and rotation 
			of the world, allowing an interactive walk through the scene.</LI>
		<LI>Add a light to the scene.</LI>
		<LI>Use the framework! Put files where they are supposed to be. 
			If you use extra textures, put them in the <CODE>texture/</CODE> directory. 
			If you add <CODE>.c</CODE> or <CODE>.h</CODE> files, put them in your 
			<CODE>project/</CODE> directory. If you create your own models, put 
			them in the <CODE>models/</CODE> directory.</LI>
		<LI>Make the program stand alone! It is not sufficient if your program runs 
			from Visual Studio. It should be able to run from the <CODE>bin/</CODE> 
			directory directly.</LI>
		<LI>Clean your project before submission. We will not accept submissions 
			over 1 MB (or 2 MB if you use your own textures).</LI>
		<LI>Your program should use the timer functions for animation and space bar 
			should pause and resume the application.</LI>
		</OL>
		All these requirements <I>must</I> be implemented to get any grade at all. 
		Without any extras, your grade will be between 5.5 and 7 
		(depending on how well it was programmed). To get a higher grade, 
		extra features should be implemented. Easy points can be gained by adding, 
		for example, fog, extra lights, extra textures (other than the ones given by us) 
		and by putting more than one animated dino or pony into the scene. More complex 
		(and more rewarding) extras are, for example: collision detection, transparency, 
		shadows and adding other complex objects (such as bridges, trees, 
		hills (e.g. curved landscape), complex dungeons with stairs, etc.). 
		Advanced interaction will also add to the final grade: intuitive camera controls, 
		interaction with dino or pony, making it a game, etc. Creativity is the key in 
		making us generous with points. Finally, bonus points are awarded for fancy 
		programming features such as using display lists and/or a scene graph.</P>
		<P> 
		With all these points it is, at least in theory, possible to exceed 10 points. 
		Due to regulations any grade exceeding 10 will be rounded down to 10.</P>
	</TD>
	<TD ALIGN="center" VALIGN="top">
		<A HREF="images/example-dino-big.jpg">
		<IMG SRC="images/example-dino-small.jpg" WIDTH="250" HEIGHT="259" BORDER="1" ALT="Example Dinosaur project by Olli Juhani Miettinen (2002)"></A><BR>
		<SPAN CLASS="caption">Example Dinosaur project by Olli Juhani Miettinen (2002)</SPAN><BR>
		<BR>
		<A HREF="images/example-pony-big.jpg">
		<IMG SRC="images/example-pony-small.jpg" WIDTH="250" HEIGHT="194" BORDER="1" ALT="Example Pony project by Jasper van der Sterren (2002)"></A><BR>
		<SPAN CLASS="caption">Example Pony project by Jasper van der Sterren (2002)</SPAN><BR>
	</TD></TR>
</TABLE></P>


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
</tr><tr><td colspan="8"><div align="center">If you spot a mistake, please <a href="mailto:graphics&#64;few.vu.nl?subject=About%3A%20%2Fhome%2Fgraphics%2Fwww%2Fexercises.php">e-mail the maintainer</a> of this page.</div>
<div align="center" style="visibility:hidden"><b>Your browser does not fully support CSS. This may result in visual artifacts.</b></div></td></tr></table>
			</TD></TR>
		</TABLE>
		</BODY>
        </HTML>

        