package com.songoda.epicenchants.utils;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicenchants.EpicEnchants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Methods {

    public static ItemStack getGlass() {
        EpicEnchants instance = EpicEnchants.getInstance();
        return Methods.getGlass(instance.getConfig().getBoolean("Interfaces.Replace Glass Type 1 With Rainbow Glass"), instance.getConfig().getInt("Interfaces.Glass Type 1"));
    }

    public static ItemStack getBackgroundGlass(boolean type) {
        EpicEnchants instance = EpicEnchants.getInstance();
        if (type)
            return getGlass(false, instance.getConfig().getInt("Interfaces.Glass Type 2"));
        else
            return getGlass(false, instance.getConfig().getInt("Interfaces.Glass Type 3"));
    }

    private static ItemStack getGlass(Boolean rainbow, int type) {
        int randomNum = 1 + (int) (Math.random() * 6);
        ItemStack glass;
        if (rainbow) {
            glass = new ItemStack(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13) ?
                    Material.LEGACY_STAINED_GLASS_PANE : Material.valueOf("STAINED_GLASS_PANE"), 1, (short) randomNum);
        } else {
            glass = new ItemStack(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13) ?
                    Material.LEGACY_STAINED_GLASS_PANE : Material.valueOf("STAINED_GLASS_PANE"), 1, (short) type);
        }
        ItemMeta glassmeta = glass.getItemMeta();
        glassmeta.setDisplayName("§l");
        glass.setItemMeta(glassmeta);
        return glass;
    }

    public static boolean isInt(String number) {
        if (number == null || number.equals(""))
            return false;
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static String formatText(String text) {
        if (text == null || text.equals(""))
            return "";
        return formatText(text, false);
    }

    public static String formatText(String text, boolean cap) {
        if (text == null || text.equals(""))
            return "";
        if (cap)
            text = text.substring(0, 1).toUpperCase() + text.substring(1);
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
