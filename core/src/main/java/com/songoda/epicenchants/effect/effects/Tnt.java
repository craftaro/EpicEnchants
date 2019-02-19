package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.objects.LeveledModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

public class Tnt extends EffectExecutor {
    public Tnt(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player wearer, LivingEntity opponent, int level, EventType eventType) {
        consume(player -> {
            for (int i = 0; i < LeveledModifier.of(getSection().getString("amount")).get(level, 1); i++) {
                TNTPrimed tntPrimed = (TNTPrimed) player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
                tntPrimed.setFuseTicks((int) LeveledModifier.of(getSection().getString("fuse")).get(level, 60));
            }
        }, wearer, opponent);
    }
}
