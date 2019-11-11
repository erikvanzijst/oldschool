// BNV Online
// Datawarehouse applicatie
// Erik van Zijst, 30.11.98 icehawk@xs4all.nl
// Gebaseerd op Anthony's sources

import java.io.*;
import java.sql.*;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.html.*;
import oracle.rdbms.*;
import oracle.owas.nls.*;
import oracle.owas.wrb.services.http.*;


// TU Delft Datawarehouse project - BNV_SessionLogout pagina
// This class displays a simple message confirming explicit logout from the system.
// It is started via a "Logout" button on various pages.


public class BNV_SessionLogout extends HttpServlet
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
        	BNV_Constants bnvConstantsSL = new BNV_Constants();

        	String cleanup_timeout      = bnvConstantsSL.getCleanupTimeoutSQL_String();

        	// Standard External references to standard static HTML document
        	String tag1 = "dynamicItem1";
        	String tag2 = "dynamicItem2";
        	String tag3 = "dynamicItem3";
        	String tag4 = "dynamicItem4";
        	String tag5 = "dynamicItem5";
        	String tag6 = "dynamicItem6";
        	String tag7 = "dynamicItem7";

        	// HTML constants for BNV_SessionLogout
        	String headerText1      = "Civiele techniek informatie - eind van sessie";
        	String dbaseError       = "Database probleem: neem contact op met uw database administrator";
        	String notifyText1      = "Sessie over : u heeft gekozen om uw sessie te beëindigen";
        	String notifyText3      = "Als u toch verder wilt gaan klik dan op   Terug naar loginscherm   en log opnieuw in";
        	String notifyText4      = "Wilt u nu stoppen dan is het raadzaam om uw browser te sluiten om de pas ingelezen data te vernietigen";
        	String notifyText5      = "Systeembericht: Geen sessieID doorgestuurd naar Sessiondead";
        	String logStatus        = "Logout";
        	String logStatus1       = "Opruim timeout";

        	// Standard HTML page elements / references
        	CompoundItem report     = new CompoundItem();
        	CompoundItem errorMsg   = new CompoundItem();
		Date date		= new Date();

        	ServletOutputStream out = res.getOutputStream();
        	HtmlFile template       = new HtmlFile(bnvConstantsSL.getHtmlFile_Name());

        	//URL Parameters
        	String sessionID    = req.getParameter("sessionid");

        	// Standard DataBase variables / references
        	Connection conn;
		Statement stmt;
		ResultSet result;


        	// Code Body starts here.......

        	if (sessionID == null)
             		errorMsg.addItem(new SimpleItem(notifyText5).setBold());
        	else
		{
            		// Standard Oracle database driver registration
            		try
			{
                		Class.forName(bnvConstantsSL.getDriver_class());
            		}
            		catch (ClassNotFoundException e)
			{
                 		errorMsg.addItem(new SimpleItem(dbaseError))
                          	        .addItem(new SimpleItem("Could not find the driver: " + bnvConstantsSL.getDriver_class()))
                          	        .addItem(SimpleItem.LineBreak)
                          	        .addItem(new SimpleItem("Message:  " + e.getMessage()));
	        	}

            		// Attempt connection to Database
            		try
			{
                		conn = DriverManager.getConnection(bnvConstantsSL.getConnect_URL(), bnvConstantsSL.getConnect_user(), bnvConstantsSL.getConnect_password());

                		// Cleanup time, write to bnv_sessionlog and remove record in bnv_session
                		try
				{
                    			stmt  = conn.createStatement();

                     			String userID = null;

                    			// Routine Cleanup:  write to the log all records which are older than the cleanup timeout & then delete them from bnv_session.......
                    			stmt.executeUpdate ("UPDATE BNV_SESSION SET TYPELOGOUT = '" + logStatus1 + "' WHERE ("+ cleanup_timeout +") > STARTDATE");
                    			stmt.executeUpdate("COMMIT");

                    			stmt.executeUpdate("INSERT INTO BNV_SESSIONLOG SELECT SESSIONID, USERID, REALNAME, STARTDATE, LANGUAGEID, TYPELOGOUT, EQS, STAMPS FROM BNV_SESSION WHERE ("+ cleanup_timeout +") > STARTDATE");
                    			stmt.executeUpdate("COMMIT");

                    			stmt.executeUpdate ("DELETE FROM BNV_SESSION WHERE (" + cleanup_timeout + ") > STARTDATE");
                    			stmt.executeUpdate("COMMIT");

                    			try
					{
                        			// Set logstatus of current session record, write it to bnv_sessionlog, & then delete it from bnv_session
                        			stmt.executeUpdate ("UPDATE BNV_SESSION SET TYPELOGOUT = '" + logStatus + "' WHERE SESSIONID = '" + sessionID + "'");
                        			stmt.executeUpdate("COMMIT");

                        			stmt.executeUpdate("INSERT INTO BNV_SESSIONLOG SELECT SESSIONID, USERID, REALNAME, STARTDATE, LANGUAGEID, TYPELOGOUT, EQS, STAMPS FROM BNV_SESSION WHERE SESSIONID = '" + sessionID + "'");
                        			stmt.executeUpdate("COMMIT");

                        			stmt.executeUpdate("DELETE FROM BNV_SESSION WHERE SESSIONID='" + sessionID +"'");
                        			stmt.executeUpdate("COMMIT");

                        			stmt.close();

                    			}
                    			catch (SQLException e)
					{
                        			errorMsg .addItem(new SimpleItem("FOUT3 :" +  dbaseError))
                                 		         .addItem(SimpleItem.LineBreak)
                                 		         .addItem(new SimpleItem("Message:  " + e.getMessage()));
                    			}
                		}
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
            		}
            		catch (SQLException e)
			{
                		errorMsg.addItem(new SimpleItem("FOUT1 :" +  dbaseError))
                         	        .addItem(SimpleItem.LineBreak)
                         	        .addItem(new SimpleItem("<BR>" + "Message:  " + e.getMessage()));
            		} // end of database processing

        	} //end if /else (sessionID == null)

        	report.addItem(SimpleItem.LineBreak)
              	      .addItem(SimpleItem.LineBreak)
              	      .addItem(SimpleItem.HorizontalRule)
              	      .addItem(SimpleItem.Paragraph)
              	      .addItem(new SimpleItem(notifyText1).setBold().setFontBig())
              	      .addItem(SimpleItem.LineBreak)
              	      .addItem(SimpleItem.LineBreak)
              	      .addItem(new SimpleItem(notifyText4).setBold())
              	      .addItem(SimpleItem.LineBreak)
              	      .addItem(SimpleItem.LineBreak)
              	      .addItem(new SimpleItem(notifyText3).setBold())
              	      .addItem(SimpleItem.Paragraph)
              	      .addItem(SimpleItem.HorizontalRule)
              	      .addItem(SimpleItem.LineBreak)
              	      .addItem(SimpleItem.LineBreak);

        	// Print out errors on HTML page
        	if (errorMsg.size() > 0)
		{
            		report.addItem(errorMsg)
                  	      .addItem(SimpleItem.LineBreak)
                  	      .addItem(SimpleItem.LineBreak);
       		}
        	report.addItem(SimpleItem.Paragraph)
              	      .addItem(new SimpleItem(bnvConstantsSL.getLoginURL1()));

       		// Load dynamic content into standaard.html
        	template.setItemAt(tag1, ((new SimpleItem(""))))
                	.setItemAt(tag2, ((new SimpleItem(headerText1))).setBold())
                	.setItemAt(tag3, (report))
                	.setItemAt(tag4, (new SimpleItem(bnvConstantsSL.getCopyRight_Page())))
                	.setItemAt(tag5, (new SimpleItem(date.toString())))
                	.setItemAt(tag6, (new SimpleItem(bnvConstantsSL.getPageAdministrator_Name())))
                	.setItemAt(tag7, (new SimpleItem(bnvConstantsSL.getwebMaster_Name())));

       		template.print(out);
       	}
}
