package com.songoda.epicenchants.wrappers;

import com.songoda.epicenchants.objects.LeveledModifier;
import com.songoda.epicenchants.utils.GeneralUtils;
import com.songoda.epicenchants.utils.ItemBuilder;
import lombok.Builder;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import static java.util.concurrent.ThreadLocalRandom.current;

@Builder
public class MobWrapper {
    private String displayName;
    private int amount;
    private int health;
    private EntityType entityType;
    private LeveledModifier spawnPercentage;
    private double attackDamage;
    private boolean hostile;
    private ItemBuilder helmet, chestPlate, leggings, boots;

    public boolean trySpawn(Location location, int level) {
        if (!GeneralUtils.chance(spawnPercentage.get(level))) {
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
            entity.getEquipment().setHelmet(helmet.buildWithWrappers(level));
            entity.getEquipment().setChestplate(chestPlate.buildWithWrappers(level));
            entity.getEquipment().setLeggings(leggings.buildWithWrappers(level));
            entity.getEquipment().setBoots(boots.buildWithWrappers(level));
        }

        return true;
    }
}
