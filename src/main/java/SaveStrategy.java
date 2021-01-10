import query.QueryBuilder;
import query.UpdateQueryBuilder;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class SaveStrategy<T, ID> {
    protected Connection connection;
    protected Mapper<T> mapper;
    protected String tableName;
    protected Map.Entry<String, String> idName;

    public T update(T obj) {
        HashMap<String, Object> hashMap = mapper.serialize(obj);
        hashMap.remove(idName.getValue());

        UpdateQueryBuilder queryBuilder = QueryBuilder.update(tableName);
        hashMap.forEach(queryBuilder::set);
        queryBuilder.where(idName.getValue().concat(" = ?"));

        try {
            Field idField = obj.getClass().getDeclaredField(idName.getKey());
            idField.setAccessible(true);
            PreparedStatement statement = connection.prepareStatement(queryBuilder.build());
            statement.setObject(1, idField.get(obj));
            if (statement.executeUpdate() > 0)
                return obj;
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract T insert(T obj);

    public SaveStrategy(Connection connection, Mapper<T> mapper, String tableName, Map.Entry<String, String> idName) {
        this.connection = connection;
        this.mapper = mapper;
        this.tableName = tableName;
        this.idName = idName;
    }
}
