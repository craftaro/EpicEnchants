package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.songoda.epicenchants.enums.EffectType.HELD_ITEM;
import static com.songoda.epicenchants.enums.EffectType.STATIC_EFFECT;
import static com.songoda.epicenchants.enums.EventType.ON;

public class Fly extends EffectExecutor {
    public Fly(ConfigurationSection section) {
        super(section, STATIC_EFFECT, HELD_ITEM);
    }

    @Override
    public void execute(@NotNull Player wearer, LivingEntity opponent, int level, EventType eventType) {
        wearer.setAllowFlight(eventType == ON);
        wearer.setFlying(eventType == ON);
    }

}
