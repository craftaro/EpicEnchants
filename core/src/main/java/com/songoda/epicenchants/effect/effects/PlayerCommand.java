package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.songoda.epicenchants.effect.EffectExecutor.Who.OPPONENT;
import static com.songoda.epicenchants.enums.EventType.NONE;
import static com.songoda.epicenchants.enums.EventType.ON;

public class PlayerCommand extends EffectExecutor {
    public PlayerCommand(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player wearer, LivingEntity opponent, int level, EventType eventType) {
        if (eventType == ON || eventType == NONE) {
            if (who() == OPPONENT && !(opponent instanceof Player)) {
                return;
            }

            consume(entity -> ((Player) entity).performCommand(getSection().getString("command")
                    .replace("{level}", "" + level)
                    .replace("{wearer}", wearer.getName())
                    .replace("{opponent}", opponent.getName())), wearer, opponent);
        }

    }
}
