package com.songoda.epicenchants.managers;

import com.songoda.epicenchants.EpicEnchants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.io.File.separator;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.tuple.Pair.of;

public class FileManager extends Manager<String, FileConfiguration> {

    private final LinkedHashSet<Pair<String, Boolean>> files = new LinkedHashSet<>(asList(of("menus/main-info-menu.yml", true),
            of("menus/enchanter-menu.yml", true),
            of("menus/tinkerer-menu.yml", true),
            of("menus/groups/simple-menu.yml", false),
            of("menus/groups/unique-menu.yml", false),
            of("menus/groups/elite-menu.yml", false),
            of("menus/groups/ultimate-menu.yml", false),
            of("menus/groups/legendary-menu.yml", false),
            of("enchants/example-enchant.yml", false),
            of("config.yml", true),
            of("groups.yml", true),
            of("actions.yml", true),
            of("items/special-items.yml", true),
            of("items/dusts.yml", true)
    ));

    public FileManager(EpicEnchants instance) {
        super(instance);
    }

    public void loadFiles() {
        Set<String> recentDirs = new HashSet<>();
        files.forEach(pair -> {
            File file = new File(instance.getDataFolder() + separator + pair.getLeft());

            if (!file.exists() && (pair.getRight() || (!file.getParent().equals(instance.getDataFolder().getPath())
                    && (!file.getParentFile().exists() || recentDirs.contains(file.getParent()))))) {
                file.getParentFile().mkdirs();
                recentDirs.add(file.getParent());
                Bukkit.getConsoleSender().sendMessage("Creating file: " + pair.getLeft());
                try {
                    FileUtils.copyInputStreamToFile(instance.getResource(pair.getLeft()), file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (pair.getRight()) {
                FileConfiguration configuration = new YamlConfiguration();
                try {
                    configuration.load(file);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                add(pair.getLeft().replace(".yml", ""), configuration);
            }
        });
    }

    public FileConfiguration getConfiguration(String key) {
        return getValueUnsafe(key);
    }

    public Optional<List<File>> getYmlFiles(String directory) {
        File dir = new File(instance.getDataFolder() + separator + directory + separator);
        File[] files = dir.listFiles((dir1, filename) -> filename.endsWith(".yml"));

        if (files != null)
            return Optional.of(Arrays.asList(files));

        return Optional.empty();
    }
}
