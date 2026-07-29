package org.killeryt.killerCoreAPI.gui.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface GuiMenu {
    String getName();
    void open(Player player, Object... args);
    void handleClick(Player player, InventoryClickEvent event);
}