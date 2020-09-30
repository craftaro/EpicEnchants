package com.songoda.epicenchants.utils;

import com.songoda.core.nms.nbt.NBTItem;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Group;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import com.songoda.epicenchants.utils.settings.Settings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

import static com.songoda.epicenchants.objects.Placeholder.of;
import static com.songoda.epicenchants.utils.single.GeneralUtils.color;

public class SpecialItems {
    private final EpicEnchants instance;

    public SpecialItems(EpicEnchants instance) {
        this.instance = instance;
    }

    public ItemStack getWhiteScroll(int amount) {
        NBTItem nbtItem = new ItemBuilder(instance.getFileManager().getConfiguration("items/special-items").getConfigurationSection("white-scroll")).nbt();
        nbtItem.set("white-scroll", true);
        ItemStack itemStack = nbtItem.finish();

        itemStack.setAmount(amount);

        return itemStack;
    }

    public ItemStack getBlackScroll(int amount, int chance) {
        int successRate = chance == -1 ? ThreadLocalRandom.current().nextInt(Settings.BLACK_MIN.getInt(), Settings.BLACK_MAX.getInt() + 1) : chance;
        NBTItem nbtItem = new ItemBuilder(instance.getFileManager().getConfiguration("items/special-items").getConfigurationSection("black-scroll"), of("success-rate", successRate)).nbt();

        nbtItem.set("black-scroll", true);
        nbtItem.set("success-rate", successRate);

        ItemStack itemStack = nbtItem.finish();

        itemStack.setAmount(amount);

        return itemStack;
    }

    public ItemStack getMysteryBook(Group group) {
        NBTItem nbtItem = new ItemBuilder(instance.getFileManager().getConfiguration("items/special-items").getConfigurationSection("mystery-book"),
                of("group-color", group.getColor()),
                of("group-name", group.getName())).nbt();

        nbtItem.set("mystery-book", true);
        nbtItem.set("group", group.getIdentifier());
        return nbtItem.finish();
    }


    public ItemStack getSecretDust(NBTItem book) {
        Group group = instance.getEnchantManager().getValueUnsafe(book.getNBTObject("enchant").asString()).getGroup();
        return getSecretDust(group, (int) Math.floor(book.getNBTObject("success-rate").asInt() / 10.0));
    }

    public ItemStack getSecretDust(Group group, int max) {
        NBTItem nbtItem = new ItemBuilder(instance.getFileManager().getConfiguration("items/dusts").getConfigurationSection("secret-dust"),
                of("group-color", group.getColor()),
                of("group-name", group.getName()),
                of("max-rate", max),
                of("min-rate", 0)).nbt();

        nbtItem.set("secret-dust", true);
        nbtItem.set("group", group.getIdentifier());
        nbtItem.set("max-rate", max + 1);
        nbtItem.set("min-rate", 1);
        return nbtItem.finish();
    }

    public ItemStack getDust(Group group, @Nullable String type, int percentage, boolean command) {
        FileConfiguration dustConfig = instance.getFileManager().getConfiguration("items/dusts");
        int random = ThreadLocalRandom.current().nextInt(101);
        int counter = 0;

        if (type == null) {
            for (String s : dustConfig.getConfigurationSection("dusts").getKeys(false)) {
                int chance = dustConfig.getConfigurationSection("dusts." + s).getInt("chance");
                if (random < (chance + counter) && random >= counter) {
                    type = s;
                }
                counter += chance;
            }
        }

        type = type == null ? "mystery" : type;

        ConfigurationSection config = dustConfig.getConfigurationSection("dusts." + type);

        if (!command && config.isInt("min-rate") && config.isInt("max-rate")) {
            int minRate = config.getInt("min-rate");
            int maxRate = config.getInt("max-rate");
            percentage = ThreadLocalRandom.current().nextInt(minRate, maxRate + 1);
        } else if (percentage == -1) {
            percentage = ThreadLocalRandom.current().nextInt(0, 10);
        }

        NBTItem nbtItem = new ItemBuilder(config,
                of("group-color", group.getColor()),
                of("group-name", group.getName()),
                of("percentage", percentage)).nbt();

        if (type.equalsIgnoreCase("mystery")) {
            return nbtItem.finish();
        }

        nbtItem.set("dust", true);
        nbtItem.set("percentage", percentage);
        nbtItem.set("group", group.getIdentifier());

        return nbtItem.finish();
    }

    public String getWhiteScrollLore() {
        return color(instance.getFileManager().getConfiguration("items/special-items").getString("white-scroll.format"));
    }
}
