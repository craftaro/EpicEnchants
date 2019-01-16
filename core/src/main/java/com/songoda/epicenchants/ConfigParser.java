package com.songoda.epicenchants;

import com.songoda.epicenchants.objects.ActionClass;
import com.songoda.epicenchants.wrappers.PotionEffectWrapper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.stream.Collectors;

public class ConfigParser {
    public static PotionEffect parsePotionEffect(String input) {
        if (input.contains(":")) {
            return new PotionEffect(PotionEffectType.getByName(input.split(":")[0]), Integer.MAX_VALUE, Integer.valueOf(input.split(":")[1]));
        }

        return new PotionEffect(PotionEffectType.getByName(input), Integer.MAX_VALUE, 0);
    }

    public static ActionClass parseActionClass(ConfigurationSection section) {
        return ActionClass.builder()
                .modifyDamageGiven(section.getDouble("modify-damage-given"))
                .modifyDamageTaken(section.getDouble("modify-damage-taken"))
                .potionEffects(section.getConfigurationSection("potion-effects")
                        .getKeys(false).stream()
                        .map(section::getConfigurationSection)
                        .map(ConfigParser::parsePotionEffect)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static PotionEffectWrapper parsePotionEffect(ConfigurationSection section) {
        return new PotionEffectWrapper(new PotionEffect(
                PotionEffectType.getByName(section.getName()),
                section.getInt("duration"),
                section.getInt("amplifier")), section.getDouble("chance"));
    }
}
