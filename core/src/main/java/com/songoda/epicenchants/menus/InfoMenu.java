package com.songoda.epicenchants.menus;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.objects.Group;
import com.songoda.epicenchants.utils.FastInv;
import com.songoda.epicenchants.utils.ItemBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.objects.Placeholder.of;
import static com.songoda.epicenchants.utils.GeneralUtils.color;
import static com.songoda.epicenchants.utils.GeneralUtils.getSlot;
import static java.util.Arrays.stream;

public class InfoMenu extends FastInv {
    public InfoMenu(EpicEnchants instance, FileConfiguration config) {
        super(config.getInt("size"), color(config.getString("title")));

        Group group = instance.getGroupManager().getGroup(config.getString("group")).orElseThrow(() -> new IllegalArgumentException("Invalid group: " + config.getString("group")));

        String[] split = config.getString("slots").split(",");
        Set<Integer> slots = stream(split, 0, split.length)
                .filter(StringUtils::isNumeric)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        Iterator<Enchant> enchantIterator = instance.getEnchantManager().getEnchants(group).iterator();
        slots.stream().filter(slot -> enchantIterator.hasNext()).forEach(slot -> {
            Enchant enchant = enchantIterator.next();
            addItem(slot, new ItemBuilder(config.getConfigurationSection("enchant-item"),
                    of("group_color", enchant.getGroup().getColor()),
                    of("enchant", enchant.getIdentifier()),
                    of("description", enchant.getDescription())).build());
        });

        if (config.isConfigurationSection("contents"))
            config.getConfigurationSection("contents").getKeys(false)
                    .stream()
                    .map(s -> "contents." + s)
                    .map(config::getConfigurationSection)
                    .forEach(section -> addItem(getSlot(section.getInt("row"), section.getInt("column")), new ItemBuilder(section).build()));
    }
}
