package org.killeryt.killerCoreAPI.utils.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class Database {

    private static DatabaseManager manager;
    private static boolean initialized = false;

    private Database() {}

    /**
     * Инициализация БД из конфига плагина.
     * @param plugin плагин, в конфиге которого есть секция database
     */
    public static void init(JavaPlugin plugin) {
        if (initialized) return;
        DatabaseConfig config = new DatabaseConfig(plugin.getConfig());
        manager = DatabaseFactory.create(config, plugin.getDataFolder(), plugin.getLogger());
        try {
            manager.createTables();
            initialized = true;
            plugin.getLogger().info("Database initialized: " + config.getType());
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to initialize database: " + e.getMessage());
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }

    // ========== Основные методы ==========

    public static void executeUpdate(String sql, Object... params) throws SQLException {
        checkInit();
        manager.executeUpdate(sql, params);
    }

    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        checkInit();
        return manager.executeQuery(sql, params);
    }

    public static Connection getConnection() throws SQLException {
        checkInit();
        return manager.getConnection();
    }

    public static void closeResources(AutoCloseable... resources) {
        checkInit();
        manager.closeResources(resources);
    }

    private static void checkInit() {
        if (!initialized) {
            throw new IllegalStateException("Database not initialized! Call Database.init(plugin) first.");
        }
    }
}