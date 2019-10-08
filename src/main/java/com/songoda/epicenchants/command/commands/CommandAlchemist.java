package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import com.songoda.epicenchants.menus.AlchemistMenu;
import com.songoda.epicenchants.menus.TinkererMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandAlchemist extends AbstractCommand {

    public CommandAlchemist(AbstractCommand parent) {
        super(parent, true, "alchemist");
    }

    @Override
    protected ReturnType runCommand(EpicEnchants instance, CommandSender sender, String... args) {
        Player player = (Player)sender;
        new AlchemistMenu(instance, instance.getFileManager().getConfiguration("menus/alchemist-menu")).open(player);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicEnchants instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.alchemist";
    }

    @Override
    public String getSyntax() {
        return "/ee alchemist";
    }

    @Override
    public String getDescription() {
        return "Opens the Alchemist.";
    }
}
