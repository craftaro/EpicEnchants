package com.craftaro.epicenchants.effect.effects;

import com.craftaro.epicenchants.effect.EffectExecutor;
import com.craftaro.epicenchants.enums.EventType;
import com.craftaro.epicenchants.objects.LeveledModifier;
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
        consume(entity -> entity.getWorld().createExplosion(entity.getLocation(), (float) LeveledModifier
                        .of(getSection().getString("magnitude")).get(level, level, user, opponent)),
                user, opponent);
    }
}
