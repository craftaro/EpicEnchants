package com.songoda.epicenchants.utils;

import org.bukkit.ChatColor;

public class Chat {
    public static String color(String input) {
        return format(input, "", null);
    }

    public static String format(String input, String placeholder, Object toReplace) {
        return ChatColor.translateAlternateColorCodes('&', input).replaceAll(placeholder, toReplace == null ? "" : toReplace.toString());
    }
}
