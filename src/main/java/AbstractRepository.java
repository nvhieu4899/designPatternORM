import annotation.Column;
import annotation.Entity;
import annotation.Id;
import connection.ConnectionFactory;
import query.QueryBuilder;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class AbstractRepository<T, ID> {
    private Class<T> tClass;
    private Class<ID> idClass;
    private Mapper<T> mapper;
    private ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
    private String tableName;


    private Map.Entry<String, String> idFieldMap;

    public List<T> findAll() {
        Connection connection = connectionFactory.open();
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


    public List<T> findAllWhere(String whereClause, Object... parameters) {
        Connection connection = connectionFactory.open();
        Entity entity = tClass.getAnnotation(Entity.class);
        String tableName = tClass.getSimpleName();
        if (!entity.table().isEmpty()) {
            tableName = entity.table();
        }
        try {
            String query = QueryBuilder.select("*").from(tableName).where(whereClause).build();
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            return mapper.deserialize(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public T findOneWhere(String whereClause, Object... parameters) {
        Connection connection = connectionFactory.open();
        Entity entity = tClass.getAnnotation(Entity.class);
        String tableName = tClass.getSimpleName();
        if (!entity.table().isEmpty()) {
            tableName = entity.table();
        }
        try {
            String query = QueryBuilder.select("*").from(tableName).where(whereClause).limitBy(1).build();
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            return mapper.deserialize(resultSet).get(0);

        } catch (SQLException e) {
            return null;
        }
    }

    public T delete(T obj) {
        try {
            Field fieldId = obj.getClass().getDeclaredField(idFieldMap.getKey());
            fieldId.setAccessible(true);
            Object deletedId = fieldId.get(obj);
            Connection connection = connectionFactory.open();
            String query = QueryBuilder.deleteFrom(tableName).where(idFieldMap.getValue() + "=?").build();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, deletedId);
            preparedStatement.execute();
            return obj;
        } catch (NoSuchFieldException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public T save(T obj) {
        throw new UnsupportedOperationException();
    }

    public AbstractRepository(Class<T> tClass, Class<ID> idClass) {
        this.tClass = tClass;
        this.idClass = idClass;
        this.mapper = new Mapper<>(tClass);
        Entity entity = tClass.getAnnotation(Entity.class);
        tableName = tClass.getSimpleName();
        HashMap<String, String> idMap = new HashMap<>();
        if (!entity.table().isEmpty()) tableName = entity.table();


        for (Field field : tClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                String objField = field.getName();
                String entityCol = field.getName();
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    if (!column.name().isEmpty()) {
                        entityCol = column.name();
                    }
                }
                idMap.put(objField, entityCol);
                this.idFieldMap = idMap.entrySet().iterator().next();
                break;
            }
        }

    }
}
