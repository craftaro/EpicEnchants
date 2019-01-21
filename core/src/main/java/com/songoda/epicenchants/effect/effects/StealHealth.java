package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class StealHealth extends EffectExecutor {
    public StealHealth(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level) {
        getAmount().ifPresent(amount -> {
            wearer.setHealth(wearer.getHealth() + amount);
            opponent.setHealth(opponent.getHealth() - amount);
        });
    }
}
