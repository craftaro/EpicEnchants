package com.songoda.epicenchants.wrappers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

@Builder
@AllArgsConstructor
public class PotionEffectWrapper {
    private PotionEffectType type;
    private String amplifier;
    private String duration;

    public PotionEffect get(int tier) {
        int tempAmplifier = amplifier.isEmpty() ? 0 : parseInt(amplifier.replace("{tier}", valueOf(tier)));
        int tempDuration = duration.isEmpty() ? 0 : parseInt(duration.replace("{tier}", valueOf(tier)));
        return new PotionEffect(type, tempDuration, tempAmplifier);
    }
}
