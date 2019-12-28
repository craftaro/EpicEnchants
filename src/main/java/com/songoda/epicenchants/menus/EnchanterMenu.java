package com.songoda.epicenchants.menus;

import com.songoda.core.hooks.EconomyManager;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Group;
import com.songoda.epicenchants.utils.objects.FastInv;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.songoda.epicenchants.objects.Placeholder.of;
import static com.songoda.epicenchants.utils.single.Experience.*;
import static com.songoda.epicenchants.utils.single.GeneralUtils.*;

public class EnchanterMenu extends FastInv {
    private final Map<UUID, Long> DELAY = new HashMap<>();

    public EnchanterMenu(EpicEnchants instance, FileConfiguration config, Player player) {
        super(config.getInt("rows") * 9, color(config.getString("title")));

        if (config.isConfigurationSection("fill")) {
            fill(new ItemBuilder(config.getConfigurationSection("fill")).build());
        }

        config.getConfigurationSection("contents").getKeys(false)
                .stream()
                .map(s -> "contents." + s)
                .map(config::getConfigurationSection)
                .filter(s -> s.get("group") != null)
                .forEach(section -> {
                    int expCost = section.getInt("exp-cost");
                    int ecoCost = section.getInt("eco-cost");
                    int xpLeft = Math.max(expCost - player.getLevel(), 0);
                    double ecoLeft = 0.0d;
                    if (EconomyManager.isEnabled())
                        ecoLeft = ecoCost - EconomyManager.getBalance(player) < 0 ? 0 : ecoCost - EconomyManager.getBalance(player);
                    Group group = instance.getGroupManager().getValue(section.getString("group").toUpperCase())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid group set in enchanter: " + section.getString("group")));
                    ItemStack itemStack = new ItemBuilder(section,
                            of("exp_cost", expCost),
                            of("eco_cost", ecoCost),
                            of("exp_left", xpLeft),
                            of("eco_left", ecoLeft)).build();

                    addItem(getSlots(section.getString("slot")), itemStack, event -> {
                        // Todo: wanna change this
                        if (DELAY.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis()) {
                            return;
                        }

                        if (EconomyManager.isEnabled() && !EconomyManager.hasBalance((player), ecoCost) || getExp(player) < expCost) {
                            instance.getLocale().getMessage("enchanter.cannotafford").sendPrefixedMessage(player);
                            return;
                        }

                        if (EconomyManager.isEnabled()) {
                            EconomyManager.withdrawBalance(player, ecoCost);
                            instance.getLocale().getMessage("enchanter.success")
                                    .processPlaceholder("group_name", group.getName())
                                    .processPlaceholder("group_color", group.getColor())
                                    .processPlaceholder("eco_cost", ecoCost)
                                    .processPlaceholder("exp_cost", expCost)
                                    .sendPrefixedMessage(player);
                        }

                        changeExp(player, -expCost);
                        player.getInventory().addItem(instance.getSpecialItems().getMysteryBook(group));
                        DELAY.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() + 120);
                    });
                });

        config.getConfigurationSection("contents").getKeys(false)
                .stream()
                .map(s -> "contents." + s)
                .map(config::getConfigurationSection)
                .filter(s -> s.get("group") == null)
                .forEach(section -> addItem(getSlots(section.getString("slot")), new ItemBuilder(section).build()));
    }
}

