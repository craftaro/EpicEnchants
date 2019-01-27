package com.songoda.epicenchants.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.menus.EnchanterMenu;
import com.songoda.epicenchants.objects.Enchant;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

import static com.songoda.epicenchants.objects.Placeholder.of;

@CommandAlias("epicenchants|ee")
public class EnchantCommand extends BaseCommand {

    @Dependency("instance")
    private EpicEnchants instance;

    @Default
    @Subcommand("enchanter")
    @Description("Opens the Enchanter")
    public void onGui(Player player) {
        new EnchanterMenu(instance, instance.getFileManager().getConfiguration("menus/enchanter-menu"), player).open(player);
    }

    //ee give {player} {enchant} {group}
    @Subcommand("give")
    @CommandCompletion("@players @enchants @nothing @nothing @nothing")
    @Description("Give enchant books to players")
    @CommandPermission("epicenchants.give")
    public void onGiveBook(CommandSender sender, @Flags("other") Player target, Enchant enchant, @Optional Integer level, @Optional Integer successRate, @Optional Integer destroyRate) {
        target.getInventory().addItem(enchant.getBookItem().get(enchant, level, successRate, destroyRate));
        target.sendMessage(instance.getLocale().getMessageWithPrefix("command.book.given", of("enchant", enchant.getIdentifier())));
        sender.sendMessage(instance.getLocale().getMessageWithPrefix("command.book.gave", of("player", target.getName()), of("enchant", enchant.getIdentifier())));
    }

    //ee apply {enchant} {group}
    @Subcommand("apply")
    @CommandCompletion("@enchants @nothing")
    @Description("Apply enchant to item in hand")
    @CommandPermission("epicenchants.apply")
    public void onApply(Player player, Enchant enchant, int level, @Optional Integer successRate, @Optional Integer destroyRate) {
        int slot = player.getInventory().getHeldItemSlot();
        ItemStack before = player.getItemInHand();
        Pair<ItemStack, EnchantResult> result = instance.getEnchantUtils().apply(before, enchant, level,
                successRate == null ? 100 : successRate, destroyRate == null ? 0 : destroyRate);
        String messageKey = "";

        switch (result.getRight()) {
            case FAILURE:
                messageKey = "enchant.failure";
                break;
            case BROKEN_FAILURE:
                player.getInventory().clear(slot);
                messageKey = "enchant.brokenfailure";
                break;
            case SUCCESS:
                messageKey = "enchant.success";
                break;
            case CONFLICT:
                messageKey = "enchant.conflict";
        }

        player.sendMessage(instance.getLocale().getMessageWithPrefix(messageKey, of("enchant", enchant.getIdentifier())));
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
    @CommandAlias("load")
    @Description("Reload all config files, or reload/load specific enchant files")
    @CommandCompletion("@enchantFiles")
    public void onReload(CommandSender sender, @Optional File fileName) {
        if (fileName == null) {
            instance.reload();
            sender.sendMessage(instance.getLocale().getMessageWithPrefix("command.reload"));
            return;
        }

        try {
            instance.getEnchantManager().loadEnchant(fileName);
        } catch (Exception e) {
            sender.sendMessage(instance.getLocale().getMessageWithPrefix("command.filereload.failed", of("file-name", fileName.getName())));
            Bukkit.getConsoleSender().sendMessage("Something went wrong loading the enchant from file " + fileName.getName());
            Bukkit.getConsoleSender().sendMessage("Please check to make sure there are no errors in the file.");
            e.printStackTrace();
            return;
        }
        sender.sendMessage(instance.getLocale().getMessageWithPrefix("command.filereload.success", of("file-name", fileName.getName())));

    }
}
