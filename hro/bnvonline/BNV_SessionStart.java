// BNV Online.
// datawarehouse application, geschreven door Erik van Zijst, icehawk@xs4all.nl, 25/11/98
// meeste geript van Anthony.

import java.sql.*;

import oracle.html.*;
import oracle.rdbms.*;
import oracle.owas.nls.*;
import oracle.owas.wrb.services.http.*;

// TU Delft Datawarehouse project - BNV_SessionStart service class
// This class creates a new session record in bnv_session after a succesful logon
// It is called from BNV_LoginUser

public  class BNV_SessionStart
{
        public String createSession(String inUserID, String inRealName, String inEqs)
	{
           // Set up BNV_Constants object to be used in in this class
            BNV_Constants bnvConstantsSS   = new BNV_Constants();

            String cleanup_timeout      = bnvConstantsSS.getCleanupTimeoutSQL_String();
            String cleanup_timeout_msg  = bnvConstantsSS.getCleanupTimeoutMsg_String();

            String  sessionID = null;
            String  userID;
            String  realName;
            String  eqs;

            String  languageID  = "Nederlands";
            String  logStatus   = "Thans ingelogd";
            String  logStatus1  = "Opruim timeout";

            // Standard DataBase variables / references
            Connection conn;
		    Statement stmt;

            String  dbaseError  = "error";

            userID              = inUserID;
            realName            = inRealName;
            eqs                 = inEqs;
            //languageID       = inLanguaggeID

            // validate incoming parameters......
            if (userID == null)
                return "error1";

           // First allocate a unique random for the new sessionid...........
            sessionID        = new BNV_Randomize().nextSessionID();
            //unlikely......
            if (sessionID == null || sessionID.equals("error"))
                  return "error2";

            // Standard Oracle database driver registration
            try {
                Class.forName(bnvConstantsSS.getDriver_class());
             }
            catch (ClassNotFoundException e){
                 return "error3";
		    }

            // Connect to database...
	    try {
                conn = DriverManager.getConnection(bnvConstantsSS.getConnect_URL(), bnvConstantsSS.getConnect_user(), bnvConstantsSS.getConnect_password());

                try {
                    stmt = conn.createStatement();

                    // Routine Cleanup:  write to the log all records which are older than the cleanup timeout & then delete them from bnv_session.......
                    // NB the typelogout field is first updated; this  is necessary for the log......
                        stmt.executeUpdate("UPDATE BNV_SESSION SET TYPELOGOUT = '" + logStatus1 + "' WHERE ("+ cleanup_timeout +") > STARTDATE");
                        stmt.executeUpdate("COMMIT");

                        stmt.executeUpdate("INSERT INTO BNV_SESSIONLOG SELECT SESSIONID, USERID, REALNAME, STARTDATE, LANGUAGEID, TYPELOGOUT, EQS, STAMPS FROM BNV_SESSION WHERE ("+ cleanup_timeout +") > STARTDATE");
                        stmt.executeUpdate("COMMIT");

                    try {
                        stmt.executeUpdate ("DELETE FROM BNV_SESSION WHERE ("+ cleanup_timeout +") > STARTDATE");
                        stmt.executeUpdate("COMMIT");

                        try {
                            // Now create the new bnv-session record ......
                            stmt.executeUpdate("INSERT INTO BNV_SESSION VALUES('"+sessionID+"', '" + userID + "','" + realName+ "', SYSDATE, '" + languageID + "', '" + logStatus + "', '" + eqs + "', 0)");
                            stmt.executeUpdate("COMMIT");
                        }
                        catch (SQLException e) {
                            return "error4";
                        }
                    }
                    catch (SQLException e) {
                        return "error5";
                    }
                }
                catch (SQLException e) {
                     return "error6";
                }
                finally {
                    conn.close();
                }
            }
            catch (SQLException e) {
                return "error7";
            }
         return sessionID;
        }
}
