// package BNV Online
// meeste geript van Anthony
// Erik van Zijst, 26/11/98, icehawk@xs4all.nl

import java.util.Date;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.html.*;
import oracle.owas.nls.*;
import oracle.owas.wrb.services.http.*;


// TU Delft Datawarehouse project - BNV_SessionWelcome page
// This class displays the welcome page directly after a succesful logon. It utilizes the extra Tag in the
// header to insert a META REFRESH tag which provides automatic passthrough to the BNV_SessionIndex page after
// for x seconds. This timeout can be set in BNV_Constants (welcomeRefreshTag1)
// Note:  Because of the limited duration of display this page is NOT timestamped

public  class BNV_SessionWelcome extends HttpServlet
{
	// No global stuff, this must be thread-safe

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		try
		{
			doPost(req, res);
		}
		catch(ServletException e)
		{	System.out.println("Error in doPost() function: ServletException caught.\n");
		}
		catch(IOException e)
		{	System.out.println("Error in doPost() function: IOException caught.\n");
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		BNV_Constants bnvConstantsLU = new BNV_Constants();

		// Standard HTML page elements / references
		ServletOutputStream out		= res.getOutputStream();
		HtmlHead page_head = new HtmlHead("Servlet accessing Oracle Lite Database");	// declare, not allocate yet!
		HtmlBody page_body = new HtmlBody();	// here goes the output
		HtmlPage whole_page = new HtmlPage(page_head, page_body);

		page_body.addItem(new SimpleItem("<BR><H3>FOUT: Dit servlet kan niet met de hand worden aangeroepen. Log eerst in.</H3><BR>").setBold() );
		whole_page.print(out);
	}

    public void showWelcome(String inRealName, String inSessionID, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {

       // Set up BNV_Constants object to be used in in this class
        BNV_Constants bnvConstantsSW = new BNV_Constants();

        // References / variables for BNV_SessionWelcome
        String realName     = inRealName;
        String sessionID    = inSessionID;

        // Standard External references to standard static HTML document
        String tag1 = "dynamicItem1";
        String tag2 = "dynamicItem2";
        String tag3 = "dynamicItem3";
        String tag4 = "dynamicItem4";
        String tag5 = "dynamicItem5";
        String tag6 = "dynamicItem6";
        String tag7 = "dynamicItem7";

        // HTML constants for BNV_SessionWelcome
        String headerText1      = "";
        String welcomeText1     = "Welkom";
        String welcomeText2     = "Klik op de   Hoofdmenu  link hieronder om meteen naar het hoofdmenu te gaan ";
        String welcomeText3     = "Deze pagina's zijn geoptimaliseerd voor Netscape browsers";
        String space            = "&nbsp" + ';';

        // Standard HTML page elements / references
        CompoundItem report     = new CompoundItem();
        CompoundItem errorMsg   = new CompoundItem();
	Date date = new Date();

	ServletOutputStream out	= res.getOutputStream();
        HtmlFile template       = new HtmlFile(bnvConstantsSW.getHtmlFile_Name());

        // Display welcome page with sessionid parameter and link  to Index(including allocated sessionID)...
        report.addItem(SimpleItem.LineBreak)
              .addItem(new SimpleItem("<H1>" + welcomeText1 + "</H1>"))
              .addItem(SimpleItem.LineBreak)
              .addItem(new SimpleItem("<H2>" +  realName + "</H2>"))
              .addItem(SimpleItem.LineBreak)
              .addItem(new SimpleItem(bnvConstantsSW.getTimePerPageMsg_String1()).setBold())
              .addItem(SimpleItem.LineBreak)
              .addItem(SimpleItem.LineBreak)
              .addItem(SimpleItem.LineBreak)
              .addItem(new SimpleItem(bnvConstantsSW.getMetaRefreshTimeMsg1()).setBold())
              .addItem(SimpleItem.LineBreak)
              .addItem(SimpleItem.LineBreak)
              .addItem(new SimpleItem(space + space + space + space + space + space + space + space + space + space + "of").setBold())
              .addItem(SimpleItem.LineBreak)
              .addItem(SimpleItem.LineBreak)
              .addItem(new SimpleItem(welcomeText2).setBold())
              .addItem(SimpleItem.LineBreak)
              .addItem(SimpleItem.LineBreak)
              .addItem(SimpleItem.LineBreak)
              .addItem(new SimpleItem(bnvConstantsSW.getIndexURL1(sessionID)))
              .addItem(SimpleItem.LineBreak)
              .addItem(SimpleItem.LineBreak)
              .addItem(SimpleItem.LineBreak)
              .addItem(new SimpleItem(welcomeText3));

        //  Print out errors on HTML page ....currently not used
        if (errorMsg.size() > 0) {

                report.addItem(SimpleItem.LineBreak)
                      .addItem(SimpleItem.LineBreak)
                      .addItem(errorMsg)
                      .addItem(SimpleItem.LineBreak);
        }

        // Load dynamic content into Welcome.html
        template.setItemAt(tag1, ((new SimpleItem(bnvConstantsSW.getMetaRefreshTag(sessionID)))))
                .setItemAt(tag2, ((new SimpleItem(headerText1))).setBold())
                .setItemAt(tag3, (report))
                .setItemAt(tag4, (new SimpleItem( bnvConstantsSW.getCopyRight_Page() )))
                .setItemAt(tag5, (new SimpleItem(date.toString())))
                .setItemAt(tag6, (new SimpleItem(bnvConstantsSW.getPageAdministrator_Name())))
	        .setItemAt(tag7, (new SimpleItem(bnvConstantsSW.getwebMaster_Name())));

        template.print(out);
    }
}
