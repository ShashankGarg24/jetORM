package org.jetORM.operations;

import org.jetORM.config.DbLogger;
import org.jetORM.utils.TableUtility;

import java.lang.reflect.Field;
import java.sql.SQLException;

public class Writer {

    private static final DbLogger logger = DbLogger.getInstance();

    public static void write(Class<?> clazz, Object object) throws SQLException, IllegalAccessException {
        String insertQuery = buildQuery(clazz, object);
        logger.info(insertQuery);
        TableUtility.executeInsert(insertQuery);
        logger.info("Object Saved Successfully");
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
