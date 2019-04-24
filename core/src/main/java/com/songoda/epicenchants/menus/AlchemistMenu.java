package com.songoda.epicenchants.menus;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.objects.Group;
import com.songoda.epicenchants.objects.Placeholder;
import com.songoda.epicenchants.utils.objects.FastInv;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import com.songoda.epicenchants.utils.single.GeneralUtils;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

import static com.songoda.epicenchants.objects.Placeholder.of;
import static com.songoda.epicenchants.utils.single.Experience.*;
import static com.songoda.epicenchants.utils.single.GeneralUtils.*;

public class AlchemistMenu extends FastInv {
    private final EpicEnchants instance;
    private final FileConfiguration config;
    private final int LEFT_SLOT, RIGHT_SLOT, PREVIEW_SLOT, ACCEPT_SLOT;
    private final ItemStack PREVIEW_ITEM, ACCEPT_ITEM;

    public AlchemistMenu(EpicEnchants instance, FileConfiguration config) {
        super(config.getInt("rows") * 9, color(config.getString("title")));

        this.instance = instance;
        this.config = config;

        LEFT_SLOT = config.getInt("left-slot");
        RIGHT_SLOT = config.getInt("right-slot");
        PREVIEW_SLOT = config.getInt("preview-slot");
        ACCEPT_SLOT = config.getInt("accept-slot");

        PREVIEW_ITEM = new ItemBuilder(config.getConfigurationSection("contents.preview")).build();
        ACCEPT_ITEM = new ItemBuilder(config.getConfigurationSection("contents.accept-before")).build();

        if (config.isConfigurationSection("fill")) {
            fill(new ItemBuilder(config.getConfigurationSection("fill")).build());
        }

        Set<String> filter = new HashSet<String>() {{
            add("preview");
            add("accept-before");
            add("accept-after");
        }};

        config.getConfigurationSection("contents").getKeys(false)
                .stream()
                .filter(s -> !filter.contains(s))
                .map(s -> "contents." + s)
                .map(config::getConfigurationSection)
                .forEach(section -> addItem(getSlots(config.getString("slot")), new ItemBuilder(section).build()));

        clear(RIGHT_SLOT);
        clear(LEFT_SLOT);

        updateSlots();

        // Player clicked an item in tinkerer
        onClick(event -> {
            if (event.getEvent().getClickedInventory() == null && event.getInventory().equals(this)) {
                return;
            }

            int slot = event.getSlot();

            if (slot != RIGHT_SLOT && slot != LEFT_SLOT) {
                return;
            }

            if (getInventory().getItem(slot) != null && getInventory().getItem(slot).getType() != Material.AIR) {
                event.getPlayer().getInventory().addItem(getInventory().getItem(slot));
                getInventory().clear(slot);
                updateSlots();
            }
        });

        // Player clicked his own inv
        onClick(event -> {
            if (event.getEvent().getClickedInventory() == null || event.getEvent().getClickedInventory().getType() != InventoryType.PLAYER) {
                return;
            }

            ItemStack itemStack = event.getItem();

            if (!handleItem(event.getPlayer(), itemStack)) {
                return;
            }

            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(itemStack.getAmount() - 1);
                return;
            }

            event.getEvent().getClickedInventory().clear(event.getEvent().getSlot());
        });

        // Player closed inventory
        onClose(event -> {
            if (getInventory().getItem(RIGHT_SLOT) != null)
                event.getPlayer().getInventory().addItem(getInventory().getItem(RIGHT_SLOT));
            if (getInventory().getItem(LEFT_SLOT) != null)
                event.getPlayer().getInventory().addItem(getInventory().getItem(LEFT_SLOT));
        });
    }

    private boolean handleItem(Player player, ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        ItemStack toHandle = itemStack.clone();
        toHandle.setAmount(1);

        NBTItem nbtItem = new NBTItem(toHandle);

        if (!nbtItem.hasKey("book-item") && !nbtItem.hasKey("dust")) {
            instance.getAction().perform(player, "alchemist.not-interested");
            return false;
        }

        // Both slots occupied
        if (getInventory().getItem(LEFT_SLOT) != null && getInventory().getItem(RIGHT_SLOT) != null) {
            instance.getAction().perform(player, "alchemist.max-two-items");
            return false;
        }

        int successRate = nbtItem.getInteger("success-rate");

        // Both slots empty
        if (getInventory().getItem(LEFT_SLOT) == null && getInventory().getItem(RIGHT_SLOT) == null) {
            if (nbtItem.hasKey("book-item")) {
                Enchant enchant = instance.getEnchantManager().getValue(nbtItem.getString("enchant")).orElseThrow(() -> new IllegalStateException("Book without enchant!"));
                int level = nbtItem.getInteger("level");

                if (enchant.getMaxLevel() == level) {
                    instance.getAction().perform(player, "alchemist.max-level-book");
                    return false;
                }
            } else {
                Group group = instance.getGroupManager().getValue(nbtItem.getString("group")).orElseThrow(() -> new IllegalStateException("Dust without group!"));

                if (group.getOrder() == instance.getGroupManager().getValues().stream().mapToInt(Group::getOrder).max().orElse(0) || successRate == 100) {
                    instance.getAction().perform(player, "alchemist." + (successRate == 100 ? "max-percentage-dust" : "highest-group-dust"));
                    return false;
                }
            }

            getInventory().setItem(LEFT_SLOT, toHandle);
            return true;
        }

        NBTItem other = new NBTItem(getInventory().getItem(getInventory().getItem(LEFT_SLOT) == null ? RIGHT_SLOT : LEFT_SLOT));
        int emptySlot = getInventory().getItem(LEFT_SLOT) == null ? LEFT_SLOT : RIGHT_SLOT;

        if (other.hasKey("book-item")) {
            if (!nbtItem.getString("enchant").equals(other.getString("enchant"))) {
                instance.getAction().perform(player, "alchemist.different-enchantment");
                return false;
            }

            if (!nbtItem.getInteger("level").equals(other.getInteger("level"))) {
                instance.getAction().perform(player, "alchemist.different-levels");
                return false;
            }
        } else {
            if (!nbtItem.getString("group").equals(other.getString("group"))) {
                instance.getAction().perform(player, "alchemist.different-groups");
                return false;
            }

            if (successRate == 100) {
                instance.getAction().perform(player, "alchemist.max-percentage-dust");
                return false;
            }
        }

        getInventory().setItem(emptySlot, toHandle);
        updateSlots();
        return true;
    }

    private void updateSlots() {
        if (getInventory().getItem(LEFT_SLOT) == null || getInventory().getItem(RIGHT_SLOT) == null) {
            addItem(ACCEPT_SLOT, ACCEPT_ITEM);
            addItem(PREVIEW_SLOT, PREVIEW_ITEM);
            return;
        }

        NBTItem leftItem = new NBTItem(getInventory().getItem(LEFT_SLOT));
        NBTItem rightItem = new NBTItem(getInventory().getItem(RIGHT_SLOT));
        int ecoCost;
        int expCost;

        if (leftItem.hasKey("book-item")) {
            int level = leftItem.getInteger("level");
            Enchant enchant = instance.getEnchantManager().getValue(leftItem.getString("enchant")).orElseThrow(() -> new IllegalStateException("Book without enchant!"));
            int leftSuccess = leftItem.getInteger("success-rate");
            int rightSuccess = rightItem.getInteger("success-rate");
            int leftDestroy = leftItem.getInteger("destroy-rate");
            int rightDestroy = rightItem.getInteger("destroy-rate");

            Placeholder[] placeholders = new Placeholder[]{
                    of("left_success_rate", leftSuccess),
                    of("right_success_rate", rightSuccess),
                    of("left_destroy_rate", leftDestroy),
                    of("right_destroy_rate", rightDestroy),
                    of("max_destroy_rate", Math.max(leftDestroy, rightDestroy)),
                    of("min_destroy_rate", Math.min(leftDestroy, rightDestroy)),
                    of("max_success_rate", Math.max(leftSuccess, rightSuccess)),
                    of("min_success_rate", Math.min(leftSuccess, rightSuccess))
            };

            int successRate = getFromFormula("book.success-rate-formula", placeholders);
            int destroyRate = getFromFormula("book.destroy-rate-formula", placeholders);

            Placeholder[] costPlaceholders = new Placeholder[]{
                    of("group_order_index", enchant.getGroup().getOrder()),
                    of("final_success_rate", successRate),
                    of("final_destroy_rate", destroyRate),
            };

            ecoCost = getFromFormula("book.eco-cost-formula", costPlaceholders);
            expCost = getFromFormula("book.exp-cost-formula", costPlaceholders);

            getInventory().setItem(PREVIEW_SLOT, enchant.getBook().get(enchant, level + 1, successRate, destroyRate));
        } else {
            Group group = instance.getGroupManager().getValue(leftItem.getString("group")).orElseThrow(() -> new IllegalStateException("Dust without group!"));

            Placeholder[] placeholders = new Placeholder[]{
                    of("left_percentage", leftItem.getInteger("percentage")),
                    of("right_percentage", rightItem.getInteger("percentage"))
            };

            int successRate = getFromFormula("dust.percentage-formula", placeholders);

            Placeholder[] costPlaceholders = new Placeholder[]{
                    of("group_order_index", group.getOrder()),
                    of("final_success_rate", successRate),
            };

            ecoCost = getFromFormula("dust.eco-cost-formula", costPlaceholders);
            expCost = getFromFormula("dust.exp-cost-formula", costPlaceholders);

            Group newGroup = instance.getGroupManager().getValues().stream()
                    .filter(s -> s.getOrder() == group.getOrder() + 1)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No group higher than: " + group.getIdentifier()));

            getInventory().setItem(PREVIEW_SLOT, instance.getSpecialItems().getDust(newGroup, "magic", successRate, true));
        }

        addItem(ACCEPT_SLOT, new ItemBuilder(config.getConfigurationSection("contents.accept-after"),
                of("eco_cost", ecoCost),
                of("exp_cost", expCost)
        ).build(), event -> {
            if (!instance.getEconomy().has(event.getPlayer(), ecoCost) || getExp(event.getPlayer()) < expCost) {
                instance.getAction().perform(event.getPlayer(), "alchemist.cannot-afford");
                return;
            }

            instance.getEconomy().withdrawPlayer(event.getPlayer(), ecoCost);
            changeExp(event.getPlayer(), -expCost);
            instance.getAction().perform(event.getPlayer(), "alchemist.success", of("eco_cost", ecoCost), of("exp_cost", expCost));

            event.getPlayer().getInventory().addItem(getInventory().getItem(PREVIEW_SLOT));
            clear(RIGHT_SLOT);
            clear(LEFT_SLOT);
            event.getPlayer().closeInventory();
        });
    }

    private int getFromFormula(String path, Placeholder... placeholders) {
        String toTest = config.getString(path);

        for (Placeholder placeholder : placeholders)
            toTest = toTest.replace(placeholder.getPlaceholder(), placeholder.getToReplace().toString());

        return (int) Double.parseDouble(GeneralUtils.parseJS(toTest, "alchemist expression", 0).toString());
    }
}