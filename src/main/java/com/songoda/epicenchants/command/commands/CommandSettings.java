package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandSettings extends AbstractCommand {

    public CommandSettings(AbstractCommand parent) {
        super(parent, true, "Settings");
    }

    @Override
    protected ReturnType runCommand(EpicEnchants instance, CommandSender sender, String... args) {
        instance.getSettingsManager().openSettingsManager((Player) sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicEnchants instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.admin";
    }

    @Override
    public String getSyntax() {
        return "/ee settings";
    }

    @Override
    public String getDescription() {
        return "Edit the EpicEnchants Settings.";
    }
}
