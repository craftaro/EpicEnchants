package com.songoda.epicenchants;

import co.aikar.commands.BukkitCommandManager;
import com.songoda.epicenchants.listeners.ArmorListener;
import com.songoda.epicenchants.listeners.EntityListener;
import com.songoda.epicenchants.listeners.PlayerListener;
import com.songoda.epicenchants.listeners.item.BlackScrollListener;
import com.songoda.epicenchants.listeners.item.BookListener;
import com.songoda.epicenchants.listeners.item.DustListener;
import com.songoda.epicenchants.listeners.item.WhiteScrollListener;
import com.songoda.epicenchants.managers.*;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.EnchantUtils;
import com.songoda.epicenchants.utils.SpecialItems;
import com.songoda.epicenchants.utils.objects.FastInv;
import com.songoda.epicenchants.utils.single.ItemGroup;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.single.GeneralUtils.color;
import static org.bukkit.Bukkit.getConsoleSender;

@Getter
public class EpicEnchants extends JavaPlugin {

    private BukkitCommandManager commandManager;
    private EnchantManager enchantManager;
    private InfoManager infoManager;
    private GroupManager groupManager;
    private FileManager fileManager;
    private HookManager hookManager;

    private SpecialItems specialItems;
    private Action action;
    private Economy economy;
    private EnchantUtils enchantUtils;
    private ItemGroup itemGroup;
    private int version;

    @Override
    public void onEnable() {
        getConsoleSender().sendMessage(color("&a============================="));
        getConsoleSender().sendMessage(color("&7" + getDescription().getName() + " " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        getConsoleSender().sendMessage(color("&7Action: &aEnabling&7..."));

        preload();

        this.action = new Action();
        this.groupManager = new GroupManager(this);
        this.enchantManager = new EnchantManager(this);
        this.enchantUtils = new EnchantUtils(this);
        this.infoManager = new InfoManager(this);
        this.specialItems = new SpecialItems(this);
        this.economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        this.commandManager = new CommandManager(this);
        this.hookManager = new HookManager();
        this.itemGroup = new ItemGroup(this);

        groupManager.loadGroups();
        enchantManager.loadEnchants();
        infoManager.loadMenus();
        action.load(fileManager.getConfiguration("actions"));
        hookManager.setup();

        setupListeners();

        if (!enchantManager.getValues().isEmpty()) {
            getLogger().info("Successfully loaded enchants: " + enchantManager.getValues().stream().map(Enchant::getIdentifier).collect(Collectors.joining(", ")));
        }

        getConsoleSender().sendMessage(color("&a============================="));
    }

    private void preload() {
        FastInv.init(this);
        this.version = Integer.parseInt(Bukkit.getServer().getBukkitVersion().split("\\.")[1]);
        this.fileManager = new FileManager(this);
        fileManager.loadFiles();
    }

    @Override
    public void onDisable() {
        getConsoleSender().sendMessage(color("&a============================="));
        getConsoleSender().sendMessage(color("&7" + getDescription().getName() + " " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        getConsoleSender().sendMessage(color("&7Action: &cDisabling&7..."));
        getConsoleSender().sendMessage(color("&a============================="));
    }

    private void setupListeners() {
        EpicEnchants instance = this;
        new HashSet<Listener>() {{
            add(new BookListener(instance));
            add(new ArmorListener());
            add(new PlayerListener(instance));
            add(new EntityListener(instance));
            add(new WhiteScrollListener(instance));
            add(new BlackScrollListener(instance));
            add(new DustListener(instance));
        }}.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public void reload() {
        reloadConfig();

        fileManager.clear();
        fileManager.loadFiles();

        groupManager.clear();
        groupManager.loadGroups();

        enchantManager.clear();
        enchantManager.loadEnchants();

        infoManager.clear();
        infoManager.loadMenus();

        action.load(fileManager.getConfiguration("actions"));
    }
}
