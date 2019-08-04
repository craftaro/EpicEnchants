package com.songoda.epicenchants;

import co.aikar.commands.BukkitCommandManager;
import com.songoda.epicenchants.economy.Economy;
import com.songoda.epicenchants.economy.PlayerPointsEconomy;
import com.songoda.epicenchants.economy.ReserveEconomy;
import com.songoda.epicenchants.economy.VaultEconomy;
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
import com.songoda.epicenchants.utils.ServerVersion;
import com.songoda.epicenchants.utils.SpecialItems;
import com.songoda.epicenchants.utils.locale.Locale;
import com.songoda.epicenchants.utils.objects.FastInv;
import com.songoda.epicenchants.utils.settings.Setting;
import com.songoda.epicenchants.utils.settings.SettingsManager;
import com.songoda.epicenchants.utils.single.ItemGroup;
import com.songoda.epicenchants.utils.updateModules.LocaleModule;
import com.songoda.update.Plugin;
import com.songoda.update.SongodaUpdate;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.single.GeneralUtils.color;
import static org.bukkit.Bukkit.getConsoleSender;

public class EpicEnchants extends JavaPlugin {

    private static EpicEnchants INSTANCE;

    private EnchantManager enchantManager;
    private InfoManager infoManager;
    private GroupManager groupManager;
    private FileManager fileManager;
    private HookManager hookManager;
    private SettingsManager settingsManager;
    private BukkitCommandManager commandManager;

    private Locale locale;

    private ServerVersion serverVersion = ServerVersion.fromPackageName(Bukkit.getServer().getClass().getPackage().getName());

    private SpecialItems specialItems;
    private Economy economy;
    private EnchantUtils enchantUtils;
    private ItemGroup itemGroup;
    private int version;

    public static EpicEnchants getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

        getConsoleSender().sendMessage(color("&a============================="));
        getConsoleSender().sendMessage(color("&7" + getDescription().getName() + " " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        getConsoleSender().sendMessage(color("&7Action: &aEnabling&7..."));

        // Setup Setting Manager
        this.settingsManager = new SettingsManager(this);
        this.settingsManager.setupConfig();

        // Setup Language
        new Locale(this, "en_US");
        this.locale = Locale.getLocale(getConfig().getString("System.Language Mode"));

        // Running Songoda Updater
        Plugin plugin = new Plugin(this, -1);
        plugin.addModule(new LocaleModule());
        SongodaUpdate.load(plugin);

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

        // Setup Economy
        if (Setting.VAULT_ECONOMY.getBoolean() && pluginManager.isPluginEnabled("Vault"))
            this.economy = new VaultEconomy();
        else if (Setting.RESERVE_ECONOMY.getBoolean() && pluginManager.isPluginEnabled("Reserve"))
            this.economy = new ReserveEconomy();
        else if (Setting.PLAYER_POINTS_ECONOMY.getBoolean() && pluginManager.isPluginEnabled("PlayerPoints"))
            this.economy = new PlayerPointsEconomy();

        // Start Metrics
        new Metrics(this);

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

        this.locale = Locale.getLocale(getConfig().getString("System.Language Mode"));
        this.locale.reloadMessages();
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public boolean isServerVersion(ServerVersion version) {
        return serverVersion == version;
    }

    public boolean isServerVersion(ServerVersion... versions) {
        return ArrayUtils.contains(versions, serverVersion);
    }

    public boolean isServerVersionAtLeast(ServerVersion version) {
        return serverVersion.ordinal() >= version.ordinal();
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

    public Economy getEconomy() {
        return this.economy;
    }

    public EnchantUtils getEnchantUtils() {
        return this.enchantUtils;
    }

    public ItemGroup getItemGroup() {
        return this.itemGroup;
    }

    public int getVersion() {
        return this.version;
    }

    public Locale getLocale() {
        return locale;
    }
}
