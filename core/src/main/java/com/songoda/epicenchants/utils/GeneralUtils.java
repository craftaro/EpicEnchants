package com.songoda.epicenchants.utils;

import org.bukkit.ChatColor;

import java.util.concurrent.ThreadLocalRandom;

public class GeneralUtils {
    public static boolean chance(int chance) {
        return chance((double) chance);
    }

    public static boolean chance(double chance) {
        return ThreadLocalRandom.current().nextDouble(101) < chance;
    }

    public static String color(String input) {
        return format(input, "", null);
    }

    public static String format(String input, String placeholder, Object toReplace) {
        return ChatColor.translateAlternateColorCodes('&', input).replaceAll(placeholder, toReplace == null ? "" : toReplace.toString());
    }

    public static int getSlot(int row, int column) {
        if (column > 9 || row < 1) {
            return 0;
        }
        return (row - 1) * 9 + column - 1;
    }
}
