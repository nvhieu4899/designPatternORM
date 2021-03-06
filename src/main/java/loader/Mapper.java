package loader;

import annotation.Column;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mapper<T> {

    private Class<T> persistenceClass;

    public List<T> deserialize(ResultSet resultSet) {

        List<T> results = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            HashMap<String, String> deserializeContext = getDeserializeContext(metaData);
            int colNums = metaData.getColumnCount();

            while (resultSet.next()) {
                T mappedObj = persistenceClass.newInstance();

                for (int i = 0; i < colNums; i++) {
                    String colName = metaData.getColumnName(i + 1);
                    String objField = deserializeContext.getOrDefault(colName, null);

                    if (objField != null) {
                        Field field = persistenceClass.getDeclaredField(objField);
                        field.setAccessible(true);
                        Object fieldValue = resultSet.getObject(i + 1, field.getType());
                        field.set(mappedObj, fieldValue);
                    }
                }

                results.add(mappedObj);
            }

//            Class clazz = Helper.class;
//            clazz.getField("unknown").getType()

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //getCol
    private HashMap<String, String> getDeserializeContext(ResultSetMetaData metaData) throws SQLException {
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            hashMap.put(metaData.getColumnName(i + 1), null);
        }
        Field[] outputField = persistenceClass.getDeclaredFields();

        for (Field f : outputField) {
            String mapperName = f.isAnnotationPresent(Column.class) ? f.getAnnotation(Column.class).name() : f.getName();
            hashMap.put(mapperName, f.getName());
        }
        return hashMap;
    }


    public HashMap<String, Object> serialize(T obj) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            Field[] fields = persistenceClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object columnValue = field.get(obj);
                if (columnValue == null) continue;
                String columnName = field.isAnnotationPresent(Column.class) ? field.getAnnotation(Column.class).name() : field.getName();
                hashMap.put(columnName, columnValue);
            }
            return hashMap;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Mapper(Class<T> tClass) {
        this.persistenceClass = tClass;
    }
}
