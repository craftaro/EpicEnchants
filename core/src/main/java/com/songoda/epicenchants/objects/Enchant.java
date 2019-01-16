package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.wrappers.MobWrapper;
import com.songoda.epicenchants.wrappers.PotionEffectWrapper;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Set;

@Builder
public class Enchant {
    @Getter private String identifier;
    @Getter private int tier;
    private Set<MobWrapper> mobs;
    private Set<PotionEffectWrapper> potionEffects;
    private Set<Material> itemWhitelist;
    @Getter private int maxLevel;
    private String format;
    private ActionClass action;
    @Getter private BookItem bookItem;
}
