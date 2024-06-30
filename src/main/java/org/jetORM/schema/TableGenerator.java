package org.jetORM.schema;

import org.jetORM.annotations.Entity;
import org.jetORM.annotations.Id;
import org.jetORM.config.DbLogger;
import org.jetORM.exceptions.PrimaryKeyNotPresentException;
import org.jetORM.exceptions.UnsupportedDataTypeException;
import org.reflections.Reflections;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class TableGenerator {

    private static final Reflections reflections = new Reflections("org.jetORM");
    private static final DbLogger logger = DbLogger.getInstance();
    private final Connection connection;

    public TableGenerator(Connection connection){
        this.connection = connection;
    }

    public void generateDatabaseTablesFromClasses(){
        Set<Class<?>> entities = fetchClasses();
        for(Class<?> entity : entities){
            try {
                logger.info("Class name -> " + entity);
                String createTableQuery = generateTableQuery(entity);
                logger.info(createTableQuery);
                executeQuery(createTableQuery);
            } catch (PrimaryKeyNotPresentException | UnsupportedDataTypeException | SQLException e){
                logger.error("Failed to create table for: " + e.getMessage());
            }
            logger.info("Tables created successfully");
        }
    }

    private Set<Class<?>> fetchClasses() {
        return reflections.getTypesAnnotatedWith(Entity.class);
    }

    private void executeQuery(String createTableQuery) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
            preparedStatement.execute();
        }
    }

    private String generateTableQuery(Class<?> entity) throws PrimaryKeyNotPresentException, UnsupportedDataTypeException {

        Field primaryKey = checkAndFetchPrimaryKeyForEntity(entity);
        String className = entity.getSimpleName();
        Field[] classFields = entity.getDeclaredFields();

        StringBuilder tableQuery = new StringBuilder(String.format("CREATE TABLE `%s`(", className));
        for (Field field : classFields) {
            field.setAccessible(true);
            appendQueryForFieldInTableQuery(field, tableQuery);
        }

        tableQuery.append(String.format("PRIMARY KEY (%s));", primaryKey.getName()));
        return tableQuery.toString();
    }

    private Field checkAndFetchPrimaryKeyForEntity(Class<?> entity) throws PrimaryKeyNotPresentException {

        for (Field field : entity.getDeclaredFields()) {
            field.setAccessible(true);
            if(field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new PrimaryKeyNotPresentException("Primary Key not present for class : " + entity.getName());
    }

    private void appendQueryForFieldInTableQuery(Field field, StringBuilder tableQuery) throws UnsupportedDataTypeException {

        String sqlDataType = mapJavaDataTypeToSql(field.getType().getSimpleName());
        String fieldQuery = String.format("%s %s,", field.getName(), sqlDataType);
        tableQuery.append(fieldQuery);
    }

    private String mapJavaDataTypeToSql(String type) throws UnsupportedDataTypeException {

        switch(type) {
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
