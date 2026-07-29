package org.killeryt.killerCoreAPI.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class NBTUtils {

    private final JavaPlugin plugin;

    public NBTUtils(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ==================== ITEM ====================

    public void setItemTag(ItemStack item, String key, String value) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        meta.getPersistentDataContainer().set(nk, PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }

    public void setItemTag(ItemStack item, String key, int value) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        meta.getPersistentDataContainer().set(nk, PersistentDataType.INTEGER, value);
        item.setItemMeta(meta);
    }

    public void setItemTag(ItemStack item, String key, double value) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        meta.getPersistentDataContainer().set(nk, PersistentDataType.DOUBLE, value);
        item.setItemMeta(meta);
    }

    public void setItemTag(ItemStack item, String key, boolean value) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        meta.getPersistentDataContainer().set(nk, PersistentDataType.BOOLEAN, value);
        item.setItemMeta(meta);
    }

    public String getItemString(ItemStack item, String key) {
        if (item == null || !item.hasItemMeta()) return null;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        return item.getItemMeta().getPersistentDataContainer().get(nk, PersistentDataType.STRING);
    }

    public Integer getItemInt(ItemStack item, String key) {
        if (item == null || !item.hasItemMeta()) return null;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        return item.getItemMeta().getPersistentDataContainer().get(nk, PersistentDataType.INTEGER);
    }

    public Double getItemDouble(ItemStack item, String key) {
        if (item == null || !item.hasItemMeta()) return null;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        return item.getItemMeta().getPersistentDataContainer().get(nk, PersistentDataType.DOUBLE);
    }

    public Boolean getItemBoolean(ItemStack item, String key) {
        if (item == null || !item.hasItemMeta()) return null;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        return item.getItemMeta().getPersistentDataContainer().get(nk, PersistentDataType.BOOLEAN);
    }

    public boolean hasItemTag(ItemStack item, String key) {
        if (item == null || !item.hasItemMeta()) return false;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        return item.getItemMeta().getPersistentDataContainer().has(nk);
    }

    public void removeItemTag(ItemStack item, String key) {
        if (item == null || !item.hasItemMeta()) return;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        item.getItemMeta().getPersistentDataContainer().remove(nk);
    }

    // ==================== ENTITY ====================

    public void setEntityTag(Entity entity, String key, String value) {
        if (entity == null) return;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        entity.getPersistentDataContainer().set(nk, PersistentDataType.STRING, value);
    }

    public String getEntityString(Entity entity, String key) {
        if (entity == null) return null;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        return entity.getPersistentDataContainer().get(nk, PersistentDataType.STRING);
    }

    public boolean hasEntityTag(Entity entity, String key) {
        if (entity == null) return false;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        return entity.getPersistentDataContainer().has(nk);
    }

    public void removeEntityTag(Entity entity, String key) {
        if (entity == null) return;
        NamespacedKey nk = new NamespacedKey(plugin, key);
        entity.getPersistentDataContainer().remove(nk);
    }
}