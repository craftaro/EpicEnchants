package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConsoleCommand extends EffectExecutor {
    public ConsoleCommand(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player user, LivingEntity opponent, int level, EventType eventType) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getSection().getString("command")
                .replace("{level}", "" + level)
                .replace("{user}", user.getName())
                .replace("{opponent}", opponent == null ? "" : opponent.getName()));
    }
}
