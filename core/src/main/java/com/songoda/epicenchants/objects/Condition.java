package com.songoda.epicenchants.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

    public boolean get(Player player, int level, boolean def) {
        if (string == null || string.isEmpty()) {
            return true;
        }

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");

        try {
            return Boolean.parseBoolean(scriptEngine.eval(string.replace("{level}", "" + level)).toString());
        } catch (ScriptException | NumberFormatException e) {
            Bukkit.getLogger().warning("[EpicEnchants] One of your condition expressions is not properly formatted.");
            return def;
        }
    }
}
