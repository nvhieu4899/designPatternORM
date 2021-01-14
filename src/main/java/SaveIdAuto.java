import loader.Mapper;
import query.QueryBuilder;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SaveIdAuto<T, ID> extends SaveStrategy<T, ID> {
    @Override
    public T insert(T obj) {
        HashMap<String, Object> properties = mapper.serialize(obj);
        properties.remove(idName.getValue());
        String[] columnNames = new String[properties.size()];
        Object[] parameters = new Object[properties.size()];
        properties.keySet().toArray(columnNames);
        Arrays.fill(parameters, "?");
        try {
            String query = QueryBuilder.insertInto(tableName).columns(columnNames).values(parameters).build();
            System.out.println(query);
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Object columnValue : properties.values()) statement.setObject(i++, columnValue);
            if (statement.executeUpdate() > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    Field idField = obj.getClass().getDeclaredField(idName.getKey());
                    idField.setAccessible(true);
                    idField.set(obj, resultSet.getLong(1));
                    return obj;
                }
            }
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SaveIdAuto(Connection connection, Mapper<T> mapper, String tableName, Map.Entry<String, String> idName) {
        super(connection, mapper, tableName, idName);
    }
}