package org.jetORM.schema;

import org.jetORM.annotations.Id;
import org.jetORM.config.DbLogger;
import org.jetORM.exceptions.PrimaryKeyNotPresentException;
import org.jetORM.utils.TableUtility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableOperations {

    private static final DbLogger logger = DbLogger.getInstance();

    public static <T> T read(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException, IllegalAccessException, SQLException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String readQuery = buildReadQuery(clazz);
        logger.info(readQuery);
        return TableUtility.executeRead(readQuery, clazz, id);
    }

    public static void write(Class<?> clazz, Object object) throws SQLException, IllegalAccessException, NoSuchMethodException, PrimaryKeyNotPresentException, InstantiationException, InvocationTargetException {
        Field primaryField = TableUtility.fetchPrimaryKeyForEntity(clazz);
        if(objectPresent(clazz, primaryField.get(object))){
            ArrayList<Object> values = new ArrayList<>();
            String updateQuery = buildUpdateQuery(clazz, object, values);
            logger.info(updateQuery);
            TableUtility.executeUpdate(updateQuery, values);
            logger.info("Object Updated Successfully");
            return;
        }
        String insertQuery = buildWriteQuery(clazz, object);
        logger.info(insertQuery);
        TableUtility.executeInsert(insertQuery);
        logger.info("Object Saved Successfully");
    }

    public static void remove(Class<?> clazz, Object id) throws SQLException, PrimaryKeyNotPresentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        if(!objectPresent(clazz, id)){
            logger.warn("Object not present");
            return;
        }
        String removeQuery = buildRemoveQuery(clazz);
        logger.info(removeQuery);
        TableUtility.executeDelete(removeQuery, clazz, id);
        logger.info("Record Removed Successfully");
    }

    private static boolean objectPresent(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException, IllegalAccessException, SQLException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String readQuery = buildReadQuery(clazz);
        return TableUtility.executeRead(readQuery, clazz, id) != null;
    }

    private static String buildWriteQuery(Class<?> clazz, Object object) throws IllegalAccessException {
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


    private static String buildUpdateQuery(Class<?> clazz, Object obj, ArrayList<Object> values) throws IllegalAccessException {
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(clazz.getSimpleName().toLowerCase()).append(" SET");
        Field[] fields = clazz.getDeclaredFields();
        Field primaryField = TableUtility.fetchPrimaryKeyForEntity(clazz);

        for(Field field : fields){
            field.setAccessible(true);
            if(!field.isAnnotationPresent(Id.class)){
                query.append(" ").append(field.getName()).append("=?,");
                values.add(field.get(obj));
            }
        }
        values.add(primaryField.get(obj));
        query.setLength(query.length()-1);
        query.append(" WHERE ").append(primaryField.getName()).append("=?;");
        return query.toString();
    }

    private static String buildReadQuery(Class<?> clazz) throws PrimaryKeyNotPresentException, IllegalAccessException {
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(clazz.getSimpleName().toLowerCase()).append(" WHERE ");
        Field primaryField = TableUtility.fetchPrimaryKeyForEntity(clazz);

        if(primaryField == null){
            throw new PrimaryKeyNotPresentException("Unable to retrieve the object as no Primary key is present");
        }
        query.append(primaryField.getName()).append(" = ?;");
        return query.toString();
    }

    private static String buildRemoveQuery(Class<?> clazz) throws PrimaryKeyNotPresentException {
        StringBuilder query = new StringBuilder("DELETE FROM ");
        query.append(clazz.getSimpleName().toLowerCase()).append(" WHERE ");
        Field primaryField = TableUtility.fetchPrimaryKeyForEntity(clazz);

        if(primaryField == null){
            throw new PrimaryKeyNotPresentException("Unable to retrieve the object as no Primary key is present");
        }
        query.append(primaryField.getName()).append(" = ?;");
        return query.toString();
    }

}
