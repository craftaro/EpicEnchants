package com.songoda.epicenchants.utils;

import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class VersionDependentList {
    private static Set<Material> blacklistLegacy;
    private static Set<Material> blacklist;

    public static void initLegacy() {
        blacklistLegacy = new HashSet<Material>() {
            {
                add(Material.valueOf("FURNACE"));
                add(Material.valueOf("CHEST"));
                add(Material.valueOf("TRAPPED_CHEST"));
                add(Material.valueOf("BEACON"));
                add(Material.valueOf("DISPENSER"));
                add(Material.valueOf("DROPPER"));
                add(Material.valueOf("HOPPER"));
                add(Material.valueOf("WORKBENCH"));
                add(Material.valueOf("ENCHANTMENT_TABLE"));
                add(Material.valueOf("ENDER_CHEST"));
                add(Material.valueOf("ANVIL"));
                add(Material.valueOf("BED_BLOCK"));
                add(Material.valueOf("FENCE_GATE"));
                add(Material.valueOf("SPRUCE_FENCE_GATE"));
                add(Material.valueOf("BIRCH_FENCE_GATE"));
                add(Material.valueOf("ACACIA_FENCE_GATE"));
                add(Material.valueOf("JUNGLE_FENCE_GATE"));
                add(Material.valueOf("DARK_OAK_FENCE_GATE"));
                add(Material.valueOf("IRON_DOOR_BLOCK"));
                add(Material.valueOf("WOODEN_DOOR"));
                add(Material.valueOf("SPRUCE_DOOR"));
                add(Material.valueOf("BIRCH_DOOR"));
                add(Material.valueOf("JUNGLE_DOOR"));
                add(Material.valueOf("ACACIA_DOOR"));
                add(Material.valueOf("DARK_OAK_DOOR"));
                add(Material.valueOf("WOOD_BUTTON"));
                add(Material.valueOf("STONE_BUTTON"));
                add(Material.valueOf("TRAP_DOOR"));
                add(Material.valueOf("IRON_TRAPDOOR"));
                add(Material.valueOf("DIODE_BLOCK_OFF"));
                add(Material.valueOf("DIODE_BLOCK_ON"));
                add(Material.valueOf("REDSTONE_COMPARATOR_OFF"));
                add(Material.valueOf("REDSTONE_COMPARATOR_ON"));
                add(Material.valueOf("FENCE"));
                add(Material.valueOf("SPRUCE_FENCE"));
                add(Material.valueOf("BIRCH_FENCE"));
                add(Material.valueOf("JUNGLE_FENCE"));
                add(Material.valueOf("DARK_OAK_FENCE"));
                add(Material.valueOf("ACACIA_FENCE"));
                add(Material.valueOf("NETHER_FENCE"));
                add(Material.valueOf("BREWING_STAND"));
                add(Material.valueOf("CAULDRON"));
                add(Material.valueOf("SIGN_POST"));
                add(Material.valueOf("WALL_SIGN"));
                add(Material.valueOf("SIGN"));
                add(Material.valueOf("LEVER"));
                add(Material.valueOf("DAYLIGHT_DETECTOR_INVERTED"));
                add(Material.valueOf("DAYLIGHT_DETECTOR"));
            }
        };
    }

    public static void initDefault() {
        blacklist = new HashSet<Material>() {
            {
                add(Material.valueOf("FURNACE"));
                add(Material.valueOf("CHEST"));
                add(Material.valueOf("TRAPPED_CHEST"));
                add(Material.valueOf("BEACON"));
                add(Material.valueOf("DISPENSER"));
                add(Material.valueOf("DROPPER"));
                add(Material.valueOf("HOPPER"));
                add(Material.valueOf("CRAFTING_TABLE"));
                add(Material.valueOf("ENCHANTING_TABLE"));

                add(Material.valueOf("BLACK_BED"));
                add(Material.valueOf("BLUE_BED"));
                add(Material.valueOf("BROWN_BED"));
                add(Material.valueOf("CYAN_BED"));
                add(Material.valueOf("GRAY_BED"));
                add(Material.valueOf("GREEN_BED"));
                add(Material.valueOf("LIGHT_BLUE_BED"));
                add(Material.valueOf("LIGHT_GRAY_BED"));
                add(Material.valueOf("LIME_BED"));
                add(Material.valueOf("MAGENTA_BED"));
                add(Material.valueOf("ORANGE_BED"));
                add(Material.valueOf("PINK_BED"));
                add(Material.valueOf("PURPLE_BED"));
                add(Material.valueOf("RED_BED"));
                add(Material.valueOf("WHITE_BED"));
                add(Material.valueOf("YELLOW_BED"));

                add(Material.valueOf("ENDER_CHEST"));
                add(Material.valueOf("ANVIL"));

                add(Material.valueOf("ACACIA_FENCE_GATE"));
                add(Material.valueOf("BIRCH_FENCE_GATE"));
                add(Material.valueOf("DARK_OAK_FENCE_GATE"));
                add(Material.valueOf("JUNGLE_FENCE_GATE"));
                add(Material.valueOf("OAK_FENCE_GATE"));
                add(Material.valueOf("SPRUCE_FENCE_GATE"));

                add(Material.valueOf("IRON_DOOR"));

                add(Material.valueOf("ACACIA_DOOR"));
                add(Material.valueOf("BIRCH_DOOR"));
                add(Material.valueOf("DARK_OAK_DOOR"));
                add(Material.valueOf("JUNGLE_DOOR"));
                add(Material.valueOf("OAK_DOOR"));
                add(Material.valueOf("SPRUCE_DOOR"));

                add(Material.valueOf("ACACIA_TRAPDOOR"));
                add(Material.valueOf("BIRCH_TRAPDOOR"));
                add(Material.valueOf("DARK_OAK_TRAPDOOR"));
                add(Material.valueOf("JUNGLE_TRAPDOOR"));
                add(Material.valueOf("OAK_TRAPDOOR"));
                add(Material.valueOf("SPRUCE_TRAPDOOR"));

                add(Material.valueOf("ACACIA_BUTTON"));
                add(Material.valueOf("BIRCH_BUTTON"));
                add(Material.valueOf("DARK_OAK_BUTTON"));
                add(Material.valueOf("JUNGLE_BUTTON"));
                add(Material.valueOf("OAK_BUTTON"));
                add(Material.valueOf("SPRUCE_BUTTON"));

                add(Material.valueOf("ACACIA_FENCE"));
                add(Material.valueOf("BIRCH_FENCE"));
                add(Material.valueOf("DARK_OAK_FENCE"));
                add(Material.valueOf("JUNGLE_FENCE"));
                add(Material.valueOf("OAK_FENCE"));
                add(Material.valueOf("SPRUCE_FENCE"));

                add(Material.valueOf("REPEATER"));
                add(Material.valueOf("COMPARATOR"));

                add(Material.valueOf("BREWING_STAND"));
                add(Material.valueOf("CAULDRON"));
                add(Material.valueOf("WALL_SIGN"));
                add(Material.valueOf("SIGN"));
                add(Material.valueOf("LEVER"));
                add(Material.valueOf("DAYLIGHT_DETECTOR"));

                add(Material.valueOf("SHULKER_BOX"));
                add(Material.valueOf("BLACK_SHULKER_BOX"));
                add(Material.valueOf("BLUE_SHULKER_BOX"));
                add(Material.valueOf("BROWN_SHULKER_BOX"));
                add(Material.valueOf("CYAN_SHULKER_BOX"));
                add(Material.valueOf("GRAY_SHULKER_BOX"));
                add(Material.valueOf("GREEN_SHULKER_BOX"));
                add(Material.valueOf("LIGHT_BLUE_SHULKER_BOX"));
                add(Material.valueOf("LIGHT_GRAY_SHULKER_BOX"));
                add(Material.valueOf("LIME_SHULKER_BOX"));
                add(Material.valueOf("MAGENTA_SHULKER_BOX"));
                add(Material.valueOf("ORANGE_SHULKER_BOX"));
                add(Material.valueOf("PINK_SHULKER_BOX"));
                add(Material.valueOf("PURPLE_SHULKER_BOX"));
                add(Material.valueOf("RED_SHULKER_BOX"));
                add(Material.valueOf("WHITE_SHULKER_BOX"));
                add(Material.valueOf("YELLOW_SHULKER_BOX"));
            }
        };
    }

    public static Set<Material> getBlackList() {
        return !blacklist.isEmpty() ? blacklist : !blacklistLegacy.isEmpty() ? blacklistLegacy : null;
    }
}

