package com.songoda.epicenchants.wrappers;

import com.songoda.epicenchants.objects.LeveledModifier;
import com.songoda.epicenchants.utils.GeneralUtils;
import com.songoda.epicenchants.utils.ItemBuilder;
import de.tr7zw.itemnbtapi.NBTEntity;
import de.tr7zw.itemnbtapi.NBTList;
import de.tr7zw.itemnbtapi.NBTListCompound;
import de.tr7zw.itemnbtapi.NBTType;
import lombok.Builder;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import static java.util.concurrent.ThreadLocalRandom.current;

@Builder
public class MobWrapper {
    private String displayName;
    private int amount;
    private int health;
    private int armorDropChance;
    private EntityType entityType;
    private LeveledModifier spawnPercentage;
    private double attackDamage;
    private boolean hostile;
    private ItemBuilder helmet, chestPlate, leggings, boots;

    public boolean trySpawn(@NotNull Player player, Player opponent, int level) {
        if (!GeneralUtils.chance(spawnPercentage.get(level))) {
            return false;
        }

        Location location = player.getLocation();


        for (int i = 0; i < current().nextInt(amount + 1); i++) {
            Location spawnLocation = location.clone().add(current().nextInt(-3, 3), 0, current().nextInt(-3, 3));
            int y = location.getWorld().getHighestBlockAt(spawnLocation).getY();

            if (y < location.getY() - 10 || y > location.getY() + 10) {
                continue;
            }

            Entity entity = location.getWorld().spawnEntity(spawnLocation, entityType);

            entity.setCustomName(displayName);
            entity.setCustomNameVisible(true);

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.getEquipment().setHelmet(helmet.buildWithWrappers(level));
                livingEntity.getEquipment().setChestplate(chestPlate.buildWithWrappers(level));
                livingEntity.getEquipment().setLeggings(leggings.buildWithWrappers(level));
                livingEntity.getEquipment().setBoots(boots.buildWithWrappers(level));
                livingEntity.getEquipment().setHelmetDropChance(armorDropChance);
                livingEntity.getEquipment().setLeggingsDropChance(armorDropChance);
                livingEntity.getEquipment().setHelmetDropChance(armorDropChance);
                livingEntity.getEquipment().setChestplateDropChance(armorDropChance);
            }

            if (entity instanceof Monster && opponent != null) {
                ((Monster) entity).setTarget(opponent);
            }

            NBTEntity nbtEntity = new NBTEntity(entity);
            NBTList list = nbtEntity.getList("Attributes", NBTType.NBTTagCompound);

            for (int j = 0; j < list.size(); j++) {
                NBTListCompound lc = list.getCompound(j);
                if (lc.getString("Name").equals("generic.attackDamage")) {
                    lc.setDouble("Base", attackDamage);
                    continue;
                }

                if (lc.getString("Name").equals("generic.maxHealth")) {
                    lc.setDouble("Base", health);
                }
            }
        }

        return true;
    }
}
