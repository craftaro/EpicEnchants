package com.songoda.epicenchants.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.Material.*;

public class VersionDependent {
    private static Set<Material> blacklistLegacy;
    private static Set<Material> blacklist;
    private static int version;

    public static void initLegacy(int serverVersion) {
        version = serverVersion;
        blacklistLegacy = new HashSet<Material>() {
            {
                add(valueOf("FURNACE"));
                add(valueOf("CHEST"));
                add(valueOf("TRAPPED_CHEST"));
                add(valueOf("BEACON"));
                add(valueOf("DISPENSER"));
                add(valueOf("DROPPER"));
                add(valueOf("HOPPER"));
                add(valueOf("WORKBENCH"));
                add(valueOf("ENCHANTMENT_TABLE"));
                add(valueOf("ENDER_CHEST"));
                add(valueOf("ANVIL"));
                add(valueOf("BED_BLOCK"));
                add(valueOf("FENCE_GATE"));
                add(valueOf("SPRUCE_FENCE_GATE"));
                add(valueOf("BIRCH_FENCE_GATE"));
                add(valueOf("ACACIA_FENCE_GATE"));
                add(valueOf("JUNGLE_FENCE_GATE"));
                add(valueOf("DARK_OAK_FENCE_GATE"));
                add(valueOf("IRON_DOOR_BLOCK"));
                add(valueOf("WOODEN_DOOR"));
                add(valueOf("SPRUCE_DOOR"));
                add(valueOf("BIRCH_DOOR"));
                add(valueOf("JUNGLE_DOOR"));
                add(valueOf("ACACIA_DOOR"));
                add(valueOf("DARK_OAK_DOOR"));
                add(valueOf("WOOD_BUTTON"));
                add(valueOf("STONE_BUTTON"));
                add(valueOf("TRAP_DOOR"));
                add(valueOf("IRON_TRAPDOOR"));
                add(valueOf("DIODE_BLOCK_OFF"));
                add(valueOf("DIODE_BLOCK_ON"));
                add(valueOf("REDSTONE_COMPARATOR_OFF"));
                add(valueOf("REDSTONE_COMPARATOR_ON"));
                add(valueOf("FENCE"));
                add(valueOf("SPRUCE_FENCE"));
                add(valueOf("BIRCH_FENCE"));
                add(valueOf("JUNGLE_FENCE"));
                add(valueOf("DARK_OAK_FENCE"));
                add(valueOf("ACACIA_FENCE"));
                add(valueOf("NETHER_FENCE"));
                add(valueOf("BREWING_STAND"));
                add(valueOf("CAULDRON"));
                add(valueOf("SIGN_POST"));
                add(valueOf("WALL_SIGN"));
                add(valueOf("SIGN"));
                add(valueOf("LEVER"));
                add(valueOf("DAYLIGHT_DETECTOR_INVERTED"));
                add(valueOf("DAYLIGHT_DETECTOR"));
            }
        };
    }

    public static void initDefault(int serverVersion) {
        version = serverVersion;
        blacklist = new HashSet<Material>() {
            {
                add(valueOf("FURNACE"));
                add(valueOf("CHEST"));
                add(valueOf("TRAPPED_CHEST"));
                add(valueOf("BEACON"));
                add(valueOf("DISPENSER"));
                add(valueOf("DROPPER"));
                add(valueOf("HOPPER"));
                add(valueOf("CRAFTING_TABLE"));
                add(valueOf("ENCHANTING_TABLE"));

                add(valueOf("BLACK_BED"));
                add(valueOf("BLUE_BED"));
                add(valueOf("BROWN_BED"));
                add(valueOf("CYAN_BED"));
                add(valueOf("GRAY_BED"));
                add(valueOf("GREEN_BED"));
                add(valueOf("LIGHT_BLUE_BED"));
                add(valueOf("LIGHT_GRAY_BED"));
                add(valueOf("LIME_BED"));
                add(valueOf("MAGENTA_BED"));
                add(valueOf("ORANGE_BED"));
                add(valueOf("PINK_BED"));
                add(valueOf("PURPLE_BED"));
                add(valueOf("RED_BED"));
                add(valueOf("WHITE_BED"));
                add(valueOf("YELLOW_BED"));

                add(valueOf("ENDER_CHEST"));
                add(valueOf("ANVIL"));

                add(valueOf("ACACIA_FENCE_GATE"));
                add(valueOf("BIRCH_FENCE_GATE"));
                add(valueOf("DARK_OAK_FENCE_GATE"));
                add(valueOf("JUNGLE_FENCE_GATE"));
                add(valueOf("OAK_FENCE_GATE"));
                add(valueOf("SPRUCE_FENCE_GATE"));

                add(valueOf("IRON_DOOR"));

                add(valueOf("ACACIA_DOOR"));
                add(valueOf("BIRCH_DOOR"));
                add(valueOf("DARK_OAK_DOOR"));
                add(valueOf("JUNGLE_DOOR"));
                add(valueOf("OAK_DOOR"));
                add(valueOf("SPRUCE_DOOR"));

                add(valueOf("ACACIA_TRAPDOOR"));
                add(valueOf("BIRCH_TRAPDOOR"));
                add(valueOf("DARK_OAK_TRAPDOOR"));
                add(valueOf("JUNGLE_TRAPDOOR"));
                add(valueOf("OAK_TRAPDOOR"));
                add(valueOf("SPRUCE_TRAPDOOR"));

                add(valueOf("ACACIA_BUTTON"));
                add(valueOf("BIRCH_BUTTON"));
                add(valueOf("DARK_OAK_BUTTON"));
                add(valueOf("JUNGLE_BUTTON"));
                add(valueOf("OAK_BUTTON"));
                add(valueOf("SPRUCE_BUTTON"));

                add(valueOf("ACACIA_FENCE"));
                add(valueOf("BIRCH_FENCE"));
                add(valueOf("DARK_OAK_FENCE"));
                add(valueOf("JUNGLE_FENCE"));
                add(valueOf("OAK_FENCE"));
                add(valueOf("SPRUCE_FENCE"));

                add(valueOf("REPEATER"));
                add(valueOf("COMPARATOR"));

                add(valueOf("BREWING_STAND"));
                add(valueOf("CAULDRON"));
                add(valueOf("WALL_SIGN"));
                add(valueOf("SIGN"));
                add(valueOf("LEVER"));
                add(valueOf("DAYLIGHT_DETECTOR"));

                add(valueOf("SHULKER_BOX"));
                add(valueOf("BLACK_SHULKER_BOX"));
                add(valueOf("BLUE_SHULKER_BOX"));
                add(valueOf("BROWN_SHULKER_BOX"));
                add(valueOf("CYAN_SHULKER_BOX"));
                add(valueOf("GRAY_SHULKER_BOX"));
                add(valueOf("GREEN_SHULKER_BOX"));
                add(valueOf("LIGHT_BLUE_SHULKER_BOX"));
                add(valueOf("LIGHT_GRAY_SHULKER_BOX"));
                add(valueOf("LIME_SHULKER_BOX"));
                add(valueOf("MAGENTA_SHULKER_BOX"));
                add(valueOf("ORANGE_SHULKER_BOX"));
                add(valueOf("PINK_SHULKER_BOX"));
                add(valueOf("PURPLE_SHULKER_BOX"));
                add(valueOf("RED_SHULKER_BOX"));
                add(valueOf("WHITE_SHULKER_BOX"));
                add(valueOf("YELLOW_SHULKER_BOX"));
            }
        };
    }

    public static Set<Material> getBlackList() {
        return !blacklist.isEmpty() ? blacklist : !blacklistLegacy.isEmpty() ? blacklistLegacy : null;
    }

    public static ItemStack getStainedGlassPane(int data) {
        if (version >= 13) {
            switch (data) {
                case 0:
                    return new ItemStack(WHITE_STAINED_GLASS_PANE);
                case 1:
                    return new ItemStack(ORANGE_STAINED_GLASS_PANE);
                case 2:
                    return new ItemStack(MAGENTA_STAINED_GLASS_PANE);
                case 3:
                    return new ItemStack(LIGHT_BLUE_STAINED_GLASS_PANE);
                case 4:
                    return new ItemStack(YELLOW_STAINED_GLASS_PANE);
                case 5:
                    return new ItemStack(LIME_STAINED_GLASS_PANE);
                case 6:
                    return new ItemStack(PINK_STAINED_GLASS);
                case 7:
                    return new ItemStack(GRAY_STAINED_GLASS_PANE);
                case 8:
                    return new ItemStack(LIGHT_GRAY_STAINED_GLASS_PANE);
                case 9:
                    return new ItemStack(CYAN_STAINED_GLASS_PANE);
                case 10:
                    return new ItemStack(PURPLE_STAINED_GLASS_PANE);
                case 11:
                    return new ItemStack(BLUE_STAINED_GLASS_PANE);
                case 12:
                    return new ItemStack(BROWN_STAINED_GLASS_PANE);
                case 13:
                    return new ItemStack(GREEN_STAINED_GLASS_PANE);
                case 14:
                    return new ItemStack(RED_STAINED_GLASS_PANE);
                case 15:
                    return new ItemStack(BLACK_STAINED_GLASS_PANE);
                default:
                    return null;
            }
        }

        return new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) data);
    }
}

