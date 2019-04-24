package com.songoda.epicenchants.utils;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.enums.TriggerType;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import com.songoda.epicenchants.utils.single.GeneralUtils;
import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.enums.EnchantResult.*;
import static com.songoda.epicenchants.enums.TriggerType.HELD_ITEM;

public class EnchantUtils {

    private final EpicEnchants instance;

    public EnchantUtils(EpicEnchants instance) {
        this.instance = instance;
    }

    public Pair<ItemStack, EnchantResult> apply(ItemStack itemStack, Enchant enchant, int level, int successRate, int destroyRate) {
        boolean hasProtection = new NBTItem(itemStack).hasKey("protected");

        Map<Enchant, Integer> currentEnchantMap = getEnchants(itemStack);
        Set<String> currentIds = currentEnchantMap.keySet().stream().map(Enchant::getIdentifier).collect(Collectors.toSet());
        Set<String> currentConflicts = currentEnchantMap.keySet().stream().map(Enchant::getConflict).flatMap(Collection::stream).collect(Collectors.toSet());

        if (enchant.getConflict().stream().anyMatch(currentIds::contains) || currentConflicts.contains(enchant.getIdentifier())) {
            return Pair.of(itemStack, CONFLICT);
        }

        if (currentEnchantMap.entrySet().stream().anyMatch(entry -> entry.getKey().equals(enchant) && entry.getValue() == enchant.getMaxLevel())) {
            return Pair.of(itemStack, MAXED_OUT);
        }

        if (currentEnchantMap.entrySet().stream().anyMatch(entry -> entry.getKey().equals(enchant) && entry.getValue() >= level)) {
            return Pair.of(itemStack, ALREADY_APPLIED);
        }

        if (!GeneralUtils.chance(successRate)) {
            if (GeneralUtils.chance(destroyRate)) {
                if (hasProtection) {
                    NBTItem nbtItem = new ItemBuilder(itemStack).removeLore(instance.getSpecialItems().getWhiteScrollLore()).nbt();
                    nbtItem.removeKey("protected");
                    return Pair.of(nbtItem.getItem(), PROTECTED);
                }
                return Pair.of(new ItemStack(Material.AIR), BROKEN_FAILURE);
            }
            return Pair.of(itemStack, FAILURE);
        }

        ItemBuilder itemBuilder = new ItemBuilder(itemStack);

        if (hasProtection) {
            itemBuilder.removeLore(instance.getSpecialItems().getWhiteScrollLore());
        }

        itemBuilder.removeLore(enchant.getFormat(-1, instance.getFileManager().getConfiguration("config").getBoolean("roman-numbers")).replace("-1", "").trim());
        itemBuilder.addLore(enchant.getFormat(level, instance.getFileManager().getConfiguration("config").getBoolean("roman-numbers")));

        if (hasProtection) {
            itemBuilder.addLore(instance.getSpecialItems().getWhiteScrollLore());
        }

        NBTItem nbtItem = itemBuilder.nbt();

        nbtItem.addCompound("enchants");

        NBTCompound compound = nbtItem.getCompound("enchants");
        compound.setInteger(enchant.getIdentifier(), level);

        return Pair.of(nbtItem.getItem(), SUCCESS);
    }

    public Map<Enchant, Integer> getEnchants(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return Collections.emptyMap();
        }

        NBTItem nbtItem = new NBTItem(itemStack);

        if (!nbtItem.hasNBTData() || !nbtItem.hasKey("enchants")) {
            return Collections.emptyMap();
        }

        NBTCompound compound = nbtItem.getCompound("enchants");

        if (compound == null) {
            return Collections.emptyMap();
        }

        return compound.getKeys().stream().filter(key -> instance.getEnchantManager().getValueUnsafe(key) != null)
                .collect(Collectors.toMap(key -> instance.getEnchantManager().getValueUnsafe(key), compound::getInteger));
    }

    public void handlePlayer(@NotNull Player player, @Nullable LivingEntity opponent, Event event, TriggerType triggerType) {
        List<ItemStack> stacks = new ArrayList<>(Arrays.asList(player.getInventory().getArmorContents()));
        stacks.add(GeneralUtils.getHeldItem(player, event));
        stacks.removeIf(Objects::isNull);

        if (triggerType == HELD_ITEM) {
            stacks = Collections.singletonList(player.getItemInHand());
        }

        stacks.stream().map(this::getEnchants).forEach(list -> list.forEach((enchant, level) -> {
            enchant.onAction(player, opponent, event, level, triggerType, EventType.NONE);
        }));
    }

    public ItemStack removeEnchant(ItemStack itemStack, Enchant enchant) {
        if (itemStack == null) {
            return null;
        }

        NBTItem nbtItem = new NBTItem(itemStack);

        if (nbtItem.getCompound("enchants") == null || nbtItem.getCompound("enchants").getInteger(enchant.getIdentifier()) == null) {
            return itemStack;
        }

        nbtItem.getCompound("enchants").removeKey(enchant.getIdentifier());
        ItemBuilder output = new ItemBuilder(nbtItem.getItem());
        output.removeLore(enchant.getFormat().replace("{level}", "").trim());
        return output.build();
    }
}
