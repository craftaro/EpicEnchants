package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import static com.songoda.epicenchants.effect.EffectExecutor.Who.PLAYER;

public class PlayerCommand extends EffectExecutor {
    public PlayerCommand(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level) {
        String command = getSection().getString("command").replace("{level}", "" + level);

        who().ifPresent(who -> {
            if (who == PLAYER) wearer.performCommand(command);
            else opponent.performCommand(command);
        });
    }
}
