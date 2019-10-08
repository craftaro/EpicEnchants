package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import com.songoda.epicenchants.menus.EnchanterMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandEnchanter extends AbstractCommand {

    public CommandEnchanter(AbstractCommand parent) {
        super(parent, true, "enchanter");
    }

    @Override
    protected ReturnType runCommand(EpicEnchants instance, CommandSender sender, String... args) {
        Player player = (Player)sender;
        new EnchanterMenu(instance, instance.getFileManager().getConfiguration("menus/enchanter-menu"), player).open(player);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicEnchants instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.enchanter";
    }

    @Override
    public String getSyntax() {
        return "/ee enchanter";
    }

    @Override
    public String getDescription() {
        return "Opens the Enchanter.";
    }
}
