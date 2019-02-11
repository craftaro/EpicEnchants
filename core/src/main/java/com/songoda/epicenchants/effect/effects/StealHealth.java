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

        if (opponent == null) {
            return;
        }

        double opponentHealth = opponent.getHealth() - amount;
        double wearerHealth = wearer.getHealth() + amount;

        if (opponentHealth <= 0) {
            opponent.setHealth(0);
        } else if (opponentHealth > opponent.getMaxHealth()) {
            opponent.setHealth(opponent.getMaxHealth());
        } else {
            opponent.setHealth(opponentHealth);
        }

        if (wearerHealth <= 0) {
            wearer.setHealth(0);
        } else if (wearerHealth > wearer.getMaxHealth()) {
            wearer.setHealth(wearer.getMaxHealth());
        } else {
            wearer.setHealth(wearerHealth);
        }
    }
}
