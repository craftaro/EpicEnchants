package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.managers.HookManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.songoda.epicenchants.utils.single.GeneralUtils.color;

public class Message extends EffectExecutor {
    public Message(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player user, LivingEntity opponent, int level, EventType eventType) {
        if (eventType == EventType.ON || eventType == EventType.NONE)
            consume(entity -> entity.sendMessage(color(HookManager.setPAPIPlaceholders(getSection().getString("message"), user, opponent, level))), user, opponent);
    }
}
