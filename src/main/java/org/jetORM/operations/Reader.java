package org.jetORM.operations;

import org.jetORM.config.DbLogger;
import org.jetORM.exceptions.PrimaryKeyNotPresentException;
import org.jetORM.utils.TableUtility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Reader {

    private static final DbLogger logger = DbLogger.getInstance();

    public static <T> T read(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException, IllegalAccessException, SQLException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String readQuery = buildQuery(clazz, id);
        logger.info(readQuery);
        return TableUtility.executeRead(readQuery, clazz, id);
    }

    private static String buildQuery(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException, IllegalAccessException {
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(clazz.getSimpleName().toLowerCase()).append(" WHERE ");
        Field primaryField = TableUtility.fetchPrimaryKeyForEntity(clazz);

        if(primaryField == null){
            throw new PrimaryKeyNotPresentException("Unable to retrieve the object as no Primary key is present");
        }
        query.append(primaryField.getName()).append(" = ?;");
        return query.toString();
    }

}
