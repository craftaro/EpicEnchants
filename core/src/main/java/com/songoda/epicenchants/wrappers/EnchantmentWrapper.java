package com.songoda.epicenchants.wrappers;

import com.songoda.epicenchants.objects.LeveledModifier;
import lombok.Builder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Builder
public class EnchantmentWrapper {
    private LeveledModifier amplifier;
    private Enchantment enchantment;

    public int getAmplifier(int level, @NotNull Player user, @Nullable LivingEntity opponent) {
        return (int) amplifier.get(level, 0, user, opponent);
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }
}
