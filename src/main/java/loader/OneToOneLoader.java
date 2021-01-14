package loader;

import annotation.Entity;
import annotation.Id;
import connection.ConnectionFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneToOneLoader<T> implements ReferenceLoader<T> {

    private Class<T> persistenceClass;

    public OneToOneLoader(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    @Override
    public void load(List<T> entities, String propertyName) {
        Connection connection = ConnectionFactory.getInstance().open();

        try {
            // get info of property type
            Field propertyField = persistenceClass.getDeclaredField(propertyName);
            propertyField.setAccessible(true);
            Class propertyType = propertyField.getType();
            Entity entityAnnotation = (Entity) propertyType.getAnnotation(Entity.class);
            String tableName = propertyType.getSimpleName();
            if(!entityAnnotation.table().isEmpty())
                tableName = entityAnnotation.table();

            // get info of key
            Field propertyPrimaryKey = null;
            Field entityPrimaryKey = null;

            for (Field field : propertyType.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    propertyPrimaryKey = field;
                    propertyPrimaryKey.setAccessible(true);
                    break;
                }
            }

            for (Field field : persistenceClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    entityPrimaryKey = field;
                    entityPrimaryKey.setAccessible(true);
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
            List result = mapper.deserialize((resultSet));
            Map<Object, Object> propertyObjectMap = new HashMap<>();

            // group objects
            for (Object obj : result) {
                propertyObjectMap.put(propertyPrimaryKey.get(obj), obj);
            }

            // reference objects to respective entities
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
