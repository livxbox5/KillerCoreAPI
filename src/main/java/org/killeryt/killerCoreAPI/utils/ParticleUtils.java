package org.killeryt.killerCoreAPI.utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ParticleUtils {

    // ==================== БАЗОВЫЕ МЕТОДЫ ====================

    public static void spawnParticle(Player player, Location loc, Particle particle, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        player.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, extra);
    }

    public static void spawnParticle(World world, Location loc, Particle particle, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        world.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, extra);
    }

    public static void spawnColoredParticle(Player player, Location loc, Color color, int count) {
        player.spawnParticle(Particle.DUST, loc, count, 0, 0, 0, 0, new Particle.DustOptions(color, 1.0f));
    }

    public static void spawnColoredParticle(World world, Location loc, Color color, int count) {
        world.spawnParticle(Particle.DUST, loc, count, 0, 0, 0, 0, new Particle.DustOptions(color, 1.0f));
    }

    // ==================== ЛИНИЯ ====================

    public static void spawnLine(Player player, Location from, Location to, Particle particle, int density) {
        spawnLine(player, from, to, particle, density, null);
    }

    public static void spawnLine(Player player, Location from, Location to, Particle particle, int density, Color color) {
        double distance = from.distance(to);
        double step = distance / density;
        for (double t = 0; t <= distance; t += step) {
            Location loc = from.clone().add(to.clone().subtract(from).multiply(t / distance));
            if (color != null) {
                spawnColoredParticle(player, loc, color, 1);
            } else {
                spawnParticle(player, loc, particle, 1, 0, 0, 0, 0);
            }
        }
    }

    // ==================== СФЕРА ====================

    public static void spawnSphere(Player player, Location center, double radius, Particle particle, int points) {
        spawnSphere(player, center, radius, particle, points, null);
    }

    public static void spawnSphere(Player player, Location center, double radius, Particle particle, int points, Color color) {
        for (int i = 0; i < points; i++) {
            double theta = 2 * Math.PI * Math.random();
            double phi = Math.acos(2 * Math.random() - 1);
            double x = center.getX() + radius * Math.sin(phi) * Math.cos(theta);
            double y = center.getY() + radius * Math.sin(phi) * Math.sin(theta);
            double z = center.getZ() + radius * Math.cos(phi);
            Location loc = new Location(center.getWorld(), x, y, z);
            if (color != null) {
                spawnColoredParticle(player, loc, color, 1);
            } else {
                spawnParticle(player, loc, particle, 1, 0, 0, 0, 0);
            }
        }
    }

    // ==================== КУБ ====================

    public static void spawnCube(Player player, Location center, double size, Particle particle, int density) {
        spawnCube(player, center, size, particle, density, null);
    }

    public static void spawnCube(Player player, Location center, double size, Particle particle, int density, Color color) {
        double half = size / 2;
        for (double x = -half; x <= half; x += size / density) {
            for (double y = -half; y <= half; y += size / density) {
                for (double z = -half; z <= half; z += size / density) {
                    // Только на поверхности
                    if (Math.abs(x) < half && Math.abs(y) < half && Math.abs(z) < half) continue;
                    Location loc = center.clone().add(x, y, z);
                    if (color != null) {
                        spawnColoredParticle(player, loc, color, 1);
                    } else {
                        spawnParticle(player, loc, particle, 1, 0, 0, 0, 0);
                    }
                }
            }
        }
    }

    // ==================== ВСЕМ ИГРОКАМ В РАДИУСЕ ====================

    public static void spawnForNearby(Location center, double radius, Particle particle, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        for (Player p : center.getWorld().getPlayers()) {
            if (p.getLocation().distance(center) <= radius) {
                spawnParticle(p, center, particle, count, offsetX, offsetY, offsetZ, extra);
            }
        }
    }

    public static void spawnColoredForNearby(Location center, double radius, Color color, int count) {
        for (Player p : center.getWorld().getPlayers()) {
            if (p.getLocation().distance(center) <= radius) {
                spawnColoredParticle(p, center, color, count);
            }
        }
    }
}