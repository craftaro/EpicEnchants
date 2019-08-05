package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.CommandCommons;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.enums.EnchantResult.BROKEN_FAILURE;
import static com.songoda.epicenchants.utils.single.GeneralUtils.getMessageFromResult;

public class CommandApply extends AbstractCommand {

    public CommandApply(AbstractCommand parent) {
        super(parent, true, "apply");
    }

    //ee apply [enchant] [level] <success-rate> <destroy-rate>
    @Override
    protected ReturnType runCommand(EpicEnchants instance, CommandSender sender, String... args) {
        if (args.length < 3 || args.length > 5)
            return ReturnType.SYNTAX_ERROR;

        Optional<Enchant> optionalEnchant = instance.getEnchantManager().getValue(args[1].replaceAll("_", " "));

        if (!optionalEnchant.isPresent()) {
            instance.getLocale().newMessage("&cNo enchants exist with that name...").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (!CommandCommons.isInt(args[2], sender))
            return ReturnType.FAILURE;

        int successRate = 100;
        int destroyRate = 0;

        if (args.length > 3) {
            if (!CommandCommons.isInt(args[3], sender))
                return ReturnType.FAILURE;
            successRate = Integer.parseInt(args[3]);
        }

        if (args.length > 4) {
            if (!CommandCommons.isInt(args[4], sender))
                return ReturnType.FAILURE;
            destroyRate = Integer.parseInt(args[4]);
        }
        Enchant enchant = optionalEnchant.get();
        int level = Integer.parseInt(args[2]);
        Player player = (Player) sender;

        if (!enchant.getItemWhitelist().contains(player.getItemInHand().getType())) {
            System.out.println("List = " + enchant.getItemWhitelist());
            instance.getLocale().getMessage("command.apply.invaliditem")
                    .processPlaceholder("enchant", enchant.getIdentifier())
                    .sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        int slot = player.getInventory().getHeldItemSlot();
        ItemStack before = player.getItemInHand();
        Tuple<ItemStack, EnchantResult> result = instance.getEnchantUtils().apply(before, enchant, level,
                successRate, destroyRate);

        instance.getLocale().getMessage(getMessageFromResult(result.getRight()))
                .processPlaceholder("enchant", enchant.getIdentifier())
                .sendPrefixedMessage(player);

        if (result.getRight() == BROKEN_FAILURE) {
            player.getInventory().clear(slot);
            return ReturnType.FAILURE;
        }

        player.getInventory().setItem(slot, result.getLeft());
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicEnchants instance, CommandSender sender, String... args) {
        if (args.length == 2) {
            return instance.getEnchantManager().getValues()
                    .stream().map(Enchant::getIdentifier).collect(Collectors.toList());
        } else if (args.length == 3) {
            Enchant enchant = instance.getEnchantManager().getValues()
                    .stream().findFirst().orElse(null);
            List<String> levels = new ArrayList<>();
            if (enchant != null) {
                for (int i = 1; i <= enchant.getMaxLevel(); i ++)
                    levels.add(String.valueOf(i));
            }
            return levels;
        } else if (args.length == 4 || args.length == 5) {
            List<String> rates = new ArrayList<>();
            for (int i = 1; i <= 100; i ++)
                rates.add(String.valueOf(i));
            return rates;
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.apply";
    }

    @Override
    public String getSyntax() {
        return "/ee apply <enchant> <level> [success-rate] [destroy-rate]";
    }

    @Override
    public String getDescription() {
        return "Apply an enchant to the item in hand.";
    }
}
