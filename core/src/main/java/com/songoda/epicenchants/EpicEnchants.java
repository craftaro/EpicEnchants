package com.songoda.epicenchants;

import com.songoda.epicenchants.managers.EnchantManager;
import com.songoda.epicenchants.managers.FileManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicEnchants extends JavaPlugin {

    @Getter private FileManager fileManager;
    @Getter private EnchantManager enchantManager;

    @Override
    public void onDisable() {
        this.fileManager = new FileManager(this);
        this.enchantManager = new EnchantManager();

        saveDefaultConfig();

        try {
            fileManager.loadEnchants();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {

    }
}
