package org.killeryt.killerCoreAPI.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DebugUtils {

    private final JavaPlugin plugin;
    private boolean debugEnabled = false;

    public DebugUtils(JavaPlugin plugin) {
        this.plugin = plugin;
        reload(plugin.getConfig());
    }

    public void reload(FileConfiguration config) {
        this.debugEnabled = config.getBoolean("debug", false);
    }

    public void log(String message) {
        if (!debugEnabled) return;
        plugin.getLogger().info(ColorUtils.color("&8[&bDEBUG&8] &7" + message));
    }

    public void console(String message) {
        if (!debugEnabled) return;
        plugin.getLogger().info(ColorUtils.color("[DEBUG] " + message));
    }

    public void player(CommandSender sender, String message) {
        if (!debugEnabled) return;
        if (sender instanceof Player player && player.hasPermission("coreapi.debug")) {
            player.sendMessage(ColorUtils.color("&8[&bDEBUG&8] &7" + message));
        }
    }

    // ===== НОВЫЕ МЕТОДЫ =====
    public void info(String message) {
        if (!debugEnabled) return;
        plugin.getLogger().info(ColorUtils.color("&8[&bINFO&8] &7" + message));
    }

    public void warning(String message) {
        if (!debugEnabled) return;
        plugin.getLogger().warning(ColorUtils.color("&8[&bWARN&8] &7" + message));
    }

    public void debug(String message) {
        log(message);
    }

    public boolean isEnabled() {
        return debugEnabled;
    }
}