package com.songoda.epicenchants.effect;

import com.songoda.epicenchants.enums.EffectType;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.objects.LeveledModifier;
import com.songoda.epicenchants.utils.GeneralUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;

import static com.songoda.epicenchants.effect.EffectExecutor.Who.OPPONENT;
import static com.songoda.epicenchants.effect.EffectExecutor.Who.WEARER;

public abstract class EffectExecutor {
    @Getter private final ConfigurationSection section;
    @Getter private final EffectType effectType;
    private EffectType[] allowedEffects;

    public EffectExecutor(ConfigurationSection section, EffectType... allowedEffects) {
        this.section = section;
        this.effectType = EffectType.valueOf(section.getString("type"));
        this.allowedEffects = allowedEffects;
    }

    public void testAndRun(@NotNull Player wearer, @Nullable LivingEntity opponent, int level, EffectType type, Event event, EventType eventType) {
        if (type != effectType) {
            return;
        }

        if (allowedEffects.length != 0 && Arrays.stream(allowedEffects).noneMatch(t -> t == effectType)) {
            throw new IllegalStateException(section.getName() + " cannot be triggered by " + effectType.toString());
        }

        if (section.isString("chance") && !GeneralUtils.chance(LeveledModifier.of(section.getString("chance")).get(level, 100))) {
            return;
        }

        if (this instanceof EffectEventExecutor) {
            ((EffectEventExecutor) this).execute(wearer, opponent, level, event, eventType);
            return;
        }

        execute(wearer, opponent, level, eventType);
    }

    public abstract void execute(@NotNull Player wearer, @Nullable LivingEntity opponent, int level, EventType eventType);

    public Who who() {
        if (section.isString("who")) {
            if (section.getString("who").equalsIgnoreCase("wearer")) return WEARER;
            else if (section.getString("who").equalsIgnoreCase("opponent")) return OPPONENT;
        }
        return WEARER;
    }

    public LeveledModifier getAmount() {
        return LeveledModifier.of(section.getString("amount"));
    }

    public void consume(Consumer<LivingEntity> playerConsumer, Player wearer, @Nullable LivingEntity opponent) {
        if (effectType == EffectType.HELD_ITEM || effectType == EffectType.STATIC_EFFECT) {
            playerConsumer.accept(wearer);
            return;
        }

        switch (who()) {
            case WEARER:
                playerConsumer.accept(wearer);
                break;
            case OPPONENT:
                if (opponent != null)
                    playerConsumer.accept(opponent);
        }
    }

    public enum Who {
        WEARER, OPPONENT
    }
}
