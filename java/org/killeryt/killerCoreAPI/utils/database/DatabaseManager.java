package org.killeryt.killerCoreAPI.utils.database;

import java.sql.*;
import java.util.logging.Logger;

public abstract class DatabaseManager {

    protected final DatabaseConfig config;
    protected final Logger logger;

    public DatabaseManager(DatabaseConfig config, Logger logger) {
        this.config = config;
        this.logger = logger;
    }

    public abstract Connection getConnection() throws SQLException;

    public void executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        }
    }

    public ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps.executeQuery();
    }

    public void closeResources(AutoCloseable... resources) {
        for (AutoCloseable r : resources) {
            if (r != null) {
                try {
                    r.close();
                } catch (Exception e) {
                    logger.warning("Failed to close resource: " + e.getMessage());
                }
            }
        }
    }

    public abstract void createTables() throws SQLException;
}