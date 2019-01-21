package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import static com.songoda.epicenchants.effect.EffectExecutor.Who.PLAYER;

public class Fly extends EffectExecutor {
    public Fly(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level) {
        who().ifPresent(who -> {
            if (who == PLAYER) wearer.setFlying(!wearer.isFlying());
            else opponent.setFlying(!opponent.isFlying());
        });
    }
}
