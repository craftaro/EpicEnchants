package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.CommandCommons;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import com.songoda.epicenchants.objects.Enchant;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandGiveScroll extends AbstractCommand {

    public CommandGiveScroll(AbstractCommand parent) {
        super(parent, false, "givescroll");
    }

    //ee givescroll <giveType> <player> [amount] [success-rate]
    @Override
    protected ReturnType runCommand(EpicEnchants instance, CommandSender sender, String... args) {
        if (args.length < 3 || args.length > 6)
            return ReturnType.SYNTAX_ERROR;

        String giveType = args[1];
        OfflinePlayer target = Bukkit.getPlayer(args[2]);

        if (target == null) {
            instance.getLocale().newMessage("&cThis player does not exist...").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        int amount = 1;
        int successRate = -1;


        if (args.length > 3) {
            if (!CommandCommons.isInt(args[3], sender))
                return ReturnType.FAILURE;
            amount =  Integer.parseInt(args[3]);
        }

        if (args.length > 4) {
            if (!CommandCommons.isInt(args[4], sender))
                return ReturnType.FAILURE;
            successRate = Integer.parseInt(args[4]);
        }

        String messageKey;
        switch (giveType.toLowerCase()) {
            case "whitescroll":
                target.getPlayer().getInventory().addItem(instance.getSpecialItems().getWhiteScroll(amount));
                messageKey = "whitescroll";
                break;
            case "blackscroll":
                messageKey = "blackscroll";
                target.getPlayer().getInventory().addItem(instance.getSpecialItems().getBlackScroll(amount, successRate));
                break;
            default:
                instance.getLocale().getMessage("command.giveunknown")
                        .processPlaceholder("unknown", giveType)
                        .sendPrefixedMessage(sender);
                return ReturnType.FAILURE;
        }

        instance.getLocale().getMessage("command." + messageKey + ".received")
                .sendPrefixedMessage(target.getPlayer());
        instance.getLocale().getMessage("command." + messageKey + ".gave")
                .processPlaceholder("player", target.getName())
                .sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicEnchants instance, CommandSender sender, String... args) {
        if (args.length == 2) {
            return Arrays.asList("whitescroll", "blackscroll");
        } else if (args.length == 3) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        } else if (args.length == 4 || args.length == 5) {
            List<String> rates = new ArrayList<>();
            for (int i = 1; i <= (args.length == 4 ? 10 : 100); i ++)
                rates.add(String.valueOf(i));
            return rates;
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.givescroll";
    }

    @Override
    public String getSyntax() {
        return "/ee givescroll <whitescroll/blackscroll> <player> [amount] [success-rate]";
    }

    @Override
    public String getDescription() {
        return "Give enchant scrolls to players.";
    }
}
