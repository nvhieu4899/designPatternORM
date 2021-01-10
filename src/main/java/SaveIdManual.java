import query.QueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SaveIdManual<T, ID> extends SaveStrategy<T, ID> {
    @Override
    public T insert(T obj) {
        HashMap<String, Object> properties = mapper.serialize(obj);
        String[] columnNames = new String[properties.size()];
        Object[] parameters = new Object[properties.size()];
        properties.keySet().toArray(columnNames);
        Arrays.fill(parameters, "?");
        try {
            String query = QueryBuilder.insertInto(tableName).columns(columnNames).values(parameters).build();
            PreparedStatement statement = connection.prepareStatement(query);
            int i = 1;
            for (Object columnValue : properties.values()) statement.setObject(i++, columnValue);
            if (statement.executeUpdate() > 0)
                return obj;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SaveIdManual(Connection connection, Mapper<T> mapper, String tableName, Map.Entry<String, String> idName) {
        super(connection, mapper, tableName, idName);
    }
}