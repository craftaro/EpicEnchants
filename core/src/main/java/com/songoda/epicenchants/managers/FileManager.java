package com.songoda.epicenchants.managers;

import com.songoda.epicenchants.EpicEnchants;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.songoda.epicenchants.utils.parser.ConfigParser.parseEnchant;
import static java.io.File.separator;
import static java.util.Arrays.asList;

public class FileManager {
    private final EpicEnchants instance;
    private final Map<String, FileConfiguration> configurationMap;

    public FileManager(EpicEnchants instance) {
        this.instance = instance;
        this.configurationMap = new HashMap<>();
    }

    public void createFiles() {
        File dir = new File(instance.getDataFolder() + separator + "enchants" + separator);

        if (!dir.exists()) {
            File def = new File(instance.getDataFolder() + separator + "enchants" + separator + "StrengthEnchant.yml");
            try {
                FileUtils.copyInputStreamToFile(instance.getResource("ExampleEnchant.yml"), def);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String name : asList("config", "bookMenu")) {
            File file = new File(instance.getDataFolder(), name + ".yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                instance.saveResource(file.getName(), false);
            }
            FileConfiguration configuration = new YamlConfiguration();
            try {
                configuration.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            configurationMap.put(name, configuration);
        }
    }

    public void loadEnchants() {
        getEnchantFiles().ifPresent(list -> list.forEach(this::loadEnchant));
    }

    public FileConfiguration getConfiguration(String key) {
        return configurationMap.get(key);
    }

    public void loadEnchant(File file) {
        try {
            instance.getEnchantManager().addEnchant(parseEnchant(YamlConfiguration.loadConfiguration(file)));
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("Something went wrong loading the enchant from file " + file.getName());
            Bukkit.getConsoleSender().sendMessage("Please check to make sure there are no errors in the file.");
            e.printStackTrace();
        }
    }

    public Optional<File> getEnchantFile(String path) {
        File file = new File(instance.getDataFolder() + separator + "enchants" + separator + path);
        return file.exists() ? Optional.of(file) : Optional.empty();
    }

    public Optional<List<File>> getEnchantFiles() {
        File dir = new File(instance.getDataFolder() + separator + "enchants" + separator);
        File[] files = dir.listFiles((dir1, filename) -> filename.endsWith(".yml"));

        if (files != null)
            return Optional.of(Arrays.asList(files));

        return Optional.empty();
    }
}
