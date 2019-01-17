package com.songoda.epicenchants.wrappers;

import com.songoda.epicenchants.objects.LeveledModifier;
import com.songoda.epicenchants.utils.GeneralUtils;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@Getter
public class PotionChanceWrapper extends PotionEffectWrapper {
    private LeveledModifier chance;

    @Builder(builderMethodName = "chanceBuilder")
    PotionChanceWrapper(PotionEffectType type, LeveledModifier amplifier, LeveledModifier duration, LeveledModifier chance ) {
        super(type, amplifier, duration);
        this.chance = chance;
    }

    public boolean test(int level) {
        return GeneralUtils.chance(chance.get(level));
    }

    public void perform(Player player, int level) {
        player.addPotionEffect(get(level));
    }
}
