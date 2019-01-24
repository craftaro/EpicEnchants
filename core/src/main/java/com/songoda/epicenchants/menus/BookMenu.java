package com.songoda.epicenchants.menus;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.FastInv;
import com.songoda.epicenchants.utils.ItemBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Optional;

public class BookMenu extends FastInv {
    public BookMenu(EpicEnchants instance, FileConfiguration config) {
        super(config.getInt("rows") * 9);

        if (config.isConfigurationSection("fill")) {
            fill(new ItemBuilder(config.getConfigurationSection("fill")).build());
        }

        config.getConfigurationSection("contents").getKeys(false)
                .stream()
                .filter(StringUtils::isNumeric)
                .map(s -> "contents." + s)
                .map(config::getConfigurationSection)
                .forEach(section -> {
                    double expCost = section.getDouble("exp-cost");
                    double ecoCost = section.getDouble("eco-cost");
                    int tier = section.getInt("tier");
                    addItem(getSlot(section.getInt("row"), section.getInt("column")), new ItemBuilder(section).build(), event -> {
                        Player player = event.getPlayer();

                        if (!instance.getEconomy().has((player), ecoCost) || (player).getLevel() < expCost) {
                            player.sendMessage(instance.getLocale().getPrefix() + instance.getLocale().getMessage("event.purchase.cannotafford"));
                            return;
                        }

                        Optional<Enchant> enchant = instance.getEnchantManager().getRandomEnchant(tier);

                        if (!enchant.isPresent()) {
                            player.sendMessage(instance.getLocale().getPrefix() + instance.getLocale().getMessage("event.purchase.noenchant"));
                            return;
                        }

                        instance.getEconomy().withdrawPlayer(player, ecoCost);
                        player.setLevel((int) (player.getLevel() - expCost));
                        player.getInventory().addItem(enchant.get().getBookItem().get(enchant.get()));
                        player.sendMessage(instance.getLocale().getPrefix() + instance.getLocale().getMessage("event.purchase.success", "" + tier));
                    });
                });
    }

    private int getSlot(int row, int column) {
        if (column > 9 || row < 1) {
            return 0;
        }
        return (row - 1) * 9 + column - 1;
    }
}
