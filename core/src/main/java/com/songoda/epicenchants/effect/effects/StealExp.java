package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.songoda.epicenchants.utils.single.Experience.changeExp;
import static com.songoda.epicenchants.utils.single.Experience.getExp;

public class StealExp extends EffectExecutor {
    public StealExp(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player user, @Nullable LivingEntity entity, int level, EventType eventType) {
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

        if (getExp(user) + amount <= 0) {
            changeExp(user, 0);
        } else {
            changeExp(user, (int) amount);
        }
    }
}
