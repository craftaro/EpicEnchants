package com.songoda.epicenchants.utils.settings;

import com.craftaro.core.configuration.Config;
import com.craftaro.core.configuration.ConfigSetting;
import com.craftaro.core.hooks.EconomyManager;
import com.songoda.epicenchants.EpicEnchants;

import java.util.stream.Collectors;

public class Settings {

    static final Config config = EpicEnchants.getInstance().getCoreConfig();

    public static final ConfigSetting ROMAN = new ConfigSetting(config, "Main.Roman Numerals", true);

    public static final ConfigSetting BLACK_MIN = new ConfigSetting(config, "Main.Black Scroll Min", 20);
    public static final ConfigSetting BLACK_MAX = new ConfigSetting(config, "Main.Black Scroll Max", 100);

    public static final ConfigSetting ECONOMY_PLUGIN = new ConfigSetting(config, "Main.Economy", EconomyManager.getEconomy() == null ? "Vault" : EconomyManager.getEconomy().getName(),
            "Which economy plugin should be used?",
            "Supported plugins you have installed: \"" + EconomyManager.getManager().getRegisteredPlugins().stream().collect(Collectors.joining("\", \"")) + "\".");

    public static final ConfigSetting GLASS_TYPE_1 = new ConfigSetting(config, "Interfaces.Glass Type 1", 7);
    public static final ConfigSetting GLASS_TYPE_2 = new ConfigSetting(config, "Interfaces.Glass Type 2", 11);
    public static final ConfigSetting GLASS_TYPE_3 = new ConfigSetting(config, "Interfaces.Glass Type 3", 3);

    public static final ConfigSetting FIRST_LOAD = new ConfigSetting(config, "System.First Load", true);

    public static final ConfigSetting LANGUGE_MODE = new ConfigSetting(config, "System.Language Mode", "en_US",
            "The enabled language file.",
            "More language files (if available) can be found in the plugins data folder.");

    /**
     * In order to set dynamic economy comment correctly, this needs to be
     * called after EconomyManager load
     */
    public static void setupConfig() {
        config.load();
        config.setAutoremove(true).setAutosave(true);

        // convert economy settings
        if (config.getBoolean("Economy.Use Vault Economy") && EconomyManager.getManager().isEnabled("Vault")) {
            config.set("Main.Economy", "Vault");
        } else if (config.getBoolean("Economy.Use Reserve Economy") && EconomyManager.getManager().isEnabled("Reserve")) {
            config.set("Main.Economy", "Reserve");
        } else if (config.getBoolean("Economy.Use Player Points Economy") && EconomyManager.getManager().isEnabled("PlayerPoints")) {
            config.set("Main.Economy", "PlayerPoints");
        }

        config.saveChanges();
    }
}
