package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import static com.songoda.epicenchants.enums.EventType.NONE;
import static com.songoda.epicenchants.enums.EventType.ON;

public class PlayerCommand extends EffectExecutor {
    public PlayerCommand(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level, EventType eventType) {
        if (eventType == ON || eventType == NONE)
            consume(player -> player.performCommand(getSection().getString("command")
                    .replace("{level}", "" + level)
                    .replace("{wearer}", wearer.getName())
                    .replace("{opponent}", opponent.getName())), wearer, opponent);

    }
}
