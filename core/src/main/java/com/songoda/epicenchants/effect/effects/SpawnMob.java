package com.songoda.epicenchants.effect.effects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.objects.LeveledModifier;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import de.tr7zw.itemnbtapi.NBTEntity;
import de.tr7zw.itemnbtapi.NBTList;
import de.tr7zw.itemnbtapi.NBTListCompound;
import de.tr7zw.itemnbtapi.NBTType;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.songoda.epicenchants.objects.LeveledModifier.of;
import static com.songoda.epicenchants.utils.single.GeneralUtils.color;
import static java.util.concurrent.ThreadLocalRandom.current;

public class SpawnMob extends EffectExecutor {
    private LeveledModifier attackDamage;
    private String displayName;
    private EntityType entityType;
    private LeveledModifier equipmentDropChance;
    private LeveledModifier health;
    private ItemBuilder helmet, chestPlate, leggings, boots, handItem;
    private boolean hostile;
    private LeveledModifier amount;

    public SpawnMob(ConfigurationSection section) {
        super(section);

        entityType = EntityType.valueOf(section.getString("mob-type"));
        amount = of(section.getString("amount"));
        health = of(section.getString("health"));
        attackDamage = of(section.getString("attack-damage"));
        equipmentDropChance = LeveledModifier.of(section.getString("equipment-drop-chance"));
        hostile = section.getBoolean("hostile", false);
        displayName = section.isString("display-name") ? color(section.getString("display-name")) : "";
        helmet = section.isConfigurationSection("equipment.helmet") ? new ItemBuilder(section.getConfigurationSection("equipment.helmet")) : null;
        chestPlate = section.isConfigurationSection("equipment.chestplate") ? new ItemBuilder(section.getConfigurationSection("equipment.chestplate")) : null;
        leggings = section.isConfigurationSection("equipment.leggings") ? new ItemBuilder(section.getConfigurationSection("equipment.leggings")) : null;
        boots = section.isConfigurationSection("equipment.boots") ? new ItemBuilder(section.getConfigurationSection("equipment.boots")) : null;
        handItem = section.isConfigurationSection("equipment.hand-item") ? new ItemBuilder(section.getConfigurationSection("equipment.hand-item")) : null;
    }

    @Override
    public void execute(@NotNull Player user, @Nullable LivingEntity opponent, int level, EventType eventType) {
        Location location = user.getLocation();

        for (int i = 0; i < amount.get(level, 1, user, opponent); i++) {
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
                livingEntity.setRemoveWhenFarAway(true);
                int dropChance = (int) equipmentDropChance.get(level, 0, user, opponent);

                if (helmet != null)
                    livingEntity.getEquipment().setHelmet(helmet.buildWithWrappers(level, user, opponent));
                if (chestPlate != null)
                    livingEntity.getEquipment().setChestplate(chestPlate.buildWithWrappers(level, user, opponent));
                if (leggings != null)
                    livingEntity.getEquipment().setLeggings(leggings.buildWithWrappers(level, user, opponent));
                if (boots != null) livingEntity.getEquipment().setBoots(boots.buildWithWrappers(level, user, opponent));
                livingEntity.getEquipment().setHelmetDropChance(dropChance);
                livingEntity.getEquipment().setLeggingsDropChance(dropChance);
                livingEntity.getEquipment().setHelmetDropChance(dropChance);
                livingEntity.getEquipment().setChestplateDropChance(dropChance);

                if (handItem != null)
                    livingEntity.getEquipment().setItemInHand(handItem.buildWithWrappers(level, user, opponent));
                livingEntity.getEquipment().setItemInHandDropChance(dropChance);
            }

            if (hostile && entity instanceof Monster && opponent != null) {
                ((Monster) entity).setTarget(opponent);
            }

            NBTEntity nbtEntity = new NBTEntity(entity);

            nbtEntity.setBoolean(user.getUniqueId().toString(), true);

            NBTList list = nbtEntity.getList("Attributes", NBTType.NBTTagCompound);

            for (int j = 0; j < list.size(); j++) {
                NBTListCompound lc = list.getCompound(j);
                if (lc.getString("Name").equals("generic.attackDamage")) {
                    lc.setDouble("Base", attackDamage.get(level, (int) lc.getDouble("Base"), user, opponent));
                    continue;
                }

                if (lc.getString("Name").equals("generic.maxHealth")) {
                    lc.setDouble("Base", health.get(level, (int) lc.getDouble("Base"), user, opponent));
                }
            }
        }
    }
}
