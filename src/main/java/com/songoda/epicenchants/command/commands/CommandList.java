package com.songoda.epicenchants.command.commands;

import com.songoda.epicenchants.CommandCommons;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.command.AbstractCommand;
import com.songoda.epicenchants.enums.EnchantResult;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.utils.Tuple;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

import static com.songoda.epicenchants.enums.EnchantResult.BROKEN_FAILURE;
import static com.songoda.epicenchants.utils.single.GeneralUtils.getMessageFromResult;

public class CommandList extends AbstractCommand {

    public CommandList(AbstractCommand parent) {
        super(parent, true, "list");
    }

    @Override
    protected ReturnType runCommand(EpicEnchants instance, CommandSender sender, String... args) {
        instance.getInfoManager().getMainInfoMenu().open((Player)sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(EpicEnchants instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicenchants.list";
    }

    @Override
    public String getSyntax() {
        return "/ee list";
    }

    @Override
    public String getDescription() {
        return "List all enchants with their description.";
    }
}
