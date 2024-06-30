package org.jetORM;

import org.jetORM.config.DatabaseConnectionManager;
import org.jetORM.config.PropertyLoader;
import org.jetORM.schema.TableGenerator;

import java.sql.Connection;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        PropertyLoader propertyLoader = new PropertyLoader(System.getProperty("user.dir")+"\\src\\database.properties");
        Connection connection = DatabaseConnectionManager.getConnectionInstance(
                propertyLoader.get("database.driver.name"),
                propertyLoader.get("database.host"),
                propertyLoader.get("database.username"),
                propertyLoader.get("database.password"));
        TableGenerator tableGenerator = new TableGenerator(connection);
        tableGenerator.generateDatabaseTablesFromClasses();
    }
}