package org.jetORM.utils;

import org.jetORM.config.DatabaseConnectionManager;
import org.reflections.Reflections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Executer {

    private static final Connection connection = DatabaseConnectionManager.getConnectionInstance();

    public static void execute(String query) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.execute();
        }
    }
}
