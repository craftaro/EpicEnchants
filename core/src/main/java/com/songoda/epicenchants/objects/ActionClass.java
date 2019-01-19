package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.enums.EnchantProcType;
import com.songoda.epicenchants.enums.MaterialType;
import com.songoda.epicenchants.wrappers.MobWrapper;
import com.songoda.epicenchants.wrappers.PotionChanceWrapper;
import lombok.Builder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

import static com.songoda.epicenchants.enums.EnchantProcType.*;
import static com.songoda.epicenchants.enums.MaterialType.*;

@Builder
public class ActionClass {
    private Set<PotionChanceWrapper> potionEffectsWearer;
    private Set<PotionChanceWrapper> potionEffectOpponent;
    private LeveledModifier modifyDamage;
    private Set<MobWrapper> mobs;

    public double run(@NotNull Player wearer, @Nullable Player opponent, int level, double damage, EnchantProcType procType, MaterialType type) {
        potionEffectsWearer.stream().filter(p -> p.test(level)).forEach(p -> p.perform(wearer, level));
        Optional.ofNullable(opponent).ifPresent(a -> potionEffectOpponent.stream().filter(p -> p.test(level)).forEach(p -> p.perform(opponent, level)));

        mobs.forEach(mob -> mob.trySpawn(wearer.getLocation(), level));

        double percentage = 0;

        if((procType == DAMAGED && type == ARMOR) || (procType == DEALT_DAMAGE && type == WEAPON)) {
            percentage = modifyDamage.get(level);
        }

        return damage + damage * percentage;
    }
}
