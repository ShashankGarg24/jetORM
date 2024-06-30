package org.jetORM;

import org.jetORM.config.DbLogger;
import org.jetORM.exceptions.ClassMismatchException;
import org.jetORM.exceptions.PrimaryKeyNotPresentException;
import org.jetORM.schema.TableGenerator;
import org.jetORM.schema.TableOperations;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class JetOrmImpl implements JetOrm{

    private static final DbLogger logger = DbLogger.getInstance();

    @Override
    public void configure(String path) {
        new TableGenerator(path);
    }

    @Override
    public void save(Class<?> clazz, Object object) throws ClassMismatchException, PrimaryKeyNotPresentException {
        if(!clazz.isInstance(object)){
            throw new ClassMismatchException("Given class doesn't matches the object");
        }
        try {
            TableOperations.write(clazz, object);
        } catch (SQLException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public Object getById(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException {
        try {
            return TableOperations.read(clazz, id);
        } catch (IllegalAccessException | SQLException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteById(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException {
        try {
            TableOperations.remove(clazz, id);
        } catch (SQLException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

}