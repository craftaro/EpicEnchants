package com.songoda.epicenchants;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.InvalidCommandArgument;
import com.songoda.epicenchants.commands.EnchantCommand;
import com.songoda.epicenchants.enums.GiveType;
import com.songoda.epicenchants.listeners.*;
import com.songoda.epicenchants.managers.EnchantManager;
import com.songoda.epicenchants.managers.FileManager;
import com.songoda.epicenchants.managers.GroupManager;
import com.songoda.epicenchants.managers.InfoManager;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.EnchantUtils;
import com.songoda.epicenchants.utils.FastInv;
import com.songoda.epicenchants.utils.SpecialItems;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.songoda.epicenchants.utils.GeneralUtils.color;
import static org.bukkit.Bukkit.getConsoleSender;

@Getter
public class EpicEnchants extends JavaPlugin {

    private BukkitCommandManager commandManager;
    private Economy economy;
    private EnchantManager enchantManager;
    private InfoManager infoManager;
    private GroupManager groupManager;
    private EnchantUtils enchantUtils;
    private FileManager fileManager;
    private SpecialItems specialItems;
    private Locale locale;

    @Override
    public void onEnable() {
        getConsoleSender().sendMessage(color("&a============================="));
        getConsoleSender().sendMessage(color("&7" + getDescription().getName() + " " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        getConsoleSender().sendMessage(color("&7Action: &aEnabling&7..."));

        Locale.init(this);
        FastInv.init(this);

        this.locale = Locale.getLocale(getConfig().getString("language"));
        this.fileManager = new FileManager(this);
        this.groupManager = new GroupManager(this);
        this.enchantManager = new EnchantManager(this);
        this.enchantUtils = new EnchantUtils(this);
        this.infoManager = new InfoManager(this);
        this.specialItems = new SpecialItems(this);
        this.economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        fileManager.createFiles();
        groupManager.loadGroups();
        enchantManager.loadEnchants();
        infoManager.loadMenus();

        setupCommands();
        setupListeners();

        if (!enchantManager.getEnchants().isEmpty()) {
            getLogger().info("Successfully loaded enchants: " + enchantManager.getEnchants().stream().map(Enchant::getIdentifier).collect(Collectors.joining(", ")));
        }

        getConsoleSender().sendMessage(color("&a============================="));
    }

    @Override
    public void onDisable() {
        getConsoleSender().sendMessage(color("&a============================="));
        getConsoleSender().sendMessage(color("&7" + getDescription().getName() + " " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        getConsoleSender().sendMessage(color("&7Action: &cDisabling&7..."));
        getConsoleSender().sendMessage(color("&a============================="));
    }

    private void setupCommands() {
        this.commandManager = new BukkitCommandManager(this);

        commandManager.registerDependency(EpicEnchants.class, "instance", this);

        commandManager.getCommandCompletions().registerCompletion("enchants", c -> enchantManager.getEnchants().stream().map(Enchant::getIdentifier).collect(Collectors.toList()));
        commandManager.getCommandCompletions().registerCompletion("enchantFiles", c -> fileManager.getYmlFiles("enchants").orElse(Collections.emptyList()).stream().map(File::getName).collect(Collectors.toList()));
        commandManager.getCommandCompletions().registerCompletion("giveType", c -> Arrays.stream(GiveType.values()).map(s -> s.toString().replace("_", "").toLowerCase()).collect(Collectors.toList()));
        commandManager.getCommandCompletions().registerCompletion("levels", c -> IntStream.rangeClosed(1, c.getContextValue(Enchant.class).getMaxLevel()).boxed().map(Objects::toString).collect(Collectors.toList()));
        commandManager.getCommandCompletions().registerCompletion("increment", c -> IntStream.rangeClosed(0, 100).filter(i -> i % 10 == 0).boxed().map(Objects::toString).collect(Collectors.toList()));

        commandManager.getCommandContexts().registerContext(Enchant.class, c -> enchantManager.getEnchant(c.popFirstArg()).orElseThrow(() -> new InvalidCommandArgument("No enchant exists by that name")));
        commandManager.getCommandContexts().registerContext(File.class, c -> enchantManager.getEnchantFile(c.popFirstArg()).orElseThrow(() -> new InvalidCommandArgument("No EnchantFile exists by that name")));

        commandManager.getCommandContexts().registerContext(GiveType.class, c -> Arrays.stream(GiveType.values())
                .filter(s -> s.toString().toLowerCase().replace("_", "").equalsIgnoreCase(c.popFirstArg()))
                .findFirst()
                .orElseThrow(() -> new InvalidCommandArgument("No item by that type.")));

        commandManager.registerCommand(new EnchantCommand());
    }

    private void setupListeners() {
        EpicEnchants instance = this;
        new HashSet<Listener>() {{
            add(new BookListener(instance));
            add(new ArmorListener());
            add(new PlayerListener(instance));
            add(new EntityListener(instance));
            add(new WhiteScrollListener(instance));
        }}.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public void reload() {
        reloadConfig();
        locale.reloadMessages();

        enchantManager.loadEnchants();
        groupManager.loadGroups();
        infoManager.loadMenus();
    }
}
