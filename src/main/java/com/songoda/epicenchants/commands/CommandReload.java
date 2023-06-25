package com.songoda.epicenchants.commands;

import com.craftaro.core.commands.AbstractCommand;
import com.songoda.epicenchants.EpicEnchants;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandReload extends AbstractCommand {

    private final EpicEnchants plugin;

    public CommandReload(EpicEnchants plugin) {
        super(false, "reload");
        this.plugin = plugin;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        plugin.reloadConfig();
        plugin.getLocale().getMessage("command.reload").sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.reload";
    }

    @Override
    public String getSyntax() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the Configuration files.";
    }
}
