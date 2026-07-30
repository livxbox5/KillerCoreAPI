package org.killeryt.killerCoreAPI.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class PostgreSQLDatabase extends DatabaseManager {

    public PostgreSQLDatabase(DatabaseConfig config, Logger logger) {
        super(config, logger);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.severe("PostgreSQL JDBC driver not found!");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        String url = String.format("jdbc:postgresql://%s:%d/%s",
                config.getHost(), config.getPort(), config.getDatabase());
        return DriverManager.getConnection(url, config.getUsername(), config.getPassword());
    }

    @Override
    public void createTables() throws SQLException {
        executeUpdate("""
            CREATE TABLE IF NOT EXISTS regions (
                id SERIAL PRIMARY KEY,
                name VARCHAR(64) NOT NULL UNIQUE,
                owner VARCHAR(36) NOT NULL,
                world VARCHAR(64) NOT NULL,
                x INTEGER NOT NULL,
                y INTEGER NOT NULL,
                z INTEGER NOT NULL,
                radius INTEGER NOT NULL,
                flags TEXT,
                members TEXT
            )
        """);
    }
}