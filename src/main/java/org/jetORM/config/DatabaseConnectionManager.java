package org.jetORM.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseConnectionManager {

    private static final DbLogger logger = DbLogger.getInstance();
    private static volatile Connection connectionInstance;
    private static PropertyLoader propertyLoader;

    private DatabaseConnectionManager() throws SQLException, ClassNotFoundException {
        propertyLoader = new PropertyLoader(System.getProperty("user.dir")+"\\src\\database.properties");
        connectionInstance = getDatabaseConnection();
    }


    public static Connection getConnectionInstance() {
        try{
            if(connectionInstance == null) {
                synchronized (DatabaseConnectionManager.class) {
                    if(connectionInstance == null) {
                        new DatabaseConnectionManager();
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connectionInstance;
    }



    private static Connection getDatabaseConnection() throws ClassNotFoundException, SQLException {
        Class.forName(propertyLoader.get("database.driver.name"));
        Connection connection = DriverManager.getConnection(
                propertyLoader.get("database.host"),
                propertyLoader.get("database.username"),
                propertyLoader.get("database.password"));
        logger.info("Database connected");
        return connection;
    }

}
