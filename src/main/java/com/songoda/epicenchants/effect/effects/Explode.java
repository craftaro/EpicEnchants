package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Explode extends EffectExecutor {
    public Explode(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player user, @Nullable LivingEntity opponent, int level, EventType eventType) {
        consume(entity -> entity.getWorld().createExplosion(entity.getLocation(), getSection().getInt("magnitude")), user, opponent);
    }
}
