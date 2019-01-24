package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EnchantType;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import static com.songoda.epicenchants.enums.EventType.ON;

public class Fly extends EffectExecutor {
    public Fly(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level, EventType eventType) {
        if (getEnchantType() != EnchantType.STATIC_EFFECT && getEnchantType() != EnchantType.HELD_ITEM) {
            throw new IllegalStateException("Fly effect is not a STATIC_EFFECT or HELD_ITEM");
        }

        wearer.setAllowFlight(eventType == ON);
        wearer.setFlying(eventType == ON);
    }

}
