package com.songoda.epicenchants;

import com.songoda.epicenchants.objects.Placeholder;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static com.songoda.epicenchants.utils.single.GeneralUtils.color;

public class Action {
    private FileConfiguration config;
    private String prefix;

    public void perform(CommandSender sender, String node, Placeholder... placeholders) {
        if (config.isString(node)) {
            sender.sendMessage(getMessage(node, placeholders));
            return;
        }

        if (!config.isConfigurationSection(node)) {
            return;
        }

        ConfigurationSection section = config.getConfigurationSection(node);

        if (section.isString("message")) {
            sender.sendMessage(getMessage(node + ".message", placeholders));
        }

        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;

        if (section.isString("sound")) {
            player.playSound(player.getLocation(), Sound.valueOf(section.getString("sound").toUpperCase()), 1F, 1F);
        }

        if (section.isString("effect")) {
            player.playEffect(player.getEyeLocation(), Effect.valueOf(section.getString("effect")), 10);
        }

    }

    public String getMessage(String node, Placeholder... placeholders) {
        String string = config.getString(node);

        for (Placeholder placeholder : placeholders) {
            string = string.replace(placeholder.getPlaceholder(), placeholder.getToReplace().toString());
        }

        return color(string);
    }

    public void load(FileConfiguration config) {
        this.config = config;
        this.prefix = config.getString("general.prefix");
    }
}
