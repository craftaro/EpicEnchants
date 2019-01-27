package com.songoda.epicenchants.managers;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.objects.Group;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.ConfigParser.parseEnchant;
import static java.io.File.separator;

public class EnchantManager {
    private final Map<String, Enchant> enchantMap;
    private final EpicEnchants instance;

    public EnchantManager(EpicEnchants instance) {
        this.instance = instance;
        this.enchantMap = new HashMap<>();
    }

    public Optional<Enchant> getEnchant(String identifier) {
        return Optional.ofNullable(enchantMap.get(identifier));
    }

    public void addEnchant(Enchant enchant) {
        enchantMap.put(enchant.getIdentifier(), enchant);
    }

    public Collection<Enchant> getEnchants(Group group) {
        return Collections.unmodifiableCollection(enchantMap.values().stream().filter(s -> s.getGroup().equals(group)).collect(Collectors.toList()));
    }

    public Optional<Enchant> getRandomEnchant(Group group) {
        Collection<Enchant> tierList = getEnchants(group);
        return tierList.stream().skip((int) (tierList.size() * Math.random())).findFirst();
    }

    public Collection<Enchant> getEnchants() {
        return Collections.unmodifiableCollection(enchantMap.values());
    }


    public Enchant getEnchantUnsafe(String identifier) {
        return getEnchant(identifier).orElse(null);
    }

    public void loadEnchants() {
        instance.getFileManager().getYmlFiles("enchants").ifPresent(list -> list.forEach(file -> {
            try {
                loadEnchant(file);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("Something went wrong loading the enchant from file " + file.getName());
                Bukkit.getConsoleSender().sendMessage("Please check to make sure there are no errors in the file.");
                e.printStackTrace();
            }
        }));
    }

    public void loadEnchant(File file) throws Exception {
        addEnchant(parseEnchant(instance, YamlConfiguration.loadConfiguration(file)));
    }

    public Optional<File> getEnchantFile(String path) {
        File file = new File(instance.getDataFolder() + separator + "enchants" + separator + path);
        return file.exists() ? Optional.of(file) : Optional.empty();
    }

}
