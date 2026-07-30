package org.killeryt.killerCoreAPI.utils.database;

import java.io.File;
import java.util.logging.Logger;

public class DatabaseFactory {

    public static DatabaseManager create(DatabaseConfig config, File dataFolder, Logger logger) {
        switch (config.getType()) {
            case SQLITE:
                return new SQLiteDatabase(config, dataFolder, logger);
            case MYSQL:
                return new MySQLDatabase(config, logger);
            case POSTGRESQL:
                return new PostgreSQLDatabase(config, logger);
            case H2:
                return new H2Database(config, dataFolder, logger);
            default:
                throw new IllegalArgumentException("Unsupported database type: " + config.getType());
        }
    }
}