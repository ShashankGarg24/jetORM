package org.jetORM.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static volatile Connection connectionInstance;


    public static Connection getConnectionInstance(String driverName, String jdbcUrl, String userName, String password) {
        try{
            if(connectionInstance == null) {
                synchronized (DatabaseConnectionManager.class) {
                    if(connectionInstance == null) {
                        System.out.println("Connection instance created");
                        connectionInstance = getDatabaseConnection(driverName, jdbcUrl, userName, password);
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connectionInstance;
    }



    private static Connection getDatabaseConnection(String driverName, String jdbcUrl, String userName, String password) throws ClassNotFoundException, SQLException {
        System.out.println(driverName);
        Class.forName(driverName);
        Connection connection = DriverManager.getConnection(jdbcUrl, userName, password);
        return connection;
    }

}
