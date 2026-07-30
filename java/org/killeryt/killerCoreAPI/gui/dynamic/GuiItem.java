package org.killeryt.killerCoreAPI.gui.dynamic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.killeryt.killerCoreAPI.utils.ColorUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuiItem {
    private final int slot;
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final List<Map<String, Object>> actions;

    public GuiItem(int slot, Material material, String name, List<String> lore, List<Map<String, Object>> actions) {
        this.slot = slot;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.actions = actions;
    }

    public int getSlot() { return slot; }

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (name != null && !name.isEmpty()) meta.setDisplayName(ColorUtils.color(name));
            if (lore != null && !lore.isEmpty()) {
                meta.setLore(lore.stream().map(ColorUtils::color).collect(Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public List<Map<String, Object>> getActions() { return actions; }
}