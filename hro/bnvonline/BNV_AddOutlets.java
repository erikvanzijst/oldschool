// BNV Online - Datawarehouse applicatie
// Erik van Zijst, 01.12.98 icehawk@xs4all.nl

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


// TU Delft BNV project - BNV_AddOutlets pagina
// This page allows you to add new outlets to the database. Up to 10.
// This page is called from the main Menu. Only accessible to admin-users

public class BNV_AddOutlets extends HttpServlet
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
		if(validateSession(req, res)) return;

		runServlet(req, res);

	} // That's all


	public boolean validateSession(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		BNV_Constants bnvConstantsAO	= new BNV_Constants();
		String sessionID		= null;
		sessionID			= req.getParameter("sessionid");
		if(sessionID == null)
		{
			new BNV_ErrorPage().cancelSession(req, res);
			return true;
		}

		// Now validate the session and equivalences and stamp it
		String sessionStatus	= new BNV_SessionStamp().checkSession(sessionID, bnvConstantsAO.getBNV_AddOutletsEqs());
		if( sessionStatus.equals("alreadyKilled") || sessionStatus.equals("overTimePerPageLimit") || sessionStatus.equals("error") || sessionStatus.equals("error1") || sessionStatus.equals("error2") || sessionStatus.equals("arrayError") || sessionStatus.equals("wrongEquivalences") )
		{
			new BNV_SessionDead().killSession(sessionID, sessionStatus, req, res);
			return true;
		}
		else
		{	return false;
		}
	}


	public void runServlet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		// Gimme default constants-object
		ServletOutputStream out		= res.getOutputStream();
		BNV_Constants bnvConstantsAO	= new BNV_Constants();
		Date date			= new Date();
		String sessionID		= null;
		String useridString		= null;
		sessionID			= req.getParameter("sessionid");
		boolean stop			= false;
		int floor			= 0;
		String temp			= "";

		// Standard HTML components / references
		HtmlFile template 	= new HtmlFile(bnvConstantsAO.getHtmlFile_Name());
		CompoundItem report	= new CompoundItem();
		CompoundItem errorMsg	= new CompoundItem();
		Form form		= new Form("POST", bnvConstantsAO.getAddOutletsFormAddress());	// Save button
		Form form2		= new Form("POST", bnvConstantsAO.getAddOutletsForm2Address());	// Logout button
		DynamicTable table1	= new DynamicTable(3);
		DynamicTable table2	= new DynamicTable(2);
		TableRow row;
		Submit submit		= new Submit("Submit", "Voeg toe");
		Submit logout		= new Submit("Submit", "Annuleren");
		Select pullDown		= new Select("col2");

	        // Standard External references to standard static HTML document
		String col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12 = null;
       		String tag1 = "dynamicItem1";
	       	String tag2 = "dynamicItem2";
	        String tag3 = "dynamicItem3";
	        String tag4 = "dynamicItem4";
        	String tag5 = "dynamicItem5";
	        String tag6 = "dynamicItem6";
        	String tag7 = "dynamicItem7";

		col1	= req.getParameter("col1");
		col2	= req.getParameter("col2");
		col3	= req.getParameter("col3");
		col4	= req.getParameter("col4");
		col5	= req.getParameter("col5");
		col6	= req.getParameter("col6");
		col7	= req.getParameter("col7");
		col8	= req.getParameter("col8");
		col9	= req.getParameter("col9");
		col10	= req.getParameter("col10");
		col11	= req.getParameter("col11");
		col12	= req.getParameter("col12");

		pullDown.addOption(new Option("K"))
		        .addOption(new Option("B"))
		        .addOption(new Option("1"))
		        .addOption(new Option("2"))
		        .addOption(new Option("3"))
		        .addOption(new Option("4"))
		        .addOption(new Option("5"))
		        .addOption(new Option("6"));

	        // HTML constants for BNV_AddOutlets
        	String headerText1      = "BNV Online  -  Een outlet toevoegen";
	        String TextLine1	= "Voer de gegevens voor de nieuwe outlet in en druk op 'Opslaan'. Druk op 'Annuleren' om af te breken zonder op te slaan.<BR>N.B. De rode velden moeten verplicht ingevuld worden.";
	        String TextLine2	= "Druk op 'Annuleren' om terug te gaan naar het Hoofdmenu zonder de wijzigingen op te slaan.";
		String TextLine3	= "Outlet toegevoegd!";
		String errorText1	= "FOUT1: Niet alle verplichte velden zijn (juist) ingevuld. Vul tenminste alle rood gemarkeerde velden in.";
		String errorText2	= "FOUT2: Uw loginnaam kon niet achterhaald worden. (Dit is noodzakelijk voor in de log files.)";
		String errorText3	= "De nieuwe outlet is NIET toegevoegd aan de database.";
	        String dbaseError       = "Database probleem: neem contact op met uw database beheerder";

		// Database objects
		Connection conn;
		Statement stmt;
		ResultSet result;

		// Check for the parameters
		if ( (col1 == null) || (col3 == null) || (col4 == null) || (col5 == null) || (col6 == null) || (col7 == null) || (col8 == null) || (col9 == null) || (col10 == null) || (col11 == null) || (col12 == null) )
		{
			// User just entered this page, give a blank form

			table1.setBorder(0).setCellPadding(0);

			report.addItem(SimpleItem.LineBreak)
			      .addItem(SimpleItem.LineBreak);

			form.addItem(new SimpleItem(TextLine1))
			    .addItem(SimpleItem.LineBreak)
			    .addItem(SimpleItem.LineBreak);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Outlet:").setBold().setFontColor(Color.red)) )
			   .addCell(new TableDataCell(new TextField("col1",15,12, "")) )
			   .addCell(new TableDataCell(new SimpleItem("Voorbeeld: LD17.17")).setHAlign(1) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Kamer nr:").setBold().setFontColor(Color.red)) )
			   .addCell(new TableDataCell(new TextField("col3",20,12, "")) )
			   .addCell(new TableDataCell(new SimpleItem("Voorbeeld: 3.06.1 (Nummeriek)")).setHAlign(1) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Tel. / IPnr:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col4",20,12, "")) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Ser ADP:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col5",10,12, "0")) )
			   .addCell(new TableDataCell(new SimpleItem("(Nummeriek)")).setHAlign(1) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("LSA + HWA:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col6",10,12, "0")) )
			   .addCell(new TableDataCell(new SimpleItem("(Nummeriek)")).setHAlign(1) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("VLAN:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col7",30,12, "")) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("HUB / Stack:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col8",15,12, "")) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Port nr:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col9",10,12, "")) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Segment:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col10",15,12, "")) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Segment naam:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col11",30,12, "")) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Opmerkingen:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col12",255,12, "")) );
			table1.addRow(row);


			row = new TableRow();
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("")))
			   .addCell(new TableDataCell(submit).setWidth(120).setHAlign(2) );
			table1.addRow(row);

			form.addItem(new Hidden("sessionid", sessionID));
			form.addItem(table1);
			report.addItem(form)
			      .addItem(SimpleItem.LineBreak)
			      .addItem(SimpleItem.HorizontalRule)
			      .addItem(SimpleItem.LineBreak);

			form2.addItem(new Hidden("sessionid", sessionID));

			table2.setBorder(0).setCellPadding(0);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem(TextLine2)) )
			   .addCell(new TableDataCell(logout).setWidth(120).setHAlign(3));
			table2.addRow(row);
			form2.addItem(table2);
			report.addItem(form2);
		}
		else	// User has submitted a new outlet
		{
			// Process the database..
			col1 = col1.toUpperCase().trim();
			col3 = col3.trim();
			if (col4.length() > 0) col4 = col4.trim();
			if (col5.length() > 0) col5 = col5.trim(); else col5 = "0";
			if (col6.length() > 0) col6 = col6.trim(); else col6 = "0";
			if (col7.length() > 0) col7 = col7.trim();
			if (col8.length() > 0) col8 = col8.trim();
			if (col9.length() > 0) col9 = col9.trim();

			// First check for the NOT NULL fields...
			if ((col1.equals("")) || (col1 == null)) stop = true;
//			if ((col2.equals("")) || (col2 == null)) stop = true;
			if ((col3.equals("")) || (col3 == null)) stop = true;
			if(stop) errorMsg.addItem(new SimpleItem(errorText1));

			
			temp = col1;
			col1 = new BNV_CheckSyntax().checkOutlet(col1);
			if( col1.equals("error") )
			{
				errorMsg.addItem(new SimpleItem("Het outletnummer '"+temp+"' is ongeldig!"))
				        .addItem(SimpleItem.LineBreak);
			}
			else if( (new BNV_FloorExtractor().findFloor(col3)) == -2)
				errorMsg.addItem(new SimpleItem("Het kamernummer is ongeldig!"));
			else
				col2 = (String) String.valueOf((int) new BNV_FloorExtractor().findFloor(col3));

			if(errorMsg.size() < 1)
			{	// OK, continue

				// Open the database...
				try
				{
					Class.forName((String) bnvConstantsAO.getDriver_class());
				}
				catch(ClassNotFoundException e)
				{
					errorMsg.addItem(new SimpleItem(dbaseError))
					        .addItem(new SimpleItem("<BR>Could not find the JDBC driver: "+ bnvConstantsAO.getDriver_class()))
					        .addItem(SimpleItem.LineBreak)
					        .addItem(new SimpleItem("Message: "+ e.getMessage()))
					        .addItem(SimpleItem.LineBreak);
				}
				try
				{
					conn = DriverManager.getConnection(bnvConstantsAO.getConnect_URL(), bnvConstantsAO.getConnect_user(), bnvConstantsAO.getConnect_password() );
					try
					{
						stmt = conn.createStatement();
						// ok done, now first retrieve the current userid (for logging)
						result = stmt.executeQuery("SELECT USERID FROM BNV_SESSION WHERE SESSIONID = '"+sessionID+"'");
						result.next();
						try // Check if the userID can be found
						{	useridString = result.getString(1);
						}
						catch(SQLException e)
						{
							errorMsg.addItem(new SimpleItem(errorText2).setBold())
							        .addItem(SimpleItem.LineBreak);
						}
						if(errorMsg.size() == 0)	// if all went right.. add the new outlet. But first check if the outlet doesn't already exist!
						{
							result = stmt.executeQuery("SELECT * FROM BNV_OUTLET WHERE BNV_OUTLET = '"+col1+"'");
							if(result.next()) // Already exists!
							{
								errorMsg.addItem(new SimpleItem("FOUT3: Outlet "+col1+" bestaat al!").setBold());
							}
							else
							{ // finally insert the damn thing..
								stmt.executeUpdate("INSERT INTO BNV_OUTLET VALUES ('"+col1+"', '"+col3+"', "+col2+", '"+col4+"', "+col5+", "+col6+", '"+col7+"', '"+col8+"', '"+col9+"', '"+col10+"', '"+col11+"', '', '', SYSDATE, '"+useridString+"', '"+col12+"')");
								stmt.executeUpdate("COMMIT");
							}
						}
						stmt.close();
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
					errorMsg.addItem(new SimpleItem("FOUT4: "+ dbaseError))
					        .addItem(SimpleItem.LineBreak)
					        .addItem(new SimpleItem("Message: "+ e.getMessage()) );
				}



			}
			// Now construct the after-add-page...

			report.addItem(SimpleItem.LineBreak)
			      .addItem(SimpleItem.LineBreak);
			if(errorMsg.size() > 0) // Error, outlet not added!
			{
				report.addItem(new SimpleItem(errorText3).setBold())
				      .addItem(SimpleItem.LineBreak)
				      .addItem(new SimpleItem(errorMsg).setBold());
			}
			else // Outlet was added, now add another one
			{
				report.addItem(new SimpleItem("De nieuwe outlet is toegevoegd aan het systeem.<BR>Voer de volgende nieuwe outlet toe of kies 'Annuleren'."))
				      .addItem(SimpleItem.LineBreak);
			}

			// Generate the forms...

			table1.setBorder(0).setCellPadding(0);

			report.addItem(SimpleItem.LineBreak)
			      .addItem(SimpleItem.LineBreak);

			form.addItem(new SimpleItem(TextLine1))
			    .addItem(SimpleItem.LineBreak)
			    .addItem(SimpleItem.LineBreak);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Outlet:").setBold().setFontColor(Color.red)) )
			   .addCell(new TableDataCell(new TextField("col1",15,12, temp)) )
			   .addCell(new TableDataCell(new SimpleItem("Voorbeeld: LD17.17")).setHAlign(1) );
			table1.addRow(row);

/*
			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Verdieping:").setBold().setFontColor(Color.red)) )
			   .addCell(new TableDataCell(pullDown).setHAlign(3) );
			table1.addRow(row);
*/

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Kamer nr:").setBold().setFontColor(Color.red)) )
			   .addCell(new TableDataCell(new TextField("col3",20,12, col3)) )
			   .addCell(new TableDataCell(new SimpleItem("Voorbeeld: 3.06.1 (Nummeriek)")).setHAlign(1) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Tel. / IPnr:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col4",20,12, col4)) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Ser ADP:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col5",10,12, col5)) )
			   .addCell(new TableDataCell(new SimpleItem("(Nummeriek)")).setHAlign(1) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("LSA + HWA:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col6",10,12, col6)) )
			   .addCell(new TableDataCell(new SimpleItem("(Nummeriek)")).setHAlign(1) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("VLAN:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col7",30,12, col7)) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("HUB / Stack:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col8",15,12, col8)) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Port nr:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col9",10,12, col9)) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Segment:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col10",15,12, col10)) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Segment naam:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col11",30,12, col11)) );
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("Opmerkingen:").setBold()) )
			   .addCell(new TableDataCell(new TextField("col12",255,12, col12)) );
			table1.addRow(row);


			row = new TableRow();
			table1.addRow(row);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem("")))
			   .addCell(new TableDataCell(submit).setWidth(120).setHAlign(2) );
			table1.addRow(row);

			form.addItem(new Hidden("sessionid", sessionID));
			form.addItem(table1);
			report.addItem(form)
			      .addItem(SimpleItem.LineBreak)
			      .addItem(SimpleItem.HorizontalRule)
			      .addItem(SimpleItem.LineBreak);

			form2.addItem(new Hidden("sessionid", sessionID));

			table2.setBorder(0).setCellPadding(0);

			row = new TableRow();
			row.addCell(new TableDataCell(new SimpleItem(TextLine2)) )
			   .addCell(new TableDataCell(logout).setWidth(120).setHAlign(3));
			table2.addRow(row);
			form2.addItem(table2);
			report.addItem(form2);

		}



    		// Load dynamic content into Standaard.html
		template.setItemAt(tag1, ((new SimpleItem(""))))
       	    	        .setItemAt(tag2, ((new SimpleItem(headerText1))).setBold())
      		        .setItemAt(tag3, (report))
       		        .setItemAt(tag4, (new SimpleItem(bnvConstantsAO.getCopyRight_Page())))
      		        .setItemAt(tag5, (new SimpleItem(date.toString())))
      		        .setItemAt(tag6, (new SimpleItem(bnvConstantsAO.getPageAdministrator_Name())))
      		        .setItemAt(tag7, (new SimpleItem(bnvConstantsAO.getwebMaster_Name())));

    		template.print(out);
	}
}
