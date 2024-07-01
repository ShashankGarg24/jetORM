package org.jetORM;

import org.jetORM.exceptions.ClassMismatchException;
import org.jetORM.exceptions.PrimaryKeyNotPresentException;

public interface JetOrm {

    void configure(String path);

    void save(Class<?> clazz, Object object) throws ClassMismatchException, PrimaryKeyNotPresentException;

    Object getById(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException;

    void deleteById(Class<?> clazz, Object id) throws PrimaryKeyNotPresentException;
}
