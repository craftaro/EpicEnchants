package com.craftaro.epicenchants.effect.effects;

import com.craftaro.epicenchants.effect.EffectExecutor;
import com.craftaro.epicenchants.enums.EventType;
import com.craftaro.epicenchants.utils.objects.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DropItem extends EffectExecutor {
    public DropItem(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player user, @Nullable LivingEntity opponent, int level, EventType eventType) {
        consume(entity -> entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemBuilder(getSection(), ((Player) entity)).build()), user, opponent);
    }
}
