import connection.ConnectionFactory;
import org.junit.Test;
import query.QueryBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class RepositoryTest {
    @Test
    public void mapper() throws SQLException {
        Connection connection = ConnectionFactory.getInstance().open();
        String query = QueryBuilder.select("*").from("SINHVIEN").build();
        assert connection != null;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        Mapper<Student> mapper = new Mapper<>(Student.class);
        List<Student> svs = mapper.deserialize(rs);
        assert svs != null;
        assert !svs.isEmpty();
        for (Student sv : svs) {
            System.out.print(sv.getMssv());
            System.out.print(sv.getHoVaTen());
            System.out.println(sv.getNgaySinh());
        }
    }

    @Test
    public void repositorySelect() {
        SinhVienRepository repository = new SinhVienRepository();
        List<Student> sinhViens = repository.findAll();
        assert sinhViens != null;
        assert sinhViens.size() > 0;
        for (Student sv : sinhViens) {
            System.out.println(sv.getMssv());
            System.out.println(sv.getHoVaTen());
            System.out.println(sv.getNgaySinh());
        }
    }

    @Test
    public void repositoryDelete() {
        SinhVienRepository repository = new SinhVienRepository();
        List<Student> sinhViens = repository.findAll();
        assert sinhViens != null;
        assert sinhViens.size() > 0;
        Student deletedStudent = sinhViens.get(0);
        deletedStudent = repository.delete(deletedStudent);
        assert deletedStudent != null;
    }


    @Test
    public void selectWhereTest() {
        SinhVienRepository repository = new SinhVienRepository();
        List<Student> sinhviens = repository.findAllWhere("id=?", "1712446");
        assert sinhviens != null;
        for (Student sv : sinhviens) {
            System.out.println(sv.getMssv());
            System.out.println(sv.getHoVaTen());
            System.out.println(sv.getNgaySinh());
        }
    }

    @Test
    public void selectOneWhereTest() {
        SinhVienRepository repository = new SinhVienRepository();
        Student hieu = repository.findOneWhere("id=?", "1712447");
        assert hieu != null;
        System.out.println(hieu.getMssv());
        System.out.println(hieu.getHoVaTen());
        System.out.println(hieu.getNgaySinh());
    }

}
