package org.killeryt.killerCoreAPI.utils.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultEconomyManager {

    private final JavaPlugin plugin;
    private Economy economy = null;

    public VaultEconomyManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setupEconomy();
    }

    private void setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("§cVault не найден! Экономика отключена.");
            return;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            economy = rsp.getProvider();
            plugin.getLogger().info("§aVault Economy успешно подключён!");
        }
    }

    public boolean hasEnough(Player player, double amount) {
        return economy != null && economy.has(player, amount);
    }

    public boolean withdraw(Player player, double amount) {
        if (economy == null) return false;
        if (hasEnough(player, amount)) {
            economy.withdrawPlayer(player, amount);
            return true;
        }
        return false;
    }

    public void deposit(Player player, double amount) {
        if (economy != null) {
            economy.depositPlayer(player, amount);
        }
    }

    public double getBalance(Player player) {
        return economy != null ? economy.getBalance(player) : 0.0;
    }

    public boolean isEnabled() {
        return economy != null;
    }
}