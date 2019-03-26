package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.managers.HookManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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

    public boolean get(Player user, @Nullable LivingEntity attacker, int level, boolean def) {
        if (string == null || string.isEmpty()) {
            return true;
        }

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        String toValidate = ChatColor.stripColor(HookManager.setPAPIPlaceholders(string, user, attacker, level));

        try {
            return Boolean.parseBoolean(scriptEngine.eval(toValidate).toString());
        } catch (ScriptException ignore) {
            Bukkit.getLogger().warning("[EpicEnchants] One of your condition expressions is not properly formatted.");
            Bukkit.getLogger().warning(toValidate);
            return def;
        }
    }
}
