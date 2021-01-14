package Loader;

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

public interface ReferenceLoader<T> {



    public static <T> void load(T entity, String propertyName) {


        Connection connection = ConnectionFactory.getInstance().open();
        try {
            Class clazz = entity.getClass();
            Field propertyField = clazz.getDeclaredField(propertyName);
            OneToMany annotation = propertyField.getAnnotation(OneToMany.class);
            Type type = propertyField.getGenericType();
            String tableName = "";
            String propertyForeignKey = null;
            List<String> primaryKey = new ArrayList<>();
            Class propertyType = null;

            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                propertyType = (Class) pt.getActualTypeArguments()[0];

                for (Field field : propertyType.getDeclaredFields()) {
                    if (field.isAnnotationPresent(ManyToOne.class) && field.getType() == clazz) {
                        propertyForeignKey = field.getAnnotation(ManyToOne.class).keyPropertyName();
                        break;
//                      primaryKey.add(field.getName());
                    }
                }

                tableName = propertyType.getSimpleName();
            }

            if (annotation != null) {

                List<Object> values = new ArrayList<>();

                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        field.setAccessible(true);
                        values.add(field.get(entity));
                    }
                }

//                for (int i = 0; i < propertyForeignKey.length; ++i) {
//                    propertyForeignKey[i] += " = ?";
//                }

//                for (int i = 0; i < primaryKey.size(); ++i) {
//                    primaryKey.set(i, primaryKey.get(i) + " = ?");
//                }


                StringBuilder command = new StringBuilder();
                command.append("select * from ");
                command.append(tableName);
//                command.append(" where ");
//                command.append(String.join(" AND ", propertyForeignKey));

                PreparedStatement preparedStatement
                        = connection.prepareStatement(command.toString());

//                for (int i = 0; i < values.size(); ++i) {
//                    preparedStatement.setObject(i + 1, values.get(i));
//                }
                // not processing entity annotation
                ResultSet resultSet = preparedStatement.executeQuery();
                Mapper mapper = new Mapper(propertyType);

                Map<Object, List> propertyObjectMap = new HashMap<>();


                Field field = clazz.getDeclaredField(propertyName);
                field.setAccessible(true);
                field.set(entity, mapper.deserialize(resultSet));

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

    public void load(List<T> entities, String propertyName);

}
