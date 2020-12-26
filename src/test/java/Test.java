import connection.DBConnection;
import query.QueryBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Test {
    @org.junit.Test
    public void mapper() throws SQLException {
        Connection connection = DBConnection.open();
        String query = QueryBuilder.select("*").from("SINHVIEN").build();
        assert connection != null;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        Mapper<SinhVien> mapper = new Mapper<>();
        List<SinhVien> svs = mapper.deserialize(rs, SinhVien.class);
        assert svs != null;
        assert !svs.isEmpty();
    }
}
