package com.songoda.epicenchants.managers;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.utils.objects.FileLocation;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

import static com.songoda.epicenchants.utils.objects.FileLocation.of;
import static java.io.File.separator;
import static java.util.Arrays.asList;

public class FileManager extends Manager<String, FileConfiguration> {

    private final String directory;
    private final LinkedHashSet<FileLocation> files = new LinkedHashSet<>(asList(
            of("config.yml", true),
            of("src/main/resources/menus/main-info-menu.yml", true),
            of("menus/enchanter-menu.yml", true, true),
            of("menus/tinkerer-menu.yml", true, true),
            of("menus/alchemist-menu.yml", true, true),
            of("src/main/resources/menus/groups/simple-menu.yml", false),
            of("src/main/resources/menus/groups/unique-menu.yml", false),
            of("src/main/resources/menus/groups/elite-menu.yml", false),
            of("src/main/resources/menus/groups/ultimate-menu.yml", false),
            of("src/main/resources/menus/groups/legendary-menu.yml", false),
            of("src/main/resources/enchants/example-enchant.yml", false),
            of("src/main/resources/groups.yml", true),
            of("items/special-items.yml", true, true),
            of("items/dusts.yml", true, true)
    ));

    public FileManager(EpicEnchants instance) {
        super(instance);
        directory = instance.getVersion() > 12 ? "master" : "legacy";
        Bukkit.getConsoleSender().sendMessage("Using the " + directory + " configurations because version is 1." + instance.getVersion());
    }

    public void loadFiles() {
        files.forEach(fileLocation -> {
            File file = new File(instance.getDataFolder() + separator + fileLocation.getPath());

            if (!file.exists() && (fileLocation.isRequired() || getConfiguration("config").getBoolean("System.First Load"))) {
                file.getParentFile().mkdirs();
                Bukkit.getConsoleSender().sendMessage("Creating file: " + fileLocation.getPath());

                try {
                    InputStream in = instance.getResource(fileLocation.getResourcePath(directory));
                    if (in != null)
                        Files.copy(in, file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileLocation.isRequired()) {
                FileConfiguration configuration = new YamlConfiguration();
                try {
                    configuration.load(file);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                add(fileLocation.getPath().replace(".yml", ""), configuration);
            }
        });

        getConfiguration("config").set("System.First Load", false);
        try {
            getConfiguration("config").save(new File(instance.getDataFolder() + separator + "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfiguration(String key) {
        return getValueUnsafe(key);
    }

    public List<File> getYmlFiles(String directory) {
        File dir = new File(instance.getDataFolder() + separator + directory + separator);
        File[] allFiles = dir.listFiles();
        List<File> output = new ArrayList<>();

        if (allFiles == null) {
            return output;
        }

        Optional.ofNullable(dir.listFiles((dir1, filename) -> filename.endsWith(".yml"))).ifPresent(list -> {
            output.addAll(Arrays.asList(list));
        });

        Arrays.stream(allFiles)
                .filter(File::isDirectory)
                .filter(s -> !s.getName().equalsIgnoreCase("old"))
                .forEach(f -> output.addAll(getYmlFiles(directory + separator + f.getName())));

        return output;
    }
}
