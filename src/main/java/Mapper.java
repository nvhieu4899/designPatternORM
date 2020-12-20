import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mapper {

    public <T> List<T> deserialize(ResultSet resultSet, Class<T> outputClass) throws IllegalAccessException, InstantiationException,
            SQLException, NoSuchFieldException {

        List<T> results = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        HashMap<String, String> deserializeContext = getDeserializeContext(metaData, outputClass);
        int rowIndex = 0;
        int colNums = metaData.getColumnCount();
        while (resultSet.next()) {

            T mappedObj = outputClass.newInstance();
            for (int i = 0; i < colNums; i++) {
                String colName = metaData.getColumnName(i + 1);
                String objField = deserializeContext.getOrDefault(colName, null);

                if (objField != null) {
                    Field field = outputClass.getDeclaredField(objField);
                    field.setAccessible(true);
                    Object fieldValue = resultSet.getObject(i + 1);
                    field.set(mappedObj, fieldValue);
                }
            }
            results.add(mappedObj);
        }
        return results;
    }

    //getCol
    private HashMap<String, String> getDeserializeContext(ResultSetMetaData metaData, Class outputClass) throws SQLException {
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            hashMap.put(metaData.getColumnName(i + 1), null);
        }

        Field[] outputField = outputClass.getDeclaredFields();

        for (Field f : outputField) {
            hashMap.put(f.getName(), f.getName());
        }
        return hashMap;
    }
}
