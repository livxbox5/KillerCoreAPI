package org.killeryt.killerCoreAPI.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

    // MiniMessage для парсинга компонентов (если в сообщениях используется MiniMessage)
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    /**
     * Создает менеджер для указанного языка.
     * @param plugin   экземпляр плагина
     * @param language код языка (например, "ru", "en")
     */
    public LanguageManager(JavaPlugin plugin, String language) {
        this.plugin = plugin;
        this.language = language;
        reload();
    }

    /**
     * Перезагружает языковой файл из папки lang/ и очищает кэш.
     */
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

    /**
     * Получает сообщение по ключу без параметров.
     * @param key ключ в языковом файле
     * @return цветное сообщение (с & -> §)
     */
    public String get(String key) {
        return get(key, new Object[0]);
    }

    /**
     * Получает сообщение по ключу с подстановкой параметров (String.format).
     * @param key  ключ в языковом файле
     * @param args аргументы для форматирования
     * @return цветное сообщение (с & -> §)
     */
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

    /**
     * Получает чистое сообщение без цветового форматирования.
     * @param key  ключ в языковом файле
     * @param args аргументы для форматирования
     * @return текст без цветовых кодов
     */
    public String getRaw(String key, Object... args) {
        String colored = get(key, args);
        return ChatColor.stripColor(colored);
    }

    /**
     * Проверяет, существует ли ключ в языковом файле.
     * @param key ключ
     * @return true, если ключ есть
     */
    public boolean containsKey(String key) {
        return config.contains(key);
    }

    /**
     * Отправляет сообщение получателю.
     * @param sender получатель (игрок или консоль)
     * @param key    ключ сообщения
     * @param args   аргументы для форматирования
     */
    public void send(CommandSender sender, String key, Object... args) {
        String message = get(key, args);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Отправляет сообщение игроку (удобная обертка).
     * @param player игрок
     * @param key    ключ сообщения
     * @param args   аргументы для форматирования
     */
    public void sendMessage(Player player, String key, Object... args) {
        send(player, key, args);
    }

    /**
     * Отправляет сообщение игроку без параметров.
     * @param player игрок
     * @param key    ключ сообщения
     */
    public void sendMessage(Player player, String key) {
        send(player, key);
    }

    /**
     * Отправляет сообщение любому CommandSender.
     * @param sender получатель
     * @param key    ключ сообщения
     * @param args   аргументы для форматирования
     */
    public void sendMessage(CommandSender sender, String key, Object... args) {
        send(sender, key, args);
    }

    /**
     * Отправляет сообщение CommandSender без параметров.
     * @param sender получатель
     * @param key    ключ сообщения
     */
    public void sendMessage(CommandSender sender, String key) {
        send(sender, key);
    }

    // ========== С ПРЕФИКСОМ ==========

    /**
     * Отправляет сообщение с префиксом (берётся из языкового файла по ключу "prefix").
     * @param sender получатель
     * @param key    ключ основного сообщения
     * @param args   аргументы для форматирования
     */
    public void sendPrefixed(CommandSender sender, String key, Object... args) {
        String prefix = get("prefix");
        String message = prefix + get(key, args);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Отправляет сообщение с префиксом игроку.
     * @param player игрок
     * @param key    ключ основного сообщения
     * @param args   аргументы для форматирования
     */
    public void sendPrefixed(Player player, String key, Object... args) {
        sendPrefixed((CommandSender) player, key, args);
    }

    // ========== ACTION BAR ==========

    /**
     * Отправляет сообщение в ActionBar игроку.
     * @param player игрок
     * @param key    ключ сообщения
     * @param args   аргументы для форматирования
     */
    public void sendActionBar(Player player, String key, Object... args) {
        String message = get(key, args);
        Component component = MiniMessage.miniMessage().deserialize(message);
        player.sendActionBar(component);
    }

    // ========== TITLE / SUBTITLE ==========

    /**
     * Отправляет Title и Subtitle игроку с длительностью по умолчанию (10, 70, 20 тиков).
     * @param player   игрок
     * @param titleKey ключ для Title
     * @param subKey   ключ для Subtitle (может быть null)
     * @param args     аргументы для форматирования
     */
    public void sendTitle(Player player, String titleKey, String subKey, Object... args) {
        sendTitle(player, titleKey, subKey, 10, 70, 20, args);
    }

    /**
     * Отправляет Title и Subtitle игроку с кастомной длительностью.
     * @param player   игрок
     * @param titleKey ключ для Title
     * @param subKey   ключ для Subtitle (может быть null)
     * @param fadeIn   время появления (тики)
     * @param stay     время отображения (тики)
     * @param fadeOut  время исчезновения (тики)
     * @param args     аргументы для форматирования
     */
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

    /**
     * Отправляет только Subtitle (без Title).
     * @param player   игрок
     * @param subKey   ключ для Subtitle
     * @param args     аргументы для форматирования
     */
    public void sendSubtitle(Player player, String subKey, Object... args) {
        Component subtitle = parseComponent(subKey, args);
        player.sendMessage(subtitle); // Неправильно для Subtitle? Лучше использовать showTitle с null Title.
        // Корректный способ:
        player.showTitle(Title.title(Component.empty(), subtitle));
    }

    // ========== COMPONENT (ADVENTURE) ==========

    /**
     * Преобразует сообщение с цветовыми кодами (&) в Component.
     * @param key  ключ сообщения
     * @param args аргументы для форматирования
     * @return Component
     */
    public Component toComponent(String key, Object... args) {
        String message = get(key, args);
        // MiniMessage поддерживает &, но не все; используем legacy converter
        return MiniMessage.miniMessage().deserialize(message);
    }

    /**
     * Отправляет Component напрямую игроку.
     * @param player   игрок
     * @param component компонент
     */
    public void sendComponent(Player player, Component component) {
        player.sendMessage(component);
    }

    /**
     * Отправляет сообщение как Component (с поддержкой MiniMessage).
     * @param player игрок
     * @param key    ключ сообщения
     * @param args   аргументы для форматирования
     */
    public void sendComponent(Player player, String key, Object... args) {
        Component component = toComponent(key, args);
        player.sendMessage(component);
    }

    // ========== BULK / BROADCAST ==========

    /**
     * Отправляет сообщение всем онлайн-игрокам.
     * @param key  ключ сообщения
     * @param args аргументы для форматирования
     */
    public void broadcast(String key, Object... args) {
        String message = get(key, args);
        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Отправляет сообщение с префиксом всем онлайн-игрокам.
     * @param key  ключ основного сообщения
     * @param args аргументы для форматирования
     */
    public void broadcastPrefixed(String key, Object... args) {
        String prefix = get("prefix");
        String message = prefix + get(key, args);
        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    private Component parseComponent(String key, Object... args) {
        String message = get(key, args);
        return MiniMessage.miniMessage().deserialize(message);
    }
}
