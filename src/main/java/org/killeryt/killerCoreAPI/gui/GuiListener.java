package org.killeryt.killerCoreAPI.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Универсальный слушатель для обработки кликов по GUI, созданных через InventoryBuilder.
 */
public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getView().getTopInventory().getHolder();
        if (holder instanceof InventoryBuilder builder) {
            event.setCancelled(true);
            builder.handleClick(event);
        }
    }
}