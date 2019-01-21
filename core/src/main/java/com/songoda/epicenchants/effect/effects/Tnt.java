package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import static com.songoda.epicenchants.effect.EffectExecutor.Who.PLAYER;

public class Tnt extends EffectExecutor {
    public Tnt(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level) {
        getAmount().ifPresent(amount -> who().ifPresent(who -> {
            if (who == PLAYER) wearer.getWorld().spawnEntity(wearer.getLocation(), EntityType.PRIMED_TNT);
            else opponent.getWorld().spawnEntity(opponent.getLocation(), EntityType.PRIMED_TNT);
        }));
    }
}
