import connection.ConnectionFactory;
import query.QueryBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
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

    @org.junit.Test
    public void testSelectAllByID() {
        CategoryRepository repository = new CategoryRepository();
        List<Long> ids = new ArrayList<>();
        ids.add(20L);
        ids.add(5L);
        ids.add(19L);
        List<Category> categories = repository.findAllByID(ids);
        assert categories != null;
        assert categories.size() > 0;
        System.out.println(System.lineSeparator() + "Test Select all by ID result:");
        for (Category category : categories) {
            System.out.println(category);
        }
    }

    @org.junit.Test
    public void testSaveNewRow() {
        CategoryRepository repository = new CategoryRepository();
        Category newCategory = new Category();
        newCategory.setName("New Category 1");
        newCategory.setModifiedDate(new Date(Calendar.getInstance().getTime().getTime()));
        Category result = repository.save(newCategory);
        System.out.println(result);
        assert result != null;
    }

    @org.junit.Test
    public void testSaveUpdateRow() {
        CategoryRepository repository = new CategoryRepository();
        Category existCategory = repository.findById(60L);
        existCategory.setName(existCategory.getName() + "1");
        existCategory.setModifiedDate(new Date(Calendar.getInstance().getTime().getTime()));
        Category result = repository.save(existCategory);
        System.out.println(result);
        assert result != null;
    }
}
