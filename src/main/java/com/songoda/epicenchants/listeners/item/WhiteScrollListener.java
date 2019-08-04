package com.songoda.epicenchants.listeners.item;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WhiteScrollListener extends ItemListener {

    public WhiteScrollListener(EpicEnchants instance) {
        super(instance);
    }

    @Override
    void onApply(InventoryClickEvent event, NBTItem cursor, NBTItem current) {
        if (!cursor.hasKey("white-scroll") || !cursor.getBoolean("white-scroll")) {
            return;
        }

        event.setCancelled(true);

        if (current.hasKey("protected")) {
            instance.getAction().perform(event.getWhoClicked(), "white-scroll.already-applied");
            return;
        }

        current.setBoolean("protected", true);
        instance.getAction().perform(event.getWhoClicked(), "white-scroll.applied");

        ItemStack toSet = new ItemBuilder(current.getItem()).addLore(instance.getSpecialItems().getWhiteScrollLore()).build();

        event.getClickedInventory().setItem(event.getSlot(), toSet);
        useItem(event);
    }
}
