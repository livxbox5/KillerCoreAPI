package org.killeryt.killerCoreAPI.gui.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public class GuiRegistry {
    private static GuiRegistry instance;
    private final Map<String, GuiMenu> menus = new HashMap<>();

    private GuiRegistry() {}

    public static GuiRegistry getInstance() {
        if (instance == null) {
            instance = new GuiRegistry();
        }
        return instance;
    }

    public void registerMenu(String name, GuiMenu menu) {
        menus.put(name, menu);
    }

    public GuiMenu getMenu(String name) {
        return menus.get(name);
    }

    public void handleClick(Player player, String menuName, InventoryClickEvent event) {
        GuiMenu menu = menus.get(menuName);
        if (menu != null) {
            menu.handleClick(player, event);
        }
    }
}