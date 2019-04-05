package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import com.songoda.epicenchants.utils.single.GeneralUtils;
import com.songoda.epicenchants.utils.single.ItemGroup;
import com.songoda.epicenchants.utils.single.RomanNumber;
import de.tr7zw.itemnbtapi.NBTItem;
import lombok.Builder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.single.GeneralUtils.color;
import static java.util.concurrent.ThreadLocalRandom.current;

@Builder
public class BookItem {
    private EpicEnchants instance;
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

    public ItemStack get(Enchant enchant, @Nullable Integer level, @Nullable Integer successRate, @Nullable Integer destroyRate) {
        successRate = successRate == null ? current().nextInt(101) : successRate;
        destroyRate = destroyRate == null ? current().nextInt(101) : destroyRate;
        level = level == null ? current().nextInt(1, enchant.getMaxLevel() + 1) : level;

        int finalSuccessRate = successRate;
        int finalDestroyRate = destroyRate;

        List<String> toSet = new ArrayList<>(lore);

        for (int i = toSet.size() - 1; i >= 0; i--) {
            String string = toSet.get(i);

            if (string.contains("{description}")) {
                toSet.remove(i);
                toSet.addAll(i, enchant.getDescription().stream().map(s -> enchant.getGroup().getDescriptionColor() + s).map(GeneralUtils::color).collect(Collectors.toList()));
                continue;
            }

            string = string
                    .replace("{item_group}", "" + instance.getItemGroup().getGroup(enchant.getItemWhitelist()).map(ItemGroup.Group::getName).orElse("N/A"))
                    .replace("{success_rate}", "" + finalSuccessRate)
                    .replace("{destroy_rate}", "" + finalDestroyRate);

            toSet.set(i, string);
        }

        ItemBuilder itemBuilder = new ItemBuilder(material)
                .name(color(displayName
                        .replace("{level}", "" + (instance.getFileManager().getConfiguration("config").getBoolean("roman-numbers") ? RomanNumber.toRoman(level) : level))
                        .replace("{enchant}", "" + enchant.getIdentifier())
                        .replace("{group_color}", enchant.getGroup().getColor())
                        .replace("{group_name}", enchant.getGroup().getName())
                ))
                .lore(toSet);

        NBTItem nbtItem = itemBuilder.nbt();
        nbtItem.setBoolean("book-item", true);
        nbtItem.setInteger("success-rate", successRate);
        nbtItem.setInteger("destroy-rate", destroyRate);
        nbtItem.setInteger("level", level);
        nbtItem.setString("enchant", enchant.getIdentifier());

        return nbtItem.getItem();
    }
}
