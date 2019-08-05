package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import com.songoda.epicenchants.utils.Methods;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandEpicEnchants extends AbstractCommand {

    public CommandEpicEnchants() {
        super(null, false, "EpicEnchants");
    }

    @Override
    protected AbstractCommand.ReturnType runCommand(EpicEnchants instance, CommandSender sender, String... args) {
        sender.sendMessage("");
        instance.getLocale().newMessage("&7Version " + instance.getDescription().getVersion()
                + " Created with <3 by &5&l&oSongoda").sendPrefixedMessage(sender);

        for (AbstractCommand command : instance.getCommandManager().getCommands()) {
            if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
                sender.sendMessage(Methods.formatText("&8 - &a" + command.getSyntax() + "&7 - " + command.getDescription()));
            }
        }
        sender.sendMessage("");

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicEnchants instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/EpicEnchants";
    }

    @Override
    public String getDescription() {
        return "Displays this page.";
    }
}
