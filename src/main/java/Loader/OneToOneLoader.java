package Loader;

import annotation.Key;
import annotation.ManyToOne;
import annotation.OneToMany;
import connection.ConnectionFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class OneToOneLoader<T> {

    private Class<T> persistenceClass;

    public OneToOneLoader(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    public void load(List<T> entities, String propertyName) {
        Connection connection = ConnectionFactory.getInstance().open();

        try {
//            Method method = OneToOneLoader.class.getMethod("load", List.class, String.class);
//            Type type;
//            Class entityType = null;

//            for (Type parameterType : method.getGenericParameterTypes()) {
//                if (parameterType instanceof ParameterizedType) {
//                    ParameterizedType pt = (ParameterizedType) parameterType;
//                    entityType = (Class) pt.getActualTypeArguments()[0];
//                    break;
//                }
//            }


//            if (type instanceof ParameterizedType) {

//            } else return;


            Field propertyField = persistenceClass.getDeclaredField(propertyName);
            propertyField.setAccessible(true);
            Class propertyType = propertyField.getType();

            String tableName = propertyField.getType().getSimpleName();

            // primary key of
            Field propertyPrimaryKey = null;
            Field entityPrimaryKey = null;

            for (Field field : propertyType.getDeclaredFields()) {
                if (field.isAnnotationPresent(Key.class)) {
                    propertyPrimaryKey = field;
                    propertyPrimaryKey.setAccessible(true);
                    break;
                }
            }

            for (Field field : persistenceClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Key.class)) {
                    entityPrimaryKey = field;
                    entityPrimaryKey.setAccessible(true);
                    break;
                }
            }

//            List<Object> values = new ArrayList<>();
//
//            for (Field field : entityType.getDeclaredFields()) {
//                if (field.isAnnotationPresent(Key.class)) {
//                    field.setAccessible(true);
//                    values.add(field.get(entities));
//
//                }
//            }


//            for (int i = 0; i < primaryKey.size(); ++i) {
//                primaryKey.set(i, primaryKey.get(i) + " = ?");
//            }

            StringBuilder command = new StringBuilder();
            command.append("select * from ");
            command.append(tableName);
//            command.append(" where ");
//            command.append(String.join(" AND ", primaryKey));

            PreparedStatement preparedStatement
                    = connection.prepareStatement(command.toString());

//            for (int i = 0; i < values.size(); ++i) {
//                preparedStatement.setObject(i + 1, values.get(i));
//            }
            // not processing entity annotation
            ResultSet resultSet = preparedStatement.executeQuery();
            Mapper mapper = new Mapper(propertyType);

//            Field field = entityType.getDeclaredField(propertyName);
//            field.setAccessible(true);
            List result = mapper.deserialize((resultSet));

            Map<Object, Object> propertyObjectMap = new HashMap<>();
//            for (T entity : entities) {
//                propertyObjectMap.put(entityPrimaryKey.get(entity), new ArrayList());
//            }

            for (Object obj : result) {
                propertyObjectMap.put(propertyPrimaryKey.get(obj), obj);
            }

            for (T entity : entities) {
                if (propertyObjectMap.containsKey(entityPrimaryKey.get(entity))) {
                    propertyField.set(entity, propertyObjectMap.get(entityPrimaryKey.get(entity)));
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
