package com.songoda.epicenchants.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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

    public boolean get(Player wearer, LivingEntity attacker, int level, boolean def) {
        if (string == null || string.isEmpty()) {
            return true;
        }

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        String toValidate = string;

        for (Placeholder pair : Arrays.asList(
                Placeholder.of("level", level),
                Placeholder.of("wearer_health", wearer.getHealth()),
                Placeholder.of("attacker_health", attacker.getHealth()),
                Placeholder.of("wearer_food", wearer.getFoodLevel()),
                Placeholder.of("attacker_food", attacker instanceof Player ? ((Player) attacker).getFoodLevel() : 0),
                Placeholder.of("wearer_is_sneaking", wearer.isSneaking()),
                Placeholder.of("attacker_is_sneaking", attacker instanceof Player && ((Player) attacker).isSneaking()),
                Placeholder.of("world", wearer.getWorld().getName()),
                Placeholder.of("players_near", wearer.getNearbyEntities(4, 4, 4).size()),
                Placeholder.of("wearer_on_fire", wearer.getFireTicks() != 0),
                Placeholder.of("attacker_on_fire", attacker.getFireTicks() != 0)
        )) {
            toValidate = toValidate.replace(pair.getPlaceholder(), pair.getToReplace().toString());
        }

        try {
            return Boolean.parseBoolean(scriptEngine.eval(toValidate).toString());
        } catch (ScriptException | NumberFormatException e) {
            Bukkit.getLogger().warning("[EpicEnchants] One of your condition expressions is not properly formatted.");
            return def;
        }
    }
}
