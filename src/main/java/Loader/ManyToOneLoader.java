package Loader;

import annotation.Key;
import annotation.ManyToOne;
import annotation.OneToMany;
import connection.ConnectionFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManyToOneLoader<T> {

    private Class<T> persistenceClass;

    public ManyToOneLoader(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    public void load(List<T> entities, String propertyName) {
        Connection connection = ConnectionFactory.getInstance().open();
        try {
            Field propertyField = persistenceClass.getDeclaredField(propertyName);
            propertyField.setAccessible(true);
            ManyToOne annotation = propertyField.getAnnotation(ManyToOne.class);
//            Type type = propertyField.getGenericType();
            String entityForeignKeyName = "";
            Class propertyType = propertyField.getType();
            String tableName = propertyType.getSimpleName();


//            if (type instanceof ParameterizedType) {
//                ParameterizedType pt = (ParameterizedType) type;
//                propertyType = (Class) pt.getActualTypeArguments()[0];
//
//                for (Field field : propertyType.getDeclaredFields()) {
//                    if (field.isAnnotationPresent(ManyToOne.class) && field.getType() == persistenceClass) {
//                        propertyForeignKeyName = field.getAnnotation(ManyToOne.class).keyPropertyName();
//                        break;
//                    }
//                }
//
////                tableName = ;
//            }

            if (annotation != null) {
                entityForeignKeyName = annotation.keyPropertyName();
                Field propertyPrimaryKeyField = null;
                Field entityForeignKeyField = persistenceClass.getDeclaredField(entityForeignKeyName);
                entityForeignKeyField.setAccessible(true);

                for (Field field : propertyType.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Key.class)) {
                        propertyPrimaryKeyField = field;
                        propertyPrimaryKeyField.setAccessible(true);
                        break;
                    }
                }

                StringBuilder command = new StringBuilder();
                command.append("select * from ");
                command.append(tableName);
                PreparedStatement preparedStatement
                        = connection.prepareStatement(command.toString());

                // not processing entity annotation
                ResultSet resultSet = preparedStatement.executeQuery();
                Mapper mapper = new Mapper(propertyType);
                List result = mapper.deserialize(resultSet);
                Map<Object, Object> propertyObjectMap = new HashMap<>();

//                for (Object obj : entities) {
//                    propertyObjectMap.put(propertyPrimaryKeyField.get(obj), );
//                }

                for (Object obj : result) {
                    Object propertyForeignKeyValue = propertyPrimaryKeyField.get(obj);
                    propertyObjectMap.put(propertyForeignKeyValue, obj);
                }

                for (Object obj : entities) {
                    Object key = entityForeignKeyField.get(obj);
                    if (propertyObjectMap.containsKey(key)) {
                        propertyField.set(obj, propertyObjectMap.get(key));
                    }
                }

            }
        } catch (NoSuchFieldException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
