// BNV Online - Datawarehouse applicatie
// Erik van Zijst, 14.12.98 icehawk@bart.nl

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


// TU Delft BNV project - BNV_EditOutlet pagina
// At this page you can edit the an existing record. Links to other pages.
// This page is called from the main Menu. Accessible to admins only.

public class BNV_EditOutlet extends HttpServlet
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
		BNV_Constants bnvConstantsEO	= new BNV_Constants();
		String sessionID		= null;
		sessionID			= req.getParameter("sessionid");
		if(sessionID == null)
		{
			new BNV_ErrorPage().cancelSession(req, res);
			return true;
		}

		// Now validate the session and equivalences and stamp it
		String sessionStatus	= new BNV_SessionStamp().checkSession(sessionID, bnvConstantsEO.getBNV_EditOutletsEqs());
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


	public Form generateEditForm(String sessionID, String outlet)
	{
		BNV_Constants bnvConstantsEO	= new BNV_Constants();
		Form editForm			= new Form("POST", bnvConstantsEO.getEditOutletEditFormAddress());	// edit-box menu
		String textLine1		= "Typ het nummer van de outlet die je wilt editten en druk op 'edit'.";
		String textLine2		= "Outlet nummer: ";

		DynamicTable table		= new DynamicTable(3);
		Submit editSubmit		= new Submit("SubmitEdit", "edit");
		TextField outletBox		= new TextField("outletBox", 10,10, outlet);
		TableRow row			= new TableRow();

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem(textLine2)))
		   .addCell(new TableDataCell(outletBox))
		   .addCell(new TableDataCell(editSubmit));
		table.addRow(row);

		editForm.addItem(new SimpleItem(textLine1))
		        .addItem(table.setBorder(0).setCellSpacing(0).setCellPadding(0))
		        .addItem(new Hidden("sessionid", sessionID))
		        .addItem(new Hidden("button", "edit"));

		return editForm;
	}


	public Form generateSearchForm(String sessionID)
	{
		BNV_Constants bnvConstantsEO	= new BNV_Constants();
		Form searchForm			= new Form("POST", bnvConstantsEO.getEditOutletSearchFormAddress());	// Search menu
		String textLine1		= " naar een bepaalde outlet aan de hand van wildcards.";

		DynamicTable table		= new DynamicTable(2);
		Submit searchSubmit		= new Submit("SubmitSearch", "Zoek");
		TableRow row			= new TableRow();

		row.addCell(new TableDataCell(searchSubmit))
		   .addCell(new TableDataCell(new SimpleItem(textLine1)));
		table.addRow(row);

		searchForm.addItem(table.setBorder(0))
		          .addItem(new Hidden("sessionid", sessionID));

		return searchForm;
	}


	public Form generateHoofdmenuForm(String sessionID)
	{
		BNV_Constants bnvConstantsEO	= new BNV_Constants();
		Form hoofdmenuForm		= new Form("POST", bnvConstantsEO.getEditOutletHoofdmenuFormAddress());	// Search menu
		String textLine1		= "Klik hier om terug te keren naar het hoofdmenu. (Eventuele wijzigingen worden niet opgeslagen.)";

		DynamicTable table		= new DynamicTable(2);
		Submit hoofdmenuSubmit		= new Submit("SubmitHoofdmenu", "Hoofdmenu");
		TableRow row			= new TableRow();

		row.addCell(new TableDataCell(hoofdmenuSubmit))
		   .addCell(new TableDataCell(new SimpleItem(textLine1)));
		table.addRow(row);

		hoofdmenuForm.addItem(table.setBorder(0).setCellSpacing(6))
		             .addItem(new Hidden("sessionid", sessionID));

		return hoofdmenuForm;
	}


	public Form generateOopsForm(String sessionID, String outlet)
	{
		BNV_Constants bnvConstantsEO	= new BNV_Constants();
		Form oopsForm			= new Form("POST", bnvConstantsEO.getEditOutletOopsFormAddress());
		Submit oopsSubmit		= new Submit("SubmitOops", "Oops");
		DynamicTable table		= new DynamicTable(2);
		TableRow row			= new TableRow();
		String textLine1		= "foutje... (wijzig opnieuw)";

		row.addCell(new TableDataCell(oopsSubmit))
		   .addCell(new TableDataCell(new SimpleItem(textLine1)));
		table.addRow(row);

		oopsForm.addItem(table.setBorder(0).setCellSpacing(0))
		        .addItem(new Hidden("sessionid", sessionID))
		        .addItem(new Hidden("button", "edit"))
		        .addItem(new Hidden("outletBox", outlet));

		return oopsForm;
	}


	public DynamicTable generateResultTable(String r1, String r2, String r3, String r4, String r5, String r6, String r7, String r8, String r9, String r10, String r11)
	{
		DynamicTable resultTable	= new DynamicTable(2);
		TableRow row			= new TableRow();
		resultTable.setBorder(0).setBackgroundColor(Color.lightGray).setCellSpacing(0).setCellPadding(0);

		row.addCell(new TableDataCell(new SimpleItem("outlet:")))
		   .addCell(new TableDataCell(new SimpleItem(r1)));
		resultTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("kamer:")))
		   .addCell(new TableDataCell(new SimpleItem(r2)));
		resultTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Tel. / IPnr:")))
		   .addCell(new TableDataCell(new SimpleItem(r3)));
		resultTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Ser ADP:")))
		   .addCell(new TableDataCell(new SimpleItem(r4)));
		resultTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("LSA + HWA:")))
		   .addCell(new TableDataCell(new SimpleItem(r5)));
		resultTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("VLAN:")))
		   .addCell(new TableDataCell(new SimpleItem(r6)));
		resultTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("HUB / Stack:")))
		   .addCell(new TableDataCell(new SimpleItem(r7)));
		resultTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Port nr:")))
		   .addCell(new TableDataCell(new SimpleItem(r8)));
		resultTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Segment:")))
		   .addCell(new TableDataCell(new SimpleItem(r9)));
		resultTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Segment naam:")))
		   .addCell(new TableDataCell(new SimpleItem(r10)));
		resultTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Opmerkingen:")))
		   .addCell(new TableDataCell(new SimpleItem(r11)));
		resultTable.addRow(row);

		return resultTable;
	}


	public Form generateReEditForm(String sessionID, String r1, String r2, String r3, String r4, String r5, String r6, String r7, String r8, String r9, String r10, String r11)
	{
		BNV_Constants bnvConstantsEO	= new BNV_Constants();
		DynamicTable editTable		= new DynamicTable(3);
		Form reEditForm			= new Form("POST", bnvConstantsEO.getEditOutletReEditFormAddress());	// edit-box menu
		String editText1		= "Herstel de fouten en druk op 'opslaan'.";

		Submit saveSubmit		= new Submit("SubmitSave", "Opslaan");


		TableRow row			= new TableRow();

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem(editText1)).setColSpan(3));
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Outlet:").setBold().setFontColor(Color.red)) )
		   .addCell(new TableDataCell(new TextField("field1", 15,12, r1)))
		   .addCell(new TableDataCell(new SimpleItem("Voorbeeld: LD17.17")).setHAlign(1) );
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Kamer nr:").setBold().setFontColor(Color.red)) )
		   .addCell(new TableDataCell(new TextField("field2", 20,12, r2)))
		   .addCell(new TableDataCell(new SimpleItem("Voorbeeld: 3.06.1 (Nummeriek)")).setHAlign(1) );
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Tel. / IPnr:").setBold()) )
		   .addCell(new TableDataCell(new TextField("field3", 20,12, r3)));
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Ser ADP:").setBold()) )
		   .addCell(new TableDataCell(new TextField("field4", 10,12, r4)));
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("LSA + HWA:").setBold()) )
		   .addCell(new TableDataCell(new TextField("field5", 10,12, r5)));
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("VLAN:").setBold()) )
		   .addCell(new TableDataCell(new TextField("field6", 30,12, r6)));
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("HUB / Stack:").setBold()) )
		   .addCell(new TableDataCell(new TextField("field7", 15,12, r7)));
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Port nr:").setBold()) )
		   .addCell(new TableDataCell(new TextField("field8", 10,12, r8)));
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Segment:").setBold()) )
		   .addCell(new TableDataCell(new TextField("field9", 15,12, r9)));
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Segment naam:").setBold()) )
		   .addCell(new TableDataCell(new TextField("field10", 30,12, r10)));
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Opmerkingen:").setBold()) )
		   .addCell(new TableDataCell(new TextField("field11", 255,12, r11)));
		editTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem(" ")))
		   .addCell(new TableDataCell(saveSubmit).setWidth(120).setHAlign(2));
		editTable.addRow(row);

		reEditForm.addItem(new Hidden("sessionid", sessionID))
		        .addItem(editTable.setBorder(0))
		        .addItem(new Hidden("button", "save"));		// the submitType

		return reEditForm;
	}


	public void runServlet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		// Gimme default constants-object
		ServletOutputStream out		= res.getOutputStream();
		BNV_Constants bnvConstantsEO	= new BNV_Constants();
		Date date			= new Date();
		String sessionID		= null;
		String useridString		= null;
		String submitType		= null;
		String outlet			= null;
		String room			= null;
		String telIP			= null;

		// Standard HTML components / references
		HtmlFile template 		= new HtmlFile(bnvConstantsEO.getHtmlFile_Name());
		CompoundItem report		= new CompoundItem();
		CompoundItem errorMsg		= new CompoundItem();
		Form editForm			= new Form("POST", bnvConstantsEO.getEditOutletsEditFormAddress());	// Save button
		Form cancelForm			= new Form("POST", bnvConstantsEO.getEditOutletsCancelFormAddress());	// 'Annuleren' button
		DynamicTable editTable		= new DynamicTable(3);
		Submit submit			= new Submit("Submit", "Opslaan");
		Submit cancel			= new Submit("Submit", "Annuleren");
		TableRow row;

		// HTML constants for BNV_ListOutlets
       		String tag1			= "dynamicItem1";
	       	String tag2			= "dynamicItem2";
	        String tag3			= "dynamicItem3";
	        String tag4			= "dynamicItem4";
        	String tag5			= "dynamicItem5";
	        String tag6			= "dynamicItem6";
        	String tag7			= "dynamicItem7";
		String headerText1		= "BNV Online  -  Wijzig een outlet";
	        String dbaseError		= "Database probleem: neem contact op met uw database beheerder";
		String errorText2		= "FOUT: Uw loginnaam kon niet achterhaald worden. (Dit is noodzakelijk voor in de log files.)";
		String editText1		= "Klik na het aanpassen op 'opslaan' om de wijzigingen door te voeren. 'Annuleren' keert terug zonder wijzigingen door te voeren.<BR>N.B. de rode velden moeten verplicht ingevuld worden.<BR>";
		String editText2		= "De record is aangepast, de wijzigingen zijn doorgevoerd in het systeem.<BR>De nieuwe waarden van het outlet zijn:<BR>";
		String field1			= null;
		String field2			= null;
		String field3			= null;
		String field4			= null;
		String field5			= null;
		String field6			= null;
		String field7			= null;
		String field8			= null;
		String field9			= null;
		String field10			= null;
		String field11			= null;
		String temp			= null;		// just for some extra space


		// Database objects
		Connection conn;
		Statement stmt;
		ResultSet result;

		// get the URL parameters
		sessionID			= req.getParameter("sessionid");
		submitType			= req.getParameter("button");	// which button was pressed?
		outlet				= req.getParameter("outletBox");

		report.addItem(SimpleItem.LineBreak)
		      .addItem(SimpleItem.LineBreak);

		editTable.setBorder(0);


		if(submitType == null)	// first time here, coming from the main menu
		{
			report.addItem((Form) generateEditForm(sessionID, ""))
			      .addItem((Form) generateSearchForm(sessionID))
			      .addItem(SimpleItem.HorizontalRule)
			      .addItem((Form) generateHoofdmenuForm(sessionID));
		}
		else if( submitType.equals("edit") )	// user typed an outletID and pressed 'edit'
		{
			temp = outlet;
			outlet = new BNV_CheckSyntax().checkOutlet(outlet);

//			if( (outlet == null) || (outlet.equals("")) )
			if( outlet.equals("error") )
			{
				errorMsg.addItem(new SimpleItem("FOUT: outlet '"+temp+"' is ongeldig. Invullen als bijvoorbeeld ZZ00.00 Extra punten, spaties etc zijn niet toegestaan.").setBold())
				        .addItem(SimpleItem.LineBreak);
				
			}
			else
			{
				temp = "";
				// database crackin' here
				// First, fetch the selected outlet from the database

				try
				{
					Class.forName((String) bnvConstantsEO.getDriver_class());
				}
				catch(ClassNotFoundException e)
				{
					errorMsg.addItem(new SimpleItem(dbaseError))
					        .addItem(new SimpleItem("<BR>Could not find the JDBC driver: "+ bnvConstantsEO.getDriver_class()))
					        .addItem(SimpleItem.LineBreak)
					        .addItem(new SimpleItem("Message: "+ e.getMessage()))
					        .addItem(SimpleItem.LineBreak);
				}
				try
				{
					conn = DriverManager.getConnection(bnvConstantsEO.getConnect_URL(), bnvConstantsEO.getConnect_user(), bnvConstantsEO.getConnect_password() );
					try
					{
						// First, check how many times 'outlet' is registered in the database: should be only one time!
						stmt = conn.createStatement();
						result = stmt.executeQuery("SELECT COUNT(*) FROM BNV_OUTLET WHERE BNV_OUTLET = '" + outlet + "'");
						result.next();
						if( (int) result.getInt(1) < 1)		// No hits or error!
						{
							errorMsg.addItem(new SimpleItem("Outlet " + outlet + " niet gevonden!").setBold())
							        .addItem(SimpleItem.LineBreak)
							        .addItem(SimpleItem.LineBreak);
						}
						else if( (int) result.getInt(1) > 1)		// more than 1 hit!
						{
							errorMsg.addItem(new SimpleItem("FOUT: outlet " + outlet + " komt " + (String) String.valueOf((int) result.getInt(1)) + "x voor in de database!").setBold())
							        .addItem(SimpleItem.LineBreak)
							        .addItem(SimpleItem.LineBreak);
						}
						else
						{
							stmt = conn.createStatement();
							result = stmt.executeQuery("SELECT BNV_OUTLET, BNV_ROOM, BNV_TEL_IP, BNV_SER_ADP, BNV_LSA_HWA, BNV_VLAN, BNV_HUB, BNV_PORT, BNV_SEGMENT, BNV_SEG_NAME, BNV_COMMENTS FROM BNV_OUTLET WHERE BNV_OUTLET = '" + outlet + "'");
							result.next();

							field1	= result.getString(1);		// outlet
							field2	= result.getString(2);		// kamer
							field3	= result.getString(3);		// tel/ip
							field4	= (String) String.valueOf( (int) result.getInt(4));	// ser adp
							field5	= (String) String.valueOf( (int) result.getInt(5));	// lsa hwa
							field6	= result.getString(6);		// vlan
							field7	= result.getString(7);		// hub
							field8	= result.getString(8);		// port
							field9	= result.getString(9);		// segment
							field10	= result.getString(10);		// segname
							field11	= result.getString(11);		// comments

							// OK, now construct a nice table with textfields for editting...

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem(editText1)).setColSpan(3));
							editTable.addRow(row);

							row = new TableRow();
							editTable.addRow(row);	// empty row to create extra space

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem("Outlet:").setBold().setFontColor(Color.red)) )
							   .addCell(new TableDataCell(new TextField("field1", 15,12, field1)))
							   .addCell(new TableDataCell(new SimpleItem("Voorbeeld: LD17.17")).setHAlign(1) );
							editTable.addRow(row);

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem("Kamer nr:").setBold().setFontColor(Color.red)) )
							   .addCell(new TableDataCell(new TextField("field2", 20,12, field2)))
							   .addCell(new TableDataCell(new SimpleItem("Voorbeeld: 3.06.1 (Nummeriek)")).setHAlign(1) );
							editTable.addRow(row);

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem("Tel. / IPnr:").setBold()) )
							   .addCell(new TableDataCell(new TextField("field3", 20,12, field3)));
							editTable.addRow(row);

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem("Ser ADP:").setBold()) )
							   .addCell(new TableDataCell(new TextField("field4", 10,12, field4)));
							editTable.addRow(row);

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem("LSA + HWA:").setBold()) )
							   .addCell(new TableDataCell(new TextField("field5", 10,12, field5)));
							editTable.addRow(row);

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem("VLAN:").setBold()) )
							   .addCell(new TableDataCell(new TextField("field6", 30,12, field6)));
							editTable.addRow(row);

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem("HUB / Stack:").setBold()) )
							   .addCell(new TableDataCell(new TextField("field7", 15,12, field7)));
							editTable.addRow(row);

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem("Port nr:").setBold()) )
							   .addCell(new TableDataCell(new TextField("field8", 10,12, field8)));
							editTable.addRow(row);

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem("Segment:").setBold()) )
							   .addCell(new TableDataCell(new TextField("field9", 15,12, field9)));
							editTable.addRow(row);

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem("Segment naam:").setBold()) )
							   .addCell(new TableDataCell(new TextField("field10", 30,12, field10)));
							editTable.addRow(row);

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem("Opmerkingen:").setBold()) )
							   .addCell(new TableDataCell(new TextField("field11", 255,12, field11)));
							editTable.addRow(row);

							row = new TableRow();
							editTable.addRow(row);	// empty cell 4 space

							row = new TableRow();
							row.addCell(new TableDataCell(new SimpleItem(" ")))
							   .addCell(new TableDataCell(submit).setWidth(120).setHAlign(2));
							editTable.addRow(row);

							editForm.addItem(new Hidden("sessionid", sessionID))
							        .addItem(editTable)
							        .addItem(new Hidden("button", "save"));		// the submitType

							// Now create the 'Annuleren' button-form
							cancelForm.addItem(new Hidden("sessionid", sessionID))
							          .addItem(cancel);

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

			// create some HTML report
			if(errorMsg.size() > 0)	// error occured
				report.addItem(errorMsg);
			else
			{
				// add the tables, forms 'n shit to the report here

				report.addItem(editForm)
				      .addItem(cancelForm);
			}

			report.addItem(SimpleItem.HorizontalRule)
			      .addItem((Form) generateEditForm(sessionID, temp))
			      .addItem((Form) generateSearchForm(sessionID))
			      .addItem(SimpleItem.HorizontalRule)
			      .addItem((Form) generateHoofdmenuForm(sessionID));

		}
		else if( submitType.equals("save") )	// user editted an outletID and pressed 'Opslaan'
		{
			// Grab the new data and update the database
			field1	= req.getParameter("field1");
			field2	= req.getParameter("field2");
			field3	= req.getParameter("field3");
			field4	= req.getParameter("field4");
			field5	= req.getParameter("field5");
			field6	= req.getParameter("field6");
			field7	= req.getParameter("field7");
			field8	= req.getParameter("field8");
			field9	= req.getParameter("field9");
			field10	= req.getParameter("field10");
			field11	= req.getParameter("field11");
			boolean reEdit = false;
			String floorString	= null;

			// check the syntax of the compulsary parts

			outlet	= new BNV_CheckSyntax().checkOutlet(field1);
			room	= new BNV_CheckSyntax().checkRoom(field2);
			telIP	= new BNV_CheckSyntax().checkTelIP(field3);
			if( outlet.equals("error") )
			{
				errorMsg.addItem(new SimpleItem("FOUT: Outlet '"+field1+"' is ongeldig. Invullen als bijvoorbeeld ZZ00.00 Extra punten, spaties etc zijn niet toegestaan.").setBold())
				        .addItem(SimpleItem.LineBreak);
				reEdit = true;
			}
			else if( room.equals("error") )
			{
				errorMsg.addItem(new SimpleItem("FOUT: Kamer '"+field2+"' is ongeldig. Invullen als bijvoorbeeld 3.06.1 Extra punten, spaties etc zijn niet toegestaan.").setBold())
				        .addItem(SimpleItem.LineBreak);
				reEdit = true;
			}
			else if( telIP.equals("error") )
			{
				errorMsg.addItem(new SimpleItem("FOUT: Telefoon- of IPnummer '"+field3+"' is ongeldig. Invullen als bijvoorbeeld '130.161.9.76' als IP, of '0152785421' als telefoon. Extra punten, spaties etc zijn niet toegestaan.").setBold())
				        .addItem(SimpleItem.LineBreak);
				reEdit = true;
			}
			else
			{
				// OK.. store the damn thing

				try
				{
					Class.forName((String) bnvConstantsEO.getDriver_class());
				}
				catch(ClassNotFoundException e)
				{
					errorMsg.addItem(new SimpleItem(dbaseError))
					        .addItem(new SimpleItem("<BR>Could not find the JDBC driver: "+ bnvConstantsEO.getDriver_class()))
					        .addItem(SimpleItem.LineBreak)
					        .addItem(new SimpleItem("Message: "+ e.getMessage()))
					        .addItem(SimpleItem.LineBreak);
				}
				try
				{
					conn = DriverManager.getConnection(bnvConstantsEO.getConnect_URL(), bnvConstantsEO.getConnect_user(), bnvConstantsEO.getConnect_password() );
					try
					{
						// First, check how many times 'outlet' is registered in the database: should be only one time!
						stmt = conn.createStatement();
						result = stmt.executeQuery("SELECT COUNT(*) FROM BNV_OUTLET WHERE BNV_OUTLET = '" + outlet + "'");
						result.next();
						if( (int) result.getInt(1) < 1)		// No hits or error!
						{
							errorMsg.addItem(new SimpleItem("Outlet " + outlet + " niet gevonden. Waarschijnlijk is iemand anders U voor geweest en is de outlet zojuist verwijderd.").setBold())
							        .addItem(SimpleItem.LineBreak)
							        .addItem(SimpleItem.LineBreak);
						}
						else if( (int) result.getInt(1) > 1)		// more than 1 hit!
						{
							errorMsg.addItem(new SimpleItem("FOUT: outlet " + outlet + " komt " + (String) String.valueOf((int) result.getInt(1)) + "x voor in de database. Dit is een ernstig probleem, neem contact op met de database beheerder.").setBold())
							        .addItem(SimpleItem.LineBreak)
							        .addItem(SimpleItem.LineBreak);
						}
						else
						{
							// ok done, now first retrieve the current userid (for logging)

							result = stmt.executeQuery("SELECT USERID FROM BNV_SESSION WHERE SESSIONID = '"+sessionID+"'");
							result.next();
							try	// Check if the userID can be found
							{	useridString = result.getString(1);
							}
							catch(SQLException e)
							{
								errorMsg.addItem(new SimpleItem(errorText2).setBold())
								        .addItem(SimpleItem.LineBreak);
							}
							if(errorMsg.size() == 0)	// OK, store it (first remove the old record, then store the new fresh one)
							{
								if( new BNV_FloorExtractor().findFloor(room) == -2)
								{
									errorMsg.addItem(new SimpleItem("De verdieping kon niet achterhaald worden. Waarschijnlijk is het kamernummer onjuist ingevuld.").setBold() );
								}
								else
								{
									floorString = (String) String.valueOf((int) new BNV_FloorExtractor().findFloor(room));
									stmt.executeUpdate("DELETE FROM BNV_OUTLET WHERE BNV_OUTLET = '"+ outlet +"'");
									stmt.executeUpdate("COMMIT");
									stmt.executeUpdate("INSERT INTO BNV_OUTLET VALUES ('"+outlet+"', '"+room+"', "+floorString+", '"+telIP+"', "+field4+", "+field5+", '"+field6+"', '"+field7+"', '"+field8+"', '"+field9+"', '"+field10+"', '', '', SYSDATE, '"+useridString+"', '"+field11+"')");
									stmt.executeUpdate("COMMIT");
								}
							}
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


			// create some HTML report
			if(errorMsg.size() > 0)	// error occured
			{
				report.addItem(errorMsg)
				      .addItem(SimpleItem.LineBreak);
			}
			else
			{
				// add the tables, forms 'n shit to the report here
				report.addItem(editText2)
				      .addItem(SimpleItem.LineBreak)
				      .addItem( (DynamicTable) generateResultTable(outlet, room, telIP, field4, field5, field6, field7, field8, field9, field10, field11))
				      .addItem(SimpleItem.LineBreak)
				      .addItem((Form) generateOopsForm(sessionID, outlet));

			}

			if(reEdit)
			{
				report.addItem((Form) generateReEditForm(sessionID, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11))
				      .addItem(SimpleItem.LineBreak);
			}

			report.addItem(SimpleItem.HorizontalRule)
			      .addItem((Form) generateEditForm(sessionID, ""))
			      .addItem((Form) generateSearchForm(sessionID))
			      .addItem(SimpleItem.HorizontalRule)
			      .addItem((Form) generateHoofdmenuForm(sessionID));


		}


    		// Load dynamic content into Standaard.html
		template.setItemAt(tag1, ((new SimpleItem(""))))
       	    	        .setItemAt(tag2, ((new SimpleItem(headerText1))).setBold())
      		        .setItemAt(tag3, (report))
       		        .setItemAt(tag4, (new SimpleItem(bnvConstantsEO.getCopyRight_Page())))
      		        .setItemAt(tag5, (new SimpleItem(date.toString())))
      		        .setItemAt(tag6, (new SimpleItem(bnvConstantsEO.getPageAdministrator_Name())))
      		        .setItemAt(tag7, (new SimpleItem(bnvConstantsEO.getwebMaster_Name())));

    		template.print(out);
	} // end of runServlet member

} // end of class
