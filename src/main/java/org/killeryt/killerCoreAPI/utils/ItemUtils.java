package org.killeryt.killerCoreAPI.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    public static ItemStack createGlass(String colorName) {
        Material glass = switch (colorName.toUpperCase()) {
            case "RED" -> Material.RED_STAINED_GLASS_PANE;
            case "GREEN" -> Material.GREEN_STAINED_GLASS_PANE;
            case "YELLOW" -> Material.YELLOW_STAINED_GLASS_PANE;
            default -> Material.GRAY_STAINED_GLASS_PANE;
        };
        // ItemBuilder теперь доступен
        return new ItemBuilder(glass).name(" ").build();
    }
}