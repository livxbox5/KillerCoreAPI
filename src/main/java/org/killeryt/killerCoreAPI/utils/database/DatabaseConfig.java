package org.killeryt.killerCoreAPI.utils.database;

import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseConfig {
    private final DatabaseType type;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final int poolSize;

    public DatabaseConfig(FileConfiguration config) {
        String typeStr = config.getString("database.type", "sqlite");
        this.type = DatabaseType.valueOf(typeStr.toUpperCase());
        this.host = config.getString("database.host", "localhost");
        this.port = config.getInt("database.port", 3306);
        this.database = config.getString("database.database", "minecraft");
        this.username = config.getString("database.username", "");
        this.password = config.getString("database.password", "");
        this.poolSize = config.getInt("database.pool-size", 10);
    }

    public DatabaseType getType() { return type; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getDatabase() { return database; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getPoolSize() { return poolSize; }
}