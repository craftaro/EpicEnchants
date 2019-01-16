package com.songoda.epicenchants.wrappers;

import io.netty.util.internal.ThreadLocalRandom;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@Getter
public class PotionChanceWrapper extends PotionEffectWrapper {
    private double chance;

    @Builder(builderMethodName = "chanceBuilder")
    PotionChanceWrapper(PotionEffectType type, String amplifier, String duration, double chance) {
        super(type, amplifier, duration);
        this.chance = chance;
    }

    public boolean test() {
        return ThreadLocalRandom.current().nextDouble(101) < chance;
    }

    public void perform(Player player, int tier) {
        player.addPotionEffect(get(tier));
    }
}
