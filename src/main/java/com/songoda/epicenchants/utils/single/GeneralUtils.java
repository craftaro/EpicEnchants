package com.songoda.epicenchants.utils.single;

import com.songoda.core.math.MathUtils;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.enums.TriggerType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GeneralUtils {

    public static boolean chance(int chance) {
        return chance((double) chance);
    }

    public static boolean chance(double chance) {
        return ThreadLocalRandom.current().nextDouble(101) < chance;
    }

    public static String color(String input) {
        return format(input, "", null);
    }

    public static List<String> getString(ConfigurationSection section, String path) {
        return section.isList(path) ? section.getStringList(path) : Collections.singletonList(section.getString(path));
    }

    public static String format(String input, String placeholder, Object toReplace) {
        return ChatColor.translateAlternateColorCodes('&', input).replaceAll(placeholder, toReplace == null ? "" : toReplace.toString());
    }

    public static String getMessageFromResult(EnchantResult result) {
        return "enchants." + result.toString().toLowerCase().replace("_", "");
    }

    public static <X> X getRandomElement(Set<X> set) {
        int item = ThreadLocalRandom.current().nextInt(set.size());
        int i = 0;
        for (X obj : set) {
            if (i == item)
                return obj;
            i++;
        }
        return null;
    }

    public static int[] getSlots(String string) {
        return Arrays.stream(string.split(",")).filter(StringUtils::isNumeric).mapToInt(Integer::parseInt).toArray();
    }

    public static Set<TriggerType> parseTrigger(String triggers) {
        return triggers == null ? Collections.emptySet() : Arrays.stream(triggers.replaceAll("\\s+", "").split(",")).map(TriggerType::valueOf).collect(Collectors.toSet());
    }

    public static ItemStack getHeldItem(LivingEntity entity, Event event) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            int slot = getHeldItemSlot(player, event);
            return player.getInventory().getItem(slot);
        } else if (entity.getEquipment() != null) {
            ItemStack item = entity.getEquipment().getItemInHand();

            try {
                if (item.getType() == Material.AIR) {
                    return entity.getEquipment().getItemInOffHand();
                }
            } catch (NoSuchMethodError ignore) {
            }
            return item;
        }
        return null;
    }

    public static int getHeldItemSlot(Player entity, Event event) {
        Player player = (Player) entity;
        int slot = player.getInventory().getHeldItemSlot();

        try {
            if (event instanceof PlayerInteractEvent && ((PlayerInteractEvent) event).getHand() == EquipmentSlot.OFF_HAND) {
                slot = 40;
            }
        } catch (NoSuchMethodError ignore) {
        }

        return slot;
    }

    public static Object parseJS(String toParse, String type, Object def) {
        return MathUtils.eval("[EpicEnchants] One of your " + type + " expressions is not properly formatted.", toParse);

    }
}
