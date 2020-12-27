import connection.ConnectionFactory;
import query.QueryBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Test {
    @org.junit.Test
    public void mapper() throws SQLException {
        Connection connection = ConnectionFactory.getInstance().open();
        String query = QueryBuilder.select("*").from("SINHVIEN").build();
        assert connection != null;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        Mapper<SinhVien> mapper = new Mapper<>(SinhVien.class);
        List<SinhVien> svs = mapper.deserialize(rs);
        assert svs != null;
        assert !svs.isEmpty();
        for (SinhVien sv : svs) {
            System.out.print(sv.getMssv());
            System.out.print(sv.getHoVaTen());
            System.out.println(sv.getNgaySinh());
        }
    }

    @org.junit.Test
    public void repositorySelect() {
        SinhVienRepository repository = new SinhVienRepository();
        List<SinhVien> sinhViens = repository.findAll();
        assert sinhViens != null;
        assert sinhViens.size() > 0;
        for (SinhVien sv : sinhViens) {
            System.out.print(sv.getMssv());
            System.out.print(sv.getHoVaTen());
            System.out.println(sv.getNgaySinh());
        }
    }
}
