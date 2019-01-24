package com.songoda.epicenchants.utils;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.enums.EffectType;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.objects.Enchant;
import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.enums.EffectType.HELD_ITEM;
import static com.songoda.epicenchants.enums.EnchantResult.*;

public class EnchantUtils {

    private final EpicEnchants instance;

    public EnchantUtils(EpicEnchants instance) {
        this.instance = instance;
    }

    public Pair<ItemStack, EnchantResult> apply(ItemStack itemStack, Enchant enchant, int level, int successRate, int destroyRate) {
        if (!GeneralUtils.chance(successRate)) {
            return GeneralUtils.chance(destroyRate) ? Pair.of(new ItemStack(Material.AIR), BROKEN_FAILURE) : Pair.of(itemStack, FAILURE);
        }

        if (getEnchants(itemStack).keySet().stream().anyMatch(s -> enchant.getConflict().contains(s.getIdentifier()))) {
            return Pair.of(itemStack, CONFLICT);
        }

        ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        itemBuilder.removeLore(enchant.getFormat().replace("{level}", "").trim());
        itemBuilder.addLore(enchant.getFormat().replace("{level}", "" + level));

        NBTItem nbtItem = itemBuilder.nbt();

        nbtItem.addCompound("enchants");

        NBTCompound compound = nbtItem.getCompound("enchants");
        compound.setInteger(enchant.getIdentifier(), level);

        return Pair.of(nbtItem.getItem(), SUCCESS);
    }

    public Map<Enchant, Integer> getEnchants(ItemStack itemStack) {
        if (itemStack == null) {
            return Collections.emptyMap();
        }

        NBTCompound compound = new NBTItem(itemStack).getCompound("enchants");

        if (compound == null) {
            return Collections.emptyMap();
        }

        return compound.getKeys().stream().filter(key -> instance.getEnchantManager().getEnchantUnsafe(key) != null)
                .collect(Collectors.toMap(key -> instance.getEnchantManager().getEnchantUnsafe(key), compound::getInteger));
    }

    public void handlePlayer(Player player, Event event, EffectType effectType) {
        if (player == null) {
            return;
        }

        List<ItemStack> stacks = new ArrayList<>(Arrays.asList(player.getInventory().getArmorContents()));
        stacks.add(player.getItemInHand());
        stacks.removeIf(Objects::isNull);

        if (effectType == HELD_ITEM) {
            stacks = Collections.singletonList(player.getItemInHand());
        }

        stacks.stream().map(this::getEnchants).forEach(list -> list.forEach((enchant, level) -> {
            Player opponent = event instanceof EntityDamageByEntityEvent ?
                    ((EntityDamageByEntityEvent) event).getDamager() instanceof Player ?
                            ((Player) ((EntityDamageByEntityEvent) event).getDamager()) : null : null;

            enchant.onAction(player, opponent, event, level, effectType, EventType.NONE);
        }));
    }
}
