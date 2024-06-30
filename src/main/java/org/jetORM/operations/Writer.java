package org.jetORM.operations;

import org.jetORM.utils.Executer;

import java.lang.reflect.Field;
import java.sql.SQLException;

public class Writer {

    public static void write(Class<?> clazz, Object object) throws SQLException, IllegalAccessException {
        String insertQuery = buildQuery(clazz, object);
        System.out.printf(insertQuery);
        Executer.execute(insertQuery);
        System.out.printf("Object Saved");
    }

    private static String buildQuery(Class<?> clazz, Object object) throws IllegalAccessException {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(clazz.getSimpleName().toLowerCase()).append(" (");

        StringBuilder fieldNames = new StringBuilder();
        StringBuilder fieldValues = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            String fieldName = field.getName();
            fieldNames.append(fieldName).append(",");
            fieldValues.append("'").append(field.get(object)).append("',");
        }
        fieldNames.setLength(fieldNames.length() - 1);
        fieldValues.setLength(fieldValues.length() - 1);

        query.append(fieldNames).append(") VALUES (").append(fieldValues).append(");");
        return query.toString();
    }
}
