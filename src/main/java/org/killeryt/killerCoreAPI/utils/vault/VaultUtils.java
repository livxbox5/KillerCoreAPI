package org.killeryt.killerCoreAPI.utils.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Утилитный класс для работы с экономикой Vault.
 * Предоставляет простые методы для проверки баланса, снятия и зачисления средств.
 */
public class VaultUtils {

    private final JavaPlugin plugin;
    private Economy economy;

    /**
     * Конструктор. Инициализирует экономику через Vault.
     *
     * @param plugin экземпляр плагина (для логирования)
     */
    public VaultUtils(JavaPlugin plugin) {
        this.plugin = plugin;
        setupEconomy();
    }

    /**
     * Настраивает экономику, получая провайдера Vault.
     */
    private void setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("Vault not found! Economy features will be disabled.");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().warning("No economy provider found!");
            return;
        }
        economy = rsp.getProvider();
        plugin.getLogger().info("Vault economy hooked successfully.");
    }

    /**
     * Проверяет, доступна ли экономика.
     *
     * @return true, если экономика настроена и работает
     */
    public boolean isEnabled() {
        return economy != null;
    }

    // ======================== БАЛАНС ========================

    /**
     * Получает баланс игрока.
     *
     * @param player игрок
     * @return баланс, или 0, если экономика недоступна
     */
    public double getBalance(Player player) {
        if (!isEnabled() || player == null) return 0;
        return economy.getBalance(player);
    }

    /**
     * Получает баланс оффлайн-игрока.
     *
     * @param player оффлайн-игрок
     * @return баланс, или 0, если экономика недоступна
     */
    public double getBalance(OfflinePlayer player) {
        if (!isEnabled() || player == null) return 0;
        return economy.getBalance(player);
    }

    /**
     * Проверяет, достаточно ли у игрока средств.
     *
     * @param player игрок
     * @param amount требуемая сумма
     * @return true, если баланс >= amount
     */
    public boolean hasBalance(Player player, double amount) {
        if (!isEnabled() || player == null) return false;
        return economy.has(player, amount);
    }

    /**
     * Проверяет, достаточно ли у оффлайн-игрока средств.
     *
     * @param player оффлайн-игрок
     * @param amount требуемая сумма
     * @return true, если баланс >= amount
     */
    public boolean hasBalance(OfflinePlayer player, double amount) {
        if (!isEnabled() || player == null) return false;
        return economy.has(player, amount);
    }

    // ======================== СНЯТИЕ ========================

    /**
     * Снимает деньги с игрока.
     *
     * @param player игрок
     * @param amount сумма для снятия (должна быть > 0)
     * @return true, если операция прошла успешно
     */
    public boolean withdraw(Player player, double amount) {
        if (!isEnabled() || player == null || amount <= 0) return false;
        if (!economy.has(player, amount)) return false;
        economy.withdrawPlayer(player, amount);
        return true;
    }

    /**
     * Снимает деньги с оффлайн-игрока.
     *
     * @param player оффлайн-игрок
     * @param amount сумма для снятия
     * @return true, если операция прошла успешно
     */
    public boolean withdraw(OfflinePlayer player, double amount) {
        if (!isEnabled() || player == null || amount <= 0) return false;
        if (!economy.has(player, amount)) return false;
        economy.withdrawPlayer(player, amount);
        return true;
    }

    // ======================== ЗАЧИСЛЕНИЕ ========================

    /**
     * Зачисляет деньги игроку.
     *
     * @param player игрок
     * @param amount сумма для зачисления (должна быть > 0)
     * @return true, если операция прошла успешно
     */
    public boolean deposit(Player player, double amount) {
        if (!isEnabled() || player == null || amount <= 0) return false;
        economy.depositPlayer(player, amount);
        return true;
    }

    /**
     * Зачисляет деньги оффлайн-игроку.
     *
     * @param player оффлайн-игрок
     * @param amount сумма для зачисления
     * @return true, если операция прошла успешно
     */
    public boolean deposit(OfflinePlayer player, double amount) {
        if (!isEnabled() || player == null || amount <= 0) return false;
        economy.depositPlayer(player, amount);
        return true;
    }

    // ======================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ========================

    /**
     * Переводит деньги от одного игрока другому.
     *
     * @param from   отправитель
     * @param to     получатель
     * @param amount сумма перевода
     * @return true, если перевод выполнен успешно
     */
    public boolean transfer(OfflinePlayer from, OfflinePlayer to, double amount) {
        if (!isEnabled() || from == null || to == null || amount <= 0) return false;
        if (!economy.has(from, amount)) return false;
        economy.withdrawPlayer(from, amount);
        economy.depositPlayer(to, amount);
        return true;
    }

    /**
     * Переводит деньги с комиссией (процент от суммы).
     *
     * @param from        отправитель
     * @param to          получатель
     * @param amount      сумма перевода
     * @param taxPercent  процент комиссии (например, 5.0 = 5%)
     * @return true, если перевод выполнен успешно
     */
    public boolean transferWithTax(OfflinePlayer from, OfflinePlayer to, double amount, double taxPercent) {
        if (!isEnabled() || from == null || to == null || amount <= 0 || taxPercent < 0) return false;
        double tax = amount * (taxPercent / 100.0);
        double total = amount + tax;
        if (!economy.has(from, total)) return false;
        economy.withdrawPlayer(from, total);
        economy.depositPlayer(to, amount);
        // Комиссия никуда не зачисляется (можно передать в консоль или в отдельный кошелёк)
        return true;
    }

    /**
     * Форматирует сумму в соответствии с валютой сервера.
     *
     * @param amount сумма
     * @return отформатированная строка
     */
    public String format(double amount) {
        if (!isEnabled()) return String.valueOf(amount);
        return economy.format(amount);
    }
}
