package org.jetORM.utils;

import org.jetORM.annotations.Id;
import org.jetORM.config.DatabaseConnectionManager;
import org.jetORM.exceptions.UnsupportedDataTypeException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableUtility {

    private static final Connection connection = DatabaseConnectionManager.getConnectionInstance();

    public static void executeInsert(String query) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.execute();
        }
    }

    public static <T> T executeRead(String query, Class<?> clazz, Object id) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setPreparedStatementParameter(preparedStatement, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return TableUtility.castResultToClassObject(rs, clazz);
            }
            return null;
        }
    }

    public static int executeUpdate(String query, Class<?> clazz, Object id) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setPreparedStatementParameter(preparedStatement, id);
            return preparedStatement.executeUpdate();
        }
    }

    public static Field fetchPrimaryKeyForEntity(Class<?> entity) {
        for (Field field : entity.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        return null;
    }

    public static <T> T castResultToClassObject(ResultSet rs, Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        T object = (T) clazz.getDeclaredConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = rs.getObject(fieldName);
            field.set(object, value);
        }
        return object;
    }

    private static void setPreparedStatementParameter(PreparedStatement pstmt, Object parameter) throws SQLException {
        if (parameter instanceof Integer) {
            pstmt.setInt(1, (Integer) parameter);
        } else if (parameter instanceof Long) {
            pstmt.setLong(1, (Long) parameter);
        } else if (parameter instanceof String) {
            pstmt.setString(1, (String) parameter);
        } else if (parameter instanceof Double) {
            pstmt.setDouble(1, (Double) parameter);
        } else if (parameter instanceof Float) {
            pstmt.setFloat(1, (Float) parameter);
        } else if (parameter instanceof Boolean) {
            pstmt.setBoolean(1, (Boolean) parameter);
        } else {
            throw new SQLException("Unsupported parameter type: " + parameter.getClass().getName());
        }
    }

    public static String mapJavaDataTypeToSql(String type) throws UnsupportedDataTypeException {

        switch (type) {
            case "Integer":
            case "int":
                return "INTEGER";

            case "Long":
            case "long":
                return "BIGINT";

            case "float":
            case "Float":
            case "Double":
            case "double":
                return "DECIMAL";

            case "String":
                return "VARCHAR(100)";

            case "Boolean":
            case "boolean":
                return "BOOLEAN";

            default:
                throw new UnsupportedDataTypeException("Datatype " + type + " not supported");
        }
    }
}