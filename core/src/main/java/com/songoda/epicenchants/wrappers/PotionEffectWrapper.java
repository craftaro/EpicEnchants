package com.songoda.epicenchants.wrappers;

import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PotionEffectWrapper {
    private PotionEffect potionEffect;
    private double chance;

    public PotionEffectWrapper(PotionEffect potionEffect, double chance) {
        this.potionEffect = potionEffect;
        this.chance = chance;
    }

    public boolean test() {
        return ThreadLocalRandom.current().nextDouble(101) < chance;
    }

    public void perform(Player player) {
        player.addPotionEffect(potionEffect);
    }
}
