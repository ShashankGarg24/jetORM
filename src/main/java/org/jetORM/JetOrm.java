package org.jetORM;

import org.jetORM.config.DatabaseConnectionManager;
import org.jetORM.config.PropertyLoader;
import org.jetORM.schema.TableGenerator;

import java.sql.Connection;
import java.util.Properties;

public class JetOrm {

    public void configure(String path) {
        Connection connection = DatabaseConnectionManager.getConnectionInstance();
        new TableGenerator(connection, path);
    }

    public void save(){

    }

}