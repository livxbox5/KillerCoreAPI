package org.killeryt.killerCoreAPI.gui.dynamic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.killeryt.killerCoreAPI.gui.action.ActionRegistry;
import org.killeryt.killerCoreAPI.gui.action.GuiAction;
import org.killeryt.killerCoreAPI.gui.menu.GuiMenu;
import org.killeryt.killerCoreAPI.utils.ColorUtils;

import java.util.List;
import java.util.Map;

public class DynamicGui implements GuiMenu, InventoryHolder {

    private final String name;
    private final String title;
    private final int size;
    private final List<GuiItem> items;
    private Inventory inventory;

    public DynamicGui(String name, String title, int size, List<GuiItem> items) {
        this.name = name;
        this.title = ColorUtils.color(title);
        this.size = size;
        this.items = items;
    }

    @Override
    public String getName() { return name; }

    @Override
    public void open(Player player, Object... args) {
        Inventory inv = Bukkit.createInventory(this, size, title);
        for (GuiItem item : items) {
            inv.setItem(item.getSlot(), item.toItemStack());
        }
        player.openInventory(inv);
        this.inventory = inv;
    }

    @Override
    public void handleClick(Player player, InventoryClickEvent event) {
        int slot = event.getSlot();
        for (GuiItem item : items) {
            if (item.getSlot() == slot) {
                for (Map<String, Object> actionData : item.getActions()) {
                    String type = (String) actionData.get("type");
                    GuiAction action = ActionRegistry.getAction(type);
                    if (action != null) {
                        action.execute(player, event, actionData);
                    }
                }
                break;
            }
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}