package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.wrappers.MobWrapper;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;

import java.util.Set;

@Builder
public class Enchant {
    @Getter private String identifier;
    private Set<MobWrapper> mobs;
    private Set<PotionEffect> potionEffects;
    private Set<Material> itemWhitelist;
    private int maxTier;
    private String format;
    private ActionClass action;

}
