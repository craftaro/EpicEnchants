package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EnchantType;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.objects.LeveledModifier;
import com.songoda.epicenchants.utils.GeneralUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Potion extends EffectExecutor {
    public Potion(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level, EventType eventType) {
        if (!getSection().isString("potion-type")) {
            return;
        }

        LeveledModifier amplifier = LeveledModifier.of(getSection().getString("amplifier"));
        PotionEffectType effectType = PotionEffectType.getByName(getSection().getString("potion-type"));

        if (effectType == null) {
            return;
        }

        if (getEnchantType() == EnchantType.STATIC_EFFECT || getEnchantType() == EnchantType.HELD_ITEM) {
            if (eventType == EventType.ON) {
                consume(player -> player.addPotionEffect(new PotionEffect(effectType, Integer.MAX_VALUE, ((int) amplifier.get(level, 0)))), wearer, opponent);
            } else if (eventType == EventType.OFF) {
                consume(player -> player.removePotionEffect(effectType), wearer, opponent);
            }
            return;
        }

        LeveledModifier duration = LeveledModifier.of(getSection().getString("duration"));
        LeveledModifier chance = LeveledModifier.of(getSection().getString("chance"));

        if (!GeneralUtils.chance(chance.get(level, 100))) {
            return;
        }

        consume(player -> player.addPotionEffect(new PotionEffect(effectType, ((int) duration.get(level, Integer.MAX_VALUE)), ((int) amplifier.get(level, 0)))), wearer, opponent);
    }

}
