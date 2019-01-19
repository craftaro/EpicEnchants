package com.songoda.epicenchants.enums;

import org.bukkit.Material;

import java.util.Arrays;

public enum MaterialType {
    TOOL("PICKAXE", "SHOVEL", "SPADE", "AXE", "HOE"),
    WEAPON("SWORD", "BOW", "AXE"),
    ARMOR("HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS");

    private String[] types;

    MaterialType(String... types) {
        this.types = types;
    }

    public boolean is(Material material) {
        return Arrays.stream(types).anyMatch(s -> material.toString().contains(s));
    }

    public static MaterialType of(String string) {
        return Arrays.stream(values()).filter(s -> s.toString().equals(string.toUpperCase())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown MaterialType: " + string));
    }
}
