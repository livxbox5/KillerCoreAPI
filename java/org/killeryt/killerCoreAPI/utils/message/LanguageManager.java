package org.killeryt.killerCoreAPI.utils.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер языковых сообщений с поддержкой:
 * - Форматирования через String.format
 * - Префиксов
 * - ActionBar, Title, Subtitle (Adventure API)
 * - Компонентных сообщений (MiniMessage)
 * - Получения чистого текста без форматирования
 * - Рассылки
 */
public class LanguageManager {

    private final JavaPlugin plugin;
    private final String language;
    private FileConfiguration config;
    private final Map<String, String> cache = new HashMap<>();

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public LanguageManager(JavaPlugin plugin, String language) {
        this.plugin = plugin;
        this.language = language;
        reload();
    }

    // ========== ЗАГРУЗКА И ПЕРЕЗАГРУЗКА ==========

    public void reload() {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        File langFile = new File(langFolder, language + ".yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang/" + language + ".yml", false);
        }
        config = YamlConfiguration.loadConfiguration(langFile);
        cache.clear();
    }

    public String getLanguage() {
        return language;
    }

    public boolean containsKey(String key) {
        return config.contains(key);
    }

    // ========== ПОЛУЧЕНИЕ СООБЩЕНИЙ ==========

    public String get(String key) {
        return get(key, new Object[0]);
    }

    public String get(String key, Object... args) {
        if (cache.containsKey(key)) {
            return String.format(cache.get(key), args);
        }
        String message = config.getString(key);
        if (message == null) {
            message = "&cMissing language key: " + key;
        }
        cache.put(key, message);
        return String.format(message, args);
    }

    public String getRaw(String key, Object... args) {
        String colored = get(key, args);
        return ChatColor.stripColor(colored);
    }

    public String getPrefix() {
        return get("prefix", new Object[0]);
    }

    public Component toComponent(String key, Object... args) {
        String message = get(key, args);
        return MiniMessage.miniMessage().deserialize(message);
    }

    // ========== ОТПРАВКА СООБЩЕНИЙ (базовые) ==========

    public void send(CommandSender sender, String key, Object... args) {
        String message = get(key, args);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendMessage(Player player, String key, Object... args) {
        send(player, key, args);
    }

    public void sendMessage(Player player, String key) {
        send(player, key);
    }

    public void sendMessage(CommandSender sender, String key, Object... args) {
        send(sender, key, args);
    }

    public void sendMessage(CommandSender sender, String key) {
        send(sender, key);
    }

    // ========== С ПРЕФИКСОМ ==========

    public void sendPrefixed(CommandSender sender, String key, Object... args) {
        String prefix = getPrefix();
        String message = prefix + get(key, args);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendPrefixed(Player player, String key, Object... args) {
        sendPrefixed((CommandSender) player, key, args);
    }

    public void sendPrefixed(CommandSender sender, String key) {
        sendPrefixed(sender, key, new Object[0]);
    }

    // ========== ACTION BAR ==========

    public void sendActionBar(Player player, String key, Object... args) {
        String message = get(key, args);
        Component component = MiniMessage.miniMessage().deserialize(message);
        player.sendActionBar(component);
    }

    public void sendActionBar(Player player, String key, int ticks, Object... args) {
        sendActionBar(player, key, args);
        if (ticks > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendActionBar(Component.empty());
                }
            }.runTaskLater(plugin, ticks);
        }
    }

    // ========== TITLE / SUBTITLE ==========

    public void sendTitle(Player player, String titleKey, String subKey, Object... args) {
        sendTitle(player, titleKey, subKey, 10, 70, 20, args);
    }

    public void sendTitle(Player player, String titleKey, String subKey,
                          int fadeIn, int stay, int fadeOut, Object... args) {
        Component title = titleKey != null ? parseComponent(titleKey, args) : null;
        Component subtitle = subKey != null ? parseComponent(subKey, args) : null;

        Title.Times times = Title.Times.times(
                Duration.ofMillis(fadeIn * 50L),
                Duration.ofMillis(stay * 50L),
                Duration.ofMillis(fadeOut * 50L)
        );

        Title titleObj = Title.title(title, subtitle, times);
        player.showTitle(titleObj);
    }

    public void sendSubtitle(Player player, String subKey, Object... args) {
        Component subtitle = parseComponent(subKey, args);
        player.showTitle(Title.title(Component.empty(), subtitle));
    }

    public void sendTitleOnly(Player player, String titleKey, Object... args) {
        sendTitle(player, titleKey, null, args);
    }

    // ========== COMPONENT (ADVENTURE) ==========

    public void sendComponent(Player player, Component component) {
        player.sendMessage(component);
    }

    public void sendComponent(Player player, String key, Object... args) {
        Component component = toComponent(key, args);
        player.sendMessage(component);
    }

    public void sendComponent(CommandSender sender, Component component) {
        sender.sendMessage(component);
    }

    // ========== BROADCAST (РАССЫЛКА) ==========

    public void broadcast(String key, Object... args) {
        String message = get(key, args);
        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void broadcastPrefixed(String key, Object... args) {
        String prefix = getPrefix();
        String message = prefix + get(key, args);
        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    // ========== ОТПРАВКА В КОНСОЛЬ ==========

    public void sendToConsole(String key, Object... args) {
        String message = get(key, args);
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    // ========== ОТПРАВКА С ЗАДЕРЖКОЙ ==========

    public void sendLater(Player player, String key, long delay, Object... args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                sendMessage(player, key, args);
            }
        }.runTaskLater(plugin, delay);
    }

    public void sendTitleLater(Player player, String titleKey, String subKey, long delay, Object... args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                sendTitle(player, titleKey, subKey, args);
            }
        }.runTaskLater(plugin, delay);
    }

    // ========== ИНТЕГРАЦИЯ С PLACEHOLDERAPI ==========

    public String parsePlaceholders(Player player, String text) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            try {
                return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, text);
            } catch (Exception e) {
                // Игнорируем
            }
        }
        return text;
    }

    public void sendWithPAPI(Player player, String key, Object... args) {
        String message = get(key, args);
        message = parsePlaceholders(player, message);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    // ========== ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ==========

    public void sendPrefixedWithPAPI(Player player, String key, Object... args) {
        String prefix = getPrefix();
        String message = prefix + get(key, args);
        message = parsePlaceholders(player, message);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendActionBarWithPAPI(Player player, String key, Object... args) {
        String message = get(key, args);
        message = parsePlaceholders(player, message);
        Component component = MiniMessage.miniMessage().deserialize(message);
        player.sendActionBar(component);
    }

    public void sendPrefixedComponent(Player player, String key, Object... args) {
        Component prefix = toComponent("prefix");
        Component message = toComponent(key, args);
        player.sendMessage(prefix.append(message));
    }

    public void sendActionBarLater(Player player, String key, long delay, Object... args) {
        sendActionBar(player, key, args);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendActionBar(Component.empty());
            }
        }.runTaskLater(plugin, delay);
    }

    public void sendTitleWithPAPI(Player player, String titleKey, String subKey, Object... args) {
        String title = parsePlaceholders(player, get(titleKey, args));
        String subtitle = subKey != null ? parsePlaceholders(player, get(subKey, args)) : "";
        player.showTitle(Title.title(
                MiniMessage.miniMessage().deserialize(title),
                subtitle.isEmpty() ? null : MiniMessage.miniMessage().deserialize(subtitle)
        ));
    }

    public String getPrefixed(String key, Object... args) {
        return getPrefix() + get(key, args);
    }

    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    private Component parseComponent(String key, Object... args) {
        String message = get(key, args);
        return MiniMessage.miniMessage().deserialize(message);
    }

    // ========== СТАТИЧЕСКИЙ ДОСТУП ==========

    private static LanguageManager instance;

    public static void init(JavaPlugin plugin, String language) {
        if (instance == null) {
            instance = new LanguageManager(plugin, language);
        }
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("LanguageManager не инициализирован! Вызовите LanguageManager.init()");
        }
        return instance;
    }
}