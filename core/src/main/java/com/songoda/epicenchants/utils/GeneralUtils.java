package com.songoda.epicenchants.utils;

import java.util.concurrent.ThreadLocalRandom;

public class GeneralUtils {
    public static boolean chance(Double chance) {
        return ThreadLocalRandom.current().nextDouble(101) < chance;
    }
}
