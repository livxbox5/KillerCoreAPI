package org.killeryt.killerCoreAPI.gui.dynamic;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class GuiLoader {
    private final JavaPlugin plugin;

    public GuiLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Map<String, DynamicGui> loadAllGuis(File guiFolder) {
        Map<String, DynamicGui> guiMap = new HashMap<>();
        if (!guiFolder.exists()) {
            plugin.getLogger().warning("Папка GUI не найдена: " + guiFolder.getPath());
            return guiMap;
        }
        File[] files = guiFolder.listFiles((d, n) -> n.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                try {
                    DynamicGui gui = loadGui(file);
                    if (gui != null) {
                        guiMap.put(gui.getName(), gui);
                        plugin.getLogger().info("Загружен динамический GUI: " + gui.getName());
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Ошибка загрузки GUI из " + file.getName() + ": " + e.getMessage());
                }
            }
        }
        return guiMap;
    }

    public DynamicGui loadGui(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String name = file.getName().replace(".yml", "");
        String title = config.getString("gui-title", "&fМеню");
        int size = config.getInt("size", 27);

        List<GuiItem> items = new ArrayList<>();
        ConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection == null) continue;

                List<Integer> slots = parseSlots(key, itemSection);
                if (slots.isEmpty()) continue;

                Material material = getMaterial(itemSection);
                String displayName = itemSection.getString("name", "");
                List<String> lore = itemSection.getStringList("lore");
                List<Map<String, Object>> actions = parseActions(itemSection);

                for (int slot : slots) {
                    if (slot < 0 || slot >= size) continue;
                    items.add(new GuiItem(slot, material, displayName, lore, actions));
                }
            }
        }
        return new DynamicGui(name, title, size, items);
    }

    private List<Integer> parseSlots(String key, ConfigurationSection section) {
        List<Integer> result = new ArrayList<>();
        if (section.contains("slot")) {
            int slot = section.getInt("slot");
            if (slot >= 0) result.add(slot);
            return result;
        }
        if (section.contains("slots")) {
            Object slotsObj = section.get("slots");
            if (slotsObj instanceof List<?>) {
                for (Object obj : (List<?>) slotsObj) {
                    if (obj instanceof Number) {
                        result.add(((Number) obj).intValue());
                    } else if (obj instanceof String) {
                        result.addAll(parseRange((String) obj));
                    }
                }
            } else if (slotsObj instanceof String) {
                result.addAll(parseRange((String) slotsObj));
            }
            return result;
        }
        if (key.startsWith("slot-")) {
            try {
                int slot = Integer.parseInt(key.substring(5));
                if (slot >= 0) result.add(slot);
            } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    private List<Integer> parseRange(String range) {
        List<Integer> result = new ArrayList<>();
        if (range == null || range.isBlank()) return result;
        String[] parts = range.split("-");
        if (parts.length == 2) {
            try {
                int start = Integer.parseInt(parts[0].trim());
                int end = Integer.parseInt(parts[1].trim());
                if (start <= end) {
                    for (int i = start; i <= end; i++) result.add(i);
                }
            } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    private Material getMaterial(ConfigurationSection section) {
        String materialName = section.getString("material", "STONE");
        Material material = Material.matchMaterial(materialName);
        return material != null ? material : Material.STONE;
    }

    private List<Map<String, Object>> parseActions(ConfigurationSection section) {
        List<Map<String, Object>> actions = new ArrayList<>();
        if (section.contains("actions")) {
            for (Object actionObj : section.getList("actions", new ArrayList<>())) {
                if (actionObj instanceof Map<?, ?>) {
                    actions.add(new HashMap<>((Map<String, Object>) actionObj));
                }
            }
        }
        return actions;
    }
}