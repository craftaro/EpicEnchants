package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import com.songoda.epicenchants.menus.EnchanterMenu;
import com.songoda.epicenchants.menus.TinkererMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandTinkerer extends AbstractCommand {

    public CommandTinkerer(AbstractCommand parent) {
        super(parent, true, "tinkerer");
    }

    @Override
    protected ReturnType runCommand(EpicEnchants instance, CommandSender sender, String... args) {
        Player player = (Player)sender;
        new TinkererMenu(instance, instance.getFileManager().getConfiguration("menus/tinkerer-menu")).open(player);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicEnchants instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.tinkerer";
    }

    @Override
    public String getSyntax() {
        return "/ee tinkerer";
    }

    @Override
    public String getDescription() {
        return "Opens the Tinkerer.";
    }
}
