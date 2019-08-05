package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.CommandCommons;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

import static com.songoda.epicenchants.enums.EnchantResult.BROKEN_FAILURE;
import static com.songoda.epicenchants.utils.single.GeneralUtils.getMessageFromResult;

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
            level =  Integer.parseInt(args[3]);
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
                    .processPlaceholder("maxlevel", enchant.getMaxLevel())
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
