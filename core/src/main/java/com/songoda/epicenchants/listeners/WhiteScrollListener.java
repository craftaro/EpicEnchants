package com.songoda.epicenchants.listeners;

import com.songoda.epicenchants.EpicEnchants;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class WhiteScrollListener implements Listener {
    private final EpicEnchants instance;

    public WhiteScrollListener(EpicEnchants instance) {
        this.instance = instance;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCursor() == null || event.getCurrentItem() == null || event.getAction() != InventoryAction.SWAP_WITH_CURSOR || event.getClickedInventory().getType() == InventoryType.CREATIVE) {
            return;
        }

        NBTItem nbtItem = new NBTItem(event.getCursor());
        NBTItem toApplyTo = new NBTItem(event.getCurrentItem());

        if (nbtItem == null) {
            return;
        }

        if (!nbtItem.getBoolean("white-scroll")) {
            return;
        }

        if (toApplyTo.hasKey("protected")) {
            event.getWhoClicked().sendMessage(instance.getLocale().getMessageWithPrefix("whitescroll.alreadyapplied"));
            return;
        }

        toApplyTo.setBoolean("protected", true);
        event.getWhoClicked().sendMessage(instance.getLocale().getMessageWithPrefix("whitescroll.applied"));

        //TODO: add lore

        event.getWhoClicked().setItemOnCursor(null);
        event.getClickedInventory().setItem(event.getSlot(), toApplyTo.getItem());
    }
}
