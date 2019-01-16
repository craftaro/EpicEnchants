package com.songoda.epicenchants.managers;

import com.songoda.epicenchants.ConfigParser;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.io.File.separator;

public class FileManager {
    private final EpicEnchants instance;

    public FileManager(EpicEnchants instance) {
        this.instance = instance;
    }

    public void loadEnchants() {
        File dir = new File(instance.getDataFolder() + separator + "enchants" + separator);

        if (!dir.exists()) {
            instance.saveResource("StrengthEnchant.yml", false);
        }

        Arrays.stream(dir.listFiles((dir1, filename) -> filename.endsWith(".yml"))).forEach(file -> {
            try {
                instance.getEnchantManager().addEnchant(loadEnchant(YamlConfiguration.loadConfiguration(file)));
            } catch (Exception e) {
                instance.getLogger().severe(ChatColor.RED + "Something went wrong loading the enchant from file " + file.getName());
                instance.getLogger().severe(ChatColor.RED + "Please check to make sure there are no errors in the file.");
                e.printStackTrace();
            }
        });
    }

    private Enchant loadEnchant(FileConfiguration config) {
        return Enchant.builder()
                .identifier(config.getString("identifier"))
                .maxTier(config.getInt("max-tier"))
                .format(config.getString("applied-format"))
                .itemWhitelist(config.getStringList("item-whitelist").stream().map(Material::valueOf).collect(Collectors.toSet()))
                .potionEffects(config.getStringList("potion-effects").stream().map(ConfigParser::parsePotionEffect).collect(Collectors.toSet()))
                .action(ConfigParser.parseActionClass(config.getConfigurationSection("action")))
                .build();
    }
}
