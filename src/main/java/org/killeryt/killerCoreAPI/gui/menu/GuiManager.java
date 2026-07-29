package org.killeryt.killerCoreAPI.gui.menu;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {
    private static GuiManager instance;
    private final JavaPlugin plugin;
    private final Map<UUID, String> openGuis = new HashMap<>();

    private GuiManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static void init(JavaPlugin plugin) {
        if (instance == null) {
            instance = new GuiManager(plugin);
        }
    }

    public static GuiManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GuiManager не инициализирован!");
        }
        return instance;
    }

    public void openMenu(Player player, String menuName, Object... args) {
        GuiMenu menu = GuiRegistry.getInstance().getMenu(menuName);
        if (menu != null) {
            menu.open(player, args);
            setCurrentGui(player, menuName);
        } else {
            plugin.getLogger().warning("Меню не найдено: " + menuName);
        }
    }

    public void setCurrentGui(Player player, String menuName) {
        openGuis.put(player.getUniqueId(), menuName);
    }

    public String getCurrentGui(Player player) {
        return openGuis.get(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        openGuis.remove(player.getUniqueId());
    }

    public void openMainMenu(Player player) {
        openMenu(player, "MainMenuGui");
    }
}