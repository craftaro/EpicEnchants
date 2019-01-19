package com.songoda.epicenchants.utils;

import java.util.concurrent.ThreadLocalRandom;

public class GeneralUtils {
    public static boolean chance(int chance) {
        return chance((double) chance);
    }

    public static boolean chance(double chance) {
        return ThreadLocalRandom.current().nextDouble(101) < chance;
    }
}
