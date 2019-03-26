package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.utils.single.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Condition {
    private final String string;

    private Condition(String string) {
        this.string = string;
    }

    public static Condition of(String string) {
        return new Condition(string);
    }

    public boolean get(Player user, @Nullable LivingEntity attacker, int level, @Nullable Event event, boolean def) {
        if (string == null || string.isEmpty()) {
            return true;
        }

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        String toValidate = ChatColor.stripColor(Placeholders.setPlaceholders(string, user, attacker, level, event));

        System.out.println("Verifying: " + toValidate);

        try {
            return Boolean.parseBoolean(scriptEngine.eval(toValidate).toString());
        } catch (ScriptException ignore) {
            Bukkit.getLogger().warning("[EpicEnchants] One of your condition expressions is not properly formatted.");
            Bukkit.getLogger().warning(toValidate);
            return def;
        }
    }
}
