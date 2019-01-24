package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EffectType;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.wrappers.MobWrapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Set;

@Builder
@Getter
public class Enchant {
    private String identifier;
    private int tier;
    private int maxLevel;
    @Singular("conflict") private Set<String> conflict;
    @Singular("whiteItem") private Set<Material> itemWhitelist;
    @Singular("effect") private Set<EffectExecutor> effectExecutors;
    @Singular("mob") private Set<MobWrapper> mobs;
    private String format;
    private BookItem bookItem;
    private LeveledModifier modifyDamage;

    public void onAction(Player wearer, Player attacker, Event event, int level, EffectType effectType, EventType eventType) {
        effectExecutors.forEach(effect -> effect.testAndRun(wearer, attacker, level, effectType, event, eventType));
        mobs.forEach(mobWrapper -> mobWrapper.trySpawn(wearer, attacker, level, effectType));
    }
}
