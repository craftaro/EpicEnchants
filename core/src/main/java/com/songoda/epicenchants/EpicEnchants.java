package com.songoda.epicenchants;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.InvalidCommandArgument;
import com.songoda.epicenchants.commands.EnchantCommand;
import com.songoda.epicenchants.managers.EnchantManager;
import com.songoda.epicenchants.managers.FileManager;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.InventoryParser;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.Chat.color;
import static org.bukkit.Bukkit.getConsoleSender;

public class EpicEnchants extends JavaPlugin {

    @Getter private FileManager fileManager;
    @Getter private EnchantManager enchantManager;
    @Getter private BukkitCommandManager commandManager;
    @Getter private InventoryManager inventoryManager;
    @Getter private Economy economy;
    @Getter private Locale locale;
    @Getter private SmartInventory bookInventory;

    @Override
    public void onEnable() {
        getConsoleSender().sendMessage(color("&a============================="));
        getConsoleSender().sendMessage(color("&7" + getDescription().getName() + " " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        getConsoleSender().sendMessage(color("&7Action: &aEnabling&7..."));

        this.fileManager = new FileManager(this);
        this.enchantManager = new EnchantManager();
        this.inventoryManager = new InventoryManager(this);
        this.locale = Locale.getLocale(getConfig().getString("System.Language Mode", getConfig().getString("language")));
        this.economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        fileManager.createFiles();
        fileManager.loadEnchants();
        inventoryManager.init();

        setupCommands();

        if (!enchantManager.getEnchants().isEmpty()) {
            getLogger().info("Successfully loaded: " + enchantManager.getEnchants().stream().map(Enchant::getIdentifier).collect(Collectors.joining(",")));
        }

        this.bookInventory = InventoryParser.parseBookMenu(this, fileManager.getConfiguration("bookMenu"));

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

        commandManager.getCommandContexts().registerContext(Enchant.class, c -> enchantManager.getEnchant(c.getFirstArg()).orElseThrow(() -> new InvalidCommandArgument("Unknown enchant: " + c.getFirstArg())));

        commandManager.registerCommand(new EnchantCommand());
    }
}
