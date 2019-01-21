package com.songoda.epicenchants.utils.parser;

import com.songoda.epicenchants.enums.MaterialType;
import com.songoda.epicenchants.objects.ActionClass;
import com.songoda.epicenchants.objects.BookItem;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.objects.LeveledModifier;
import com.songoda.epicenchants.utils.Chat;
import com.songoda.epicenchants.utils.ItemBuilder;
import com.songoda.epicenchants.wrappers.EnchantmentWrapper;
import com.songoda.epicenchants.wrappers.MobWrapper;
import com.songoda.epicenchants.wrappers.PotionChanceWrapper;
import com.songoda.epicenchants.wrappers.PotionEffectWrapper;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.Chat.color;

public class ConfigParser {
    public static Enchant parseEnchant(FileConfiguration config) {
        return Enchant.builder()
                .identifier(config.getString("identifier"))
                .tier(config.getInt("tier"))
                .materialType(MaterialType.of(config.getString("type")))
                .maxLevel(config.getInt("max-level"))
                .format(color(config.getString("applied-format")))
                .action(parseActions(config))
                .bookItem(parseBookItem(config.getConfigurationSection("book-item")))
                .itemWhitelist(config.getStringList("item-whitelist").stream().map(Material::valueOf).collect(Collectors.toSet()))
                .potionEffects(config.getConfigurationSection("potion-effect").getKeys(false).stream()
                        .map(s -> "potion-effect." + s)
                        .map(config::getConfigurationSection)
                        .map(ConfigParser::parsePotionEffect)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static ActionClass parseActions(ConfigurationSection section) {
        return section != null ? ActionClass.builder()
                .modifyDamage(LeveledModifier.of(section.getString("modify-damage")))
                .potionEffectsWearer(ConfigParser.getPotionChanceSet(section.getConfigurationSection("potion-effect-wearer")))
                .potionEffectOpponent(ConfigParser.getPotionChanceSet(section.getConfigurationSection("potion-effect-opponent")))
                .mobs(section.getConfigurationSection("mobs").getKeys(false).stream()
                        .map(s -> "mobs." + s)
                        .map(section::getConfigurationSection)
                        .map(ConfigParser::parseMobWrapper).collect(Collectors.toSet()))
                .build() : null;
    }

    private static Set<PotionChanceWrapper> getPotionChanceSet(ConfigurationSection section) {
        return section != null ? section.getKeys(false).stream()
                .map(section::getConfigurationSection)
                .map(ConfigParser::parsePotionChanceEffect)
                .collect(Collectors.toSet()) : Collections.emptySet();
    }

    public static PotionChanceWrapper parsePotionChanceEffect(ConfigurationSection section) {
        return section != null ? PotionChanceWrapper.chanceBuilder()
                .type(PotionEffectType.getByName(section.getName()))
                .amplifier(LeveledModifier.of(section.getString("amplifier")))
                .duration(LeveledModifier.of(section.getString("duration")))
                .chance(LeveledModifier.of(section.getString("chance")))
                .build() : null;
    }

    public static PotionEffectWrapper parsePotionEffect(ConfigurationSection section) {
        return section != null ? PotionEffectWrapper.builder()
                .type(PotionEffectType.getByName(section.getName()))
                .amplifier(LeveledModifier.of(section.getString("amplifier")))
                .duration(LeveledModifier.of(section.getString("duration")))
                .build() : null;
    }

    public static MobWrapper parseMobWrapper(ConfigurationSection section) {
        return section != null ? MobWrapper.builder()
                .entityType(EntityType.valueOf(section.getName()))
                .amount(section.getInt("max-amount"))
                .spawnPercentage(LeveledModifier.of(section.getString("spawn-percentage")))
                .health(section.getInt("health"))
                .attackDamage(section.getDouble("attack-damage"))
                .hostile(section.getBoolean("hostile"))
                .displayName(color(section.getString("display-name")))
                .helmet(section.isConfigurationSection("equipment.helmet") ? new ItemBuilder(section.getConfigurationSection("equipment.helmet")) : null)
                .chestPlate(section.isConfigurationSection("equipment.chestplate") ? new ItemBuilder(section.getConfigurationSection("equipment.chestplate")) : null)
                .leggings(section.isConfigurationSection("equipment.leggings") ? new ItemBuilder(section.getConfigurationSection("equipment.leggings")) : null)
                .boots(section.isConfigurationSection("equipment.boots") ? new ItemBuilder(section.getConfigurationSection("equipment.boots")) : null)
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
