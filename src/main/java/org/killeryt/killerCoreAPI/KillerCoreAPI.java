package org.killeryt.killerCoreAPI;

import org.bukkit.plugin.java.JavaPlugin;
import org.killeryt.killerCoreAPI.gui.action.ActionRegistry;
import org.killeryt.killerCoreAPI.gui.dynamic.ConfigurableGuiManager;
import org.killeryt.killerCoreAPI.gui.listener.GuiListener;
import org.killeryt.killerCoreAPI.gui.menu.GuiManager;
import org.killeryt.killerCoreAPI.utils.DebugUtils;

import lombok.Getter;

@Getter
public final class KillerCoreAPI extends JavaPlugin {

    private static KillerCoreAPI instance;
    private static DebugUtils debugUtils;
    private static ConfigurableGuiManager configurableGuiManager;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("KillerCoreAPI загружен!");

        // Инициализация DebugUtils – используем синглтон
        debugUtils = DebugUtils.getInstance();
        debugUtils.initialize(getLogger(), getConfig());

        GuiManager.init(this);
    }

    @Override
    public void onDisable() {
        if (configurableGuiManager != null) {
            configurableGuiManager.clear();
        }
        getLogger().info("KillerCoreAPI выгружен.");
    }

    public static KillerCoreAPI getInstance() {
        return instance;
    }

    /**
     * Инициализация GUI-системы. Вызывается из CorePlugin.onEnable().
     */
    public static void initGui(JavaPlugin plugin) {
        // DebugUtils уже инициализирован в onEnable, но если вызывают до onEnable,
        // можно повторно инициализировать (но лучше вызывать после onEnable)
        if (debugUtils == null) {
            debugUtils = DebugUtils.getInstance();
            debugUtils.initialize(plugin.getLogger(), plugin.getConfig());
        }

        plugin.getServer().getPluginManager().registerEvents(new GuiListener(debugUtils), plugin);
        ActionRegistry.registerDefaults(plugin);
        configurableGuiManager = new ConfigurableGuiManager(plugin, "modules/guis", debugUtils);
        configurableGuiManager.loadAllGuis();
    }

    public static DebugUtils getDebugUtils() {
        return debugUtils;
    }

    public static ConfigurableGuiManager getConfigurableGuiManager() {
        return configurableGuiManager;
    }
}