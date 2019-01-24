package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class StealHealth extends EffectExecutor {
    public StealHealth(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level, EventType eventType) {
        double amount = getAmount().get(level, 0);
        wearer.setHealth(wearer.getHealth() + amount);
        opponent.setHealth(opponent.getHealth() - amount);
    }
}
