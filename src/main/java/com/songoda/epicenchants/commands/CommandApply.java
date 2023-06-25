package com.songoda.epicenchants.commands;

import com.craftaro.core.commands.AbstractCommand;
import com.craftaro.core.compatibility.CompatibleMaterial;
import com.songoda.epicenchants.CommandCommons;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.Tuple;
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

    private final EpicEnchants plugin;

    public CommandApply(EpicEnchants plugin) {
        super(true, "apply");
        this.plugin = plugin;
    }

    //apply [enchant] [level] <success-rate> <destroy-rate>
    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length < 2 || args.length > 4)
            return ReturnType.SYNTAX_ERROR;

        Optional<Enchant> optionalEnchant = plugin.getEnchantManager().getValue(args[0].replaceAll("_", " "));

        if (!optionalEnchant.isPresent()) {
            plugin.getLocale().newMessage("&cNo enchants exist with that name...").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (!CommandCommons.isInt(args[1], sender))
            return ReturnType.FAILURE;

        int successRate = 100;
        int destroyRate = 0;

        if (args.length > 2) {
            if (!CommandCommons.isInt(args[2], sender))
                return ReturnType.FAILURE;
            successRate = Integer.parseInt(args[2]);
        }

        if (args.length > 3) {
            if (!CommandCommons.isInt(args[3], sender))
                return ReturnType.FAILURE;
            destroyRate = Integer.parseInt(args[3]);
        }
        Enchant enchant = optionalEnchant.get();
        int level = Integer.parseInt(args[1]);
        Player player = (Player) sender;

        if (!enchant.getItemWhitelist().contains(CompatibleMaterial.getMaterial(player.getItemInHand().getType()).get())) {
            System.out.println("List = " + enchant.getItemWhitelist());
            plugin.getLocale().getMessage("command.apply.invaliditem")
                    .processPlaceholder("enchant", enchant.getIdentifier())
                    .sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        int slot = player.getInventory().getHeldItemSlot();
        ItemStack before = player.getItemInHand();
        Tuple<ItemStack, EnchantResult> result = plugin.getEnchantUtils().apply(before, enchant, level,
                successRate, destroyRate);

        plugin.getLocale().getMessage(getMessageFromResult(result.getRight()))
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
    protected List<String> onTab(CommandSender sender, String... args) {
        if (args.length == 1) {
            return plugin.getEnchantManager().getValues()
                    .stream().map(Enchant::getIdentifier).collect(Collectors.toList());
        } else if (args.length == 2) {
            Enchant enchant = plugin.getEnchantManager().getValues()
                    .stream().findFirst().orElse(null);
            List<String> levels = new ArrayList<>();
            if (enchant != null) {
                for (int i = 1; i <= enchant.getMaxLevel(); i++)
                    levels.add(String.valueOf(i));
            }
            return levels;
        } else if (args.length == 3 || args.length == 4) {
            List<String> rates = new ArrayList<>();
            for (int i = 1; i <= 100; i++)
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
        return "apply <enchant> <level> [success-rate] [destroy-rate]";
    }

    @Override
    public String getDescription() {
        return "Apply an enchant to the item in hand.";
    }
}
