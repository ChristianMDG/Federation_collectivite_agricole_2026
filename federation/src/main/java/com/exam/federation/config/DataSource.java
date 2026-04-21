package com.exam.federation.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

@Component
public class DataSource {

    private final Dotenv dotenv;

    public DataSource() {
        this.dotenv = Dotenv.load();
    }

    public Connection getConnection() {
        try {
            String jdbcURL = dotenv.get("JDBC_URL");
            String user = dotenv.get("USERNAME");
            String password = dotenv.get("PASSWORD");
            return DriverManager.getConnection(jdbcURL, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}