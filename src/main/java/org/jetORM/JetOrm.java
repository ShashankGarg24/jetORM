package org.jetORM;

import org.jetORM.exceptions.ClassMismatchException;
import org.jetORM.exceptions.PrimaryKeyNotPresentException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface JetOrm {

    void configure(String path);
    void save(Class<?> clazz, Object object) throws ClassMismatchException, SQLException, IllegalAccessException, NoSuchMethodException, PrimaryKeyNotPresentException, InstantiationException, InvocationTargetException;
    Object getById(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException, IllegalAccessException, InvocationTargetException, SQLException, InstantiationException, NoSuchMethodException;
    void deleteById(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException, IllegalAccessException, InvocationTargetException, SQLException, InstantiationException, NoSuchMethodException;
}
