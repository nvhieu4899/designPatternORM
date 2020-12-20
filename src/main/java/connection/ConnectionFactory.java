package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection Factory Class
 */
public class ConnectionFactory {

    //Dang tim hieu phuong phap doc cac thuoc tinh ben duoi tu file XML
    //va se cap nhat trong commit tiep theo.
    public static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String URL = "jdbc:sqlserver://4de3f572-86f0-47db-bee6-ac58007e9723.sqlserver.sequelizer.com;databaseName=db4de3f57286f047dbbee6ac58007e9723;user=gnrtmtydlfifkgmw;password=Pz626ohHizzmEJrWoKHME62n8XyNYW7AfhFKmvnbyhLetTKY4E7WNYwSGR2gWARu;";
    public static final String USERNAME = "jibkjvkqfajeoemr";
    public static final String PASSWORD = "Q7KpWcoKC7Zw6SK7YbL8hHkpdWg6zXM2RrSkjGotgz4xacPCyxtGFEFhUvk84qkM";

    /**
     * Get connection to Database
     *
     * @return Connection object
     * @throws SQLException ClassNotFoundException
     */
    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}