package connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    //Default location
    private static final String CONFIG_FILE_PATH = "\\config.properties";
    private String URL = "";
    private String USERNAME = "";
    private String PASSWORD = "";
    private static ConnectionFactory instance;

    private ConnectionFactory() {
    }

    /**
     * Open connection to Database
     *
     * @return Connection object
     */

    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
            instance.readConfig(CONFIG_FILE_PATH);
        }
        return instance;
    }


    public Connection open() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Read config data to establish JDBC connection.
     */
    private void readConfig(String configFilePath) {
        Properties properties = new Properties();
        InputStream input = null;
        try {
            String _dirname = System.getProperty("user.dir");
            input = new FileInputStream(_dirname.concat(configFilePath));
            properties.load(input);
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