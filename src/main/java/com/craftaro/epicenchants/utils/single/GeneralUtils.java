package com.craftaro.epicenchants.utils.single;

import com.craftaro.core.compatibility.CompatibleHand;
import com.craftaro.core.math.MathUtils;
import com.craftaro.epicenchants.enums.EnchantResult;
import com.craftaro.epicenchants.enums.TriggerType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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

            if (item == null || item.getType() == Material.AIR) {
                return CompatibleHand.OFF_HAND.getItem(entity);
            }
            return item;
        }
        return null;
    }

    public static int getHeldItemSlot(Player player, Event event) {
        if (CompatibleHand.getHand(event) == CompatibleHand.OFF_HAND) {
            return 40;
        }
        return player.getInventory().getHeldItemSlot();
    }

    public static Object parseJS(String toParse, String type, Object def) {
        if (toParse.trim().matches("^\\d+(?>\\.\\d+)?\\s+([<>])\\s*\\d+(?>\\.\\d+)?$")) {   // e.g. "1 < 2"
            toParse = toParse.trim();

            double firstNumber = Double.parseDouble(toParse.substring(0, toParse.indexOf(" ")));
            String symbol = toParse.substring(toParse.indexOf(" ") + 1, toParse.indexOf(" ") + 2);
            double secondNumber = Double.parseDouble(toParse.substring(toParse.indexOf(" ") + 2));

            if (symbol.equals(">")) {
                return firstNumber > secondNumber;
            }

            return firstNumber < secondNumber;
        }

        // FIXME: JavaScript != Math...
        //        Input "false ? (8 * 3) : (4 * 3)" fails for obvious reasons
        return MathUtils.eval(toParse, "[EpicEnchants] One of your " + type + " expressions is not properly formatted.");
    }
}
