package com.craftaro.epicenchants.effect.effects;

import com.craftaro.epicenchants.effect.EffectExecutor;
import com.craftaro.epicenchants.enums.EventType;
import com.craftaro.epicenchants.utils.single.GeneralUtils;
import com.craftaro.epicenchants.utils.single.Placeholders;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Message extends EffectExecutor {
    public Message(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player user, LivingEntity opponent, int level, EventType eventType) {
        if (eventType == EventType.ON || eventType == EventType.NONE) {
            consume(entity -> GeneralUtils.getString(getSection(), "message")
                    .stream()
                    .map(s -> Placeholders.setPlaceholders(s, user, opponent, level))
                    .forEach(entity::sendMessage), user, opponent);
        }
    }
}
