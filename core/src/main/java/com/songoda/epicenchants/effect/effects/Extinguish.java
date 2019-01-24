package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Extinguish extends EffectExecutor {
    public Extinguish(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level, EventType eventType) {
        consume(player -> player.setFireTicks(0), wearer, opponent);
    }
}
