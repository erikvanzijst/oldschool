// BNV Online - Datawarehouse applicatie
// Erik van Zijst, 07.01.99 icehawk@bart.nl

import java.io.*;
import java.sql.*;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.html.*;
import oracle.owas.nls.*;
import oracle.owas.wrb.services.http.*;

// TU Delft BNV project - BNV_DeleteOutlet pagina
// At this page you can delete an existing record. Links to other pages.
// This page is called from the main Menu. Accessible to admins only.

public class BNV_DeleteOutlet extends HttpServlet
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
		BNV_Constants bnvConstantsDO	= new BNV_Constants();
		String sessionID		= null;
		sessionID			= req.getParameter("sessionid");
		if(sessionID == null)
		{
			new BNV_ErrorPage().cancelSession(req, res);
			return true;
		}

		// Now validate the session and equivalences and stamp it
		String sessionStatus = new BNV_SessionStamp().checkSession(sessionID, bnvConstantsDO.getBNV_DeleteOutletsEqs());
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


	public Form generateDeleteForm(String sessionID, String outlet)
	{
		BNV_Constants bnvConstantsDO	= new BNV_Constants();
		DynamicTable deleteTable	= new DynamicTable(3);
		TableRow row			= new TableRow();
		Submit deleteSubmit		= new Submit("SubmitDelete", "delete");
		Form deleteForm			= new Form("POST", bnvConstantsDO.getDeleteOutletDeleteFormAddress());
		String textLine1		= "Verwijder outlet nummer: ";

		deleteTable.setBorder(0);

		row.addCell(new TableDataCell(new SimpleItem(textLine1)))
		   .addCell(new TableDataCell(new TextField("field1", 8, 10, outlet)))
		   .addCell(new TableDataCell(deleteSubmit));
		deleteTable.addRow(row);

		deleteForm.addItem(deleteTable)
		          .addItem(new Hidden("sessionid", sessionID))
		          .addItem(new Hidden("button", "delete"));

		return deleteForm;
	}


	public Form generateZoekForm(String sessionID)
	{
		BNV_Constants bnvConstantsDO	= new BNV_Constants();
		DynamicTable zoekTable		= new DynamicTable(2);
		TableRow row			= new TableRow();
		Submit zoekSubmit		= new Submit("SubmitZoek", "Zoek");
		Form zoekForm			= new Form("POST", bnvConstantsDO.getDeleteOutletZoekFormAddress());
		String textLine1		= " een outlet om te verwijderen.";

		zoekTable.setBorder(0);

		row.addCell(new TableDataCell(zoekSubmit))
		   .addCell(new TableDataCell(new SimpleItem(textLine1)));
		zoekTable.addRow(row);

		zoekForm.addItem(zoekTable)
		        .addItem(new Hidden("sessionid", sessionID));

		return zoekForm;
	}


	public Form generateHoofdmenuForm(String sessionID)
	{
		BNV_Constants bnvConstantsDO	= new BNV_Constants();
		DynamicTable hoofdmenuTable	= new DynamicTable(2);
		TableRow row			= new TableRow();
		Submit hoofdmenuSubmit		= new Submit("SubmitHoofdmenu", "Hoofdmenu");
		Form hoofdmenuForm		= new Form("POST", bnvConstantsDO.getDeleteOutletHoofdmenuFormAddress());
		String textLine1		= "Keer terug naar het hoofdmenu.";

		hoofdmenuTable.setBorder(0);

		row.addCell(new TableDataCell(hoofdmenuSubmit))
		   .addCell(new TableDataCell(new SimpleItem(textLine1)));
		hoofdmenuTable.addRow(row);

		hoofdmenuForm.addItem(hoofdmenuTable)
		             .addItem(new Hidden("sessionid", sessionID));

		return hoofdmenuForm;
	}


	public CompoundItem generateConfirmation(String sessionID, String r1, String r2, String r3, String r4, String r5, String r6, String r7, String r8, String r9, String r10, String r11)
	{
		BNV_Constants bnvConstantsDO	= new BNV_Constants();
		Form yesForm			= new Form("POST", bnvConstantsDO.getDeleteOutletYesNoFormAddress());
		Form noForm			= new Form("POST", bnvConstantsDO.getDeleteOutletYesNoFormAddress());
		Submit yesSubmit		= new Submit("SubmitYes", "Ja");
		Submit noSubmit			= new Submit("SubmitNo", "Nee");
		DynamicTable infoTable		= new DynamicTable(2);
		CompoundItem total		= new CompoundItem();
		DynamicTable yesNoTable		= new DynamicTable(3);
		DynamicTable yesTable		= new DynamicTable(1);
		DynamicTable noTable		= new DynamicTable(1);
		TableRow row			= new TableRow();
		String textLine1		= "Doorgaan met verwijderen?";

		// first create the info table
		infoTable.setBorder(0).setBackgroundColor(Color.lightGray).setCellSpacing(0).setCellPadding(0);
		yesTable.setBorder(0);
		noTable.setBorder(0);
		yesNoTable.setBorder(0);

		row.addCell(new TableDataCell(new SimpleItem("outlet:")))
		   .addCell(new TableDataCell(new SimpleItem(r1)));
		infoTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("kamer:")))
		   .addCell(new TableDataCell(new SimpleItem(r2)));
		infoTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Tel. / IPnr:")))
		   .addCell(new TableDataCell(new SimpleItem(r3)));
		infoTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Ser ADP:")))
		   .addCell(new TableDataCell(new SimpleItem(r4)));
		infoTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("LSA + HWA:")))
		   .addCell(new TableDataCell(new SimpleItem(r5)));
		infoTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("VLAN:")))
		   .addCell(new TableDataCell(new SimpleItem(r6)));
		infoTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("HUB / Stack:")))
		   .addCell(new TableDataCell(new SimpleItem(r7)));
		infoTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Port nr:")))
		   .addCell(new TableDataCell(new SimpleItem(r8)));
		infoTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Segment:")))
		   .addCell(new TableDataCell(new SimpleItem(r9)));
		infoTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Segment naam:")))
		   .addCell(new TableDataCell(new SimpleItem(r10)));
		infoTable.addRow(row);

		row			= new TableRow();
		row.addCell(new TableDataCell(new SimpleItem("Opmerkingen:")))
		   .addCell(new TableDataCell(new SimpleItem(r11)));
		infoTable.addRow(row);

		row = new TableRow();
		row.addCell(new TableDataCell(yesSubmit).setVAlign(2));
		yesTable.addRow(row);
		yesForm.addItem(new Hidden("sessionid", sessionID))
		       .addItem(new Hidden("button", "affirmative"))
		       .addItem(new Hidden("outlet", r1))
		       .addItem(yesTable);

		row = new TableRow();
		row.addCell(new TableDataCell(noSubmit).setVAlign(2));
		noTable.addRow(row);
		noForm.addItem(new Hidden("sessionid", sessionID))
		      .addItem(noTable);

		row = new TableRow();
		row.addCell(new TableDataCell(new SimpleItem(textLine1)).setVAlign(2))
		   .addCell(new TableDataCell(yesForm).setVAlign(2))
		   .addCell(new TableDataCell(noForm).setVAlign(2));
		yesNoTable.addRow(row);

		total.addItem(infoTable)
		     .addItem(SimpleItem.LineBreak)
		     .addItem(yesNoTable);

		return total;
	}


	public Form generateAgainForm(String sessionID)
	{
		BNV_Constants bnvConstantsDO	= new BNV_Constants();
		Form againForm			= new Form("POST", bnvConstantsDO.getDeleteOutletAgainFormAddress());
		Submit againSubmit		= new Submit("SubmitAgain", "Nog een outlet wissen");

		againForm.addItem(againSubmit)
		         .addItem(new Hidden("sessionid", sessionID));

		return againForm;
	}


	public void runServlet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		// Gimme default constants-object
		ServletOutputStream out		= res.getOutputStream();
		BNV_Constants bnvConstantsDO	= new BNV_Constants();
		Date date			= new Date();
		String sessionID		= null;
		String submitType		= null;
		String outlet			= null;
		String temp			= "";

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

		// Standard HTML components / references
		HtmlFile template 		= new HtmlFile(bnvConstantsDO.getHtmlFile_Name());
		CompoundItem report		= new CompoundItem();
		CompoundItem errorMsg		= new CompoundItem();
		CompoundItem confirmItem	= new CompoundItem();

		// HTML constants for BNV_DeleteOutlets
       		String tag1			= "dynamicItem1";
	       	String tag2			= "dynamicItem2";
	        String tag3			= "dynamicItem3";
	        String tag4			= "dynamicItem4";
        	String tag5			= "dynamicItem5";
	        String tag6			= "dynamicItem6";
        	String tag7			= "dynamicItem7";
		String headerText1		= "BNV Online  -  Verwijder een outlet";
	        String dbaseError		= "Database probleem: neem contact op met uw database beheerder";
		String textLine1		= "Type het nummer van het te verwijderen outlet en druk op 'delete'.<BR>Of zoek eerst een bepaalde outlet.";
		String textLine21		= "U staat op het punt om outlet ";
		String textLine22		= " te verwijderen.<BR><BR>Inhoud van de outlet:";
		String textLine31		= "Outlet ";
		String textLine32		= " is verwijderd uit het systeem.";

		// Database objects
		Connection conn;
		Statement stmt;
		ResultSet result;

		// get the URL parameters
		sessionID			= req.getParameter("sessionid");
		submitType			= req.getParameter("button");	// which button was pressed?

		report.addItem(SimpleItem.LineBreak)
		      .addItem(SimpleItem.LineBreak);

		if(submitType == null)	// first time here, coming from the main menu
		{
			report.addItem(new SimpleItem(textLine1))
			      .addItem(SimpleItem.LineBreak)
			      .addItem((Form) generateDeleteForm(sessionID, ""))
			      .addItem((Form) generateZoekForm(sessionID))
			      .addItem(SimpleItem.HorizontalRule)
			      .addItem((Form) generateHoofdmenuForm(sessionID));
		}

		else if(submitType.equals("delete"))	// display "are-you-sure-screen"
		{
			outlet = req.getParameter("field1");
			temp = outlet;
			outlet = new BNV_CheckSyntax().checkOutlet(outlet);
			if( outlet.equals("error") )
			{
				errorMsg.addItem(new SimpleItem("FOUT: outlet '"+temp+"' is ongeldig. Invullen als bijvoorbeeld ZZ00.00 Extra punten, spaties etc zijn niet toegestaan.").setBold())
				        .addItem(SimpleItem.LineBreak);
			}
			else	// lookup the outlet in the database
			{
				try
				{
					Class.forName((String) bnvConstantsDO.getDriver_class());
				}
				catch(ClassNotFoundException e)
				{
					errorMsg.addItem(new SimpleItem(dbaseError))
					        .addItem(new SimpleItem("<BR>Could not find the JDBC driver: "+ bnvConstantsDO.getDriver_class()))
					        .addItem(SimpleItem.LineBreak)
					        .addItem(new SimpleItem("Message: "+ e.getMessage()))
					        .addItem(SimpleItem.LineBreak);
				}
				try
				{
					conn = DriverManager.getConnection(bnvConstantsDO.getConnect_URL(), bnvConstantsDO.getConnect_user(), bnvConstantsDO.getConnect_password() );
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
						else	// fetch the outlet's data
						{
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
				      .addItem(SimpleItem.HorizontalRule)
				      .addItem((Form) generateDeleteForm(sessionID, temp))
				      .addItem((Form) generateZoekForm(sessionID));
			}
			else
			{
				// print the are-u-sure thing here and the inhoud-table
				report.addItem(new SimpleItem(textLine21 + outlet + textLine22))
				      .addItem((CompoundItem) generateConfirmation(sessionID, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11));

			}

			report.addItem(SimpleItem.HorizontalRule)
			      .addItem((Form) generateHoofdmenuForm(sessionID));

		}

		else if(submitType.equals("affirmative"))	// ok, now make oracle delete the record
		{
			outlet = req.getParameter("outlet");
			temp = outlet;
			outlet = new BNV_CheckSyntax().checkOutlet(outlet);
			if( outlet.equals("error") )
			{
				errorMsg.addItem(new SimpleItem("FOUT: outlet '"+temp+"' is ongeldig. Invullen als bijvoorbeeld ZZ00.00 Extra punten, spaties etc zijn niet toegestaan.").setBold())
				        .addItem(SimpleItem.LineBreak);
			}
			else	// lookup the outlet in the database
			{
				try
				{
					Class.forName((String) bnvConstantsDO.getDriver_class());
				}
				catch(ClassNotFoundException e)
				{
					errorMsg.addItem(new SimpleItem(dbaseError))
					        .addItem(new SimpleItem("<BR>Could not find the JDBC driver: "+ bnvConstantsDO.getDriver_class()))
					        .addItem(SimpleItem.LineBreak)
					        .addItem(new SimpleItem("Message: "+ e.getMessage()))
					        .addItem(SimpleItem.LineBreak);
				}
				try
				{
					conn = DriverManager.getConnection(bnvConstantsDO.getConnect_URL(), bnvConstantsDO.getConnect_user(), bnvConstantsDO.getConnect_password() );
					try
					{
						// First, check how many times 'outlet' is registered in the database: should be only one time!
						stmt = conn.createStatement();
						result = stmt.executeQuery("SELECT COUNT(*) FROM BNV_OUTLET WHERE BNV_OUTLET = '" + outlet + "'");
						result.next();
						if( (int) result.getInt(1) < 1)		// No hits or error!
						{
							errorMsg.addItem(new SimpleItem("Outlet " + outlet + " niet gevonden. Waarschijnlijk is iemand U voor geweest met verwijderen.").setBold())
							        .addItem(SimpleItem.LineBreak)
							        .addItem(SimpleItem.LineBreak);
						}
						else if( (int) result.getInt(1) > 1)		// more than 1 hit!
						{
							errorMsg.addItem(new SimpleItem("FOUT: outlet " + outlet + " komt " + (String) String.valueOf((int) result.getInt(1)) + "x voor in de database!").setBold())
							        .addItem(SimpleItem.LineBreak)
							        .addItem(SimpleItem.LineBreak);
						}
						else	// destroy the outlet
						{
							stmt.executeUpdate("DELETE FROM BNV_OUTLET WHERE BNV_OUTLET = '"+ outlet +"'");
							stmt.executeUpdate("COMMIT");
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
				// outlet removed
				report.addItem(new SimpleItem(textLine31 + outlet + textLine32))
				      .addItem((Form) generateAgainForm(sessionID));
			}

			report.addItem(SimpleItem.HorizontalRule)
			      .addItem((Form) generateHoofdmenuForm(sessionID));
		}


    		// Load dynamic content into Standaard.html
		template.setItemAt(tag1, ((new SimpleItem(""))))
       	    	        .setItemAt(tag2, ((new SimpleItem(headerText1))).setBold())
      		        .setItemAt(tag3, (report))
       		        .setItemAt(tag4, (new SimpleItem(bnvConstantsDO.getCopyRight_Page())))
      		        .setItemAt(tag5, (new SimpleItem(date.toString())))
      		        .setItemAt(tag6, (new SimpleItem(bnvConstantsDO.getPageAdministrator_Name())))
      		        .setItemAt(tag7, (new SimpleItem(bnvConstantsDO.getwebMaster_Name())));

    		template.print(out);
	}


} // end of class
