package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    //TODO Read connection's config from file
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://4de3f572-86f0-47db-bee6-ac58007e9723.sqlserver.sequelizer.com";
    private static final String USERNAME = "gnrtmtydlfifkgmw";
    private static final String PASSWORD = "Pz626ohHizzmEJrWoKHME62n8XyNYW7AfhFKmvnbyhLetTKY4E7WNYwSGR2gWARu";
    private static Connection connection = null;

    private DBConnection() {
    }

    /**
     * Open connection to Database
     *
     * @return Connection object
     *
     * @throws SQLException if connection string is invalid
     * @throws ClassNotFoundException if driver class name is invalid or not exist
     */
    public static Connection open() {
        if (connection == null) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Close connection to Database
     *
     * @throws SQLException if closing connection fail
     */
    public static void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}