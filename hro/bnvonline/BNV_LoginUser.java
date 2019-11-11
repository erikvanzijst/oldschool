// package bnv_online;
// TODO: omzetten naar methodes; huidige struktuur is niet overzichtelijk!!!
// Geript van Anthony, werkt nu als login pagina voor BNV-online.

import java.io.*;
import java.sql.*;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.html.*;
//import oracle.pol.jac.*;
//import oracle.rdbms.*;
import oracle.owas.nls.*;
import oracle.owas.wrb.services.http.*;


// TU Delft BNV project - BNV_LoginUser pagina
// This class generates the login page which is the entry point to the system. If entries are
// made in the loginnaam and wachtwoord fields the data is validated & for a valid login a unique
// sessionid is generated before control is passed to the BNV_sessionWelcome page. This needs to be
// started from a link/button outside the system.


public class BNV_LoginUser extends HttpServlet
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
		Date date = new Date();

	        // Standard External references to standard static HTML document
       		String tag1 = "dynamicItem1";
	       	String tag2 = "dynamicItem2";
	        String tag3 = "dynamicItem3";
	        String tag4 = "dynamicItem4";
        	String tag5 = "dynamicItem5";
	        String tag6 = "dynamicItem6";
        	String tag7 = "dynamicItem7";

	        // HTML constants for BNV_LoginUser
        	String headerText1      = "BNV Online -  login scherm";
	        String headerText2      = "Sessie registratie probleem";
	        String loginText1       = "Vul uw loginnaam en wachtwoord in voor admin toegang en klik op de Login knop.<BR>Of druk direct op login om in te loggen als guest.";
	        String loginText2       = "Het login-proces kan even duren, heb daarom even geduld alstublieft";
        	String badLogin         = "FOUT:  Uw login mislukte. Probeer opnieuw alstublieft";
	        String oneFieldBlank    = "FOUT:  U moet beide velden invullen om te kunnen inloggen, of beide leeg laten voor guest access.";
        	String noNewSession     = "Uw login was goed maar er is een probleem met het registreren van uw sessie. Neem contact op met uw database beheerder";
	        String dbaseError       = "Database probleem: neem contact op met uw database beheerder";

		// Standard HTML page elements / references
		ServletOutputStream out		= res.getOutputStream();
		HtmlFile template 		= new HtmlFile(bnvConstantsLU.getHtmlFile_Name());
		CompoundItem report 		= new CompoundItem();
		CompoundItem errorMsg 		= new CompoundItem();
		DynamicTable tab;
		TableRow row;

		String useridString		= req.getParameter("useridInput");
		String passwordString		= req.getParameter("passwordInput");
		String sessionID		= null;
		Submit submit			= new Submit("Submit", "Login");

		// Variables used internally
		String password			= null;
		String eqs			= null;
		String userid			= null;
		String realname			= null;

		// Standard DataBase variables / references
		Connection conn;
		Statement stmt;
		ResultSet result;
		int recordCount = 0;
		String recordString = "";

		// Let's Rock!

		Form form			= new Form("POST", bnvConstantsLU.getLoginUserFormAddress());
		tab				= new DynamicTable(3);
		tab.setBorder(0);

		// If no parameters were given, the user first entered..
		if(useridString == null && passwordString == null)
		{
			report.addItem(SimpleItem.LineBreak)
			      .addItem(SimpleItem.LineBreak);

			form.addItem(new SimpleItem(loginText1))
			    .addItem(SimpleItem.LineBreak)
			    .addItem(SimpleItem.LineBreak);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Loginnaam:").setBold()))
			   .addCell(new TableDataCell(new TextField("useridInput",20,20,"guest")));
			tab.addRow(row);

			row = new TableRow();
			tab.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Wachtwoord:").setBold()))
			   .addCell(new TableDataCell(new PasswordField("passwordInput",20,20,"guest")))
			   .addCell(new TableDataCell(submit).setWidth(110).setHAlign(2));
			tab.addRow(row);

			form.addItem(tab);
			report.addItem(form)
			      .addItem(SimpleItem.LineBreak)
			      .addItem(new SimpleItem(loginText2));
		}
		else
		{
			// User tries to log in.. let's process his information
			// Start to interact with the database

			try
			{
				Class.forName((String) bnvConstantsLU.getDriver_class());
				//Class.forName("oracle.jdbc.driver.OracleDriver");
			}	
			catch(ClassNotFoundException e)
			{
				errorMsg.addItem(new SimpleItem(dbaseError))
				        .addItem(new SimpleItem("<BR>Could not find the driver: "+ bnvConstantsLU.getDriver_class()))
				        .addItem(SimpleItem.LineBreak)
				        .addItem(new SimpleItem("Message: "+ e.getMessage()))
				        .addItem(SimpleItem.LineBreak);
			}


			// Now try to link with the database (Oracle Lite on NT Server in this case)

			try
			{
				conn = DriverManager.getConnection(bnvConstantsLU.getConnect_URL(), bnvConstantsLU.getConnect_user(), bnvConstantsLU.getConnect_password() );
				try
				{
					stmt = conn.createStatement();
					useridString = useridString.toUpperCase();
					result = stmt.executeQuery("SELECT * FROM BNVUSER WHERE UPPER(USERID) = '"+useridString+"'");
					result.next();
					try
					{	useridString = result.getString(1);
					}
					catch(SQLException e)
					{
						errorMsg.addItem(new SimpleItem(badLogin).setBold())
						        .addItem(new SimpleItem("<BR>Gebruiker "+useridString+" niet gevonden.").setBold())
						        .addItem(SimpleItem.LineBreak);
					}
					if(errorMsg.size() == 0)	// if all went right..
					{
						// check the password
						useridString = useridString.toUpperCase();
						result = stmt.executeQuery("SELECT * FROM BNVUSER WHERE UPPER(USERID) = '"+useridString+"'");
						result.next();

						try {
							password	= result.getString(2);	// fetch the password from the database
							userid		= result.getString(1);
							realname	= result.getString(3);
							eqs		= result.getString(4);
						}
						catch(SQLException e)
						{
							errorMsg.addItem(new SimpleItem(badLogin).setBold())
							        .addItem(new SimpleItem("<BR>Wachtwoord voor gebruiker "+useridString+" niet gevonden!<BR>"))
							        .addItem(SimpleItem.LineBreak);
						}

						stmt.close();
						if( (  (password.trim()).compareTo((passwordString.trim()) )) != 0)	// password incorrect!
						{
							errorMsg.addItem(new SimpleItem(badLogin).setBold())
							        .addItem(new SimpleItem("<BR>Wachtwoord onjuist!").setBold() )
							        .addItem(SimpleItem.LineBreak);
							stmt.close();
						}
					}
	
				}
				catch(SQLException e)
				{
					errorMsg.addItem(new SimpleItem("FOUT2: "+ dbaseError))
					        .addItem(SimpleItem.LineBreak)
					        .addItem(new SimpleItem("Message: "+ e.getMessage()) );
				}
				finally
				{
					// All went right
					conn.close();
				}
			}
			catch(SQLException e)
			{
				errorMsg.addItem(new SimpleItem("FOUT1: "+ dbaseError))
				        .addItem(SimpleItem.LineBreak)
				        .addItem(new SimpleItem("Message: "+ e.getMessage()) );
			}


			// OK, all set. Now generate some HTML..

			if(errorMsg.size() > 0)	// Oopz...
			{
				form.addItem(SimpleItem.LineBreak)
				    .addItem(new SimpleItem(loginText1))
				    .addItem(SimpleItem.LineBreak)
				    .addItem(SimpleItem.LineBreak);

				row = new TableRow();
				row.addCell(new TableDataCell(new SimpleItem("Loginnaam:").setBold()))
				   .addCell(new TableDataCell(new TextField("useridInput",20,20, (useridString.toLowerCase()).trim() )));
				tab.addRow(row);

				row = new TableRow();
				tab.addRow(row);

				row = new TableRow();
				row.addCell(new TableDataCell(new SimpleItem("Wachtwoord:").setBold()))
				   .addCell(new TableDataCell(new PasswordField("passwordInput",20,20, (passwordString.toLowerCase()).trim() )))
				   .addCell(new TableDataCell(submit).setWidth(110).setHAlign(2));
				tab.addRow(row);

				form.addItem(tab);
				report.addItem(SimpleItem.LineBreak)
				      .addItem(form)
				      .addItem(SimpleItem.LineBreak)
				      .addItem(errorMsg);	// harras the user with extensive data on the error
			}
			else
			{	// OK.. logged in.
				sessionID = new BNV_SessionStart().createSession(userid, realname, eqs);
				if(sessionID.startsWith("error"))
				{
					report.addItem(new SimpleItem(headerText2).setBold() )
					      .addItem(SimpleItem.LineBreak)
					      .addItem(SimpleItem.LineBreak)
					      .addItem(new SimpleItem(noNewSession).setBold() )
					      .addItem(new SimpleItem("<BR>Message: "+sessionID).setBold() )
					      .addItem(errorMsg);
				}
				else
				{ // OK, user was logged in, session was created, show welcome page...
					new BNV_SessionWelcome().showWelcome(realname, sessionID, req, res);
					return;	// done with this thread.
				}
			}



		}

		// Load dynamic content into standaard.html

		template.setItemAt(tag1, (new SimpleItem("")))
		        .setItemAt(tag2, (new SimpleItem(headerText1)).setBold())
		        .setItemAt(tag3, (report))
		        .setItemAt(tag4, (new SimpleItem(bnvConstantsLU.getCopyRight_Page())))
		        .setItemAt(tag5, (new SimpleItem( date.toString() )))
		        .setItemAt(tag6, (new SimpleItem(bnvConstantsLU.getPageAdministrator_Name())))
		        .setItemAt(tag7, (new SimpleItem(bnvConstantsLU.getwebMaster_Name())));

		// We're all set, now pass the dynamic HTML page to the client

		template.print(out);
	}
}
