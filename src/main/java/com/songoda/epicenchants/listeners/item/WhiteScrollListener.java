package com.songoda.epicenchants.listeners.item;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.utils.itemnbtapi.NBTItem;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
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
            instance.getLocale().getMessage("whitescroll.alreadyapplied")
                    .sendPrefixedMessage(event.getWhoClicked());
            return;
        }

        current.setBoolean("protected", true);
        instance.getLocale().getMessage("whitescrollapplied").sendPrefixedMessage(event.getWhoClicked());

        ItemStack toSet = new ItemBuilder(current.getItem()).addLore(instance.getSpecialItems().getWhiteScrollLore()).build();

        event.getClickedInventory().setItem(event.getSlot(), toSet);
        useItem(event);
    }
}
