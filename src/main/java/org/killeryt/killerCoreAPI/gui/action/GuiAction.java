package org.killeryt.killerCoreAPI.gui.action;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

@FunctionalInterface
public interface GuiAction {
    void execute(Player player, InventoryClickEvent event, Map<String, Object> params);
}