package com.songoda.epicenchants.effect;

import com.songoda.epicenchants.objects.LeveledModifier;
import com.songoda.epicenchants.utils.GeneralUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Optional;

public abstract class EffectExecutor {
    @Getter private final ConfigurationSection section;

    public EffectExecutor(ConfigurationSection section) {
        this.section = section;
    }

    public void testAndRun(Player wearer, Player opponent, int level) {
        if (!section.isString("chance")) {
            execute(wearer, opponent, level);
            return;
        }

        if (GeneralUtils.chance(LeveledModifier.of(section.getString("chance")).get(level))) {
            execute(wearer, opponent, level);
        }
    }

    public abstract void execute(Player wearer, Player opponent, int level);

    public Optional<Who> who() {
        return Optional.ofNullable(section.getString("who").equalsIgnoreCase("player") ?
                Who.PLAYER : section.getString("who").equalsIgnoreCase("opponent") ?
                Who.OPPONENT : null);
    }

    public enum Who {
        PLAYER, OPPONENT
    }

    public Optional<Integer> getAmount() {
        return section.isInt("amount") ? Optional.of(section.getInt("amount")) : Optional.empty();
    }
}
