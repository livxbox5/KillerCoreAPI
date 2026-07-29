package org.killeryt.killerCoreAPI.utils;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Генератор конфигурационных файлов из ресурсов JAR.
 */
public class ConfigGenerator {

    private final JavaPlugin plugin;

    public ConfigGenerator(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void generateAllConfigs() {
        plugin.getLogger().info(ColorUtils.color("&a[CoreAPI] Запуск генерации ресурсов..."));
        copyAllResourcesFromJar();
        plugin.getLogger().info(ColorUtils.color("&a[CoreAPI] Генерация завершена."));
    }

    private void copyAllResourcesFromJar() {
        File jarFile = getJarFile();
        if (jarFile == null) return;

        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (entry.isDirectory() || name.startsWith("META-INF") ||
                        name.endsWith(".class") || name.equals("plugin.yml") ||
                        name.equals("paper-plugin.yml")) {
                    continue;
                }

                File dest = new File(plugin.getDataFolder(), name);
                if (dest.exists()) continue;

                if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
                    plugin.getLogger().warning("Не удалось создать папку: " + dest.getParent());
                    continue;
                }

                try (InputStream in = plugin.getResource(name)) {
                    if (in != null) {
                        Files.copy(in, dest.toPath());
                        plugin.getLogger().info(ColorUtils.color("&aСкопировано: &f" + name));
                    }
                } catch (IOException e) {
                    plugin.getLogger().warning("Ошибка копирования: " + name);
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка чтения JAR: " + e.getMessage());
        }
    }

    private File getJarFile() {
        try {
            return new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (Exception e) {
            plugin.getLogger().severe("Не удалось получить JAR: " + e.getMessage());
            return null;
        }
    }
}