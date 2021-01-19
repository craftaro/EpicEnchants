package com.songoda.epicenchants.listeners.item;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.nms.nbt.NBTCompound;
import com.songoda.core.nms.nbt.NBTItem;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WhiteScrollListener extends ItemListener {

    public WhiteScrollListener(EpicEnchants instance) {
        super(instance);
    }

    @Override
    void onApply(InventoryClickEvent event, NBTItem cursor, NBTItem current) {
        if (!cursor.has("white-scroll") || !cursor.getNBTObject("white-scroll").asBoolean()) {
            return;
        }

        event.setCancelled(true);

        if (current.has("protected")) {
            instance.getLocale().getMessage("whitescroll.alreadyapplied")
                    .sendPrefixedMessage(event.getWhoClicked());
            return;
        }

        if (!instance.getItemGroup().isValid(CompatibleMaterial.getMaterial(event.getCurrentItem())))
            return;

        current.set("protected", true);
        instance.getLocale().getMessage("whitescrollapplied").sendPrefixedMessage(event.getWhoClicked());

        ItemStack toSet = new ItemBuilder(current.finish()).addLore(instance.getSpecialItems().getWhiteScrollLore()).build();

        event.getClickedInventory().setItem(event.getSlot(), toSet);
        useItem(event);
    }
}
