package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.utils.Experience;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModifyExp extends EffectExecutor {
    public ModifyExp(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player wearer, @Nullable LivingEntity opponent, int level, EventType eventType) {
        consume(entity -> {
            if (entity instanceof Player) {
                Experience.changeExp(((Player) entity), (int) getAmount().get(level, 0));
            }
        }, wearer, opponent);
    }
}
