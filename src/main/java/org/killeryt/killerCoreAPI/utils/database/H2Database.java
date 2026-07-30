package org.killeryt.killerCoreAPI.utils.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class H2Database extends DatabaseManager {

    private final File dbFile;

    public H2Database(DatabaseConfig config, File dataFolder, Logger logger) {
        super(config, logger);
        this.dbFile = new File(dataFolder, config.getDatabase() + ".h2");
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            logger.severe("H2 JDBC driver not found!");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:" + dbFile.getAbsolutePath(), "sa", "");
    }

    @Override
    public void createTables() throws SQLException {
        executeUpdate("""
            CREATE TABLE IF NOT EXISTS regions (
                id IDENTITY PRIMARY KEY,
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