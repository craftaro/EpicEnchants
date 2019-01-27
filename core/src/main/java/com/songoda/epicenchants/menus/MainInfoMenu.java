package com.songoda.epicenchants.menus;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Group;
import com.songoda.epicenchants.utils.FastInv;
import com.songoda.epicenchants.utils.GeneralUtils;
import com.songoda.epicenchants.utils.ItemBuilder;
import org.bukkit.configuration.file.FileConfiguration;

import static com.songoda.epicenchants.utils.GeneralUtils.color;

public class MainInfoMenu extends FastInv {

    public MainInfoMenu(EpicEnchants instance, FileConfiguration config) {
        super(config.getInt("size"), color(config.getString("title")));
        config.getConfigurationSection("contents").getKeys(false)
                .stream()
                .map(s -> "contents." + s)
                .map(config::getConfigurationSection)
                .forEach(section -> {
                    addItem(GeneralUtils.getSlot(section.getInt("row"), section.getInt("column")), new ItemBuilder(section).build(), event -> {
                        Group group = instance.getGroupManager().getGroup(section.getString("group"))
                                .orElseThrow(() -> new IllegalArgumentException("Invalid group: " + section.getString("group")));
                        instance.getInfoManager().getMenu(group).ifPresent(menu -> menu.open(event.getPlayer()));
                    });
                });
    }

}
