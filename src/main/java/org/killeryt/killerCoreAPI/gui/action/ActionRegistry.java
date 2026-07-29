package org.killeryt.killerCoreAPI.gui.action;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.killeryt.killerCoreAPI.gui.menu.GuiManager;

import java.util.HashMap;
import java.util.Map;

public class ActionRegistry {
    private static final Map<String, GuiAction> actions = new HashMap<>();

    public static void registerDefaults(JavaPlugin plugin) {
        register("OPEN_GUI", (p, e, params) -> {
            String guiName = (String) params.get("gui");
            if (guiName != null) GuiManager.getInstance().openMenu(p, guiName);
        });
        register("RUN_COMMAND", (p, e, params) -> {
            String cmd = (String) params.get("command");
            if (cmd != null) p.performCommand(cmd);
        });
        register("CONSOLE_COMMAND", (p, e, params) -> {
            String cmd = (String) params.get("command");
            if (cmd != null) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        });
        register("CLOSE", (p, e, params) -> p.closeInventory());
        register("TOGGLE_FLAG", (p, e, params) -> {
            // Может быть переопределён в плагине
        });
        register("CUSTOM", (p, e, params) -> { /* расширяется */ });
    }

    public static void register(String name, GuiAction action) {
        actions.put(name.toUpperCase(), action);
    }

    public static GuiAction getAction(String name) {
        return actions.get(name.toUpperCase());
    }
}