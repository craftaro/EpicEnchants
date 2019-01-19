package com.songoda.epicenchants.utils.parser;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Optional;

import static com.songoda.epicenchants.utils.Chat.color;
import static java.util.concurrent.ThreadLocalRandom.current;

public class InventoryParser {
    public static SmartInventory parseBookMenu(EpicEnchants instance, FileConfiguration config) {
        return SmartInventory.builder()
                .title(color(config.getString("title")))
                .size(config.getInt("rows"), 9)
                .provider(new InventoryProvider() {
                    @Override
                    public void init(Player opener, InventoryContents inventoryContents) {
                        if (config.isConfigurationSection("fill")) {
                            inventoryContents.fill(ClickableItem.empty(new ItemBuilder(config.getConfigurationSection("fill")).build()));
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
                                    inventoryContents.set(section.getInt("row"), section.getInt("column"), ClickableItem.of(new ItemBuilder(section).build(), event -> {
                                        Player player = ((Player) event.getWhoClicked());
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
                                    }));
                                });
                    }

                    @Override
                    public void update(Player player, InventoryContents inventoryContents) {

                    }
                })
                .manager(instance.getInventoryManager())
                .build();
    }
}
