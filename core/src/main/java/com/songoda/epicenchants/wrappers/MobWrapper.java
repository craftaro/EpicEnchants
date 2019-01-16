package com.songoda.epicenchants.wrappers;

import com.songoda.epicenchants.utils.GeneralUtils;
import lombok.Builder;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import static java.util.concurrent.ThreadLocalRandom.current;

@Builder
public class MobWrapper {
    private String displayName;
    private int amount;
    private int health;
    private EntityType entityType;
    private double spawnPercentage;
    private double attackDamage;
    private boolean hostile;
    private ItemStack helmet, chestPlate, leggings, boots;

    public boolean trySpawn(Location location) {
        if (!GeneralUtils.chance(spawnPercentage)) {
            return false;
        }

        for (int i = 0; i < current().nextInt(amount + 1); i++) {
            Location spawnLocation = location.clone().add(current().nextInt(-3, 3), 0, current().nextInt(-3, 3));
            int y = location.getWorld().getHighestBlockAt(spawnLocation).getY();

            if (y < location.getY() - 10 || y > location.getY() + 10) {
                continue;
            }

            LivingEntity entity = (LivingEntity) location.getWorld().spawn(spawnLocation, entityType.getEntityClass());

            entity.setCustomName(displayName);
            entity.setCustomNameVisible(true);
            entity.setHealth(health);
            entity.getEquipment().setHelmet(helmet);
            entity.getEquipment().setChestplate(chestPlate);
            entity.getEquipment().setLeggings(leggings);
            entity.getEquipment().setBoots(boots);
        }

        return true;
    }
}
