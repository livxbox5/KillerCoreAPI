package org.killeryt.killerCoreAPI.utils;

import net.md_5.bungee.api.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Утилита для работы с цветами в Minecraft.
 * Поддерживает: &a, &#RRGGBB, <#RRGGBB>текст<#RRGGBB>
 */
public class ColorUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>(.+?)<#([A-Fa-f0-9]{6})>");

    public static String color(String text) {
        if (text == null || text.isEmpty()) return "";

        // 1. Классические коды &a, &l и т.д.
        text = ChatColor.translateAlternateColorCodes('&', text);

        // 2. HEX цвета: &#RRGGBB
        Matcher hexMatcher = HEX_PATTERN.matcher(text);
        while (hexMatcher.find()) {
            String hex = hexMatcher.group(1);
            text = text.replace("&#" + hex, ChatColor.of("#" + hex).toString());
        }

        // 3. Простые градиенты: <#RRGGBB>текст<#RRGGBB>
        Matcher gradientMatcher = GRADIENT_PATTERN.matcher(text);
        while (gradientMatcher.find()) {
            String startColor = gradientMatcher.group(1);
            String content = gradientMatcher.group(2);
            text = text.replace(gradientMatcher.group(0), ChatColor.of("#" + startColor) + content);
        }

        return text;
    }
}