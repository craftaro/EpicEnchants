package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.wrappers.PotionChanceWrapper;
import lombok.Builder;

import java.util.Set;

@Builder
public class ActionClass {
    private Set<PotionChanceWrapper> potionEffects;
    private double modifyDamageTaken;
    private double modifyDamageGiven;

}
