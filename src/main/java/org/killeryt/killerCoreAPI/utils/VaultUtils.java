package org.killeryt.killerCoreAPI.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@SuppressWarnings({"unused", "WeakerAccess"})
public class VaultUtils {

    private final JavaPlugin plugin;
    private Economy economy;
    private boolean enabled = false;

    public VaultUtils(JavaPlugin plugin) {
        this.plugin = plugin;
        setup();
    }

    private void setup() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("Vault не найден! Экономика недоступна.");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().warning("Нет зарегистрированного провайдера экономики!");
            return;
        }
        economy = rsp.getProvider();
        enabled = true;
        plugin.getLogger().info("Vault успешно подключён.");
    }

    public boolean isEnabled() { return enabled && economy != null; }

    public Economy getEconomy() { return economy; }

    public double getBalance(OfflinePlayer player) {
        if (!isEnabled()) return 0.0;
        try { return economy.getBalance(player); } catch (Exception e) { return 0.0; }
    }

    public double getBalance(Player player) { return getBalance((OfflinePlayer) player); }

    public boolean hasBalance(OfflinePlayer player, double amount) {
        if (!isEnabled()) return false;
        try { return economy.has(player, amount); } catch (Exception e) { return false; }
    }

    public boolean hasBalance(Player player, double amount) {
        return hasBalance((OfflinePlayer) player, amount);
    }

    public boolean deposit(OfflinePlayer player, double amount) {
        if (!isEnabled()) return false;
        try {
            economy.depositPlayer(player, amount);
            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Ошибка при депозите для " + player.getName(), e);
            return false;
        }
    }

    public boolean deposit(Player player, double amount) {
        return deposit((OfflinePlayer) player, amount);
    }

    public boolean withdraw(OfflinePlayer player, double amount) {
        if (!isEnabled()) return false;
        try {
            economy.withdrawPlayer(player, amount);
            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Ошибка при снятии для " + player.getName(), e);
            return false;
        }
    }

    public boolean withdraw(Player player, double amount) {
        return withdraw((OfflinePlayer) player, amount);
    }

    public String format(double amount) {
        if (!isEnabled()) return String.valueOf(amount);
        try { return economy.format(amount); } catch (Exception e) { return String.valueOf(amount); }
    }

    public String getCurrencyNamePlural() {
        if (!isEnabled()) return "coins";
        try { return economy.currencyNamePlural(); } catch (Exception e) { return "coins"; }
    }

    public String getCurrencyNameSingular() {
        if (!isEnabled()) return "coin";
        try { return economy.currencyNameSingular(); } catch (Exception e) { return "coin"; }
    }
}