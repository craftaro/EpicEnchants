package com.songoda.epicenchants.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.objects.Enchant;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("epicenchants|ee")
public class EnchantCommand extends BaseCommand {

    @Dependency("instance")
    private EpicEnchants instance;

    @Default
    @Subcommand("gui")
    @Description("Opens the GUI for getting enchants")
    public void onGui(Player player) {
        instance.getBookInventory().open(player);
    }

    //ee give {player} {enchant} {tier}
    @Subcommand("give")
    @CommandCompletion("@players @enchants @nothing @nothing @nothing")
    @Description("Give enchant books to players")
    @CommandPermission("epicenchants.give")
    public void onGiveBook(CommandSender sender, @Flags("other") Player target, Enchant enchant, @Optional Integer level, @Optional Integer successRate, @Optional Integer destroyRate) {
        target.getInventory().addItem(enchant.getBookItem().get(enchant, level, successRate, destroyRate));
        target.sendMessage(instance.getLocale().getMessageWithPrefix("command.given", enchant.getIdentifier()));
        sender.sendMessage(instance.getLocale().getMessageWithPrefix("command.gave", target.getName(), enchant.getIdentifier()));
    }

    //ee apply {enchant} {tier}
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

        player.sendMessage(instance.getLocale().getMessageWithPrefix(messageKey, enchant.getIdentifier()));
        player.getInventory().setItem(slot, result.getLeft());
    }
}

/*ABSORPTION
BLINDNESS
CONFUSION
DAMAGE_RESISTANCE
FAST_DIGGING
FIRE_RESISTANCE
HARM
HEAL
HEALTH_BOOST
HUNGER
INCREASE_DAMAGE
INVISIBILITY
JUMP
NIGHT_VISION
POISON
REGENERATION
SATURATION
SLOW
SLOW_DIGGING
SPEED
WATER_BREATHING
WEAKNESS
WITHER*/

/*
ABSORPTION
BLINDNESS
CONFUSION
DAMAGE_RESISTANCE
FAST_DIGGING
FIRE_RESISTANCE
GLOWING
HARM
HEAL
HEALTH_BOOST
HUNGER
INCREASE_DAMAGE
INVISIBILITY
JUMP
LEVITATION
LUCK
NIGHT_VISION
POISON
REGENERATION
SATURATION
SLOW
SLOW_DIGGING
SPEED
UNLUCK
WATER_BREATHING
WEAKNESS
WITHER
 */
