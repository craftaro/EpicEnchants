package com.songoda.epicenchants.listeners;

import com.songoda.epicenchants.EpicEnchants;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Nullable;

public class BlackScrollListener {
    private final EpicEnchants instance;

    public BlackScrollListener(EpicEnchants instance) {
        this.instance = instance;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCursor() == null || event.getCurrentItem() == null || event.getAction() != InventoryAction.SWAP_WITH_CURSOR || event.getClickedInventory().getType() == InventoryType.CREATIVE) {
            return;
        }

        @Nullable NBTItem nbtItem = new NBTItem(event.getCursor());
        NBTItem toApplyTo = new NBTItem(event.getCurrentItem());

        if (nbtItem == null) {
            return;
        }

        if (!nbtItem.getBoolean("black-scroll")) {
            return;
        }

        if (toApplyTo.getCompound("enchants") == null || toApplyTo.getCompound("enchants").getKeys().isEmpty()) {
            event.getWhoClicked().sendMessage(instance.getLocale().getMessageWithPrefix("blackscroll.noenchants"));
            return;
        }

        //TODO: Blackscroll

        event.getWhoClicked().sendMessage(instance.getLocale().getMessageWithPrefix("blackscroll.success"));
    }
}
