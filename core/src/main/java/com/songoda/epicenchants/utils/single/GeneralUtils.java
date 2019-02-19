package com.songoda.epicenchants.utils.single;

import com.songoda.epicenchants.enums.EnchantResult;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
        return "enchant." + result.toString().toLowerCase().replace("_", "");
    }

    public static <X> X getRandomElement(Set<X> set) {
        int item = ThreadLocalRandom.current().nextInt(set.size());
        int i = 0;
        for (X obj : set) {
            if (i == item)
                return obj;
            i++;
        }
        return null;
    }

    public static int[] getSlots(String string) {
        return Arrays.stream(string.split(",")).filter(StringUtils::isNumeric).mapToInt(Integer::parseInt).toArray();
    }

    public static List<Integer> getSlotsList(String string) {
        return Arrays.stream(string.split(",")).filter(StringUtils::isNumeric).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
    }
}
