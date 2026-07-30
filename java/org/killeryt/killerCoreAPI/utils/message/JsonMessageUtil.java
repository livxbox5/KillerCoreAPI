package org.killeryt.killerCoreAPI.utils.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Утилиты для работы с интерактивными JSON-сообщениями (Adventure API).
 * Позволяют создавать кликабельные кнопки, подсказки, составные сообщения
 * и готовые меню для управления регионами и командами.
 */
public final class JsonMessageUtil {

    private JsonMessageUtil() {}

    // ========== Базовые методы ==========

    public static Component joinComponents(List<Component> components, Component separator) {
        if (components == null || components.isEmpty()) return Component.empty();
        Component result = components.get(0);
        for (int i = 1; i < components.size(); i++) {
            result = result.append(separator).append(components.get(i));
        }
        return result;
    }

    public static Component joinComponents(Component[] components, Component separator) {
        if (components == null || components.length == 0) return Component.empty();
        Component result = components[0];
        for (int i = 1; i < components.length; i++) {
            result = result.append(separator).append(components[i]);
        }
        return result;
    }

    public static Component createSuggestComponent(String text, String hoverText, String command) {
        Component component = Component.text(text);
        if (hoverText != null) {
            component = component.hoverEvent(HoverEvent.showText(Component.text(hoverText)));
        }
        if (command != null) {
            component = component.clickEvent(ClickEvent.suggestCommand(command));
        }
        return component;
    }

    public static Component createRunComponent(String text, String hoverText, String command) {
        Component component = Component.text(text);
        if (hoverText != null) {
            component = component.hoverEvent(HoverEvent.showText(Component.text(hoverText)));
        }
        if (command != null) {
            component = component.clickEvent(ClickEvent.runCommand(command));
        }
        return component;
    }

    public static void sendCompositeMessage(Player player, Component... components) {
        Component combined = Component.empty();
        for (Component c : components) {
            combined = combined.append(c);
        }
        player.sendMessage(combined);
    }

    public static Component text(String text) {
        return Component.text(text);
    }

    public static Component colored(String text, TextColor color) {
        return Component.text(text, color);
    }

    public static Component miniMessage(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    public static void sendFromLanguage(Player player, LanguageManager lang, String key, Object... args) {
        Component component = lang.toComponent(key, args);
        player.sendMessage(component);
    }

    // ========== Предопределённые кнопки ==========

    public static Component createSetPos1Button() {
        return createRunComponent("§b[🎯 Set Pos1]", "§7Установить первую позицию здесь", "/region pos1");
    }

    public static Component createSetPos2Button() {
        return createRunComponent("§b[🎯 Set Pos2]", "§7Установить вторую позицию здесь", "/region pos2");
    }

    public static Component createExpandButton() {
        return createSuggestComponent("§e[⬆ Expand]", "§7Нажмите, чтобы вставить команду /region expand", "/region expand ");
    }

    public static Component createClaimButton() {
        return createSuggestComponent("§a[□ Create Region]", "§7Нажмите, чтобы вставить команду /region claim\n§7Затем введите имя региона", "/region claim ");
    }

    public static Component createClearButton() {
        return createRunComponent("§c[🗑 Clear]", "§7Очистить позиции", "/region clear");
    }

    public static Component createHelpButton() {
        return createRunComponent("§6[? Help]", "§7Показать справку по командам", "/region help");
    }

    public static Component createRegionInfoButton(String regionName) {
        return createRunComponent("§a[📋 Info]", "§7Показать информацию о регионе " + regionName, "/region info " + regionName);
    }

    public static Component createAddMemberButton(String regionName) {
        return createSuggestComponent("§a[➕ Add Member]", "§7Добавить участника в регион " + regionName, "/region addmember " + regionName + " ");
    }

    public static Component createRemoveMemberButton(String regionName) {
        return createSuggestComponent("§c[➖ Remove Member]", "§7Удалить участника из региона " + regionName, "/region removemember " + regionName + " ");
    }

    public static Component createAddOwnerButton(String regionName) {
        return createSuggestComponent("§a[➕ Add Owner]", "§7Добавить владельца в регион " + regionName, "/region addowner " + regionName + " ");
    }

    public static Component createRemoveOwnerButton(String regionName) {
        return createSuggestComponent("§c[➖ Remove Owner]", "§7Удалить владельца из региона " + regionName, "/region removeowner " + regionName + " ");
    }

    public static Component createQuickExpandSuggestButton(int blocks, String direction) {
        return createSuggestComponent("§e[⬆ Expand " + direction + "]", "§7Нажмите, чтобы вставить команду расширения на " + blocks + " блоков " + direction, "/region expand " + blocks + " " + direction);
    }

    public static Component createQuickCreateButton(String regionName) {
        return createRunComponent("§a[🏷 Create]", "§7Создать регион " + regionName, "/region claim " + regionName);
    }

    public static Component createCreateAnotherButton() {
        return createSuggestComponent("§6[🔄 Create Another]", "§7Создать другой регион", "/region claim ");
    }

    // ========== Готовые меню ==========

    public static void sendPositionMenu(Player player, boolean hasPos1, boolean hasPos2) {
        String status = "§7Статус: " + (hasPos1 ? "§aPos1✓ " : "§cPos1✗ ") + (hasPos2 ? "§aPos2✓" : "§cPos2✗");
        player.sendMessage(Component.text(status));

        Component posLine = Component.empty()
                .append(createSetPos1Button()).append(Component.text(" "))
                .append(createSetPos2Button()).append(Component.text(" "))
                .append(createHelpButton()).append(Component.text(" "))
                .append(createClearButton());
        player.sendMessage(posLine);

        if (hasPos1 && hasPos2) {
            player.sendMessage(Component.text("§a✓ Обе позиции установлены! Доступные действия:"));
            Component actionLine = Component.empty()
                    .append(createClaimButton()).append(Component.text(" "))
                    .append(createExpandButton());
            player.sendMessage(actionLine);
        }
    }

    public static void sendSimpleMenu(Player player) {
        Component line = Component.empty()
                .append(createClaimButton()).append(Component.text(" "))
                .append(createExpandButton()).append(Component.text(" "))
                .append(createClearButton());
        player.sendMessage(line);
    }

    public static void sendAfterPositionMenu(Player player) {
        player.sendMessage(Component.text("§6=== Позиции установлены ==="));
        Component line = Component.empty()
                .append(createClaimButton()).append(Component.text(" "))
                .append(createExpandButton()).append(Component.text(" "))
                .append(createClearButton());
        player.sendMessage(line);
        player.sendMessage(Component.text("§7Нажмите §a[Create Region]§7 чтобы создать регион"));
        player.sendMessage(Component.text("§7Нажмите §e[Expand]§7 чтобы расширить регион"));
    }

    public static void sendExpandSuggestMenu(Player player) {
        player.sendMessage(Component.text("§6=== Расширение региона ==="));
        player.sendMessage(Component.text("§7Нажмите кнопку, чтобы вставить команду:"));
        Component line = Component.empty().append(createExpandButton());
        player.sendMessage(line);
        player.sendMessage(Component.text("§7Затем введите: §e<количество_блоков> <up|down>"));
        player.sendMessage(Component.text("§7Пример: §e10 up§7 или §e5 down"));
        player.sendMessage(Component.text("§7И нажмите §aEnter§7 для выполнения"));

        // Быстрые шаблоны
        player.sendMessage(Component.text("§7Быстрые шаблоны:"));
        Component quickLine = Component.empty()
                .append(createQuickExpandSuggestButton(5, "up")).append(Component.text(" "))
                .append(createQuickExpandSuggestButton(10, "up")).append(Component.text(" "))
                .append(createQuickExpandSuggestButton(5, "down")).append(Component.text(" "))
                .append(createQuickExpandSuggestButton(10, "down"));
        player.sendMessage(quickLine);
        player.sendMessage(Component.text("§7Нажмите на шаблон и затем Enter для выполнения"));
    }

    public static void sendClaimMenu(Player player) {
        player.sendMessage(Component.text("§6=== Создание региона ==="));
        player.sendMessage(Component.text("§7Нажмите кнопку, чтобы вставить команду:"));
        Component line = Component.empty().append(createClaimButton());
        player.sendMessage(line);
        player.sendMessage(Component.text("§7Затем введите имя региона и нажмите Enter"));
    }

    public static void sendRegionManagementMenu(Player player, String regionName) {
        player.sendMessage(Component.text("§6=== Управление регионом " + regionName + " ==="));

        Component mainActions = Component.empty()
                .append(createRegionInfoButton(regionName)).append(Component.text(" "))
                .append(createExpandButton());
        player.sendMessage(mainActions);

        player.sendMessage(Component.text("§7Управление участниками:"));
        Component memberActions = Component.empty()
                .append(createAddMemberButton(regionName)).append(Component.text(" "))
                .append(createRemoveMemberButton(regionName));
        player.sendMessage(memberActions);

        player.sendMessage(Component.text("§7Управление владельцами:"));
        Component ownerActions = Component.empty()
                .append(createAddOwnerButton(regionName)).append(Component.text(" "))
                .append(createRemoveOwnerButton(regionName));
        player.sendMessage(ownerActions);
    }

    public static void sendSuccessMenu(Player player, String regionName) {
        player.sendMessage(Component.text("§6=== Регион создан! ==="));
        player.sendMessage(Component.text("§a✓ Регион '§e" + regionName + "§a' успешно создан"));
        Component line = Component.empty()
                .append(createRegionInfoButton(regionName)).append(Component.text(" "))
                .append(createAddMemberButton(regionName)).append(Component.text(" "))
                .append(createCreateAnotherButton());
        player.sendMessage(line);
    }

    public static void sendQuickActionMenu(Player player) {
        Component line = Component.empty()
                .append(createClaimButton()).append(Component.text(" "))
                .append(createExpandButton()).append(Component.text(" "))
                .append(createClearButton());
        player.sendMessage(line);
    }
}