package cs5200project.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final String USER = "root";
    private static final String PASSWORD = "Northeastern5200DBMS";
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 3306;
    private static final String SCHEMA = "cs5200project";
    private static final String TIMEZONE = "UTC";

    private static DatabaseConfig instance = null;
    private Connection connection = null;

    private DatabaseConfig() {
        // Private constructor to prevent instantiation
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Properties connectionProperties = new Properties();
            connectionProperties.put("user", USER);
            connectionProperties.put("password", PASSWORD);
            connectionProperties.put("serverTimezone", TIMEZONE);

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found", e);
            }

            connection = DriverManager.getConnection(
                String.format(
                    "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true",
                    HOSTNAME,
                    PORT,
                    SCHEMA
                ),
                connectionProperties
            );
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
} 