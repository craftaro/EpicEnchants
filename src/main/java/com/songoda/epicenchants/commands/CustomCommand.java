package com.songoda.epicenchants.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.menus.AlchemistMenu;
import com.songoda.epicenchants.menus.EnchanterMenu;
import com.songoda.epicenchants.menus.TinkererMenu;
import org.bukkit.entity.Player;

public class CustomCommand extends BaseCommand {

    @Dependency("instance")
    private EpicEnchants instance;

    @CommandAlias("%enchanter")
    @Description("Opens the Enchanter")
    @CommandPermission("epicenchants.enchanter")
    public void onEnchanter(Player player) {
        new EnchanterMenu(instance, instance.getFileManager().getConfiguration("menus/enchanter-menu"), player).open(player);
    }

    @CommandAlias("%tinkerer")
    @Description("Opens the Tinkerer")
    @CommandPermission("epicenchants.tinkerer")
    public void onTinkerer(Player player) {
        new TinkererMenu(instance, instance.getFileManager().getConfiguration("menus/tinkerer-menu")).open(player);
    }

    @CommandAlias("%alchemist")
    @Description("Opens the Alchemist")
    @CommandPermission("epicenchants.alchemist")
    public void onAlchemist(Player player) {
        new AlchemistMenu(instance, instance.getFileManager().getConfiguration("menus/alchemist-menu")).open(player);
    }

}
