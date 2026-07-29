package org.killeryt.killerCoreAPI.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {
    private final JavaPlugin plugin;
    private final String language;
    private FileConfiguration config;
    private final Map<String, String> cache = new HashMap<>();

    public LanguageManager(JavaPlugin plugin, String language) {
        this.plugin = plugin;
        this.language = language;
        reload();
    }

    public void reload() {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) langFolder.mkdirs();

        File langFile = new File(langFolder, language + ".yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang/" + language + ".yml", false);
        }
        config = YamlConfiguration.loadConfiguration(langFile);
        cache.clear();
    }

    public String get(String key) {
        return get(key, new Object[0]);
    }

    public String get(String key, Object... args) {
        if (cache.containsKey(key)) {
            return String.format(cache.get(key), args);
        }
        String message = config.getString(key);
        if (message == null) {
            message = "&cMissing language key: " + key;
        }
        cache.put(key, message);
        return String.format(message, args);
    }

    public void send(CommandSender sender, String key, Object... args) {
        String message = get(key, args);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
