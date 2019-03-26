package com.songoda.epicenchants.effect;

import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.enums.TriggerType;
import com.songoda.epicenchants.objects.Condition;
import com.songoda.epicenchants.objects.LeveledModifier;
import com.songoda.epicenchants.utils.single.GeneralUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;

import static com.songoda.epicenchants.effect.EffectExecutor.Who.OPPONENT;
import static com.songoda.epicenchants.effect.EffectExecutor.Who.USER;

public abstract class EffectExecutor {
    @Getter private final ConfigurationSection section;
    @Getter private final Set<TriggerType> triggerTypes;
    private final Condition condition;

    public EffectExecutor(ConfigurationSection section) {
        this.section = section;
        this.triggerTypes = GeneralUtils.parseTrigger(section.getString("trigger"));
        this.condition = Condition.of(section.getString("condition"));
    }

    public void testAndRun(@NotNull Player user, @Nullable LivingEntity opponent, int level, TriggerType type, Event event, EventType eventType) {
        if (!triggerTypes.contains(type)) {
            return;
        }

        if (section.isString("chance") && !GeneralUtils.chance(LeveledModifier.of(section.getString("chance")).get(level, 100, user, opponent))) {
            return;
        }

        if (!condition.get(user, opponent, level, event, false)) {
            return;
        }

        if (this instanceof EffectEventExecutor) {
            ((EffectEventExecutor) this).execute(user, opponent, level, event, eventType);
            return;
        }

        execute(user, opponent, level, eventType);
    }

    public abstract void execute(@NotNull Player user, @Nullable LivingEntity opponent, int level, EventType eventType);

    protected Who who() {
        if (section.isString("who")) {
            if (section.getString("who").equalsIgnoreCase("user")) return USER;
            else if (section.getString("who").equalsIgnoreCase("opponent")) return OPPONENT;
        }
        return USER;
    }

    public LeveledModifier getAmount() {
        return LeveledModifier.of(section.getString("amount"));
    }

    public void consume(Consumer<LivingEntity> playerConsumer, Player user, @Nullable LivingEntity opponent) {
        if (triggerTypes.contains(TriggerType.HELD_ITEM) || triggerTypes.contains(TriggerType.STATIC_EFFECT)) {
            playerConsumer.accept(user);
            return;
        }

        switch (who()) {
            case USER:
                playerConsumer.accept(user);
                break;
            case OPPONENT:
                if (opponent != null)
                    playerConsumer.accept(opponent);
        }
    }

    public enum Who {
        USER, OPPONENT
    }
}
