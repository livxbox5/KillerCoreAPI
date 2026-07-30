package org.killeryt.killerCoreAPI.utils.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SQLiteDatabase extends DatabaseManager {

    private final File dbFile;

    public SQLiteDatabase(DatabaseConfig config, File dataFolder, Logger logger) {
        super(config, logger);
        this.dbFile = new File(dataFolder, config.getDatabase() + ".db");
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            logger.severe("SQLite driver not found!");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
    }

    @Override
    public void createTables() throws SQLException {
        executeUpdate("""
            CREATE TABLE IF NOT EXISTS regions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
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