package org.jetORM.schema;

import org.jetORM.annotations.Entity;
import org.jetORM.config.DbLogger;
import org.jetORM.exceptions.PrimaryKeyNotPresentException;
import org.jetORM.exceptions.UnsupportedDataTypeException;
import org.jetORM.utils.TableUtility;
import org.reflections.Reflections;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Set;

public class TableGenerator {

    private static final DbLogger logger = DbLogger.getInstance();
    private final Reflections reflections;

    public TableGenerator(String path){
        reflections = new Reflections(path);
        generateDatabaseTablesFromClasses();
    }

    private void generateDatabaseTablesFromClasses(){
        Set<Class<?>> entities = fetchClasses();
        for(Class<?> entity : entities){
            try {
                logger.info("Class name -> " + entity);
                String createTableQuery = generateTableQuery(entity);
                logger.info(createTableQuery);
                TableUtility.executeInsert(createTableQuery);
            } catch (PrimaryKeyNotPresentException | UnsupportedDataTypeException | SQLException e){
                logger.error("Failed to create table for: " + e.getMessage());
            }
        }
        logger.info("Tables created successfully");
    }

    private Set<Class<?>> fetchClasses() {
        return reflections.getTypesAnnotatedWith(Entity.class);
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
        Field primaryField = TableUtility.fetchPrimaryKeyForEntity(entity);
        if(primaryField != null){
            return primaryField;
        }
        throw new PrimaryKeyNotPresentException("Primary Key not present for class : " + entity.getName());
    }

    private void appendQueryForFieldInTableQuery(Field field, StringBuilder tableQuery) throws UnsupportedDataTypeException {

        String sqlDataType = TableUtility.mapJavaDataTypeToSql(field.getType().getSimpleName());
        String fieldQuery = String.format("%s %s,", field.getName(), sqlDataType);
        tableQuery.append(fieldQuery);
    }
}
