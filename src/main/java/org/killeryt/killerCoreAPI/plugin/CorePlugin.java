package org.killeryt.killerCoreAPI.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.killeryt.killerCoreAPI.gui.GuiListener;
import org.killeryt.killerCoreAPI.utils.ConfigGenerator;
import org.killeryt.killerCoreAPI.utils.DebugUtils;
import org.killeryt.killerCoreAPI.utils.LanguageManager;

/**
 * Абстрактный класс для всех плагинов, использующих KillerCoreAPI.
 * Предоставляет готовые утилиты и автоматическую генерацию конфигов.
 */
public abstract class CorePlugin extends JavaPlugin {

    protected DebugUtils debug;
    protected LanguageManager languageManager;
    protected ConfigGenerator configGenerator;

    @Override
    public void onEnable() {
        // Генерация конфигов (копирование ресурсов)
        configGenerator = new ConfigGenerator(this);
        configGenerator.generateAllConfigs();

        // Инициализация отладки
        debug = new DebugUtils(this);
        debug.log("CoreAPI инициализирован для плагина " + getName());

        // Языковой менеджер
        languageManager = new LanguageManager(this, "ru");

        // Регистрируем общий GuiListener
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }

    @Override
    public void onDisable() {
        if (debug != null) debug.log("CoreAPI выключен для плагина " + getName());
    }

    public DebugUtils getDebug() { return debug; }
    public LanguageManager getLanguageManager() { return languageManager; }
    public ConfigGenerator getConfigGenerator() { return configGenerator; }
}