package org.jetORM;

import org.jetORM.exceptions.ClassMismatchException;
import org.jetORM.exceptions.PrimaryKeyNotPresentException;
import org.jetORM.schema.TableGenerator;
import org.jetORM.schema.TableOperations;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class JetOrm {

    public void configure(String path) {
        new TableGenerator(path);
    }

    public void save(Class<?> clazz, Object object) throws ClassMismatchException, SQLException, IllegalAccessException {
        if(!clazz.isInstance(object)){
            throw new ClassMismatchException("Given class doesn't matches the object");
        }
        TableOperations.write(clazz, object);
    }

    public Object getById(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException, IllegalAccessException, InvocationTargetException, SQLException, InstantiationException, NoSuchMethodException {
        return TableOperations.read(clazz, id);
    }

    public void deleteById(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException, IllegalAccessException, InvocationTargetException, SQLException, InstantiationException, NoSuchMethodException {
        TableOperations.remove(clazz, id);
    }

}