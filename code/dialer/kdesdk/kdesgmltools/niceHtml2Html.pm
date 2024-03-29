#
#  Html2Html.pm
#
#  $Id: niceHtml2Html.pm,v 1.5 1998/01/11 02:12:02 panayoti Exp $
#
#  Convert parsed linuxdoc-sgml to html.
# 	- Split files; match references, generate TOC and navigation
# 	aids, etc.
#
#  Rules based on html2html.l
#

# Hacked by Panayotis Vryonis <vrypan@hol.gr>
# to be used by kdesgmltools.
# 11 Jan 1998
#

package SGMLTools::niceHtml2Html;

use FileHandle;
use SGMLTools::Lang;

# Externally visible variables
$html2html = {};

# Initialize: set splitlevel, extension, images, language, and filename
# Usage: &{$html2html->{init}}(split,ext,img,filename,filenum,label);
#       split level:	 0 - super page mode
#        		 1 - big page mode
#        		 2 - small page mode
$html2html->{init} = sub {
    $splitlevel = shift;
    SWITCH: {
        $super_page_mode = 0, $big_page_mode = 1, last SWITCH
            if ($splitlevel == 1);
        $super_page_mode = 0, $big_page_mode = 0, last SWITCH
            if ($splitlevel == 2);
    }
    $fileext = shift;
    $use_imgs = shift;
    $firstname = shift;
    $filecount = 1 + shift;
    $lprec = shift;
    $nextlabel = Xlat ("Next");
    $prevlabel = Xlat ("Previous");
    $toclabel = Xlat ("Table of Contents");

    # Read the "NiceIncludeFile (nif)" and put its contents in apropriate strings.
    my $mode = 0 ;
    $partA = "" ;
    $partB = "" ;
    $partC = "" ;
    $bartags1 = "" ;
    $bartags2 = "" ;
    my $nifh = new FileHandle "<$firstname.nif"  
       	or die qq(nicehtml2html: Fatal: Could not open file $firstname.nif\n);
    while (<$nifh>) { 
	SWITCH: {
	$mode="A" , last SWITCH if (/<--A-->/);
	$mode="B" , last SWITCH if (/<--B-->/);
	$mode="C" , last SWITCH if (/<--C-->/);
	$mode="bartags1" , last SWITCH if (/<--BarTags1-->/);
	$mode="bartags2" , last SWITCH if (/<--BarTags2-->/);
	$partA = join("", $partA, qq($_)), last SWITCH if ($mode =~ "A") ;
	$partB = join("", $partB, qq($_)), last SWITCH if ($mode =~ "B") ;
	$partC = join("", $partC, qq($_)), last SWITCH if ($mode =~ "C") ;
	$bartags1 = join("", $bartags1, qq($_)), last SWITCH if ($mode =~ "bartags1") ;
	$bartags2 = join("", $bartags2, qq($_)), last SWITCH if ($mode =~ "bartags2") ;
	}
    }
    $nifh->close ;
    #
};

# Package variables
$big_page_mode = 0;             # '-2' subsection splitting
$super_page_mode = 1;		# One page vs. page/section
$chapter_mode = 0;              # <article> vs. <report>
$current = "";                  # State of section/subsection/etc.
$filenum = 1;                   # Current output file number
$filecount = 1;
$firstname = "$$";              # Base name for file
$headbuf = "";                  # Buffer for URL's
$fileext = "html";       	# "html" vs. "htm" for 8.3
$in_appendix = 0;               # n.n vs. a.n section numbers
$in_section_list = 0;           # List of sections flag
$language = "";                 # Default English; use '-Lname'
# $lprec{label}                 # Label record
$nextlabel = "";		# Link string
$outfh = STDOUT;		# Output filehandle
$outname = "";			# Output file name
$prevlabel = "";		# Link string
$refname = "";                  # Ref string
$sectname = "";                 # Section name
$secnr = 0;                     # Section count
$ssectname = "";                # Subsection name
$ssecnr = 0;                    # Subsection count
$skipnewline = 0;               # Flag to ignore new line
$toclabel = "";			# Link string
$titlename = "";                # Title of document
$use_imgs = 0;                  # '-img' pictorial links
$urlname = "";                  # Name for url links

# Ruleset
$html2html->{rules} = {};		# Individual parsing rules

$html2html->{rules}->{'^<@@appendix>.*$'} = sub {
    $in_appendix = 1; $secnr = 0; $ssecnr = 0;
};

$html2html->{rules}->{'^<@@url>(.*)$'} = sub {
    $skipnewline = 1; $urlname = $1; $headbuf = qq(<A HREF="$1">);
};

$html2html->{rules}->{'^<@@urlnam>(.*)$'} = sub { 
    $headbuf = $headbuf . "$urlname</A>"; 
};

$html2html->{rules}->{'^<@@endurl>.*$'} = sub {
    $skipnewline = -1; $outfh->print($headbuf); $headbuf = "";
};

$html2html->{rules}->{'^<@@title>(.*)$'} = sub {
    $titlename = $1; &heading(STDOUT); print(STDOUT "<H1>$1</H1>\n\n");
};

$html2html->{rules}->{'^<@@head>(.*)$'} = sub { 
    $skipnewline = 1; $headbuf = $1; 
};

$html2html->{rules}->{'^<@@part>.*$'} = sub { $current = "PART"; };

$html2html->{rules}->{'^<@@endhead>.*$'} = sub {
    SWITCH: {
    $outfh->print("<H2>$headbuf</H2>\n\n"), last SWITCH 
        if ($current eq "PART");
    $outfh->print("<H2>$headbuf</H2>\n\n"), last SWITCH 
        if ($current eq "CHAPTER");
    $outfh->print("<H3>$headbuf</H3>\n\n"), last SWITCH 
        if ($current eq "SECTION");
    $outfh->print("<H3>$headbuf</H3>\n\n"), last SWITCH 
        if ($current eq "SUBSECT");
    $outfh->print("<H4>$headbuf</H4>\n\n"), last SWITCH;
    }
    $current = ""; $headbuf = ""; $skipnewline = 0;
};

$html2html->{rules}->{'^<@@chapt>(.*)$'} = sub {
    $chapter_mode = 1; $skipnewline = 1; $sectname = $1;
    &start_chapter($sectname);
};

$html2html->{rules}->{'^<@@sect>(.*)$'} = sub {
    $skipnewline = 1; $ssectname = $1;
    if ($chapter_mode) {
        &start_section($ssectname);
    } else {
        $sectname = $ssectname; &start_chapter($ssectname);
    }
};

$html2html->{rules}->{'^<@@ssect>(.*)$'} = sub {
    $skipnewline = 1; $ssectname = $1;
    if (!$chapter_mode) {
        &start_section($ssectname);
    } else {
        $current = ""; $headbuf = $ssectname;
    }
};

$html2html->{rules}->{'^<@@endchapt>.*$'} = sub {
    STDOUT->print("</UL>\n") if ($in_section_list);
    if ($outfh->fileno != STDOUT->fileno) {
        &footing($outfh); $outfh->close; $outfh = STDOUT;
    }
};

$html2html->{rules}->{'^<@@endsect>.*$'} = sub {
    STDOUT->print("</UL>\n") if (!$chapter_mode && $in_section_list);
    if (($outfh->fileno != STDOUT->fileno) 
           && ((!$chapter_mode) || (!$big_page_mode))) {
        &footing($outfh); $outfh->close; $outfh = STDOUT;
    }
};

$html2html->{rules}->{'^<@@endssect>.*$'} = sub {
    if (($outfh->fileno != STDOUT->fileno) 
           && (!$chapter_mode) && (!big_page_mode)) {
        &footing($outfh); $outfh->close; $outfh = STDOUT;
    }
};

$html2html->{rules}->{'^<@@enddoc>.*$'} = sub { };

$html2html->{rules}->{'^<@@label>(.*)$'} = sub {
    if (!defined($lprec->{$1})) {
        STDERR->print(qq(html2html: Problem with label "$1"\n)); next;
    }
    if ($skipnewline) {
        $headbuf = sprintf(qq(<A NAME="%s"></A> %s), $1, $headbuf);
    } else {
        $outfh->print(qq(<A NAME="$1"></A> ));
    }
};

$html2html->{rules}->{'^<@@ref>(.*)$'} = sub {
    my $tmp;

    $refname = $1;
    if (!defined($lprec->{$1})) {
        STDERR->print(qq(html2html: Problem with ref "$1"\n));
        $skipnewline++; next;
    }
    SWITCH: {
    $tmp = qq(<A HREF="#$1">), last SWITCH 
        if ($lprec->{$1} == $filenum - 1);
    $tmp = qq(<A HREF="$firstname.$fileext#$1">), last SWITCH
        if ($lprec->{$1} == 0);
    $tmp = qq(<A HREF="$firstname-$lprec->{$1}.$fileext#$1">),
            last SWITCH;
    }
    if ($skipnewline) {
        $headbuf = "$headbuf$tmp";
    } else {
        $headbuf = $tmp;
    }
    $skipnewline++;
};

$html2html->{rules}->{'^<@@refnam>.*$'} = sub { 
    $headbuf = "$headbuf$refname</A>\n"; 
};

$html2html->{rules}->{'^<@@endref>.*$'} = sub {
    if ($skipnewline == 1) {
        $outfh->print($headbuf); $skipnewline = -1;
    } elsif ($skipnewline == 2) {
        $skipnewline--;
    } else {
        STDERR->print("html2html: Problem with endref\n");
        $skipnewline--;
    }
};

# Default parsing rule
$html2html->{defaultrule} = sub {
    $skipnewline++ if ($skipnewline < 0);
    if ($skipnewline) {
        chop; $headbuf = "$headbuf$_";
    } else {
        $outfh->print($_);
    }
};

# Finalize parsing process
$html2html->{finish} = sub {
    # Finish footers
    if ($outfh->fileno != STDOUT->fileno) {
        &footing($outfh);
        $outfh->close;
    }
    # Finish the TOC; ensure "next" points to the first page.
    &browse_links(STDOUT, 1, 0);
    print STDOUT $partC ;
    print STDOUT "</BODY>\n</HTML>\n";
};


###################################################################
# Secondary Functions
###################################################################

# Print standard links
sub browse_links {
    my ($outfh, $myfilenum, $top) = @_;

    return if ($super_page_mode);

    # NOTE: For pages where a next or prev button isn't appropriate, include
    # the graphic anyway - just don't make it a link. That way, the mouse
    # position of each button is unchanged from page to page.
    # Use the passed myfilenum since filenum may already be incremented

    # Next link (first)
    if ($bartags1 =~ /DOCNAME/) {
	split(/DOCNAME/,$bartags1); 
	$bartags1 = join "", $_[0], $titlename, $_[1] ;
    }
    $outfh->print(qq($bartags1)) ;
    my $next = $use_imgs  
                  ? qq(<IMG SRC="next.gif" ALT="$nextlabel" BORDER="0">)
                  : qq($nextlabel);
    $next = qq(<IMG SRC="blank.gif">) if ( ($myfilenum >= $filecount) && ($use_imgs) );
    $next = qq(<A HREF="$firstname-$myfilenum.$fileext">$next</A>\n)
       if ($myfilenum < $filecount);
    $outfh->print($next);

    # Previous link
    my $prev = $use_imgs  
                  ? qq(<IMG SRC="prev.gif" ALT="$prevlabel" BORDER="0">)
                  : qq($prevlabel);
    $prev = qq(<IMG SRC="blank.gif">) if ( ($myfilenum < 3) && ($use_imgs) );
    $prev = join "", qq(<A HREF="$firstname-), ($myfilenum - 2),
                     qq(.$fileext">$prev</A>\n)
       if ($myfilenum >= 3);
    $outfh->print($prev);

    # Table of contents link
    my $toc = $use_imgs 
                ? qq(<IMG SRC="toc.gif" ALT="$toclabel" BORDER="0" >)
                : qq($toclabel);
    $toc = qq(<IMG SRC="blank.gif">) if ( ($outfh->fileno == STDOUT->fileno) && ($use_imgs) );
    $toc = join "", qq(<A HREF="$firstname.$fileext#toc),
                    &section_num($secnr, 0), qq(">$toc</A>\n)
       if ($outfh->fileno != STDOUT->fileno);
    $outfh->print($toc);

    $outfh->print(qq($bartags2));
}

# Print end-of-file markup
sub footing {
    my $outfh = shift;

    &browse_links($outfh, $filenum, 0);
    $outfh->print("$partC \n");
    $outfh->print("<\/BODY>\n<\/HTML>\n");
}

# Print top-of-file markup
sub heading {
    my $outfh = shift; my $match;

    # Emit 3.2 HTML until somebody comes up with a better idea - CdG
    $outfh->print(
        qq(<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">\n));
    $outfh->print("<HTML>\n<HEAD>\n<TITLE>");
    $match = $titlename;
    $match =~ s/<[^>]*>//g;
    $outfh->print($match);
    if ($secnr > 0) {
        $match = $sectname;
        $match =~ s/<[^>]*>//g;
        $outfh->print(": $match");
    }
    if ($ssecnr > 0) {
        $match = $ssectname;
        $match =~ s/<[^>]*>//g;
        $outfh->print(": $match");
    }
    $outfh->print(qq(</TITLE>\n));
    $outfh->print("$partA");
    $outfh->print(qq(</HEAD>\n));
    $outfh->print("$partB \n");
    &browse_links($outfh, $filenum, 1);
}

# Return the section and subsection as a dotted string
sub section_num {
    my ($sec, $ssec) = @_;
    my $l = "A";

    if ($in_appendix) {
        $sec--;
        while ($sec) { $l++; $sec--; }
        return("$l.$ssec") if ($ssec > 0);
        return("$l");
    } else {
        return("$sec.$ssec") if ($ssec > 0);
        return("$sec");
    }
}

# Create a chapter head; start a new file, etc.
sub start_chapter {
    my $sectname = shift;

    if (!$super_page_mode && $outfh->fileno != STDOUT->fileno) {
        &footing($outfh); $outfh->close;
    }
    $current = "SECTION"; $secnr++; $ssecnr = 0;
    if ($super_page_mode) {
        $headbuf = sprintf(qq(<A NAME="s%s">%s. %s</A>),
                           &section_num($secnr, 0), &section_num($secnr, 0),
                           $sectname);
    } else {
        $outname = "$firstname-$filenum.$fileext"; $filenum++;
        $outfh = new FileHandle ">$outname"
            or die qq(html2html: Fatal: Could not open file "$outname"\n);
        &heading($outfh);
        $headbuf = sprintf(qq(<A NAME="s%s">%s. %s</A>),
                           &section_num($secnr, 0), &section_num($secnr, 0),
                           $sectname);
        STDOUT->printf(
          qq(<P>\n<H2><A NAME="toc%s">%s.</A> <A HREF="%s">%s</A></H2>\n\n),
               &section_num($secnr, 0), &section_num($secnr, 0),
               $outname, $sectname);
        $in_section_list = 0;
    }
}

# Create a section; start a new file, etc.
sub start_section {
    my $ssectname = shift;
    
    $current = "SUBSECT"; $ssecnr++;
    if ($super_page_mode) {
        $headbuf = sprintf("%s %s", &section_num($secnr, $ssecnr),
                           $ssectname);
    } else {
        if (!$in_section_list) {
            STDOUT->print("<UL>\n"); $in_section_list = 1;
        }
        if (!$big_page_mode) {
            if ($outfh->fileno != STDOUT->fileno) {
                &footing($outfh); $outfh->close;
            }
            $outname = "$firstname-$filenum.$fileext"; $filenum++;
            $outfh = new FileHandle ">$outname"
                or die qq(html2html: Fatal: Could not open file "$outname"\n);
            heading($outfh);
    
            # Since only one section is on any page, 
            # don't use # so that when we
            # jump to this page, we see the browse 
            # links at the top of the page. 
            $headbuf = sprintf("%s %s", &section_num($secnr, $ssecnr), 
                               $ssectname);
            STDOUT->printf(qq(<LI><A HREF="%s">%s %s</A>\n), $outname, 
                   &section_num($secnr, $ssecnr), $ssectname);
        } else {
            # Since many sections are on one page, we need to use #
            $headbuf = sprintf(qq(<A NAME="ss%s">%s %s</A>\n),
                               &section_num($secnr, $ssecnr),
                               &section_num($secnr, $ssecnr),
                               $ssectname);
            STDOUT->printf(qq(<LI><A HREF="%s#ss%s">%s %s</A>\n), $outname,
                   &section_num($secnr, $ssecnr), 
                   &section_num($secnr, $ssecnr), 
                   $ssectname);
        }
    }
}

