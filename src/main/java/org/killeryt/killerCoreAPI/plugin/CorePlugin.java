package org.killeryt.killerCoreAPI.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.killeryt.killerCoreAPI.KillerCoreAPI;
import org.killeryt.killerCoreAPI.utils.ConfigGenerator;
import org.killeryt.killerCoreAPI.utils.DebugUtils;
import org.killeryt.killerCoreAPI.utils.message.LanguageManager;

public abstract class CorePlugin extends JavaPlugin {

    protected DebugUtils debug;
    protected LanguageManager languageManager;
    protected ConfigGenerator configGenerator;

    @Override
    public void onEnable() {
        // Генерация конфигов (копирование ресурсов)
        configGenerator = new ConfigGenerator(this);
        configGenerator.generateAllConfigs();

        // Инициализация отладки – используем синглтон
        debug = DebugUtils.getInstance();
        debug.initialize(getLogger(), getConfig());
        debug.info("CoreAPI инициализирован для плагина " + getName());

        // Языковой менеджер
        languageManager = new LanguageManager(this, "ru");

        // Инициализация GUI через CoreAPI
        KillerCoreAPI.initGui(this);
    }

    @Override
    public void onDisable() {
        if (debug != null) {
            debug.info("CoreAPI выключен для плагина " + getName());
        }
    }

    public DebugUtils getDebug() { return debug; }
    public LanguageManager getLanguageManager() { return languageManager; }
    public ConfigGenerator getConfigGenerator() { return configGenerator; }
}