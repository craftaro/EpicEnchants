package com.songoda.epicenchants.effect;

import com.songoda.epicenchants.enums.EnchantType;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.objects.LeveledModifier;
import com.songoda.epicenchants.utils.GeneralUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.function.Consumer;

import static com.songoda.epicenchants.effect.EffectExecutor.Who.OPPONENT;
import static com.songoda.epicenchants.effect.EffectExecutor.Who.WEARER;

public abstract class EffectExecutor {
    @Getter private final ConfigurationSection section;
    @Getter private final EnchantType enchantType;

    public EffectExecutor(ConfigurationSection section) {
        this.section = section;
        this.enchantType = EnchantType.valueOf(section.getString("type"));
    }

    public void testAndRun(Player wearer, Player opponent, int level, EnchantType type, Event event, EventType eventType) {
        if (type != enchantType) {
            return;
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

    public abstract void execute(Player wearer, Player opponent, int level, EventType eventType);

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

    public void consume(Consumer<Player> playerConsumer, Player wearer, Player opponent) {
        if (enchantType == EnchantType.HELD_ITEM || enchantType == EnchantType.STATIC_EFFECT) {
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
