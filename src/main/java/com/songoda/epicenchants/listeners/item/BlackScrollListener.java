package com.songoda.epicenchants.listeners.item;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.single.RomanNumber;
import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static com.songoda.epicenchants.objects.Placeholder.of;
import static com.songoda.epicenchants.utils.single.GeneralUtils.getRandomElement;

public class BlackScrollListener extends ItemListener {
    public BlackScrollListener(EpicEnchants instance) {
        super(instance);
    }

    @Override
    void onApply(InventoryClickEvent event, NBTItem cursor, NBTItem current) {
        if (!cursor.hasKey("black-scroll") || !cursor.getBoolean("black-scroll")) {
            return;
        }

        event.setCancelled(true);
        NBTCompound compound = current.getCompound("enchants");

        if (compound == null || compound.getKeys().isEmpty()) {
            instance.getAction().perform(event.getWhoClicked(), "black-scroll.no-enchants");
            return;
        }

        String id = getRandomElement(compound.getKeys());
        int level = compound.getInteger(id);
        Enchant enchant = instance.getEnchantManager().getValueUnsafe(id);
        ItemStack toSet = instance.getEnchantUtils().removeEnchant(event.getCurrentItem(), enchant);

        event.getWhoClicked().getInventory().addItem(enchant.getBook().get(enchant, level, cursor.getInteger("success-rate"), 100));
        event.setCurrentItem(toSet);

        instance.getAction().perform(event.getWhoClicked(), "black-scroll.success",
                of("enchant", enchant.getIdentifier()),
                of("group_color", enchant.getGroup().getColor()),
                of("group_name", enchant.getGroup().getName()),
                of("level", RomanNumber.toRoman(level)));

        useItem(event);
    }
}
