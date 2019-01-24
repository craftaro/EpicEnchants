package com.songoda.epicenchants.objects;

import org.bukkit.Bukkit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class LeveledModifier {
    private final String string;

    private LeveledModifier(String string) {
        this.string = string;
    }

    public static LeveledModifier of(String string) {
        return new LeveledModifier(string);
    }

    public double get(int level, double def) {
        if (string == null || string.isEmpty()) {
            return def;
        }

        if (string.equalsIgnoreCase("MAX")) {
            return Integer.MAX_VALUE;
        }

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");

        try {
            return Double.parseDouble(scriptEngine.eval(string.replace("{level}", "" + level)).toString());
        } catch (ScriptException | NumberFormatException e) {
            Bukkit.getLogger().warning("[EpicEnchants] One of your math expressions is not properly formatted.");
            return def;
        }
    }
}
