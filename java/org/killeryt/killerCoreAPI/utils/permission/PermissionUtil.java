package org.killeryt.killerCoreAPI.utils.permission;

import org.bukkit.entity.Player;

/**
 * Утилиты для проверки прав игроков.
 */
public final class PermissionUtil {

    private PermissionUtil() {}

    /**
     * Проверяет, имеет ли игрок разрешение, с учётом административных прав.
     * @param player     игрок
     * @param permission строка разрешения (например, "regionmc.admin")
     * @return true, если игрок имеет право
     */
    public static boolean hasPermission(Player player, String permission) {
        if (player == null) return false;
        return player.isOp() ||
                player.hasPermission("*") ||
                player.hasPermission("regionmc.*") ||
                player.hasPermission("regionmc.admin") ||
                player.hasPermission(permission);
    }
}