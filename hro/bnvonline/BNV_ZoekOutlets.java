// BNV Online - Datawarehouse applicatie
// Erik van Zijst, 08.01.99 icehawk@bart.nl

import java.io.*;
import java.sql.*;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.html.*;
import oracle.owas.nls.*;
import oracle.owas.wrb.services.http.*;

// TU Delft BNV project - BNV_ZoekOutlets pagina
// At this page you can search for specific outlets and manipulate them.
// This page is called from the main Menu. Accessible to guest level access.

public class BNV_ZoekOutlets extends HttpServlet
{
	int MAXRULES			= 3;	// just some sort of #define ;)
	int MAXPERPAGE			= 10;	// both not to be touched by the program

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


	public boolean validateSession(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		BNV_Constants bnvConstantsZO	= new BNV_Constants();
		String sessionID		= null;
		sessionID			= req.getParameter("sessionid");
		if(sessionID == null)
		{
			new BNV_ErrorPage().cancelSession(req, res);
			return true;
		}

		// Now validate the session and equivalences and stamp it
		String sessionStatus = new BNV_SessionStamp().checkSession(sessionID, bnvConstantsZO.getBNV_ZoekOutletsEqs());
		if( sessionStatus.equals("alreadyKilled") || sessionStatus.equals("overTimePerPageLimit") || sessionStatus.equals("error") || sessionStatus.equals("error1") || sessionStatus.equals("error2") || sessionStatus.equals("arrayError") || sessionStatus.equals("wrongEquivalences") )
		{
			new BNV_SessionDead().killSession(sessionID, sessionStatus, req, res);
			return true;
		}
		else
		{	return false;
		}
	}


	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{

		if(validateSession(req, res)) return;	// XS DENiED
		runServlet(req, res);	// tha real work
	}


	public Form generateQueryForm(String sessionID)
	{
		BNV_Constants bnvConstantsZO	= new BNV_Constants();
		int nX				= 0;
		DynamicTable queryTable		= new DynamicTable(3);
		TableRow row			= new TableRow();
		Submit zoekSubmit		= new Submit("SubmitZoek", "Zoek");
		Form queryForm			= new Form("POST", bnvConstantsZO.getZoekOutletsZoekFormAddress());

		Select itemPD[]			= new Select[MAXRULES];
		Select typePD[]			= new Select[MAXRULES];
		Select plusPD[]			= new Select[MAXRULES-1];
		TextField input[]		= new TextField[MAXRULES];

		queryTable.setBorder(0);

		for(nX=0; nX<MAXRULES; nX++)
		{
			itemPD[nX] = new Select( (String) "itemPD" + ((String) String.valueOf((int) (nX+1))) );
			itemPD[nX].addOption(new Option ("Outlet"))
		                  .addOption(new Option ("Kamer"))
        		          .addOption(new Option ("Telefoon- of IP-nr."))
		                  .addOption(new Option ("Ser nummer"))
		                  .addOption(new Option ("LSA - HWA"))
		                  .addOption(new Option ("VLAN"))
		                  .addOption(new Option ("HUB / Stack"))
		                  .addOption(new Option ("Port nummer"))
		                  .addOption(new Option ("Segment"))
		                  .addOption(new Option ("Segment naam"))
		                  .addOption(new Option ("Persoon"))
		                  .addOption(new Option ("Email adres"))
		                  .addOption(new Option ("Laatste wijziging"))
		                  .addOption(new Option ("Gewijzigd door"))
		                  .addOption(new Option ("Aantekeningen"));

			typePD[nX] = new Select( (String) "typePD" + ((String) String.valueOf((int) (nX+1))) );
			typePD[nX].addOption(new Option ("bevat"))
			          .addOption(new Option ("is gelijk aan"));

			input[nX] = new TextField(  "input"+((String) String.valueOf((int) (nX+1))) , 20,20, "");

			row = new TableRow();
			row.addCell(new TableDataCell(itemPD[nX]))
			   .addCell(new TableDataCell(typePD[nX]))
			   .addCell(new TableDataCell(input[nX]));
			queryTable.addRow(row);

			if(nX < (MAXRULES-1) )
			{
				plusPD[nX] = new Select( (String) "plusPD" + ((String) String.valueOf((int) (nX+1))) );
				plusPD[nX].addOption(new Option ("en"))
				          .addOption(new Option ("of"));

				row = new TableRow();
				row.addCell(new TableDataCell(plusPD[nX]));
				queryTable.addRow(row);
			}
			else
			{
				row = new TableRow();
				row.addCell(new TableDataCell(zoekSubmit));
				queryTable.addRow(row);
			}
		}

		queryForm.addItem(new Hidden("sessionid", sessionID))
		         .addItem(new Hidden("button", "zoek"))
		         .addItem(queryTable);

		return queryForm;
	}


	public int countInput(String inputText[])	// how many texfields were filled?
	{
		int nX, count = 0;

		for(nX=0; nX<MAXRULES; nX++)
		{
			if( inputText[count].length() > 0 ) ++count;
			else nX = MAXRULES;	// make it stop
		}

		return count;
	}


	public int countHits(String CountSQLQuery, Connection conn)
	{

		ResultSet result;
		Statement stmt;
		int count;

		try {
			stmt = conn.createStatement();
		}
		catch(SQLException e)
		{
			return -1;
		}

		try
		{
			result = stmt.executeQuery(CountSQLQuery);
			result.next();

			if( (count = result.getInt(1)) < 1)	// no hits
				return 0;
		}
		catch(SQLException e)
		{
			return -1;
		}

		return count;
	}


	public String generateQuery(String itemPD[], String typePD[], String inputText[], String plusPD[])
	{
		String SQLQuery		= "";
		int nX, count = countInput(inputText);

		for(nX=0; nX<count; nX++)
		{
			if( itemPD[nX].indexOf("Outlet") != -1 ) itemPD[nX] = "UPPER(BNV_OUTLET)";
			if( itemPD[nX].indexOf("Kamer") != -1 ) itemPD[nX] = "UPPER(BNV_ROOM)";
			if( itemPD[nX].indexOf("Telefoon") != -1 ) itemPD[nX] = "UPPER(BNV_TEL_IP)";
			if( itemPD[nX].indexOf("Ser") != -1 ) itemPD[nX] = "UPPER(BNV_SER_ADP)";
			if( itemPD[nX].indexOf("LSA") != -1 ) itemPD[nX] = "UPPER(BNV_LSA_HWA)";
			if( itemPD[nX].indexOf("VLAN") != -1 ) itemPD[nX] = "UPPER(BNV_VLAN)";
			if( itemPD[nX].indexOf("HUB") != -1 ) itemPD[nX] = "UPPER(BNV_HUB)";
			if( itemPD[nX].indexOf("Port") != -1 ) itemPD[nX] = "UPPER(BNV_PORT)";
			if( itemPD[nX].indexOf("Segment") != -1 ) itemPD[nX] = "UPPER(BNV_SEGMENT)";
			if( itemPD[nX].indexOf("Segment naam") != -1 ) itemPD[nX] = "UPPER(BNV_SEG_NAME)";
			if( itemPD[nX].indexOf("Persoon") != -1 ) itemPD[nX] = "UPPER(BNV_PERSON)";
			if( itemPD[nX].indexOf("Email") != -1 ) itemPD[nX] = "UPPER(BNV_EMAIL)";
			if( itemPD[nX].indexOf("Laatste") != -1 ) itemPD[nX] = "UPPER(BNV_LASTEDIT)";
			if( itemPD[nX].indexOf("Gewijzigd") != -1 ) itemPD[nX] = "UPPER(BNV_EDITBY)";
			if( itemPD[nX].indexOf("Aantekeningen") != -1 ) itemPD[nX] = "UPPER(BNV_COMMENTS)";

			SQLQuery = SQLQuery + " " + itemPD[nX];

			if( typePD[nX].indexOf("bevat") != -1)
			{
				typePD[nX] = "LIKE";
				SQLQuery = SQLQuery + " " + typePD[nX] + " " + "'%" + inputText[nX].toUpperCase() + "%'";
			}
			else if( typePD[nX].indexOf("gelijk") != -1)
			{
				typePD[nX] = "=";
				SQLQuery = SQLQuery + " " + typePD[nX] + " '" + inputText[nX].toUpperCase() + "'";
			}

			if( nX < (count-1) )	// add the "AND" statement
			{
				if( plusPD[nX].indexOf("en") != -1 ) plusPD[nX] = "AND";
				else if( plusPD[nX].indexOf("of") != -1 ) plusPD[nX] = "OR";

				SQLQuery = SQLQuery + " " + plusPD[nX];
			}
		}

		return SQLQuery;
	}


	public Form generateHoofdmenuForm(String sessionID)
	{
		BNV_Constants bnvConstantsZO	= new BNV_Constants();
		DynamicTable hoofdmenuTable	= new DynamicTable(2);
		TableRow row			= new TableRow();
		Submit hoofdmenuSubmit		= new Submit("SubmitHoofdmenu", "Hoofdmenu");
		Form hoofdmenuForm		= new Form("POST", bnvConstantsZO.getZoekOutletsHoofdmenuFormAddress());
		String textLine1		= "Keer terug naar het hoofdmenu.";

		hoofdmenuTable.setBorder(0);

		row.addCell(new TableDataCell(hoofdmenuSubmit))
		   .addCell(new TableDataCell(new SimpleItem(textLine1)));
		hoofdmenuTable.addRow(row);

		hoofdmenuForm.addItem(hoofdmenuTable)
		             .addItem(new Hidden("sessionid", sessionID));

		return hoofdmenuForm;
	}


	public void runServlet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		// Gimme default constants-object
		ServletOutputStream out		= res.getOutputStream();
		BNV_Constants bnvConstantsZO	= new BNV_Constants();
		Date date			= new Date();
		int nX				= 0;
		String sessionID		= null;
		String submitType		= null;
		String outlet			= null;
		// input search parameters
		String itemPD[]			= new String[MAXRULES];
		String typePD[]			= new String[MAXRULES];
		String inputText[]		= new String[MAXRULES];
		String plusPD[]			= new String[MAXRULES-1];

		// Standard HTML components / references
		HtmlFile template 		= new HtmlFile(bnvConstantsZO.getHtmlFile_Name());
		CompoundItem report		= new CompoundItem();
		CompoundItem errorMsg		= new CompoundItem();

		// HTML constants for BNV_DeleteOutlets
       		String tag1			= "dynamicItem1";
	       	String tag2			= "dynamicItem2";
	        String tag3			= "dynamicItem3";
	        String tag4			= "dynamicItem4";
        	String tag5			= "dynamicItem5";
	        String tag6			= "dynamicItem6";
        	String tag7			= "dynamicItem7";
		String headerText1		= "BNV Online  -  Zoek outlets";
		String textLine1		= "Genereer een zoekopdracht om bepaalde outlets te zoeken.<BR><BR>Bijvoorbeeld \"Outlet bevat ZZ en Kamer bevat 1.\" om alle outlets op de 1e verdieping te zoeken die beginnen met ZZ.<BR>Of \"Gewijzigd door is gelijk aan prutser\" om al het geknoei van prutser te zien.";
	        String dbaseError		= "Database probleem: neem contact op met uw database beheerder";

		// Database objects
		Connection conn;
		Statement stmt;
		ResultSet result;
		String SQLQuery			= null;
		String SearchSQLQuery		= null;
		String CountSQLQuery		= null;
		int count			= 0;

		// get the URL parameters
		sessionID			= req.getParameter("sessionid");
		submitType			= req.getParameter("button");	// which button was pressed?

		report.addItem(SimpleItem.LineBreak)
		      .addItem(SimpleItem.LineBreak);


		if(submitType == null)	// first time here, coming from the main menu
		{
			report.addItem(new SimpleItem(textLine1))
			      .addItem(SimpleItem.LineBreak)
			      .addItem((Form) generateQueryForm(sessionID))
			      .addItem(SimpleItem.HorizontalRule)
			      .addItem((Form) generateHoofdmenuForm(sessionID));
		}
		else if( submitType.equals("zoek") )
		{
			// first retrieve the input textfields
			for(nX=0; nX<MAXRULES; nX++)
			{
				itemPD[nX] = "";
				itemPD[nX] = req.getParameter( "itemPD" + ((String) String.valueOf((int) (nX+1))) );

				typePD[nX] = "";
				typePD[nX] = req.getParameter( "typePD" + ((String) String.valueOf((int) (nX+1))) );

				inputText[nX] = "";
				inputText[nX] = req.getParameter( "input" + ((String) String.valueOf((int) (nX+1))) );

				if( nX < (MAXRULES-1) )
				{
					plusPD[nX] = "";
					plusPD[nX] = req.getParameter( (String) "plusPD" + ((String) String.valueOf((int) (nX+1))) );
				}
			}

			// now construct the appropriate SQL statement
			SQLQuery = generateQuery(itemPD, typePD, inputText, plusPD);
			CountSQLQuery  = "SELECT COUNT(*) FROM BNV_OUTLET WHERE" + SQLQuery;
			SearchSQLQuery = "SELECT * FROM BNV_OUTLET WHERE" + SQLQuery;

			// open the database if we have at least 1 inputfield
			if( countInput(inputText) > 0 )
			{
				try
				{
					Class.forName((String) bnvConstantsZO.getDriver_class());
				}
				catch(ClassNotFoundException e)
				{
					errorMsg.addItem(new SimpleItem(dbaseError))
					        .addItem(new SimpleItem("<BR>Could not find the JDBC driver: "+ bnvConstantsZO.getDriver_class()))
					        .addItem(SimpleItem.LineBreak)
					        .addItem(new SimpleItem("Message: "+ e.getMessage()))
					        .addItem(SimpleItem.LineBreak);
				}
				try
				{
					conn = DriverManager.getConnection(bnvConstantsZO.getConnect_URL(), bnvConstantsZO.getConnect_user(), bnvConstantsZO.getConnect_password() );
					try
					{
						stmt = conn.createStatement();

						if( (count = countHits(CountSQLQuery, conn)) < 1 )	// no hits or error
							errorMsg.addItem(new SimpleItem("Niets gevonden.").setBold());
						else
						{
							result = stmt.executeQuery(SearchSQLQuery);
							if( result.next() )
							{

							}
							else
								errorMsg.addItem(new SimpleItem("Er is een onbekende fout opgetreden.").setBold());
						}
					}
					catch(SQLException e)
					{
						errorMsg.addItem(new SimpleItem(dbaseError))
						        .addItem(SimpleItem.LineBreak)
						        .addItem(new SimpleItem("Message: "+ e.getMessage()) );
					}
					finally
					{	conn.close();
					}

				}
				catch(SQLException e)
				{
					errorMsg.addItem(new SimpleItem("FOUT: "+ dbaseError))
					        .addItem(SimpleItem.LineBreak)
					        .addItem(new SimpleItem("Message: "+ e.getMessage()) );
				}
			}


			// and make the report (incl. errors)
			if( errorMsg.size() > 0 )	// Oh ooh.. error
			{
				report.addItem( "<B>" + errorMsg + "</B><BR>" )
				      .addItem(new SimpleItem("De search query was: \""+SearchSQLQuery+"\".").setBold() )
				      .addItem(SimpleItem.HorizontalRule)
				      .addItem((Form) generateQueryForm(sessionID))
				      .addItem(SimpleItem.HorizontalRule)
				      .addItem((Form) generateHoofdmenuForm(sessionID));
			}
			else
			{
				report.addItem(SearchSQLQuery)
				      .addItem(SimpleItem.HorizontalRule)
				      .addItem((Form) generateHoofdmenuForm(sessionID));
			}
		}


    		// Load dynamic content into Standaard.html
		template.setItemAt(tag1, ((new SimpleItem(""))))
       	    	        .setItemAt(tag2, ((new SimpleItem(headerText1))).setBold())
      		        .setItemAt(tag3, (report))
       		        .setItemAt(tag4, (new SimpleItem(bnvConstantsZO.getCopyRight_Page())))
      		        .setItemAt(tag5, (new SimpleItem(date.toString())))
      		        .setItemAt(tag6, (new SimpleItem(bnvConstantsZO.getPageAdministrator_Name())))
      		        .setItemAt(tag7, (new SimpleItem(bnvConstantsZO.getwebMaster_Name())));

    		template.print(out);
	}

} // end of class
