package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.songoda.epicenchants.enums.EventType.ON;
import static com.songoda.epicenchants.enums.TriggerType.HELD_ITEM;
import static com.songoda.epicenchants.enums.TriggerType.STATIC_EFFECT;

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
