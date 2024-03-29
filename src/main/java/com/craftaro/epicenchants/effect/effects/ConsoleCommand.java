package com.craftaro.epicenchants.effect.effects;

import com.craftaro.epicenchants.effect.EffectExecutor;
import com.craftaro.epicenchants.enums.EventType;
import com.craftaro.epicenchants.utils.single.GeneralUtils;
import com.craftaro.epicenchants.utils.single.Placeholders;
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
        GeneralUtils.getString(getSection(), "command")
                .stream()
                .map(s -> Placeholders.setPlaceholders(s, user, opponent, level))
                .forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s));
    }
}
