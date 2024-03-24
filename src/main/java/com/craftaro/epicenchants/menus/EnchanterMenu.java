package com.craftaro.epicenchants.menus;

import com.craftaro.core.configuration.Config;
import com.craftaro.core.hooks.EconomyManager;
import com.craftaro.epicenchants.EpicEnchants;
import com.craftaro.epicenchants.objects.Group;
import com.craftaro.epicenchants.objects.Placeholder;
import com.craftaro.epicenchants.utils.objects.FastInv;
import com.craftaro.epicenchants.utils.objects.ItemBuilder;
import com.craftaro.epicenchants.utils.settings.Settings;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.craftaro.epicenchants.utils.single.Experience.changeExp;
import static com.craftaro.epicenchants.utils.single.Experience.getExp;
import static com.craftaro.epicenchants.utils.single.GeneralUtils.color;
import static com.craftaro.epicenchants.utils.single.GeneralUtils.getSlots;

public class EnchanterMenu extends FastInv {
    private final Map<UUID, Long> delay = new HashMap<>();

    public EnchanterMenu(EpicEnchants instance, FileConfiguration config, Player player) {
        super(config.getInt("rows") * 9, color(config.getString("title")));

        if (config.isConfigurationSection("fill")) {
            fill(new ItemBuilder(config.getConfigurationSection("fill")).build());
        }
        String Mode = Settings.MODE.getString();
        config.getConfigurationSection("contents").getKeys(false)
                .stream()
                .map(s -> "contents." + s)
                .map(config::getConfigurationSection)
                .filter(section -> section.get("group") != null)
                .forEach(section -> {
                    int expCost = section.getInt("exp-cost");
                    int ecoCost = section.getInt("eco-cost");
                    int xpLeft = Math.max(expCost - player.getLevel(), 0);
                    double ecoLeft = 0.0d;
                    if (EconomyManager.isEnabled() && Mode.equalsIgnoreCase("ECO"))
                        ecoLeft = ecoCost - EconomyManager.getBalance(player) < 0 ? 0 : ecoCost - EconomyManager.getBalance(player);
                    Group group = instance.getGroupManager().getValue(section.getString("group").toUpperCase())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid group set in enchanter: " + section.getString("group")));
                    ItemStack itemStack = new ItemBuilder(section,
                            Placeholder.of("exp_cost", expCost),
                            Placeholder.of("eco_cost", ecoCost),
                            Placeholder.of("exp_left", xpLeft),
                            Placeholder.of("eco_left", ecoLeft)).build();

                    addItem(getSlots(section.getString("slot")), itemStack, event -> {
                        if (this.delay.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis()) {
                            return;
                        }

                        if (EconomyManager.isEnabled() && Mode.equalsIgnoreCase("ECO") && EconomyManager.getBalance(player) < ecoCost) {
                            instance.getLocale().getMessage("enchanter.cannotafford").sendPrefixedMessage(player);
                            return;
                        }

                        if (Mode.equalsIgnoreCase("EXP") && getExp(player) < expCost) {
                            instance.getLocale().getMessage("enchanter.cannotafford").sendPrefixedMessage(player);
                            return;
                        }
                        switch (Mode.toUpperCase()) {
                            case "ECO":
                                EconomyManager.withdrawBalance(player, ecoCost);
                                event.getPlayer().sendMessage(instance.getLocale().getMessage("enchanter.successeco")
                                        .processPlaceholder("group_name", group.getName())
                                        .processPlaceholder("group_color", group.getColor())
                                        .processPlaceholder("eco_cost", ecoCost)
                                        .getPrefixedMessage());
                                break;
                            case "EXP":
                                changeExp(player, -expCost);
                                event.getPlayer().sendMessage(instance.getLocale().getMessage("enchanter.successexp")
                                        .processPlaceholder("group_name", group.getName())
                                        .processPlaceholder("group_color", group.getColor())
                                        .processPlaceholder("exp_cost", expCost)
                                        .getPrefixedMessage());
                                break;
                            default:
                                System.out.println("Wrong MODE value detected. Use ECO or EXP.");
                                break;
                        }
                        player.getInventory().addItem(instance.getSpecialItems().getMysteryBook(group));
                        this.delay.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() + 120);
                    });
                });

        config.getConfigurationSection("contents").getKeys(false)
                .stream()
                .map(s -> "contents." + s)
                .map(config::getConfigurationSection)
                .filter(section -> section.get("group") == null)
                .forEach(section -> addItem(getSlots(section.getString("slot")), new ItemBuilder(section).build()));
    }
}
