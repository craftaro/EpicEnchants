package com.craftaro.epicenchants;

import com.craftaro.core.SongodaCore;
import com.craftaro.core.SongodaPlugin;
import com.craftaro.core.commands.CommandManager;
import com.craftaro.core.configuration.Config;
import com.craftaro.core.gui.GuiManager;
import com.craftaro.core.hooks.EconomyManager;
import com.craftaro.epicenchants.commands.CommandAlchemist;
import com.craftaro.epicenchants.commands.CommandApply;
import com.craftaro.epicenchants.commands.CommandEnchanter;
import com.craftaro.epicenchants.commands.CommandGiveBook;
import com.craftaro.epicenchants.commands.CommandGiveItemDust;
import com.craftaro.epicenchants.commands.CommandGiveRandomBook;
import com.craftaro.epicenchants.commands.CommandGiveScroll;
import com.craftaro.epicenchants.commands.CommandList;
import com.craftaro.epicenchants.commands.CommandReload;
import com.craftaro.epicenchants.commands.CommandSettings;
import com.craftaro.epicenchants.commands.CommandTinkerer;
import com.craftaro.epicenchants.listeners.ArmorListener;
import com.craftaro.epicenchants.listeners.EntityListener;
import com.craftaro.epicenchants.listeners.HeldItemListener;
import com.craftaro.epicenchants.listeners.PlayerListener;
import com.craftaro.epicenchants.listeners.item.BlackScrollListener;
import com.craftaro.epicenchants.listeners.item.BookListener;
import com.craftaro.epicenchants.listeners.item.DustListener;
import com.craftaro.epicenchants.listeners.item.WhiteScrollListener;
import com.craftaro.epicenchants.managers.EnchantManager;
import com.craftaro.epicenchants.managers.FileManager;
import com.craftaro.epicenchants.managers.GroupManager;
import com.craftaro.epicenchants.managers.InfoManager;
import com.craftaro.epicenchants.objects.Enchant;
import com.craftaro.epicenchants.utils.EnchantUtils;
import com.craftaro.epicenchants.utils.SpecialItems;
import com.craftaro.epicenchants.utils.objects.FastInv;
import com.craftaro.epicenchants.utils.settings.Settings;
import com.craftaro.epicenchants.utils.single.ItemGroup;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.stream.Collectors;

public class EpicEnchants extends SongodaPlugin {
    private final GuiManager guiManager = new GuiManager(this);
    private EnchantManager enchantManager;
    private InfoManager infoManager;
    private GroupManager groupManager;
    private FileManager fileManager;
    private CommandManager commandManager;

    private SpecialItems specialItems;
    private EnchantUtils enchantUtils;
    private ItemGroup itemGroup;

    @Override
    public void onPluginLoad() {
    }

    @Override
    public void onPluginEnable() {
        SongodaCore.registerPlugin(this, 67, XMaterial.DIAMOND_SWORD);

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
        this.itemGroup = new ItemGroup();

        this.groupManager.loadGroups();
        this.enchantManager.loadEnchants();
        this.infoManager.loadMenus();

        // Listeners
        this.guiManager.init();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BookListener(this), this);
        pluginManager.registerEvents(new ArmorListener(), this);
        pluginManager.registerEvents(new HeldItemListener(), this);
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new EntityListener(this), this);
        pluginManager.registerEvents(new WhiteScrollListener(this), this);
        pluginManager.registerEvents(new BlackScrollListener(this), this);
        pluginManager.registerEvents(new DustListener(this), this);

        if (!this.enchantManager.getValues().isEmpty()) {
            getLogger().info("Successfully loaded enchants: " + this.enchantManager.getValues().stream().map(Enchant::getIdentifier).collect(Collectors.joining(", ")));
        }
    }

    private void preload() {
        FastInv.init(this);
        this.fileManager = new FileManager(this);
        this.fileManager.loadFiles();
    }

    @Override
    public void onDataLoad() {
    }

    @Override
    public void onPluginDisable() {
    }

    @Override
    public void onConfigReload() {
        this.fileManager.clear();
        this.fileManager.loadFiles();

        this.groupManager.clear();
        this.groupManager.loadGroups();

        this.enchantManager.clear();
        this.enchantManager.loadEnchants();

        this.infoManager.clear();
        this.infoManager.loadMenus();

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
        return this.commandManager;
    }

    public GuiManager getGuiManager() {
        return this.guiManager;
    }

    /**
     * @deprecated Use {@link EpicEnchants#getPlugin(Class)} instead
     */
    @Deprecated
    public static EpicEnchants getInstance() {
        return EpicEnchants.getPlugin(EpicEnchants.class);
    }
}
