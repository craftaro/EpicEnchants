package com.craftaro.epicenchants.effect.effects;

import com.craftaro.epicenchants.effect.EffectEventExecutor;
import com.craftaro.epicenchants.enums.EventType;
import com.craftaro.epicenchants.objects.LeveledModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

public class ModifyDamage extends EffectEventExecutor {
    public ModifyDamage(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(Player user, LivingEntity opponent, int level, Event event, EventType eventType) {
        if (!(event instanceof EntityDamageEvent)) {
            return;
        }

        ((EntityDamageEvent) event).setDamage(LeveledModifier.of(getSection().getString("modifier")).get(level, 1, user, opponent));
    }
}
