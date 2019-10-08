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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandGiveBook extends AbstractCommand {

    public CommandGiveBook(AbstractCommand parent) {
        super(parent, false, "givebook");
    }

    //ee givebook <player> <enchant> [level] [success-rate] [destroy-rate]
    @Override
    protected ReturnType runCommand(EpicEnchants instance, CommandSender sender, String... args) {
        if (args.length < 3 || args.length > 6)
            return ReturnType.SYNTAX_ERROR;

        OfflinePlayer target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            instance.getLocale().newMessage("&cThis player does not exist...").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        Optional<Enchant> optionalEnchant = instance.getEnchantManager().getValue(args[2].replaceAll("_", " "));

        if (!optionalEnchant.isPresent()) {
            instance.getLocale().newMessage("&cNo enchants exist with that name...").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        Enchant enchant = optionalEnchant.get();
        int level = -1;
        int successRate = -1;
        int destroyRate = -1;

        if (args.length > 3) {
            if (!CommandCommons.isInt(args[3], sender))
                return ReturnType.FAILURE;
            level = Integer.parseInt(args[3]);
        }

        if (args.length > 4) {
            if (!CommandCommons.isInt(args[4], sender))
                return ReturnType.FAILURE;
            successRate = Integer.parseInt(args[4]);
        }

        if (args.length > 5) {
            if (!CommandCommons.isInt(args[5], sender))
                return ReturnType.FAILURE;
            destroyRate = Integer.parseInt(args[5]);
        }

        if (level != -1 && (level > enchant.getMaxLevel() || level < 1)) {
            instance.getLocale().getMessage("command.book." + (level > enchant.getMaxLevel() ? "maxlevel" : "minlevel"))
                    .processPlaceholder("enchant", enchant.getIdentifier())
                    .processPlaceholder("max_level", enchant.getMaxLevel())
                    .sendPrefixedMessage(sender);
            return ReturnType.SUCCESS;
        }

        target.getPlayer().getInventory().addItem(enchant.getBook().get(enchant, level, successRate, destroyRate));
        instance.getLocale().getMessage("command.book.received")
                .processPlaceholder("enchant", enchant.getIdentifier())
                .sendPrefixedMessage(target.getPlayer());
        instance.getLocale().getMessage("command.book.gave")
                .processPlaceholder("player", target.getPlayer().getName())
                .processPlaceholder("enchant", enchant.getIdentifier())
                .sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicEnchants instance, CommandSender sender, String... args) {
        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        } else if (args.length == 3) {
            return instance.getEnchantManager().getValues()
                    .stream().map(Enchant::getIdentifier).collect(Collectors.toList());
        } else if (args.length == 4) {
            Enchant enchant = instance.getEnchantManager().getValues()
                    .stream().findFirst().orElse(null);
                List<String> levels = new ArrayList<>();
            if (enchant != null) {
                for (int i = 1; i <= enchant.getMaxLevel(); i ++)
                    levels.add(String.valueOf(i));
            }
            return levels;
        } else if (args.length == 5 || args.length == 6) {
            List<String> rates = new ArrayList<>();
                for (int i = 1; i <= 100; i ++)
                    rates.add(String.valueOf(i));
            return rates;
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.givebook";
    }

    @Override
    public String getSyntax() {
        return "/ee givebook <player> <enchant> [level] [success-rate] [destroy-rate]";
    }

    @Override
    public String getDescription() {
        return "Give enchant books to players.";
    }
}
