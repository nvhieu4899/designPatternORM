package connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    //Default location
    private static final String CONFIG_FILE_PATH = "\\config.properties";
    private static String DRIVER = "";
    private static String URL = "";
    private static String USERNAME = "";
    private static String PASSWORD = "";

    private DBConnection() {
    }

    /**
     * Open connection to Database
     *
     * @return Connection object
     */
    public static Connection open() {
        try {
            if (DRIVER.isEmpty() || URL.isEmpty() || USERNAME.isEmpty() || PASSWORD.isEmpty()) {
                readConfig();
            }
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Read config data to establish JDBC connection.
     */
    private static void readConfig() {
        System.out.println("Reading config file...");
        Properties properties = new Properties();
        InputStream input = null;
        try {
            String _dirname = System.getProperty("user.dir");
            input = new FileInputStream(_dirname.concat(CONFIG_FILE_PATH));
            properties.load(input);
            DRIVER = properties.getProperty("DRIVER");
            URL = properties.getProperty("URL");
            USERNAME = properties.getProperty("USERNAME");
            PASSWORD = properties.getProperty("PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}