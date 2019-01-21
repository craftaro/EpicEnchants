package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class DropHead extends EffectExecutor {
    public DropHead(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level) {

    }
}
