package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EffectType;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.wrappers.MobWrapper;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Builder
@Getter
public class Enchant {
    private String identifier;
    private Group group;
    private int maxLevel;
    private Set<String> conflict;
    private Set<Material> itemWhitelist;
    private Set<EffectExecutor> effectExecutors;
    private Set<MobWrapper> mobs;
    private Set<String> description;
    @Nullable private String format;
    @Nullable private BookItem bookItem;
    private LeveledModifier modifyDamage;

    public void onAction(@NotNull Player wearer, @Nullable LivingEntity opponent, Event event, int level, EffectType effectType, EventType eventType) {
        effectExecutors.forEach(effect -> effect.testAndRun(wearer, opponent, level, effectType, event, eventType));
        mobs.forEach(mobWrapper -> mobWrapper.trySpawn(wearer, opponent, level, effectType));
    }

    public BookItem getBookItem() {
        return bookItem != null ? bookItem : group.getBookItem();
    }

    public String getFormat() {
        return format != null ? format : group.getFormat();
    }
}
