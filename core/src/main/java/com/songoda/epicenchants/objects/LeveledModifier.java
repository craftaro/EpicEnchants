package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.utils.single.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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

    public double get(int level, double def, Player user, LivingEntity opponent) {
        if (string == null || string.isEmpty()) {
            return def;
        }

        if (string.equalsIgnoreCase("MAX")) {
            return Integer.MAX_VALUE;
        }

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
        String toTest = Placeholders.setPlaceholders(string, user, opponent, level);

        try {
            return Double.parseDouble(scriptEngine.eval(toTest).toString());
        } catch (ScriptException | NumberFormatException e) {
            Bukkit.getLogger().warning("[EpicEnchants] One of your math expressions is not properly formatted.");
            Bukkit.getLogger().warning(toTest);
            return def;
        }
    }
}
