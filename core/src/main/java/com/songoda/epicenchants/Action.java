package com.songoda.epicenchants;

import com.songoda.epicenchants.objects.Placeholder;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.single.GeneralUtils.color;

public class Action {
    private FileConfiguration config;

    public void perform(CommandSender sender, String node, Placeholder... placeholders) {
        if (config.isString(node)) {
            getMessage(node, placeholders).forEach(sender::sendMessage);
            return;
        }

        if (!config.isConfigurationSection(node)) {
            return;
        }

        ConfigurationSection section = config.getConfigurationSection(node);

        if (section.isString("message") || section.isList("message")) {
            getMessage(node + ".message", placeholders).forEach(sender::sendMessage);
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

    public List<String> getMessage(String node, Placeholder... placeholders) {
        List<String> output = config.isList(node) ? config.getStringList(node) : config.getString(node).isEmpty() ? Collections.emptyList() : Collections.singletonList(config.getString(node));

        return output.stream().map(s -> {
            for (Placeholder placeholder : placeholders) {
                s = s.replace(placeholder.getPlaceholder(), placeholder.getToReplace().toString());
            }

            return color(s);
        }).collect(Collectors.toList());
    }

    public void load(FileConfiguration config) {
        this.config = config;
    }
}
