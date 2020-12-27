package manipulation;

import connection.DBConnection;
import org.apache.commons.collections4.BidiMap;
import query.QueryBuilder;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager<E, K> {
    private BidiMap<String, String> columnNames = null;
    private Connection connection = null;
    private Class<E> entityClass;

    public CategoryManager(Class<E> entity) {
        columnNames = MapManager.getMap("Category");
        connection = DBConnection.open();
        this.entityClass = entity;
    }

    public List<E> findAll() {
        try {
            Statement statement = connection.createStatement();
            String query = QueryBuilder.select("*").from("Category").build();
            System.out.println("[Query] " + query);
            ResultSet rs = statement.executeQuery(query);
            ArrayList<E> result = new ArrayList<>();
            while (rs.next()) {
                E category1 = entityClass.newInstance();
                Class<?> c = category1.getClass();
                for (Field field : entityClass.getDeclaredFields()) {
                    String columnName = columnNames.get(field.getName());
                    if (field.isAccessible()) {
                        field.set(category1, rs.getObject(columnName, field.getType()));
                    } else {
                        field.setAccessible(true);
                        field.set(category1, rs.getObject(columnName, field.getType()));
                        field.setAccessible(false);
                    }
                }
                result.add(category1);
            }
            return result;
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public E findByID(Long ID) {
        return null;
    }
}