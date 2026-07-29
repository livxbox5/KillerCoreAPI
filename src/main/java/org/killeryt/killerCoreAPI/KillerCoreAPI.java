package org.killeryt.killerCoreAPI;

import org.bukkit.plugin.java.JavaPlugin;
import org.killeryt.killerCoreAPI.plugin.*;
import org.killeryt.killerCoreAPI.utils.*;

import lombok.Getter;

@Getter
public final class KillerCoreAPI extends JavaPlugin {

    private static KillerCoreAPI instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("KillerCoreAPI загружен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("KillerCoreAPI выгружен.");
    }

    public static KillerCoreAPI getInstance() {
        return instance;
    }
}