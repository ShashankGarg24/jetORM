package org.jetORM;

import org.jetORM.config.DatabaseConnectionManager;
import org.jetORM.config.PropertyLoader;
import org.jetORM.schema.TableGenerator;

import java.sql.Connection;
import java.util.Properties;

public class JetOrm {

    public void configure(String path) {
        PropertyLoader propertyLoader = new PropertyLoader(System.getProperty("user.dir")+"\\src\\database.properties");
        Connection connection = DatabaseConnectionManager.getConnectionInstance(
                propertyLoader.get("database.driver.name"),
                propertyLoader.get("database.host"),
                propertyLoader.get("database.username"),
                propertyLoader.get("database.password"));
        new TableGenerator(connection, path);
    }

    public void save(){

    }

    public
}