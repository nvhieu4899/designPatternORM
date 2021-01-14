package Loader;

import annotation.Entity;
import annotation.Id;
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

public class OneToManyLoader<T> implements ReferenceLoader<T> {
    private Class<T> persistenceClass;

    public OneToManyLoader(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    @Override
    public void load(List<T> entities, String propertyName) {

        Connection connection = ConnectionFactory.getInstance().open();
        try {
            // get info of property type
            Field propertyField = persistenceClass.getDeclaredField(propertyName);
            propertyField.setAccessible(true);
            OneToMany oneToManyAnnotation = propertyField.getAnnotation(OneToMany.class);
            Type type = propertyField.getGenericType();
            String tableName = "";
            String propertyForeignKeyName = null;
            Class propertyType = null;

            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                propertyType = (Class) pt.getActualTypeArguments()[0];

                for (Field field : propertyType.getDeclaredFields()) {
                    if (field.isAnnotationPresent(ManyToOne.class) && field.getType() == persistenceClass) {
                        propertyForeignKeyName = field.getAnnotation(ManyToOne.class).keyPropertyName();
                        break;
                    }
                }
                Entity entityAnnotation = (Entity) propertyType.getAnnotation(Entity.class);
                if (entityAnnotation.table().isEmpty())
                    tableName = propertyType.getSimpleName();
                else
                    tableName = entityAnnotation.table();
            }

            if (oneToManyAnnotation != null) {
                // get info of key
                Field entityPrimaryKeyField = null;
                Field propertyTypeForeignKeyField = propertyType.getDeclaredField(propertyForeignKeyName);
                propertyTypeForeignKeyField.setAccessible(true);

                for (Field field : persistenceClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        entityPrimaryKeyField = field;
                        entityPrimaryKeyField.setAccessible(true);
                        break;
                    }
                }

                // execute query
                StringBuilder command = new StringBuilder();
                command.append("select * from ");
                command.append(tableName);

                PreparedStatement preparedStatement
                        = connection.prepareStatement(command.toString());

                // process result
                ResultSet resultSet = preparedStatement.executeQuery();
                Mapper mapper = new Mapper(propertyType);
                List result = mapper.deserialize(resultSet);
                Map<Object, List> propertyObjectMap = new HashMap<>();

                // initialize empty object list
                for (Object obj : entities) {
                    propertyObjectMap.put(entityPrimaryKeyField.get(obj), new ArrayList());
                }

                // group objects
                for (Object obj : result) {
                    Object propertyForeignKeyValue = propertyTypeForeignKeyField.get(obj);
                    if (propertyObjectMap.containsKey(propertyForeignKeyValue)) {
                        propertyObjectMap.get(propertyForeignKeyValue).add(obj);
                    }
                }

                // reference object list to respective entities
                for (Object obj : entities) {
                    Object key = entityPrimaryKeyField.get(obj);
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
