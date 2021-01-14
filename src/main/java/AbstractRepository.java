import loader.Mapper;
import annotation.Entity;
import connection.ConnectionFactory;
import query.QueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
            ResultSet resultSet
                    = connection.createStatement().executeQuery(QueryBuilder.select("*").from(tableName).build());
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
        throw new UnsupportedOperationException();
    }

    public AbstractRepository(Class<T> tClass, Class<ID> idClass) {
        this.tClass = tClass;
        this.idClass = idClass;
        this.mapper = new Mapper<>(tClass);
    }
}
