package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.objects.LeveledModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.jetbrains.annotations.NotNull;

public class Tnt extends EffectExecutor {
    public Tnt(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player user, LivingEntity opponent, int level, EventType eventType) {
        consume(player -> {
            for (int i = 0; i < LeveledModifier.of(getSection().getString("amount")).get(level, 1, user, opponent); i++) {
                TNTPrimed tntPrimed = (TNTPrimed) player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
                tntPrimed.setFuseTicks((int) LeveledModifier.of(getSection().getString("fuse")).get(level, 60, user, opponent));
            }
        }, user, opponent);
    }
}
