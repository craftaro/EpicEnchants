package com.songoda.epicenchants.menus;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Group;
import com.songoda.epicenchants.utils.objects.FastInv;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.songoda.epicenchants.objects.Placeholder.of;
import static com.songoda.epicenchants.utils.single.Experience.*;
import static com.songoda.epicenchants.utils.single.GeneralUtils.*;

public class EnchanterMenu extends FastInv {
    public EnchanterMenu(EpicEnchants instance, FileConfiguration config, Player player) {
        super(config.getInt("rows") * 9, color(config.getString("title")));

        if (config.isConfigurationSection("fill")) {
            fill(new ItemBuilder(config.getConfigurationSection("fill")).build());
        }

        config.getConfigurationSection("contents").getKeys(false)
                .stream()
                .map(s -> "contents." + s)
                .map(config::getConfigurationSection)
                .forEach(section -> {
                    double expCost = section.getDouble("exp-cost");
                    double ecoCost = section.getDouble("eco-cost");
                    double xpLeft = expCost - player.getLevel() < 0 ? 0 : expCost - player.getLevel();
                    double ecoLeft = ecoCost - instance.getEconomy().getBalance(player) < 0 ? 0 : ecoCost - instance.getEconomy().getBalance(player);
                    Group group = instance.getGroupManager().getValue(section.getString("group").toUpperCase())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid group set in enchanter: " + section.getString("group")));
                    ItemStack itemStack = new ItemBuilder(section,
                            of("exp_cost", expCost),
                            of("eco_cost", ecoCost),
                            of("exp_left", xpLeft),
                            of("eco_left", ecoLeft)).build();

                    addItem(getSlots(section.getString("slot")), itemStack, event -> {
                        if (!instance.getEconomy().has((player), ecoCost) || getExp(player) < expCost) {
                            instance.getAction().perform(player, "enchanter.cannot-afford");
                            return;
                        }

                        instance.getEconomy().withdrawPlayer(player, ecoCost);
                        instance.getAction().perform(player, "event.purchase.success", of("group-name", group.getName()), of("group_color", group.getColor()));

                        changeExp(player, (int) -expCost);
                        player.getInventory().addItem(instance.getSpecialItems().getMysteryBook(group));
                    });
                });
    }
}

