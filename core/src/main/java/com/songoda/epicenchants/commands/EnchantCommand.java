package com.songoda.epicenchants.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import org.bukkit.entity.Player;

@CommandAlias("epicenchants|ee")
public class EnchantCommand extends BaseCommand {

    @Dependency("instance")
    private EpicEnchants instance;

    //ee give {player} {enchant} {tier}
    @Subcommand("give")
    @CommandCompletion("@players @enchants @nothing @nothing @nothing")
    @Description("Give books to players")
    @CommandPermission("epicenchants.givebook")
    public void onGiveBook(@Flags("other") Player target, Enchant enchant, @Default("1") int tier, @Optional Double successRate, @Optional Double destroyRate) {
        target.getInventory().addItem(enchant.getBookItem().get(enchant, tier, successRate, destroyRate));
    }

    @Default
    @Subcommand("gui")
    @Description("Opens the GUI for getting enchants")
    public void onGui(Player player) {
        instance.getBookInventory().open(player);
    }
}
