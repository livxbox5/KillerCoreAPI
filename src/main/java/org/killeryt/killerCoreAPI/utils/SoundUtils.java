package org.killeryt.killerCoreAPI.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SoundUtils {

    // ==================== ОДНОМУ ИГРОКУ ====================

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        if (player != null && player.isOnline()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public static void playSound(Player player, Sound sound) {
        playSound(player, sound, 1.0f, 1.0f);
    }

    public static void playSound(Player player, String soundName, float volume, float pitch) {
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            playSound(player, sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            // звук не найден
        }
    }

    // ==================== ВСЕМ ИГРОКАМ ====================

    public static void playSoundGlobal(Sound sound, float volume, float pitch) {
        for (Player p : org.bukkit.Bukkit.getOnlinePlayers()) {
            playSound(p, sound, volume, pitch);
        }
    }

    public static void playSoundGlobal(Sound sound) {
        playSoundGlobal(sound, 1.0f, 1.0f);
    }

    // ==================== В РАДИУСЕ ====================

    public static void playSoundAtLocation(Location loc, Sound sound, float volume, float pitch, double radius) {
        for (Player p : loc.getWorld().getPlayers()) {
            if (p.getLocation().distance(loc) <= radius) {
                p.playSound(loc, sound, volume, pitch);
            }
        }
    }

    public static void playSoundAtLocation(Location loc, Sound sound, double radius) {
        playSoundAtLocation(loc, sound, 1.0f, 1.0f, radius);
    }

    public static void playSoundAtLocation(Location loc, String soundName, double radius) {
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            playSoundAtLocation(loc, sound, radius);
        } catch (IllegalArgumentException ignored) {}
    }

    // ==================== КОЛЛЕКЦИЯ ИГРОКОВ ====================

    public static void playSoundForPlayers(Collection<? extends Player> players, Sound sound, float volume, float pitch) {
        for (Player p : players) {
            playSound(p, sound, volume, pitch);
        }
    }
}