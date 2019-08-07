package com.songoda.epicenchants.managers;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.utils.objects.FileLocation;
import com.songoda.epicenchants.utils.settings.Setting;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

import static com.songoda.epicenchants.utils.objects.FileLocation.of;
import static java.io.File.separator;
import static java.util.Arrays.asList;

public class FileManager extends Manager<String, FileConfiguration> {

    private final String directory;
    private final LinkedHashSet<FileLocation> files = new LinkedHashSet<>(asList(
            of("menus/main-info-menu.yml", true),
            of("menus/enchanter-menu.yml", true, true),
            of("menus/tinkerer-menu.yml", true, true),
            of("menus/alchemist-menu.yml", true, true),
            of("menus/groups/simple-menu.yml", false),
            of("menus/groups/unique-menu.yml", false),
            of("menus/groups/elite-menu.yml", false),
            of("menus/groups/ultimate-menu.yml", false),
            of("menus/groups/legendary-menu.yml", false),

            of("enchants/elite/AntiGravity.yml", false),
            of("enchants/elite/Frozen.yml", false),
            of("enchants/elite/Poison.yml", false),
            of("enchants/elite/RocketEscape.yml", false),
            of("enchants/elite/Shockwave.yml", false),
            of("enchants/elite/Wither.yml", false),

            of("enchants/legendary/DeathBringer.yml", false),
            of("enchants/legendary/DeathGod.yml", false),
            of("enchants/legendary/Enlightened.yml", false),
            of("enchants/legendary/Gears.yml", false),
            of("enchants/legendary/LifeSteal.yml", false),
            of("enchants/legendary/Overload.yml", false),
            of("enchants/legendary/Resist.yml", false),
            of("enchants/legendary/SkillSwipe.yml", false),

            of("enchants/simple/Aquatic.yml", false),
            of("enchants/simple/Confusion.yml", false),
            of("enchants/simple/Experience.yml", false),
            of("enchants/simple/Glowing.yml", false),
            of("enchants/simple/Haste.yml", false),
            of("enchants/simple/Insomnia.yml", false),
            of("enchants/simple/Lightning.yml", false),
            of("enchants/simple/Obliterate.yml", false),
            of("enchants/simple/Oxygenate.yml", false),

            of("enchants/ultimate/Blind.yml", false),
            of("enchants/ultimate/Dodge.yml", false),
            of("enchants/ultimate/Fly.yml", false),
            of("enchants/ultimate/FoodSteal.yml", false),
            of("enchants/ultimate/IceAspect.yml", false),
            of("enchants/ultimate/StormFall.yml", false),

            of("enchants/unique/Berserk.yml", false),
            of("enchants/unique/Decapitation.yml", false),
            of("enchants/unique/Explosive.yml", false),
            of("enchants/unique/FeatherWeight.yml", false),
            of("enchants/unique/Inquisitive.yml", false),
            of("enchants/unique/ObsidianDestroyer.yml", false),
            of("enchants/unique/PlagueCarrier.yml", false),
            of("enchants/unique/Ragdoll.yml", false),
            of("enchants/unique/SelfDestruct.yml", false),

            of("groups.yml", true),
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

            if (!file.exists() && (fileLocation.isRequired() || Setting.FIRST_LOAD.getBoolean())) {
                file.getParentFile().mkdirs();
                Bukkit.getConsoleSender().sendMessage("Creating file: " + fileLocation.getPath());

                try {
                    System.out.println(fileLocation.getResourcePath(directory) + " : " + file.toPath());
                    copy(instance.getResource(fileLocation.getResourcePath(directory)), Files.newOutputStream(file.toPath()));
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

        instance.getConfig().set("System.First Load", false);
        instance.saveConfig();
        instance.getSettingsManager().reloadConfig();
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

    private static void copy(InputStream input, OutputStream output) {
        int n;
        byte[] buffer = new byte[1024 * 4];

        try {
            while ((n = input.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
