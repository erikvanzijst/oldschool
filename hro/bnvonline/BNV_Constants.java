//package bnv_online;

// TU Delft DNV project -  BNV_Constants service class
// This class is a central repository for all common or changeable constants in the BNV_.. group of classes
// Ripped van Anthony.

class BNV_Constants {

    // NB! The database connect strings are different for NT and Solaris.
    //     NT uses the string: connect_String retrieved by  the method getConnect_String
    //     Solaris uses 3 strings: connect_URL, connect_user & connectpassword  retrieved by 3 separate methods
    //     so if servers are switched you have to change this method call in all the classes too.


    /*    CONSTANTS FOR DUTCCIB - SOLARIS SERVER      */
/*
    // HTML page constants
    private String htmlFile_Name          = "/home/oracle/app/oracle/product/7.3.2/ows/3.0/lib/Standaard.html";
    private String copyRight_Page         = "<a href=/copyright.html>&copy;  </a>";
    private String webMaster_Name         = "achater@icgroup.nl";
    private String pageAdministrator_Name = "Data Warehouse Manager";


    // Class Form Addresses

    // CT_KiesCijferOVerzicht
    private String kiesCijferOverzichtFormAddress   = "http://sysia75.citg.tudelft.nl/java/CT_CijferOverzicht";
    // CT_LoginUser
    private String loginUserFormAddress             = "http://sysia75.citg.tudelft.nl/java/CT_LoginUser";
    // CT_SessionIndex
    private String sessionIndexFormAddress          = "http://sysia75.citg.tudelft.nl/java/CT_SessionLogout";
    // CT_ToonVak
    private String toonVakFormAddress               = "http://sysia75.citg.tudelft.nl/java/CT_ToonTentamen";
    // CT_ToonVakInfo
    private String toonVakInfoFormAddress           = "http://sysia75.citg.tudelft.nl/java/CT_ToonVakInfo";
    // CT_ZoekStudent
    private String zoekStudentFormAddress           = "http://sysia75.citg.tudelft.nl/java/CT_ZoekStudent";
    // CT_ZoekVak
    private String zoekVakFormAddress               = "http://sysia75.citg.tudelft.nl/java/CT_ZoekVak";
    private String zoekVakVakFormAddress            = "http://sysia75.citg.tudelft.nl/java/CT_ToonVak";

    // Standard database constants
    private String driver_class     = "oracle.jdbc.OracleDriver";
    private String connect_URL      = "jdbc:oracle:oci7";
    private String connect_user     = "www_dba";  // www_user
    private String connect_password = "chaos.";


    // Security timeout constants.....NB. Change all asscociated ..Msg_String strings when the timeouts are altered

    // Time allowance for which each page is stamped: the numerator of the fraction is minutes; so 10/1440 would be 10 minutes
    private String timePerPageSQL_String    = "SYSDATE - (20/1440)";
    private String timePerPageMsg_String    = "u heeft langer dan 20 minuten op deze pagina gezeten";  //overTimePerPageLimit
    private String timePerPageMsg_String1   = "Let op: Om veiligheidsredenen wordt uw sessie automatisch afgebroken wanneer er meer dan 20 minuten rusttijd tussen twee opeenvolgende interacties zit";  //overTimePerPageLimit msg on welcome screen
    // Total Inactivity Time allowance for a session: The classes StartSession,SessionStamp, and SessionDead all have a cleanup
    // routine which deletes all records from ct_session which are older than this timeperiod; format as above; so so 60/1440 would be 1 hour
    private String cleanupTimeoutSQL_String = "SYSDATE - (30/1440)";
    private String cleanupTimeoutMsg_String = "u bent al uitgelogd of u bent langer dan 30 minuten zonder activiteit ingelogd ";


    // Page Equivalence Constants: these fields determine the rights required by a user in order to open each CT_ page...
    // There is currently a maximum of 10 equivalences allowed; NB all fields MUST teminate with a ':'
    // Note: the eqivalencies for CT_SessionIndex include all possibilities(have to!!) but a further check is carried out within this class
    private String ct_CijferOverzichtEqs        = "admin:student:";
    private String ct_DisplayMetaEqs            = "admin:student:";
    private String ct_KiesCijferOverzichtEqs    = "admin:student:";
    private String ct_SessionIndexEqs           = "admin:student:public";
    private String ct_ToonTentamenEqs           = "admin:student:";
    private String ct_ToonVakEqs                = "admin:student:";
    private String ct_ToonVakInfoEqs            = "admin:student:";
    private String ct_ZoekStudentEqs            = "admin:student:public:";
    private String ct_ZoekVakEqs                = "admin:student:";

    // Encryption keyword  : this is used to encrypt/decrypt passwords
    //    Note: Important!! if this is changed it must be changed in CT_AdminConstants
    private String theWord = "network";


    // Welome page <META> refresh tag... The timeout is the figure after CONTENT (in seconds) after which the Index page will automatically start
    //           NB.   When timeout is changed change welcomeRefreshTimeMsg1 too
    private String welcomeRefreshTag1       = "<META HTTP-EQUIV=\"Refresh\" CONTENT =\"10" + ';' + "URL=http://sysia75.citg.tudelft.nl/java/CT_SessionIndex?sessionid=";
    private String welcomeRefreshTimeMsg1   = "Na 10 seconden wordt u automatisch doorgestuurd naar de hoofdmenu";


    // Index (or menu) URL links
    private String IndexURL1 = "<a href=http://sysia75.citg.tudelft.nl/java/CT_SessionIndex?sessionid=";
    private String IndexURL2 = "&eqsid=";
    private String IndexURL3 = "> <img src=/image/hoofd_menu.gif border=0>Terug naar hoofdmenu</a>";
    private String IndexURL4 = "> <img src=/image/hoofd_menu.gif border=0>Hoofdmenu</a>";


    // Metadata links
    private String metaURL_ct_cijfer1   = "<a href=http://sysia75.citg.tudelft.nl/java/CT_DisplayMeta?tabelnaam=ct_cijfer&sessionid=";
    private String metaURL_ct_cijfer2   = "> <img src=/image/meta.gif border=0>Cijfertabel</a>";
    private String metaURL_ct_meta1     = "<a href=/java/CT_DisplayMeta?tabelnaam=ct_meta&sessionid=";
    private String metaURL_ct_meta2     = "> <img src=/image/meta.gif border=0>Metatabel</a>";
    private String metaURL_ct_student1  = "<a href=/java/CT_DisplayMeta?tabelnaam=ct_student&sessionid=";
    private String metaURL_ct_student2  = "> <img src=/image/meta.gif border=0>Studententabel</a>";
    private String metaURL_ct_tentamen1 = "<a href=/java/CT_DisplayMeta?tabelnaam=ct_tentamen&sessionid=";
    private String metaURL_ct_tentamen2 = "> <img src=/image/meta.gif border=0>Tentamentabel</a>";
    private String metaURL_ct_vak1      = "<a href=/java/CT_DisplayMeta?tabelnaam=ct_vak&sessionid=";
    private String metaURL_ct_vak2      = "> <img src=/image/meta.gif border=0>Vaktabel</a>";
    private String metaURL_ct_vakinfo1  = "<a href=/java/CT_DisplayMeta?tabelnaam=ct_vakinfo&sessionid=";
    private String metaURL_ct_vakinfo2  = "> <img src=/image/meta.gif border=0>Vakinformatietabel</a>";


    // HoofdMenu page links
    private String zoekStudentMenuLink1     = "<a href=/java/CT_ZoekStudent?sessionid=";
    private String zoekStudentMenuLink2     = "> Zoek een mede ct-student</a>";
    private String zoekVakMenuLink1         = "<a href=/java/CT_ZoekVak?sessionid=";
    private String zoekVakMenuLink2         = "> Zoek vak- en tentameninformatie</a>";
    private String kiesCijferOZichtLink1    = "<a href=/java/CT_KiesCijferOverzicht?sessionid=";
    private String kiesCijferOZichtLink2    = "> Kies een cijferoverzicht</a>";
    private String ct_AdminIndexLink1       = "<a href=/java/CT_AdminIndex?sessionid=";
    private String ct_AdminIndexLink2       = "> Beheermenu</a>";

    // Link to Login Page
    private String loginURL1    = "<a href=/java/CT_LoginUser> <img src=/image/login.gif border=0>Terug naar loginscherm</a>";

    // Links to pages in the CT_Admin.. group
    private String ct_AdminIndexURL1    = "<a href=/java/ct_datawarehouse/CT_AdminIndex?sessionid=";
    private String ct_AdminIndexURL2    = "> <img src=/image/beheer_menu.gif border=0>Terug naar beheermenu</a>";
    private String ct_AdminCijfersURL1  = "<a href=/java/ct_datawarehouse/CT_AdminCijfers?sessionid=";
    private String ct_AdminCijfersURL2  = "> <img src=/image/cijfers.gif border=0>Terug naar Studentencijfers</a>";


    // Other crosslinks located on pages
    String toonVakURLexToonT1       = "<a href=http://sysia75.citg.tudelft.nl/java/CT_ToonVak?vak=";
    String toonVakURLexToonT2       = "> <img src=/image/vak_info.gif border=0>Terug naar vakinformatie</a>";
    String toonVakURLexCijferO      = "<a href=http://sysia75.citg.tudelft.nl/java/CT_ToonVak?vak=";
    String toonTentamenURLexCijferO = "<a href=http://sysia75.citg.tudelft.nl/java/CT_ToonTentamen?date=";


      // end of  CONSTANTS FOR SYSIA75 - SOLARIS SERVER
*/

    // METHODS
    // HTML page constant Methods

    public String getHtmlFile_Name() {
         return htmlFile_Name;
    }

    public String getCopyRight_Page() {
         return copyRight_Page;
    }

    public String getPageAdministrator_Name() {
         return pageAdministrator_Name;
    }

     public String getLoginUserFormAddress() {
         return loginUserFormAddress;
    }

    public String getwebMaster_Name() {
         return webMaster_Name;
    }


    // Standard database constant Methods

    public String getDriver_class() {
         return driver_class;
    }

    /*  This is the NT connect string method
    public String getConnect_String() {
         return connect_String;
    }*/

    public String getConnect_URL() {
         return connect_URL;
    }

    public String getConnect_user() {
         return connect_user;
    }

    public String getConnect_password() {
         return connect_password;
    }

    public  String getCleanupTimeoutSQL_String() {
	    return cleanupTimeoutSQL_String;
    }

    public  String getCleanupTimeoutMsg_String() {
	    return cleanupTimeoutMsg_String;
    }

    public  String getTimePerPageMsg_String() {
	    return timePerPageMsg_String;
    }

    public  String getTimePerPageMsg_String1() {
	    return timePerPageMsg_String1;
    }

    public  String getTimePerPageSQL_String() {
	    return timePerPageSQL_String;
    }



    public  String getLoginURL1() {
	    return loginURL1;
    }


    // Page Equivalence methods

    public  String getBNV_SessionIndexEqs() {
	    return bnv_SessionIndexEqs;
    }

    public String getBNV_AddOutletsEqs()
    {
	return bnv_AddOutletsEqs;
    }

    public String getBNV_ListOutletsEqs()
    {
	return bnv_ListOutletsEqs;
    }

    public String getBNV_EditOutletsEqs()
    {
	return bnv_EditOutletsEqs;
    }


    public String getBNV_DeleteOutletsEqs()
    {
	return bnv_DeleteOutletsEqs;
    }

    public String getBNV_ZoekOutletsEqs()
    {
	return bnv_ZoekOutletsEqs;
    }





    // Welome page <META> refresh tag Methods
    public  String getMetaRefreshTag(String sessionID) {
        String metaRefresh = welcomeRefreshTag1 + sessionID + "\">";
	    return metaRefresh;
    }

    public  String getMetaRefreshTimeMsg1() {
        return welcomeRefreshTimeMsg1;
    }


    // Index URL link Methods

    public  String getIndexURL1(String sessionID) {
        // This method returns the URL for the Welcome page...
        String IURL1 = IndexURL1 + sessionID + IndexURL4;
	    return IURL1;
    }

    public  String getIndexURL2(String sessionID) {
        // This method returns the URL which links back to the Hoofdmenu...
        String IURL2 = IndexURL1 + sessionID + IndexURL3;
	    return IURL2;
    }


    	// Hoofdmenu link methods

    	public  String getListOutletsLink(String sessionID)
	{
        	String LOMenuLink = listOutletsMenuLink1 + sessionID + listOutletsMenuLink2;
	    	return LOMenuLink;
    	}

	public String getZoekOutletsLink(String sessionID)
	{
		String ZOMenuLink = zoekOutletsMenuLink1 + sessionID + zoekOutletsMenuLink2;
		return ZOMenuLink;
	}

	public String getAdd10OutletsLink(String sessionID)
	{
		String AOMenuLink = add10OutletsMenuLink1 + sessionID + add10OutletsMenuLink2;
		return AOMenuLink;
	}

	public String getEditOutletLink(String sessionID)
	{
		String EOMenuLink = editOutletMenuLink1 + sessionID + editOutletMenuLink2;
		return EOMenuLink;
	}

	public String getDeleteOutletLink(String sessionID)
	{
		String DOMenuLink = deleteOutletMenuLink1 + sessionID + deleteOutletMenuLink2;
		return DOMenuLink;
	}


    	// Class Form Address Methods
    	public String getSessionIndexFormAddress()
	{	return sessionIndexFormAddress;
    	}

    	public String getAddOutletsFormAddress()
	{	return addOutletsFormAddress;
    	}

    	public String getAddOutletsForm2Address()
	{
		return addOutletsForm2Address;
    	}

    	public String getListOutletsMenuFormAddress()
	{	return listOutletsMenuFormAddress;
    	}

    	public String getListOutletsLogoutFormAddress()
	{	return listOutletsLogoutFormAddress;
    	}

    	public String getListOutletsNextFormAddress()
	{	return listOutletsNextFormAddress;
    	}

    	public String getEditOutletEditFormAddress()
	{	return editOutletEditFormAddress;
    	}

    	public String getEditOutletSearchFormAddress()
	{	return editOutletSearchFormAddress;
    	}

    	public String getEditOutletHoofdmenuFormAddress()
	{	return editOutletHoofdmenuFormAddress;
    	}

    	public String getEditOutletsEditFormAddress()
	{	return editOutletsEditFormAddress;
    	}

    	public String getEditOutletsCancelFormAddress()
	{	return editOutletsCancelFormAddress;
    	}


    	public String getEditOutletOopsFormAddress()
	{	return editOutletsOopsFormAddress;
    	}


    	public String getEditOutletReEditFormAddress()
	{	return editOutletReEditFormAddress;
    	}


    	public String getDeleteOutletDeleteFormAddress()
	{	return deleteOutletDeleteFormAddress;
    	}


    	public String getDeleteOutletZoekFormAddress()
	{	return deleteOutletZoekFormAddress;
    	}


    	public String getDeleteOutletHoofdmenuFormAddress()
	{	return deleteOutletHoofdmenuFormAddress;
    	}


    	public String getDeleteOutletYesNoFormAddress()
	{	return deleteOutletYesNoFormAddress;
    	}


    	public String getDeleteOutletAgainFormAddress()
	{	return deleteOutletAgainFormAddress;
    	}


    	public String getZoekOutletsHoofdmenuFormAddress()
	{	return zoekOutletsHoofdmenuFormAddress;
    	}


    	public String getZoekOutletsZoekFormAddress()
	{	return zoekOutletsZoekFormAddress;
    	}





    // Page Equivalence Constants: these fields determine the rights required by a user in order to open each BNV_ page...
    // There is currently a maximum of 10 equivalences allowed; NB all fields MUST teminate with a ':'
    // Note: the eqivalencies for BNV_SessionIndex include all possibilities(have to!!) but a further check is carried out within this class
    private String bnv_SessionIndexEqs           = "admin:guest:";
    private String bnv_AddOutletsEqs		 = "admin:";
    private String bnv_ListOutletsEqs		 = "admin:guest:";
    private String bnv_EditOutletsEqs		 = "admin:";
    private String bnv_DeleteOutletsEqs		 = "admin:";
    private String bnv_ZoekOutletsEqs		 = "admin:guest:";


    /*     CONSTANTS FOR SYSIA76 - NT SERVER  */

    // HTML page constants
    private String htmlFile_Name     = "C:\\erik\\bnvonline\\standaard.html";
    private String webMaster_Name    = "icehawk@xs4all.nl";
    private String pageAdministrator_Name = "Erik van Zijst";
    private String copyRight_Page         = "<a href=http://sysia76.citg.tudelft.nl:81/copyright.html>&copy;  </a>";

    // Class Form Addresses
    // LoginUser
    private String loginUserFormAddress         	= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_LoginUser";
    // SessionIndex - logout
    private String sessionIndexFormAddress      	= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_SessionLogout";
    private String addOutletsFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_AddOutlets";
    private String addOutletsForm2Address		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_SessionIndex";
    private String listOutletsMenuFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_ListOutlets";
    private String listOutletsLogoutFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_SessionIndex";
    private String listOutletsNextFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_ListOutlets";
    private String editOutletEditFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_EditOutlet";
    private String editOutletSearchFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_SearchOutlets";
    private String editOutletHoofdmenuFormAddress	= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_SessionIndex";
    private String editOutletsEditFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_EditOutlet";
    private String editOutletsCancelFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_EditOutlet";
    private String editOutletsOopsFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_EditOutlet";
    private String editOutletReEditFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_EditOutlet";
    private String deleteOutletDeleteFormAddress	= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_DeleteOutlet";
    private String deleteOutletZoekFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_SearchOutlets";
    private String deleteOutletHoofdmenuFormAddress	= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_SessionIndex";
    private String deleteOutletYesNoFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_DeleteOutlet";
    private String deleteOutletAgainFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_DeleteOutlet";
    private String zoekOutletsHoofdmenuFormAddress	= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_SessionIndex";
    private String zoekOutletsZoekFormAddress		= "http://sysia76.citg.tudelft.nl:8000/servlet/BNV_ZoekOutlets";




    // Standard database constants
//    private String driver_class      = "oracle.jdbc.driver.OracleDriver"; //(Solaris)
//    private String driver_class      = "sun.jdbc.odbc.JdbcOdbcDriver"; //(NT-OraLite)
    private String driver_class		= "oracle.pol.poljdbc.POLJDBCDriver";
    private String connect_String    = "jdbc:oracle:oci7:www_dba/chaos.@sysia76";
//    private String connect_URL      = "jdbc:oracle:oci7";	//(Solaris)
//    private String connect_URL      = "jdbc:odbc:POLite";	//(NT-OraLite)
    private String connect_URL      = "jdbc:polite:POLITE";	//(NT-OraLite)
//    private String connect_user     = "www_dba";  // www_user Solaris
    private String connect_user     = "SYSTEM";  // www_user NT
//    private String connect_password = "chaos.";	// Solaris
    private String connect_password = "*";	// NT

    // Security timeout constants

    // Time allowance for which each page is stamped: the numerator of the fraction is minutes; so 10/1440 would be 10 minutes
    //private String timePerPageSQL_String            = "SYSDATE - (1/1440)";
    private String timePerPageSQL_String            = "((SYSDATE + 0) - 1.006944)";	// ( 10/1440 = 10 min )
    private String timePerPageMsg_String            = "U hebt langer dan 10 minuten op deze pagina gezeten";  //overTimePerPageLimit";
    private String timePerPageMsg_String1	    = "Let op: Om veiligheidsredenen wordt uw sessie automatisch afgebroken wanneer er meer dan 20 minuten rusttijd tussen twee opeenvolgende interacties zit";  //overTimePerPageLimit msg on welcome screen
    // Total Inactivity Time allowance for a session: The classes StartSession,SessionStamp, and SessionDead all have a cleanup
    // routine which deletes all records from bnv_session which are older than this timeperiod; format as above; so 60/1440 would be 1 hour
    //private String cleanupTimeoutSQL_String         = "SYSDATE - (3/1440)";
    private String cleanupTimeoutSQL_String         = "((SYSDATE + 0) - 1.041667)";	// ( 60/1440 = 60 min )
    private String cleanupTimeoutMsg_String         = "u bent al uitgelogd of u ben langer dan 1 uur zonder activiteit aangelogd ";


    // Index URL links
    private String IndexURL1 = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/BNV_SessionIndex?sessionid=";
    private String IndexURL2 = "&eqsid=";
    private String IndexURL3 = "> <img src=http://sysia76.citg.tudelft.nl:81/image/link_back.gif border=0>Terug naar hoofdmenu</a>";
    private String IndexURL4 = "> <img src=http://sysia76.citg.tudelft.nl:81/image/link_back.gif border=0>Hoofdmenu</a>";


    // Metadata links
    String metaURL_ct_cijfer1   = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/DisplayMeta?tabelnaam=ct_cijfer&sessionid=";
    String metaURL_ct_cijfer2   = "> <img         src=http://sysia76.citg.tudelft.nl:81/image/meta.gif border=0>Cijfertabel</a>";
    String metaURL_ct_meta1     = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/DisplayMeta?tabelnaam=ct_meta&sessionid=";
    String metaURL_ct_meta2     = "> <img src=http://sysia76.citg.tudelft.nl:81/image/meta.gif border=0>Metatabel</a>";
    String metaURL_ct_student1  = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/DisplayMeta?tabelnaam=ct_student&sessionid=";
    String metaURL_ct_student2  = "> <img src=http://sysia76.citg.tudelft.nl:81/image/meta.gif border=0>Studententabel</a>";
    String metaURL_ct_tentamen1 = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/DisplayMeta?tabelnaam=ct_tentamen&sessionid=";
    String metaURL_ct_tentamen2 = "> <img src=http://sysia76.citg.tudelft.nl:81/image/meta.gif border=0>Tentamentabel</a>";
    String metaURL_ct_vak1      = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/DisplayMeta?tabelnaam=ct_vak&sessionid=";
    String metaURL_ct_vak2      = "> <img src=http://sysia76.citg.tudelft.nl:81/image/meta.gif border=0>Vaktabel</a>";

    // HoofdMenu page links
    private String listOutletsMenuLink1     = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/BNV_ListOutlets?sessionid=";
    private String listOutletsMenuLink2     = "> List outlets per verdieping</a> (zeer lange lijsten!)";
    private String zoekOutletsMenuLink1     = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/BNV_ZoekOutlets?sessionid=";
    private String zoekOutletsMenuLink2     = "> Zoek outlets met wildcarts op IPnr, Telnr etc.</a>";
    private String add10OutletsMenuLink1    = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/BNV_AddOutlets?sessionid=";
    private String add10OutletsMenuLink2    = "> Voeg nieuwe outlets toe.</a>";
    private String editOutletMenuLink1      = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/BNV_EditOutlet?sessionid=";
    private String editOutletMenuLink2      = "> Wijzig de informatie van een outlet.</a>";
    private String deleteOutletMenuLink1    = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/BNV_DeleteOutlet?sessionid=";
    private String deleteOutletMenuLink2    = "> Verwijder een outlet uit de database.</a>";


    // Welome page <META> refresh tag... The timeout is the figure after CONTENT (in seconds) after which the Index page will automatically start
    //           NB.   When timeout is changed change welcomeRefreshTimeMsg1 too
    private String welcomeRefreshTag1       = "<META HTTP-EQUIV=\"Refresh\" CONTENT =\"10" + ';' + "URL=http://sysia76.citg.tudelft.nl:8000/servlet/BNV_SessionIndex?sessionid=";
    private String welcomeRefreshTimeMsg1   = "Na 10 seconden wordt u automatisch doorgestuurd naar het hoofdmenu";

    //  Links to Login Page
    private String loginURL1    = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/BNV_LoginUser> <img src=http://sysia76.citg.tudelft.nl:81/image/link_back.gif border=0>Terug naar loginscherm</a>";

    // Other links located on pages
    String toonVakURLexToonT1       = "<a href=\"http://sysia76.citg.tudelft.nl:8000/servlet/ToonVak?vak=";
    String toonVakURLexToonT2       = "> <img src=http://sysia76.citg.tudelft.nl:81/image/link_back.gif border=0>Terug naar vakinformatie</a>";
    String toonVakURLexCijferO      = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/ToonVak?vak=";
    String toonTentamenURLexCijferO = "<a href=http://sysia76.citg.tudelft.nl:8000/servlet/ToonTentamen?date=";

       // end of  CONSTANTS FOR SYSIA76 - NT SERVER


}