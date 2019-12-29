package com.songoda.epicenchants;

import com.songoda.core.SongodaCore;
import com.songoda.core.SongodaPlugin;
import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.configuration.Config;
import com.songoda.core.hooks.EconomyManager;
import com.songoda.epicenchants.command.CommandManager;
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
import com.songoda.epicenchants.utils.Metrics;
import com.songoda.epicenchants.utils.SpecialItems;
import com.songoda.epicenchants.utils.objects.FastInv;
import com.songoda.epicenchants.utils.settings.Setting;
import com.songoda.epicenchants.utils.settings.SettingsManager;
import com.songoda.epicenchants.utils.single.ItemGroup;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.single.GeneralUtils.color;
import static org.bukkit.Bukkit.getConsoleSender;

public class EpicEnchants extends SongodaPlugin {

    private static EpicEnchants INSTANCE;

    private EnchantManager enchantManager;
    private InfoManager infoManager;
    private GroupManager groupManager;
    private FileManager fileManager;
    private HookManager hookManager;
    private SettingsManager settingsManager;
    private CommandManager commandManager;

    private SpecialItems specialItems;
    private EnchantUtils enchantUtils;
    private ItemGroup itemGroup;

    public static EpicEnchants getInstance() {
        return INSTANCE;
    }

    @Override
    public void onPluginLoad() {
        INSTANCE = this;
    }

    @Override
    public void onPluginEnable() {
        // Run Songoda Updater
        SongodaCore.registerPlugin(this, 67, CompatibleMaterial.DIAMOND_SWORD);

        // Setup Setting Manager
        this.settingsManager = new SettingsManager(this);
        this.settingsManager.setupConfig();

        // Setup Language
        this.setLocale(getConfig().getString("System.Language Mode"), false);

        preload();

        this.groupManager = new GroupManager(this);
        this.enchantManager = new EnchantManager(this);
        this.enchantUtils = new EnchantUtils(this);
        this.infoManager = new InfoManager(this);
        this.specialItems = new SpecialItems(this);
        this.commandManager = new CommandManager(this);
        this.hookManager = new HookManager();
        this.itemGroup = new ItemGroup(this);

        groupManager.loadGroups();
        enchantManager.loadEnchants();
        infoManager.loadMenus();
        hookManager.setup();

        PluginManager pluginManager = Bukkit.getPluginManager();

        // Listeners
        pluginManager.registerEvents(new BookListener(this), this);
        pluginManager.registerEvents(new ArmorListener(), this);
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new EntityListener(this), this);
        pluginManager.registerEvents(new WhiteScrollListener(this), this);
        pluginManager.registerEvents(new BlackScrollListener(this), this);
        pluginManager.registerEvents(new DustListener(this), this);

        String economyPlugin = null;

        // Setup Economy
        if (Setting.VAULT_ECONOMY.getBoolean() && pluginManager.isPluginEnabled("Vault"))
            economyPlugin = "Vault";
        else if (Setting.RESERVE_ECONOMY.getBoolean() && pluginManager.isPluginEnabled("Reserve"))
            economyPlugin = "Reserve";
        else if (Setting.PLAYER_POINTS_ECONOMY.getBoolean() && pluginManager.isPluginEnabled("PlayerPoints"))
            economyPlugin = "PlayerPoints";

        EconomyManager.load();
        if (economyPlugin != null)
            EconomyManager.getManager().setPreferredHook(economyPlugin);

        // Start Metrics
        new Metrics(this);

        if (!enchantManager.getValues().isEmpty()) {
            getLogger().info("Successfully loaded enchants: " + enchantManager.getValues().stream().map(Enchant::getIdentifier).collect(Collectors.joining(", ")));
        }
    }

    private void preload() {
        FastInv.init(this);
        this.fileManager = new FileManager(this);
        fileManager.loadFiles();
    }

    @Override
    public void onPluginDisable() {
        getConsoleSender().sendMessage(color("&a============================="));
        getConsoleSender().sendMessage(color("&7" + getDescription().getName() + " " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        getConsoleSender().sendMessage(color("&7Action: &cDisabling&7..."));
        getConsoleSender().sendMessage(color("&a============================="));
    }

    @Override
    public void onConfigReload() {

    }

    @Override
    public List<Config> getExtraConfig() {
        return null;
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

        this.setLocale(getConfig().getString("System.Language Mode"), true);
        this.locale.reloadMessages();
    }

    public EnchantManager getEnchantManager() {
        return this.enchantManager;
    }

    public InfoManager getInfoManager() {
        return this.infoManager;
    }

    public GroupManager getGroupManager() {
        return this.groupManager;
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public HookManager getHookManager() {
        return this.hookManager;
    }

    public SpecialItems getSpecialItems() {
        return this.specialItems;
    }

    public EnchantUtils getEnchantUtils() {
        return this.enchantUtils;
    }

    public ItemGroup getItemGroup() {
        return this.itemGroup;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}
