package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StealHealth extends EffectExecutor {
    public StealHealth(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player wearer, LivingEntity opponent, int level, EventType eventType) {
        double amount = getAmount().get(level, 0);
        wearer.setHealth(wearer.getHealth() + amount);
        opponent.setHealth(opponent.getHealth() - amount);
    }
}
