package Loader;

import annotation.Entity;
import annotation.Id;
import annotation.ManyToOne;
import connection.ConnectionFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManyToOneLoader<T> implements ReferenceLoader<T> {

    private Class<T> persistenceClass;

    public ManyToOneLoader(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    public void load(List<T> entities, String propertyName) {
        Connection connection = ConnectionFactory.getInstance().open();
        try {
            // get info of property type
            Field propertyField = persistenceClass.getDeclaredField(propertyName);
            propertyField.setAccessible(true);
            Class propertyType = propertyField.getType();
            ManyToOne manyToOneAnnotation = propertyField.getAnnotation(ManyToOne.class);
            Entity entityAnnotation = (Entity) propertyType.getAnnotation(Entity.class);
            String tableName = propertyType.getSimpleName();

            // get table name
            if (!entityAnnotation.table().isEmpty()) {
                tableName = entityAnnotation.table();
            }

            if (manyToOneAnnotation != null) {
                // get info of key
                String entityForeignKeyName = manyToOneAnnotation.keyPropertyName();
                Field entityForeignKeyField = persistenceClass.getDeclaredField(entityForeignKeyName);
                entityForeignKeyField.setAccessible(true);
                Field propertyTypePrimaryKeyField = null;

                for (Field field : propertyType.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        propertyTypePrimaryKeyField = field;
                        propertyTypePrimaryKeyField.setAccessible(true);
                        break;
                    }
                }

                // execute query
                StringBuilder command = new StringBuilder();
                command.append("select * from ");
                command.append(tableName);
                PreparedStatement preparedStatement
                        = connection.prepareStatement(command.toString());
                ResultSet resultSet = preparedStatement.executeQuery();

                // process result
                Mapper mapper = new Mapper(propertyType);
                List result = mapper.deserialize(resultSet);
                Map<Object, Object> propertyObjectMap = new HashMap<>();

                // group objects
                for (Object obj : result) {
                    Object propertyForeignKeyValue = propertyTypePrimaryKeyField.get(obj);
                    propertyObjectMap.put(propertyForeignKeyValue, obj);
                }

                // reference object to respective entities
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
