package com.songoda.epicenchants.utils.settings;


import com.songoda.epicenchants.EpicEnchants;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Setting {

    ROMAN("Main.Roman Numerals", true),

    BLACK_MIN("Main.Black Scroll Min", 20),
    BLACK_MAX("Main.Black Scroll Max", 100),

    VAULT_ECONOMY("Economy.Use Vault Economy", true,
            "Should Vault be used?"),

    RESERVE_ECONOMY("Economy.Use Reserve Economy", true,
            "Should Reserve be used?"),

    PLAYER_POINTS_ECONOMY("Economy.Use Player Points Economy", false,
            "Should PlayerPoints be used?"),

    GLASS_TYPE_1("Interfaces.Glass Type 1", 7),
    GLASS_TYPE_2("Interfaces.Glass Type 2", 11),
    GLASS_TYPE_3("Interfaces.Glass Type 3", 3),

    FIRST_LOAD("System.First Load", true),

    LANGUGE_MODE("System.Language Mode", "en_US",
            "The enabled language file.",
            "More language files (if available) can be found in the plugins data folder.");

    private String setting;
    private Object option;
    private String[] comments;

    Setting(String setting, Object option, String... comments) {
        this.setting = setting;
        this.option = option;
        this.comments = comments;
    }

    Setting(String setting, Object option) {
        this.setting = setting;
        this.option = option;
        this.comments = null;
    }

    public static Setting getSetting(String setting) {
        List<Setting> settings = Arrays.stream(values()).filter(setting1 -> setting1.setting.equals(setting)).collect(Collectors.toList());
        if (settings.isEmpty()) return null;
        return settings.get(0);
    }

    public String getSetting() {
        return setting;
    }

    public Object getOption() {
        return option;
    }

    public String[] getComments() {
        return comments;
    }

    public List<Integer> getIntegerList() {
        return EpicEnchants.getInstance().getConfig().getIntegerList(setting);
    }

    public List<String> getStringList() {
        return EpicEnchants.getInstance().getConfig().getStringList(setting);
    }

    public boolean getBoolean() {
        return EpicEnchants.getInstance().getConfig().getBoolean(setting);
    }

    public int getInt() {
        return EpicEnchants.getInstance().getConfig().getInt(setting);
    }

    public long getLong() {
        return EpicEnchants.getInstance().getConfig().getLong(setting);
    }

    public String getString() {
        return EpicEnchants.getInstance().getConfig().getString(setting);
    }

    public char getChar() {
        return EpicEnchants.getInstance().getConfig().getString(setting).charAt(0);
    }

    public double getDouble() {
        return EpicEnchants.getInstance().getConfig().getDouble(setting);
    }

    public Material getMaterial() {
        String materialStr = EpicEnchants.getInstance().getConfig().getString(setting);
        Material material = Material.getMaterial(materialStr);

        if (material == null) {
            System.out.println(String.format("Config value \"%s\" has an invalid material name: \"%s\"", setting, materialStr));
        }

        return material;
    }
}