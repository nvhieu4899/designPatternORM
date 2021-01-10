import Loader.Mapper;
import connection.ConnectionFactory;
import query.QueryBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Test1 {

    /**
     * Test for Microsoft SQL Server
     */

    @org.junit.Test
    public void testMapping() throws SQLException {
        Connection connection = ConnectionFactory.getInstance().open();
        assert connection != null;
        String query = QueryBuilder.select("*").from("Category").build();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        assert resultSet != null;
        Mapper<Category> categoryMapper = new Mapper<>(Category.class);
        List<Category> categories = categoryMapper.deserialize(resultSet);
        assert categories != null;
        assert categories.size() > 0;
        System.out.println(System.lineSeparator() + "Test mapping result:");
        for (Category category : categories) {
            System.out.println(category);
        }
    }

    @org.junit.Test
    public void testSelectAll() {
        CategoryRepository repository = new CategoryRepository();
        List<Category> categories = repository.findAll();
        assert categories != null;
        assert categories.size() > 0;
        System.out.println(System.lineSeparator() + "Test Select all result:");
        for (Category category : categories) {
            System.out.println(category);
        }
    }

    @org.junit.Test
    public void testSelectByID() {
        CategoryRepository repository = new CategoryRepository();
        Category category = repository.findById(5L);
        assert category != null;
        System.out.println(System.lineSeparator() + "Test Select by ID result:");
        System.out.println(category);
    }
}
