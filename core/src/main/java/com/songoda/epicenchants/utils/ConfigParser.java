package com.songoda.epicenchants.utils;

import com.songoda.epicenchants.objects.ActionClass;
import com.songoda.epicenchants.objects.BookItem;
import com.songoda.epicenchants.wrappers.*;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.Chat.color;

public class ConfigParser {
    public static ActionClass parseActionClass(ConfigurationSection section) {
        return ActionClass.builder()
                .modifyDamageGiven(section.getDouble("modify-damage-given"))
                .modifyDamageTaken(section.getDouble("modify-damage-taken"))
                .potionEffects(section.getConfigurationSection("potion-effects")
                        .getKeys(false).stream()
                        .map(s -> "potion-effects." + s)
                        .map(section::getConfigurationSection)
                        .map(ConfigParser::parsePotionChanceEffect)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static PotionChanceWrapper parsePotionChanceEffect(ConfigurationSection section) {
        return PotionChanceWrapper.chanceBuilder()
                .type(PotionEffectType.getByName(section.getName()))
                .amplifier(section.getString("amplifier"))
                .duration(section.getString("duration"))
                .chance(section.getDouble("chance"))
                .build();
    }

    public static PotionEffectWrapper parsePotionEffect(ConfigurationSection section) {
        return PotionEffectWrapper.builder()
                .type(PotionEffectType.getByName(section.getName()))
                .amplifier(section.getString("amplifier"))
                .duration(section.getString("duration"))
                .build();
    }

    public static MobWrapper parseMobWrapper(ConfigurationSection section) {
        return MobWrapper.builder()
                .amount(section.getInt("amount"))
                .spawnPercentage(section.getDouble("spawn-percentage"))
                .health(section.getInt("health"))
                .attackDamage(section.getDouble("attack-damage"))
                .hostile(section.getBoolean("hostile"))
                .displayName(color(section.getString("display-name")))
                .helmet(new ItemBuilder(section.getConfigurationSection("armor.helmet")).build())
                .leggings(new ItemBuilder(section.getConfigurationSection("armor.chest-plate")).build())
                .chestPlate(new ItemBuilder(section.getConfigurationSection("armor.leggings")).build())
                .boots(new ItemBuilder(section.getConfigurationSection("armor.boots")).build())
                .build();
    }

    public static EnchantmentWrapper parseEnchantmentWrapper(String key) {
        return EnchantmentWrapper.builder()
                .amplifier(key.contains(":") ? key.split(":")[1] : "")
                .enchantment(Enchantment.getByName(key.split(":")[0]))
                .build();
    }

    public static BookItem parseBookItem(ConfigurationSection section) {
        return BookItem.builder()
                .material(Material.valueOf(section.getString("material")))
                .displayName(color(section.getString("display-name")))
                .lore(section.getStringList("lore").stream().map(Chat::color).collect(Collectors.toList()))
                .build();
    }
}
