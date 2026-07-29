package org.killeryt.killerCoreAPI.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

/**
 * Менеджер языковых файлов.
 */
public class LanguageManager {

    private final JavaPlugin plugin;
    private FileConfiguration langConfig;
    private final String defaultLang;

    public LanguageManager(JavaPlugin plugin, String defaultLang) {
        this.plugin = plugin;
        this.defaultLang = defaultLang;
        reloadLanguage();
    }

    public void reloadLanguage() {
        String lang = plugin.getConfig().getString("lang", defaultLang);
        File langFile = new File(plugin.getDataFolder(), "lang/" + lang + ".yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang/" + lang + ".yml", false);
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    public String get(String path) {
        return ColorUtils.color(langConfig.getString(path, "§cСообщение не найдено: " + path));
    }

    public String get(String path, String defaultValue) {
        return ColorUtils.color(langConfig.getString(path, defaultValue));
    }
}