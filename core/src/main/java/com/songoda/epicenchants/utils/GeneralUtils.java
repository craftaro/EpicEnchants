package com.songoda.epicenchants.utils;

import com.songoda.epicenchants.enums.EnchantResult;
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

    public static String getMessageFromResult(EnchantResult result) {
        switch (result) {
            case FAILURE:
                return "enchant.failure";
            case BROKEN_FAILURE:
                return "enchant.brokenfailure";
            case SUCCESS:
                return "enchant.success";
            case CONFLICT:
                return "enchant.conflict";
            case MAXED_OUT:
                return "enchant.maxedout";
            case ALREADY_APPLIED:
                return "enchant.alreadyapplied";
        }

        return "";
    }
}
