package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class DropHead extends EffectExecutor {
    public DropHead(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level, EventType eventType) {
        consume(player -> player.getWorld().dropItemNaturally(player.getLocation(),
                new ItemBuilder(Material.LEGACY_SKULL_ITEM).skullOwner(player.getName()).build()), wearer, opponent);
    }
}
