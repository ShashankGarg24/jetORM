package org.jetORM;

import org.jetORM.exceptions.ClassMismatchException;
import org.jetORM.exceptions.PrimaryKeyNotPresentException;
import org.jetORM.operations.Reader;
import org.jetORM.operations.Writer;
import org.jetORM.schema.TableGenerator;

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
        Writer.write(clazz, object);
    }

    public Object getById(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException, IllegalAccessException, InvocationTargetException, SQLException, InstantiationException, NoSuchMethodException {
        return Reader.read(clazz, id);
    }

}