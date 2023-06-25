package com.songoda.epicenchants;

import com.craftaro.core.SongodaCore;
import com.craftaro.core.SongodaPlugin;
import com.craftaro.core.commands.CommandManager;
import com.craftaro.core.compatibility.CompatibleMaterial;
import com.craftaro.core.configuration.Config;
import com.craftaro.core.gui.GuiManager;
import com.craftaro.core.hooks.EconomyManager;
import com.craftaro.core.third_party.com.cryptomorin.xseries.XMaterial;
import com.songoda.epicenchants.commands.CommandAlchemist;
import com.songoda.epicenchants.commands.CommandApply;
import com.songoda.epicenchants.commands.CommandEnchanter;
import com.songoda.epicenchants.commands.CommandGiveBook;
import com.songoda.epicenchants.commands.CommandGiveItemDust;
import com.songoda.epicenchants.commands.CommandGiveRandomBook;
import com.songoda.epicenchants.commands.CommandGiveScroll;
import com.songoda.epicenchants.commands.CommandList;
import com.songoda.epicenchants.commands.CommandReload;
import com.songoda.epicenchants.commands.CommandSettings;
import com.songoda.epicenchants.commands.CommandTinkerer;
import com.songoda.epicenchants.listeners.ArmorListener;
import com.songoda.epicenchants.listeners.EntityListener;
import com.songoda.epicenchants.listeners.HeldItemListener;
import com.songoda.epicenchants.listeners.PlayerListener;
import com.songoda.epicenchants.listeners.item.BlackScrollListener;
import com.songoda.epicenchants.listeners.item.BookListener;
import com.songoda.epicenchants.listeners.item.DustListener;
import com.songoda.epicenchants.listeners.item.WhiteScrollListener;
import com.songoda.epicenchants.managers.EnchantManager;
import com.songoda.epicenchants.managers.FileManager;
import com.songoda.epicenchants.managers.GroupManager;
import com.songoda.epicenchants.managers.HookManager;
import com.songoda.epicenchants.managers.InfoManager;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.EnchantUtils;
import com.songoda.epicenchants.utils.SpecialItems;
import com.songoda.epicenchants.utils.objects.FastInv;
import com.songoda.epicenchants.utils.settings.Settings;
import com.songoda.epicenchants.utils.single.ItemGroup;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.stream.Collectors;

public class EpicEnchants extends SongodaPlugin {

    private static EpicEnchants INSTANCE;

    private final GuiManager guiManager = new GuiManager(this);
    private EnchantManager enchantManager;
    private InfoManager infoManager;
    private GroupManager groupManager;
    private FileManager fileManager;
    private HookManager hookManager;
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
        SongodaCore.registerPlugin(this, 67, XMaterial.DIAMOND_SWORD);

        // setup commands
        this.commandManager = new com.craftaro.core.commands.CommandManager(this);
        this.commandManager.addMainCommand("ee")
                .addSubCommand(new CommandReload(this))
                .addSubCommand(new CommandAlchemist(this))
                .addSubCommand(new CommandApply(this))
                .addSubCommand(new CommandEnchanter(this))
                .addSubCommand(new CommandGiveBook(this))
                .addSubCommand(new CommandGiveItemDust(this))
                .addSubCommand(new CommandGiveRandomBook(this))
                .addSubCommand(new CommandGiveScroll(this))
                .addSubCommand(new CommandList(this))
                .addSubCommand(new CommandSettings(this))
                .addSubCommand(new CommandTinkerer(this));

        EconomyManager.load();

        // Setup Config
        Settings.setupConfig();
        this.setLocale(Settings.LANGUGE_MODE.getString(), false);

        EconomyManager.getManager().setPreferredHook(Settings.ECONOMY_PLUGIN.getString());

        preload();

        this.groupManager = new GroupManager(this);
        this.enchantManager = new EnchantManager(this);
        this.enchantUtils = new EnchantUtils(this);
        this.infoManager = new InfoManager(this);
        this.specialItems = new SpecialItems(this);
        this.commandManager = new CommandManager(this);
        this.hookManager = new HookManager();
        this.itemGroup = new ItemGroup();

        groupManager.loadGroups();
        enchantManager.loadEnchants();
        infoManager.loadMenus();
        hookManager.setup();

        // Listeners
        guiManager.init();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BookListener(this), this);
        pluginManager.registerEvents(new ArmorListener(), this);
        pluginManager.registerEvents(new HeldItemListener(), this);
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new EntityListener(this), this);
        pluginManager.registerEvents(new WhiteScrollListener(this), this);
        pluginManager.registerEvents(new BlackScrollListener(this), this);
        pluginManager.registerEvents(new DustListener(this), this);

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
    public void onDataLoad() {
    }

    @Override
    public void onPluginDisable() {
    }

    @Override
    public void onConfigReload() {
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

    @Override
    public List<Config> getExtraConfig() {
        return null;
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

    public GuiManager getGuiManager() {
        return guiManager;
    }
}
