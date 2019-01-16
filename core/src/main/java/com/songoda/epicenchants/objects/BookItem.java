package com.songoda.epicenchants.objects;

import co.aikar.commands.annotation.Optional;
import com.songoda.epicenchants.utils.ItemBuilder;
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

    public ItemStack get(Enchant enchant, int level) {
        return get(enchant, level, current().nextDouble(101), current().nextDouble(101));
    }

    public ItemStack get(Enchant enchant, int level, @Optional Double successRate, @Optional Double destroyRate) {
        successRate = successRate == null ? current().nextInt(101) : successRate;
        destroyRate = destroyRate == null ? current().nextInt(101) : destroyRate;

        double finalSuccessRate = successRate;
        double finalDestroyRate = destroyRate;
        ItemBuilder itemBuilder = new ItemBuilder(material)
                .name(displayName.replace("{level}", "" + level))
                .lore(lore.stream()
                        .map(s -> s.replace("{level}", "" + level)
                                .replace("{success_rate}", "" + finalSuccessRate)
                                .replace("{destroy_rate}", "" + finalDestroyRate))
                        .collect(Collectors.toList()));

        NBTItem nbtItem = itemBuilder.nbt();
        nbtItem.setDouble("success-rate", successRate);
        nbtItem.setDouble("destroy-rate", destroyRate);
        nbtItem.setString("enchant", enchant.getIdentifier());

        return nbtItem.getItem();
    }
}
