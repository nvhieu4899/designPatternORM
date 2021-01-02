import annotation.Entity;
import connection.ConnectionFactory;
import query.QueryBuilder;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public abstract class AbstractRepository<T, ID> {
    private Class<T> tClass;
    private Class<ID> idClass;
    private Mapper<T> mapper;
    private ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    public List<T> findAll() {
        Connection connection = connectionFactory.open();
        Entity entity = tClass.getAnnotation(Entity.class);
        String tableName = tClass.getSimpleName();
        if (!entity.table().isEmpty()) tableName = entity.table();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(QueryBuilder.select("*").from(tableName).build());
            return mapper.deserialize(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public T findById(ID id) {
        Connection connection = connectionFactory.open();
        Entity entity = tClass.getAnnotation(Entity.class);
        String tableName = tClass.getSimpleName();
        if (!entity.table().isEmpty()) {
            tableName = entity.table();
        }
        try {
            String query = QueryBuilder.select("*").from(tableName).where("ID = ?").build();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            return mapper.deserialize(resultSet).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<T> findAllByID(Collection<ID> ids) {
        List<T> result = new ArrayList<>();
        for (ID id : ids) {
            result.add(this.findById(id));
        }
        return result;
    }

    public T delete(T obj) {
        throw new UnsupportedOperationException();
    }

    public T save(T obj) {
        Connection connection = ConnectionFactory.getInstance().open();
        Entity entity = tClass.getAnnotation(Entity.class);
        String tableName = tClass.getSimpleName();
        if (!entity.table().isEmpty()) {
            tableName = entity.table();
        }
        HashMap<String, Object> hashMap = mapper.serialize(obj);
        String[] columnNames = new String[hashMap.size()];
        hashMap.keySet().toArray(columnNames);
        List<Object> columnValues = new ArrayList<>(hashMap.values());
        Object[] parameters = new Object[hashMap.size()];
        Arrays.fill(parameters, "?");
        try {
            String query = QueryBuilder.insertInto(tableName).columns(columnNames).values(parameters).build();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < columnValues.size(); i++) {
                statement.setObject(i + 1, columnValues.get(i));
            }
            int effectedRow = statement.executeUpdate();
            if (effectedRow > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    ID generatedKey = resultSet.getObject(1, idClass);
                    for (Field field : tClass.getDeclaredFields()) {
                        if (field.getName().equalsIgnoreCase("ID")) {
                            field.setAccessible(true);
                            field.set(obj, generatedKey);
                            return obj;
                        }
                    }
                }
            }
            return null;
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public AbstractRepository(Class<T> tClass, Class<ID> idClass) {
        this.tClass = tClass;
        this.idClass = idClass;
        this.mapper = new Mapper<>(tClass);
    }
}
