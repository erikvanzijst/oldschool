// BNV Online
// Erik van Zijst, 27.11.98, icehawk@xs4all.nl
// Datawarehouse application
// inspired by Anthony's work

import java.util.Date;
import java.sql.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.rdbms.*;
import oracle.owas.nls.*;
import oracle.owas.wrb.services.http.*;
import oracle.html.*;


// TU Delft Datawarehouse project - BNV_SessionStamp service class
// This class checks the status of a session and is called whenever a new page is called in the system.
// The most common check is for timeouts on a page for its sessionid. Other session statuses whcih are checked
// are listed below. In the case of an invalid session a sessionstatus is returned which causes the session to be terminated

/*  Note: This class returns a sessionstatus string value.
    Valid sessionstatuses are:-
                 "error"
                 "error1"
                 "error2"
                 "arrayError"
                 "overTimePerPageLimit"
                 "alreadyKilled"
                 "restamped"
                 "wrongEquivalences"
*/

public class BNV_SessionStamp extends HttpServlet
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
		// Standard HTML page elements / references
		ServletOutputStream out		= res.getOutputStream();
		HtmlHead page_head = new HtmlHead("TU Delft, Civiele Techniek Datawarehouse - BNV Online");	// declare, not allocate yet!
		HtmlBody page_body = new HtmlBody();	// here goes the output
		HtmlPage whole_page = new HtmlPage(page_head, page_body);

		page_body.addItem(new SimpleItem("<BR><H3>FOUT: Dit servlet kan niet met de hand worden aangeroepen. ZEKER NIET DOOR JOU, STEFAN!!<BR>Wat doe je hier trouwens nog?? Ga's werken man! PRUTSER!.</H3><BR>").setBold() );
		whole_page.print(out);
	}


        public String checkSession(String inSessionID, String inEqs)
	{
            // Set up BNV_Constants object to be used in in this class
            BNV_Constants bnvConstantsSST = new BNV_Constants();

            String page_timeout         = bnvConstantsSST.getTimePerPageSQL_String();
            String cleanup_timeout      = bnvConstantsSST.getCleanupTimeoutSQL_String();

	    String sessionStatus	= "error";

            String logStatus1       = "opruim timeout";
            String sessionID        = null;
            String eqsID            = null;
            String startDate;
            String sessionEqsString = null;
            int sessionStamps       = 0;

            // Check for invalid  incoming parameters
            sessionID     = inSessionID;
            eqsID         = inEqs;

            if (sessionID == null || eqsID == null)
                return sessionStatus;  // shouldn't occur

            // Standard DataBase variables / references
            Connection conn;
	    Statement stmt;
            ResultSet result;

            // Standard Oracle database driver registration
            	try
		{
			Class.forName(bnvConstantsSST.getDriver_class());
             	}
            	catch (ClassNotFoundException e)
		{
                	return sessionStatus;
		}


	        // Connect to Database
		try
		{
                	conn = DriverManager.getConnection(bnvConstantsSST.getConnect_URL(), bnvConstantsSST.getConnect_user(), bnvConstantsSST.getConnect_password());
                	try
			{
                    		stmt  = conn.createStatement();

				// Routine Cleanup:  write to the log all records which are older than the cleanup timeout & then delete them from bnv_session.......
                    		stmt.executeUpdate ("UPDATE BNV_SESSION SET TYPELOGOUT = '" + logStatus1 + "' WHERE ("+ cleanup_timeout +") > STARTDATE");
                    		stmt.executeUpdate("COMMIT");

                    		stmt.executeUpdate("INSERT INTO BNV_SESSIONLOG SELECT SESSIONID, USERID, REALNAME, STARTDATE, LANGUAGEID, TYPELOGOUT, EQS, STAMPS FROM BNV_SESSION WHERE ("+ cleanup_timeout +") > STARTDATE");
                    		stmt.executeUpdate("COMMIT");

                    		stmt.executeUpdate ("DELETE FROM BNV_SESSION WHERE (" + cleanup_timeout + ") > STARTDATE");
                    		stmt.executeUpdate("COMMIT");

                    		// First check all records in bnv_session to see if a record exists for this sessionid.. if not it's already been killed
                    		int count = 0;

                    		try
				{
                        		result    = stmt.executeQuery ("SELECT COUNT(*) FROM BNV_SESSION WHERE sessionid = '" + sessionID + "'");

                        		while (result.next()) count = result.getInt(1);

                        		if (count == 0)
					{
                            			sessionStatus   = "alreadyKilled";
                            			return sessionStatus;
                        		}
                        		else
					{
                            			//Record exists.. first check equivalences. Note: use this query to pick up the Stamps field as well..

                            			result = stmt.executeQuery ("SELECT EQS, STAMPS FROM BNV_SESSION WHERE SESSIONID = '" + sessionID + "'");

                            			while (result.next())
						{
                                			sessionEqsString = result.getString(result.findColumn ("EQS"));
                                			sessionStamps    = result.getInt(result.findColumn ("STAMPS"));
                            			}

                            			if ( (sessionStatus = checkEquivalences(sessionEqsString, eqsID, sessionStatus))  == "wrongEquivalences" )
						{
                                			return sessionStatus;
                            			}

                            			//now check for standard 10 minutes (pagetimeout) allowance.....
                            			count = 0;

                            			result = stmt.executeQuery ("SELECT COUNT(*) FROM BNV_SESSION WHERE sessionid = '" + sessionID + "' AND (" + page_timeout + ") > STARTDATE");

                                		while (result.next())  count = result.getInt(1);

                                		//Time up .... kill session // deletion & logging occurs in sessiondead..
                                		if (count > 0)
						{
                                    			sessionStatus  = "overTimePerPageLimit";
                                    			return sessionStatus;
                                		}

                                		// Still valid session: increment stampcount & re-stamp for another x minutes............
                                		else
						{
                                    			sessionStamps++;

                                    			stmt.executeUpdate("UPDATE BNV_SESSION SET STARTDATE = SYSDATE, STAMPS = " + sessionStamps + " WHERE SESSIONID = '" + sessionID + "'");
                                    			stmt.executeUpdate("COMMIT");

                                    			sessionStatus  = "restamped";
                                		}
                        		}
                    		}
                    		catch (SQLException e)
				{
                        		sessionStatus  = "error2";
                        		return sessionStatus;
                    		}

                	}
                	catch (SQLException e)
			{
                    		sessionStatus  = "error1";
                    		return sessionStatus;
                	}
                	finally
			{
                     		conn.close();
                	}
            	}
            	catch (SQLException e)
		{
                	sessionStatus  = "error";
                	return sessionStatus;
            	} // end database processing

        	return sessionStatus;
    	}   // end of checkSession() Method


    	public  String checkEquivalences(String sessionEqs, String pageEqs, String sessionStatus)
	{

        	//This method  compares the session eqs string (sessionEqsString) to the page eqs string (eqsID) to validate page access

        	if ((sessionEqs.indexOf("guest") != -1) && (pageEqs.indexOf("guest") != -1))
            		return sessionStatus;
        	else if ((sessionEqs.indexOf("admin") != -1) && (pageEqs.indexOf("admin") != -1))
            		return sessionStatus;
        	else
		{
            		sessionStatus = "wrongEquivalences";
            		return sessionStatus;
        	}
    	}
}
