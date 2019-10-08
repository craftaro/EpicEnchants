package com.songoda.epicenchants;

import com.songoda.epicenchants.utils.Methods;
import org.bukkit.command.CommandSender;

public class CommandCommons {

    public static boolean isInt(String number, CommandSender sender) {
        if (!Methods.isInt(number)) {
            EpicEnchants.getInstance().getLocale().newMessage("Not a number.").sendPrefixedMessage(sender);
            return false;
        }
        return true;
    }
}
