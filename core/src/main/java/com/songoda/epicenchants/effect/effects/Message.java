package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.songoda.epicenchants.utils.GeneralUtils.color;

public class Message extends EffectExecutor {
    public Message(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player wearer, LivingEntity opponent, int level, EventType eventType) {
        if (eventType == EventType.ON || eventType == EventType.NONE)
            consume(entity -> entity.sendMessage(color(getSection().getString("message"))
                    .replace("{level}", "" + level)
                    .replace("{wearer}", wearer.getName())
                    .replace("{opponent}", opponent.getName())), wearer, opponent);
    }
}
