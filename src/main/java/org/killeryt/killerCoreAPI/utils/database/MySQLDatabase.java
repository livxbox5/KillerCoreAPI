package org.killeryt.killerCoreAPI.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySQLDatabase extends DatabaseManager {

    public MySQLDatabase(DatabaseConfig config, Logger logger) {
        super(config, logger);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ignored) {}
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                config.getHost(), config.getPort(), config.getDatabase());
        return DriverManager.getConnection(url, config.getUsername(), config.getPassword());
    }

    @Override
    public void createTables() throws SQLException {
        executeUpdate("""
            CREATE TABLE IF NOT EXISTS regions (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(64) NOT NULL UNIQUE,
                owner VARCHAR(36) NOT NULL,
                world VARCHAR(64) NOT NULL,
                x INT NOT NULL,
                y INT NOT NULL,
                z INT NOT NULL,
                radius INT NOT NULL,
                flags TEXT,
                members TEXT
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8
        """);
    }
}