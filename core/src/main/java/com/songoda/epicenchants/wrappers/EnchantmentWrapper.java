package com.songoda.epicenchants.wrappers;

import lombok.Builder;
import org.bukkit.enchantments.Enchantment;

@Builder
public class EnchantmentWrapper {
    private String amplifier;
    private Enchantment enchantment;

    public int getAmplifier(int tier) {
        return amplifier.isEmpty() ? 0 : Integer.parseInt(amplifier.replaceAll("\\{tier}", "" + tier));
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }
}
