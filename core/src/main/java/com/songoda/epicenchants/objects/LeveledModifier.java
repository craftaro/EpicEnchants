package com.songoda.epicenchants.objects;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class LeveledModifier {
    private String string;
    private static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");

    private LeveledModifier(String string) {
        this.string = string;
    }

    public static LeveledModifier of(String string) {
        return new LeveledModifier(string);
    }

    public double get(int level) {
        try {
            return Double.parseDouble(scriptEngine.eval(string.replaceAll("\\{level}", "" + level)).toString());
        } catch (ScriptException | NumberFormatException e) {
            return 0;
        }
    }
}
