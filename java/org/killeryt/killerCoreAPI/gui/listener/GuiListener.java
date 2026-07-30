package org.killeryt.killerCoreAPI.gui.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.killeryt.killerCoreAPI.gui.menu.GuiManager;
import org.killeryt.killerCoreAPI.gui.menu.GuiMenu;
import org.killeryt.killerCoreAPI.gui.menu.GuiRegistry;
import org.killeryt.killerCoreAPI.utils.DebugUtils;

public class GuiListener implements Listener {

    private final DebugUtils debug;

    public GuiListener(DebugUtils debug) {
        this.debug = debug;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory top = event.getView().getTopInventory();
        InventoryHolder holder = top.getHolder();

        debug.debug("Клик в инвентаре, holder=" + (holder != null ? holder.getClass().getSimpleName() : "null"));

        if (!(holder instanceof GuiMenu)) {
            return;
        }

        event.setCancelled(true);

        String currentGui = GuiManager.getInstance().getCurrentGui(player);
        if (currentGui != null) {
            GuiRegistry.getInstance().handleClick(player, currentGui, event);
        } else {
            debug.warning("Клик в GUI, но currentGui не установлен для игрока " + player.getName());
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory top = event.getView().getTopInventory();
        InventoryHolder holder = top.getHolder();

        if (holder instanceof GuiMenu) {
            event.setCancelled(true);
        }
    }
}