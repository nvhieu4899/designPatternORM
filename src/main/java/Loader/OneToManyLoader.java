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

public class OneToManyLoader<T> {
    private Class<T> persistenceClass;

    public OneToManyLoader(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    public void load(List<T> entities, String propertyName) {

        Connection connection = ConnectionFactory.getInstance().open();
        try {
//            Class clazz = entity.getClass();
            Field propertyField = persistenceClass.getDeclaredField(propertyName);
            propertyField.setAccessible(true);
            OneToMany annotation = propertyField.getAnnotation(OneToMany.class);
            Type type = propertyField.getGenericType();
            String tableName = "";
            String propertyForeignKeyName = null;
//            List<String> primaryKey = new ArrayList<>();
            Class propertyType = null;

            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                propertyType = (Class) pt.getActualTypeArguments()[0];

                for (Field field : propertyType.getDeclaredFields()) {
                    if (field.isAnnotationPresent(ManyToOne.class) && field.getType() == persistenceClass) {
                        propertyForeignKeyName = field.getAnnotation(ManyToOne.class).keyPropertyName();
                        break;
//                      primaryKey.add(field.getName());
                    }
                }

                tableName = propertyType.getSimpleName();
            }

            if (annotation != null) {

                Field entityPrimaryKeyField = null;
                Field propertyForeignKeyField = propertyType.getDeclaredField(propertyForeignKeyName);
                propertyForeignKeyField.setAccessible(true);

                for (Field field : persistenceClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Key.class)) {
                        entityPrimaryKeyField = field;
                        entityPrimaryKeyField.setAccessible(true);
                        break;
                    }
                }

//                for (int i = 0; i < propertyForeignKeyName.length; ++i) {
//                    propertyForeignKeyName[i] += " = ?";
//                }

//                for (int i = 0; i < primaryKey.size(); ++i) {
//                    primaryKey.set(i, primaryKey.get(i) + " = ?");
//                }


                StringBuilder command = new StringBuilder();
                command.append("select * from ");
                command.append(tableName);
//                command.append(" where ");
//                command.append(String.join(" AND ", propertyForeignKeyName));

                PreparedStatement preparedStatement
                        = connection.prepareStatement(command.toString());

//                for (int i = 0; i < values.size(); ++i) {
//                    preparedStatement.setObject(i + 1, values.get(i));
//                }
                // not processing entity annotation
                ResultSet resultSet = preparedStatement.executeQuery();
                Mapper mapper = new Mapper(propertyType);
                List result = mapper.deserialize(resultSet);
                Map<Object, List> propertyObjectMap = new HashMap<>();

                for (Object obj : entities) {
                    propertyObjectMap.put(entityPrimaryKeyField.get(obj), new ArrayList());
                }

                for (Object obj : result) {
                    Object propertyForeignKeyValue = propertyForeignKeyField.get(obj);
                    if (propertyObjectMap.containsKey(propertyForeignKeyValue)) {
                        propertyObjectMap.get(propertyForeignKeyValue).add(obj);
                    }
                }

                for (Object obj : entities) {
                    Object key = entityPrimaryKeyField.get(obj);
                    if (propertyObjectMap.containsKey(key)) {
                        propertyField.set(obj, propertyObjectMap.get(key));
                    }
                }


//                Field field = persistenceClass.getDeclaredField(propertyName);
//                field.setAccessible(true);
//                field.set(entity, mapper.deserialize(resultSet));

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
