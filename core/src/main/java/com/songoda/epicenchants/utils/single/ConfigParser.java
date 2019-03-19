package com.songoda.epicenchants.utils.single;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.effect.EffectManager;
import com.songoda.epicenchants.enums.TriggerType;
import com.songoda.epicenchants.objects.*;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import com.songoda.epicenchants.wrappers.EnchantmentWrapper;
import com.songoda.epicenchants.wrappers.MobWrapper;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;

import java.util.*;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.single.GeneralUtils.color;

public class ConfigParser {
    public static Enchant parseEnchant(EpicEnchants instance, FileConfiguration config) {
        return Enchant.builder()
                .identifier(config.getString("identifier"))
                .group(instance.getGroupManager().getValue(config.getString("group").toUpperCase()).orElseThrow(() -> new IllegalArgumentException("Invalid group: " + config.getString("group"))))
                .maxLevel(config.getInt("max-level"))
                .format(color(config.getString("applied-format")))
                .bookItem(parseBookItem(config.getConfigurationSection("book-item")))
                .itemWhitelist((config.isList("item-whitelist") ? config.getStringList("item-whitelist").stream().map(instance.getItemGroup()::get).flatMap(Collection::stream).collect(Collectors.toSet()) : Collections.emptySet()))
                .conflict(config.isList("conflicting-enchants") ? new HashSet<>(config.getStringList("conflicting-enchants")) : Collections.emptySet())
                .condition(Condition.of(config.getString("condition")))
                .mobs(config.isConfigurationSection("mobs") ? config.getConfigurationSection("mobs").getKeys(false).stream()
                        .map(s -> "mobs." + s)
                        .map(config::getConfigurationSection)
                        .map(ConfigParser::parseMobWrapper).collect(Collectors.toSet()) : Collections.emptySet())
                .effectExecutors(config.isConfigurationSection("effects") ? config.getConfigurationSection("effects").getKeys(false).stream()
                        .map(s -> "effects." + s)
                        .map(config::getConfigurationSection)
                        .map(EffectManager::getEffect)
                        .map(o -> o.orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()) : Collections.emptySet())
                .description(config.isList("description") ? new HashSet<>(config.getStringList("description")) : Collections.emptySet())
                .build();
    }

    public static MobWrapper parseMobWrapper(ConfigurationSection section) {
        return section != null ? MobWrapper.builder()
                .triggerType(TriggerType.valueOf(section.getString("trigger")))
                .entityType(EntityType.valueOf(section.getName()))
                .maxAmount(LeveledModifier.of(section.getString("max-amount")))
                .spawnPercentage(LeveledModifier.of(section.getString("spawn-percentage")))
                .health(LeveledModifier.of(section.getString("health")))
                .attackDamage(LeveledModifier.of(section.getString("attack-damage")))
                .equipmentDropChance(LeveledModifier.of(section.getString("equipment-drop-chance")))
                .hostile(section.getBoolean("hostile"))
                .displayName(color(section.getString("display-name")))
                .helmet(section.isConfigurationSection("equipment.helmet") ? new ItemBuilder(section.getConfigurationSection("equipment.helmet")) : null)
                .chestPlate(section.isConfigurationSection("equipment.chestplate") ? new ItemBuilder(section.getConfigurationSection("equipment.chestplate")) : null)
                .leggings(section.isConfigurationSection("equipment.leggings") ? new ItemBuilder(section.getConfigurationSection("equipment.leggings")) : null)
                .boots(section.isConfigurationSection("equipment.boots") ? new ItemBuilder(section.getConfigurationSection("equipment.boots")) : null)
                .handItem(section.isConfigurationSection("equipment.hand-item") ? new ItemBuilder(section.getConfigurationSection("equipment.hand-item")) : null)
                .build() : null;
    }

    public static EnchantmentWrapper parseEnchantmentWrapper(String key) {
        return EnchantmentWrapper.builder()
                .amplifier(LeveledModifier.of(key.contains(":") ? key.split(":")[1] : ""))
                .enchantment(Enchantment.getByName(key.split(":")[0]))
                .build();
    }

    private static BookItem parseBookItem(ConfigurationSection section) {
        return section != null ? BookItem.builder()
                .material(Material.valueOf(section.getString("material")))
                .displayName(color(section.getString("display-name")))
                .lore(section.getStringList("lore").stream().map(GeneralUtils::color).collect(Collectors.toList()))
                .build() : null;
    }

    public static Group parseGroup(ConfigurationSection section) {
        return section != null ? Group.builder()
                .identifier(section.getName())
                .name(color(section.getString("group-name")))
                .format(section.getString("group-lore-format"))
                .color(section.getString("group-color"))
                .bookItem(parseBookItem(section.getConfigurationSection("book-item")))
                .slotsUsed(section.getInt("slots-used"))
                .tinkererExp(section.getInt("tinkerer-exp-per-level"))
                .destroyRateMin(section.getInt("rates.destroy-min"))
                .destroyRateMax(section.getInt("rates.destroy-max"))
                .successRateMin(section.getInt("rates.success-min"))
                .successRateMax(section.getInt("rates.success-max"))
                .build() : null;
    }
}
