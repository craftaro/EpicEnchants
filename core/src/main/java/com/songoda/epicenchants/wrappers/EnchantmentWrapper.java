package com.songoda.epicenchants.wrappers;

import com.songoda.epicenchants.objects.LeveledModifier;
import lombok.Builder;
import org.bukkit.enchantments.Enchantment;

@Builder
public class EnchantmentWrapper {
    private LeveledModifier amplifier;
    private Enchantment enchantment;

    public int getAmplifier(int level) {
        return (int) amplifier.get(level, 0, user, opponent);
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }
}
