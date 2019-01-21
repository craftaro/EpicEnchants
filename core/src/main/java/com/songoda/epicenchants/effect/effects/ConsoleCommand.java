package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ConsoleCommand extends EffectExecutor {
    public ConsoleCommand(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level) {
        Bukkit.getConsoleSender().sendMessage(getSection().getString("command")
                .replace("{level}", "" + level)
                .replace("{wearer}", wearer.getName()
                        .replace("{opponent}", opponent.getName())));
    }
}
