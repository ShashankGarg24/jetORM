package org.jetORM;

import org.jetORM.config.DatabaseConnectionManager;
import org.jetORM.schema.TableGenerator;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        Connection connection = DatabaseConnectionManager.getConnectionInstance("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost/demo?", "root", "rootshl");
        TableGenerator tableGenerator = new TableGenerator(connection);
        tableGenerator.generateDatabaseTablesFromClasses();
    }
}