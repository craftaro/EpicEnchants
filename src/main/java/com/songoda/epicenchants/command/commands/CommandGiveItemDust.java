package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.CommandCommons;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.objects.Group;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandGiveItemDust extends AbstractCommand {

    public CommandGiveItemDust(AbstractCommand parent) {
        super(parent, false, "giveitemdust");
    }

    //ee giveitemdust <player> <group> [type] [percentage]
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

        String dustType = null;
        int percentage = -1;

        if (args.length > 3) {
            dustType = args[3];
        }

        if (args.length > 4) {
            if (!CommandCommons.isInt(args[4], sender))
                return ReturnType.FAILURE;
            percentage = Integer.parseInt(args[4]);
        }

        target.getPlayer().getInventory().addItem(instance.getSpecialItems().getDust(group, dustType, percentage, true));
        instance.getLocale().getMessage("command.dust.received")
                .processPlaceholder("group", group.getIdentifier())
                .sendPrefixedMessage(target.getPlayer());
        instance.getLocale().getMessage("command.dust.gave")
                .processPlaceholder("player", target.getPlayer().getName())
                .processPlaceholder("group", group.getIdentifier())
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
        } else if (args.length == 4) {
            List<String> dusts = new ArrayList<>();

            FileConfiguration dustConfig = instance.getFileManager().getConfiguration("items/dusts");
            dusts.addAll(dustConfig.getConfigurationSection("dusts").getKeys(false));
            return dusts;
        } else if (args.length == 5) {
            List<String> rates = new ArrayList<>();
            for (int i = 1; i <= 100; i ++)
                rates.add(String.valueOf(i));
            return rates;
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.giveitemdust";
    }

    @Override
    public String getSyntax() {
        return "/ee giveitemdust <player> <group> [type] [percentage]";
    }

    @Override
    public String getDescription() {
        return "Give item dust.";
    }
}
