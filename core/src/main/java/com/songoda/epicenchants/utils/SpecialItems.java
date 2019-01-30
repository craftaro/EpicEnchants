package com.songoda.epicenchants.utils;

import com.songoda.epicenchants.EpicEnchants;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

import static com.songoda.epicenchants.objects.Placeholder.of;

public class SpecialItems {
    private final EpicEnchants instance;

    public SpecialItems(EpicEnchants instance) {
        this.instance = instance;
    }

    public ItemStack getWhiteScroll(Integer amount) {
        NBTItem nbtItem = new ItemBuilder(instance.getFileManager().getConfiguration("special-items").getConfigurationSection("white-scroll")).nbt();
        nbtItem.setBoolean("white-scroll", true);
        ItemStack itemStack = nbtItem.getItem();

        if (amount != null) {
            itemStack.setAmount(amount);
        }

        return itemStack;
    }

    public ItemStack getBlackScroll(Integer amount, Integer chance) {
        int percentage = chance == null ? ThreadLocalRandom.current().nextInt(instance.getConfig().getInt("rates.black-scroll-min"), instance.getConfig().getInt("rates.black-scroll-max") + 1) : chance;
        NBTItem nbtItem = new ItemBuilder(instance.getFileManager().getConfiguration("special-items").getConfigurationSection("black-scroll"), of("percentage", percentage)).nbt();

        nbtItem.setBoolean("black-scroll", true);
        nbtItem.setInteger("percentage", percentage);

        ItemStack itemStack = nbtItem.getItem();

        if (amount != null) {
            itemStack.setAmount(amount);
        }

        return itemStack;
    }
}
