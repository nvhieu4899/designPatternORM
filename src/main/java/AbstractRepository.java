import annotation.Entity;
import connection.ConnectionFactory;
import query.QueryBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            ResultSet resultSet = connection.createStatement().executeQuery(QueryBuilder.select("*").from(tableName).build());
            return mapper.deserialize(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public T findById(ID id) {
        throw new UnsupportedOperationException();
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
