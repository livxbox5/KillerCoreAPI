package org.killeryt.killerCoreAPI.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.killeryt.killerCoreAPI.utils.ColorUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Абстрактный класс для создания GUI-инвентарей.
 */
public abstract class InventoryBuilder implements InventoryHolder {

    protected final JavaPlugin plugin;
    protected final Inventory inventory;
    protected final Map<Integer, Consumer<InventoryClickEvent>> clickHandlers = new HashMap<>();

    public InventoryBuilder(JavaPlugin plugin, int size, String title) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, size, ColorUtils.color(title));
    }

    public abstract void init();

    public void open(Player player) {
        init();
        player.openInventory(inventory);
    }

    protected void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> handler) {
        inventory.setItem(slot, item);
        if (handler != null) clickHandlers.put(slot, handler);
    }

    protected void setItem(int slot, ItemStack item) {
        setItem(slot, item, null);
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Consumer<InventoryClickEvent> handler = clickHandlers.get(event.getSlot());
        if (handler != null) handler.accept(event);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}