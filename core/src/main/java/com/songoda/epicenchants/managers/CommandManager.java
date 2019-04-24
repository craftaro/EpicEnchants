package com.songoda.epicenchants.managers;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.InvalidCommandArgument;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.commands.CustomCommand;
import com.songoda.epicenchants.commands.EnchantCommand;
import com.songoda.epicenchants.enums.GiveType;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.objects.Group;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommandManager extends BukkitCommandManager {
    public CommandManager(EpicEnchants instance) {
        super(instance);

        // DEPENDENCIES

        registerDependency(EpicEnchants.class, "instance", instance);

        // COMPLETIONS

        getCommandCompletions().registerCompletion("enchants", c ->
                instance.getEnchantManager().getKeys().stream().map(s -> s.replaceAll("\\s", "_")).collect(Collectors.toList()));

        getCommandCompletions().registerCompletion("giveType", c ->
                Arrays.stream(GiveType.values()).map(s -> s.toString().replace("_", "-").toLowerCase()).collect(Collectors.toList()));

        getCommandCompletions().registerCompletion("levels", c ->
                IntStream.rangeClosed(1, c.getContextValue(Enchant.class).getMaxLevel()).boxed().map(Objects::toString).collect(Collectors.toList()));

        getCommandCompletions().registerCompletion("increment", c ->
                IntStream.rangeClosed(0, 100).filter(i -> i % 10 == 0).boxed().map(Objects::toString).collect(Collectors.toList()));

        getCommandCompletions().registerCompletion("groups", c ->
                instance.getGroupManager().getValues().stream().map(Group::getIdentifier).collect(Collectors.toList()));

        getCommandCompletions().registerCompletion("dustTypes", c ->
                instance.getFileManager().getConfiguration("items/dusts").getConfigurationSection("dusts").getKeys(false));

        // CONTEXTS

        getCommandContexts().registerContext(Enchant.class, c ->
                instance.getEnchantManager().getValue(c.popFirstArg().replaceAll("_", " ")).orElseThrow(() ->
                        new InvalidCommandArgument("No enchant exists by that name", false)));

        getCommandContexts().registerContext(GiveType.class, c -> Arrays.stream(GiveType.values())
                .filter(s -> s.toString().toLowerCase().replace("_", "-").equalsIgnoreCase(c.popFirstArg()))
                .findFirst()
                .orElseThrow(() -> new InvalidCommandArgument("No item by that type.", false)));

        getCommandContexts().registerContext(Group.class, c -> instance.getGroupManager().getValue(c.popFirstArg().toUpperCase()).orElseThrow(() ->
                new InvalidCommandArgument("No group exists by that name", false)));

        // REPLACEMENTS

        getCommandReplacements().addReplacements(
                "enchanter", instance.getFileManager().getConfiguration("config").getString("commands.enchanter"),
                "alchemist", instance.getFileManager().getConfiguration("config").getString("commands.alchemist"),
                "tinkerer", instance.getFileManager().getConfiguration("config").getString("commands.tinkerer")
        );

        // API

        enableUnstableAPI("help");

        // COMMANDS

        registerCommand(new EnchantCommand());
        registerCommand(new CustomCommand());
    }
}
