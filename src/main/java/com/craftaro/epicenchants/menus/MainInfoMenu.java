package com.craftaro.epicenchants.menus;

import com.craftaro.epicenchants.EpicEnchants;
import com.craftaro.epicenchants.objects.Group;
import com.craftaro.epicenchants.utils.objects.FastInv;
import com.craftaro.epicenchants.utils.objects.ItemBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import static com.craftaro.epicenchants.utils.single.GeneralUtils.color;
import static com.craftaro.epicenchants.utils.single.GeneralUtils.getSlots;

public class MainInfoMenu extends FastInv implements Listener {
    public MainInfoMenu(EpicEnchants instance, FileConfiguration config) {
        super(config.getInt("rows") * 9, color(config.getString("title")));
        config.getConfigurationSection("contents").getKeys(false)
                .stream()
                .map(s -> "contents." + s)
                .map(config::getConfigurationSection)
                .forEach(section -> addItem(getSlots(section.getString("slot")), new ItemBuilder(section).build(), event -> {
                    if (section.getString("group") == null) {
                        return;
                    }

                    Group group = instance
                            .getGroupManager()
                            .getValue(section.getString("group"))
                            .orElseThrow(() -> new IllegalArgumentException("Invalid group: " + section.getString("group")));

                    instance.getInfoManager()
                            .getMenu(group)
                            .ifPresent(menu -> menu.open(event.getPlayer()));
                }));
    }
}
