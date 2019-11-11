// BNV Online
// Erik van Zijst, 27.11.98, icehawk@xs4all.nl
// meeste geript van Anthony

import java.io.*;
import java.sql.*;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.html.*;
import oracle.rdbms.*;
import oracle.owas.nls.*;
import oracle.owas.wrb.services.http.*;


// TU Delft Datawarehouse project - BNV_SessionIdex pagina
// This class displays the main menu directly after the welkom page. menu options are determined by a user's equivalencies
// All pages in the system have a link back to this page. The class is started either automatically or via the link on the
// BNV_SessionWelcome page.


public  class BNV_SessionIndex extends HttpServlet
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
	        // Set up BNV_Constants object to be used in in this class
		BNV_Constants bnvConstantsSI = new BNV_Constants();

	        // Standard External references to standard static HTML document
	       	String tag1 = "dynamicItem1";
        	String tag2 = "dynamicItem2";
	        String tag3 = "dynamicItem3";
	        String tag4 = "dynamicItem4";
	        String tag5 = "dynamicItem5";
        	String tag6 = "dynamicItem6";
	        String tag7 = "dynamicItem7";

	        // Standard HTML page elements / references for BNV_SessionIndex
	        CompoundItem report     = new CompoundItem();
	        CompoundItem errorMsg   = new CompoundItem();
	        Form form               = new Form("GET", bnvConstantsSI.getSessionIndexFormAddress());
		ServletOutputStream out	= res.getOutputStream();
	        HtmlFile template       = new HtmlFile(bnvConstantsSI.getHtmlFile_Name());
		Date date		= new Date();

		String sessionID	= req.getParameter("sessionid");

	        // HTML constants for BNV_SessionIndex
        	String headerText1      = "Hoofdmenu BNV Online";
	        String headerText2      = "Klik op een van de links hieronder om verder te gaan";
        	String headerText3      = "Wilt u deze pagina's veilig verlaten klik dan op de  Logout  knop.";
	        String dbaseError       = "Database probleem: neem contact op met uw database administrator";
	        String error1           = "Neem contact op met uw database administrator";
	        String arrayError       = "Array probleem in het programma: neem contact op met uw database administrator";

	        // Internal references
	        String eqsID  = null;
	        String userID = null;

	        // Standard DataBase variables / references
        	Connection conn;
		Statement stmt;
		ResultSet result;

	        // Validate sessionid parameter ...cancel session if problems
        	if (sessionID == null)
		{
	            new BNV_ErrorPage().cancelSession(req, res);
        	    return;
	        }

	        // Standard routine to validate current session status & page equivalences
	        String sessionStatus = new BNV_SessionStamp().checkSession(sessionID, bnvConstantsSI.getBNV_SessionIndexEqs());
	        if (sessionStatus.equals("alreadyKilled") || sessionStatus.equals("overTimePerPageLimit") || sessionStatus.equals("error") || sessionStatus.equals("error1") || sessionStatus.equals("error2") || sessionStatus.equals("arrayError") || sessionStatus.equals("wrongEquivalences"))
		{
	            new BNV_SessionDead().killSession(sessionID, sessionStatus, req, res);
        	    return;
	        }
	        else

            	//set up form submit fields......
            	form.addItem(new Hidden("sessionid", sessionID));

        	// Retrieve this user's equivalences from bnv_session... this happens only in this class because the display of menu options depends on user's rights...

        	// Standard Oracle database driver registration
        	try
		{	Class.forName(bnvConstantsSI.getDriver_class());
        	}
        	catch (ClassNotFoundException e)
		{	errorMsg.addItem(new SimpleItem("FOUT :" + dbaseError))
                    	        .addItem(new SimpleItem("Could not find the driver: " + bnvConstantsSI.getDriver_class()))
                    	        .addItem(SimpleItem.LineBreak)
                    	        .addItem(new SimpleItem("Message:  " + e.getMessage()));
        	}

	    	// Connect to Database
        	try
		{	conn = DriverManager.getConnection(bnvConstantsSI.getConnect_URL(), bnvConstantsSI.getConnect_user(), bnvConstantsSI.getConnect_password());
            		try
			{	stmt       = conn.createStatement();
                		result = stmt.executeQuery ("SELECT USERID, EQS FROM BNV_SESSION WHERE SESSIONID = '" + sessionID + "'");
                		while (result.next())
				{
                    			userID = result.getString(result.findColumn("USERID"));
                    			eqsID  = result.getString(result.findColumn("EQS"));
                		}
                		stmt.close();
            		} // end try 2
            		catch (SQLException e)
			{
                		errorMsg.addItem(new SimpleItem("FOUT2 :" + dbaseError))
                         	        .addItem(SimpleItem.LineBreak)
                         	        .addItem(new SimpleItem("Message:  " + e.getMessage()));
            		}
            		finally
			{
                		conn.close();
            		}
        	} // end try 1
        	catch (SQLException e)
		{
            		errorMsg.addItem(new SimpleItem("FOUT1 :" + dbaseError))
                     	        .addItem(SimpleItem.LineBreak)
                     	        .addItem(new SimpleItem("Message:  " + e.getMessage()));
        	} // end of database processing


        	// Only process menuitems if no error in retrieval of equivalences string or userid......
        	if (eqsID == null)
            		errorMsg .addItem(new SimpleItem("RECHTEN PROBLEEM :" + "error1").setBold());

        	else if (userID == null)
            		errorMsg .addItem(new SimpleItem("USER PROBLEEM :" + "error1").setBold());

        	else
		{
             		// Set up menu items according to equivalences
             		// Note: the page eqs string is not applicable here here. It is used as a sort of dummy in this class to enforce compatibility
             		// with the sessionCheck() method Simply compare each menuitem with the session eqs string (eqsID) field retrieved from  bnv_session

            		report.addItem(SimpleItem.LineBreak)
                  	      .addItem(new SimpleItem(headerText2))
                  	      .addItem(SimpleItem.LineBreak)
                  	      .addItem(SimpleItem.LineBreak)
                  	      .addItem(SimpleItem.LineBreak);

       			// Show  items for eqs  guest: and admin: (List/Zoek outlets)
       	    		if ( (eqsID.indexOf("guest") != -1) || (eqsID.indexOf("admin") != -1) )
				report.addItem(SimpleItem.Paragraph)
       			      	      .addItem(new SimpleItem(bnvConstantsSI.getListOutletsLink(sessionID)))
				      .addItem(SimpleItem.LineBreak)
				      .addItem(SimpleItem.Paragraph)
       			      	      .addItem(new SimpleItem(bnvConstantsSI.getZoekOutletsLink(sessionID)))
				      .addItem(SimpleItem.LineBreak);


       			// Show  items for eqs  admin: only ()
            		if ((eqsID.indexOf("admin") != -1))
       		    		report.addItem(SimpleItem.Paragraph)
       		          	      .addItem(new SimpleItem(bnvConstantsSI.getAdd10OutletsLink(sessionID)))
                      		      .addItem(SimpleItem.LineBreak)
                      		      .addItem(SimpleItem.Paragraph)
                      		      .addItem(new SimpleItem(bnvConstantsSI.getEditOutletLink(sessionID)))
                      		      .addItem(SimpleItem.LineBreak)
                      		      .addItem(SimpleItem.Paragraph)
                      		      .addItem(new SimpleItem(bnvConstantsSI.getDeleteOutletLink(sessionID)))
                      		      .addItem(SimpleItem.LineBreak);

      		} // end of if(eqsID == null))......


    		//  Print out errors on HTML page .
    		if (errorMsg.size() > 0)
		{
        		report.addItem(SimpleItem.Paragraph)
              		      .addItem(errorMsg)
       		  	      .addItem(SimpleItem.LineBreak);
    		}

    		// add form with logout
    		report.addItem(SimpleItem.LineBreak)
          	      .addItem(SimpleItem.LineBreak)
          	      .addItem(SimpleItem.HorizontalRule)
         	      .addItem(SimpleItem.LineBreak);

    		form.addItem(new SimpleItem(headerText3))
        	    .addItem(SimpleItem.LineBreak)
        	    .addItem(SimpleItem.LineBreak)
        	    .addItem(new Submit("Submit","Logout"))
        	    .addItem(new Hidden("sessionid", sessionID))
        	    .addItem(SimpleItem.LineBreak)
        	    .addItem(SimpleItem.LineBreak);

    		report.addItem(form)
          	      .addItem(SimpleItem.HorizontalRule)
          	      .addItem(SimpleItem.LineBreak);

    		// Load dynamic content into Standaard.html
    		template.setItemAt(tag1, ((new SimpleItem(""))))
            	        .setItemAt(tag2, ((new SimpleItem(headerText1))).setBold())
            	        .setItemAt(tag3, (report))
            	        .setItemAt(tag4, (new SimpleItem(bnvConstantsSI.getCopyRight_Page())))
            	        .setItemAt(tag5, (new SimpleItem(date.toString())))
            	        .setItemAt(tag6, (new SimpleItem(bnvConstantsSI.getPageAdministrator_Name())))
            	        .setItemAt(tag7, (new SimpleItem(bnvConstantsSI.getwebMaster_Name())));

    		template.print(out);
    	}
}
