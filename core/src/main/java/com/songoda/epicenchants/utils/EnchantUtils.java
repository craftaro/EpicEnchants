package com.songoda.epicenchants.utils;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.enums.EnchantProcType;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.objects.Enchant;
import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

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

        return compound.getKeys().stream().collect(Collectors.toMap(key -> instance.getEnchantManager().getEnchantUnsafe(key), compound::getInteger));
    }

    public void handlePlayer(Player player, Event event, EnchantProcType damageType) {
        List<ItemStack> stacks = Arrays.asList(player.getInventory().getArmorContents());
        stacks.add(player.getItemInHand());
        stacks.removeIf(Objects::isNull);

        stacks.stream().map(this::getEnchants).forEach(list -> list.forEach((enchant, level) -> {
            switch (damageType) {
                case DAMAGED:
                    enchant.onReceiveDamage((EntityDamageByEntityEvent) event, level);
                case DEALT_DAMAGE:
                    enchant.onDealDamage((EntityDamageByEntityEvent) event, level);
                case MINED:
                    enchant.onMine((BlockBreakEvent) event, level);
            }
        }));
    }
}
