package org.jetORM.schema;

import org.jetORM.annotations.Entity;
import org.jetORM.annotations.Id;
import org.jetORM.config.DatabaseConnectionManager;
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

    private final Connection connection;

    public TableGenerator(Connection connection){
        this.connection = connection;
    }
    public void generateDatabaseTablesFromClasses(){
        try{
            Set<Class<?>> entities = fetchClasses();
            System.out.println(entities);              /**test**/
            createAndExecuteTableQueriesForClasses(entities);
        }
        catch (PrimaryKeyNotPresentException | UnsupportedDataTypeException | SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private Set<Class<?>> fetchClasses() {
        return reflections.getTypesAnnotatedWith(Entity.class);
    }

    private void createAndExecuteTableQueriesForClasses(Set<Class<?>> entities) throws PrimaryKeyNotPresentException, UnsupportedDataTypeException, SQLException {
        for(Class<?> entity : entities){
            System.out.println("Class name -> " + entity);
            String createTableQuery = generateTableQuery(entity);
            System.out.println(createTableQuery);
            executeQuery(createTableQuery);
        }
    }

    private void executeQuery(String createTableQuery) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery);
        preparedStatement.execute();
        System.out.println("Table created successfully");
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
