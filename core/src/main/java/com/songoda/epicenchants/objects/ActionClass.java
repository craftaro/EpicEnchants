package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.wrappers.PotionChanceWrapper;
import lombok.Builder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.songoda.epicenchants.objects.ActionClass.DamageType.TAKEN;

@Builder
public class ActionClass {
    private Set<PotionChanceWrapper> potionEffectsWearer;
    private Set<PotionChanceWrapper> potionEffectOpponent;
    private LeveledModifier modifyDamageTaken;
    private LeveledModifier modifyDamageGiven;

    public double run(@NotNull Player wearer, @Nullable Player opponent, int level, double damage, DamageType damageType) {
        potionEffectsWearer.stream().filter(p -> p.test(level)).forEach(p -> p.perform(wearer, level));

        if (opponent != null) {
            potionEffectOpponent.stream().filter(p -> p.test(level)).forEach(p -> p.perform(opponent, level));
        }


        return damageType == TAKEN ? modifyDamageTaken.get(level) : modifyDamageGiven.get(level);
    }

    public enum DamageType {
        TAKEN, GIVEN
    }
}
