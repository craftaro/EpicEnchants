package com.songoda.epicenchants.utils;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.enums.TriggerType;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.itemnbtapi.NBTCompound;
import com.songoda.epicenchants.utils.itemnbtapi.NBTItem;
import com.songoda.epicenchants.utils.objects.ItemBuilder;
import com.songoda.epicenchants.utils.settings.Setting;
import com.songoda.epicenchants.utils.single.GeneralUtils;
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

    public Tuple<ItemStack, EnchantResult> apply(ItemStack itemStack, Enchant enchant, int level, int successRate, int destroyRate) {
        boolean hasProtection = new NBTItem(itemStack).hasKey("protected");

        Map<Enchant, Integer> currentEnchantMap = getEnchants(itemStack);
        Set<String> currentIds = currentEnchantMap.keySet().stream().map(Enchant::getIdentifier).collect(Collectors.toSet());
        Set<String> currentConflicts = currentEnchantMap.keySet().stream().map(Enchant::getConflict).flatMap(Collection::stream).collect(Collectors.toSet());

        if (enchant.getConflict().stream().anyMatch(currentIds::contains) || currentConflicts.contains(enchant.getIdentifier())) {
            return Tuple.of(itemStack, CONFLICT);
        }

        if (currentEnchantMap.entrySet().stream().anyMatch(entry -> entry.getKey().equals(enchant) && entry.getValue() == enchant.getMaxLevel())) {
            return Tuple.of(itemStack, MAXED_OUT);
        }

        if (currentEnchantMap.entrySet().stream().anyMatch(entry -> entry.getKey().equals(enchant) && entry.getValue() >= level)) {
            return Tuple.of(itemStack, ALREADY_APPLIED);
        }

        if (!GeneralUtils.chance(successRate)) {
            if (GeneralUtils.chance(destroyRate)) {
                if (hasProtection) {
                    NBTItem nbtItem = new ItemBuilder(itemStack).removeLore(instance.getSpecialItems().getWhiteScrollLore()).nbt();
                    nbtItem.removeKey("protected");
                    return Tuple.of(nbtItem.getItem(), PROTECTED);
                }
                return Tuple.of(new ItemStack(Material.AIR), BROKEN_FAILURE);
            }
            return Tuple.of(itemStack, FAILURE);
        }

        ItemBuilder itemBuilder = new ItemBuilder(itemStack);

        if (hasProtection) {
            itemBuilder.removeLore(instance.getSpecialItems().getWhiteScrollLore());
        }

        itemBuilder.removeLore(enchant.getFormat(-1, Setting.ROMAN.getBoolean()).replace("-1", "").trim());
        itemBuilder.addLore(enchant.getFormat(level, Setting.ROMAN.getBoolean()));

        if (hasProtection) {
            itemBuilder.addLore(instance.getSpecialItems().getWhiteScrollLore());
        }

        NBTItem nbtItem = itemBuilder.nbt();

        nbtItem.addCompound("src/main/resources/enchants");

        NBTCompound compound = nbtItem.getCompound("src/main/resources/enchants");
        compound.setInteger(enchant.getIdentifier(), level);

        return Tuple.of(nbtItem.getItem(), SUCCESS);
    }

    public Map<Enchant, Integer> getEnchants(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return Collections.emptyMap();
        }

        NBTItem nbtItem = new NBTItem(itemStack);

        if (!nbtItem.hasNBTData() || !nbtItem.hasKey("src/main/resources/enchants")) {
            return Collections.emptyMap();
        }

        NBTCompound compound = nbtItem.getCompound("src/main/resources/enchants");

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

        if (nbtItem.getCompound("src/main/resources/enchants") == null || nbtItem.getCompound("src/main/resources/enchants").getInteger(enchant.getIdentifier()) == null) {
            return itemStack;
        }

        nbtItem.getCompound("src/main/resources/enchants").removeKey(enchant.getIdentifier());
        ItemBuilder output = new ItemBuilder(nbtItem.getItem());
        output.removeLore(enchant.getFormat().replace("{level}", "").trim());
        return output.build();
    }
}
