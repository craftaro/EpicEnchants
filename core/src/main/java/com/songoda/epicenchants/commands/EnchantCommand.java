package com.songoda.epicenchants.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.menus.EnchanterMenu;
import com.songoda.epicenchants.menus.TinkererMenu;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.objects.Group;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.songoda.epicenchants.enums.EnchantResult.BROKEN_FAILURE;
import static com.songoda.epicenchants.objects.Placeholder.of;
import static com.songoda.epicenchants.utils.single.GeneralUtils.getMessageFromResult;

@CommandAlias("epicenchants|ee")
public class EnchantCommand extends BaseCommand {

    @Dependency("instance")
    private EpicEnchants instance;

    @Subcommand("%enchanter")
    @Description("Opens the Enchanter")
    @CommandPermission("epicenchants.enchanter")
    public void onEnchanter(Player player) {
        new EnchanterMenu(instance, instance.getFileManager().getConfiguration("menus/enchanter-menu"), player).open(player);
    }

    @Subcommand("%tinkerer")
    @Description("Opens the Tinkerer")
    @CommandPermission("epicenchants.tinkerer")
    public void onTinkerer(Player player) {
        new TinkererMenu(instance, instance.getFileManager().getConfiguration("menus/tinkerer-menu")).open(player);
    }

    //ee give book [player] [enchant] <level> <success-rate> <destroy-rate>
    @Subcommand("give book")
    @CommandCompletion("@players @enchants @levels @increment @increment")
    @Description("Give enchant books to players")
    @CommandPermission("epicenchants.give.book")
    public void onGiveBook(CommandSender sender, @Flags("other") Player target, Enchant enchant, @Optional Integer level, @Optional Integer successRate, @Optional Integer destroyRate) {
        if (level != null && level > enchant.getMaxLevel()) {
            instance.getAction().perform(sender, "command.book.max-level",
                    of("enchant", enchant.getIdentifier()),
                    of("max-level", enchant.getMaxLevel()));
            return;
        }

        target.getInventory().addItem(enchant.getBook().get(enchant, level, successRate, destroyRate));
        instance.getAction().perform(target, "command.book.received", of("enchant", enchant.getIdentifier()));
        instance.getAction().perform(sender, "command.book.gave", of("player", target.getName()), of("enchant", enchant.getIdentifier()));
    }

    //ee give item dust [player] [group] <type> <percentage>
    @Subcommand("give item dust")
    @CommandCompletion("@players @groups @dustTypes @nothing")
    @CommandPermission("epicenchants.give.item.dust")
    public void onGiveDust(CommandSender sender, @Flags("other") Player target, Group group, @Optional String dustType, @Optional Integer percentage) {
        target.getInventory().addItem(instance.getSpecialItems().getDust(group, dustType, percentage));
        instance.getAction().perform(target, "command.dust.received", of("group", group.getIdentifier()));
        instance.getAction().perform(sender, "command.dust.gave", of("player", target.getName()), of("group", group.getIdentifier()));
    }

    //ee give item [giveType] [player] <amount> <success-rate>
    @Subcommand("give item")
    @CommandCompletion("@players @giveType @nothing @nothing")
    @Description("Give enchant books to players")
    @CommandPermission("epicenchants.give.item")
    public void onGiveItem(CommandSender sender, @Flags("other") Player target, String giveType, @Optional Integer amount, @Optional Integer successRate) {
        String messageKey;
        switch (giveType.toLowerCase()) {
            case "whitescroll":
                target.getInventory().addItem(instance.getSpecialItems().getWhiteScroll(amount));
                messageKey = "whitescroll";
                break;
            case "blackscroll":
                messageKey = "blackscroll";
                target.getInventory().addItem(instance.getSpecialItems().getBlackScroll(amount, successRate));
                break;
            default:
                return;
        }

        instance.getAction().perform(target, "command." + messageKey + ".received");
        instance.getAction().perform(sender, "command." + messageKey + ".gave", of("player", target.getName()));
    }


    //ee apply [enchant] [level] <success-rate> <destroy-rate>
    @Subcommand("apply")
    @CommandCompletion("@enchants @nothing")
    @Description("Apply enchant to item in hand")
    @CommandPermission("epicenchants.apply")
    public void onApply(Player player, Enchant enchant, int level, @Optional Integer successRate, @Optional Integer destroyRate) {
        if (player.getItemInHand() == null) {
            instance.getAction().perform(player, "command.apply.noitem", of("enchant", enchant.getIdentifier()));
            return;
        }

        int slot = player.getInventory().getHeldItemSlot();
        ItemStack before = player.getItemInHand();
        Pair<ItemStack, EnchantResult> result = instance.getEnchantUtils().apply(before, enchant, level,
                successRate == null ? 100 : successRate, destroyRate == null ? 0 : destroyRate);

        instance.getAction().perform(player, getMessageFromResult(result.getRight()), of("enchant", enchant.getIdentifier()));

        if (result.getRight() == BROKEN_FAILURE) {
            player.getInventory().clear(slot);
            return;
        }

        player.getInventory().setItem(slot, result.getLeft());
    }

    //ee list
    @Subcommand("info")
    @CommandPermission("epicenchants.info")
    @Description("List all enchants with their description")
    public void onList(Player player) {
        instance.getInfoManager().getMainInfoMenu().open(player);
    }

    //ee reload [enchantFileName]
    @Subcommand("reload")
    @Description("Reload all config files.")
    @CommandCompletion("@enchantFiles")
    @CommandPermission("epicenchants.reload")
    public void onReload(CommandSender sender) {
        instance.reload();
        instance.getAction().perform(sender, "command.reload");
    }

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
