#
#  fmt_html.pl
#
#  $Id: fmt_html.pl,v 1.2 1998/01/11 02:12:01 panayoti Exp $
#
#  HTML-specific driver stuff
#
#  © Copyright 1996, Cees de Groot
#
# Hacked by Panayotis Vryonis <vrypan@hol.gr>, 26.11.97
#	to use kdesgml package.
#
package SGMLTools::fmt_html;
use strict;

use SGMLTools::CharEnts;
use SGMLTools::Vars;

use SGMLTools::FixRef;
my $fixref = $SGMLTools::FixRef::fixref;

use SGMLTools::Html2Html;
my $html2html = $SGMLTools::Html2Html::html2html;

use SGMLTools::niceHtml2Html;
my $nicehtml2html =  $SGMLTools::niceHtml2Html::html2html;

my $html = {};
$html->{NAME} = "html";
$html->{HELP} = "";
$html->{OPTIONS} = [
   { option => "split", type => "l", 
     'values' => [ "0", "1", "2" ], short => "s" },
   { option => "dosnames", type => "f", short => "D" },
   { option => "imagebuttons", type => "f", short => "I"},
   { option => "nice", type => "f", short => "N"}
];
$html->{'split'}  = 1;
$html->{dosnames}  = 0;
$html->{imagebuttons}  = 0;
$html->{preNSGMLS} = sub {
  $global->{NsgmlsOpts} .= " -ifmthtml ";
};

$Formats{$html->{NAME}} = $html;

# HTML escape sub.  this is called-back by `parse_data' below in
# `html_preASP' to properly escape `<' and `&' characters coming from
# the SGML source.
my %html_escapes;
$html_escapes{'&'} = '&amp;';
$html_escapes{'<'} = '&lt;';

my $html_escape = sub {
    my ($data) = @_;

    # replace the char with it's HTML equivalent
    $data =~ s|([&<])|$html_escapes{$1}|ge;

    return ($data);
};

#
#  Translate character entities and escape HTML special chars.
#
$html->{preASP} = sub
{
  my ($infile, $outfile) = @_;
  # note the conversion of `sdata_dirs' list to an anonymous array to
  # make a single argument
  my $char_maps = load_char_maps ('.2html', [ Text::EntityMap::sdata_dirs() ]);

  while (<$infile>)
    {
      if (/^-/)
        {
	    my ($str) = $';
	    chop ($str);
	    print $outfile "-" . parse_data ($str, $char_maps, $html_escape) . "\n";
        }
      elsif (/^A/)
        {
	  /^A(\S+) (IMPLIED|CDATA|NOTATION|ENTITY|TOKEN)( (.*))?$/
	      || die "bad attribute data: $_\n";
	  my ($name,$type,$value) = ($1,$2,$4);
	  if ($type eq "CDATA")
	    {
	      # CDATA attributes get translated also
	      $value = parse_data ($value, $char_maps, $html_escape);
	    }
	  print $outfile "A$name $type $value\n";
        }
      else
        {
	  print $outfile $_;
        }
    }
  return 0;
};

#
#  Take the sgmlsasp output, and make something
#  useful from it.
#
$html->{postASP} = sub
{
  my $infile = shift;
  my $filename = $global->{filename};

  #
  #  Set various stuff as a result of option processing.
  #
  my $ext   = "html";
  $ext   = "htm"  if $html->{dosnames};
  my $img   = 0;
  $img   = 1 if $html->{imagebuttons};

  #
  # Bring in file
  #
  my @file = <$infile>;

  #
  #  Find references
  #
  &{$fixref->{init}}($html->{'split'});
  LINE: foreach (@file) {
      foreach my $pat (keys %{$fixref->{rules}}) {
          if (/$pat/) {
              # Call rule function then skip to next line
              &{$fixref->{rules}->{$pat}}; next LINE;
          }
      }
      &{$fixref->{defaultrule}};
  }
  &{$fixref->{finish}};

  #  
  #  Run through html2html, preserving stdout
  #  Also, handle prehtml.sed's tasks
  #
  open SAVEOUT, ">&STDOUT";
  open STDOUT, ">$filename.$ext" or die qq(Cannot open "$filename.$ext");

  if ($html->{nice}) 
  {
  &{$nicehtml2html->{init}}($html->{'split'}, $ext, $img, $filename,
                        $fixref->{filenum}, $fixref->{lrec});
  LINE: foreach (@file) {
      s,<P></P>,,g; 			# remove empty <P></P> containers
      foreach my $pat (keys %{$nicehtml2html->{rules}}) {
          if (/$pat/) {
              # Call rule function then skip to next line
              &{$nicehtml2html->{rules}->{$pat}}; next LINE;
          }
      }
      &{$nicehtml2html->{defaultrule}};
  }
  &{$nicehtml2html->{finish}};
  } else {
  &{$html2html->{init}}($html->{'split'}, $ext, $img, $filename,
                        $fixref->{filenum}, $fixref->{lrec});
  LINE: foreach (@file) {
      s,<P></P>,,g; 			# remove empty <P></P> containers
      foreach my $pat (keys %{$html2html->{rules}}) {
          if (/$pat/) {
              # Call rule function then skip to next line
              &{$html2html->{rules}->{$pat}}; next LINE;
          }
      }
      &{$html2html->{defaultrule}};
  }
  &{$html2html->{finish}};
  }
  close STDOUT;
  open STDOUT, ">&SAVEOUT";

  return 0;
};

1;

