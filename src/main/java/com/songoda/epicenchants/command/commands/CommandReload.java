package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandReload extends AbstractCommand {

    public CommandReload(AbstractCommand parent) {
        super(parent, false, "reload");
    }

    @Override
    protected AbstractCommand.ReturnType runCommand(EpicEnchants instance, CommandSender sender, String... args) {
        instance.reloadConfig();
        instance.getLocale().getMessage("command.reload").sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicEnchants instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.reload";
    }

    @Override
    public String getSyntax() {
        return "/ee reload";
    }

    @Override
    public String getDescription() {
        return "Reload the Configuration files.";
    }
}
