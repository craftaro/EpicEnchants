package com.songoda.epicenchants.listeners;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.events.EnchantApplyEvent;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.GeneralUtils;
import de.tr7zw.itemnbtapi.NBTItem;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import static com.songoda.epicenchants.enums.EnchantResult.*;
import static com.songoda.epicenchants.objects.Placeholder.of;

public class BookListener implements Listener {
    private final EpicEnchants instance;

    public BookListener(EpicEnchants instance) {
        this.instance = instance;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCursor() == null || event.getCurrentItem() == null || event.getAction() != InventoryAction.SWAP_WITH_CURSOR || event.getClickedInventory().getType() == InventoryType.CREATIVE) {
            return;
        }

        NBTItem nbtItem = new NBTItem(event.getCursor());
        ItemStack toApplyTo = event.getCurrentItem();

        if (!nbtItem.getBoolean("book-item")) {
            return;
        }

        Enchant enchant = instance.getEnchantManager().getEnchant(nbtItem.getString("enchant")).orElseThrow(() -> new IllegalStateException("Book without enchant!"));

        if (!enchant.getItemWhitelist().contains(toApplyTo.getType())) {
            event.getWhoClicked().sendMessage(instance.getLocale().getMessageWithPrefix("enchant.invalidmaterial", of("enchant", enchant.getIdentifier())));
            return;
        }

        int level = nbtItem.getInteger("level");
        int successRate = nbtItem.getInteger("success-rate");
        int destroyRate = nbtItem.getInteger("destroy-rate");

        EnchantApplyEvent enchantEvent = new EnchantApplyEvent(toApplyTo, enchant, level, successRate, destroyRate);
        Bukkit.getPluginManager().callEvent(enchantEvent);

        if (enchantEvent.isCancelled()) {
            return;
        }

        Pair<ItemStack, EnchantResult> result = instance.getEnchantUtils().apply(toApplyTo, enchant, enchantEvent.getLevel(), enchantEvent.getSuccessRate(), enchantEvent.getDestroyRate());

        event.getWhoClicked().sendMessage(instance.getLocale().getMessageWithPrefix(GeneralUtils.getMessageFromResult(result.getRight()),
                of("enchant", enchant.getIdentifier())));
        event.setCancelled(true);

        if (result.getRight() == BROKEN_FAILURE) {
            event.getClickedInventory().clear(event.getSlot());
        }

        if (result.getRight() != CONFLICT && result.getRight() != MAXED_OUT) {
            if (event.getCursor().getAmount() > 1) {
                ItemStack toSet = event.getCursor();
                toSet.setAmount(toSet.getAmount() - 1);
                event.getWhoClicked().setItemOnCursor(toSet);
            } else {
                event.getWhoClicked().setItemOnCursor(null);
            }
        }

        event.getClickedInventory().setItem(event.getSlot(), result.getLeft());

    }
}
