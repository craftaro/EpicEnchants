package com.songoda.epicenchants.objects;

import co.aikar.commands.annotation.Optional;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import de.tr7zw.itemnbtapi.NBTItem;
import lombok.Builder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.concurrent.ThreadLocalRandom.current;

@Builder
public class BookItem {
    private Material material;
    private String displayName;
    private List<String> lore;

    public ItemStack get(Enchant enchant) {
        return get(enchant, current().nextInt(enchant.getMaxLevel()) + 1);
    }

    public ItemStack get(Enchant enchant, int level) {
        return get(enchant, level,
                current().nextInt(enchant.getGroup().getSuccessRateMin(), enchant.getGroup().getSuccessRateMax()),
                current().nextInt(enchant.getGroup().getDestroyRateMin(), enchant.getGroup().getDestroyRateMax()));
    }

    public ItemStack get(Enchant enchant, @Optional Integer level, @Optional Integer successRate, @Optional Integer destroyRate) {
        successRate = successRate == null ? current().nextInt(101) : successRate;
        destroyRate = destroyRate == null ? current().nextInt(101) : destroyRate;
        level = level == null ? current().nextInt(1, enchant.getMaxLevel() + 1) : level;

        int finalSuccessRate = successRate;
        int finalDestroyRate = destroyRate;
        int finalLevel = level;
        ItemBuilder itemBuilder = new ItemBuilder(material)
                .name(displayName.replace("{level}", "" + level))
                .lore(lore.stream()
                        .map(s -> s.replace("{level}", "" + finalLevel)
                                .replace("{success_rate}", "" + finalSuccessRate)
                                .replace("{destroy_rate}", "" + finalDestroyRate))
                        .collect(Collectors.toList()));

        NBTItem nbtItem = itemBuilder.nbt();
        nbtItem.setBoolean("book-item", true);
        nbtItem.setInteger("success-rate", successRate);
        nbtItem.setInteger("destroy-rate", destroyRate);
        nbtItem.setInteger("level", level);
        nbtItem.setString("enchant", enchant.getIdentifier());

        return nbtItem.getItem();
    }
}
