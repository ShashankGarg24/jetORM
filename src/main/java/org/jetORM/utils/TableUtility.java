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
import java.util.ArrayList;

public class TableUtility {

    private static final Connection connection = DatabaseConnectionManager.getConnectionInstance();

    public static void executeInsert(String query) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.execute();
        }
    }

    public static <T> T executeRead(String query, Class<?> clazz, Object id) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setPreparedStatementParameter(preparedStatement, id, 1);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return TableUtility.castResultToClassObject(rs, clazz);
            }
            return null;
        }
    }

    public static int executeDelete(String query, Class<?> clazz, Object id) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setPreparedStatementParameter(preparedStatement, id, 1);
            return preparedStatement.executeUpdate();
        }
    }

    public static void executeUpdate(String query, ArrayList<Object> values) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 1; i <= values.size(); i++) {
                setPreparedStatementParameter(preparedStatement, values.get(i - 1), i);
            }
            preparedStatement.executeUpdate();
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

    private static void setPreparedStatementParameter(PreparedStatement pstmt, Object parameter, int index) throws SQLException {
        if (parameter instanceof Integer) {
            pstmt.setInt(index, (Integer) parameter);
        } else if (parameter instanceof Long) {
            pstmt.setLong(index, (Long) parameter);
        } else if (parameter instanceof String) {
            pstmt.setString(index, (String) parameter);
        } else if (parameter instanceof Double) {
            pstmt.setDouble(index, (Double) parameter);
        } else if (parameter instanceof Float) {
            pstmt.setFloat(index, (Float) parameter);
        } else if (parameter instanceof Boolean) {
            pstmt.setBoolean(index, (Boolean) parameter);
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