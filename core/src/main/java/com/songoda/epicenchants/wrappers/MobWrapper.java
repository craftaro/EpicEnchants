package com.songoda.epicenchants.wrappers;

import com.songoda.epicenchants.enums.TriggerType;
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
import org.jetbrains.annotations.Nullable;

import static java.util.concurrent.ThreadLocalRandom.current;

@Builder
public class MobWrapper {
    private String displayName;
    private EntityType entityType;
    private LeveledModifier attackDamage;
    private TriggerType triggerType;
    private LeveledModifier equipmentDropChance;
    private LeveledModifier spawnPercentage;
    private LeveledModifier health;
    private ItemBuilder helmet, chestPlate, leggings, boots, handItem;
    private boolean hostile;
    private LeveledModifier maxAmount;

    public void trySpawn(@NotNull Player player, @Nullable LivingEntity opponent, int level, TriggerType triggerType) {
        if (this.triggerType != triggerType) {
            return;
        }

        if (!GeneralUtils.chance(spawnPercentage.get(level, 100))) {
            return;
        }

        Location location = player.getLocation();

        for (int i = 0; i < current().nextInt((int) (maxAmount.get(level, 1) + 1)); i++) {
            Location spawnLocation = location.clone().add(current().nextInt(-3, 3), 0, current().nextInt(-3, 3));
            int y = location.getWorld().getHighestBlockAt(spawnLocation).getY();

            if (y < location.getY() - 10 || y > location.getY() + 10) {
                continue;
            }

            Entity entity = location.getWorld().spawnEntity(spawnLocation, entityType);

            entity.setCustomName(displayName.replace("{level}", "" + level));
            entity.setCustomNameVisible(true);

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                int dropChance = (int) equipmentDropChance.get(level, 0);

                if (helmet != null) livingEntity.getEquipment().setHelmet(helmet.buildWithWrappers(level));
                if (chestPlate != null) livingEntity.getEquipment().setChestplate(chestPlate.buildWithWrappers(level));
                if (leggings != null) livingEntity.getEquipment().setLeggings(leggings.buildWithWrappers(level));
                if (boots != null) livingEntity.getEquipment().setBoots(boots.buildWithWrappers(level));
                livingEntity.getEquipment().setHelmetDropChance(dropChance);
                livingEntity.getEquipment().setLeggingsDropChance(dropChance);
                livingEntity.getEquipment().setHelmetDropChance(dropChance);
                livingEntity.getEquipment().setChestplateDropChance(dropChance);

                if (handItem != null) livingEntity.getEquipment().setItemInMainHand(handItem.buildWithWrappers(level));
                livingEntity.getEquipment().setItemInMainHandDropChance(dropChance);
            }

            if (entity instanceof Monster && opponent != null) {
                ((Monster) entity).setTarget(opponent);
            }

            NBTEntity nbtEntity = new NBTEntity(entity);

            nbtEntity.setBoolean(player.getUniqueId().toString(), true);

            NBTList list = nbtEntity.getList("Attributes", NBTType.NBTTagCompound);

            for (int j = 0; j < list.size(); j++) {
                NBTListCompound lc = list.getCompound(j);
                if (lc.getString("Name").equals("generic.attackDamage")) {
                    lc.setDouble("Base", attackDamage.get(level, (int) lc.getDouble("Base")));
                    continue;
                }

                if (lc.getString("Name").equals("generic.maxHealth")) {
                    lc.setDouble("Base", health.get(level, (int) lc.getDouble("Base")));
                }
            }
        }
    }
}
