package org.killeryt.killerCoreAPI.gui.dynamic;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.killeryt.killerCoreAPI.gui.menu.GuiRegistry;
import org.killeryt.killerCoreAPI.utils.DebugUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigurableGuiManager {
    private final JavaPlugin plugin;
    private final String resourcePath;
    private final Map<String, DynamicGui> guis = new HashMap<>();
    private final DebugUtils debug;

    public ConfigurableGuiManager(JavaPlugin plugin, String resourcePath, DebugUtils debug) {
        this.plugin = plugin;
        this.resourcePath = resourcePath;
        this.debug = debug;
    }

    public void loadAllGuis() {
        guis.clear();
        File guiFolder = new File(plugin.getDataFolder(), resourcePath);
        if (!guiFolder.exists()) {
            guiFolder.mkdirs();
            copyDefaultGuis(guiFolder);
        }
        GuiLoader loader = new GuiLoader(plugin);
        guis.putAll(loader.loadAllGuis(guiFolder));
        debug.info("Загружено " + guis.size() + " динамических GUI");

        // Регистрируем их в GuiRegistry
        for (DynamicGui gui : guis.values()) {
            GuiRegistry.getInstance().registerMenu(gui.getName(), gui);
        }
    }

    private void copyDefaultGuis(File targetFolder) {
        String[] defaults = {"MainMenuGui.yml", "BlockCreationGui.yml", "MyBlocksGui.yml", "RegionBlockEditorGui.yml"};
        for (String name : defaults) {
            String resourcePath = "modules/guis/" + name;
            try {
                plugin.saveResource(resourcePath, false);
            } catch (IllegalArgumentException e) {
                debug.warning("Дефолтный GUI не найден в ресурсах: " + resourcePath);
            }
        }
    }

    public boolean hasGui(String name) {
        return guis.containsKey(name);
    }

    public void openGui(Player player, String name, Object... args) {
        DynamicGui gui = guis.get(name);
        if (gui != null) {
            gui.open(player, args);
        } else {
            debug.warning("Динамический GUI не найден: " + name);
        }
    }

    public Map<String, DynamicGui> getGuis() {
        return guis;
    }

    public void clear() {
        guis.clear();
    }
}