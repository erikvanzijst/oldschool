// BNV Online
// Datawarehouse project
// Erik van Zijst, 30.11.98, icehawk@xs4all.nl
// Based on Anthony's source

import java.sql.*;
import java.util.Date;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.html.*;
import oracle.rdbms.*;
import oracle.owas.nls.*;
import oracle.owas.wrb.services.http.*;


// TU Delft Datawarehouse project - BNV_SessionDead pagina
// This class generates an HTML page with appropriate descriptive text whenever a timeout or certain other
// error conditions are detected. It is called most often from within session validation routines


public class BNV_SessionDead extends HttpServlet
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

	public void killSession(String inSessionID, String inSessionStatus, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
        	// Set up BNV_Constants object to be used in in this class
        	BNV_Constants bnvConstantsSD = new BNV_Constants();

        	String cleanup_timeout      = bnvConstantsSD.getCleanupTimeoutSQL_String();

        	// Standard External references to standard static HTML document
        	String tag1 = "dynamicItem1";
        	String tag2 = "dynamicItem2";
        	String tag3 = "dynamicItem3";
        	String tag4 = "dynamicItem4";
        	String tag5 = "dynamicItem5";
        	String tag6 = "dynamicItem6";
        	String tag7 = "dynamicItem7";

        	// HTML constants for BNV_SessionDead (this source)
        	String headerText1      = "Civiele techniek informatie - eind van sessie";
        	String dbaseError       = "Database probleem: neem contact op met uw database administrator";
        	String systemText1      = "Systeembericht: Geen sessieID doorgestuurd naar Sessiondead";

        	String notifyText1      = "Sessie over :";
        	String notifyText2      = "Het is dus niet mogelijk om uw sessiegegevens verder te controleren";
        	String notifyText3      = "Er was een database probleem tijdens het controleren van uw sessiegegevens: neem contact op met uw database administrator. "; //error
        	String notifyText4      = "Er was een array probleem tijdens het controleren van uw sessiegegevens: neem contact op met uw systeembeheerder. "; //arrayError
        	String notifyText5      = "Tijdens het controleren van uw sessiegegevens is er geconstateerd dat u geen rechten hebt voor deze pagina"; //wrongEquivalences

        	String notifyText8      = "Wilt u nu stoppen dan is het raadzaam om uw browser te sluiten om de pas ingelezen data te vernietigen";
        	String linkText1        = "Als u toch verder wilt gaan klik dan op   Terug naar loginscherm   en log opnieuw in";
        	String logStatus        = "Abnormaal";
        	String logStatus1       = "Opruim timeout";
        	String logStatus2       = "Pagina timeout";


        	// Standard HTML page elements / references
        	ServletOutputStream out	= res.getOutputStream();
        	CompoundItem report     = new CompoundItem();
        	CompoundItem errorMsg   = new CompoundItem();
        	HtmlFile template       = new HtmlFile(bnvConstantsSD.getHtmlFile_Name());

        	// Standard DataBase variables / references
		Date date = new Date();
        	Connection conn;
		Statement stmt;
		ResultSet result;

        	// Code Body starts here.......

        	String sessionID        = inSessionID;
        	String sessionStatus    = inSessionStatus;

        	if (sessionID == null)
             		errorMsg.addItem(new SimpleItem(systemText1).setBold());

        	// Standard Oracle database driver registration
        	try
		{
            		Class.forName(bnvConstantsSD.getDriver_class());
        	}
        	catch (ClassNotFoundException e)
		{
             		errorMsg.addItem(new SimpleItem(dbaseError))
		                .addItem(new SimpleItem("Could not find the driver: " + bnvConstantsSD.getDriver_class()))
                                .addItem(SimpleItem.LineBreak)
                                .addItem(new SimpleItem("Message:  " + e.getMessage()));
	    	}
        	// Attempt connection to Database
        	try
		{
            		conn = DriverManager.getConnection(bnvConstantsSD.getConnect_URL(), bnvConstantsSD.getConnect_user(), bnvConstantsSD.getConnect_password());
            		// Set up a record in bnv_sessionlog, remove record in bnv_session & remove any other records for this userid

            		try
			{
                		stmt = conn.createStatement();
                		String userID = null;

                		// Routine Cleanup:  write to the log all records which are older than the cleanup timeout & then delete them from bnv_session.......
                		stmt.executeUpdate ("UPDATE BNV_SESSION SET TYPELOGOUT = '" + logStatus1 + "' WHERE ("+ cleanup_timeout +") > STARTDATE");
                		stmt.executeUpdate("COMMIT");

                		stmt.executeUpdate("INSERT INTO BNV_SESSIONLOG SELECT SESSIONID, USERID, REALNAME, STARTDATE, LANGUAGEID, TYPELOGOUT, EQS, STAMPS FROM BNV_SESSION WHERE ("+ cleanup_timeout +") > STARTDATE");
                		stmt.executeUpdate("COMMIT");

                		stmt.executeUpdate ("DELETE FROM BNV_SESSION WHERE (" + cleanup_timeout + ") > STARTDATE");
                		stmt.executeUpdate("COMMIT");

                		// Only need to continue database processing for records with  "overTimePerPageLimit" status
                		if (sessionStatus.equals("overTimePerPageLimit"))
				{

                    			try
					{
                        			// Set logstatus of current session record, write it to bnv_sessionlog, & then delete it from bnv_session
                        			stmt.executeUpdate ("UPDATE BNV_SESSION SET TYPELOGOUT = '" + logStatus2 + "' WHERE SESSIONID = '" + sessionID + "'");
                        			stmt.executeUpdate("COMMIT");

                        			stmt.executeUpdate("INSERT INTO BNV_SESSIONLOG SELECT SESSIONID, USERID, REALNAME, STARTDATE, LANGUAGEID, TYPELOGOUT, EQS, STAMPS FROM BNV_SESSION WHERE SESSIONID = '" + sessionID + "'");
                        			stmt.executeUpdate("COMMIT");

                        			stmt.executeUpdate("DELETE FROM BNV_SESSION WHERE sessionid='" + sessionID +"'");
                        			stmt.executeUpdate("COMMIT");
                    			}
                    			catch (SQLException e)
					{
                        			errorMsg .addItem(new SimpleItem("FOUT3 :" +  dbaseError))
                                 		         .addItem(SimpleItem.LineBreak)
                                 		         .addItem(new SimpleItem("Message:  " + e.getMessage()));
                    			}
                		}
            			stmt.close();
            		}
            		catch (SQLException e)
			{
                		errorMsg.addItem(new SimpleItem("FOUT2 :" +  dbaseError))
                         	        .addItem(SimpleItem.LineBreak)
                         	        .addItem(new SimpleItem("Message:  " + e.getMessage()));
            		}
            		finally
			{
                		conn.close();
            		}
        	}
        	catch (SQLException e)
		{
            		errorMsg.addItem(new SimpleItem("FOUT1 :" +  dbaseError))
                     	        .addItem(SimpleItem.LineBreak)
                     	        .addItem(new SimpleItem("Message:  " + e.getMessage()));
        	} // end of database processing

		report.addItem(SimpleItem.LineBreak)
              	      .addItem(SimpleItem.HorizontalRule)
              	      .addItem(SimpleItem.Paragraph);

        	// pagetimeout.....
        	if(sessionStatus.equals("overTimePerPageLimit"))
			report.addItem(new SimpleItem(notifyText1).setBold().setFontBig())
		      	      .addItem(SimpleItem.LineBreak)
                      	      .addItem(SimpleItem.LineBreak)
                      	      .addItem(new SimpleItem(bnvConstantsSD.getTimePerPageMsg_String()).setBold().setFontBig())
                              .addItem(SimpleItem.LineBreak);

        	// already cleaned up.....
        	else  if(sessionStatus.equals("alreadyKilled"))
        		report.addItem(new SimpleItem(notifyText1).setBold().setFontBig())
                      	      .addItem(SimpleItem.LineBreak)
                              .addItem(SimpleItem.LineBreak)
                      	      .addItem(new SimpleItem(bnvConstantsSD.getCleanupTimeoutMsg_String()).setBold().setFontBig())
                      	      .addItem(SimpleItem.LineBreak)
                              .addItem(new SimpleItem(notifyText2).setBold().setFontBig());

        	// database error.......
        	else  if(sessionStatus.equals("error"))
			report.addItem(new SimpleItem("FOUT : " +  notifyText3).setBold().setFontBig());

        	// database error 1 .......cleanup SQL
        	else  if(sessionStatus.equals("error1"))
			report.addItem(new SimpleItem("FOUT 1 :" + notifyText3).setBold().setFontBig());

        	// database error 2 ....... eqs SQL
        	else  if(sessionStatus.equals("error2"))
			report.addItem(new SimpleItem("FOUT 2 :" + notifyText3).setBold().setFontBig());

        	// sessionstamp array error....
        	else  if(sessionStatus.equals("arrayError"))
			report.addItem(new SimpleItem(notifyText4).setBold().setFontBig());

        	// no rights for this page.......
        	else  if(sessionStatus.equals("wrongEquivalences"))
			report.addItem(new SimpleItem(notifyText5).setBold().setFontBig());

        	report.addItem(SimpleItem.LineBreak)
              	      .addItem(SimpleItem.LineBreak)
               	      .addItem(new SimpleItem(notifyText8).setBold())
              	      .addItem(SimpleItem.LineBreak)
                      .addItem(SimpleItem.LineBreak)
                      .addItem(new SimpleItem(linkText1).setBold())
                      .addItem(SimpleItem.Paragraph)
                      .addItem(SimpleItem.HorizontalRule)
                      .addItem(SimpleItem.LineBreak)
                      .addItem(SimpleItem.LineBreak);

        	// Print out errors on HTML page
        	if (errorMsg.size() > 0)
		{
			report.addItem(SimpleItem.Paragraph)
                      	      .addItem(errorMsg)
                      	      .addItem(SimpleItem.Paragraph)
                      	      .addItem(SimpleItem.HorizontalRule)
                              .addItem(SimpleItem.LineBreak)
                              .addItem(SimpleItem.LineBreak);
       		}

        	report.addItem(SimpleItem.Paragraph)
              	      .addItem(new SimpleItem(bnvConstantsSD.getLoginURL1()));

		// Load dynamic content into standaard.html
        	template.setItemAt(tag1, ((new SimpleItem(""))))
                        .setItemAt(tag2, ((new SimpleItem(headerText1))).setBold())
                	.setItemAt(tag3, (report))
                	.setItemAt(tag4, (new SimpleItem(bnvConstantsSD.getCopyRight_Page())))
                	.setItemAt(tag5, (new SimpleItem( date.toString() )))
                	.setItemAt(tag6, (new SimpleItem(bnvConstantsSD.getPageAdministrator_Name())))
                	.setItemAt(tag7, (new SimpleItem(bnvConstantsSD.getwebMaster_Name())));

       		template.print(out);
	}
// End Of Class
}
