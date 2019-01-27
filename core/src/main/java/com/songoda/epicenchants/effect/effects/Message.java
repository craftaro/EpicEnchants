package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import static com.songoda.epicenchants.utils.GeneralUtils.color;

public class Message extends EffectExecutor {
    public Message(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player wearer, Player opponent, int level, EventType eventType) {
        if (eventType == EventType.ON || eventType == EventType.NONE)
            consume(player -> player.sendMessage(color(getSection().getString("message"))
                    .replace("{level}", "" + level)
                    .replace("{wearer}", wearer.getName())
                    .replace("{opponent}", opponent.getName())), wearer, opponent);
    }
}
