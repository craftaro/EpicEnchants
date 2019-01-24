package com.songoda.epicenchants.utils.parser;

import com.songoda.epicenchants.effect.EffectManager;
import com.songoda.epicenchants.enums.EffectType;
import com.songoda.epicenchants.objects.BookItem;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.objects.LeveledModifier;
import com.songoda.epicenchants.utils.Chat;
import com.songoda.epicenchants.utils.ItemBuilder;
import com.songoda.epicenchants.wrappers.EnchantmentWrapper;
import com.songoda.epicenchants.wrappers.MobWrapper;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.Chat.color;

public class ConfigParser {
    public static Enchant parseEnchant(FileConfiguration config) {
        return Enchant.builder()
                .identifier(config.getString("identifier"))
                .tier(config.getInt("tier"))
                .maxLevel(config.getInt("max-level"))
                .format(color(config.getString("applied-format")))
                .bookItem(parseBookItem(config.getConfigurationSection("book-item")))
                .itemWhitelist(config.getStringList("item-whitelist").stream().map(Material::valueOf).collect(Collectors.toSet()))
                .conflict(new HashSet<>(config.getStringList("conflicting-enchants")))
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
                .build();
    }

    public static MobWrapper parseMobWrapper(ConfigurationSection section) {
        return section != null ? MobWrapper.builder()
                .effectType(EffectType.valueOf(section.getString("effect-type")))
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

    public static BookItem parseBookItem(ConfigurationSection section) {
        return section != null ? BookItem.builder()
                .material(Material.valueOf(section.getString("material")))
                .displayName(color(section.getString("display-name")))
                .lore(section.getStringList("lore").stream().map(Chat::color).collect(Collectors.toList()))
                .build() : null;
    }
}
