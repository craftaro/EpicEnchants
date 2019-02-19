package com.songoda.epicenchants.listeners.item;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
            instance.getAction().perform(event.getWhoClicked(), "blackscroll.noenchants");
            return;
        }

        String id = getRandomElement(compound.getKeys());
        int level = compound.getInteger(id);
        Enchant enchant = instance.getEnchantManager().getValueUnsafe(id);
        ItemStack toSet = instance.getEnchantUtils().removeEnchant(event.getCurrentItem(), enchant);

        event.getWhoClicked().getInventory().addItem(enchant.getBook().get(enchant, level, cursor.getInteger("success-rate"), 100));
        event.setCurrentItem(toSet);

        instance.getAction().perform(event.getWhoClicked(), "blackscroll.success");
        useItem(event);
    }
}
