// BNV Online

import java.sql.*;
import oracle.rdbms.*;
import oracle.owas.nls.*;

import java.util.Random;


// TU Delft Datawarehouse project - BNV_Randomize service class
// This class generates a unique sessionid each time it runs.
// It is called from the BNV_SessionStart class.


class BNV_Randomize {

    // Random range constants
    int min   = 1000000000;
    int max   = 2100000000;
    int range = max - min;


    public String nextSessionID() {

        Random  random;
        String  newSessionID;

        boolean unique      = true;
        String  dbaseError  = "error";

        // Set up BNV_Constants object to be used in in this class
        BNV_Constants bnvConstantsR = new BNV_Constants();

        Connection conn;
        Statement stmt;
        ResultSet result;

        // Standard Oracle database driver registration
            try {
                Class.forName(bnvConstantsR.getDriver_class());
             }
            catch (ClassNotFoundException e){
                 return dbaseError;
		    }

		    // Connect to Database
            try {
                conn = DriverManager.getConnection(bnvConstantsR.getConnect_URL(), bnvConstantsR.getConnect_user(), bnvConstantsR.getConnect_password());

                try {
                    stmt = conn.createStatement();

                    // allocate new random for sessionid & check to see that it does not already exist... if so keep trying...
                    do {
                        int count = 0;
                        random = new Random();
                        int i = (int)(random.nextFloat() * range + min);
                        newSessionID = new Integer(i).toString();

                        result = stmt.executeQuery ("SELECT COUNT(*) FROM BNV_SESSION WHERE sessionid = '" + newSessionID + "'");
                        while (result.next()) count = result.getInt(1);

                        if (count > 0)
                            count = 0;
                        else
                            unique = false;
                    }
                    while (unique);

                    stmt.close();
                }
                catch (SQLException e) {
                    return dbaseError;
                }
                finally {
                    conn.close();
                }
            }
            catch (SQLException e) {
                return dbaseError;
            } // end database processing

        return newSessionID;
    }
}
