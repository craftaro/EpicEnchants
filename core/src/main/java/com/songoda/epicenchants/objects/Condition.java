package com.songoda.epicenchants.objects;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;

public class Condition {
    private final String string;

    private Condition(String string) {
        this.string = string;
    }

    public static Condition of(String string) {
        return new Condition(string);
    }

    public boolean get(Player user, @Nullable LivingEntity attacker, int level, boolean def) {
        if (string == null || string.isEmpty()) {
            return true;
        }

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        String toValidate = string;

        for (Placeholder pair : Arrays.asList(
                Placeholder.of("level", level),
                Placeholder.of("user_health", user.getHealth()),
                Placeholder.of("attacker_health", attacker == null ? -1 : attacker.getHealth()),
                Placeholder.of("user_food", user.getFoodLevel()),
                Placeholder.of("attacker_food", attacker instanceof Player ? ((Player) attacker).getFoodLevel() : 0),
                Placeholder.of("user_is_sneaking", user.isSneaking()),
                Placeholder.of("attacker_is_sneaking", attacker instanceof Player && ((Player) attacker).isSneaking()),
                Placeholder.of("world", user.getWorld().getName()),
                Placeholder.of("players_near", user.getNearbyEntities(4, 4, 4).size()),
                Placeholder.of("user_on_fire", user.getFireTicks() != 0),
                Placeholder.of("attacker_on_fire", attacker != null && attacker.getFireTicks() != 0)
        )) {
            toValidate = toValidate.replace(pair.getPlaceholder(), pair.getToReplace().toString());
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            toValidate = PlaceholderAPI.setPlaceholders(user, toValidate);

            if (attacker instanceof Player) {
                toValidate = PlaceholderAPI.setRelationalPlaceholders(user, (Player) attacker, toValidate);
            }
        }

        toValidate = ChatColor.stripColor(toValidate);

        try {
            return Boolean.parseBoolean(scriptEngine.eval(toValidate).toString());
        } catch (ScriptException ignore) {
            Bukkit.getLogger().warning("[EpicEnchants] One of your condition expressions is not properly formatted.");
            Bukkit.getLogger().warning(toValidate);
            return def;
        }
    }
}
