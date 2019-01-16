package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.wrappers.PotionEffectWrapper;
import lombok.Builder;

import java.util.Set;

@Builder
public class ActionClass {
    private Set<PotionEffectWrapper> potionEffects;
    private double modifyDamageTaken;
    private double modifyDamageGiven;

}
