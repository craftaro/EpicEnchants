package com.songoda.epicenchants.wrappers;

import lombok.Builder;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

@Builder
public class MobWrapper {
    private EntityType entityType;
    private double spawnPercentage;
    private double health;
    private boolean hostile;
    private ItemStack helmet, chestPlate, leggings, boots;
}
