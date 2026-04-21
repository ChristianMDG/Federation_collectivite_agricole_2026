package com.exam.federation.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
public class DataSource {
            private final Dotenv dotenv;
             public DataSource() {
                this.dotenv = Dotenv.configure()
                        .ignoreIfMissing()
                        .load();
            }
            public Connection getConnection() {
            try {
                String jdbcURl = System.getenv("JDBC_URl");
                String user = System.getenv("USERNAME");
                String password = System.getenv("PASSWORD");
                return DriverManager.getConnection(jdbcURl, user, password);
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
