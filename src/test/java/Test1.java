import connection.ConnectionFactory;
import org.junit.Ignore;
import org.junit.Test;
import query.QueryBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Test1 {

    /**
     * Test for Microsoft SQL Server
     */

    @Test
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

    @Test
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

    @Test
    public void testSelectByID_Exist() {
        CategoryRepository repository = new CategoryRepository();
        Category category = repository.findById(5L);
        assert category != null;
        System.out.println(System.lineSeparator() + "Test Select by ID result:");
        System.out.println(category);
    }

    @Test
    public void testSelectByID_NotExist() {
        CategoryRepository repository = new CategoryRepository();
        Category category = repository.findById(1712354L);
        assert category == null;
        System.out.println(System.lineSeparator() + "Test Select by ID result:");
        System.out.println(category);
    }

    @Test
    public void testSelectAllByID_AllExist() {
        CategoryRepository repository = new CategoryRepository();
        List<Long> ids = new ArrayList<>();
        ids.add(20L);
        ids.add(5L);
        ids.add(19L);
        List<Category> categories = repository.findAllByID(ids);
        assert categories != null;
        assert categories.size() == 3;
        for (Category category : categories) {
            assert ids.contains(category.getId());
        }
    }

    @Test
    public void testSelectAllByID_OneExist() {
        CategoryRepository repository = new CategoryRepository();
        List<Long> ids = new ArrayList<>();
        ids.add(1712354L);
        ids.add(5L);
        ids.add(1712354L);
        List<Category> categories = repository.findAllByID(ids);
        assert categories != null;
        assert categories.size() == 1;
        for (Category category : categories) {
            assert ids.contains(category.getId());
        }
    }

    @Ignore
    @Test
    public void testUpdate_IdManual_NoID() {
        CategoryRepository repository = new CategoryRepository();
        Category newCategory = new Category();
        String newName = "Test 1234";
        Date newDate = new Date(Calendar.getInstance().getTime().getTime());
        newCategory.setName(newName);
        newCategory.setModifiedDate(newDate);
        Category result = repository.save(newCategory);
        System.out.println(result);
        assert result == null;
    }

    @Ignore
    @Test
    public void testUpdate_IdManual_WithID38() {
        CategoryRepository repository = new CategoryRepository();
        Category existCategory = repository.findById(38L);
        assert existCategory != null;
        String newName = existCategory.getName() + 1;
        Date newDate = new Date(existCategory.getModifiedDate().getTime() + 86400000);
        existCategory.setName(newName);
        existCategory.setModifiedDate(newDate);
        Category result = repository.save(existCategory);
        assert result != null;
        assert result.getName().equals(newName);
        assert result.getModifiedDate().equals(newDate);
    }

    @Ignore
    @Test
    public void testInsert_IdManual_NoID() {
        CategoryRepository repository = new CategoryRepository();
        Category newCategory = new Category();
        newCategory.setName("Test 1234");
        newCategory.setModifiedDate(new Date(Calendar.getInstance().getTime().getTime()));
        Category result = repository.save(newCategory);
        System.out.println(result);
        assert result == null;
    }

    @Ignore
    @Test
    public void testInsert_IdManual_WithID() {
        CategoryRepository repository = new CategoryRepository();
        Category newCategory = new Category();
        Long newID;
        String newName = "Test 1234";
        Date newDate = new Date(Calendar.getInstance().getTime().getTime());
        for (Long i = 100L; ; i++) {
            if (repository.findById(i) == null) {
                newID = i;
                break;
            }
        }
        newCategory.setId(newID);
        newCategory.setName(newName);
        newCategory.setModifiedDate(newDate);
        System.out.print("New data: ");
        System.out.println(newCategory);
        Category result = repository.save(newCategory);
        System.out.println(result);
        assert result != null;
        assert result.getId().equals(newID);
        assert result.getName().equals(newName);
        assert result.getModifiedDate() == newDate;
    }

    //    @Ignore
    @Test
    public void testUpdate_IdAuto_NoID() {
        CategoryRepository repository = new CategoryRepository();
        Category newCategory = new Category();
        String newName = "Test 1234";
        Date newDate = new Date(Calendar.getInstance().getTime().getTime());
        newCategory.setName(newName);
        newCategory.setModifiedDate(newDate);
        Category result = repository.save(newCategory);
        System.out.println(result);
        assert result != null;
        assert result.getName().equals(newName);
        assert result.getModifiedDate().equals(newDate);
    }

    //    @Ignore
    @Test
    public void testUpdate_IdAuto_WithID60() {
        CategoryRepository repository = new CategoryRepository();
        Category existCategory = repository.findById(60L);
        assert existCategory != null;
        String newName = existCategory.getName() + 1;
        Date newDate = new Date(existCategory.getModifiedDate().getTime() + 86400000);
        existCategory.setName(newName);
        existCategory.setModifiedDate(newDate);
        Category result = repository.save(existCategory);
        assert result != null;
        assert result.getName().equals(newName);
        assert result.getModifiedDate().equals(newDate);
    }

    //    @Ignore
    @Test
    public void testInsert_IdAuto_NoID() {
        String newName = "Test 1712354";
        Date newDate = new Date(Calendar.getInstance().getTime().getTime());
        Category newCategory = new Category();
        newCategory.setName(newName);
        newCategory.setModifiedDate(newDate);
        CategoryRepository repository = new CategoryRepository();
        Category result = repository.save(newCategory);
        System.out.println(result);
        assert result != null;
        assert result.getName().equals(newName);
        assert result.getModifiedDate().equals(newDate);
    }

    //    @Ignore
    @Test
    public void testInsert_IdAuto_WithID() {
        CategoryRepository repository = new CategoryRepository();
        Category newCategory = new Category();
        Long newID = 1712354L;
        String newName = "Test 1234";
        Date newDate = new Date(Calendar.getInstance().getTime().getTime());
        newCategory.setId(newID);
        newCategory.setName(newName);
        newCategory.setModifiedDate(newDate);
        Category result = repository.save(newCategory);
        System.out.println(result);
        assert result != null;
        assert !result.getId().equals(newID);
        assert result.getName().equals(newName);
        assert result.getModifiedDate() == newDate;
    }

}
