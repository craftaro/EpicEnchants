package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import com.songoda.epicenchants.objects.Group;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CommandGiveRandomBook extends AbstractCommand {

    public CommandGiveRandomBook(AbstractCommand parent) {
        super(parent, false, "giverandombook");
    }

    //ee giverandombook <player> <group>
    @Override
    protected ReturnType runCommand(EpicEnchants instance, CommandSender sender, String... args) {
        if (args.length < 3 || args.length > 6)
            return ReturnType.SYNTAX_ERROR;

        OfflinePlayer target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            instance.getLocale().newMessage("&cThis player does not exist...").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }


        List<Group> groups = instance.getGroupManager().getValues().stream()
                .filter(group -> group.getIdentifier().equalsIgnoreCase(args[2])).collect(Collectors.toList());

        if (groups.isEmpty()) {
            instance.getLocale().newMessage("&cThe group you entered was no found...").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        Group group = groups.get(0);

        target.getPlayer().getInventory().addItem(instance.getSpecialItems().getMysteryBook(group));
        instance.getLocale().getMessage("command.book.received")
                .sendPrefixedMessage(target.getPlayer());
        instance.getLocale().getMessage("command.book.gave")
                .processPlaceholder("player", target.getPlayer().getName())
                .sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicEnchants instance, CommandSender sender, String... args) {
        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        } else if (args.length == 3) {
            return instance.getGroupManager().getValues().stream()
                    .map(Group::getIdentifier).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.giverandombook";
    }

    @Override
    public String getSyntax() {
        return "/ee giverandombook <player> <group>";
    }

    @Override
    public String getDescription() {
        return "Give random enchant books to players.";
    }
}
