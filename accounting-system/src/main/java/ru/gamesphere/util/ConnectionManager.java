package ru.gamesphere.util;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    private ConnectionManager() {
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
