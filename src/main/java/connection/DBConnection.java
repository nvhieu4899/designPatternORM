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
    private static String DRIVER = null;
    private static String URL = null;
    private static String USERNAME = null;
    private static String PASSWORD = null;

    private DBConnection() {
    }

    /**
     * Open connection to Database
     *
     * @return Connection object
     */
    public static Connection open() {
        try {
            if (DRIVER == null || URL == null || USERNAME == null || PASSWORD == null) {
                readConfig();
            }
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
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