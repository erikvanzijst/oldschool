// BNV Online
// Erik van Zijst, 27.11.98, icehawk@xs4all.nl
// Datawarehouse application

import java.util.Date;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.html.*;
import oracle.owas.nls.*;
import oracle.owas.wrb.services.http.*;


// TU Delft Datawarehouse project - BNV_ErrorPage pagina
// Thsi class shuts down a user's session in the case of unexpected errors & generates an HTML page
// to display the appropriate error message. A link back to the BNV_LoginUser page is provided.
// Coded by Erik van Zijst, 27.11.98


public class BNV_ErrorPage extends HttpServlet
{

	// No global stuff, we're multithreaded...

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
		HtmlHead page_head = new HtmlHead("TU Delft, Civiele Techniek Datawarehouse - BNV Online");	// declare, not allocate yet!
		HtmlBody page_body = new HtmlBody();	// here goes the output
		HtmlPage whole_page = new HtmlPage(page_head, page_body);

		page_body.addItem(new SimpleItem("<BR><H3>FOUT: Dit servlet kan niet met de hand worden aangeroepen.</H3><BR>").setBold() );
		whole_page.print(out);
	}

	public void cancelSession(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{

	        // Set up BNV_Constants object to be used in in this class
        	BNV_Constants bnvConstantsEP = new BNV_Constants();

	        // Standard External references to standard static HTML document
	        String tag1 = "dynamicItem1";
	        String tag2 = "dynamicItem2";
	        String tag3 = "dynamicItem3";
	        String tag4 = "dynamicItem4";
	        String tag5 = "dynamicItem5";
	        String tag6 = "dynamicItem6";
	        String tag7 = "dynamicItem7";

	        // HTML constants for BNV_ErrorPage (this source)
	        String headerText1      = "BNV Online - geannuleerde sessie pagina";
	        String notifyText1      = "Sessie om veiligheidsredenen afgebroken";
        	String notifyText2      = "Er was een onverwachte fout tijdens het doorsturen van uw sessievariabelen";
	        String notifyText3      = "Het is dus niet mogelijk om uw sessiegevens verder te controleren";
	        String notifyText4      = "U kunt op Terug naar loginscherm klikken om opnieuw in te loggen";
	        String notifyText5      = "Neem contact op met uw systeem beheerder als dit probleem vaker voorkomt";
	        String space            = "&nbsp" + ';';

	        // Standard HTML page elements / references
        	CompoundItem report     = new CompoundItem();
	        CompoundItem errorMsg   = new CompoundItem();
		Date date		= new Date();

        	ServletOutputStream out = res.getOutputStream();
	        HtmlFile template       = new HtmlFile(bnvConstantsEP.getHtmlFile_Name());

	        // Main Code Body starts here.......

	        report.addItem(SimpleItem.LineBreak)
	              .addItem(SimpleItem.LineBreak)
	              .addItem(SimpleItem.HorizontalRule)
	              .addItem(SimpleItem.Paragraph)
	              .addItem(new SimpleItem("<BLINK>"))
	              .addItem(new SimpleItem("!").setBold())
	              .addItem(new SimpleItem("</BLINK>"))
	              .addItem(new SimpleItem(space + space))
	              .addItem(new SimpleItem(notifyText1).setBold().setFontBig())
	              .addItem(SimpleItem.LineBreak)
	              .addItem(SimpleItem.LineBreak)
	              .addItem(SimpleItem.LineBreak)
	              .addItem(new SimpleItem(notifyText2).setBold().setFontBig())
	              .addItem(SimpleItem.LineBreak)
	              .addItem(new SimpleItem(notifyText3).setBold().setFontBig())
	              .addItem(SimpleItem.LineBreak)
	              .addItem(SimpleItem.LineBreak)
	              .addItem(SimpleItem.LineBreak)
	              .addItem(new SimpleItem(notifyText4).setBold())
	              .addItem(SimpleItem.Paragraph)
	              .addItem(SimpleItem.HorizontalRule)
	              .addItem(SimpleItem.LineBreak)
	              .addItem(SimpleItem.LineBreak)
	              .addItem(new SimpleItem(bnvConstantsEP.getLoginURL1()))
	              .addItem(SimpleItem.LineBreak)
	              .addItem(SimpleItem.LineBreak)
	              .addItem(SimpleItem.LineBreak)
	              .addItem(new SimpleItem(notifyText5));


	       // Load dynamic content into Standaard.html
        	template.setItemAt(tag1, ((new SimpleItem(""))))
	                .setItemAt(tag2, ((new SimpleItem(headerText1))).setBold())
	                .setItemAt(tag3, (report))
	                .setItemAt(tag4, (new SimpleItem(bnvConstantsEP.getCopyRight_Page())))
	                .setItemAt(tag5, (new SimpleItem( date.toString() )))
	                .setItemAt(tag6, (new SimpleItem(bnvConstantsEP.getPageAdministrator_Name())))
	                .setItemAt(tag7, (new SimpleItem(bnvConstantsEP.getwebMaster_Name())));

	       template.print(out);

       } // end of cancelSession method
}
