package com.k11.automation.coreframework.databasehelper;

import com.k11.automation.coreframework.logger.Log;
import com.k11.automation.coreframework.util.ConfigurationManager;
import org.testng.Reporter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The database utility class.
 */
public class DBUtil {

    /**
     * The connection object.
     */
    protected static Connection connection = null;

    public static boolean getDBConnection(String url,
            String driver,
            String user,
            String password) {
        boolean connected = true;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            String encrypted = ConfigurationManager.getBundle().getString("encrypted.db.pwd");
            Log.info("Connected DB using URL=" + url + " USERNAME=" + user + " PASSWORD=" + encrypted, true);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            connected = false;
        }
        return connected;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                Reporter.log("DB Connection closed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
