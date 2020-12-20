import connection.ConnectionFactory;
import query.QueryBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String query1 = QueryBuilder.select("name", "age").from("SINHVIEN").where("mssv > 14").build();
        System.out.println(query1);
        String query2 = QueryBuilder.insertInto("SINHVIEN").columns("name", "age").values("Hieu", 18).build();
        System.out.println(query2);
        String query3 = QueryBuilder.update("SINHVIEN").set("name", "hieu").set("age", 21).where("name = 'Hieu'").build();
        System.out.println(query3);
        String query4 = QueryBuilder.deleteFrom("SINHVIEN").where("name = 'hieu'").build();
        System.out.println(query4);

        /**
         * Demo Connection Factory
         */
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            String query5 = QueryBuilder.select("Id", "Name").from("Category").build();
            System.out.println(query5);
            ResultSet rs = statement.executeQuery(query5);
            while (rs.next()) {
                System.out.print(rs.getString(1) + "\t"
                        + rs.getString(2) + System.lineSeparator());
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
