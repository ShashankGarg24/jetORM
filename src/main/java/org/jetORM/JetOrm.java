package org.jetORM;

import org.jetORM.config.DatabaseConnectionManager;
import org.jetORM.config.PropertyLoader;
import org.jetORM.exceptions.ClassMismatchException;
import org.jetORM.operations.Writer;
import org.jetORM.schema.TableGenerator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

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

}