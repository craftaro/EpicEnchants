package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.songoda.epicenchants.utils.single.Experience.*;

public class StealExp extends EffectExecutor {
    public StealExp(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player wearer, @Nullable LivingEntity entity, int level, EventType eventType) {
        double amount = getAmount().get(level, 0);

        if (!(entity instanceof Player)) {
            return;
        }

        Player opponent = (Player) entity;


        if (getExp(opponent) - amount <= 0) {
            changeExp(opponent, 0);
        } else {
            changeExp(opponent, (int) -amount);
        }

        if (getExp(wearer) + amount <= 0) {
            changeExp(wearer, 0);
        } else {
            changeExp(wearer, (int) amount);
        }
    }
}
