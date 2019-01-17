package com.songoda.epicenchants.wrappers;

import com.songoda.epicenchants.objects.LeveledModifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Builder
@AllArgsConstructor
public class PotionEffectWrapper {
    private PotionEffectType type;
    private LeveledModifier amplifier;
    private LeveledModifier duration;

    public PotionEffect get(int level) {
        return new PotionEffect(type, (int) amplifier.get(level), (int) duration.get(level));
    }
}
