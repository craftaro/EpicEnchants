package com.songoda.epicenchants.menus;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.utils.objects.FastInv;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import static com.songoda.epicenchants.utils.single.GeneralUtils.*;

public class AlchemistMenu extends FastInv {
    public AlchemistMenu(EpicEnchants instance, FileConfiguration config) {
        super(config.getInt("rows") * 9, color(config.getString("title")));

        if (config.isConfigurationSection("fill")) {
            fill(new ItemBuilder(config.getConfigurationSection("fill")).build());
        }

        config.getConfigurationSection("contents").getKeys(false)
                .stream()
                .map(s -> "contents." + s)
                .map(config::getConfigurationSection)
                .forEach(section -> {
                    addItem(getSlots(config.getString("slot")), new ItemBuilder(section).build(), event -> {
                        if (section.getName().equalsIgnoreCase("left-item") || section.getName().equalsIgnoreCase("right-item")) {
                            if (getInventory().getItem(event.getSlot()) != null && getInventory().getItem(event.getSlot()).getType() != Material.AIR) {
                                event.getPlayer().getInventory().addItem(getInventory().getItem(event.getSlot()));
                                getInventory().clear(event.getSlot());
                            }
                        }
                    });
                });
    }
}
