// BNV Online - Datawarehouse applicatie
// Erik van Zijst, 08.12.98 icehawk@xs4all.nl

import java.io.*;
import java.sql.*;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.html.*;
import oracle.pol.jac.*;
import oracle.rdbms.*;
import oracle.owas.nls.*;
import oracle.owas.wrb.services.http.*;


// TU Delft BNV project - BNV_ListOutlets pagina
// This page lists all outlets on the specified floor.
// This page is called from the main Menu. Accessible to everyone

public class BNV_ListOutlets extends HttpServlet
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


	public boolean validateSession(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		BNV_Constants bnvConstantsLO	= new BNV_Constants();
		String sessionID		= null;
		sessionID			= req.getParameter("sessionid");
		if(sessionID == null)
		{
			new BNV_ErrorPage().cancelSession(req, res);
			return true;
		}

		// Now validate the session and equivalences and stamp it
		String sessionStatus	= new BNV_SessionStamp().checkSession(sessionID, bnvConstantsLO.getBNV_ListOutletsEqs());
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


	public String addQuery(String Col, boolean check, boolean first)
	{
		if(check == false) return null;

		String returnString = "";
		if(first == false) returnString = ", ";
		returnString = returnString + Col;
		return returnString;
	}


	public void runServlet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		// Gimme default constants-object
		ServletOutputStream out		= res.getOutputStream();
		BNV_Constants bnvConstantsLO	= new BNV_Constants();
		Date date			= new Date();
		String sessionID		= null;
		String maxHits			= "20";
		String submitType		= null;
		String floorString		= null;
		String orderByString		= null;
		String CBOutlet			= null;
		String CBRoom			= null;
		String CBTelIP			= null;
		String CBSERADP			= null;
		String CBLSAHWA			= null;
		String CBVLAN			= null;
		String CBHUB			= null;
		String CBPORT			= null;
		String CBSegment		= null;
		String CBSegName		= null;
		String CBPerson			= null;
		String CBEmail			= null;
		String CBLastEdit		= null;
		String CBEditBy			= null;
		String CBComments		= null;
		// Default values of the CheckBoxes
		boolean outletC		= true;
		boolean roomC		= true;
		boolean TelIPC		= true;
		boolean SERADPC		= true;
		boolean LSAHWAC		= false;
		boolean VLANC		= false;
		boolean HUBC		= false;
		boolean PORTC		= false;
		boolean SegmentC	= true;
		boolean SegNameC	= false;
		boolean PersonC		= true;
		boolean EmailC		= true;
		boolean LastEditC	= false;
		boolean EditByC		= false;
		boolean CommentsC	= true;

		sessionID			= req.getParameter("sessionid");
		submitType			= req.getParameter("button");
		maxHits				= req.getParameter("maxHits");
		floorString			= req.getParameter("floorPD");
		orderByString			= req.getParameter("orderBy");
		CBOutlet			= req.getParameter("outletCB");
		CBRoom				= req.getParameter("roomCB");
		CBTelIP				= req.getParameter("TelIPCB");
		CBSERADP			= req.getParameter("SERADPCB");
		CBLSAHWA			= req.getParameter("LSAHWACB");
		CBVLAN				= req.getParameter("VLANCB");
		CBHUB				= req.getParameter("HUBCB");
		CBPORT				= req.getParameter("PORTCB");
		CBSegment			= req.getParameter("SegmentCB");
		CBSegName			= req.getParameter("SegNameCB");
		CBPerson			= req.getParameter("PersonCB");
		CBEmail				= req.getParameter("EmailCB");
		CBLastEdit			= req.getParameter("LastEditCB");
		CBEditBy			= req.getParameter("EditByCB");
		CBComments			= req.getParameter("CommentsCB");

		// Standard HTML components / references
		HtmlFile template 		= new HtmlFile(bnvConstantsLO.getHtmlFile_Name());
		CompoundItem report		= new CompoundItem();
		CompoundItem errorMsg		= new CompoundItem();
		Form menuForm			= new Form("POST", bnvConstantsLO.getListOutletsMenuFormAddress());	// Go button in menufield
		Form logoutForm			= new Form("POST", bnvConstantsLO.getListOutletsLogoutFormAddress());	// Logout button
		Form nextForm			= new Form("POST", bnvConstantsLO.getListOutletsNextFormAddress());	// Next 20 hits -form
		DynamicTable resultTable	= new DynamicTable(16);
		DynamicTable menuTable		= new DynamicTable(1);
		DynamicTable logoutTable	= new DynamicTable(2);
		DynamicTable nextTable		= new DynamicTable(3);
		TableRow row;
		Submit nextHits			= new Submit("SubmitNextHits", "Volgende");
		Submit go			= new Submit("SubmitGo", "Zoek");
		Submit logout			= new Submit("SubmitHoofdmenu", "Hoofdmenu");
		Select floorPD			= new Select("floorPD");
		Select orderByPD		= new Select("orderBy");

		// HTML constants for BNV_ListOutlets
       		String tag1			= "dynamicItem1";
	       	String tag2			= "dynamicItem2";
	        String tag3			= "dynamicItem3";
	        String tag4			= "dynamicItem4";
        	String tag5			= "dynamicItem5";
	        String tag6			= "dynamicItem6";
        	String tag7			= "dynamicItem7";
		String headerText1		= "BNV Online  -  List outlets per verdieping";
	        String dbaseError		= "Database probleem: neem contact op met uw database beheerder";
		String textLine1		= "Specificeer de zoek creteria en druk op 'Zoek' om de lijst van alle outlets op een verdieping te genereren.";
		String resultText1		= "Lijst van alle outlets ";
		String resultText2		= "";
		String resultText3		= "Totaal aantal: ";
		String menuText0		= "Nieuwe zoekopdracht";
		String menuText1		= "List verdieping:";
		String menuText2		= "sorteer op:";
		String menuText3		= "Maximaal";
		String menuText4		= "hits per pagina.";
		String menuText5		= "Weergeven:";
		String logoutText		= "Druk hier om terug te gaan naar het Hoofdmenu.";
		String tempHtml			= "";

		// Database objects
		Connection conn;
		Statement stmt;
		ResultSet result;
		String query = "";
		String tempQuery;
		Integer tempInt;
		int recordCount = 0;
		int nX, mX = 0;
		boolean first;


		if( submitType == null )
		{
			// First time, an empty sheet and set the checkboxes to their default
			maxHits = "20";

			report.addItem(SimpleItem.LineBreak)
			      .addItem(SimpleItem.LineBreak)
			      .addItem(new SimpleItem(textLine1));

			// Done with the first-time-page
		}
		else if( submitType.equals("menu") )
		{	// Search query was submitted

			if(floorString.equals("K")) floorString = "-1";
			if(floorString.equals("B")) floorString = "0";

			// Restore the checkboxes
			if(CBOutlet != null) outletC = true; else outletC = false;
			if(CBRoom != null) roomC = true; else roomC = false;
			if(CBTelIP != null) TelIPC = true; else TelIPC = false;
			if(CBSERADP != null) SERADPC = true; else SERADPC = false;
			if(CBLSAHWA != null) LSAHWAC = true; else LSAHWAC = false;
			if(CBVLAN != null) VLANC = true; else VLANC = false;
			if(CBHUB != null) HUBC = true; else HUBC = false;
			if(CBPORT != null) PORTC = true; else PORTC = false;
			if(CBSegment != null) SegmentC = true; else SegmentC = false;
			if(CBSegName != null) SegNameC = true; else SegNameC = false;
			if(CBPerson != null) PersonC = true; else PersonC = false;
			if(CBEmail != null) EmailC = true; else EmailC = false;
			if(CBLastEdit != null) LastEditC = true; else LastEditC = false;
			if(CBEditBy != null) EditByC = true; else LastEditC = false;
			if(CBComments != null) CommentsC = true; else CommentsC = false;

			// Open the database

			try
			{
				Class.forName((String) bnvConstantsLO.getDriver_class());
			}
			catch(ClassNotFoundException e)
			{
				errorMsg.addItem(new SimpleItem(dbaseError))
				        .addItem(new SimpleItem("<BR>Could not find the JDBC driver: " + bnvConstantsLO.getDriver_class() ))
				        .addItem(new SimpleItem("<BR>Message: " + e.getMessage()) );
			}
			try
			{
				conn = DriverManager.getConnection(bnvConstantsLO.getConnect_URL(), bnvConstantsLO.getConnect_user(), bnvConstantsLO.getConnect_password() );
				try
				{
					stmt = conn.createStatement();
					first = true;

					// Now we can mess in the database
					// First construct the SELECT query... "SELECT BNV_COL1, BNV_COL2 FROM BNV_OUTLET WHERE BNV_FLOOR = floorString ORDER BY orderByString

					query = "SELECT ";
					if(  (tempQuery = addQuery("BNV_OUTLET", outletC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_ROOM", roomC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_TEL_IP", TelIPC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_SER_ADP", SERADPC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_LSA_HWA", LSAHWAC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_VLAN", VLANC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_HUB", HUBC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_PORT", PORTC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_SEGMENT", SegmentC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_SEG_NAME", SegNameC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_PERSON", PersonC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_EMAIL", EmailC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_LASTEDIT", LastEditC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_EDITBY", EditByC, first)) != null ) { query = query + tempQuery; first = false; }
					if(  (tempQuery = addQuery("BNV_COMMENTS", CommentsC, first)) != null ) { query = query + tempQuery; first = false; }

					query = query + " FROM BNV_OUTLET WHERE BNV_FLOOR = " + floorString + " ORDER BY ";

					if     (orderByString.equals("Outlet")) query = query + "BNV_OUTLET";
					else if(orderByString.equals("Kamer")) query = query + "BNV_ROOM";
					else if(orderByString.equals("Telefoon- of IP-nr")) query = query + "BNV_TEL_IP";
					else if(orderByString.equals("Ser nummer")) query = query + "BNV_SER_ADP";
					else if(orderByString.equals("LSA - HWA")) query = query + "BNV_LSA_HWA";
					else if(orderByString.equals("VLAN")) query = query + "BNV_VLAN";
					else if(orderByString.equals("HUB / Stack")) query = query + "BNV_HUB";
					else if(orderByString.equals("Port nummer")) query = query + "BNV_PORT";
					else if(orderByString.equals("Segment")) query = query + "BNV_SEGMENT";
					else if(orderByString.equals("Segment naam"))  query = query + "BNV_SEG_NAME";
					else if(orderByString.equals("Person"))  query = query + "BNV_PERSON";
					else if(orderByString.equals("Email adres"))  query = query + "BNV_EMAIL";
					else if(orderByString.equals("Laatste wijziging"))  query = query + "BNV_LASTEDIT";
					else if(orderByString.equals("Gewijzigd door"))  query = query + "BNV_EDITBY";
					else if(orderByString.equals("Aantekeningen"))  query = query + "BNV_COMMENTS";

					// Determine the number of total hits
					result = stmt.executeQuery("SELECT COUNT(*) FROM BNV_OUTLET WHERE BNV_FLOOR = " + floorString);
					result.next();
					recordCount = result.getInt(1);
					// report.addItem(new SimpleItem(query));	// for testing only..


					// Apply the custom query :)

					result = stmt.executeQuery(query);
					Integer intMax = new Integer(maxHits);

					// Create the caption of the result-table
					row = new TableRow();
					if(outletC)	row.addCell(new TableDataCell(new SimpleItem("outlet").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(roomC)	row.addCell(new TableDataCell(new SimpleItem("kamer").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(TelIPC)	row.addCell(new TableDataCell(new SimpleItem("tel- of IP nr").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(SERADPC)	row.addCell(new TableDataCell(new SimpleItem("ser nr").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(LSAHWAC)	row.addCell(new TableDataCell(new SimpleItem("Hardware adres").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(VLANC)	row.addCell(new TableDataCell(new SimpleItem("VLAN").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(HUBC)	row.addCell(new TableDataCell(new SimpleItem("HUB / Stack").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(PORTC)	row.addCell(new TableDataCell(new SimpleItem("port nr").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(SegmentC)	row.addCell(new TableDataCell(new SimpleItem("segment").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(SegNameC)	row.addCell(new TableDataCell(new SimpleItem("segment naam").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(PersonC)	row.addCell(new TableDataCell(new SimpleItem("persoon").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(EmailC)	row.addCell(new TableDataCell(new SimpleItem("Email adres").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(LastEditC)	row.addCell(new TableDataCell(new SimpleItem("laatste wijziging").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(EditByC)	row.addCell(new TableDataCell(new SimpleItem("gewijzigd door").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					if(CommentsC)	row.addCell(new TableDataCell(new SimpleItem("opmerkingen").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
					resultTable.addRow(row).setCenter();
					resultTable.setCellSpacing(1).setBorder(0).setCellPadding(3);

					for(nX = 0; nX < intMax.intValue() ; ++nX)
					{
						if(result.next())	// only if there is another row
						{
							mX = 1;
							row = new TableRow();
							if(outletC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(roomC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(TelIPC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(SERADPC) row.addCell(new TableDataCell(new SimpleItem( (String) String.valueOf((int)result.getInt(mX++)) )).setNoWrap(true).setHAlign(3));
							if(LSAHWAC) row.addCell(new TableDataCell(new SimpleItem( (String) String.valueOf((int)result.getInt(mX++)) )).setNoWrap(true).setHAlign(3));
							if(VLANC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(HUBC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(PORTC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(SegmentC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(SegNameC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(PersonC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(EmailC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(LastEditC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(EditByC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
							if(CommentsC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));

							resultTable.addRow(row);
						}
					}

					// Done with filling the result-table, now creat the statistics table

					nextHits	 = new Submit("SubmitNextHits", "Volgende "+maxHits);
					nextTable.setCellSpacing(3).setBorder(0).setWidth("100%");

					row = new TableRow();
					row.addCell(new TableDataCell(new SimpleItem(resultText3 + (String) String.valueOf((int)recordCount) ) ))
					   .addCell(new TableDataCell(new SimpleItem("weergegeven: 1 t/m " + maxHits) ).setHAlign(2));
					if(recordCount > intMax.intValue())	// if there's a next page
						row.addCell(new TableDataCell(nextHits).setHAlign(3) );
					nextTable.addRow(row);

					// next-table done, place all in the report

					if((floorString == "K") || (floorString == "-1")) resultText2 = resultText1 + "in de kelder.";
					else if((floorString == "B") || (floorString == "0")) resultText2 = resultText1 + "op de begane grond.";
					else
					{	resultText2 = resultText1 + floorString + "e verdieping.";
					}

					report.addItem(SimpleItem.LineBreak)
					      .addItem(new SimpleItem(resultText2).setBold().setCenter() )
					      .addItem(SimpleItem.LineBreak);
					if(errorMsg.size() > 0)
						report.addItem(errorMsg);
					else
						report.addItem(resultTable);
					report.addItem(SimpleItem.LineBreak);

					//tempInt = new Integer(maxHits);

					nextForm.addItem(new Hidden("sessionid", sessionID))

					        .addItem(new Hidden("outletCB", CBOutlet))
					        .addItem(new Hidden("roomCB", CBRoom))
					        .addItem(new Hidden("TelIPCB", CBTelIP))
					        .addItem(new Hidden("SERADPCB", CBSERADP))
					        .addItem(new Hidden("LSAHWACB", CBLSAHWA))
					        .addItem(new Hidden("VLANCB", CBVLAN))
					        .addItem(new Hidden("HUBCB", CBHUB))
					        .addItem(new Hidden("PORTCB", CBPORT))
					        .addItem(new Hidden("SegmentCB", CBSegment))
					        .addItem(new Hidden("SegNameCB", CBSegName))
					        .addItem(new Hidden("PersonCB", CBPerson))
					        .addItem(new Hidden("EmailCB", CBEmail))
					        .addItem(new Hidden("LastEditCB", CBLastEdit))
					        .addItem(new Hidden("EditByCB", CBEditBy))
					        .addItem(new Hidden("CommentsCB", CBComments))
					        .addItem(new Hidden("floorPD", floorString))
					        .addItem(new Hidden("orderBy", orderByString))

					        .addItem(new Hidden("maxHits", maxHits))
					        .addItem(new Hidden("from", maxHits))
					        .addItem(new Hidden("to", String.valueOf( (int) ((int) new Integer(maxHits).intValue() * 2)  )))
					        .addItem(new Hidden("button", "next"))
					        .addItem(nextTable);
					report.addItem(nextForm)
					      .addItem(SimpleItem.HorizontalRule);


				}
				catch(SQLException e)
				{
					errorMsg.addItem(new SimpleItem(dbaseError))
					        .addItem(new SimpleItem("<BR>Message: " + e.getMessage()) );
				}
				finally
				{	conn.close();
				}
			}
			catch(SQLException e)
			{
				errorMsg.addItem(new SimpleItem(dbaseError))
				        .addItem(new SimpleItem("<BR>Message: " + e.getMessage()) );
			}



		}
		else if( submitType.equals("next") )
		{
			// User requested the next 20 (or so) records

			// Restore the checkboxes
			if(CBOutlet.equals("check")) outletC = true; else outletC = false;
			if(CBRoom.equals("check")) roomC = true; else roomC = false;
			if(CBTelIP.equals("check") ) TelIPC = true; else TelIPC = false;
			if(CBSERADP.equals("check") ) SERADPC = true; else SERADPC = false;
			if(CBLSAHWA.equals("check") ) LSAHWAC = true; else LSAHWAC = false;
			if(CBVLAN.equals("check") ) VLANC = true; else VLANC = false;
			if(CBHUB.equals("check") ) HUBC = true; else HUBC = false;
			if(CBPORT.equals("check") ) PORTC = true; else PORTC = false;
			if(CBSegment.equals("check") ) SegmentC = true; else SegmentC = false;
			if(CBSegName.equals("check") ) SegNameC = true; else SegNameC = false;
			if(CBPerson.equals("check") ) PersonC = true; else PersonC = false;
			if(CBEmail.equals("check") ) EmailC = true; else EmailC = false;
			if(CBLastEdit.equals("check") ) LastEditC = true; else LastEditC = false;
			if(CBEditBy.equals("check") ) EditByC = true; else EditByC = false;
			if(CBComments.equals("check") ) CommentsC = true; else CommentsC = false;

			// Get some new parameters
			String from	= req.getParameter("from");
			String to	= req.getParameter("to");

			// Just in case, probably already in right format anyway..
			if(floorString.equals("K")) floorString = "-1";
			if(floorString.equals("B")) floorString = "0";

			// Open the database
			try
			{
				Class.forName((String) bnvConstantsLO.getDriver_class());
			}
			catch(ClassNotFoundException e)
			{
				errorMsg.addItem(new SimpleItem(dbaseError))
				        .addItem(new SimpleItem("<BR>Could not find the JDBC driver: " + bnvConstantsLO.getDriver_class() ))
				        .addItem(new SimpleItem("<BR>Message: " + e.getMessage()) );
			}
			try
			{
				conn = DriverManager.getConnection(bnvConstantsLO.getConnect_URL(), bnvConstantsLO.getConnect_user(), bnvConstantsLO.getConnect_password() );
				try
				{
					stmt = conn.createStatement();

					// Determine the number of total hits
					result = stmt.executeQuery("SELECT COUNT(*) FROM BNV_OUTLET WHERE BNV_FLOOR = " + floorString);
					result.next();
					recordCount = result.getInt(1);

					tempInt = new Integer(from);
					if(tempInt.intValue() > recordCount)
					{
						errorMsg.addItem(new SimpleItem("Alle records zijn weergegeven."))
						        .addItem(SimpleItem.LineBreak);
					}
					else	// construct the result table
					{
						first = true;

						// Now we can mess in the database
						// First construct the SELECT query... "SELECT BNV_COL1, BNV_COL2 FROM BNV_OUTLET WHERE BNV_FLOOR = floorString ORDER BY orderByString

						query = "SELECT ";
						if(  (tempQuery = addQuery("BNV_OUTLET", outletC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_ROOM", roomC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_TEL_IP", TelIPC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_SER_ADP", SERADPC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_LSA_HWA", LSAHWAC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_VLAN", VLANC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_HUB", HUBC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_PORT", PORTC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_SEGMENT", SegmentC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_SEG_NAME", SegNameC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_PERSON", PersonC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_EMAIL", EmailC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_LASTEDIT", LastEditC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_EDITBY", EditByC, first)) != null ) { query = query + tempQuery; first = false; }
						if(  (tempQuery = addQuery("BNV_COMMENTS", CommentsC, first)) != null ) { query = query + tempQuery; first = false; }

						query = query + " FROM BNV_OUTLET WHERE BNV_FLOOR = " + floorString + " ORDER BY ";

						if     (orderByString.equals("Outlet")) query = query + "BNV_OUTLET";
						else if(orderByString.equals("Kamer")) query = query + "BNV_ROOM";
						else if(orderByString.equals("Telefoon- of IP-nr")) query = query + "BNV_TEL_IP";
						else if(orderByString.equals("Ser nummer")) query = query + "BNV_SER_ADP";
						else if(orderByString.equals("LSA - HWA")) query = query + "BNV_LSA_HWA";
						else if(orderByString.equals("VLAN")) query = query + "BNV_VLAN";
						else if(orderByString.equals("HUB / Stack")) query = query + "BNV_HUB";
						else if(orderByString.equals("Port nummer")) query = query + "BNV_PORT";
						else if(orderByString.equals("Segment")) query = query + "BNV_SEGMENT";
						else if(orderByString.equals("Segment naam"))  query = query + "BNV_SEG_NAME";
						else if(orderByString.equals("Person"))  query = query + "BNV_PERSON";
						else if(orderByString.equals("Email adres"))  query = query + "BNV_EMAIL";
						else if(orderByString.equals("Laatste wijziging"))  query = query + "BNV_LASTEDIT";
						else if(orderByString.equals("Gewijzigd door"))  query = query + "BNV_EDITBY";
						else if(orderByString.equals("Aantekeningen"))  query = query + "BNV_COMMENTS";

						// Apply the custom query :)

						result = stmt.executeQuery(query);

						// Create the caption of the result-table
						row = new TableRow();
						if(outletC)	row.addCell(new TableDataCell(new SimpleItem("outlet").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(roomC)	row.addCell(new TableDataCell(new SimpleItem("kamer").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(TelIPC)	row.addCell(new TableDataCell(new SimpleItem("tel- of IP nr").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(SERADPC)	row.addCell(new TableDataCell(new SimpleItem("ser nr").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(LSAHWAC)	row.addCell(new TableDataCell(new SimpleItem("Hardware adres").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(VLANC)	row.addCell(new TableDataCell(new SimpleItem("VLAN").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(HUBC)	row.addCell(new TableDataCell(new SimpleItem("HUB / Stack").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(PORTC)	row.addCell(new TableDataCell(new SimpleItem("port nr").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(SegmentC)	row.addCell(new TableDataCell(new SimpleItem("segment").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(SegNameC)	row.addCell(new TableDataCell(new SimpleItem("segment naam").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(PersonC)	row.addCell(new TableDataCell(new SimpleItem("persoon").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(EmailC)	row.addCell(new TableDataCell(new SimpleItem("Email adres").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(LastEditC)	row.addCell(new TableDataCell(new SimpleItem("laatste wijziging").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(EditByC)	row.addCell(new TableDataCell(new SimpleItem("gewijzigd door").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						if(CommentsC)	row.addCell(new TableDataCell(new SimpleItem("opmerkingen").setBold()).setBackgroundColor(Color.lightGray).setHAlign(2) );
						resultTable.addRow(row).setCenter();
						resultTable.setCellSpacing(1).setBorder(0).setCellPadding(3);

						// first FForward to the starting point..
						tempInt = new Integer(from);
						for(nX=0; nX<tempInt.intValue(); nX++) result.next();

						tempInt = new Integer(maxHits);
						for(nX = 0; nX < tempInt.intValue() ; ++nX)
						{
							if(result.next())	// only if there is another row
							{
								mX = 1;
								row = new TableRow();
								if(outletC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(roomC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(TelIPC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(SERADPC) row.addCell(new TableDataCell(new SimpleItem( (String) String.valueOf((int)result.getInt(mX++)) )).setNoWrap(true).setHAlign(3));
								if(LSAHWAC) row.addCell(new TableDataCell(new SimpleItem( (String) String.valueOf((int)result.getInt(mX++)) )).setNoWrap(true).setHAlign(3));
								if(VLANC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(HUBC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(PORTC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(SegmentC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(SegNameC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(PersonC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(EmailC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(LastEditC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(EditByC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));
								if(CommentsC) row.addCell(new TableDataCell(new SimpleItem(result.getString(mX++))).setNoWrap(true).setHAlign(3));

								resultTable.addRow(row);
							}
						}


						// Done with filling the result-table, now creat the statistics table

						nextHits	 = new Submit("SubmitNextHits", "Volgende "+maxHits);
						nextTable.setCellSpacing(3).setBorder(0).setWidth("100%");

						row = new TableRow();
						row.addCell(new TableDataCell(new SimpleItem(resultText3 + (String) String.valueOf((int)recordCount) ) ))
						   .addCell(new TableDataCell(new SimpleItem("weergegeven: " + from + " t/m " + to) ).setHAlign(2));
						tempInt = new Integer(to);
						if(recordCount > tempInt.intValue())	// if there's a next page
							row.addCell(new TableDataCell(nextHits).setHAlign(3) );
						nextTable.addRow(row);
					}

					// next-table done, place all in the report

					if((floorString.equals("K")) || (floorString.equals("-1"))) resultText2 = resultText1 + "in de kelder.";
					else if((floorString.equals("B")) || (floorString.equals("0"))) resultText2 = resultText1 + "op de begane grond.";
					else
					{	resultText2 = resultText1 + floorString + "e verdieping.";
					}

					report.addItem(SimpleItem.LineBreak)
					      .addItem(new SimpleItem(resultText2).setBold().setCenter() )
					      .addItem(SimpleItem.LineBreak);
					if(errorMsg.size() > 0)
						report.addItem(errorMsg);
					else
						report.addItem(resultTable);
					report.addItem(SimpleItem.LineBreak);

					nextForm.addItem(new Hidden("sessionid", sessionID))

					        .addItem(new Hidden("outletCB", CBOutlet))
					        .addItem(new Hidden("roomCB", CBRoom))
					        .addItem(new Hidden("TelIPCB", CBTelIP))
					        .addItem(new Hidden("SERADPCB", CBSERADP))
					        .addItem(new Hidden("LSAHWACB", CBLSAHWA))
					        .addItem(new Hidden("VLANCB", CBVLAN))
					        .addItem(new Hidden("HUBCB", CBHUB))
					        .addItem(new Hidden("PORTCB", CBPORT))
					        .addItem(new Hidden("SegmentCB", CBSegment))
					        .addItem(new Hidden("SegNameCB", CBSegName))
					        .addItem(new Hidden("PersonCB", CBPerson))
					        .addItem(new Hidden("EmailCB", CBEmail))
					        .addItem(new Hidden("LastEditCB", CBLastEdit))
					        .addItem(new Hidden("EditByCB", CBEditBy))
					        .addItem(new Hidden("CommentsCB", CBComments))
					        .addItem(new Hidden("floorPD", floorString))
					        .addItem(new Hidden("orderBy", orderByString))

					        .addItem(new Hidden("maxHits", maxHits))
					        .addItem(new Hidden("from", to))
					        .addItem(new Hidden("to", String.valueOf( (int) ((int) new Integer(to).intValue() + (int) new Integer(maxHits).intValue() )  )  ))
					        .addItem(new Hidden("button", "next"))
					        .addItem(nextTable);
					report.addItem(nextForm)
					      .addItem(SimpleItem.HorizontalRule);


				}
				catch(SQLException e)
				{
					errorMsg.addItem(new SimpleItem(dbaseError))
					        .addItem(new SimpleItem("<BR>Message: " + e.getMessage()) );
				}
				finally
				{	conn.close();
				}
			}
			catch(SQLException e)
			{
				errorMsg.addItem(new SimpleItem(dbaseError))
				        .addItem(new SimpleItem("<BR>Message: " + e.getMessage()) );
			}
		}



		// Create the "new-search-menu"
		TextField maxHitsField	= new TextField("maxHits", 3,3, maxHits);
		
		// Create the CheckBoxes with their default (hardcoded) state
		CheckBox outletCB	= new CheckBox("outletCB", "check", outletC);		// default on
		CheckBox roomCB		= new CheckBox("roomCB", "check", roomC);		// default on
		CheckBox TelIPCB	= new CheckBox("TelIPCB", "check", TelIPC);		// default on
		CheckBox SERADPCB	= new CheckBox("SERADPCB", "check", SERADPC);		// default on
		CheckBox LSAHWACB	= new CheckBox("LSAHWACB", "check", LSAHWAC);		// default off
		CheckBox VLANCB		= new CheckBox("VLANCB", "check", VLANC);		// default off
		CheckBox HUBCB		= new CheckBox("HUBCB", "check", HUBC);			// default off
		CheckBox PORTCB		= new CheckBox("PORTCB", "check", PORTC);		// default off
		CheckBox SegmentCB	= new CheckBox("SegmentCB", "check", SegmentC);		// default on
		CheckBox SegNameCB	= new CheckBox("SegNameCB", "check", SegNameC);		// default off
		CheckBox PersonCB	= new CheckBox("PersonCB", "check", PersonC);		// default on
		CheckBox EmailCB	= new CheckBox("EmailCB", "check", EmailC);		// default on
		CheckBox LastEditCB	= new CheckBox("LastEditCB", "check", LastEditC);	// default off
		CheckBox EditByCB	= new CheckBox("EditByCB", "check", EditByC);		// default off
		CheckBox CommentsCB	= new CheckBox("CommentsCB", "check", CommentsC);	// default on

		// The little pull down box for the floor selection..
		floorPD.addOption(new Option ("K"))
		       .addOption(new Option ("B"))
		       .addOption(new Option ("1"))
		       .addOption(new Option ("2"))
		       .addOption(new Option ("3"))
		       .addOption(new Option ("4"))
		       .addOption(new Option ("5"))
		       .addOption(new Option ("6"));

		orderByPD.addOption(new Option ("Outlet"))
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

		menuTable.setBackgroundColor(Color.lightGray).setBorderColor(Color.lightGray).setCellSpacing(1).setBorder(1).setCellPadding(3);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem(menuText0).setBold()));
		menuTable.addRow(row);

		row = new TableRow();
		tempHtml = menuText1 + floorPD.toHTML() + menuText2 + orderByPD.toHTML() + menuText3 + maxHitsField.toHTML() + menuText4;
		row.addCell(new TableDataCell(new SimpleItem(tempHtml)) );
		menuTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem(menuText5)));
		menuTable.addRow(row);

		row = new TableRow();
		tempHtml = new String();
		tempHtml = outletCB.toHTML() + "outlet, " + roomCB.toHTML() + "kamer nr, " + TelIPCB.toHTML() + "Tel- of IPnummer, " + SERADPCB.toHTML() + "Ser nummer, " + LSAHWACB.toHTML() + "Hardware adres, " + VLANCB.toHTML() + "VLAN, " + HUBCB.toHTML() + "HUB/Stack, " + PORTCB.toHTML() + "Port#, " + SegmentCB.toHTML() + "segment, " + SegNameCB.toHTML() + "segment naam, " + PersonCB.toHTML() + "Persoon, " + EmailCB.toHTML() + "Email, " + LastEditCB.toHTML() + "laatste wijziging, " + EditByCB.toHTML() + "gewijzigd door, " + CommentsCB.toHTML() + "Opmerkingen";
		row.addCell(new TableDataCell(new SimpleItem(tempHtml)) );
		menuTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(go).setWidth(120).setHAlign(1));
		menuTable.addRow(row);

		menuForm.addItem(new Hidden("sessionid", sessionID))
		        .addItem(new Hidden("button", "menu"))
		        .addItem(menuTable);

		report.addItem(menuForm)
		      .addItem(SimpleItem.LineBreak)
		      .addItem(SimpleItem.HorizontalRule);

		// OK, the search menu is done, now create the button form back to the main menu

		logoutTable.setCellSpacing(2).setBorder(0);

		row = new TableRow();
		row.addCell(new TableDataCell(logout).setWidth(120).setHAlign(1) )
		   .addCell(new TableDataCell(new SimpleItem(logoutText)));
		logoutTable.addRow(row);
		logoutForm.addItem(logoutTable);
		logoutForm.addItem(new Hidden("sessionid", sessionID));

		report.addItem(logoutForm);


    		// Load dynamic content into Standaard.html
		template.setItemAt(tag1, ((new SimpleItem(""))))
       	    	        .setItemAt(tag2, ((new SimpleItem(headerText1))).setBold())
      		        .setItemAt(tag3, (report))
       		        .setItemAt(tag4, (new SimpleItem(bnvConstantsLO.getCopyRight_Page())))
      		        .setItemAt(tag5, (new SimpleItem(date.toString())))
      		        .setItemAt(tag6, (new SimpleItem(bnvConstantsLO.getPageAdministrator_Name())))
      		        .setItemAt(tag7, (new SimpleItem(bnvConstantsLO.getwebMaster_Name())));

    		template.print(out);

	} // End of runServlet() memberfunction

} // End of class
